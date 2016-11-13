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
import java.io.IOException;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;

public class ModelWriter
{
	private final VersionSetter versionSetter;

	private final PropertySetter propertySetter;

	public ModelWriter(final VersionSetter versionSetter, final PropertySetter propertySetter)
	{
		Preconditions.checkNotNull(versionSetter, "Missing 'versionSetter'.");
		Preconditions.checkNotNull(propertySetter, "Missing 'propertySetter'.");
		this.versionSetter = versionSetter;
		this.propertySetter = propertySetter;
	}

	public void write(final Model original, final Model result)
	{
		final File tempDir = Files.createTempDir();
		tempDir.deleteOnExit();
		final ImmutableMap.Builder<Pom, File> pomToFileMapBuilder = ImmutableMap.builder();
		final Zipper<Pom, Pom> pomZipper = Zipper.BUILDER.zip(original.poms, result.poms);
		for (final Tuple.Duo<Pom, Pom> tuple : pomZipper)
		{
			final Pom originalPom = tuple._1;
			final Pom resultPom = tuple._2;
			if (originalPom != resultPom)
			{
				System.err.println("POM " + originalPom.gav.toText() + " changed");
				final File pomFile = new File(tempDir, originalPom.gav.groupArtifact.toText() + ".xml");
				pomToFileMapBuilder.put(originalPom, pomFile);
				copy(originalPom.file, pomFile);
				if (originalPom.parent != Pom.NONE)
				{
					System.err.println("POM " + originalPom.gav.toText() + ": change parent to " + resultPom.parent.gav.toText());
					this.versionSetter.updateAll(pomFile, resultPom.parent.gav);
				}
				for (final Dependency dependency : resultPom.dependencies)
				{
					if (!dependency.gav.version.hasPropertyValue())
					{
						System.err.println("POM " + originalPom.gav.toText() + ": change dependency to " + dependency.gav.toText());
						this.versionSetter.updateAll(pomFile, dependency.gav);
					}
				}
				for (final Property property : resultPom.properties)
				{
					System.err.println("POM " + originalPom.gav.toText() + ": change property to " + property);
					this.propertySetter.updateProperty(pomFile, property);
				}
				System.err.println("POM " + originalPom.gav.toText() + ": change GAV to " + resultPom.gav.toText());
				this.versionSetter.updateAll(pomFile, resultPom.gav);
				copy(pomFile, originalPom.file);
				pomFile.delete();
			}
		}
	}

	public static final void copy(final File fromFile, final File toFile)
	{
		try
		{
			Files.copy(fromFile, toFile);
		}
		catch (final IOException e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
}
