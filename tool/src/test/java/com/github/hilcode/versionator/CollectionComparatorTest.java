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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.google.common.collect.Lists;

/**
 * The unit tests for {@code CollectionComparator}.
 */
public final class CollectionComparatorTest
{
	private List<Integer> list;

	private CollectionComparator<Integer> comparator;

	@Before
	public void setUp()
	{
		this.list = Lists.newArrayList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
		this.comparator = new CollectionComparator<>();
	}

	@Test
	public final void the_CollectionComparator_instance_is_always_the_same()
	{
		assertSame(CollectionComparator.instance(), CollectionComparator.instance());
	}

	@Test
	public final void comparing_a_collection_to_itself_works_as_expected()
	{
		assertEquals(0, this.comparator.compare(this.list, this.list));
	}

	@Test
	public final void comparing_a_collection_to_null_works_as_expected_1()
	{
		assertEquals(-1, this.comparator.compare(null, this.list));
	}

	@Test
	public final void comparing_a_collection_to_null_works_as_expected_2()
	{
		assertEquals(1, this.comparator.compare(this.list, null));
	}

	@Test
	public final void comparing_a_collection_to_a_bigger_collection_works_as_expected()
	{
		final List<Integer> otherList =
				Lists.newArrayList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4));
		assertEquals(-1, this.comparator.compare(this.list, otherList));
		assertEquals(1, this.comparator.compare(otherList, this.list));
	}

	@Test
	public final void comparing_a_collection_to_an_other_collection_works_as_expected()
	{
		final List<Integer> otherList =
				Lists.newArrayList(Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(3));
		assertEquals(-1, this.comparator.compare(this.list, otherList));
		assertEquals(1, this.comparator.compare(otherList, this.list));
	}

	@Test
	public final void comparing_collections_of_a_different_type_works_as_expected()
	{
		final List<Integer> otherList = Lists.newLinkedList(this.list);
		assertEquals(0, this.comparator.compare(this.list, otherList));
		assertEquals(0, this.comparator.compare(otherList, this.list));
	}
}
