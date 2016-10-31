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

import com.github.hilcode.versionator.api.FlagExtractor;
import com.github.hilcode.versionator.api.Flags;
import com.github.hilcode.versionator.api.ReleaseRequest;
import com.github.hilcode.versionator.api.ReleaseRequestExtractor;
import com.github.hilcode.versionator.api.RequestExtractor;
import com.github.hilcode.versionator.api.VersionChangeRequest;
import com.github.hilcode.versionator.api.VersionChangeRequestExtractor;
import com.github.hilcode.versionator.misc.Either;
import com.github.hilcode.versionator.misc.Result;
import com.google.common.collect.ImmutableList;

public final class DefaultRequestExtractor
	implements
		RequestExtractor
{
	private final FlagExtractor flagExtractor;

	private final VersionChangeRequestExtractor versionChangeRequestExtractor;

	private final ReleaseRequestExtractor releaseRequestExtractor;

	public DefaultRequestExtractor(
			final FlagExtractor flagExtractor,
			final VersionChangeRequestExtractor versionChangeRequestExtractor,
			final ReleaseRequestExtractor releaseRequestExtractor)
	{
		this.flagExtractor = flagExtractor;
		this.versionChangeRequestExtractor = versionChangeRequestExtractor;
		this.releaseRequestExtractor = releaseRequestExtractor;
	}

	@Override
	public final Result<String, Either<VersionChangeRequest, ReleaseRequest>> extract(
			final ImmutableList<String> arguments)
	{
		final Result<String, Flags> result = this.flagExtractor.extract(arguments);
		if (result.isFailure())
		{
			return Result.asFailure(result);
		}
		final Flags flags = result.success();
		switch (flags.command)
		{
			case SET_VERSION:
			{
				return extractSetVersionCommand(flags);
			}
			case RELEASE:
			default:
			{
				return extractReleaseCommand(flags);
			}
		}
	}

	Result<String, Either<VersionChangeRequest, ReleaseRequest>> extractSetVersionCommand(final Flags flags)
	{
		final Result<String, VersionChangeRequest> versionChangeRequest =
				this.versionChangeRequestExtractor.extract(flags);
		if (versionChangeRequest.isFailure())
		{
			return Result.asFailure(versionChangeRequest);
		}
		else
		{
			final Either<VersionChangeRequest, ReleaseRequest> request =
					Either.left(versionChangeRequest.success());
			return Result.success(request);
		}
	}

	Result<String, Either<VersionChangeRequest, ReleaseRequest>> extractReleaseCommand(final Flags flags)
	{
		final Result<String, ReleaseRequest> releaseRequest =
				this.releaseRequestExtractor.extract(flags);
		if (releaseRequest.isFailure())
		{
			return Result.asFailure(releaseRequest);
		}
		else
		{
			final Either<VersionChangeRequest, ReleaseRequest> request =
					Either.right(releaseRequest.success());
			return Result.success(request);
		}
	}
}
