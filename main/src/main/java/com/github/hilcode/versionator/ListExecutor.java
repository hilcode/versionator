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

import static com.github.hilcode.versionator.maven.impl.PomParserUtils.evaluateNodes;
import static com.github.hilcode.versionator.maven.impl.PomParserUtils.newXpathExpression;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.github.hilcode.versionator.Globs.Glob;
import com.github.hilcode.versionator.maven.Gav;
import com.github.hilcode.versionator.maven.GroupArtifact;
import com.github.hilcode.versionator.maven.Pom;
import com.github.hilcode.versionator.maven.PomFinder;
import com.github.hilcode.versionator.maven.PomParser;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class ListExecutor
{
	private final PomParser pomParser;

	private final PomFinder pomFinder;

	private final Command.List commandList;

	public ListExecutor(final PomParser pomParser, final PomFinder pomFinder, final Command.List commandList)
	{
		Preconditions.checkNotNull(pomParser, "Missing 'pomParser'.");
		Preconditions.checkNotNull(pomFinder, "Missing 'pomFinder'.");
		Preconditions.checkNotNull(commandList, "Missing 'commandList'.");
		this.pomParser = pomParser;
		this.pomFinder = pomFinder;
		this.commandList = commandList;
	}

	public final void execute()
	{
		final File rootDir_ = this.commandList.rootDir.getAbsoluteFile();
		final File rootDir = rootDir_.getPath().endsWith(".")
				? rootDir_.getParentFile()
				: rootDir_;
		final ImmutableList<Pom> poms = this.pomFinder.findAllPoms(rootDir);
		final XPath xpath = XPathFactory.newInstance().newXPath();
		final XPathExpression xpathExpr = newXpathExpression(xpath, "/project/*//*/artifactId");
		final XPathExpression artifactIdXpath = newXpathExpression(xpath, "artifactId");
		final XPathExpression groupIdXpath = newXpathExpression(xpath, "groupId");
		final XPathExpression versionXpath = newXpathExpression(xpath, "version");
		final Set<PomAndGav> pomAndGavs = Sets.newConcurrentHashSet();
		for (final Pom pom : poms)
		{
			final Document pomDocument = this.pomParser.toDocument(pom.file);
			final NodeList nodeList = evaluateNodes(xpathExpr, pomDocument);
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				final Node node = nodeList.item(i);
				final Node parent = node.getParentNode();
				final Optional<String> maybeGroupId = extractText(parent, groupIdXpath);
				final String groupId;
				if (maybeGroupId.isPresent())
				{
					groupId = maybeGroupId.get();
				}
				else
				{
					if ("plugin".equals(parent.getNodeName()))
					{
						groupId = "org.apache.maven.plugins";
					}
					else
					{
						groupId = "";
					}
				}
				final String artifactId = extractText(parent, artifactIdXpath).get();
				final Optional<String> maybeVersion = extractText(parent, versionXpath);
				final String version;
				if (maybeVersion.isPresent())
				{
					version = maybeVersion.get();
				}
				else
				{
					version = "";
				}
				final Gav gav = Gav.BUILDER.build(GroupArtifact.BUILDER.build(groupId, artifactId), version);
				boolean includeGav = true;
				for (final String pattern : this.commandList.patterns)
				{
					final boolean exclusion = pattern.startsWith("!");
					final Glob glob = exclusion ? Globs.create(pattern.substring(1)) : Globs.create(pattern);
					if (exclusion)
					{
						if (glob.match(gav.toText()))
						{
							includeGav = false;
						}
					}
					else
					{
						if (glob.match(gav.toText()))
						{
							includeGav = true;
						}
					}
				}
				if (includeGav)
				{
					pomAndGavs.add(new PomAndGav(pom, gav));
				}
			}
		}
		final List<PomAndGav> pomAndGavs_ = Lists.newArrayList(pomAndGavs);
		Collections.sort(pomAndGavs_);
		if (this.commandList.grouping == Command.Grouping.BY_POM)
		{
			final List<Pom> poms_ = Lists.newArrayList();
			final Map<Pom, List<Gav>> map = Maps.newConcurrentMap();
			for (final PomAndGav pomAndGav : pomAndGavs_)
			{
				if (!map.containsKey(pomAndGav.pom))
				{
					poms_.add(pomAndGav.pom);
					map.put(pomAndGav.pom, Lists.<Gav> newArrayList());
				}
				map.get(pomAndGav.pom).add(pomAndGav.gav);
			}
			Collections.sort(poms_);
			final String pomMask = "%" + (1 + (int) Math.floor(Math.log10(poms_.size()))) + "d) %s (%s)";
			int pomIndex = 0;
			for (final Pom pom : poms_)
			{
				if (pomIndex != 0)
				{
					System.out.println();
				}
				pomIndex++;
				final File relativePomFile = new File(pom.file.getPath().substring(rootDir.getAbsolutePath().length() + 1));
				System.out.println(String.format(pomMask, Integer.valueOf(pomIndex), pom.gav.toText(), relativePomFile));
				final List<Gav> gavs = map.get(pom);
				final String gavMask = "    %" + (1 + (int) Math.floor(Math.log10(gavs.size()))) + "d) %s";
				int gavIndex = 0;
				for (final Gav gav : gavs)
				{
					gavIndex++;
					System.out.println(String.format(gavMask, Integer.valueOf(gavIndex), gav.toText()));
				}
			}
		}
		else
		{
			if (this.commandList.verbosity == Command.Verbosity.VERBOSE)
			{
				final List<GroupArtifact> groupArtifacts = Lists.newArrayList();
				final Map<GroupArtifact, Set<Pom>> map = Maps.newConcurrentMap();
				int maxLength = 0;
				int maxGroupSize = 0;
				for (final PomAndGav gavAndPom : pomAndGavs_)
				{
					final GroupArtifact groupArtifact = gavAndPom.gav.groupArtifact;
					if (!map.containsKey(groupArtifact))
					{
						groupArtifacts.add(groupArtifact);
						map.put(groupArtifact, Sets.<Pom> newConcurrentHashSet());
					}
					map.get(groupArtifact).add(gavAndPom.pom);
					final int groupSize = map.get(groupArtifact).size();
					if (groupSize > maxGroupSize)
					{
						maxGroupSize = groupSize;
					}
					final int length = gavAndPom.pom.gav.groupArtifact.toText().length();
					if (length > maxLength)
					{
						maxLength = length;
					}
				}
				final int maxGroupLength = 1 + (int) Math.floor(Math.log10(maxGroupSize));
				Collections.sort(groupArtifacts);
				final String groupArtifactMask = "%" + (1 + (int) Math.floor(Math.log10(groupArtifacts.size()))) + "d) %s";
				int groupArtifactIndex = 0;
				final String pomMask = "    %" + maxGroupLength + "d) %-" + maxLength + "s   %s";
				for (final GroupArtifact groupArtifact : groupArtifacts)
				{
					if (groupArtifactIndex != 0)
					{
						System.out.println();
					}
					groupArtifactIndex++;
					System.out.println(String.format(groupArtifactMask, Integer.valueOf(groupArtifactIndex), groupArtifact.toText()));
					final List<Pom> poms_ = Lists.newArrayList(map.get(groupArtifact));
					Collections.sort(poms_);
					int pomIndex = 0;
					for (final Pom pom : poms_)
					{
						pomIndex++;
						final File relativePomFile = new File(pom.file.getPath().substring(rootDir.getAbsolutePath().length() + 1));
						System.out.println(String.format(pomMask, Integer.valueOf(pomIndex), pom.gav.groupArtifact.toText(), relativePomFile));
					}
				}
			}
			else
			{
				final List<Gav> gavs = Lists.newArrayList();
				final Set<Gav> gavsSeen = Sets.newConcurrentHashSet();
				for (final PomAndGav gavAndPom : pomAndGavs_)
				{
					final Gav gav = gavAndPom.gav;
					if (!gavsSeen.contains(gav))
					{
						gavs.add(gav);
						gavsSeen.add(gav);
					}
				}
				Collections.sort(gavs);
				final String gavMask = "%" + (1 + (int) Math.floor(Math.log10(gavs.size()))) + "d) %s";
				int gavIndex = 0;
				for (final Gav gav : gavs)
				{
					gavIndex++;
					System.out.println(String.format(gavMask, Integer.valueOf(gavIndex), gav.toText()));
				}
			}
		}
	}

	public static final Optional<String> extractText(final Node parent, final XPathExpression xpathExpression)
	{
		try
		{
			final Node node = (Node) xpathExpression.evaluate(parent, XPathConstants.NODE);
			return node != null ? Optional.of(node.getTextContent()) : Optional.<String> absent();
		}
		catch (final XPathExpressionException e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
}
