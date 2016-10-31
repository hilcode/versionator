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

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import com.github.hilcode.versionator.maven.Gav;
import com.github.hilcode.versionator.maven.GroupArtifact;
import com.github.hilcode.versionator.maven.Pom;
import com.github.hilcode.versionator.maven.PomFinder;
import com.github.hilcode.versionator.maven.PomParser;
import com.github.hilcode.versionator.maven.Property;
import com.github.hilcode.versionator.maven.Type;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class Main2
{
	public static final ImmutableList<Pom> findAllPoms(final File rootDir)
	{
		final PomFinder pomFinder = new PomFinder();
		final PomParser pomParser = new PomParser();
		final List<Pom> allPoms = Lists.newArrayList();
		for (final File pomFile : pomFinder.findPoms(rootDir))
		{
			final Document pomDocument = pomParser.toDocument(pomFile);
			final Optional<Gav> parentGav = pomParser.findParentGav(pomDocument);
			final Gav gav = pomParser.findGav(pomDocument);
			final Type type = pomParser.findType(pomDocument);
			final ImmutableList<Property> properties = pomParser.findProperties(pomDocument);
			final ImmutableList<Gav> dependencies = pomParser.findDependencies(pomDocument);
			final Pom pom = new Pom(gav,pomFile, type, parentGav, properties, dependencies);
			allPoms.add(pom);
		}
		Collections.sort(allPoms);
		return ImmutableList.copyOf(allPoms);
	}

	public static final ImmutableList<Gav> findAllGavs(final ImmutableList<Pom> poms)
	{
		final Set<Gav> allGavsSet = Sets.newConcurrentHashSet();
		for (final Pom pom : poms)
		{
			allGavsSet.add(pom.gav);
			if (pom.parentGav.isPresent())
			{
				allGavsSet.add(pom.parentGav.get());
			}
			for (final Gav dependency : pom.dependencies)
			{
				allGavsSet.add(dependency);
			}
		}
		final List<Gav> allGavs = Lists.newArrayList(allGavsSet);
		Collections.sort(allGavs);
		return ImmutableList.copyOf(allGavs);
	}

	public static final ImmutableList<GroupArtifact> findAllGroupArtifacts(final ImmutableList<Gav> allGavs)
	{
		final Set<GroupArtifact> allGroupArtifactsSet = Sets.newConcurrentHashSet();
		for (final Gav gav : allGavs)
		{
			allGroupArtifactsSet.add(gav.groupArtifact);
		}
		final List<GroupArtifact> allGroupArtifacts = Lists.newArrayList(allGroupArtifactsSet);
		Collections.sort(allGroupArtifacts);
		return ImmutableList.copyOf(allGroupArtifacts);
	}

	public static final ImmutableMap<GroupArtifact, ImmutableSet<Pom>> findPomsDirectlyAffectedByGroupArtifact(
			final ImmutableList<Pom> poms,
			final ImmutableList<GroupArtifact> allGroupArtifacts)
	{
		final Map<GroupArtifact, ImmutableSet.Builder<Pom>> pomsDirectlyAffectedByGroupArtifact =
				Maps.newConcurrentMap();
		for (final GroupArtifact groupArtifact : allGroupArtifacts)
		{
			pomsDirectlyAffectedByGroupArtifact.put(groupArtifact, ImmutableSet.<Pom> builder());
		}
		for (final Pom pom : poms)
		{
			pomsDirectlyAffectedByGroupArtifact.get(pom.gav.groupArtifact).add(pom);
			if (pom.parentGav.isPresent())
			{
				pomsDirectlyAffectedByGroupArtifact.get(pom.parentGav.get().groupArtifact).add(pom);
			}
			for (final Gav dependency : pom.dependencies)
			{
				pomsDirectlyAffectedByGroupArtifact.get(dependency.groupArtifact).add(pom);
			}
		}
		final ImmutableMap.Builder<GroupArtifact, ImmutableSet<Pom>> pomsDirectlyAffectedByGroupArtifactBuilder =
				ImmutableMap.builder();
		for (final GroupArtifact groupArtifact : pomsDirectlyAffectedByGroupArtifact.keySet())
		{
			pomsDirectlyAffectedByGroupArtifactBuilder.put(
					groupArtifact,
					pomsDirectlyAffectedByGroupArtifact.get(groupArtifact).build());
		}
		return pomsDirectlyAffectedByGroupArtifactBuilder.build();
	}

	public static final void printAllPoms(final ImmutableList<Pom> allPoms)
	{
		int index = 0;
		for (final Pom pom : allPoms)
		{
			index++;
			System.out.println(String.format("%2d) %s\n    %s", Integer.valueOf(index), pom.gav, pom));
		}
	}

	public static final void printAllGavs(final ImmutableList<Gav> allGavs)
	{
		int index = 0;
		for (final Gav gav : allGavs)
		{
			index++;
			System.out.println(String.format("%2d) %s", Integer.valueOf(index), gav));
		}
	}

	public static final void printAllGroupArtifacts(final ImmutableList<GroupArtifact> allGroupArtifacts)
	{
		int index = 0;
		for (final GroupArtifact groupArtifact : allGroupArtifacts)
		{
			index++;
			System.out.println(String.format("%2d) %s", Integer.valueOf(index), groupArtifact));
		}
	}

	public static void main(final String[] args)
	{
		final File rootDir = new File("/home/hilco/workspaces/smarter-maven");
		final ImmutableList<Pom> allPoms = findAllPoms(rootDir);
		printAllPoms(allPoms);
		final ImmutableList<Gav> allGavs = findAllGavs(allPoms);
		printAllGavs(allGavs);
		final ImmutableList<GroupArtifact> allGroupArtifacts = findAllGroupArtifacts(allGavs);
		printAllGroupArtifacts(allGroupArtifacts);
		final ImmutableMap<GroupArtifact, ImmutableSet<Pom>> pomsDirectlyAffectedByGroupArtifact =
				findPomsDirectlyAffectedByGroupArtifact(allPoms, allGroupArtifacts);
		for (final GroupArtifact groupArtifact : allGroupArtifacts)
		{
			final Set<Pom> affectedPoms = Sets.newConcurrentHashSet();
			for (final Pom pom : pomsDirectlyAffectedByGroupArtifact.get(groupArtifact))
			{
				affectedPoms.add(pom);
			}
			while (true)
			{
				final int size = affectedPoms.size();
				for (final Pom pom : affectedPoms)
				{
					for (final Pom pom_ : pomsDirectlyAffectedByGroupArtifact.get(pom.gav.groupArtifact))
					{
						affectedPoms.addAll(pomsDirectlyAffectedByGroupArtifact.get(pom_.gav.groupArtifact));
					}
				}
				if (size == affectedPoms.size())
				{
					break;
				}
			}
		}
	}
}
