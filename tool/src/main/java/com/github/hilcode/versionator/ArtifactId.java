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

public final class ArtifactId
	implements
		Comparable<ArtifactId>
{
	private final String value;

	private ArtifactId(final String value)
	{
		Preconditions.checkNotNull(value, "Missing 'value'.");
		final String value_ = value.trim();
		Preconditions.checkArgument(value_.length() > 0, "Empty 'value'.");
		this.value = value_;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + this.value.hashCode();
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
		final ArtifactId other = (ArtifactId) object;
		return compareTo(other) == 0;
	}

	@Override
	public int compareTo(final ArtifactId other)
	{
		return ComparisonChain
				.start()
				.compare(this.value, other.value)
				.result();
	}

	public String toText()
	{
		return this.value;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("(ArtifactId");
		builder.append(" value='").append(this.value).append("'");
		builder.append(")");
		return builder.toString();
	}

	public interface Builder
	{
		ArtifactId build(String value);
	}

	public static final Builder BUILDER = new Builder()
	{
		private final Interner<ArtifactId> interner = Interners.newWeakInterner();

		@Override
		public ArtifactId build(final String value)
		{
			return this.interner.intern(new ArtifactId(value));
		}
	};
}
