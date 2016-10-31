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

public final class FromOldToNewVersion
{
	public final GroupArtifact groupArtifact;

	public final String oldVersion;

	public final String newVersion;

	public FromOldToNewVersion(final GroupArtifact groupArtifact, final String oldVersion, final String newVersion)
	{
		this.groupArtifact = groupArtifact;
		this.oldVersion = oldVersion;
		this.newVersion = newVersion;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("(FromOldToNewVersion");
		builder.append(" groupArtifact=").append(this.groupArtifact);
		builder.append(" oldVersion='").append(this.oldVersion).append("'");
		builder.append(" newVersion='").append(this.newVersion).append("'");
		builder.append(")");
		return builder.toString();
	}
}
