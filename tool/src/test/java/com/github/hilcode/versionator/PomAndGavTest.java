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
import static com.github.hilcode.versionator.Generator.randomPom;
import static com.github.hilcode.versionator.Generator.randomPomAndGav;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.lang.reflect.Method;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code PomAndGav}.
 */
public final class PomAndGavTest
{
	private Random rnd;

	private Pom pom;

	private Gav gav;

	private PomAndGav pomAndGav;

	@Before
	public void setUp()
	{
		this.rnd = new Random();
		this.pomAndGav = randomPomAndGav(this.rnd);
		this.pom = this.pomAndGav.pom;
		this.gav = this.pomAndGav.gav;
	}

	@Test
	public final void each_PomAndGav_must_have_a_non_null_pom()
	{
		try
		{
			new PomAndGav(null, this.gav);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'pom'.", e.getMessage());
		}
	}

	@Test
	public final void each_PomAndGav_must_have_a_non_null_gav()
	{
		try
		{
			new PomAndGav(this.pom, null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'gav'.", e.getMessage());
		}
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_1()
	{
		assertEquals(this.pomAndGav.hashCode(), this.pomAndGav.hashCode());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_2()
	{
		final PomAndGav otherPomAndGav = new PomAndGav(this.pom, this.gav);
		assertEquals(this.pomAndGav.hashCode(), otherPomAndGav.hashCode());
	}

	@Test
	public final void the_equals_implementation_works_as_expected_1()
	{
		assertFalse(this.pomAndGav.equals(null));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_2()
	{
		assertTrue(this.pomAndGav.equals(this.pomAndGav));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_3()
	{
		final PomAndGav pomAndGav_ = new PomAndGav(this.pom, this.gav);
		assertTrue(this.pomAndGav.equals(pomAndGav_));
		assertTrue(pomAndGav_.equals(this.pomAndGav));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_4()
	{
		final PomAndGav otherPomAndGav =
				new PomAndGav(randomPom(this.rnd, pom_ -> pom_.gav == this.pom.gav), this.gav);
		assertFalse(this.pomAndGav.equals(otherPomAndGav));
		assertFalse(otherPomAndGav.equals(this.pomAndGav));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_5()
	{
		final PomAndGav otherPomAndGav =
				new PomAndGav(this.pom, randomGav(this.rnd, gav_ -> gav_.groupArtifact == this.gav.groupArtifact));
		assertFalse(this.pomAndGav.equals(otherPomAndGav));
		assertFalse(otherPomAndGav.equals(this.pomAndGav));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_6()
	{
		assertFalse(this.pomAndGav.equals(new Object()));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_1()
	{
		assertEquals(0, this.pomAndGav.compareTo(this.pomAndGav));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_2()
	{
		final PomAndGav pomAndGav_ = new PomAndGav(this.pom, this.gav);
		assertEquals(0, this.pomAndGav.compareTo(pomAndGav_));
		assertEquals(0, pomAndGav_.compareTo(this.pomAndGav));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_3()
	{
		final PomAndGav otherPomAndGav =
				new PomAndGav(randomPom(this.rnd, pom_ -> pom_.gav == this.pom.gav), this.gav);
		assertNotEquals(0, this.pomAndGav.compareTo(otherPomAndGav));
		assertEquals(-this.pomAndGav.compareTo(otherPomAndGav), otherPomAndGav.compareTo(this.pomAndGav));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_4()
	{
		final PomAndGav otherPomAndGav =
				new PomAndGav(this.pom, randomGav(this.rnd, gav_ -> gav_.groupArtifact == this.gav.groupArtifact));
		assertNotEquals(0, this.pomAndGav.compareTo(otherPomAndGav));
		assertEquals(-this.pomAndGav.compareTo(otherPomAndGav), otherPomAndGav.compareTo(this.pomAndGav));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_5()
	{
		assertEquals(1, this.pomAndGav.compareTo(PomAndGav.NONE));
		assertEquals(0, PomAndGav.NONE.compareTo(PomAndGav.NONE));
		assertEquals(-1, PomAndGav.NONE.compareTo(this.pomAndGav));
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_1()
	{
		assertEquals(
				String.format("(PomAndGav pom=%s gav=%s)", this.pom, this.gav),
				this.pomAndGav.toString());
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_2()
	{
		assertEquals("(PomAndGav NONE)", PomAndGav.NONE.toString());
	}

	@Test
	public final void interning_of_PomAndGavs_works_as_expected_1()
	{
		final PomAndGav pomAndGav_ = PomAndGav.BUILDER.build(this.pom, this.gav);
		assertSame(this.pomAndGav, pomAndGav_);
	}

	@Test
	public final void interning_of_PomAndGavs_works_as_expected_2()
	{
		final PomAndGav otherPomAndGav =
				new PomAndGav(randomPom(this.rnd, pom_ -> pom_.gav == this.pom.gav), this.gav);
		assertNotSame(this.pomAndGav, otherPomAndGav);
	}

	@Test
	public final void interning_of_PomAndGavs_works_as_expected_3()
	{
		final PomAndGav otherPomAndGav =
				new PomAndGav(this.pom, randomGav(this.rnd, gav_ -> gav_.groupArtifact == this.gav.groupArtifact));
		assertNotSame(this.pomAndGav, otherPomAndGav);
	}

	@Test
	public final void placate_Cobertura() throws Exception
	{
		final Method method = PomAndGav.class.getMethod("compareTo", Object.class);
		method.invoke(this.pomAndGav, this.pomAndGav);
	}
}
