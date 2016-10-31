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
import java.io.FileOutputStream;
import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;

public final class VersionSetter
{
	public static final XMLModifier newXmlModifier(final VTDNav vtdNavigator)
	{
		try
		{
			return new XMLModifier(vtdNavigator);
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public void selectXpath(final AutoPilot autoPilot, final String xpath)
	{
		try
		{
			autoPilot.selectXPath(xpath);
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static final int evalXpath(final AutoPilot autoPilot)
	{
		try
		{
			return autoPilot.evalXPath();
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static final void updateToken(final XMLModifier xmlModifier, final int index, final Gav gav)
	{
		try
		{
			xmlModifier.updateToken(index, gav.version);
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	private static final void output(final XMLModifier xmlModifier, final File newPomFile)
	{
		try
		{
			final FileOutputStream outputStream = new FileOutputStream(newPomFile);
			xmlModifier.output(outputStream);
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public void updateAll(final File pomFile, final File newPomFile, final Gav gav)
	{
		final String xpath = String.format(
				"//*[child::groupId='%s' and child::artifactId='%s']/version/text()",
				gav.groupArtifact.groupId,
				gav.groupArtifact.artifactId);
		final VTDGen vtdGenerator = new VTDGen();
		vtdGenerator.enableIgnoredWhiteSpace(true);
		if (vtdGenerator.parseFile(pomFile.getPath(), true))
		{
			final VTDNav vtdNavigator = vtdGenerator.getNav();
			final XMLModifier xmlModifier = newXmlModifier(vtdNavigator);
			final AutoPilot autoPilot = new AutoPilot(vtdNavigator);
			selectXpath(autoPilot, xpath);
			while (true)
			{
				final int index = evalXpath(autoPilot);
				if (index == -1)
				{
					break;
				}
				updateToken(xmlModifier, index, gav);
			}
			output(xmlModifier, newPomFile);
		}
		else
		{
			throw new IllegalStateException(String.format("Unable to parse '%s'.", pomFile.getPath()));
		}
	}
}
