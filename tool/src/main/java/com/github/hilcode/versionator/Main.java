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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.github.hilcode.versionator.CommandLineInterface.CommandRelease;
import com.github.hilcode.versionator.CommandLineInterface.CommandSetVersion;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public final class Main
{
	public static final void main(final String[] args) throws Exception
	{
		final PomParser pomParser = new DefaultPomParser();
		final PomFinder pomFinder = new DefaultPomFinder(pomParser);
		final CommandLineInterface.Basics basics = new CommandLineInterface.Basics();
		final JCommander commander = new JCommander(basics);
		final CommandLineInterface.CommandList commandList = new CommandLineInterface.CommandList();
		commander.addCommand(CommandLineInterface.CommandList.COMMAND, commandList);
		final CommandSetVersion commandSetVersion = new CommandSetVersion();
		commander.addCommand(CommandSetVersion.COMMAND, commandSetVersion);
		final CommandRelease commandRelease = new CommandRelease();
		commander.addCommand(CommandRelease.COMMAND, commandRelease);
		try
		{
			commander.parse(args);
			if (basics.help != null || basics.version != null)
			{
				if (basics.version != null)
				{
					final String eol = System.getProperty("line.separator");
					final StringBuilder sb = new StringBuilder();
					sb.append("Versionator ").append(Versionator.VERSION).append(eol);
					sb.append(Versionator.RELEASE_DATE).append(" UTC");
					final TimeZone utc = TimeZone.getTimeZone("UTC");
					if (!TimeZone.getDefault().equals(utc))
					{
						final SimpleDateFormat dateFormatUtc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						dateFormatUtc.setTimeZone(utc);
						final Date releaseDateUtc = dateFormatUtc.parse(Versionator.RELEASE_DATE);
						final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
						final Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(releaseDateUtc.getTime());
						sb.append(" / ").append(dateFormat.format(calendar.getTime()));
					}
					System.out.println(sb.toString());
				}
				if (basics.help != null)
				{
					commander.usage();
				}
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
				new ListExecutor(pomParser, pomFinder, list).execute();
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
				final Model model = Model.BUILDER.build(pomFinder.findAllPoms(setVersion.rootDir));
				final ImmutableList.Builder<Gav> changedGavsBuilder = ImmutableList.builder();
				for (final String gavAsText : setVersion.gavs)
				{
					changedGavsBuilder.add(Gav.BUILDER.build(gavAsText));
				}
				final Model result = Transformers.transformModelWithGavs(model, changedGavsBuilder.build());
				final ModelWriter modelWriter = new ModelWriter(new VersionSetter(), new PropertySetter());
				System.err.println("WRITE OUT MODEL:");
				modelWriter.write(model, result);
				System.err.println("WRITTEN OUT MODEL");
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
				final ImmutableSet.Builder<GroupArtifact> exclusionsBuilder = ImmutableSet.builder();
				for (final String exclusionAsText : release.exclusions)
				{
					exclusionsBuilder.add(GroupArtifact.BUILDER.build(exclusionAsText));
				}
				final ImmutableSet<GroupArtifact> exclusions = exclusionsBuilder.build();
				final Model model = Model.BUILDER.build(pomFinder.findAllPoms(release.rootDir));
				final Model result = Transformers.releaseModel(model, exclusions);
				final ModelWriter modelWriter = new ModelWriter(new VersionSetter(), new PropertySetter());
				modelWriter.write(model, result);
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
