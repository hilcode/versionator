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

public final class Dependency
	implements
		Comparable<Dependency>
{
	public final Gav gav;

	private Dependency(final Gav gav)
	{
		Preconditions.checkNotNull(gav, "Missing 'gav'.");
		this.gav = gav;
	}

	public Dependency apply(final Gav newGav)
	{
		Preconditions.checkNotNull(newGav, "Missing 'newGav'.");
		return BUILDER.build(newGav);
	}

	@Override
	public int compareTo(final Dependency other)
	{
		return ComparisonChain
				.start()
				.compare(this.gav, other.gav)
				.result();
	}

	@Override
	public String toString()
	{
		return "(Dependency gav=" + this.gav + ")";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + this.gav.hashCode();
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
		final Dependency other = (Dependency) object;
		return compareTo(other) == 0;
	}

	public interface Builder
	{
		Dependency build(Gav gav);
	}

	public static final Builder BUILDER = new Builder()
	{
		private final Interner<Dependency> interner = Interners.newWeakInterner();

		@Override
		public Dependency build(final Gav gav)
		{
			return this.interner.intern(new Dependency(gav));
		}
	};
}
