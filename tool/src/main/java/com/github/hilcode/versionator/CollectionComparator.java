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
import java.util.Comparator;
import java.util.Iterator;
import com.google.common.collect.ComparisonChain;

public final class CollectionComparator<T extends Comparable<T>>
	implements
		Comparator<Collection<T>>
{
	@SuppressWarnings("rawtypes")
	private static final Comparator INSTANCE = new CollectionComparator();

	public static final <T extends Comparable<T>> Comparator<Collection<T>> instance()
	{
		@SuppressWarnings("unchecked")
		final Comparator<Collection<T>> instance = INSTANCE;
		return instance;
	}

	CollectionComparator()
	{
		// Empty.
	}

	@Override
	public int compare(final Collection<T> as, final Collection<T> bs)
	{
		if (as == bs)
		{
			return 0;
		}
		else
		{
			if (as == null)
			{
				return -1;
			}
			else if (bs == null)
			{
				return 1;
			}
			else
			{
				if (as.size() != bs.size())
				{
					return as.size() > bs.size() ? 1 : -1;
				}
				else
				{
					final Iterator<T> aIt = as.iterator();
					final Iterator<T> bIt = bs.iterator();
					while (aIt.hasNext())
					{
						final int comparison = ComparisonChain
								.start()
								.compare(aIt.next(), bIt.next())
								.result();
						if (comparison != 0)
						{
							return comparison;
						}
					}
					return 0;
				}
			}
		}
	}
}
