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
import java.lang.reflect.Method;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code Dependency}.
 */
public final class DependencyTest
{
	private Random rnd;

	private Dependency dependency;

	@Before
	public void setUp()
	{
		this.rnd = new Random();
		this.dependency = Generator.randomDependency(this.rnd);
	}

	@Test
	public final void each_Dependency_must_have_a_non_null_value()
	{
		try
		{
			new Dependency(null);
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
		assertEquals(this.dependency.hashCode(), this.dependency.hashCode());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_2()
	{
		final Dependency otherDependency = new Dependency(this.dependency.gav);
		assertEquals(this.dependency.hashCode(), otherDependency.hashCode());
	}

	@Test
	public final void the_equals_implementation_works_as_expected_1()
	{
		assertFalse(this.dependency.equals(null));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_2()
	{
		assertTrue(this.dependency.equals(this.dependency));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_3()
	{
		final Dependency dependency_ = new Dependency(this.dependency.gav);
		assertTrue(this.dependency.equals(dependency_));
		assertTrue(dependency_.equals(this.dependency));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_4()
	{
		final Dependency otherDependency =
				new Dependency(
						Gav.BUILDER.build(
								randomGroupArtifact(this.rnd),
								Version.BUILDER.build("1.0-blabla")));
		assertFalse(this.dependency.equals(otherDependency));
		assertFalse(otherDependency.equals(this.dependency));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_5()
	{
		assertFalse(this.dependency.equals(new Object()));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_1()
	{
		assertEquals(0, this.dependency.compareTo(this.dependency));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_2()
	{
		final Dependency dependency_ = new Dependency(this.dependency.gav);
		assertEquals(0, this.dependency.compareTo(dependency_));
		assertEquals(0, dependency_.compareTo(this.dependency));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_3()
	{
		final Dependency otherDependency =
				new Dependency(
						Gav.BUILDER.build(
								GroupArtifact.BUILDER.build(
										GroupId.BUILDER.build("abc"),
										ArtifactId.BUILDER.build("abc")),
								Version.BUILDER.build("1.2.3-SNAPSHOT")));
		assertEquals(1, this.dependency.compareTo(otherDependency));
		assertEquals(-1, otherDependency.compareTo(this.dependency));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_4()
	{
		assertEquals(1, this.dependency.compareTo(Dependency.NONE));
		assertEquals(0, Dependency.NONE.compareTo(Dependency.NONE));
		assertEquals(-1, Dependency.NONE.compareTo(this.dependency));
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_1()
	{
		assertEquals(String.format("(Dependency gav=%s)", this.dependency.gav), this.dependency.toString());
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_2()
	{
		assertEquals("(Dependency NONE)", Dependency.NONE.toString());
	}

	@Test
	public final void interning_of_Dependencys_works_as_expected_1()
	{
		final Dependency dependency_ = Dependency.BUILDER.build(this.dependency.gav);
		assertSame(this.dependency, dependency_);
	}

	@Test
	public final void interning_of_Dependencys_works_as_expected_2()
	{
		final Dependency otherDependency =
				new Dependency(
						Gav.BUILDER.build(
								randomGroupArtifact(this.rnd),
								Version.BUILDER.build("0-SNAPSHOT")));
		assertNotSame(this.dependency, otherDependency);
	}

	@Test
	public final void placate_Cobertura() throws Exception
	{
		final Method method = Dependency.class.getMethod("compareTo", Object.class);
		method.invoke(this.dependency, this.dependency);
	}
}
