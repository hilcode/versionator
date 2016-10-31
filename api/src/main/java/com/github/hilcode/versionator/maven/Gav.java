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
package com.github.hilcode.versionator.maven;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

public final class Gav
	implements
		Comparable<Gav>
{
	public interface Builder
	{
		Gav build(GroupArtifact groupArtifact, String version);

		Gav build(String gav);
	}

	public static final Builder BUILDER = new Builder()
	{
		@Override
		public Gav build(final GroupArtifact groupArtifact, final String version)
		{
			return new Gav(groupArtifact, version);
		}

		@Override
		public Gav build(final String gav)
		{
			Preconditions.checkNotNull(gav, "Missing 'gav'.");
			final int firstColonIndex = gav.indexOf(':');
			Preconditions.checkState(firstColonIndex > -1, "Invalid format: expected '*:*:*' but found '*'.");
			final int secondColonIndex = gav.indexOf(':', firstColonIndex + 1);
			Preconditions.checkState(secondColonIndex > -1, "Invalid format: expected '*:*:*' but found '*:*'.");
			final String groupId = gav.substring(0, firstColonIndex);
			final String artifactId = gav.substring(firstColonIndex + 1, secondColonIndex);
			final String version = gav.substring(secondColonIndex + 1);
			return new Gav(new GroupArtifact(groupId, artifactId), version);
		}
	};

	public final GroupArtifact groupArtifact;

	public final String version;

	public Gav(final GroupArtifact groupArtifact, final String version)
	{
		Preconditions.checkNotNull(groupArtifact, "Missing 'groupArtifact'.");
		Preconditions.checkNotNull(version, "Missing 'version'.");
		this.groupArtifact = groupArtifact;
		this.version = version;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + this.groupArtifact.hashCode();
		result = prime * result + this.version.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object object)
	{
		if (this == object)
		{
			return true;
		}
		if (object == null || getClass() != object.getClass())
		{
			return false;
		}
		final Gav other = (Gav) object;
		return compareTo(other) == 0;
	}

	@Override
	public int compareTo(final Gav other)
	{
		return ComparisonChain
				.start()
				.compare(this.groupArtifact, other.groupArtifact)
				.compare(this.version.toLowerCase(), other.version.toLowerCase())
				.result();
	}

	public String toText()
	{
		return this.groupArtifact.toText() + ":" + this.version;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("(Gav");
		builder.append(" groupArtifact=").append(this.groupArtifact);
		builder.append(" version='").append(this.version).append("'");
		builder.append(")");
		return builder.toString();
	}
}
