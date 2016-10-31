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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.github.hilcode.versionator.maven.PomFinder;
import com.github.hilcode.versionator.maven.PomParser;
import com.google.common.base.Optional;

public final class Main3
{
	public static final void main(final String[] args) throws Exception
	{
		final File rootDir = new File("/home/hilco/workspaces/smarter-maven");
		final PomFinder pomFinder = new PomFinder();
		final PomParser pomParser = new PomParser();
		final XPath xpath = XPathFactory.newInstance().newXPath();
		final XPathExpression xpathExpr = PomParser.newXpathExpression(xpath, "/project/*//*/artifactId");
		final XPathExpression artifactIdXpath = PomParser.newXpathExpression(xpath, "artifactId");
		final XPathExpression groupIdXpath = PomParser.newXpathExpression(xpath, "groupId");
		final XPathExpression versionXpath = PomParser.newXpathExpression(xpath, "version");
		for (final File pomFile : pomFinder.findPoms(rootDir))
		{
			System.out.println(pomFile);
			final Document pomDocument = pomParser.toDocument(pomFile);
			final NodeList nodeList = PomParser.evaluateNodes(xpathExpr, pomDocument);
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				final Node node = nodeList.item(i);
				final Node parent = node.getParentNode();
				final StringBuilder gavBuilder = new StringBuilder();
				final Optional<String> maybeGroupId = extractText(parent, groupIdXpath);
				if (maybeGroupId.isPresent())
				{
					gavBuilder.append(maybeGroupId.get());
				}
				else
				{
					if ("plugin".equals(parent.getNodeName()))
					{
						gavBuilder.append("org.apache.maven.plugins");
					}
				}
				gavBuilder.append(':');
				gavBuilder.append(extractText(parent, artifactIdXpath).get());
				gavBuilder.append(':');
				final Optional<String> maybeVersion = extractText(parent, versionXpath);
				if (maybeVersion.isPresent())
				{
					gavBuilder.append(maybeVersion.get());
				}
				System.out.println(gavBuilder);
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
