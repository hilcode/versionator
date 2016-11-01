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

import static com.github.hilcode.versionator.PomParserUtils.evaluateNode;
import static com.github.hilcode.versionator.PomParserUtils.evaluateNodes;
import static com.github.hilcode.versionator.PomParserUtils.findDependency;
import static com.github.hilcode.versionator.PomParserUtils.newDocumentBuilder;
import static com.github.hilcode.versionator.PomParserUtils.newXpathExpression;
import static com.github.hilcode.versionator.PomParserUtils.parse;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public final class DefaultPomParser
	implements
		PomParser
{
	private final XPathExpression parentGroupIdExpr;

	private final XPathExpression parentArtifactIdExpr;

	private final XPathExpression parentVersionExpr;

	private final XPathExpression parentRelativePathExpr;

	private final XPathExpression groupIdExpr;

	private final XPathExpression artifactIdExpr;

	private final XPathExpression versionExpr;

	private final XPathExpression typeExpr;

	private final XPathExpression moduleExpr;

	private final XPathExpression propertyExpr;

	private final XPathExpression dependencyExpr;

	private final DocumentBuilder documentBuilder;

	public DefaultPomParser()
	{
		final XPath xpath = XPathFactory.newInstance().newXPath();
		this.parentGroupIdExpr = newXpathExpression(xpath, "/project/parent/groupId");
		this.parentArtifactIdExpr = newXpathExpression(xpath, "/project/parent/artifactId");
		this.parentVersionExpr = newXpathExpression(xpath, "/project/parent/version");
		this.parentRelativePathExpr = newXpathExpression(xpath, "/project/parent/relativePath");
		this.groupIdExpr = newXpathExpression(xpath, "/project/groupId");
		this.artifactIdExpr = newXpathExpression(xpath, "/project/artifactId");
		this.versionExpr = newXpathExpression(xpath, "/project/version");
		this.typeExpr = newXpathExpression(xpath, "/project/type");
		this.moduleExpr = newXpathExpression(xpath, "/project/modules/module");
		this.propertyExpr = newXpathExpression(xpath, "/project/properties/*");
		this.dependencyExpr = newXpathExpression(xpath, "/project/*//*/artifactId");
		this.documentBuilder = newDocumentBuilder();
	}

	@Override
	public Document toDocument(final File pomFile)
	{
		return parse(this.documentBuilder, pomFile);
	}

	@Override
	public String findParentGroupId(final Document pom)
	{
		final Node parentGroupIdNode = evaluateNode(this.parentGroupIdExpr, pom);
		return parentGroupIdNode != null
				? parentGroupIdNode.getTextContent().trim()
				: "";
	}

	@Override
	public String findParentArtifactId(final Document pom)
	{
		final Node parentArtifactIdNode = evaluateNode(this.parentArtifactIdExpr, pom);
		return parentArtifactIdNode != null
				? parentArtifactIdNode.getTextContent().trim()
				: "";
	}

	@Override
	public String findParentVersion(final Document pom)
	{
		final Node parentVersionNode = evaluateNode(this.parentVersionExpr, pom);
		return parentVersionNode != null
				? parentVersionNode.getTextContent().trim()
				: "";
	}

	@Override
	public String findParentRelativePath(final Document pom)
	{
		final Node parentRelativePathNode = evaluateNode(this.parentRelativePathExpr, pom);
		return parentRelativePathNode != null
				? parentRelativePathNode.getTextContent().trim()
				: "..";
	}

	@Override
	public Tuple._2<GroupIdSource, String> findGroupId(final Document pom)
	{
		final Node groupIdNode = evaluateNode(this.groupIdExpr, pom);
		return groupIdNode != null
				? new Tuple._2<>(GroupIdSource.GROUP_ID_SOURCE_IS_POM, groupIdNode.getTextContent().trim())
				: new Tuple._2<>(GroupIdSource.GROUP_ID_SOURCE_IS_PARENT, findParentGroupId(pom));
	}

	@Override
	public String findArtifactId(final Document pom)
	{
		return evaluateNode(this.artifactIdExpr, pom).getTextContent().trim();
	}

	@Override
	public Tuple._2<VersionSource, String> findVersion(final Document pom)
	{
		final Node versionNode = evaluateNode(this.versionExpr, pom);
		return versionNode != null
				? new Tuple._2<>(VersionSource.POM, versionNode.getTextContent().trim())
				: new Tuple._2<>(VersionSource.PARENT, findParentVersion(pom));
	}

	@Override
	public Optional<Gav> findParentGav(final Document pom)
	{
		final String groupId = findParentGroupId(pom);
		final String artifactId = findParentArtifactId(pom);
		final String version = findParentVersion(pom);
		return groupId.isEmpty() || artifactId.isEmpty() || version.isEmpty()
				? Optional.<Gav> absent()
				: Optional.of(
						Gav.BUILDER.build(
								GroupArtifact.BUILDER.build(
										GroupId.BUILDER.build(groupId),
										ArtifactId.BUILDER.build(artifactId)),
								Version.BUILDER.build(version)));
	}

	@Override
	public ImmutableList<String> findModules(final Document pom)
	{
		final ImmutableList.Builder<String> propertiesBuilder = ImmutableList.builder();
		final NodeList propertiesNodeList = evaluateNodes(this.moduleExpr, pom);
		for (int i = 0; i < propertiesNodeList.getLength(); i++)
		{
			final Node propertyNode = propertiesNodeList.item(i);
			final String module = propertyNode.getFirstChild().getTextContent().trim();
			propertiesBuilder.add(module);
		}
		return propertiesBuilder.build();
	}

	@Override
	public ImmutableList<Property> findProperties(final Document pom)
	{
		final Set<Property> properties = Sets.newConcurrentHashSet();
		final NodeList propertiesNodeList = evaluateNodes(this.propertyExpr, pom);
		for (int i = 0; i < propertiesNodeList.getLength(); i++)
		{
			final Node propertyNode = propertiesNodeList.item(i);
			final Key key = Key.BUILDER.build(propertyNode.getNodeName());
			final String value = propertyNode.getFirstChild() != null
					? propertyNode.getFirstChild().getTextContent().trim()
					: "";
			properties.add(Property.BUILDER.build(key, value));
		}
		final List<Property> sortedProperties = Lists.newArrayList(properties);
		Collections.sort(sortedProperties);
		return ImmutableList.copyOf(sortedProperties);
	}

	@Override
	public Tuple._3<GroupIdSource, VersionSource, Gav> findGav(final Document pom)
	{
		final Tuple._2<GroupIdSource, String> groupIdTuple = findGroupId(pom);
		final String artifactId = findArtifactId(pom);
		final Tuple._2<VersionSource, String> versionTuple = findVersion(pom);
		return new Tuple._3<>(
				groupIdTuple._1,
				versionTuple._1,
				Gav.BUILDER.build(
						GroupArtifact.BUILDER.build(
								GroupId.BUILDER.build(groupIdTuple._2),
								ArtifactId.BUILDER.build(artifactId)),
						Version.BUILDER.build(versionTuple._2)));
	}

	@Override
	public Type findType(final Document pom)
	{
		final Node typeNode = evaluateNode(this.typeExpr, pom);
		return typeNode != null
				? Type.toType(typeNode.getTextContent().trim())
				: Type.JAR;
	}

	@Override
	public ImmutableList<Dependency> findDependencies(final Document pom)
	{
		final HashSet<Dependency> dependencies = Sets.newHashSet();
		final NodeList dependenciesNodeList = evaluateNodes(this.dependencyExpr, pom);
		for (int i = 0; i < dependenciesNodeList.getLength(); i++)
		{
			final Node dependencyNode = dependenciesNodeList.item(i).getParentNode();
			final Optional<Gav> maybeGav = findDependency(dependencyNode, pom);
			if (maybeGav.isPresent())
			{
				final Dependency dependency = Dependency.BUILDER.build(maybeGav.get());
				dependencies.add(dependency);
			}
		}
		final List<Dependency> sortedDependencies = Lists.newArrayList(dependencies);
		Collections.sort(sortedDependencies);
		return ImmutableList.copyOf(sortedDependencies);
	}
}
