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
package com.github.hilcode.versionator.impl;

import com.google.common.collect.ImmutableList;

public interface Usage
{
	ImmutableList<String> LINES = ImmutableList.<String> of(
			"versionator -h|--help",
			"versionator set-version [-n|--dry-run] [-i|--interactive] {G:A:V .. G:A:V} G:A:V|G:A OLD_VERSION NEW_VERSION",
			"versionator release     [-n|--dry-run] [-i|--interactive] [-x|--exclude {G:A .. G:A}]",
			"",
			"-h|-?|--help       prints this help message;",
			"-n|--dry-run       runs normally but does not actually make any changes;",
			"-i|--interactive   allows the user to confirm/deny/alter changes;",
			"G                  a Maven group ID;",
			"A                  a Maven artifact ID;",
			"V                  a Maven version.",
			"",
			"1) Setting a POM's or a dependency's version",
			"a) versionator set-version G:A:V",
			"   This forces an update of all occurrences of group ID 'G' and artifact ID 'A' from their current versions (no matter",
			"   which) to version 'V'.",
			"   This may force additional changes in other POMs (specifically, POMs that do not currently have a SNAPSHOT version",
			"   will be changed as well). Such POMs will go from X.Y.Z to X.Y.(Z+1)-SNAPHOT, unless 'interactive' is requested in",
			"   which case the user will be asked to input the preferred version.",
			"b) versionator set-version {G:A:V .. G:A:V} G:A:V",
			"   This forces an update of all occurrences of group ID 'G' and artifact ID 'A' from their current versions (no matter",
			"   which) to version 'V'.",
			"   This may force additional changes in other POMs (specifically, POMs that do not currently have a SNAPSHOT version",
			"   will be changed as well). Such POMs will go from X.Y.Z to X.Y.(Z+1)-SNAPHOT, unless 'interactive' is requested, or",
			"   one of the G:A:V listed provides the preferred version.",
			"c) versionator set-version G:A OLD_VERSION NEW_VERSION",
			"   This forces an update of all occurrences of group ID 'G' and artifact ID 'A' with version 'OLD_VERSION' to version",
			"   'NEW_VERSION'. Any occurrences of group ID 'G' and artifact ID 'A' with a version other than 'OLD_VERSION' are",
			"   unaffected.",
			"   This may force additional changes in other POMs (specifically, POMs that do not currently have a SNAPSHOT version",
			"   will be changed as well). Such POMs will go from X.Y.Z to X.Y.(Z+1)-SNAPHOT, unless 'interactive' is requested in",
			"   which case the user will be asked to input the preferred version.",
			"d) versionator set-version {G:A:V .. G:A:V} G:A OLD_VERSION NEW_VERSION",
			"   This forces an update of all occurrences of group ID 'G' and artifact ID 'A' with version 'OLD_VERSION' to version",
			"   'NEW_VERSION'. Any occurrences of group ID 'G' and artifact ID 'A' with a version other than 'OLD_VERSION' are",
			"   unaffected.",
			"   This may force additional changes in other POMs (specifically, POMs that do not currently have a SNAPSHOT version",
			"   will be changed as well). Such POMs will go from X.Y.Z to X.Y.(Z+1)-SNAPHOT, unless 'interactive' is requested, or",
			"   one of the G:A:V listed provides the preferred version.",
			"",
			"2) Releasing (i.e. removing '-SNAPSHOT')",
			"a) versionator release",
			"   This forces an update in all POMs of all dependencies with a SNAPSHOT version to the corresponding release version.",
			"   Any POMs with a SNAPSHOT version will also be updated to their corresponding release version.",
			"   If 'interactive' is requested then the user will be asked to confirm each change.",
			"b) versionator release [-x|--exclude {G:A .. G:A}]",
			"   This forces an update in all POMs of all dependencies (except those listed as excluded) with a SNAPSHOT version to",
			"   the corresponding release version. Any POMs with a SNAPSHOT version (except those listed as excluded) will also be",
			"   updated to the corresponding release version.",
			"   If 'interactive' is requested then the user will be asked to confirm each change.");

	Usage INSTANCE = new Usage()
	{
		@Override
		public String toString()
		{
			final StringBuilder sb = new StringBuilder();
			for (final String line : Usage.LINES)
			{
				sb.append(line).append('\n');
			}
			return sb.toString();
		}
	};
}
