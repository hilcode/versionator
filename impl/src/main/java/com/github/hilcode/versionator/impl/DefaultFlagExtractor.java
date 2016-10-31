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

import static com.github.hilcode.versionator.api.Command.RELEASE;
import static com.github.hilcode.versionator.api.Command.SET_VERSION;
import static com.github.hilcode.versionator.api.Mode.AUTOMATIC;
import static com.github.hilcode.versionator.api.Mode.INTERACTIVE;
import static com.github.hilcode.versionator.api.RunType.ACTUAL;
import static com.github.hilcode.versionator.api.RunType.DRY_RUN;
import com.github.hilcode.versionator.api.Command;
import com.github.hilcode.versionator.api.FlagExtractor;
import com.github.hilcode.versionator.api.Flags;
import com.github.hilcode.versionator.api.Mode;
import com.github.hilcode.versionator.api.RunType;
import com.github.hilcode.versionator.misc.Result;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public final class DefaultFlagExtractor
	implements
		FlagExtractor
{
	private final Flags.Builder flagsBuilder;

	public DefaultFlagExtractor(final Flags.Builder flagsBuilder)
	{
		this.flagsBuilder = flagsBuilder;
	}

	@Override
	public Result<String, Flags> extract(final ImmutableList<String> arguments)
	{
		Preconditions.checkNotNull(arguments, "Missing 'arguments'.");
		if (arguments.isEmpty())
		{
			return Result.failure("No arguments provided.\n\nPerhaps try --help?");
		}
		else
		{
			final ImmutableList.Builder<String> expandedArgumentsBuilder = ImmutableList.builder();
			for (final String argument : arguments)
			{
				if (argument.startsWith("-") && !argument.startsWith("--") && argument.length() > 2)
				{
					for (int i = 1; i < argument.length(); i++)
					{
						expandedArgumentsBuilder.add("-" + argument.charAt(i));
					}
				}
				else
				{
					expandedArgumentsBuilder.add(argument);
				}
			}
			return extractFlags(expandedArgumentsBuilder.build());
		}
	}

	Result<String, Flags> extractFlags(final ImmutableList<String> arguments)
	{
		final ImmutableList.Builder<String> argumentsBuilder = ImmutableList.builder();
		RunType runType = ACTUAL;
		Mode mode = AUTOMATIC;
		String commandAsText = "";
		for (final String arg : arguments)
		{
			if (arg.equals("-h") || arg.equals("--help"))
			{
				return Result.failure(Usage.INSTANCE.toString());
			}
			else if (arg.equals("-n") || arg.equals("--dry-run"))
			{
				runType = DRY_RUN;
			}
			else if (arg.equals("-i") || arg.equals("--interactive"))
			{
				mode = INTERACTIVE;
			}
			else
			{
				if (commandAsText.isEmpty())
				{
					commandAsText = arg;
				}
				else
				{
					argumentsBuilder.add(arg);
				}
			}
		}
		return extractCommand(argumentsBuilder, runType, mode, commandAsText);
	}

	Result<String, Flags> extractCommand(
			final ImmutableList.Builder<String> argumentsBuilder,
			final RunType runType,
			final Mode mode,
			final String commandAsText)
	{
		if (commandAsText.isEmpty())
		{
			return Result.failure(
					"Missing command, expected either 'set-version' or 'release'.\n\nPerhaps try --help?");
		}
		else
		{
			final ImmutableList<String> arguments = argumentsBuilder.build();
			final Result<String, Command> command = extractCommand(commandAsText);
			if (command.isFailure())
			{
				return Result.asFailure(command);
			}
			else
			{
				return Result.success(this.flagsBuilder.build(runType, mode, command.success(), arguments));
			}
		}
	}

	Result<String, Command> extractCommand(final String commandAsText)
	{
		if (commandAsText.equals("set-version"))
		{
			return Result.success(SET_VERSION);
		}
		else if (commandAsText.equals("release"))
		{
			return Result.success(RELEASE);
		}
		else
		{
			final String message =
					String.format(
							commandAsText.startsWith("-")
									? "Unknown flag: '%s'."
									: "Invalid command: '%s' (only 'set-version' and 'release' are valid).",
							commandAsText);
			return Result.failure(message + "\n\nPerhaps try --help?");
		}
	}
}
