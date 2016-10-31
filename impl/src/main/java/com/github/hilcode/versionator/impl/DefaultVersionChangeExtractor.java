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

import static com.github.hilcode.versionator.impl.Patterns.GAV;
import static com.github.hilcode.versionator.impl.Patterns.GROUP_ARTIFACT;
import java.util.regex.Matcher;
import com.github.hilcode.versionator.api.FromAnyToNewVersion;
import com.github.hilcode.versionator.api.FromOldToNewVersion;
import com.github.hilcode.versionator.api.GavExtractor;
import com.github.hilcode.versionator.api.Matchers;
import com.github.hilcode.versionator.api.RequestedVersionChange;
import com.github.hilcode.versionator.api.VersionChangeExtractor;
import com.github.hilcode.versionator.maven.Gav;
import com.github.hilcode.versionator.maven.GroupArtifact;
import com.github.hilcode.versionator.misc.Either;
import com.github.hilcode.versionator.misc.Result;
import com.google.common.collect.ImmutableList;

public final class DefaultVersionChangeExtractor
	implements
		VersionChangeExtractor
{
	private final GavExtractor gavExtractor;

	private final Matchers matchers;

	public DefaultVersionChangeExtractor(final GavExtractor gavExtractor, final Matchers matchers)
	{
		this.gavExtractor = gavExtractor;
		this.matchers = matchers;
	}

	@Override
	public final Result<String, RequestedVersionChange> extract(final ImmutableList<String> arguments)
	{
		final int argumentCount = arguments.size();
		if (argumentCount == 0)
		{
			return Result.failure(
					"Expected either a GAV or a group:artifact followed by an old and a new version, " +
							"but you provided nothing.\n\n" +
							"Perhaps try --help?");
		}
		if (argumentCount < 3)
		{
			return extractAny(arguments);
		}
		final Matcher matcher = GROUP_ARTIFACT.matcher(arguments.get(argumentCount - 3));
		if (!matcher.matches())
		{
			return extractAny(arguments);
		}
		else
		{
			final String oldVersion = arguments.get(argumentCount - 2);
			if (oldVersion.contains(":"))
			{
				return failure(arguments.get(argumentCount - 3), arguments.get(argumentCount - 2));
			}
			final String newVersion = arguments.get(argumentCount - 1);
			if (newVersion.contains(":"))
			{
				return failure(arguments.get(argumentCount - 3), arguments.get(argumentCount - 1));
			}
			return extractOldNew(this.matchers.toGroupArtifact(matcher), oldVersion, newVersion, arguments);
		}
	}

	Result<String, RequestedVersionChange> failure(final String groupArtifact, final String version)
	{
		return Result.failure(
				String.format(
						"Found a group:artifact (%s) but '%s' is not a valid version.\n\n" +
								"Perhaps try --help?",
						groupArtifact,
						version));
	}

	Result<String, RequestedVersionChange> extractOldNew(
			final GroupArtifact groupArtifact,
			final String oldVersion,
			final String newVersion,
			final ImmutableList<String> arguments)
	{
		final Result<String, ImmutableList<Gav>> gavs =
				this.gavExtractor.extract(arguments.subList(0, arguments.size() - 3));
		if (gavs.isFailure())
		{
			return Result.asFailure(gavs);
		}
		else
		{
			final FromOldToNewVersion versionChange = new FromOldToNewVersion(groupArtifact, oldVersion, newVersion);
			final Either<FromOldToNewVersion, FromAnyToNewVersion> fromOldToNewVersion = Either.left(versionChange);
			return Result.success(new RequestedVersionChange(fromOldToNewVersion, gavs.success()));
		}
	}

	Result<String, RequestedVersionChange> extractAny(final ImmutableList<String> arguments)
	{
		final int indexOfLastArgument = arguments.size() - 1;
		final String lastArgument = arguments.get(indexOfLastArgument);
		final Matcher matcher = GAV.matcher(lastArgument);
		if (!matcher.matches())
		{
			return Result.failure(
					String.format(
							"Expected a GAV, but you provided: '%s'.\n\n" +
									"Perhaps try --help?",
							lastArgument));
		}
		final Result<String, ImmutableList<Gav>> gavs =
				this.gavExtractor.extract(arguments.subList(0, indexOfLastArgument));
		if (gavs.isFailure())
		{
			return Result.asFailure(gavs);
		}
		else
		{
			final FromAnyToNewVersion versionChange = new FromAnyToNewVersion(this.matchers.toGav(matcher));
			final Either<FromOldToNewVersion, FromAnyToNewVersion> fromAnyToNewVersion = Either.right(versionChange);
			return Result.success(new RequestedVersionChange(fromAnyToNewVersion, gavs.success()));
		}
	}
}
