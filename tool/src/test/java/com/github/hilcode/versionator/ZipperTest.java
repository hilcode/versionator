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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Iterator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.google.common.collect.Lists;

/**
 * The unit tests for {@code Zipper}.
 */
public final class ZipperTest
{
	private List<Integer> integers;

	private List<String> strings;

	private Zipper<Integer, String> zipper;

	@Before
	public void setUp()
	{
		this.integers = Lists.newArrayList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4));
		this.strings = Lists.newLinkedList(Lists.newArrayList("one", "two", "three"));
		this.zipper = new Zipper<>(this.integers, this.strings);
	}

	@Test
	public final void test_1()
	{
		try
		{
			new Zipper<Integer, String>(null, this.strings);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'a'.", e.getMessage());
		}
	}

	@Test
	public final void test_2()
	{
		try
		{
			new Zipper<Integer, String>(this.integers, null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'b'.", e.getMessage());
		}
	}

	@Test
	public final void test_3()
	{
		assertSame(this.integers, this.zipper.a);
		assertSame(this.strings, this.zipper.b);
	}

	@Test
	public final void test_4()
	{
		final Iterator<Tuple.Duo<Integer, String>> iterator = this.zipper.iterator();
		assertTrue(iterator.hasNext());
		final Tuple.Duo<Integer, String> duo_1 = iterator.next();
		assertSame(this.integers.get(0), duo_1._1);
		assertEquals(this.strings.get(0), duo_1._2);
		final Tuple.Duo<Integer, String> duo_2 = iterator.next();
		assertSame(this.integers.get(1), duo_2._1);
		assertEquals(this.strings.get(1), duo_2._2);
		final Tuple.Duo<Integer, String> duo_3 = iterator.next();
		assertSame(this.integers.get(2), duo_3._1);
		assertEquals(this.strings.get(2), duo_3._2);
		assertFalse(iterator.hasNext());
	}

	@Test
	public final void test_5()
	{
		final Iterator<Tuple.Duo<String, Integer>> iterator =
				Zipper.BUILDER.zip(this.strings, this.integers).iterator();
		assertTrue(iterator.hasNext());
		final Tuple.Duo<String, Integer> duo_1 = iterator.next();
		assertEquals(this.strings.get(0), duo_1._1);
		assertSame(this.integers.get(0), duo_1._2);
		final Tuple.Duo<String, Integer> duo_2 = iterator.next();
		assertEquals(this.strings.get(1), duo_2._1);
		assertSame(this.integers.get(1), duo_2._2);
		final Tuple.Duo<String, Integer> duo_3 = iterator.next();
		assertEquals(this.strings.get(2), duo_3._1);
		assertSame(this.integers.get(2), duo_3._2);
		assertFalse(iterator.hasNext());
	}

	@Test
	public final void test_10()
	{
		final Zipper<Integer, String> zipper_ = Zipper.BUILDER.zip(this.integers, this.strings);
		assertNotNull(zipper_);
		assertSame(this.integers, zipper_.a);
		assertSame(this.strings, zipper_.b);
	}
}
