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
package com.github.hilcode.versionator;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

public final class GroupArtifact
	implements
		Comparable<GroupArtifact>
{
	public static final GroupArtifact NONE = new GroupArtifact();

	public final GroupId groupId;

	public final ArtifactId artifactId;

	private GroupArtifact()
	{
		this(GroupId.NONE, ArtifactId.NONE);
	}

	GroupArtifact(final GroupId groupId, final ArtifactId artifactId)
	{
		Preconditions.checkNotNull(groupId, "Missing 'groupId'.");
		Preconditions.checkNotNull(artifactId, "Missing 'artifactId'.");
		this.groupId = groupId;
		this.artifactId = artifactId;
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
		if (this == NONE)
		{
			return other == NONE ? 0 : -1;
		}
		else
		{
			return ComparisonChain
					.start()
					.compare(this.groupId, other.groupId)
					.compare(this.artifactId, other.artifactId)
					.result();
		}
	}

	public String toText()
	{
		return this.groupId.toText() + ":" + this.artifactId.toText();
	}

	@Override
	public String toString()
	{
		if (this == NONE)
		{
			return "(GroupArtifact NONE)";
		}
		else
		{
			final StringBuilder builder = new StringBuilder();
			builder.append("(GroupArtifact");
			builder.append(" groupId=").append(this.groupId);
			builder.append(" artifactId=").append(this.artifactId);
			builder.append(")");
			return builder.toString();
		}
	}

	public interface Builder
	{
		GroupArtifact build(GroupId groupId, ArtifactId artifactId);

		GroupArtifact build(String groupArtifact);
	}

	public static final Builder BUILDER = new Builder()
	{
		private final Interner<GroupArtifact> interner = Interners.newWeakInterner();

		@Override
		public GroupArtifact build(final GroupId groupId, final ArtifactId artifactId)
		{
			return this.interner.intern(new GroupArtifact(groupId, artifactId));
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
			return build(GroupId.BUILDER.build(groupId), ArtifactId.BUILDER.build(artifactId));
		}
	};
}
