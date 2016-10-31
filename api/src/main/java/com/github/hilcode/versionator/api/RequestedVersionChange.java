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

import com.github.hilcode.versionator.maven.Gav;
import com.github.hilcode.versionator.misc.Either;
import com.google.common.collect.ImmutableList;

public final class RequestedVersionChange
{
	public final Either<FromOldToNewVersion, FromAnyToNewVersion> fromVersionToVersion;

	public final ImmutableList<Gav> gavs;

	public RequestedVersionChange(
			final Either<FromOldToNewVersion, FromAnyToNewVersion> fromVersionToVersion,
			final ImmutableList<Gav> gavs)
	{
		this.fromVersionToVersion = fromVersionToVersion;
		this.gavs = gavs;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("(RequestedVersionChange");
		builder.append(" fromVersionToVersion=").append(this.fromVersionToVersion);
		builder.append(" gavs=").append(this.gavs);
		builder.append(")");
		return builder.toString();
	}
}
