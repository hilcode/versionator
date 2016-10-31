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
import java.util.Collections;
import java.util.List;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public final class PomFinder
{
	public static final File toCanonical(final File file)
	{
		try
		{
			return file.getCanonicalFile();
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static final void walkDirectory(final File dir, final List<File> poms)
	{
		for (final File entry : dir.listFiles())
		{
			if (entry.isDirectory())
			{
				final File subdir = entry;
				if (!subdir.getName().equals("target"))
				{
					walkDirectory(subdir, poms);
				}
			}
			else if (entry.isFile())
			{
				final File file = toCanonical(entry.getAbsoluteFile());
				if (file.getName().equals("pom.xml"))
				{
					poms.add(file);
				}
			}
		}
	}

	public ImmutableList<File> findPoms(final File rootDir)
	{
		final List<File> poms = Lists.newArrayList();
		walkDirectory(rootDir, poms);
		Collections.sort(poms);
		return ImmutableList.copyOf(poms);
	}
}
