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

import static com.github.hilcode.versionator.Generator.randomModel;
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
 * The unit tests for {@code Model}.
 */
public final class ModelTest
{
	private Random rnd;

	private Model model;

	@Before
	public void setUp()
	{
		this.rnd = new Random();
		this.model = randomModel(this.rnd);
	}

	@Test
	public final void each_Model_must_have_a_non_null_value()
	{
		try
		{
			new Model(null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'poms'.", e.getMessage());
		}
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_1()
	{
		assertEquals(this.model.hashCode(), this.model.hashCode());
	}

	@Test
	public final void the_hashCode_implementation_works_as_expected_2()
	{
		final Model otherModel = new Model(this.model.poms);
		assertEquals(this.model.hashCode(), otherModel.hashCode());
	}

	@Test
	public final void the_equals_implementation_works_as_expected_1()
	{
		assertFalse(this.model.equals(null));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_2()
	{
		assertTrue(this.model.equals(this.model));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_3()
	{
		final Model model_ = new Model(this.model.poms);
		assertTrue(this.model.equals(model_));
		assertTrue(model_.equals(this.model));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_4()
	{
		final Model otherModel = randomModel(this.rnd);
		assertFalse(this.model.equals(otherModel));
		assertFalse(otherModel.equals(this.model));
	}

	@Test
	public final void the_equals_implementation_works_as_expected_5()
	{
		assertFalse(this.model.equals(new Object()));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_1()
	{
		assertEquals(0, this.model.compareTo(this.model));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_2()
	{
		final Model model_ = new Model(this.model.poms);
		assertEquals(0, this.model.compareTo(model_));
		assertEquals(0, model_.compareTo(this.model));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_3()
	{
		final Model otherModel = randomModel(this.rnd);
		assertNotEquals(0, this.model.compareTo(otherModel));
		assertEquals(-this.model.compareTo(otherModel), otherModel.compareTo(this.model));
	}

	@Test
	public final void the_compareTo_implementation_works_as_expected_4()
	{
		assertEquals(1, this.model.compareTo(Model.NONE));
		assertEquals(0, Model.NONE.compareTo(Model.NONE));
		assertEquals(-1, Model.NONE.compareTo(this.model));
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_1()
	{
		assertEquals(String.format("(Model poms=%s)", this.model.poms), this.model.toString());
	}

	@Test
	public final void the_textual_representation_for_debugging_is_useful_2()
	{
		assertEquals("(Model NONE)", Model.NONE.toString());
	}

	@Test
	public final void interning_of_Models_works_as_expected_1()
	{
		final Model model_ = Model.BUILDER.build(this.model.poms);
		assertSame(this.model, model_);
	}

	@Test
	public final void interning_of_Models_works_as_expected_2()
	{
		final Model otherModel = randomModel(this.rnd);
		assertNotSame(this.model, otherModel);
	}

	@Test
	public final void placate_Cobertura() throws Exception
	{
		final Method method = Model.class.getMethod("compareTo", Object.class);
		method.invoke(this.model, this.model);
	}
}
