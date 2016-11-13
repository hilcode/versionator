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
import com.google.common.base.Preconditions;

public final class PomFinderUtils
{
	public static final File toCanonical(final File file)
	{
		Preconditions.checkNotNull(file, "Missing 'file'.");
		try
		{
			return file.getCanonicalFile();
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
}
