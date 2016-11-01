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
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public final class Command
{
	public static enum Verbosity
	{
		NORMAL,
		VERBOSE
	}

	public static enum Grouping
	{
		BY_GAV,
		BY_POM
	}

	public static enum RunType
	{
		ACTUAL,
		DRY_RUN
	}

	public static enum Interactivity
	{
		NOT_INTERACTIVE,
		INTERACTIVE
	}

	public static enum Colour
	{
		NO_COLOUR,
		COLOUR
	}

	public static final class List
	{
		public final File rootDir;

		public final ImmutableList<String> patterns;

		public final Verbosity verbosity;

		public final Grouping grouping;

		public List(
				final File rootDir,
				final ImmutableList<String> patterns,
				final Verbosity verbosity,
				final Grouping grouping)
		{
			Preconditions.checkNotNull(rootDir, "Missing 'rootDir'.");
			Preconditions.checkNotNull(patterns, "Missing 'patterns'.");
			Preconditions.checkNotNull(verbosity, "Missing 'verbosity'.");
			Preconditions.checkNotNull(grouping, "Missing 'grouping'.");
			this.rootDir = rootDir;
			this.patterns = patterns;
			this.verbosity = verbosity;
			this.grouping = grouping;
		}
	}

	public static final class SetProperty
	{
		public final File rootDir;

		public final RunType runType;

		public final Interactivity interactivity;

		public final Colour colour;

		public final String key;

		public final String value;

		public final ImmutableList<String> gavs;

		public SetProperty(
				final File rootDir,
				final RunType runType,
				final Interactivity interactivity,
				final Colour colour,
				final String key,
				final String value,
				final ImmutableList<String> gavs)
		{
			Preconditions.checkNotNull(rootDir, "Missing 'rootDir'.");
			Preconditions.checkNotNull(runType, "Missing 'runType'.");
			Preconditions.checkNotNull(interactivity, "Missing 'interactivity'.");
			Preconditions.checkNotNull(colour, "Missing 'colour'.");
			Preconditions.checkNotNull(key, "Missing 'key'.");
			Preconditions.checkArgument(key.length() > 0, "Empty 'key'.");
			Preconditions.checkNotNull(value, "Missing 'value'.");
			Preconditions.checkNotNull(gavs, "Missing 'gavs'.");
			this.rootDir = rootDir;
			this.runType = runType;
			this.interactivity = interactivity;
			this.colour = colour;
			this.key = key;
			this.value = value;
			this.gavs = gavs;
		}
	}

	public static final class SetVersion
	{
		public final File rootDir;

		public final RunType runType;

		public final Interactivity interactivity;

		public final Colour colour;

		public final ImmutableList<String> gavs;

		public SetVersion(
				final File rootDir,
				final RunType runType,
				final Interactivity interactivity,
				final Colour colour,
				final ImmutableList<String> gavs)
		{
			Preconditions.checkNotNull(rootDir, "Missing 'rootDir'.");
			Preconditions.checkNotNull(runType, "Missing 'runType'.");
			Preconditions.checkNotNull(interactivity, "Missing 'interactivity'.");
			Preconditions.checkNotNull(colour, "Missing 'colour'.");
			Preconditions.checkNotNull(gavs, "Missing 'gavs'.");
			this.rootDir = rootDir;
			this.runType = runType;
			this.interactivity = interactivity;
			this.colour = colour;
			this.gavs = gavs;
		}
	}

	public static final class Release
	{
		public final File rootDir;

		public final RunType runType;

		public final Interactivity interactivity;

		public final Colour colour;

		public final ImmutableList<String> exclusions;

		public Release(
				final File rootDir,
				final RunType runType,
				final Interactivity interactivity,
				final Colour colour,
				final ImmutableList<String> exclusions)
		{
			Preconditions.checkNotNull(rootDir, "Missing 'rootDir'.");
			Preconditions.checkNotNull(runType, "Missing 'runType'.");
			Preconditions.checkNotNull(interactivity, "Missing 'interactivity'.");
			Preconditions.checkNotNull(colour, "Missing 'colour'.");
			Preconditions.checkNotNull(exclusions, "Missing 'exclusions'.");
			this.rootDir = rootDir;
			this.runType = runType;
			this.interactivity = interactivity;
			this.colour = colour;
			this.exclusions = exclusions;
		}
	}
}
