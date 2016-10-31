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

import static com.github.hilcode.versionator.maven.Type.JAR;
import static com.github.hilcode.versionator.maven.Type.POM;
import com.google.common.base.Preconditions;

public final class Main4
{
	public static void main(final String[] args)
	{
		Preconditions.checkState(Version.Builder.instance("1", POM).equals(new Version.Builder.DefaultNumber(1, false)));
		Preconditions.checkState(Version.Builder.instance("1", JAR).equals(new Version.Builder.DefaultCommon(1, false)));
		Preconditions.checkState(Version.Builder.instance("1.0", POM).equals(new Version.Builder.DefaultCommon(1, false)));
		Preconditions.checkState(Version.Builder.instance("1.0", JAR).equals(new Version.Builder.DefaultCommon(1, false)));
		Preconditions.checkState(Version.Builder.instance("1.2", POM).equals(new Version.Builder.DefaultCommon(1, 2, false)));
		Preconditions.checkState(Version.Builder.instance("1.2", JAR).equals(new Version.Builder.DefaultCommon(1, 2, false)));
		Preconditions.checkState(Version.Builder.instance("1.0.0", POM).equals(new Version.Builder.DefaultCommon(1, false)));
		Preconditions.checkState(Version.Builder.instance("1.0.0", JAR).equals(new Version.Builder.DefaultCommon(1, false)));
		Preconditions.checkState(Version.Builder.instance("1.2.0", POM).equals(new Version.Builder.DefaultCommon(1, 2, false)));
		Preconditions.checkState(Version.Builder.instance("1.2.0", JAR).equals(new Version.Builder.DefaultCommon(1, 2, false)));
		Preconditions.checkState(Version.Builder.instance("1.2.3", POM).equals(new Version.Builder.DefaultCommon(1, 2, 3, false)));
		Preconditions.checkState(Version.Builder.instance("1.2.3", JAR).equals(new Version.Builder.DefaultCommon(1, 2, 3, false)));
		Preconditions.checkState(Version.Builder.instance("1.2.3.0", POM).equals(new Version.Builder.DefaultUnusual("1.2.3.0", false)));
		Preconditions.checkState(Version.Builder.instance("1.2.3.0", JAR).equals(new Version.Builder.DefaultUnusual("1.2.3.0", false)));
		Preconditions.checkState(Version.Builder.instance("1.2.3.4", POM).equals(new Version.Builder.DefaultUnusual("1.2.3.4", false)));
		Preconditions.checkState(Version.Builder.instance("1.2.3.4", JAR).equals(new Version.Builder.DefaultUnusual("1.2.3.4", false)));
		Preconditions.checkState(Version.Builder.instance("alpha", POM).equals(new Version.Builder.DefaultUnusual("alpha", false)));
		Preconditions.checkState(Version.Builder.instance("alpha", JAR).equals(new Version.Builder.DefaultUnusual("alpha", false)));
		Preconditions.checkState(Version.Builder.instance("alpha.1", POM).equals(new Version.Builder.DefaultUnusual("alpha.1", false)));
		Preconditions.checkState(Version.Builder.instance("alpha.1", JAR).equals(new Version.Builder.DefaultUnusual("alpha.1", false)));
		Preconditions.checkState(Version.Builder.instance("alpha.10", POM).equals(new Version.Builder.DefaultUnusual("alpha.10", false)));
		Preconditions.checkState(Version.Builder.instance("alpha.10", JAR).equals(new Version.Builder.DefaultUnusual("alpha.10", false)));
		Preconditions.checkState(Version.Builder.instance("alpha.100", POM).equals(new Version.Builder.DefaultUnusual("alpha.100", false)));
		Preconditions.checkState(Version.Builder.instance("alpha.100", JAR).equals(new Version.Builder.DefaultUnusual("alpha.100", false)));
		Preconditions.checkState(Version.Builder.instance("alpha-1", POM).equals(new Version.Builder.DefaultUnusual("alpha-1", false)));
		Preconditions.checkState(Version.Builder.instance("alpha-1", JAR).equals(new Version.Builder.DefaultUnusual("alpha-1", false)));
		Preconditions.checkState(Version.Builder.instance("alpha-10", POM).equals(new Version.Builder.DefaultUnusual("alpha-10", false)));
		Preconditions.checkState(Version.Builder.instance("alpha-10", JAR).equals(new Version.Builder.DefaultUnusual("alpha-10", false)));
		Preconditions.checkState(Version.Builder.instance("alpha-100", POM).equals(new Version.Builder.DefaultUnusual("alpha-100", false)));
		Preconditions.checkState(Version.Builder.instance("alpha-100", JAR).equals(new Version.Builder.DefaultUnusual("alpha-100", false)));
		Preconditions.checkState(Version.Builder.instance("1-SNAPSHOT", POM).equals(new Version.Builder.DefaultNumber(1, true)));
		Preconditions.checkState(Version.Builder.instance("1-SNAPSHOT", JAR).equals(new Version.Builder.DefaultCommon(1, true)));
		Preconditions.checkState(Version.Builder.instance("1.0-SNAPSHOT", POM).equals(new Version.Builder.DefaultCommon(1, true)));
		Preconditions.checkState(Version.Builder.instance("1.0-SNAPSHOT", JAR).equals(new Version.Builder.DefaultCommon(1, true)));
		Preconditions.checkState(Version.Builder.instance("1.2-SNAPSHOT", POM).equals(new Version.Builder.DefaultCommon(1, 2, true)));
		Preconditions.checkState(Version.Builder.instance("1.2-SNAPSHOT", JAR).equals(new Version.Builder.DefaultCommon(1, 2, true)));
		Preconditions.checkState(Version.Builder.instance("1.0.0-SNAPSHOT", POM).equals(new Version.Builder.DefaultCommon(1, true)));
		Preconditions.checkState(Version.Builder.instance("1.0.0-SNAPSHOT", JAR).equals(new Version.Builder.DefaultCommon(1, true)));
		Preconditions.checkState(Version.Builder.instance("1.2.0-SNAPSHOT", POM).equals(new Version.Builder.DefaultCommon(1, 2, true)));
		Preconditions.checkState(Version.Builder.instance("1.2.0-SNAPSHOT", JAR).equals(new Version.Builder.DefaultCommon(1, 2, true)));
		Preconditions.checkState(Version.Builder.instance("1.2.3-SNAPSHOT", POM).equals(new Version.Builder.DefaultCommon(1, 2, 3, true)));
		Preconditions.checkState(Version.Builder.instance("1.2.3-SNAPSHOT", JAR).equals(new Version.Builder.DefaultCommon(1, 2, 3, true)));
		Preconditions.checkState(Version.Builder.instance("1.2.3.0-SNAPSHOT", POM).equals(new Version.Builder.DefaultUnusual("1.2.3.0", true)));
		Preconditions.checkState(Version.Builder.instance("1.2.3.0-SNAPSHOT", JAR).equals(new Version.Builder.DefaultUnusual("1.2.3.0", true)));
		Preconditions.checkState(Version.Builder.instance("1.2.3.4-SNAPSHOT", POM).equals(new Version.Builder.DefaultUnusual("1.2.3.4", true)));
		Preconditions.checkState(Version.Builder.instance("1.2.3.4-SNAPSHOT", JAR).equals(new Version.Builder.DefaultUnusual("1.2.3.4", true)));
		Preconditions.checkState(Version.Builder.instance("alpha-SNAPSHOT", POM).equals(new Version.Builder.DefaultUnusual("alpha", true)));
		Preconditions.checkState(Version.Builder.instance("alpha-SNAPSHOT", JAR).equals(new Version.Builder.DefaultUnusual("alpha", true)));
		Preconditions.checkState(Version.Builder.instance("alpha.1-SNAPSHOT", POM).equals(new Version.Builder.DefaultUnusual("alpha.1", true)));
		Preconditions.checkState(Version.Builder.instance("alpha.1-SNAPSHOT", JAR).equals(new Version.Builder.DefaultUnusual("alpha.1", true)));
		Preconditions.checkState(Version.Builder.instance("alpha.10-SNAPSHOT", POM).equals(new Version.Builder.DefaultUnusual("alpha.10", true)));
		Preconditions.checkState(Version.Builder.instance("alpha.10-SNAPSHOT", JAR).equals(new Version.Builder.DefaultUnusual("alpha.10", true)));
		Preconditions.checkState(Version.Builder.instance("alpha.100-SNAPSHOT", POM).equals(new Version.Builder.DefaultUnusual("alpha.100", true)));
		Preconditions.checkState(Version.Builder.instance("alpha.100-SNAPSHOT", JAR).equals(new Version.Builder.DefaultUnusual("alpha.100", true)));
		Preconditions.checkState(Version.Builder.instance("alpha-1-SNAPSHOT", POM).equals(new Version.Builder.DefaultUnusual("alpha-1", true)));
		Preconditions.checkState(Version.Builder.instance("alpha-1-SNAPSHOT", JAR).equals(new Version.Builder.DefaultUnusual("alpha-1", true)));
		Preconditions.checkState(Version.Builder.instance("alpha-10-SNAPSHOT", POM).equals(new Version.Builder.DefaultUnusual("alpha-10", true)));
		Preconditions.checkState(Version.Builder.instance("alpha-10-SNAPSHOT", JAR).equals(new Version.Builder.DefaultUnusual("alpha-10", true)));
		Preconditions.checkState(Version.Builder.instance("alpha-100-SNAPSHOT", POM).equals(new Version.Builder.DefaultUnusual("alpha-100", true)));
		Preconditions.checkState(Version.Builder.instance("alpha-100-SNAPSHOT", JAR).equals(new Version.Builder.DefaultUnusual("alpha-100", true)));
		final Version x = Version.Builder.instance("1-SNAPSHOT", POM).toRelease();
		System.out.println(x.toText());
	}
}
