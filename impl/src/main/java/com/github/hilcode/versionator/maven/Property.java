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

public final class Property
	implements
		Comparable<Property>
{
	public final String key;
	public final String value;

	public Property(final String key, final String value)
	{
		Preconditions.checkNotNull(key, "Missing 'key'.");
		Preconditions.checkArgument(key.length() > 0, "Empty 'key'.");
		Preconditions.checkNotNull(value, "Missing 'value'.");
		this.key = key;
		this.value = value;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + this.key.hashCode();
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
		final Property other = (Property) object;
		return compareTo(other) == 0;
	}

	@Override
	public int compareTo(final Property other)
	{
		return ComparisonChain
				.start()
				.compare(this.key, other.key)
				.compare(this.value, other.value)
				.result();
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("(Property");
		builder.append(" key='").append(this.key).append("'");
		builder.append(" value='").append(this.value).append("'");
		builder.append(")");
		return builder.toString();
	}
}
