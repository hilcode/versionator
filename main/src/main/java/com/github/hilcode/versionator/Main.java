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

import java.io.File;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.github.hilcode.versionator.CommandLineInterface.CommandRelease;
import com.github.hilcode.versionator.CommandLineInterface.CommandSetVersion;
import com.google.common.collect.ImmutableList;

public final class Main
{
	public static final void main(final String[] args) throws Exception
	{
		final CommandLineInterface.Help help = new CommandLineInterface.Help();
		final JCommander commander = new JCommander(help);
		final CommandLineInterface.CommandList commandList = new CommandLineInterface.CommandList();
		commander.addCommand(CommandLineInterface.CommandList.COMMAND, commandList);
		final CommandSetVersion commandSetVersion = new CommandSetVersion();
		commander.addCommand(CommandSetVersion.COMMAND, commandSetVersion);
		final CommandRelease commandRelease = new CommandRelease();
		commander.addCommand(CommandRelease.COMMAND, commandRelease);
		try
		{
			commander.parse(args);
			if (help.help != null)
			{
				commander.usage();
				return;
			}
			if (CommandLineInterface.CommandList.COMMAND.equals(commander.getParsedCommand()))
			{
				final Command.List list = new Command.List(
						new File(commandList.rootDir),
						ImmutableList.copyOf(commandList.patterns),
						commandList.verbose
								? Command.Verbosity.VERBOSE
								: Command.Verbosity.NORMAL,
						commandList.groupByPom
								? Command.Grouping.BY_POM
								: Command.Grouping.BY_GAV);
				new ListExecutor(list).execute();
			}
			else if (CommandSetVersion.COMMAND.equals(commander.getParsedCommand()))
			{
				final Command.SetVersion setVersion = new Command.SetVersion(
						new File(commandSetVersion.rootDir),
						commandSetVersion.dryRun
								? Command.RunType.DRY_RUN
								: Command.RunType.ACTUAL,
						commandSetVersion.interactive
								? Command.Interactivity.INTERACTIVE
								: Command.Interactivity.NOT_INTERACTIVE,
						commandSetVersion.colourless
								? Command.Colour.NO_COLOUR
								: Command.Colour.COLOUR,
						ImmutableList.<String> copyOf(commandSetVersion.gavs));
				new SetVersionExecutor(setVersion).execute();
			}
			else if (CommandRelease.COMMAND.equals(commander.getParsedCommand()))
			{
				final Command.Release release = new Command.Release(
						new File(commandRelease.rootDir),
						commandRelease.dryRun
								? Command.RunType.DRY_RUN
								: Command.RunType.ACTUAL,
						commandRelease.interactive
								? Command.Interactivity.INTERACTIVE
								: Command.Interactivity.NOT_INTERACTIVE,
						commandRelease.colourless
								? Command.Colour.NO_COLOUR
								: Command.Colour.COLOUR,
						ImmutableList.<String> copyOf(commandRelease.exclusions));
				new ReleaseExecutor(release).execute();
			}
			else
			{
				System.err.println("No command provided. Perhaps try --help?");
			}
		}
		catch (final ParameterException e)
		{
			System.err.println(e.getMessage());
		}
	}
}
