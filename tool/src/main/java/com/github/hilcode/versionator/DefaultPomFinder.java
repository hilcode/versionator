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

import static com.github.hilcode.versionator.PomFinderUtils.toCanonical;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class DefaultPomFinder
	implements
		PomFinder
{
	private final PomParser pomParser;

	public DefaultPomFinder(final PomParser pomParser)
	{
		Preconditions.checkNotNull(pomParser, "Missing 'pomParser'.");
		this.pomParser = pomParser;
	}

	@Override
	public ImmutableList<Pom> findAllPoms(final File rootDir)
	{
		final Map<GroupArtifact, Pom> map = Maps.newConcurrentMap();
		final File rootPomFile = new File(rootDir, "pom.xml");
		findPoms(map, rootPomFile);
		final List<Pom> allPoms = Lists.newArrayList(map.values());
		Collections.sort(allPoms);
		return ImmutableList.copyOf(allPoms);
	}

	public void findPoms(final Map<GroupArtifact, Pom> map, final File pomFile)
	{
		final Document pomDocument = this.pomParser.toDocument(pomFile);
		final Tuple.Triple<GroupIdSource, VersionSource, Gav> gavTuple = this.pomParser.findGav(pomDocument);
		final ImmutableList<String> modules = this.pomParser.findModules(pomDocument);
		final Pom pom = Pom.BUILDER.build(
				gavTuple._3,
				gavTuple._1,
				gavTuple._2,
				toCanonical(pomFile.getAbsoluteFile()),
				this.pomParser.findType(pomDocument),
				findParentPom(map, pomFile, this.pomParser.findParentGav(pomDocument), pomDocument),
				modules,
				this.pomParser.findProperties(pomDocument),
				this.pomParser.findDependencies(pomDocument));
		map.put(gavTuple._3.groupArtifact, pom);
		for (final String module : modules)
		{
			final File moduleDir = new File(pomFile.getParentFile(), module);
			findPoms(map, moduleDir.isFile() ? moduleDir : new File(moduleDir, "pom.xml"));
		}
	}

	public Pom findParentPom(
			final Map<GroupArtifact, Pom> map,
			final File pomFile,
			final Optional<Gav> parentGav,
			final Document pomDocument)
	{
		if (parentGav.isPresent())
		{
			final GroupArtifact parentGroupArtifact = parentGav.get().groupArtifact;
			if (!map.containsKey(parentGroupArtifact))
			{
				final File parentDir = new File(pomFile.getParentFile(), this.pomParser.findParentRelativePath(pomDocument));
				findPoms(map, parentDir.isFile() ? parentDir : new File(parentDir, "pom.xml"));
			}
			return map.get(parentGroupArtifact);
		}
		else
		{
			return Pom.NONE;
		}
	}
}
