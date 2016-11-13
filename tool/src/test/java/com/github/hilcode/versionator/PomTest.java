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

import static com.github.hilcode.versionator.Generator.randomDependencies;
import static com.github.hilcode.versionator.Generator.randomFile;
import static com.github.hilcode.versionator.Generator.randomGav;
import static com.github.hilcode.versionator.Generator.randomGroupIdSource;
import static com.github.hilcode.versionator.Generator.randomModules;
import static com.github.hilcode.versionator.Generator.randomPom;
import static com.github.hilcode.versionator.Generator.randomProperties;
import static com.github.hilcode.versionator.Generator.randomType;
import static com.github.hilcode.versionator.Generator.randomVersionSource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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
public final class PomTest
{
	private Random rnd;

	private Pom pom;

	@Before
	public void setUp()
	{
		rnd = new Random();
		pom = randomPom(rnd);
	}

	@Test
	public final void each_Pom_must_have_a_non_null_gav()
	{
		try
		{
			new Pom(
					null,
					pom.groupIdSource,
					pom.versionSource,
					pom.file,
					pom.type,
					pom.parent,
					pom.modules,
					pom.properties,
					pom.dependencies);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'gav'.", e.getMessage());
		}
	}

	@Test
	public final void each_Pom_must_have_a_non_null_group_id_source()
	{
		try
		{
			new Pom(
					pom.gav,
					null,
					pom.versionSource,
					pom.file,
					pom.type,
					pom.parent,
					pom.modules,
					pom.properties,
					pom.dependencies);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'groupIdSource'.", e.getMessage());
		}
	}

	@Test
	public final void each_Pom_must_have_a_non_null_version_source()
	{
		try
		{
			new Pom(
					pom.gav,
					pom.groupIdSource,
					null,
					pom.file,
					pom.type,
					pom.parent,
					pom.modules,
					pom.properties,
					pom.dependencies);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'versionSource'.", e.getMessage());
		}
	}

	@Test
	public final void each_Pom_must_have_a_non_null_file()
	{
		try
		{
			new Pom(
					pom.gav,
					pom.groupIdSource,
					pom.versionSource,
					null,
					pom.type,
					pom.parent,
					pom.modules,
					pom.properties,
					pom.dependencies);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'file'.", e.getMessage());
		}
	}

	@Test
	public final void each_Pom_must_have_a_non_null_type()
	{
		try
		{
			new Pom(
					pom.gav,
					pom.groupIdSource,
					pom.versionSource,
					pom.file,
					null,
					pom.parent,
					pom.modules,
					pom.properties,
					pom.dependencies);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'type'.", e.getMessage());
		}
	}

	@Test
	public final void each_Pom_must_have_a_non_null_parent()
	{
		try
		{
			new Pom(
					pom.gav,
					pom.groupIdSource,
					pom.versionSource,
					pom.file,
					pom.type,
					null,
					pom.modules,
					pom.properties,
					pom.dependencies);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'parent'.", e.getMessage());
		}
	}

	@Test
	public final void each_Pom_must_have_a_non_null_list_of_modules()
	{
		try
		{
			new Pom(
					pom.gav,
					pom.groupIdSource,
					pom.versionSource,
					pom.file,
					pom.type,
					pom.parent,
					null,
					pom.properties,
					pom.dependencies);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'modules'.", e.getMessage());
		}
	}

	@Test
	public final void each_Pom_must_have_a_non_null_list_of_properties()
	{
		try
		{
			new Pom(
					pom.gav,
					pom.groupIdSource,
					pom.versionSource,
					pom.file,
					pom.type,
					pom.parent,
					pom.modules,
					null,
					pom.dependencies);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'properties'.", e.getMessage());
		}
	}

	@Test
	public final void each_Pom_must_have_a_non_null_list_of_dependencies()
	{
		try
		{
			new Pom(
					pom.gav,
					pom.groupIdSource,
					pom.versionSource,
					pom.file,
					pom.type,
					pom.parent,
					pom.modules,
					pom.properties,
					null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'dependencies'.", e.getMessage());
		}
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_1()
	{
		assertEquals(pom.hashCode(), pom.hashCode());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_2()
	{
		final Pom samePom = copyInstance(pom);
		assertEquals(pom.hashCode(), samePom.hashCode());
	}

	@Test
	public final void the_equals_implementation_works_as_expected_1()
	{
		assertFalse(pom.equals(null));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_2()
	{
		assertTrue(pom.equals(pom));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_3()
	{
		final Pom samePom = copyInstance(pom);
		assertTrue(pom.equals(samePom));
		assertTrue(samePom.equals(pom));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_4()
	{
		final Pom otherPom = changeGav(rnd, pom);
		assertFalse(pom.equals(otherPom));
		assertFalse(otherPom.equals(pom));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_5()
	{
		final Pom otherPom = changeGroupIdSource(rnd, pom);
		assertFalse(pom.equals(otherPom));
		assertFalse(otherPom.equals(pom));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_6()
	{
		final Pom otherPom = changeVersionSource(rnd, pom);
		assertFalse(pom.equals(otherPom));
		assertFalse(otherPom.equals(pom));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_7()
	{
		final Pom otherPom = changeFile(rnd, pom);
		assertFalse(pom.equals(otherPom));
		assertFalse(otherPom.equals(pom));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_8()
	{
		final Pom otherPom = changeType(rnd, pom);
		assertFalse(pom.equals(otherPom));
		assertFalse(otherPom.equals(pom));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_9()
	{
		final Pom otherPom = changeParent(rnd, pom);
		assertFalse(pom.equals(otherPom));
		assertFalse(otherPom.equals(pom));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_10()
	{
		final Pom otherPom = changeModules(rnd, pom);
		assertFalse(pom.equals(otherPom));
		assertFalse(otherPom.equals(pom));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_11()
	{
		final Pom otherPom = changeProperties(rnd, pom);
		assertFalse(pom.equals(otherPom));
		assertFalse(otherPom.equals(pom));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_12()
	{
		final Pom otherPom = changeDependencies(rnd, pom);
		assertFalse(pom.equals(otherPom));
		assertFalse(otherPom.equals(pom));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_13()
	{
		assertFalse(pom.equals(new Object()));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_1()
	{
		assertEquals(0, pom.compareTo(pom));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_2()
	{
		final Pom samePom = copyInstance(pom);
		assertEquals(0, pom.compareTo(samePom));
		assertEquals(0, samePom.compareTo(pom));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_3()
	{
		final Pom otherPom = changeGav(rnd, pom);
		assertNotEquals(0, pom.compareTo(otherPom));
		assertEquals(pom.compareTo(otherPom) * -1, otherPom.compareTo(pom));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_4()
	{
		final Pom otherPom = changeGroupIdSource(rnd, pom);
		assertNotEquals(0, pom.compareTo(otherPom));
		assertEquals(pom.compareTo(otherPom) * -1, otherPom.compareTo(pom));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_5()
	{
		final Pom otherPom = changeVersionSource(rnd, pom);
		assertNotEquals(0, pom.compareTo(otherPom));
		assertEquals(pom.compareTo(otherPom) * -1, otherPom.compareTo(pom));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_6()
	{
		final Pom otherPom = changeFile(rnd, pom);
		assertNotEquals(0, pom.compareTo(otherPom));
		assertEquals(pom.compareTo(otherPom) * -1, otherPom.compareTo(pom));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_7()
	{
		final Pom otherPom = changeType(rnd, pom);
		assertNotEquals(0, pom.compareTo(otherPom));
		assertEquals(pom.compareTo(otherPom) * -1, otherPom.compareTo(pom));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_8()
	{
		final Pom otherPom = changeParent(rnd, pom);
		assertNotEquals(0, pom.compareTo(otherPom));
		assertEquals(pom.compareTo(otherPom) * -1, otherPom.compareTo(pom));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_9()
	{
		final Pom otherPom = changeModules(rnd, pom);
		assertNotEquals(0, pom.compareTo(otherPom));
		assertEquals(pom.compareTo(otherPom) * -1, otherPom.compareTo(pom));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_10()
	{
		final Pom otherPom = changeProperties(rnd, pom);
		assertNotEquals(0, pom.compareTo(otherPom));
		assertEquals(pom.compareTo(otherPom) * -1, otherPom.compareTo(pom));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_11()
	{
		final Pom otherPom = changeDependencies(rnd, pom);
		assertNotEquals(0, pom.compareTo(otherPom));
		assertEquals(pom.compareTo(otherPom) * -1, otherPom.compareTo(pom));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_12()
	{
		assertEquals(1, pom.compareTo(Pom.NONE));
		assertEquals(0, Pom.NONE.compareTo(Pom.NONE));
		assertEquals(-1, Pom.NONE.compareTo(pom));
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_1()
	{
		assertEquals(
				String.format(
						"(Pom gav=%s groupIdSource=%s versionSource=%s file='%s' type=%s parent=%s modules=%s " +
								"properties=%s dependencies=%s)",
						pom.gav,
						pom.groupIdSource,
						pom.versionSource,
						pom.file,
						pom.type,
						pom.parent,
						pom.modules,
						pom.properties,
						pom.dependencies),
				pom.toString());
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_2()
	{
		assertEquals("(Pom NONE)", Pom.NONE.toString());
	}

	@Test
	public final void interning_of_Poms_works_as_expected_1()
	{
		final Pom pom_ = Pom.BUILDER.build(
				pom.gav,
				pom.groupIdSource,
				pom.versionSource,
				pom.file,
				pom.type,
				pom.parent,
				pom.modules,
				pom.properties,
				pom.dependencies);
		assertSame(pom, pom_);
	}

	@Test
	public final void interning_of_Poms_works_as_expected_2()
	{
		final Pom otherPom = changeGav(rnd, pom);
		assertNotSame(pom, otherPom);
	}

	@Test
	public final void interning_of_Poms_works_as_expected_3()
	{
		final Pom otherPom = changeGroupIdSource(rnd, pom);
		assertNotSame(pom, otherPom);
	}

	@Test
	public final void interning_of_Poms_works_as_expected_4()
	{
		final Pom otherPom = changeVersionSource(rnd, pom);
		assertNotSame(pom, otherPom);
	}

	@Test
	public final void interning_of_Poms_works_as_expected_5()
	{
		final Pom otherPom = changeFile(rnd, pom);
		assertNotSame(pom, otherPom);
	}

	@Test
	public final void interning_of_Poms_works_as_expected_6()
	{
		final Pom otherPom = changeType(rnd, pom);
		assertNotSame(pom, otherPom);
	}

	@Test
	public final void interning_of_Poms_works_as_expected_7()
	{
		final Pom otherPom = changeParent(rnd, pom);
		assertNotSame(pom, otherPom);
	}

	@Test
	public final void interning_of_Poms_works_as_expected_8()
	{
		final Pom otherPom = changeModules(rnd, pom);
		assertNotSame(pom, otherPom);
	}

	@Test
	public final void interning_of_Poms_works_as_expected_9()
	{
		final Pom otherPom = changeProperties(rnd, pom);
		assertNotSame(pom, otherPom);
	}

	@Test
	public final void interning_of_Poms_works_as_expected_10()
	{
		final Pom otherPom = changeDependencies(rnd, pom);
		assertNotSame(pom, otherPom);
	}

	private static final Pom copyInstance(final Pom pom)
	{
		return new Pom(
				pom.gav,
				pom.groupIdSource,
				pom.versionSource,
				pom.file,
				pom.type,
				pom.parent,
				pom.modules,
				pom.properties,
				pom.dependencies);
	}

	private static final Pom changeProperties(final Random rnd, final Pom pom)
	{
		return new Pom(
				pom.gav,
				pom.groupIdSource,
				pom.versionSource,
				pom.file,
				pom.type,
				pom.parent,
				pom.modules,
				randomProperties(rnd, input -> input.equals(pom.properties)),
				pom.dependencies);
	}

	private static final Pom changeDependencies(final Random rnd, final Pom pom)
	{
		return new Pom(
				pom.gav,
				pom.groupIdSource,
				pom.versionSource,
				pom.file,
				pom.type,
				pom.parent,
				pom.modules,
				pom.properties,
				randomDependencies(rnd, input -> input.equals(pom.dependencies)));
	}

	private static final Pom changeModules(final Random rnd, final Pom pom)
	{
		return new Pom(
				pom.gav,
				pom.groupIdSource,
				pom.versionSource,
				pom.file,
				pom.type,
				pom.parent,
				randomModules(rnd, input -> input.equals(pom.modules)),
				pom.properties,
				pom.dependencies);
	}

	private static final Pom changeParent(final Random rnd, final Pom pom)
	{
		return new Pom(
				pom.gav,
				pom.groupIdSource,
				pom.versionSource,
				pom.file,
				pom.type,
				randomPom(rnd, input -> input == pom.parent),
				pom.modules,
				pom.properties,
				pom.dependencies);
	}

	private static final Pom changeType(final Random rnd, final Pom pom)
	{
		return new Pom(
				pom.gav,
				pom.groupIdSource,
				pom.versionSource,
				pom.file,
				randomType(rnd, input -> input == pom.type),
				pom.parent,
				pom.modules,
				pom.properties,
				pom.dependencies);
	}

	private static final Pom changeFile(final Random rnd, final Pom pom)
	{
		return new Pom(
				pom.gav,
				pom.groupIdSource,
				pom.versionSource,
				randomFile(rnd, input -> input.equals(pom.file)),
				pom.type,
				pom.parent,
				pom.modules,
				pom.properties,
				pom.dependencies);
	}

	private static final Pom changeVersionSource(final Random rnd, final Pom pom)
	{
		return new Pom(
				pom.gav,
				pom.groupIdSource,
				randomVersionSource(rnd, input -> input == pom.versionSource),
				pom.file,
				pom.type,
				pom.parent,
				pom.modules,
				pom.properties,
				pom.dependencies);
	}

	private static final Pom changeGroupIdSource(final Random rnd, final Pom pom)
	{
		return new Pom(
				pom.gav,
				randomGroupIdSource(rnd, input -> input == pom.groupIdSource),
				pom.versionSource,
				pom.file,
				pom.type,
				pom.parent,
				pom.modules,
				pom.properties,
				pom.dependencies);
	}

	private static final Pom changeGav(final Random rnd, final Pom pom)
	{
		return new Pom(
				randomGav(rnd, input -> input == pom.gav),
				pom.groupIdSource,
				pom.versionSource,
				pom.file,
				pom.type,
				pom.parent,
				pom.modules,
				pom.properties,
				pom.dependencies);
	}
}
