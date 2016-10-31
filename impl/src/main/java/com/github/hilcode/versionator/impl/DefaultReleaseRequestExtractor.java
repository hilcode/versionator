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

import com.github.hilcode.versionator.api.Flags;
import com.github.hilcode.versionator.api.ReleaseExtractor;
import com.github.hilcode.versionator.api.ReleaseRequest;
import com.github.hilcode.versionator.api.ReleaseRequestExtractor;
import com.github.hilcode.versionator.maven.GroupArtifact;
import com.github.hilcode.versionator.misc.Result;
import com.google.common.collect.ImmutableList;

public final class DefaultReleaseRequestExtractor
	implements
		ReleaseRequestExtractor
{
	private final ReleaseExtractor releaseExtractor;

	private final ReleaseRequest.Builder releaseRequestBuilder;

	public DefaultReleaseRequestExtractor(
			final ReleaseExtractor releaseExtractor,
			final ReleaseRequest.Builder releaseRequestBuilder)
	{
		this.releaseExtractor = releaseExtractor;
		this.releaseRequestBuilder = releaseRequestBuilder;
	}

	@Override
	public final Result<String, ReleaseRequest> extract(final Flags flags)
	{
		final Result<String, ImmutableList<GroupArtifact>> exclusions =
				this.releaseExtractor.extract(flags.arguments);
		if (exclusions.isFailure())
		{
			return Result.asFailure(exclusions);
		}
		else
		{
			return Result.success(
					this.releaseRequestBuilder.build(
							flags.runType,
							flags.mode,
							exclusions.success()));
		}
	}
}
