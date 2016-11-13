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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

public final class Model
	implements
		Comparable<Model>
{
	public static final Model NONE = new Model();

	public final ImmutableList<Pom> poms;

	private Model()
	{
		this.poms = ImmutableList.of();
	}

	Model(final ImmutableList<Pom> poms)
	{
		Preconditions.checkNotNull(poms, "Missing 'poms'.");
		this.poms = poms;
	}

	@Override
	public int compareTo(final Model other)
	{
		if (this == NONE)
		{
			return other == NONE ? 0 : -1;
		}
		else
		{
			return ComparisonChain
					.start()
					.compare(this.poms, other.poms, CollectionComparator.instance())
					.result();
		}
	}

	@Override
	public String toString()
	{
		if (this == NONE)
		{
			return "(Model NONE)";
		}
		else
		{
			return "(Model poms=" + this.poms + ")";
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + this.poms.hashCode();
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
		final Model other = (Model) object;
		return compareTo(other) == 0;
	}

	public interface Builder
	{
		Model build(ImmutableList<Pom> poms);
	}

	public static final Builder BUILDER = new Builder()
	{
		private final Interner<Model> interner = Interners.newWeakInterner();

		@Override
		public Model build(final ImmutableList<Pom> poms)
		{
			return this.interner.intern(new Model(poms));
		}
	};
}
