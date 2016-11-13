/*
 * Copyright (C) 2016 H.C. Wijbenga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hilcode.versionator;

import static com.github.hilcode.versionator.VersionSource.POM;
import java.util.Map;
import java.util.Set;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class Transformers
{
	public static final Dependency transformDependency(final Dependency dependency, final Gav newGav)
	{
		Preconditions.checkNotNull(dependency, "Missing 'dependency'.");
		Preconditions.checkNotNull(newGav, "Missing 'newGav'.");
		System.err.println("Dependency version = " + dependency.gav.version + " / '" + dependency.gav.version.toText() + "'");
		System.err.println("        " + dependency.gav.version.hasPropertyValue());
		return !dependency.gav.version.hasPropertyValue()
				? Dependency.BUILDER.build(transformGav(dependency.gav, newGav))
				: dependency;
	}

	public static final Property transformProperty(final Property property, final Property newProperty)
	{
		Preconditions.checkNotNull(property, "Missing 'property'.");
		Preconditions.checkNotNull(newProperty, "Missing 'newProperty'.");
		return property.key == newProperty.key
				? newProperty
				: property;
	}

	public static final Gav transformGav(final Gav gav, final Gav newGav)
	{
		Preconditions.checkNotNull(gav, "Missing 'gav'.");
		Preconditions.checkNotNull(newGav, "Missing 'newGav'.");
		return gav.groupArtifact == newGav.groupArtifact
				? Gav.BUILDER.build(gav.groupArtifact, newGav.version)
				: gav;
	}

	public static final Pom transformPomWithProperty(final Pom pom, final Property changedProperty)
	{
		final ImmutableList.Builder<Property> newPropertiesBuilder = ImmutableList.builder();
		for (final Property property : pom.properties)
		{
			final Property newProperty = transformProperty(property, changedProperty);
			newPropertiesBuilder.add(newProperty);
		}
		final ImmutableList<Property> newProperties = newPropertiesBuilder.build();
		return Pom.BUILDER.build(
				pom.gav,
				pom.groupIdSource,
				pom.versionSource,
				pom.file,
				pom.type,
				pom.parent,
				pom.modules,
				newProperties,
				pom.dependencies);
	}

	public static final Pom transformPomWithGav(final Pom pom, final Gav gav_)
	{
		final Pom newParent =
				pom.parent != Pom.NONE
						? Transformers.transformPomWithGav(pom.parent, gav_)
						: pom.parent;
		if (pom.parent != Pom.NONE && pom.parent != newParent)
		{
			System.err.println("PARENT: " + pom.parent.gav.toText() + " --> " + newParent.gav.toText());
		}
		final Gav newGav = transformGav(pom.gav, gav_);
		System.err.println("GAV: " + pom.gav.toText() + " --> " + newGav.toText());
		final ImmutableList.Builder<Dependency> newDependenciesBuilder = ImmutableList.builder();
		for (final Dependency dependency : pom.dependencies)
		{
			final Dependency newDependency = transformDependency(dependency, gav_);
			newDependenciesBuilder.add(newDependency);
		}
		final ImmutableList<Dependency> newDependencies = newDependenciesBuilder.build();
		return Pom.BUILDER.build(
				newGav,
				pom.groupIdSource,
				pom.versionSource,
				pom.file,
				pom.type,
				newParent,
				pom.modules,
				pom.properties,
				newDependencies);
	}

	public static final ImmutableCollection<Gav> includeImpliedGavs(final Model model, final ImmutableCollection<Gav> gavs)
	{
		final Map<GroupArtifact, Pom> groupArtifact2Pom = Maps.newConcurrentMap();
		for (final Pom pom : model.poms)
		{
			groupArtifact2Pom.put(pom.gav.groupArtifact, pom);
		}
		final Map<GroupArtifact, Version> groupArtifact2Version = Maps.newConcurrentMap();
		for (final Gav gav : gavs)
		{
			if (groupArtifact2Version.containsKey(gav.groupArtifact))
			{
				throw new IllegalStateException("Duplicate entry.");
			}
			groupArtifact2Version.put(gav.groupArtifact, gav.version);
		}
		final Set<Gav> gavs_ = Sets.newConcurrentHashSet(gavs);
		while (true)
		{
			boolean addedMoreGavs = false;
			final Map<Pom, Set<Pom>> map = Maps.newConcurrentMap();
			for (final Pom pom : model.poms)
			{
				if (pom.versionSource == POM)
				{
					if (!map.containsKey(pom))
					{
						map.put(pom, Sets.newConcurrentHashSet());
					}
					map.get(pom).add(pom);
				}
				else
				{
					final Pom pom_ = Poms.findRoot(pom);
					if (!map.containsKey(pom_))
					{
						map.put(pom_, Sets.newConcurrentHashSet());
					}
					map.get(pom_).add(pom);
				}
			}
			for (final Gav gav : gavs_)
			{
				if (groupArtifact2Pom.containsKey(gav.groupArtifact))
				{
					final Pom pom = Poms.findRoot(groupArtifact2Pom.get(gav.groupArtifact));
					for (final Pom pom_ : map.get(pom))
					{
						if (!groupArtifact2Version.containsKey(pom_.gav.groupArtifact))
						{
							addedMoreGavs = addedMoreGavs || gavs_.add(Gav.BUILDER.build(pom_.gav.groupArtifact, gav.version));
						}
						else if (groupArtifact2Version.get(pom_.gav.groupArtifact) != gav.version)
						{
							throw new IllegalStateException("Contradicting versions.");
						}
					}
				}
			}
			if (!addedMoreGavs)
			{
				break;
			}
		}
		return ImmutableSet.copyOf(gavs_);
	}

	public static final Model transformModelWithProperties(final Model model, final ImmutableCollection<Property> properties)
	{
		Preconditions.checkNotNull(properties, "Missing 'properties'.");
		Model result = model;
		for (final Property changedProperty : properties)
		{
			boolean pomChange = false;
			final ImmutableList.Builder<Pom> newPomsBuilder = ImmutableList.builder();
			for (final Pom pom : result.poms)
			{
				final Pom newPom = transformPomWithProperty(pom, changedProperty);
				pomChange = pomChange || newPom != pom;
				newPomsBuilder.add(newPom);
			}
			result = pomChange
					? Model.BUILDER.build(newPomsBuilder.build())
					: model;
		}
		return result;
	}

	public static final Model transformModelWithGavs(final Model model, final ImmutableCollection<Gav> gavs)
	{
		Preconditions.checkNotNull(gavs, "Missing 'gavs'.");
		if (gavs.isEmpty())
		{
			return model;
		}
		final ImmutableCollection<Gav> gavs_ = includeImpliedGavs(model, gavs);
		final ImmutableSet.Builder<Property> changedPropertiesBuilder = ImmutableSet.builder();
		for (final Gav gav : gavs_)
		{
			for (final Pom pom : model.poms)
			{
				for (final Dependency dependency : pom.dependencies)
				{
					if (dependency.gav.version.hasPropertyValue() && gav.groupArtifact == dependency.gav.groupArtifact)
					{
						final String versionAsText = dependency.gav.version.toText();
						changedPropertiesBuilder.add(
								Property.BUILDER.build(
										Key.BUILDER.build(versionAsText.substring(2, versionAsText.length() - 1)),
										gav.version.toText()));
					}
				}
			}
		}
		final ImmutableSet<Property> changedProperties = changedPropertiesBuilder.build();
		Model result = transformModelWithProperties(model, changedProperties);
		for (final Gav gav : gavs_)
		{
			System.err.println("GAV=" + gav.toText());
			boolean pomChange = false;
			System.err.println("pomChange = " + pomChange);
			final ImmutableList.Builder<Pom> newPomsBuilder = ImmutableList.builder();
			for (final Pom pom : result.poms)
			{
				final Pom newPom = transformPomWithGav(pom, gav);
				pomChange = pomChange || newPom != pom;
				System.err.println("pomChange = " + pomChange);
				newPomsBuilder.add(newPom);
			}
			System.err.println("pomChange = " + pomChange);
			result = pomChange
					? Model.BUILDER.build(newPomsBuilder.build())
					: model;
			System.err.println("this != result : " + (model != result));
		}
		System.err.println("XXX this != result : " + (model != result));
		final ImmutableSet.Builder<GroupArtifact> groupArtifactsBuilder = ImmutableSet.builder();
		for (final Gav gav : gavs_)
		{
			groupArtifactsBuilder.add(gav.groupArtifact);
		}
		final ImmutableSet<GroupArtifact> groupArtifacts = groupArtifactsBuilder.build();
		while (true)
		{
			System.err.println("this == result: " + (model == result));
			final ImmutableSet.Builder<Gav> collateralGavs = ImmutableSet.builder();
			for (final Tuple.Duo<Pom, Pom> tuple : Zipper.BUILDER.zip(model.poms, result.poms))
			{
				final Pom originalPom = tuple._1;
				final Pom resultPom = tuple._2;
				if (originalPom != resultPom)
				{
					System.err.println("POM " + originalPom.gav.toText() + " HAS CHANGED!");
					if (originalPom.gav != resultPom.gav && !groupArtifacts.contains(originalPom.gav.groupArtifact))
					{
						collateralGavs.add(resultPom.gav);
					}
					else if (originalPom.gav.version.isRelease())
					{
						final Pom pom = Poms.findRoot(originalPom);
						final Gav gav = Gav.BUILDER.build(pom.gav.groupArtifact, pom.gav.version.next().toSnapshot());
						if (!groupArtifacts.contains(gav.groupArtifact))
						{
							collateralGavs.add(gav);
						}
					}
				}
			}
			final Model result_ = transformModelWithGavs(result, collateralGavs.build());
			if (result_ == result)
			{
				break;
			}
			result = result_;
		}
		return result;
	}

	public static final Model releaseModel(final Model model, final ImmutableCollection<GroupArtifact> exclusions)
	{
		final ImmutableList.Builder<Gav> gavsBuilder = ImmutableList.builder();
		for (final Pom pom : model.poms)
		{
			if (pom.gav.version.isRelease() && !Poms.isReleasable(pom) && !exclusions.contains(pom.gav.groupArtifact))
			{
				gavsBuilder.add(Gav.BUILDER.build(pom.gav.groupArtifact, pom.gav.version.next().toSnapshot()));
			}
		}
		final ImmutableList<Gav> gavs = gavsBuilder.build();
		final Model result = transformModelWithGavs(model, gavs);
		final ImmutableList.Builder<Gav> gavsBuilder_ = ImmutableList.builder();
		for (final Pom pom : result.poms)
		{
			if (pom.gav.version.isSnapshot() && !exclusions.contains(pom.gav.groupArtifact))
			{
				gavsBuilder_.add(Gav.BUILDER.build(pom.gav.groupArtifact, pom.gav.version.toRelease()));
			}
			if (pom.parent != Pom.NONE && pom.parent.gav.version.isSnapshot() && !exclusions.contains(pom.parent.gav.groupArtifact))
			{
				gavsBuilder_.add(Gav.BUILDER.build(pom.parent.gav.groupArtifact, pom.parent.gav.version.toRelease()));
			}
			for (final Dependency dependency : pom.dependencies)
			{
				if (dependency.gav.version.isSnapshot() && !exclusions.contains(dependency.gav.groupArtifact))
				{
					gavsBuilder_.add(Gav.BUILDER.build(dependency.gav.groupArtifact, dependency.gav.version.toRelease()));
				}
			}
		}
		final ImmutableList<Gav> gavs_ = gavsBuilder_.build();
		final ImmutableList.Builder<Pom> pomsBuilder = ImmutableList.builder();
		for (final Pom pom : result.poms)
		{
			Pom pom_ = pom;
			for (final Gav gav_ : gavs_)
			{
				pom_ = transformPomWithGav(pom_, gav_);
			}
			pomsBuilder.add(pom_);
		}
		return Model.BUILDER.build(pomsBuilder.build());
	}
}
