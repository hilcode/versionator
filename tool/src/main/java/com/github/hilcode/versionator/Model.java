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
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class Model
{
	public final ImmutableList<Pom> poms;

	private Model(final ImmutableList<Pom> poms)
	{
		this.poms = poms;
	}

	public ImmutableCollection<Gav> createClosure(final ImmutableCollection<Gav> gavs)
	{
		final Map<GroupArtifact, Pom> groupArtifact2Pom = Maps.newConcurrentMap();
		for (final Pom pom : this.poms)
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
			for (final Pom pom : this.poms)
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
					final Pom pom_ = pom.findRoot();
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
					final Pom pom = groupArtifact2Pom.get(gav.groupArtifact).findRoot();
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

	public Model apply(final ImmutableCollection<Gav> gavs)
	{
		Preconditions.checkNotNull(gavs, "Missing 'gavs'.");
		if (gavs.isEmpty())
		{
			return this;
		}
		final ImmutableCollection<Gav> gavs_ = createClosure(gavs);
		Model result = this;
		for (final Gav gav : gavs_)
		{
			boolean pomChange = false;
			final ImmutableList.Builder<Pom> newPomsBuilder = ImmutableList.builder();
			for (final Pom pom : result.poms)
			{
				final Pom newPom = pom.apply(gav);
				pomChange = pomChange || newPom != pom;
				newPomsBuilder.add(newPom);
			}
			result = pomChange
					? BUILDER.build(newPomsBuilder.build())
					: this;
		}
		final ImmutableSet.Builder<GroupArtifact> groupArtifactsBuilder = ImmutableSet.builder();
		for (final Gav gav : gavs_)
		{
			groupArtifactsBuilder.add(gav.groupArtifact);
		}
		final ImmutableSet<GroupArtifact> groupArtifacts = groupArtifactsBuilder.build();
		while (true)
		{
			final ImmutableSet.Builder<Gav> collateralGavs = ImmutableSet.builder();
			for (final Tuple._2<Pom, Pom> tuple : Zipper.BUILDER.zip(this.poms, result.poms))
			{
				final Pom originalPom = tuple._1;
				final Pom resultPom = tuple._2;
				if (originalPom != resultPom)
				{
					if (originalPom.gav != resultPom.gav && !groupArtifacts.contains(originalPom.gav.groupArtifact))
					{
						collateralGavs.add(resultPom.gav);
					}
					else if (originalPom.gav.version.isRelease())
					{
						final Pom pom = originalPom.findRoot();
						final Gav gav = Gav.BUILDER.build(pom.gav.groupArtifact, pom.gav.version.next().toSnapshot());
						if (!groupArtifacts.contains(gav.groupArtifact))
						{
							collateralGavs.add(gav);
						}
					}
				}
			}
			final Model result_ = result.apply(collateralGavs.build());
			if (result_ == result)
			{
				break;
			}
			result = result_;
		}
		return result;
	}

	public Model release(final ImmutableCollection<GroupArtifact> exclusions)
	{
		final ImmutableList.Builder<Gav> gavsBuilder = ImmutableList.builder();
		for (final Pom pom : this.poms)
		{
			if (pom.gav.version.isRelease() && !pom.isReleasable() && !exclusions.contains(pom.gav.groupArtifact))
			{
				gavsBuilder.add(Gav.BUILDER.build(pom.gav.groupArtifact, pom.gav.version.next().toSnapshot()));
			}
		}
		final ImmutableList<Gav> gavs = gavsBuilder.build();
		final Model result = apply(gavs);
		final ImmutableList.Builder<Gav> gavsBuilder_ = ImmutableList.builder();
		for (final Pom pom : result.poms)
		{
			if (pom.gav.version.isSnapshot() && !exclusions.contains(pom.gav.groupArtifact))
			{
				gavsBuilder_.add(Gav.BUILDER.build(pom.gav.groupArtifact, pom.gav.version.toRelease()));
			}
			if (pom.parent.isPresent() && pom.parent.get().gav.version.isSnapshot() && !exclusions.contains(pom.parent.get().gav.groupArtifact))
			{
				gavsBuilder_.add(Gav.BUILDER.build(pom.parent.get().gav.groupArtifact, pom.parent.get().gav.version.toRelease()));
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
				pom_ = pom_.apply(gav_);
			}
			pomsBuilder.add(pom_);
		}
		return Model.BUILDER.build(pomsBuilder.build());
	}

	public interface Builder
	{
		Model build(ImmutableList<Pom> poms);
	}

	public static final Builder BUILDER = new Builder()
	{
		private final Interner<Model> interner = Interners.newWeakInterner();

		@Override
		public Model build(final ImmutableList<Pom> poms)
		{
			return this.interner.intern(new Model(poms));
		}
	};
}
