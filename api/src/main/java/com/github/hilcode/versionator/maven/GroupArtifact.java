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

public final class GroupArtifact
	implements
		Comparable<GroupArtifact>
{
	public interface Builder
	{
		GroupArtifact build(String groupId, String artifactId);

		GroupArtifact build(String groupArtifact);
	}

	public static final Builder BUILDER = new Builder()
	{
		@Override
		public GroupArtifact build(final String groupId, final String artifactId)
		{
			return new GroupArtifact(groupId, artifactId);
		}

		@Override
		public GroupArtifact build(final String groupArtifact)
		{
			Preconditions.checkNotNull(groupArtifact, "Missing 'groupArtifact'.");
			final int firstColonIndex = groupArtifact.indexOf(':');
			Preconditions.checkState(firstColonIndex > -1, "Invalid format: expected '*:*' but found '*'.");
			final int secondColonIndex = groupArtifact.indexOf(':', firstColonIndex + 1);
			Preconditions.checkState(secondColonIndex == -1, "Invalid format: expected '*:*' but found '*:*:*'.");
			final String groupId = groupArtifact.substring(0, firstColonIndex);
			final String artifactId = groupArtifact.substring(firstColonIndex + 1);
			return new GroupArtifact(groupId, artifactId);
		}
	};

	public final String groupId;

	public final String artifactId;

	public GroupArtifact(final String groupId, final String artifactId)
	{
		Preconditions.checkNotNull(groupId, "Missing 'groupId'.");
		Preconditions.checkArgument(groupId.length() > 0, "Empty 'groupId'.");
		Preconditions.checkNotNull(artifactId, "Missing 'artifactId'.");
		Preconditions.checkArgument(artifactId.length() > 0, "Empty 'artifactId'.");
		this.groupId = groupId.trim();
		this.artifactId = artifactId.trim();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + this.groupId.hashCode();
		result = prime * result + this.artifactId.hashCode();
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
		final GroupArtifact other = (GroupArtifact) object;
		return compareTo(other) == 0;
	}

	@Override
	public int compareTo(final GroupArtifact other)
	{
		return ComparisonChain
				.start()
				.compare(this.groupId.toLowerCase(), other.groupId.toLowerCase())
				.compare(this.artifactId.toLowerCase(), other.artifactId.toLowerCase())
				.result();
	}

	public String toText()
	{
		return this.groupId + ":" + this.artifactId;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("(GroupArtifact");
		builder.append(" groupId='").append(this.groupId).append("'");
		builder.append(" artifactId='").append(this.artifactId).append("'");
		builder.append(")");
		return builder.toString();
	}
}
