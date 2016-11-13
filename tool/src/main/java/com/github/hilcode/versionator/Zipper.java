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

public final class Zipper<A, B>
	implements
		Iterable<Tuple.Duo<A, B>>
{
	final Collection<A> a;

	final Collection<B> b;

	Zipper(final Collection<A> a, final Collection<B> b)
	{
		Preconditions.checkNotNull(a, "Missing 'a'.");
		Preconditions.checkNotNull(b, "Missing 'b'.");
		this.a = a;
		this.b = b;
	}

	@Override
	public Iterator<Tuple.Duo<A, B>> iterator()
	{
		final ImmutableList.Builder<Tuple.Duo<A, B>> zipper = ImmutableList.builder();
		final Iterator<A> as = this.a.iterator();
		final Iterator<B> bs = this.b.iterator();
		while (as.hasNext() && bs.hasNext())
		{
			final Tuple.Duo<A, B> tuple = new Tuple.Duo<>(as.next(), bs.next());
			zipper.add(tuple);
		}
		return zipper.build().iterator();
	}

	public interface Builder
	{
		<A, B> Zipper<A, B> zip(Collection<A> a, Collection<B> b);
	}

	public static final Zipper.Builder BUILDER = new Builder()
	{
		@Override
		public <A, B> Zipper<A, B> zip(final Collection<A> a, final Collection<B> b)
		{
			return new Zipper<>(a, b);
		}
	};
}
