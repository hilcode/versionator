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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.google.common.base.Optional;

public final class PomParserUtils
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
				groupId = node.getTextContent().trim();
			}
			else if (node.getNodeName().equals("artifactId"))
			{
				artifactId = node.getTextContent().trim();
			}
			else if (node.getNodeName().equals("version"))
			{
				version = node.getTextContent().trim();
			}
		}
		return groupId.isEmpty() || artifactId.isEmpty() || version.isEmpty()
				? Optional.<Gav> absent()
				: Optional.of(
						Gav.BUILDER.build(
								GroupArtifact.BUILDER.build(
										GroupId.BUILDER.build(groupId),
										ArtifactId.BUILDER.build(artifactId)),
								Version.BUILDER.build(version)));
	}
}
