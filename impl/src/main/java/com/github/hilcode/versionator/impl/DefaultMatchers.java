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

import java.util.regex.Matcher;
import com.github.hilcode.versionator.api.Matchers;
import com.github.hilcode.versionator.maven.Gav;
import com.github.hilcode.versionator.maven.GroupArtifact;
import com.google.common.base.Preconditions;

public final class DefaultMatchers
	implements
		Matchers
{
	private final Gav.Builder gavBuilder;

	private final GroupArtifact.Builder groupArtifactBuilder;

	public DefaultMatchers(final Gav.Builder gavBuilder, final GroupArtifact.Builder groupArtifactBuilder)
	{
		this.gavBuilder = gavBuilder;
		this.groupArtifactBuilder = groupArtifactBuilder;
	}

	@Override
	public GroupArtifact toGroupArtifact(final Matcher matcher)
	{
		Preconditions.checkNotNull(matcher, "Missing 'matcher'.");
		Preconditions.checkArgument(matcher.groupCount() >= 2, "Invalid Matcher.");
		return this.groupArtifactBuilder.build(toGroupId(matcher), toArtifactId(matcher));
	}

	@Override
	public Gav toGav(final Matcher matcher)
	{
		Preconditions.checkNotNull(matcher, "Missing 'matcher'.");
		Preconditions.checkArgument(matcher.groupCount() == 3, "Invalid Matcher.");
		return this.gavBuilder.build(toGroupArtifact(matcher), toVersion(matcher));
	}

	String toGroupId(final Matcher matcher)
	{
		return matcher.group(1);
	}

	String toArtifactId(final Matcher matcher)
	{
		return matcher.group(2);
	}

	String toVersion(final Matcher matcher)
	{
		return matcher.group(3);
	}
}
