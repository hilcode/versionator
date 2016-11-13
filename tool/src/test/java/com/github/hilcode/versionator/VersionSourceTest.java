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

import static com.github.hilcode.versionator.VersionSource.PARENT;
import static com.github.hilcode.versionator.VersionSource.POM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * The unit tests for {@code VersionSource}.
 */
public class VersionSourceTest
{
	public final void test1()
	{
		assertEquals(2, VersionSource.values().length);
	}

	public final void test2()
	{
		assertSame(VersionSource.values()[0], PARENT);
		assertSame(VersionSource.values()[1], POM);
	}

	public final void test3()
	{
		assertSame(PARENT, VersionSource.valueOf(PARENT.toString()));
		assertSame(POM, VersionSource.valueOf(POM.toString()));
	}
}
