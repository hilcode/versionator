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

import java.util.Collection;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public final class Zipper<T>
	implements
		Iterable<Tuple._2<T, T>>
{
	private final Collection<T> a;

	private final Collection<T> b;

	private Zipper(final Collection<T> a, final Collection<T> b)
	{
		Preconditions.checkNotNull(a, "Missing 'a'.");
		Preconditions.checkNotNull(b, "Missing 'b'.");
		this.a = a;
		this.b = b;
	}

	@Override
	public Iterator<Tuple._2<T, T>> iterator()
	{
		final ImmutableList.Builder<Tuple._2<T, T>> zipper = ImmutableList.builder();
		final Iterator<T> as = this.a.iterator();
		final Iterator<T> bs = this.b.iterator();
		while (as.hasNext() && bs.hasNext())
		{
			final Tuple._2<T, T> tuple = new Tuple._2<>(as.next(), bs.next());
			zipper.add(tuple);
		}
		return zipper.build().iterator();
	}

	public interface Builder
	{
		<T> Zipper<T> zip(Collection<T> a, Collection<T> b);
	}

	public static final Zipper.Builder BUILDER = new Builder()
	{
		@Override
		public <T> Zipper<T> zip(final Collection<T> a, final Collection<T> b)
		{
			return new Zipper<>(a, b);
		}
	};
}
