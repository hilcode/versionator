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
package com.github.hilcode.versionator.api;

import com.github.hilcode.versionator.maven.GroupArtifact;
import com.google.common.collect.ImmutableList;

public final class ReleaseRequest
{
	public interface Builder
	{
		ReleaseRequest build(RunType runType, Mode mode, ImmutableList<GroupArtifact> exclusions);
	}

	public static final Builder BUILDER = new Builder()
	{
		@Override
		public ReleaseRequest build(final RunType runType, final Mode mode, final ImmutableList<GroupArtifact> exclusions)
		{
			return new ReleaseRequest(runType, mode, exclusions);
		}
	};

	public final RunType runType;

	public final Mode mode;

	public final ImmutableList<GroupArtifact> exclusions;

	public ReleaseRequest(final RunType runType, final Mode mode, final ImmutableList<GroupArtifact> exclusions)
	{
		this.runType = runType;
		this.mode = mode;
		this.exclusions = exclusions;
	}
}
