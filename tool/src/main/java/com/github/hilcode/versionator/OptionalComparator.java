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

import java.util.Comparator;
import com.google.common.base.Optional;
import com.google.common.collect.ComparisonChain;

public final class OptionalComparator<T extends Comparable<T>>
	implements
		Comparator<Optional<T>>
{
	@SuppressWarnings("rawtypes")
	private static final Comparator INSTANCE = new OptionalComparator();

	public static final <T extends Comparable<T>> Comparator<Optional<T>> instance()
	{
		@SuppressWarnings("unchecked")
		final Comparator<Optional<T>> instance = INSTANCE;
		return instance;
	}

	@Override
	public int compare(final Optional<T> a, final Optional<T> b)
	{
		if (a == b)
		{
			return 0;
		}
		else
		{
			if (a == null)
			{
				return -1;
			}
			else if (b == null)
			{
				return 1;
			}
			else
			{
				if (a.isPresent() == b.isPresent())
				{
					return ComparisonChain
							.start()
							.compare(a.get(), b.get())
							.result();
				}
				else
				{
					return a.isPresent() ? 1 : -1;
				}
			}
		}
	}
}
