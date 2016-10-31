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

import static com.github.hilcode.versionator.Command.RunType.ACTUAL;
import static org.fusesource.jansi.Ansi.ansi;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import org.fusesource.jansi.Ansi.Color;
import org.w3c.dom.Document;
import com.github.hilcode.versionator.maven.Gav;
import com.github.hilcode.versionator.maven.GroupArtifact;
import com.github.hilcode.versionator.maven.Pom;
import com.github.hilcode.versionator.maven.PomFinder;
import com.github.hilcode.versionator.maven.PomParser;
import com.github.hilcode.versionator.maven.Property;
import com.github.hilcode.versionator.maven.Type;
import com.github.hilcode.versionator.maven.VersionSetter;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.primitives.Longs;

public final class ReleaseExecutor
{
	private final HashFunction goodFastHashFunction;

	private final File tempDir;

	private final Command.Release commandRelease;

	public ReleaseExecutor(final Command.Release commandRelease)
	{
		this.goodFastHashFunction = Hashing.goodFastHash(128);
		this.tempDir = Files.createTempDir();
		this.commandRelease = commandRelease;
	}

	public static final UUID fromHashCodeToUuid(final HashCode hashCode)
	{
		final byte[] hash = hashCode.asBytes();
		return new UUID(
				Longs.fromBytes(hash[0], hash[1], hash[2], hash[3], hash[4], hash[5], hash[6], hash[7]),
				Longs.fromBytes(hash[8], hash[9], hash[10], hash[11], hash[12], hash[13], hash[14], hash[15]));
	}

	public void execute() throws Exception
	{
		final File rootDir = this.commandRelease.rootDir;
		final ImmutableList<Pom> poms = findAllPoms(rootDir);
		final ImmutableSet.Builder<GroupArtifact> exclusionsBuilder = ImmutableSet.builder();
		for (final String exclusionGroupArtifact : this.commandRelease.exclusions)
		{
			final GroupArtifact groupArtifact = GroupArtifact.BUILDER.build(exclusionGroupArtifact);
			exclusionsBuilder.add(groupArtifact);
		}
		final ImmutableSet<GroupArtifact> exclusions = exclusionsBuilder.build();
		final ImmutableMap.Builder<Pom, File> pomToFileMapBuilder = ImmutableMap.builder();
		final Map<Pom, Gav> pomToGavMap = Maps.newConcurrentMap();
		final Map<Pom, UUID> pomToUuidMap = Maps.newConcurrentMap();
		for (final Pom pom : poms)
		{
			final File pomFile = new File(this.tempDir, pom.gav.groupArtifact.toText() + ".xml");
			pomToFileMapBuilder.put(pom, pomFile);
			Files.copy(pom.file, pomFile);
			final UUID pomUuid = fromHashCodeToUuid(Files.hash(pomFile, this.goodFastHashFunction));
			pomToUuidMap.put(pom, pomUuid);
			pomToGavMap.put(pom, pom.gav);
		}
		final ImmutableMap<Pom, File> pomToFileMap = pomToFileMapBuilder.build();
		final ImmutableList<Gav> gavs = findAllGavs(poms);
		final ImmutableList<GroupArtifact> groupArtifacts = findAllGroupArtifacts(gavs);
		int maxLength = 0;
		for (final GroupArtifact groupArtifact : groupArtifacts)
		{
			final int length = groupArtifact.toText().length();
			if (length > maxLength)
			{
				maxLength = length;
			}
		}
		final String mask = "%-" + maxLength + "s";
		final ImmutableMap<GroupArtifact, ImmutableList<Pom>> map =
				findPomsDirectlyAffectedByGroupArtifact(poms, groupArtifacts);
		final Queue<Gav> queue = Queues.newConcurrentLinkedQueue();
		for (final Pom pom : poms)
		{
			final Version version = Version.Builder.instance(pom.gav.version, pom.type);
			if (version.snapshot() && !exclusions.contains(pom.gav.groupArtifact))
			{
				final Version releaseVersion = version.toRelease();
				final Gav gav = Gav.BUILDER.build(pom.gav.groupArtifact, releaseVersion.toText());
				queue.add(gav);
			}
		}
		while (queue.size() > 0)
		{
			final Gav changeGav = queue.remove();
			final ImmutableList<Pom> affectedPoms = map.get(changeGav.groupArtifact);
			for (final Pom affectedPom : affectedPoms)
			{
				final File affectedPomFile = pomToFileMap.get(affectedPom);
				if (affectedPom.gav.groupArtifact.equals(changeGav.groupArtifact))
				{
					if (this.commandRelease.colour == Command.Colour.COLOUR)
					{
						System.out.println(ansi()
								.fgBright(Color.BLUE)
								.a('[')
								.fgBright(Color.YELLOW)
								.format(mask, changeGav.groupArtifact.toText())
								.fgBright(Color.BLUE)
								.a(']')
								.reset()
								.a(' ')
								.fgBright(Color.WHITE)
								.format(
										"Release %s as %s",
										changeGav.groupArtifact.toText(),
										changeGav.version)
								.reset()
								.toString());
					}
					else
					{
						System.out.println("[" +
								String.format(mask, changeGav.groupArtifact.toText()) +
								"] " +
								String.format(
										"Release %s as %s",
										changeGav.groupArtifact.toText(),
										changeGav.version));
					}
				}
				else
				{
					if (this.commandRelease.colour == Command.Colour.COLOUR)
					{
						System.out.println(ansi()
								.fgBright(Color.BLUE)
								.a('[')
								.fgBright(Color.YELLOW)
								.format(mask, affectedPom.gav.groupArtifact.toText())
								.fgBright(Color.BLUE)
								.a(']')
								.reset()
								.a(' ')
								.fgBright(Color.WHITE)
								.format(
										"Set %s to %s",
										changeGav.groupArtifact.toText(),
										changeGav.version)
								.reset()
								.toString());
					}
					else
					{
						System.out.println("[" +
								String.format(mask, affectedPom.gav.groupArtifact.toText()) +
								"] " +
								String.format(
										"Set %s to %s",
										changeGav.groupArtifact.toText(),
										changeGav.version));
					}
				}
				final VersionSetter versionSetter = new VersionSetter();
				final File newPomFile = pomToFileMap.get(affectedPom);
				versionSetter.updateAll(affectedPomFile, newPomFile, changeGav);
				final UUID newPomUuid = fromHashCodeToUuid(Files.hash(newPomFile, this.goodFastHashFunction));
				if (!pomToUuidMap.get(affectedPom).equals(newPomUuid))
				{
					pomToUuidMap.put(affectedPom, newPomUuid);
				}
			}
		}
		for (final Pom pom : pomToFileMap.keySet())
		{
			final File pomFile = pomToFileMap.get(pom);
			if (this.commandRelease.runType == ACTUAL)
			{
				final UUID originalPomUuid = fromHashCodeToUuid(Files.hash(pom.file, this.goodFastHashFunction));
				if (!pomToUuidMap.get(pom).equals(originalPomUuid))
				{
					Files.copy(pomFile, pom.file);
				}
			}
			pomFile.delete();
		}
		this.tempDir.delete();
	}

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
			final Pom pom = new Pom(gav, pomFile, type, parentGav, properties, dependencies);
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

	public static final ImmutableMap<GroupArtifact, ImmutableList<Pom>> findPomsDirectlyAffectedByGroupArtifact(
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
		final ImmutableMap.Builder<GroupArtifact, ImmutableList<Pom>> pomsDirectlyAffectedByGroupArtifactBuilder = ImmutableMap.builder();
		for (final GroupArtifact groupArtifact : pomsDirectlyAffectedByGroupArtifact.keySet())
		{
			final List<Pom> affectedPoms = Lists.newArrayList(pomsDirectlyAffectedByGroupArtifact.get(groupArtifact).build());
			Collections.sort(affectedPoms);
			pomsDirectlyAffectedByGroupArtifactBuilder.put(
					groupArtifact,
					ImmutableList.copyOf(affectedPoms));
		}
		return pomsDirectlyAffectedByGroupArtifactBuilder.build();
	}
}
