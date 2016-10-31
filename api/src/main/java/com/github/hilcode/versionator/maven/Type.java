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
package com.github.hilcode.versionator.maven;

import com.google.common.collect.ImmutableMap;

public enum Type
{
	POM,
	JAR,
	MAVEN_PLUGIN,
	EJB,
	WAR,
	EAR,
	RAR,
	PAR,
	OTHER;
	//
	private static final ImmutableMap<String, Type> MAP;
	static
	{
		final ImmutableMap.Builder<String, Type> mapBuilder = ImmutableMap.builder();
		MAP = mapBuilder
				.put("pom", POM)
				.put("jar", JAR)
				.put("maven-plugin", MAVEN_PLUGIN)
				.put("ejb", EJB)
				.put("war", WAR)
				.put("ear", EAR)
				.put("rar", RAR)
				.put("par", PAR)
				.build();
	}

	public static final Type toType(final String type)
	{
		final Type type_ = MAP.get(type);
		return type_ != null ? type_ : OTHER;
	}
}
