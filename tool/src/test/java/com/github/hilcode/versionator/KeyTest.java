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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code Key}.
 */
public final class KeyTest
{
	private Key key;

	@Before
	public void setUp()
	{
		final Random rnd = new Random();
		this.key = Generator.randomKey(rnd);
	}

	@Test
	public final void each_Key_must_have_a_non_null_value()
	{
		try
		{
			new Key(null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'value'.", e.getMessage());
		}
	}

	@Test
	public final void each_Key_must_have_a_non_empty_value()
	{
		try
		{
			new Key("");
			fail("Expected a IllegalArgumentException.");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("Empty 'value'.", e.getMessage());
		}
	}

	@Test
	public final void each_Key_must_have_a_non_null_value_after_trimming()
	{
		try
		{
			new Key("  \t \t \n \r \f  ");
			fail("Expected a IllegalArgumentException.");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("Empty 'value'.", e.getMessage());
		}
	}

	@Test
	public final void the_textual_version_of_an_Key_is_simply_its_value()
	{
		assertSame(this.key.value, this.key.toText());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_1()
	{
		assertEquals(this.key.hashCode(), this.key.hashCode());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_2()
	{
		final Key otherKey = new Key(this.key.value);
		assertEquals(this.key.hashCode(), otherKey.hashCode());
	}

	@Test
	public final void the_equals_implementation_works_as_expected_1()
	{
		assertFalse(this.key.equals(null));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_2()
	{
		assertTrue(this.key.equals(this.key));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_3()
	{
		final Key key_ = new Key(this.key.value);
		assertTrue(this.key.equals(key_));
		assertTrue(key_.equals(this.key));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_4()
	{
		final Key otherKey = new Key("Some Other Value");
		assertFalse(this.key.equals(otherKey));
		assertFalse(otherKey.equals(this.key));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_5()
	{
		assertFalse(this.key.equals(new Object()));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_1()
	{
		assertEquals(0, this.key.compareTo(this.key));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_2()
	{
		final Key key_ = new Key(this.key.value);
		assertEquals(0, this.key.compareTo(key_));
		assertEquals(0, key_.compareTo(this.key));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_3()
	{
		final Key otherKey = new Key("Abc");
		assertEquals(1, this.key.compareTo(otherKey));
		assertEquals(-1, otherKey.compareTo(this.key));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_4()
	{
		assertEquals(1, this.key.compareTo(Key.NONE));
		assertEquals(0, Key.NONE.compareTo(Key.NONE));
		assertEquals(-1, Key.NONE.compareTo(this.key));
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_1()
	{
		assertEquals(String.format("(Key value='%s')", this.key.value), this.key.toString());
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_2()
	{
		assertEquals("(Key NONE)", Key.NONE.toString());
	}

	@Test
	public final void interning_of_Keys_works_as_expected_1()
	{
		final Key key_ = Key.BUILDER.build("  " + this.key.value + "  ");
		assertSame(this.key, key_);
	}

	@Test
	public final void interning_of_Keys_works_as_expected_2()
	{
		final Key otherKey = new Key("Some Other Value");
		assertNotSame(this.key, otherKey);
	}
}
