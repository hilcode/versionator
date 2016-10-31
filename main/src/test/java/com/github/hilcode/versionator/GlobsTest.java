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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.github.hilcode.versionator.Globs.Glob;

/**
 * The unit tests for {@code Globs}.
 */
public final class GlobsTest
{
	@Test
	public void a_missing_pattern_should_trigger_an_NPE()
	{
		try
		{
			new Glob(null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'pattern'.", e.getMessage());
		}
	}

	@Test
	public void multiple_consecutive_asterisks_should_be_interpreted_as_a_single_asterisk()
	{
		assertEquals("*", new Glob("**").pattern);
		assertEquals("a*b*c*d*", new Glob("a**b***c****d*****").pattern);
	}

	@Test
	public void toString_works()
	{
		final Glob glob = new Glob("hello");
		assertEquals("(Glob pattern='hello')", glob.toString());
	}

	@Test
	public void matching_NULL_always_fails()
	{
		assertFalse(Globs.create("").match(null));
	}

	@Test
	public void only_an_empty_string_matches_the_empty_string()
	{
		assertTrue(Globs.create("").match(""));
		assertFalse(Globs.create("").match("a"));
	}

	@Test
	public void check_that_matching_simple_patterns_works()
	{
		assertTrue(Globs.create("a").match("a"));
		assertFalse(Globs.create("a").match(""));
		assertFalse(Globs.create("a").match("ab"));
		assertTrue(Globs.create("ab").match("ab"));
		assertFalse(Globs.create("ab").match("_ab"));
		assertFalse(Globs.create("ab").match("ab_"));
	}

	@Test
	public void check_that_matching_simple_globs_works()
	{
		assertTrue(Globs.create("?").match("a"));
		assertFalse(Globs.create("?").match(""));
		assertFalse(Globs.create("?").match("ab"));
		assertTrue(Globs.create("??").match("ab"));
		assertFalse(Globs.create("??").match("abc"));
	}

	@Test
	public void check_that_combining_single_character_globs_with_simple_patterns_works()
	{
		assertTrue(Globs.create("a?").match("ab"));
		assertTrue(Globs.create("?b").match("ab"));
		assertTrue(Globs.create("a??").match("abc"));
		assertTrue(Globs.create("?b?").match("abc"));
		assertTrue(Globs.create("??c").match("abc"));
		assertFalse(Globs.create("a??").match("_bc"));
		assertFalse(Globs.create("?b?").match("a_c"));
		assertFalse(Globs.create("??c").match("ab_"));
	}

	@Test
	public void check_that_a_single_asterisk_works()
	{
		assertTrue(Globs.create("*").match(""));
		assertTrue(Globs.create("*").match("a"));
		assertTrue(Globs.create("*").match("ab"));
		assertTrue(Globs.create("*").match("abc"));
	}

	@Test
	public void check_that_combining_multi_character_globs_and_simple_patterns_works()
	{
		assertFalse(Globs.create("a*").match(""));
		assertFalse(Globs.create("*a").match(""));
		assertFalse(Globs.create("*a*").match(""));
		assertTrue(Globs.create("a*").match("a"));
		assertFalse(Globs.create("a*").match("b"));
		assertTrue(Globs.create("*a").match("a"));
		assertFalse(Globs.create("*a").match("b"));
		assertTrue(Globs.create("*a*").match("a"));
		assertFalse(Globs.create("*a*").match("b"));
		assertTrue(Globs.create("a*").match("ab"));
		assertFalse(Globs.create("a*").match("ba"));
		assertTrue(Globs.create("*b").match("ab"));
		assertFalse(Globs.create("*b").match("ac"));
		assertTrue(Globs.create("*bc").match("abc"));
		assertFalse(Globs.create("*bc").match("acc"));
		assertTrue(Globs.create("*c").match("abc"));
		assertFalse(Globs.create("*c").match("abcd"));
		assertTrue(Globs.create("a*c").match("abc"));
		assertTrue(Globs.create("a*c").match("aabbcc"));
		assertTrue(Globs.create("ab*").match("abccc"));
	}

	@Test
	public void check_that_combining_multi_character_globs_with_single_character_globs_works()
	{
		assertFalse(Globs.create("?*").match(""));
		assertFalse(Globs.create("*?").match(""));
		assertFalse(Globs.create("*?*").match(""));
		assertTrue(Globs.create("?*").match("a"));
		assertTrue(Globs.create("*?").match("a"));
		assertTrue(Globs.create("*?*").match("a"));
		assertTrue(Globs.create("?*").match("ab"));
		assertTrue(Globs.create("*?").match("ab"));
		assertTrue(Globs.create("*??").match("abc"));
		assertTrue(Globs.create("*?").match("abc"));
		assertTrue(Globs.create("?*?").match("abc"));
		assertTrue(Globs.create("?*?").match("aabbcc"));
		assertTrue(Globs.create("??*").match("abccc"));
		assertTrue(Globs.create("?*?*").match("ab"));
		assertTrue(Globs.create("?*?*").match("abc"));
		assertTrue(Globs.create("?*?*").match("abcd"));
		assertTrue(Globs.create("?*?*").match("abcde"));
		assertFalse(Globs.create("?*?*").match(""));
		assertFalse(Globs.create("?*?*").match("a"));
		assertTrue(Globs.create("*?*?*").match("ab"));
		assertTrue(Globs.create("*?*?*").match("abc"));
		assertTrue(Globs.create("*?*?*").match("abcd"));
		assertTrue(Globs.create("*?*?*").match("abcde"));
		assertFalse(Globs.create("*?*?*").match(""));
		assertFalse(Globs.create("*?*?*").match("a"));
	}

	@Test
	public void placate_jacoco()
	{
		new Globs();
	}
}
