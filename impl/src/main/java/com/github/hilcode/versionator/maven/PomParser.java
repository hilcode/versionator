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

import java.io.File;
import org.w3c.dom.Document;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

public interface PomParser
{
	Document toDocument(File pomFile);

	String findParentGroupId(Document pom);

	String findParentArtifactId(Document pom);

	String findParentVersion(Document pom);

	String findParentRelativePath(Document pom);

	String findGroupId(Document pom);

	String findArtifactId(Document pom);

	String findVersion(Document pom);

	Optional<Gav> findParentGav(Document pom);

	ImmutableList<String> findModules(Document pom);

	ImmutableList<Property> findProperties(Document pom);

	Gav findGav(Document pom);

	Type findType(Document pom);

	ImmutableList<Gav> findDependencies(Document pom);
}
