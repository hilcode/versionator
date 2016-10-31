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
package com.github.hilcode.versionator.api;

import com.google.common.collect.ImmutableList;

public final class Flags
{
	public interface Builder
	{
		Flags build(RunType runType, Mode mode, Command command, ImmutableList<String> arguments);
	}

	public static final Builder BUILDER = new Builder()
	{
		@Override
		public Flags build(
				final RunType runType,
				final Mode mode,
				final Command command,
				final ImmutableList<String> arguments)
		{
			return new Flags(runType, mode, command, arguments);
		}
	};

	public final RunType runType;

	public final Mode mode;

	public final Command command;

	public final ImmutableList<String> arguments;

	public Flags(
			final RunType runType,
			final Mode mode,
			final Command command,
			final ImmutableList<String> arguments)
	{
		this.runType = runType;
		this.mode = mode;
		this.command = command;
		this.arguments = arguments;
	}
}
