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
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PomFinderUtilsTest
{
	@Mock
	private File mockFile;

	@Mock
	private File mockCanonicalFile;

	@Test
	public final void the_happy_path_should_return_the_canonical_representation_of_the_given_File() throws Exception
	{
		when(this.mockFile.getCanonicalFile())
				.thenReturn(this.mockCanonicalFile);
		assertSame(this.mockCanonicalFile, PomFinderUtils.toCanonical(this.mockFile));
	}

	@Test
	public final void a_non_null_File_is_required()
	{
		try
		{
			PomFinderUtils.toCanonical(null);
			fail("Expected a NullPointerException.");
		}
		catch (final NullPointerException e)
		{
			assertEquals("Missing 'file'.", e.getMessage());
		}
	}

	@Test
	public final void exceptions_are_handled_correctly() throws Exception
	{
		final IOException ioException = new IOException("Oops!");
		when(this.mockFile.getCanonicalFile())
				.thenThrow(ioException);
		try
		{
			PomFinderUtils.toCanonical(this.mockFile);
			fail("Expected an IOException.");
		}
		catch (final IllegalStateException e)
		{
			assertEquals("Oops!", e.getMessage());
			assertSame(ioException, e.getCause());
		}
	}

	@Test
	public final void this_is_just_to_get_100_percent_coverage()
	{
		new PomFinderUtils();
	}
}
