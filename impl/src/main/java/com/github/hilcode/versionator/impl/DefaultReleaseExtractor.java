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
package com.github.hilcode.versionator.impl;

import static com.github.hilcode.versionator.impl.Patterns.GROUP_ARTIFACT;
import java.util.regex.Matcher;
import com.github.hilcode.versionator.api.Matchers;
import com.github.hilcode.versionator.api.ReleaseExtractor;
import com.github.hilcode.versionator.maven.GroupArtifact;
import com.github.hilcode.versionator.misc.Result;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public final class DefaultReleaseExtractor
	implements
		ReleaseExtractor
{
	private final Matchers matchers;

	public DefaultReleaseExtractor(final Matchers matchers)
	{
		this.matchers = matchers;
	}

	@Override
	public final Result<String, ImmutableList<GroupArtifact>> extract(final ImmutableList<String> arguments)
	{
		Preconditions.checkNotNull(arguments, "Missing 'arguments'.");
		if (arguments.isEmpty())
		{
			return Result.success(ImmutableList.<GroupArtifact> of());
		}
		final String firstArgument = arguments.get(0);
		if (firstArgument.equals("-x") || firstArgument.equals("--exclude"))
		{
			final ImmutableList<String> exclusions = arguments.subList(1, arguments.size());
			return extractExclusions(exclusions);
		}
		else
		{
			return Result.failure(
					String.format(
							"Expected '-x' or '--exclude' but found '%s' instead.\n\nPerhaps try --help?",
							firstArgument));
		}
	}

	Result<String, ImmutableList<GroupArtifact>> extractExclusions(final ImmutableList<String> exclusions)
	{
		final ImmutableList.Builder<GroupArtifact> exclusionsBuilder = ImmutableList.builder();
		for (final String exclusion : exclusions)
		{
			final Matcher matcher = GROUP_ARTIFACT.matcher(exclusion);
			if (!matcher.matches())
			{
				return Result.failure(
						String.format(
								"Invalid group and artifact: '%s'.\n\nPerhaps try --help?",
								exclusion));
			}
			exclusionsBuilder.add(this.matchers.toGroupArtifact(matcher));
		}
		return Result.success(exclusionsBuilder.build());
	}
}
