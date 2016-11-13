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

import static com.github.hilcode.versionator.Generator.randomGroupId;
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
 * The unit tests for {@code GroupId}.
 */
public final class GroupIdTest
{
	private String value;

	private GroupId groupId;

	@Before
	public void setUp()
	{
		final Random rnd = new Random();
		this.groupId = randomGroupId(rnd);
		this.value = this.groupId.value;
	}

	@Test
	public final void each_GroupId_must_have_a_non_null_value()
	{
		try
		{
			new GroupId(null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'value'.", e.getMessage());
		}
	}

	@Test
	public final void each_GroupId_must_have_a_non_empty_value()
	{
		try
		{
			new GroupId("");
			fail("Expected a IllegalArgumentException.");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("Empty 'value'.", e.getMessage());
		}
	}

	@Test
	public final void each_GroupId_must_have_a_non_null_value_after_trimming()
	{
		try
		{
			new GroupId("  \t \t \n \r \f  ");
			fail("Expected a IllegalArgumentException.");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("Empty 'value'.", e.getMessage());
		}
	}

	@Test
	public final void the_textual_version_of_an_GroupId_is_simply_its_value()
	{
		assertSame(this.value, this.groupId.toText());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_1()
	{
		assertEquals(this.groupId.hashCode(), this.groupId.hashCode());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_2()
	{
		final GroupId otherGroupId = new GroupId(this.value);
		assertEquals(this.groupId.hashCode(), otherGroupId.hashCode());
	}

	@Test
	public final void the_equals_implementation_works_as_expected_1()
	{
		assertFalse(this.groupId.equals(null));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_2()
	{
		assertTrue(this.groupId.equals(this.groupId));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_3()
	{
		final GroupId groupId_ = new GroupId(this.value);
		assertTrue(this.groupId.equals(groupId_));
		assertTrue(groupId_.equals(this.groupId));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_4()
	{
		final GroupId otherGroupId = new GroupId("Some Other Value");
		assertFalse(this.groupId.equals(otherGroupId));
		assertFalse(otherGroupId.equals(this.groupId));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_5()
	{
		assertFalse(this.groupId.equals(new Object()));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_1()
	{
		assertEquals(0, this.groupId.compareTo(this.groupId));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_2()
	{
		final GroupId groupId_ = new GroupId(this.value);
		assertEquals(0, this.groupId.compareTo(groupId_));
		assertEquals(0, groupId_.compareTo(this.groupId));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_3()
	{
		final GroupId otherGroupId = new GroupId("Abc");
		assertEquals(1, this.groupId.compareTo(otherGroupId));
		assertEquals(-1, otherGroupId.compareTo(this.groupId));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_4()
	{
		assertEquals(1, this.groupId.compareTo(GroupId.NONE));
		assertEquals(0, GroupId.NONE.compareTo(GroupId.NONE));
		assertEquals(-1, GroupId.NONE.compareTo(this.groupId));
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_1()
	{
		assertEquals(String.format("(GroupId value='%s')", this.value), this.groupId.toString());
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_2()
	{
		assertEquals("(GroupId NONE)", GroupId.NONE.toString());
	}

	@Test
	public final void interning_of_GroupIds_works_as_expected_1()
	{
		final GroupId groupId_ = GroupId.BUILDER.build("  " + this.value + "  ");
		assertSame(this.groupId, groupId_);
	}

	@Test
	public final void interning_of_GroupIds_works_as_expected_2()
	{
		final GroupId otherGroupId = new GroupId("Some Other Value");
		assertNotSame(this.groupId, otherGroupId);
	}
}
