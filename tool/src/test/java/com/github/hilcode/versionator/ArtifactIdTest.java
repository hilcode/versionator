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

import static com.github.hilcode.versionator.Generator.randomArtifactId;
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
 * The unit tests for {@code ArtifactId}.
 */
public final class ArtifactIdTest
{
	private String value;

	private ArtifactId artifactId;

	@Before
	public void setUp()
	{
		final Random rnd = new Random();
		this.artifactId = randomArtifactId(rnd);
		this.value = this.artifactId.value;
	}

	@Test
	public final void each_ArtifactId_must_have_a_non_null_value()
	{
		try
		{
			new ArtifactId(null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'value'.", e.getMessage());
		}
	}

	@Test
	public final void each_ArtifactId_must_have_a_non_empty_value()
	{
		try
		{
			new ArtifactId("");
			fail("Expected a IllegalArgumentException.");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("Empty 'value'.", e.getMessage());
		}
	}

	@Test
	public final void each_ArtifactId_must_have_a_non_null_value_after_trimming()
	{
		try
		{
			new ArtifactId("  \t \t \n \r \f  ");
			fail("Expected a IllegalArgumentException.");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("Empty 'value'.", e.getMessage());
		}
	}

	@Test
	public final void the_textual_version_of_an_ArtifactId_is_simply_its_value()
	{
		assertSame(this.value, this.artifactId.toText());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_1()
	{
		assertEquals(this.artifactId.hashCode(), this.artifactId.hashCode());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_2()
	{
		final ArtifactId otherArtifactId = new ArtifactId(this.value);
		assertEquals(this.artifactId.hashCode(), otherArtifactId.hashCode());
	}

	@Test
	public final void the_equals_implementation_works_as_expected_1()
	{
		assertFalse(this.artifactId.equals(null));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_2()
	{
		assertTrue(this.artifactId.equals(this.artifactId));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_3()
	{
		final ArtifactId artifactId_ = new ArtifactId(this.value);
		assertTrue(this.artifactId.equals(artifactId_));
		assertTrue(artifactId_.equals(this.artifactId));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_4()
	{
		final ArtifactId otherArtifactId = new ArtifactId("Some Other Value");
		assertFalse(this.artifactId.equals(otherArtifactId));
		assertFalse(otherArtifactId.equals(this.artifactId));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_5()
	{
		assertFalse(this.artifactId.equals(new Object()));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_1()
	{
		assertEquals(0, this.artifactId.compareTo(this.artifactId));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_2()
	{
		final ArtifactId artifactId_ = new ArtifactId(this.value);
		assertEquals(0, this.artifactId.compareTo(artifactId_));
		assertEquals(0, artifactId_.compareTo(this.artifactId));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_3()
	{
		final ArtifactId otherArtifactId = new ArtifactId("Abc");
		assertEquals(1, this.artifactId.compareTo(otherArtifactId));
		assertEquals(-1, otherArtifactId.compareTo(this.artifactId));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_4()
	{
		assertEquals(1, this.artifactId.compareTo(ArtifactId.NONE));
		assertEquals(0, ArtifactId.NONE.compareTo(ArtifactId.NONE));
		assertEquals(-1, ArtifactId.NONE.compareTo(this.artifactId));
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_1()
	{
		assertEquals(String.format("(ArtifactId value='%s')", this.value), this.artifactId.toString());
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_2()
	{
		assertEquals("(ArtifactId NONE)", ArtifactId.NONE.toString());
	}

	@Test
	public final void interning_of_ArtifactIds_works_as_expected_1()
	{
		final ArtifactId artifactId_ = ArtifactId.BUILDER.build("  " + this.value + "  ");
		assertSame(this.artifactId, artifactId_);
	}

	@Test
	public final void interning_of_ArtifactIds_works_as_expected_2()
	{
		final ArtifactId otherArtifactId = new ArtifactId("Some Other Value");
		assertNotSame(this.artifactId, otherArtifactId);
	}
}
