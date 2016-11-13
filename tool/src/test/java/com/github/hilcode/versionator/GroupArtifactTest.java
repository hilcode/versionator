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

import static com.github.hilcode.versionator.Generator.randomGroupArtifact;
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
 * The unit tests for {@code GroupArtifact}.
 */
public final class GroupArtifactTest
{
	private GroupId groupId;

	private ArtifactId artifactId;

	private GroupArtifact groupArtifact;

	@Before
	public void setUp()
	{
		final Random rnd = new Random();
		this.groupArtifact = randomGroupArtifact(rnd);
		this.groupId = this.groupArtifact.groupId;
		this.artifactId = this.groupArtifact.artifactId;
	}

	@Test
	public final void each_GroupArtifact_must_have_a_non_null_group_id()
	{
		try
		{
			new GroupArtifact(null, this.artifactId);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'groupId'.", e.getMessage());
		}
	}

	@Test
	public final void each_GroupArtifact_must_have_a_non_null_artifact_id()
	{
		try
		{
			new GroupArtifact(this.groupId, null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'artifactId'.", e.getMessage());
		}
	}

	@Test
	public final void the_textual_version_of_an_GroupArtifact_is_the_concatenation_of_its_GroupId_and_ArtifactId()
	{
		assertEquals(this.groupId.value + ":" + this.artifactId.value, this.groupArtifact.toText());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_1()
	{
		assertEquals(this.groupArtifact.hashCode(), this.groupArtifact.hashCode());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_2()
	{
		final GroupArtifact otherGroupArtifact = new GroupArtifact(this.groupId, this.artifactId);
		assertEquals(this.groupArtifact.hashCode(), otherGroupArtifact.hashCode());
	}

	@Test
	public final void the_equals_implementation_works_as_expected_1()
	{
		assertFalse(this.groupArtifact.equals(null));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_2()
	{
		assertTrue(this.groupArtifact.equals(this.groupArtifact));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_3()
	{
		final GroupArtifact groupArtifact_ = new GroupArtifact(this.groupId, this.artifactId);
		assertTrue(this.groupArtifact.equals(groupArtifact_));
		assertTrue(groupArtifact_.equals(this.groupArtifact));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_4()
	{
		final GroupArtifact otherGroupArtifact = new GroupArtifact(GroupId.BUILDER.build("abc"), this.artifactId);
		assertFalse(this.groupArtifact.equals(otherGroupArtifact));
		assertFalse(otherGroupArtifact.equals(this.groupArtifact));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_5()
	{
		final GroupArtifact otherGroupArtifact = new GroupArtifact(this.groupId, ArtifactId.BUILDER.build("abc"));
		assertFalse(this.groupArtifact.equals(otherGroupArtifact));
		assertFalse(otherGroupArtifact.equals(this.groupArtifact));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_6()
	{
		assertFalse(this.groupArtifact.equals(new Object()));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_1()
	{
		assertEquals(0, this.groupArtifact.compareTo(this.groupArtifact));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_2()
	{
		final GroupArtifact groupArtifact_ = new GroupArtifact(this.groupId, this.artifactId);
		assertEquals(0, this.groupArtifact.compareTo(groupArtifact_));
		assertEquals(0, groupArtifact_.compareTo(this.groupArtifact));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_3()
	{
		final GroupArtifact otherGroupArtifact = new GroupArtifact(GroupId.BUILDER.build("abc"), this.artifactId);
		assertEquals(1, this.groupArtifact.compareTo(otherGroupArtifact));
		assertEquals(-1, otherGroupArtifact.compareTo(this.groupArtifact));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_4()
	{
		final GroupArtifact otherGroupArtifact = new GroupArtifact(this.groupId, ArtifactId.BUILDER.build("abc"));
		assertEquals(1, this.groupArtifact.compareTo(otherGroupArtifact));
		assertEquals(-1, otherGroupArtifact.compareTo(this.groupArtifact));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_5()
	{
		assertEquals(1, this.groupArtifact.compareTo(GroupArtifact.NONE));
		assertEquals(0, GroupArtifact.NONE.compareTo(GroupArtifact.NONE));
		assertEquals(-1, GroupArtifact.NONE.compareTo(this.groupArtifact));
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_1()
	{
		assertEquals(
				String.format("(GroupArtifact groupId=%s artifactId=%s)", this.groupId, this.artifactId),
				this.groupArtifact.toString());
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_2()
	{
		assertEquals("(GroupArtifact NONE)", GroupArtifact.NONE.toString());
	}

	@Test
	public final void interning_of_GroupArtifacts_works_as_expected_1()
	{
		final GroupArtifact groupArtifact_ = GroupArtifact.BUILDER.build(this.groupId, this.artifactId);
		assertSame(this.groupArtifact, groupArtifact_);
	}

	@Test
	public final void interning_of_GroupArtifacts_works_as_expected_2()
	{
		final GroupArtifact otherGroupArtifact = new GroupArtifact(GroupId.BUILDER.build("abc"), this.artifactId);
		assertNotSame(this.groupArtifact, otherGroupArtifact);
	}

	@Test
	public final void interning_of_GroupArtifacts_works_as_expected_3()
	{
		final GroupArtifact otherGroupArtifact = new GroupArtifact(this.groupId, ArtifactId.BUILDER.build("abc"));
		assertNotSame(this.groupArtifact, otherGroupArtifact);
	}

	@Test
	public final void each_GroupArtifact_requires_both_a_GroupId_and_an_ArtifactId()
	{
		try
		{
			GroupArtifact.BUILDER.build("abc");
			fail("Expected an IllegalStateException.");
		}
		catch (final IllegalStateException e)
		{
			assertEquals("Invalid format: expected '*:*' but found '*'.", e.getMessage());
		}
	}

	@Test
	public final void each_GroupArtifact_requires_both_a_GroupId_and_an_ArtifactId_but_no_Version()
	{
		try
		{
			GroupArtifact.BUILDER.build("abc:abc:1.2.3");
			fail("Expected an IllegalStateException.");
		}
		catch (final IllegalStateException e)
		{
			assertEquals("Invalid format: expected '*:*' but found '*:*:*'.", e.getMessage());
		}
	}
}
