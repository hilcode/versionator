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
import java.util.regex.Matcher;
import com.github.hilcode.versionator.api.GavExtractor;
import com.github.hilcode.versionator.maven.Gav;
import com.github.hilcode.versionator.maven.GroupArtifact;
import com.github.hilcode.versionator.misc.Result;
import com.google.common.collect.ImmutableList;

public final class DefaultGavExtractor
	implements
		GavExtractor
{
	private final Gav.Builder gavBuilder;

	private final GroupArtifact.Builder groupArtifactBuilder;

	public DefaultGavExtractor()
	{
		this(Gav.BUILDER, GroupArtifact.BUILDER);
	}

	DefaultGavExtractor(final Gav.Builder gavBuilder, final GroupArtifact.Builder groupArtifactBuilder)
	{
		this.gavBuilder = gavBuilder;
		this.groupArtifactBuilder = groupArtifactBuilder;
	}

	@Override
	public final Result<String, ImmutableList<Gav>> extract(final ImmutableList<String> arguments)
	{
		final ImmutableList.Builder<Gav> gavs = ImmutableList.builder();
		for (final String argument : arguments)
		{
			final Matcher matcher = GAV.matcher(argument);
			if (!matcher.matches())
			{
				return Result.failure(
						String.format(
								"Invalid GAV: '%s'.\n\nPerhaps try --help?",
								argument));
			}
			addGav(gavs, matcher);
		}
		return Result.success(gavs.build());
	}

	void addGav(final ImmutableList.Builder<Gav> gavs, final Matcher matcher)
	{
		final String groupId = matcher.group(1);
		final String artifactId = matcher.group(2);
		final String version = matcher.group(3);
		gavs.add(
				this.gavBuilder.build(
						this.groupArtifactBuilder.build(
								groupId,
								artifactId),
						version));
	}
}
