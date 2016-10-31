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
import com.github.hilcode.versionator.api.RequestedVersionChange;
import com.github.hilcode.versionator.api.VersionChangeExtractor;
import com.github.hilcode.versionator.api.VersionChangeRequest;
import com.github.hilcode.versionator.api.VersionChangeRequestExtractor;
import com.github.hilcode.versionator.misc.Result;

public final class DefaultVersionChangeRequestExtractor
	implements
		VersionChangeRequestExtractor
{
	private final VersionChangeExtractor versionChangeExtractor;

	private final VersionChangeRequest.Builder versionChangeRequestBuilder;

	public DefaultVersionChangeRequestExtractor(
			final VersionChangeExtractor versionChangeExtractor,
			final VersionChangeRequest.Builder versionChangeRequestBuilder)
	{
		this.versionChangeExtractor = versionChangeExtractor;
		this.versionChangeRequestBuilder = versionChangeRequestBuilder;
	}

	@Override
	public final Result<String, VersionChangeRequest> extract(final Flags flags)
	{
		final Result<String, RequestedVersionChange> versionChange =
				this.versionChangeExtractor.extract(flags.arguments);
		if (versionChange.isFailure())
		{
			return Result.asFailure(versionChange);
		}
		else
		{
			return Result.success(
					this.versionChangeRequestBuilder.build(
							flags.runType,
							flags.mode,
							versionChange.success()));
		}
	}
}
