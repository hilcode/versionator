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

import static com.github.hilcode.versionator.Type.EAR;
import static com.github.hilcode.versionator.Type.EJB;
import static com.github.hilcode.versionator.Type.JAR;
import static com.github.hilcode.versionator.Type.MAVEN_PLUGIN;
import static com.github.hilcode.versionator.Type.OTHER;
import static com.github.hilcode.versionator.Type.PAR;
import static com.github.hilcode.versionator.Type.POM;
import static com.github.hilcode.versionator.Type.RAR;
import static com.github.hilcode.versionator.Type.WAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 * The unit tests for {@code Type}.
 */
public final class TypeTest
{
	@Test
	public final void the_type_POM_is_identified_correctly()
	{
		assertSame(POM, Type.toType("pom"));
	}

	@Test
	public final void the_type_JAR_is_identified_correctly()
	{
		assertSame(JAR, Type.toType("jar"));
	}

	@Test
	public final void the_type_MAVEN_PLUGIN_is_identified_correctly()
	{
		assertSame(MAVEN_PLUGIN, Type.toType("maven-plugin"));
	}

	@Test
	public final void the_type_EJB_is_identified_correctly()
	{
		assertSame(EJB, Type.toType("ejb"));
	}

	@Test
	public final void the_type_WAR_is_identified_correctly()
	{
		assertSame(WAR, Type.toType("war"));
	}

	@Test
	public final void the_type_EAR_is_identified_correctly()
	{
		assertSame(EAR, Type.toType("ear"));
	}

	@Test
	public final void the_type_RAR_is_identified_correctly()
	{
		assertSame(RAR, Type.toType("rar"));
	}

	@Test
	public final void the_type_PAR_is_identified_correctly()
	{
		assertSame(PAR, Type.toType("par"));
	}

	@Test
	public final void other_types_are_identified_as_OTHER()
	{
		assertSame(OTHER, Type.toType("..."));
	}

	@Test
	public final void sanity_check_to_reach_100_percent_coverage_1()
	{
		assertEquals(9, Type.values().length);
	}

	@Test
	public final void sanity_check_to_reach_100_percent_coverage_2()
	{
		assertSame(POM, Type.valueOf("POM"));
	}
}
