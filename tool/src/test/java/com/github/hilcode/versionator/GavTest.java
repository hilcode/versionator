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

import static com.github.hilcode.versionator.Generator.randomGav;
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
 * The unit tests for {@code Gav}.
 */
public final class GavTest
{
	private GroupArtifact groupArtifact;

	private Version version;

	private Gav gav;

	@Before
	public void setUp()
	{
		final Random rnd = new Random();
		this.gav = randomGav(rnd);
		this.groupArtifact = this.gav.groupArtifact;
		this.version = this.gav.version;
	}

	@Test
	public final void each_Gav_must_have_a_non_null_groupArtifact()
	{
		try
		{
			new Gav(null, this.version);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'groupArtifact'.", e.getMessage());
		}
	}

	@Test
	public final void each_Gav_must_have_a_non_null_version()
	{
		try
		{
			new Gav(this.groupArtifact, null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'version'.", e.getMessage());
		}
	}

	@Test
	public final void the_textual_version_of_an_Gav_is_the_concatenation_of_its_GroupId_and_ArtifactId()
	{
		assertEquals(this.groupArtifact.toText() + ":" + this.version.toText(), this.gav.toText());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_1()
	{
		assertEquals(this.gav.hashCode(), this.gav.hashCode());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_2()
	{
		final Gav otherGav = new Gav(this.groupArtifact, this.version);
		assertEquals(this.gav.hashCode(), otherGav.hashCode());
	}

	@Test
	public final void the_equals_implementation_works_as_expected_1()
	{
		assertFalse(this.gav.equals(null));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_2()
	{
		assertTrue(this.gav.equals(this.gav));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_3()
	{
		final Gav gav_ = new Gav(this.groupArtifact, this.version);
		assertTrue(this.gav.equals(gav_));
		assertTrue(gav_.equals(this.gav));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_4()
	{
		final Gav otherGav = new Gav(GroupArtifact.BUILDER.build("abc:abc"), this.version);
		assertFalse(this.gav.equals(otherGav));
		assertFalse(otherGav.equals(this.gav));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_5()
	{
		final Gav otherGav = new Gav(this.groupArtifact, Version.BUILDER.build("0.1-SNAPSHOT"));
		assertFalse(this.gav.equals(otherGav));
		assertFalse(otherGav.equals(this.gav));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_6()
	{
		assertFalse(this.gav.equals(new Object()));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_1()
	{
		assertEquals(0, this.gav.compareTo(this.gav));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_2()
	{
		final Gav gav_ = new Gav(this.groupArtifact, this.version);
		assertEquals(0, this.gav.compareTo(gav_));
		assertEquals(0, gav_.compareTo(this.gav));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_3()
	{
		final Gav otherGav = new Gav(GroupArtifact.BUILDER.build("abc:abc"), this.version);
		assertEquals(1, this.gav.compareTo(otherGav));
		assertEquals(-1, otherGav.compareTo(this.gav));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_4()
	{
		final Gav otherGav = new Gav(this.groupArtifact, Version.BUILDER.build("0.1-SNAPSHOT"));
		assertEquals(1, this.gav.compareTo(otherGav));
		assertEquals(-1, otherGav.compareTo(this.gav));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_5()
	{
		assertEquals(1, this.gav.compareTo(Gav.NONE));
		assertEquals(0, Gav.NONE.compareTo(Gav.NONE));
		assertEquals(-1, Gav.NONE.compareTo(this.gav));
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_1()
	{
		assertEquals(
				String.format("(Gav groupArtifact=%s version=%s)", this.groupArtifact, this.version),
				this.gav.toString());
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_2()
	{
		assertEquals("(Gav NONE)", Gav.NONE.toString());
	}

	@Test
	public final void interning_of_Gavs_works_as_expected_1()
	{
		final Gav gav_ = Gav.BUILDER.build(this.groupArtifact, this.version);
		assertSame(this.gav, gav_);
	}

	@Test
	public final void interning_of_Gavs_works_as_expected_2()
	{
		final Gav otherGav = new Gav(GroupArtifact.BUILDER.build("abc:abc"), this.version);
		assertNotSame(this.gav, otherGav);
	}

	@Test
	public final void interning_of_Gavs_works_as_expected_3()
	{
		final Gav otherGav = new Gav(this.groupArtifact, Version.BUILDER.build("0.1-SNAPSHOT"));
		assertNotSame(this.gav, otherGav);
	}

	@Test
	public final void each_Gav_must_have_a_GroupId_an_ArtifactId_and_a_Version_1()
	{
		try
		{
			Gav.BUILDER.build("abc");
			fail("Expected an IllegalStateException.");
		}
		catch (final IllegalStateException e)
		{
			assertEquals("Invalid format: expected '*:*:*' but found '*'.", e.getMessage());
		}
	}

	@Test
	public final void each_Gav_must_have_a_GroupId_an_ArtifactId_and_a_Version_2()
	{
		try
		{
			Gav.BUILDER.build("abc:abc");
			fail("Expected an IllegalStateException.");
		}
		catch (final IllegalStateException e)
		{
			assertEquals("Invalid format: expected '*:*:*' but found '*:*'.", e.getMessage());
		}
	}

	@Test
	public final void each_Gav_must_have_a_GroupId_an_ArtifactId_and_a_Version_3()
	{
		final Gav gav_ = Gav.BUILDER.build("abc:xyz:1.2.3-SNAPSHOT");
		gav_.groupArtifact.groupId.value.equals("abc");
		gav_.groupArtifact.artifactId.value.equals("xyz");
		gav_.version.toText().equals("1.2.3-SNAPSHOT");
	}
}
