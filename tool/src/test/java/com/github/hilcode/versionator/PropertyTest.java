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
import java.lang.reflect.Method;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code Property}.
 */
public final class PropertyTest
{
	private Property property;

	@Before
	public void setUp()
	{
		final Random rnd = new Random();
		this.property = Generator.randomProperty(rnd);
	}

	@Test
	public final void each_Property_must_have_a_non_null_key()
	{
		try
		{
			new Property(null, this.property.value);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'key'.", e.getMessage());
		}
	}

	@Test
	public final void each_Property_must_have_a_non_null_value()
	{
		try
		{
			new Property(this.property.key, null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'value'.", e.getMessage());
		}
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_1()
	{
		assertEquals(this.property.hashCode(), this.property.hashCode());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_2()
	{
		final Property otherProperty = new Property(this.property.key, this.property.value);
		assertEquals(this.property.hashCode(), otherProperty.hashCode());
	}

	@Test
	public final void the_equals_implementation_works_as_expected_1()
	{
		assertFalse(this.property.equals(null));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_2()
	{
		assertTrue(this.property.equals(this.property));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_3()
	{
		final Property property_ = new Property(this.property.key, this.property.value);
		assertTrue(this.property.equals(property_));
		assertTrue(property_.equals(this.property));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_4()
	{
		final Property propertyWithDifferentKey = new Property(Key.BUILDER.build("another-key"), this.property.value);
		assertFalse(this.property.equals(propertyWithDifferentKey));
		assertFalse(propertyWithDifferentKey.equals(this.property));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_5()
	{
		final Property propertyWithDifferentValue = new Property(this.property.key, "Some Other Value");
		assertFalse(this.property.equals(propertyWithDifferentValue));
		assertFalse(propertyWithDifferentValue.equals(this.property));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_6()
	{
		assertFalse(this.property.equals(new Object()));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_1()
	{
		assertEquals(0, this.property.compareTo(this.property));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_2()
	{
		final Property property_ = new Property(this.property.key, this.property.value);
		assertEquals(0, this.property.compareTo(property_));
		assertEquals(0, property_.compareTo(this.property));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_3()
	{
		final Property otherProperty = new Property(Key.BUILDER.build("abc"), this.property.value);
		assertEquals(1, this.property.compareTo(otherProperty));
		assertEquals(-1, otherProperty.compareTo(this.property));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_4()
	{
		final Property otherProperty = new Property(this.property.key, "Abc");
		assertEquals(1, this.property.compareTo(otherProperty));
		assertEquals(-1, otherProperty.compareTo(this.property));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_5()
	{
		assertEquals(1, this.property.compareTo(Property.NONE));
		assertEquals(0, Property.NONE.compareTo(Property.NONE));
		assertEquals(-1, Property.NONE.compareTo(this.property));
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_1()
	{
		assertEquals(
				String.format("(Property key=%s value='%s')", this.property.key, this.property.value),
				this.property.toString());
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_2()
	{
		assertEquals("(Property NONE)", Property.NONE.toString());
	}

	@Test
	public final void interning_of_Propertys_works_as_expected_1()
	{
		final Property property_ = Property.BUILDER.build(this.property.key, this.property.value);
		assertSame(this.property, property_);
	}

	@Test
	public final void interning_of_Propertys_works_as_expected_2()
	{
		final Property otherProperty = new Property(Key.BUILDER.build("abc"), this.property.value);
		assertNotSame(this.property, otherProperty);
	}

	@Test
	public final void interning_of_Propertys_works_as_expected_3()
	{
		final Property otherProperty = new Property(this.property.key, "Some Other Value");
		assertNotSame(this.property, otherProperty);
	}

	@Test
	public final void placate_Cobertura() throws Exception
	{
		final Method method = Property.class.getMethod("compareTo", Object.class);
		method.invoke(this.property, this.property);
	}
}
