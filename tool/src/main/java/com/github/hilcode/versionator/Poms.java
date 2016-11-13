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

import static com.github.hilcode.versionator.VersionSource.PARENT;

public final class Poms
{
	public static final Pom findRoot(final Pom pom)
	{
		if (pom.parent == Pom.NONE)
		{
			return pom;
		}
		Pom pom_ = pom;
		while (pom_.parent != Pom.NONE && pom_.versionSource == PARENT)
		{
			pom_ = pom_.parent;
		}
		return pom_;
	}

	public static final boolean isReleasable(final Pom pom)
	{
		if (pom.parent != Pom.NONE && pom.parent.gav.version.isSnapshot())
		{
			return false;
		}
		for (final Dependency dependency : pom.dependencies)
		{
			if (dependency.gav.version.isSnapshot())
			{
				return false;
			}
		}
		return true;
	}
}
