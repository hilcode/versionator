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
import com.google.common.base.Preconditions;

public final class VersionChange
{
	public final Gav from;
	public final Gav to;

	public VersionChange(final Gav to)
	{
		Preconditions.checkNotNull(to, "Missing 'to'.");
		this.from = new Gav(to.groupArtifact, "*");
		this.to = to;
	}

	public VersionChange(final Gav from, final Gav to)
	{
		Preconditions.checkNotNull(from, "Missing 'from'.");
		Preconditions.checkNotNull(to, "Missing 'to'.");
		Preconditions.checkArgument(from.groupArtifact.equals(to.groupArtifact), "Non-matching group artifacts.");
		this.from = from;
		this.to = to;
	}
}
