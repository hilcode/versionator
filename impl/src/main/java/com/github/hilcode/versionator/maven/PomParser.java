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
package com.github.hilcode.versionator.maven;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

public final class PomParser
{
	public static final XPathExpression newXpathExpression(final XPath xpath, final String expression)
	{
		try
		{
			return xpath.compile(expression);
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static final DocumentBuilder newDocumentBuilder()
	{
		try
		{
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static final Document parse(final DocumentBuilder documentBuilder, final File pomFile)
	{
		try
		{
			return documentBuilder.parse(pomFile);
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static final NodeList evaluateNodes(final XPathExpression xpathExpression, final Document pom)
	{
		try
		{
			return (NodeList) xpathExpression.evaluate(pom, XPathConstants.NODESET);
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static final Node evaluateNode(final XPathExpression xpathExpression, final Document pom)
	{
		try
		{
			return (Node) xpathExpression.evaluate(pom, XPathConstants.NODE);
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static final Optional<Gav> findDependency(final Node dependency, final Document pom)
	{
		final NodeList nodes = dependency.getChildNodes();
		String groupId = "";
		String artifactId = "";
		String version = "";
		for (int j = 0; j < nodes.getLength(); j++)
		{
			final Node node = nodes.item(j);
			if (node.getNodeName().equals("groupId"))
			{
				groupId = node.getTextContent();
			}
			else if (node.getNodeName().equals("artifactId"))
			{
				artifactId = node.getTextContent();
			}
			else if (node.getNodeName().equals("version"))
			{
				version = node.getTextContent();
			}
		}
		return groupId.isEmpty() || artifactId.isEmpty() || version.isEmpty()
				? Optional.<Gav> absent()
				: Optional.of(new Gav(new GroupArtifact(groupId, artifactId), version));
	}

	private final XPathExpression parentGroupIdExpr;

	private final XPathExpression parentArtifactIdExpr;

	private final XPathExpression parentVersionExpr;

	private final XPathExpression groupIdExpr;

	private final XPathExpression artifactIdExpr;

	private final XPathExpression versionExpr;

	private final XPathExpression typeExpr;

	private final XPathExpression propertyExpr;

	private final XPathExpression dependencyExpr;

	private final DocumentBuilder documentBuilder;

	public PomParser()
	{
		final XPath xpath = XPathFactory.newInstance().newXPath();
		this.parentGroupIdExpr = newXpathExpression(xpath, "/project/parent/groupId");
		this.parentArtifactIdExpr = newXpathExpression(xpath, "/project/parent/artifactId");
		this.parentVersionExpr = newXpathExpression(xpath, "/project/parent/version");
		this.groupIdExpr = newXpathExpression(xpath, "/project/groupId");
		this.artifactIdExpr = newXpathExpression(xpath, "/project/artifactId");
		this.versionExpr = newXpathExpression(xpath, "/project/version");
		this.typeExpr = newXpathExpression(xpath, "/project/type");
		this.propertyExpr = newXpathExpression(xpath, "/project/properties/*");
		this.dependencyExpr = newXpathExpression(xpath, "//dependency");
		this.documentBuilder = newDocumentBuilder();
	}

	public Document toDocument(final File pomFile)
	{
		return parse(this.documentBuilder, pomFile);
	}

	public String findParentGroupId(final Document pom)
	{
		final Node parentGroupIdNode = evaluateNode(this.parentGroupIdExpr, pom);
		return parentGroupIdNode != null
				? parentGroupIdNode.getTextContent()
				: "";
	}

	public String findParentArtifactId(final Document pom)
	{
		final Node parentArtifactIdNode = evaluateNode(this.parentArtifactIdExpr, pom);
		return parentArtifactIdNode != null
				? parentArtifactIdNode.getTextContent()
				: "";
	}

	public String findParentVersion(final Document pom)
	{
		final Node parentVersionNode = evaluateNode(this.parentVersionExpr, pom);
		return parentVersionNode != null
				? parentVersionNode.getTextContent()
				: "";
	}

	public String findGroupId(final Document pom)
	{
		final Node groupIdNode = evaluateNode(this.groupIdExpr, pom);
		return groupIdNode != null
				? groupIdNode.getTextContent()
				: findParentGroupId(pom);
	}

	public String findArtifactId(final Document pom)
	{
		return evaluateNode(this.artifactIdExpr, pom).getTextContent();
	}

	public String findVersion(final Document pom)
	{
		final Node versionNode = evaluateNode(this.versionExpr, pom);
		return versionNode != null
				? versionNode.getTextContent()
				: findParentVersion(pom);
	}

	public Optional<Gav> findParentGav(final Document pom)
	{
		final String groupId = findParentGroupId(pom);
		final String artifactId = findParentArtifactId(pom);
		final String version = findParentVersion(pom);
		return groupId.isEmpty() || artifactId.isEmpty() || version.isEmpty()
				? Optional.<Gav> absent()
				: Optional.of(new Gav(new GroupArtifact(groupId, artifactId), version));
	}

	public ImmutableList<Property> findProperties(final Document pom)
	{
		final ImmutableList.Builder<Property> propertiesBuilder = ImmutableList.builder();
		final NodeList propertiesNodeList = evaluateNodes(this.propertyExpr, pom);
		for (int i = 0; i < propertiesNodeList.getLength(); i++)
		{
			final Node propertyNode = propertiesNodeList.item(i);
			final String key = propertyNode.getNodeName();
			final String value = propertyNode.getFirstChild() != null
					? propertyNode.getFirstChild().getTextContent()
					: "";
			propertiesBuilder.add(new Property(key, value));
		}
		return propertiesBuilder.build();
	}

	public Gav findGav(final Document pom)
	{
		final String groupId = findGroupId(pom);
		final String artifactId = findArtifactId(pom);
		final String version = findVersion(pom);
		return new Gav(new GroupArtifact(groupId, artifactId), version);
	}

	public Type findType(final Document pom)
	{
		final Node typeNode = evaluateNode(this.typeExpr, pom);
		return typeNode != null
				? Type.toType(typeNode.getTextContent())
				: Type.JAR;
	}

	public ImmutableList<Gav> findDependencies(final Document pom)
	{
		final ImmutableList.Builder<Gav> gavsBuilder = ImmutableList.builder();
		final NodeList dependencies = evaluateNodes(this.dependencyExpr, pom);
		for (int i = 0; i < dependencies.getLength(); i++)
		{
			final Node dependency = dependencies.item(i);
			final Optional<Gav> maybeGav = findDependency(dependency, pom);
			if (maybeGav.isPresent())
			{
				gavsBuilder.add(maybeGav.get());
			}
		}
		return gavsBuilder.build();
	}
}
