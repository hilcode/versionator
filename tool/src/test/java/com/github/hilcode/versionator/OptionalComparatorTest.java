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
import org.junit.Before;
import org.junit.Test;
import com.google.common.base.Optional;

/**
 * The unit tests for {@code OptionalComparator}.
 */
public final class OptionalComparatorTest
{
	private Optional<Integer> maybe;

	private OptionalComparator<Integer> comparator;

	@Before
	public void setUp()
	{
		this.maybe = Optional.of(Integer.valueOf(1));
		this.comparator = new OptionalComparator<>();
	}

	@Test
	public final void the_OptionalComparator_instance_is_always_the_same()
	{
		assertSame(OptionalComparator.instance(), OptionalComparator.instance());
	}

	@Test
	public final void comparing_an_Optional_to_itself_works_as_expected()
	{
		assertEquals(0, this.comparator.compare(this.maybe, this.maybe));
	}

	@Test
	public final void comparing_an_Optional_to_null_works_as_expected_1()
	{
		assertEquals(-1, this.comparator.compare(null, this.maybe));
	}

	@Test
	public final void comparing_an_Optional_to_null_works_as_expected_2()
	{
		assertEquals(1, this.comparator.compare(this.maybe, null));
	}

	@Test
	public final void comparing_an_Optional_to_absent_works_as_expected_1()
	{
		assertEquals(-1, this.comparator.compare(Optional.<Integer> absent(), this.maybe));
	}

	@Test
	public final void comparing_an_Optional_to_absent_works_as_expected_2()
	{
		assertEquals(1, this.comparator.compare(this.maybe, Optional.<Integer> absent()));
	}

	@Test
	public final void comparing_an_Optional_to_absent_works_as_expected_3()
	{
		assertEquals(0, this.comparator.compare(Optional.<Integer> absent(), Optional.<Integer> absent()));
	}

	@Test
	public final void comparing_an_Optional_to_an_other_Optional_works_as_expected_1()
	{
		final Optional<Integer> otherMaybe = Optional.of(Integer.valueOf(2));
		assertEquals(-1, this.comparator.compare(this.maybe, otherMaybe));
		assertEquals(1, this.comparator.compare(otherMaybe, this.maybe));
	}

	@Test
	public final void comparing_an_Optional_to_an_other_Optional_works_as_expected()
	{
		final Optional<Integer> otherMaybe = Optional.of(Integer.valueOf(1));
		assertEquals(0, this.comparator.compare(this.maybe, otherMaybe));
		assertEquals(0, this.comparator.compare(otherMaybe, this.maybe));
	}
}
