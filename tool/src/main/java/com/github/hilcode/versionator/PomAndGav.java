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

public final class PomAndGav
	implements
		Comparable<PomAndGav>
{
	public final Pom pom;

	public final Gav gav;

	public PomAndGav(final Pom pom, final Gav gav)
	{
		Preconditions.checkNotNull(pom, "Missing 'pom'.");
		Preconditions.checkNotNull(gav, "Missing 'gav'.");
		this.pom = pom;
		this.gav = gav;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + this.pom.hashCode();
		result = prime * result + this.gav.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null || getClass() != obj.getClass())
		{
			return false;
		}
		final PomAndGav other = (PomAndGav) obj;
		return compareTo(other) == 0;
	}

	@Override
	public int compareTo(final PomAndGav other)
	{
		return ComparisonChain
				.start()
				.compare(this.pom, other.pom)
				.compare(this.gav, other.gav)
				.result();
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("(GavAndPom");
		builder.append(" pom=").append(this.pom);
		builder.append(" gav=").append(this.gav);
		builder.append(")");
		return builder.toString();
	}
}
