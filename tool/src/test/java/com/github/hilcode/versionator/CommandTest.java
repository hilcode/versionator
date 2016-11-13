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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import java.io.File;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.google.common.collect.ImmutableList;

/**
 * The unit tests for {@code Command}.
 */
@RunWith(MockitoJUnitRunner.class)
public final class CommandTest
{
	@Mock
	private File mockRootDir;

	private ImmutableList<String> patterns;

	private Command.Verbosity verbosity;

	private Command.Grouping grouping;

	private Command.RunType runType;

	private Command.Interactivity interactivity;

	private Command.Colour colour;

	private String key;

	private String value;

	private ImmutableList<String> gavs;

	private ImmutableList<String> exclusions;

	@Before
	public void setUp() throws Exception
	{
		final Random rnd = new Random();
		this.patterns = randomStrings(rnd, "pattern-");
		this.verbosity = Command.Verbosity.values()[rnd.nextInt(Command.Verbosity.values().length)];
		this.grouping = Command.Grouping.values()[rnd.nextInt(Command.Grouping.values().length)];
		this.runType = Command.RunType.values()[rnd.nextInt(Command.RunType.values().length)];
		this.interactivity = Command.Interactivity.values()[rnd.nextInt(Command.Interactivity.values().length)];
		this.colour = Command.Colour.values()[rnd.nextInt(Command.Colour.values().length)];
		this.key = "key-" + rnd.nextInt(10000);
		this.value = "Value " + rnd.nextInt(10000);
		this.gavs = randomStrings(rnd, "gav-");
		this.exclusions = randomStrings(rnd, "exclusion-");
	}

	@Test
	public final void each_Command_List_must_have_a_non_null_root_directory()
	{
		try
		{
			new Command.List(null, this.patterns, this.verbosity, this.grouping);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'rootDir'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_List_must_have_a_non_null_list_of_patterns()
	{
		try
		{
			new Command.List(this.mockRootDir, null, this.verbosity, this.grouping);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'patterns'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_List_must_have_a_non_null_Verbosity()
	{
		try
		{
			new Command.List(this.mockRootDir, this.patterns, null, this.grouping);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'verbosity'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_List_must_have_a_non_null_Grouping()
	{
		try
		{
			new Command.List(this.mockRootDir, this.patterns, this.verbosity, null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'grouping'.", e.getMessage());
		}
	}

	@Test
	public final void the_Command_List_constructor_works_as_expected()
	{
		final Command.List commandList =
				new Command.List(this.mockRootDir, this.patterns, this.verbosity, this.grouping);
		assertSame(this.mockRootDir, commandList.rootDir);
		assertSame(this.patterns, commandList.patterns);
		assertSame(this.verbosity, commandList.verbosity);
		assertSame(this.grouping, commandList.grouping);
	}

	@Test
	public final void each_Command_SetProperty_must_have_a_non_null_root_directory()
	{
		try
		{
			new Command.SetProperty(
					null,
					this.runType,
					this.interactivity,
					this.colour,
					this.key,
					this.value,
					this.gavs);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'rootDir'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_SetProperty_must_have_a_non_null_RunType()
	{
		try
		{
			new Command.SetProperty(
					this.mockRootDir,
					null,
					this.interactivity,
					this.colour,
					this.key,
					this.value,
					this.gavs);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'runType'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_SetProperty_must_have_a_non_null_Interactivity()
	{
		try
		{
			new Command.SetProperty(
					this.mockRootDir,
					this.runType,
					null,
					this.colour,
					this.key,
					this.value,
					this.gavs);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'interactivity'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_SetProperty_must_have_a_non_null_Colour()
	{
		try
		{
			new Command.SetProperty(
					this.mockRootDir,
					this.runType,
					this.interactivity,
					null,
					this.key,
					this.value,
					this.gavs);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'colour'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_SetProperty_must_have_a_non_null_key()
	{
		try
		{
			new Command.SetProperty(
					this.mockRootDir,
					this.runType,
					this.interactivity,
					this.colour,
					null,
					this.value,
					this.gavs);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'key'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_SetProperty_must_have_a_non_empty_key()
	{
		try
		{
			new Command.SetProperty(
					this.mockRootDir,
					this.runType,
					this.interactivity,
					this.colour,
					"   ",
					this.value,
					this.gavs);
			fail("Expected an IllegalArgumentException.");
		}
		catch (final IllegalArgumentException e)
		{
			assertEquals("Empty 'key'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_SetProperty_must_have_a_non_null_value()
	{
		try
		{
			new Command.SetProperty(
					this.mockRootDir,
					this.runType,
					this.interactivity,
					this.colour,
					this.key,
					null,
					this.gavs);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'value'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_SetProperty_must_have_a_non_empty_list_of_Gavs()
	{
		try
		{
			new Command.SetProperty(
					this.mockRootDir,
					this.runType,
					this.interactivity,
					this.colour,
					this.key,
					this.value,
					null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'gavs'.", e.getMessage());
		}
	}

	@Test
	public final void the_Command_SetProperty_constructor_works_as_expected()
	{
		final Command.SetProperty commandSetProperty =
				new Command.SetProperty(
						this.mockRootDir,
						this.runType,
						this.interactivity,
						this.colour,
						this.key,
						this.value,
						this.gavs);
		assertSame(this.mockRootDir, commandSetProperty.rootDir);
		assertSame(this.runType, commandSetProperty.runType);
		assertSame(this.interactivity, commandSetProperty.interactivity);
		assertSame(this.colour, commandSetProperty.colour);
		assertSame(this.key, commandSetProperty.key);
		assertSame(this.value, commandSetProperty.value);
		assertSame(this.gavs, commandSetProperty.gavs);
	}

	@Test
	public final void each_Command_SetVersion_must_have_a_non_null_root_directory()
	{
		try
		{
			new Command.SetVersion(
					null,
					this.runType,
					this.interactivity,
					this.colour,
					this.gavs);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'rootDir'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_SetVersion_must_have_a_non_null_RunType()
	{
		try
		{
			new Command.SetVersion(
					this.mockRootDir,
					null,
					this.interactivity,
					this.colour,
					this.gavs);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'runType'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_SetVersion_must_have_a_non_null_Interactivity()
	{
		try
		{
			new Command.SetVersion(
					this.mockRootDir,
					this.runType,
					null,
					this.colour,
					this.gavs);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'interactivity'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_SetVersion_must_have_a_non_null_Colour()
	{
		try
		{
			new Command.SetVersion(
					this.mockRootDir,
					this.runType,
					this.interactivity,
					null,
					this.gavs);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'colour'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_SetVersion_must_have_a_non_empty_list_of_Gavs()
	{
		try
		{
			new Command.SetVersion(
					this.mockRootDir,
					this.runType,
					this.interactivity,
					this.colour,
					null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'gavs'.", e.getMessage());
		}
	}

	@Test
	public final void the_Command_SetVersion_constructor_works_as_expected()
	{
		final Command.SetVersion commandSetVersion =
				new Command.SetVersion(
						this.mockRootDir,
						this.runType,
						this.interactivity,
						this.colour,
						this.gavs);
		assertSame(this.mockRootDir, commandSetVersion.rootDir);
		assertSame(this.runType, commandSetVersion.runType);
		assertSame(this.interactivity, commandSetVersion.interactivity);
		assertSame(this.colour, commandSetVersion.colour);
		assertSame(this.gavs, commandSetVersion.gavs);
	}

	@Test
	public final void each_Command_Release_must_have_a_non_null_root_directory()
	{
		try
		{
			new Command.Release(
					null,
					this.runType,
					this.interactivity,
					this.colour,
					this.exclusions);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'rootDir'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_Release_must_have_a_non_null_RunType()
	{
		try
		{
			new Command.Release(
					this.mockRootDir,
					null,
					this.interactivity,
					this.colour,
					this.exclusions);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'runType'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_Release_must_have_a_non_null_Interactivity()
	{
		try
		{
			new Command.Release(
					this.mockRootDir,
					this.runType,
					null,
					this.colour,
					this.exclusions);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'interactivity'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_Release_must_have_a_non_null_Colour()
	{
		try
		{
			new Command.Release(
					this.mockRootDir,
					this.runType,
					this.interactivity,
					null,
					this.exclusions);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'colour'.", e.getMessage());
		}
	}

	@Test
	public final void each_Command_Release_must_have_a_non_empty_list_of_Gavs()
	{
		try
		{
			new Command.Release(
					this.mockRootDir,
					this.runType,
					this.interactivity,
					this.colour,
					null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'exclusions'.", e.getMessage());
		}
	}

	@Test
	public final void the_Command_Release_constructor_works_as_expected()
	{
		final Command.Release commandRelease =
				new Command.Release(
						this.mockRootDir,
						this.runType,
						this.interactivity,
						this.colour,
						this.exclusions);
		assertSame(this.mockRootDir, commandRelease.rootDir);
		assertSame(this.runType, commandRelease.runType);
		assertSame(this.interactivity, commandRelease.interactivity);
		assertSame(this.colour, commandRelease.colour);
		assertSame(this.exclusions, commandRelease.exclusions);
	}

	public static final ImmutableList<String> randomStrings(final Random rnd, final String prefix)
	{
		final int patternCount = rnd.nextInt(10);
		final ImmutableList.Builder<String> patternsBuilder = ImmutableList.builder();
		for (int i = 0; i < patternCount; i++)
		{
			patternsBuilder.add(prefix + rnd.nextInt(10000));
		}
		return patternsBuilder.build();
	}
}
