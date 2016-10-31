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

import java.io.File;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;

public final class Pom
	implements
		Comparable<Pom>
{
	public final Gav gav;

	public final File file;

	public final Type type;

	public final Optional<Pom> parent;

	public final ImmutableList<String> modules;

	public final ImmutableList<Property> properties;

	public final ImmutableList<Gav> dependencies;

	public Pom(
			final Gav gav,
			final File file,
			final Type type,
			final Optional<Pom> parent,
			final ImmutableList<String> modules,
			final ImmutableList<Property> properties,
			final ImmutableList<Gav> dependencies)
	{
		Preconditions.checkNotNull(gav, "Missing 'gav'.");
		Preconditions.checkNotNull(file, "Missing 'file'.");
		Preconditions.checkNotNull(type, "Missing 'type'.");
		Preconditions.checkNotNull(parent, "Missing 'parent'.");
		Preconditions.checkNotNull(modules, "Missing 'modules'.");
		Preconditions.checkNotNull(properties, "Missing 'properties'.");
		Preconditions.checkNotNull(dependencies, "Missing 'dependencies'.");
		this.gav = gav;
		this.file = file;
		this.type = type;
		this.parent = parent;
		this.modules = modules;
		this.properties = properties;
		this.dependencies = dependencies;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + this.gav.hashCode();
		result = prime * result + this.file.hashCode();
		result = prime * result + this.type.hashCode();
		result = prime * result + this.parent.hashCode();
		result = prime * result + this.modules.hashCode();
		result = prime * result + this.properties.hashCode();
		result = prime * result + this.dependencies.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object object)
	{
		if (this == object)
		{
			return true;
		}
		if (object == null)
		{
			return false;
		}
		if (getClass() != object.getClass())
		{
			return false;
		}
		final Pom other = (Pom) object;
		return compareTo(other) == 0;
	}

	@Override
	public int compareTo(final Pom other)
	{
		return ComparisonChain
				.start()
				.compare(this.gav, other.gav)
				.compare(this.file, other.file)
				.compare(this.type, other.type)
				.compare(this.parent, other.parent, OptionalComparator.<Pom> instance())
				.compare(this.modules, other.modules, CollectionComparator.<String> instance())
				.compare(this.properties, other.properties, CollectionComparator.<Property> instance())
				.compare(this.dependencies, other.dependencies, CollectionComparator.<Gav> instance())
				.result();
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("(Pom");
		builder.append(" gav=").append(this.gav);
		builder.append(" file='").append(this.file).append("'");
		builder.append(" type=").append(this.type);
		builder.append(" parent=").append(this.parent);
		builder.append(" modules=").append(this.modules);
		builder.append(" properties=").append(this.properties);
		builder.append(" dependencies=").append(this.dependencies);
		builder.append(")");
		return builder.toString();
	}
}
