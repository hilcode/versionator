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

import java.util.Collections;
import java.util.List;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.internal.Lists;

public final class CommandLineInterface
{
	public static final class Help
	{
		@Parameter(
				names =
				{
					"-h", "--help"
				},
				help = true)
		public Boolean help;
	}

	@Parameters(separators = "=", commandDescription = "Lists dependencies.")
	public static final class CommandList
	{
		public static final String COMMAND = "list";

		@Parameter(
				names =
				{
					"-d", "--directory"
				},
				description = "The root directory.")
		public String rootDir = ".";

		@Parameter(
				names =
				{
					"-p", "--patterns"
				},
				variableArity = true,
				description = "The glob patterns (using '*' and '?') that describe the included " +
						"and excluded dependencies; starting a pattern with '!' indicates " +
						"it is an exclusion. Patterns are processed in order, i.e. later " +
						"patterns can override earlier patterns.")
		public List<String> patterns = Lists.newArrayList("*:*:*");

		@Parameter(
				names =
				{
					"-v", "--verbose"
				},
				description = "Also shows the POM file (ignored if using --group-by-pom).")
		public boolean verbose = false;

		@Parameter(
				names =
				{
					"-g", "--group-by-pom"
				},
				description = "Lists the dependencies by POM.")
		public boolean groupByPom = false;
	}

	@Parameters(separators = "=", commandDescription = "Sets a version")
	public static final class CommandSetVersion
	{
		public static final String COMMAND = "set-version";

		@Parameter(
				names =
				{
					"-d", "--directory"
				},
				description = "The root directory.")
		public String rootDir = ".";

		@Parameter(
				names =
				{
					"-n", "--dry-run"
				},
				description = "Whether to do a dry run and not actually make any changes.")
		public boolean dryRun = false;

		@Parameter(
				hidden = true,
				names =
				{
					"-i", "--interactive"
				},
				description = "Whether to involve the user.")
		public boolean interactive = false;

		@Parameter(
				names =
				{
					"-c", "--no-colour"
				},
				description = "Whether to suppress colours in output.")
		public boolean colourless = false;

		@Parameter(
				description = "GAV {GAV}",
				required = true)
		public List<String> gavs;
	}

	@Parameters(separators = "=", commandDescription = "Releases SNAPSHOT versions")
	public static final class CommandRelease
	{
		public static final String COMMAND = "release";

		@Parameter(
				names =
				{
					"-d", "--directory"
				},
				description = "The root directory.")
		public String rootDir = ".";

		@Parameter(
				names =
				{
					"-n", "--dry-run"
				},
				description = "Whether to do a dry run and not actually make any changes.")
		public boolean dryRun = false;

		@Parameter(
				hidden = true,
				names =
				{
					"-i", "--interactive"
				},
				description = "Whether to involve the user.")
		public boolean interactive = false;

		@Parameter(
				names =
				{
					"-c", "--no-colour"
				},
				description = "Whether to suppress colours in output.")
		public boolean colourless = false;

		@Parameter(
				names =
				{
					"-x", "--exclude"
				},
				description = "Which GAVs not to release.")
		public List<String> exclusions = Collections.emptyList();
	}
}
