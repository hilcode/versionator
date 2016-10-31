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

import static com.github.hilcode.versionator.api.Mode.AUTOMATIC;
import static com.github.hilcode.versionator.api.Mode.INTERACTIVE;
import static com.github.hilcode.versionator.api.RunType.ACTUAL;
import static com.github.hilcode.versionator.api.RunType.DRY_RUN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import com.github.hilcode.versionator.api.ReleaseRequest;
import com.github.hilcode.versionator.api.VersionChangeRequest;
import com.github.hilcode.versionator.impl.DefaultTool;
import com.github.hilcode.versionator.impl.Usage;
import com.github.hilcode.versionator.maven.Gav;
import com.github.hilcode.versionator.maven.GroupArtifact;
import com.github.hilcode.versionator.misc.Either;
import com.github.hilcode.versionator.misc.Generators;
import com.github.hilcode.versionator.misc.Result;
import com.google.common.collect.ImmutableList;

public final class DefaultToolTest
{
	private final String invalidVersion = "invalid:version";

	private GroupArtifact groupArtifact1;

	private GroupArtifact groupArtifact2;

	private Gav gav1;

	private Gav gav2;

	private String oldVersion;

	private String newVersion;

	private DefaultTool tool;

	@Before
	public void setUp()
	{
		final Random rnd = new Random();
		this.groupArtifact1 = Generators.groupArtifact(rnd);
		this.groupArtifact2 = Generators.groupArtifact(rnd);
		this.gav1 = Generators.gav(rnd);
		this.gav2 = Generators.gav(rnd);
		this.oldVersion = Generators.version(rnd);
		this.newVersion = Generators.version(rnd);
		this.tool = new DefaultTool();
	}

	@Test
	public void test001()
	{
		assertEquals("No arguments provided.\n\nPerhaps try --help?", invoke().failure());
	}

	@Test
	public void test002()
	{
		assertEquals("Unknown flag: '-z'.\n\nPerhaps try --help?", invoke("-z").failure());
	}

	@Test
	public void test003()
	{
		assertEquals("Unknown flag: '-z'.\n\nPerhaps try --help?", invoke("-inz").failure());
	}

	@Test
	public void test004()
	{
		assertEquals(Usage.INSTANCE.toString(), invoke("-h").failure());
	}

	@Test
	public void test005()
	{
		assertEquals(Usage.INSTANCE.toString(), invoke("--help").failure());
	}

	@Test
	public void test006()
	{
		assertEquals(
				"Missing command, expected either 'set-version' or 'release'.\n\nPerhaps try --help?",
				invoke("-n").failure());
	}

	@Test
	public void test007()
	{
		assertEquals(
				"Missing command, expected either 'set-version' or 'release'.\n\nPerhaps try --help?",
				invoke("--dry-run").failure());
	}

	@Test
	public void test008()
	{
		assertEquals(
				"Missing command, expected either 'set-version' or 'release'.\n\nPerhaps try --help?",
				invoke("-i").failure());
	}

	@Test
	public void test009()
	{
		assertEquals(
				"Missing command, expected either 'set-version' or 'release'.\n\nPerhaps try --help?",
				invoke("--interactive").failure());
	}

	@Test
	public void test010()
	{
		assertEquals(
				"Missing command, expected either 'set-version' or 'release'.\n\nPerhaps try --help?",
				invoke("--interactive", "-n", "--dry-run", "-i", "-in").failure());
	}

	@Test
	public void test011()
	{
		assertEquals(
				"Expected either a GAV or a group:artifact followed by an old and a new version, but you provided nothing.\n\nPerhaps try --help?",
				invoke("set-version").failure());
	}

	@Test
	public void test012()
	{
		assertEquals(
				"Invalid command: 'not-a-command' (only 'set-version' and 'release' are valid).\n\nPerhaps try --help?",
				invoke("not-a-command").failure());
	}

	@Test
	public void test013()
	{
		final ReleaseRequest request = invoke("release").success().right();
		assertSame(AUTOMATIC, request.mode);
		assertSame(ACTUAL, request.runType);
		assertEquals(0, request.exclusions.size());
	}

	@Test
	public void test014()
	{
		final ReleaseRequest request = invoke("release", "-n").success().right();
		assertSame(AUTOMATIC, request.mode);
		assertSame(DRY_RUN, request.runType);
		assertEquals(0, request.exclusions.size());
	}

	@Test
	public void test015()
	{
		final ReleaseRequest request = invoke("--interactive", "release", "-n").success().right();
		assertSame(INTERACTIVE, request.mode);
		assertSame(DRY_RUN, request.runType);
		assertEquals(0, request.exclusions.size());
	}

	@Test
	public void test016()
	{
		final ReleaseRequest request = invoke("-n", "release").success().right();
		assertSame(AUTOMATIC, request.mode);
		assertSame(DRY_RUN, request.runType);
		assertEquals(0, request.exclusions.size());
	}

	@Test
	public void test017()
	{
		final ReleaseRequest request = invoke("-in", "release").success().right();
		assertSame(INTERACTIVE, request.mode);
		assertSame(DRY_RUN, request.runType);
		assertEquals(0, request.exclusions.size());
	}

	@Test
	public void test018()
	{
		assertEquals(
				String.format(
						"Expected '-x' or '--exclude' but found '%s' instead.\n\nPerhaps try --help?",
						this.gav1.toText()),
				invoke("release", this.gav1.toText()).failure());
	}

	@Test
	public void test019()
	{
		assertEquals(
				String.format(
						"Invalid group and artifact: '%s'.\n\nPerhaps try --help?",
						this.gav1.toText()),
				invoke("release", "-x", this.gav1.toText()).failure());
	}

	@Test
	public void test020()
	{
		assertEquals(
				String.format(
						"Expected a GAV, but you provided: '%s'.\n\nPerhaps try --help?",
						this.groupArtifact1.toText()),
				invoke("set-version", this.groupArtifact1.toText()).failure());
	}

	@Test
	public void test021()
	{
		assertEquals(
				String.format(
						"Found a group:artifact (%s) but '%s' is not a valid version.\n\nPerhaps try --help?",
						this.groupArtifact1.toText(),
						this.invalidVersion),
				invoke("set-version", this.groupArtifact1.toText(), this.invalidVersion, this.newVersion).failure());
	}

	@Test
	public void test022()
	{
		assertEquals(
				String.format(
						"Found a group:artifact (%s) but '%s' is not a valid version.\n\nPerhaps try --help?",
						this.groupArtifact1.toText(),
						this.invalidVersion),
				invoke("set-version", this.groupArtifact1.toText(), this.oldVersion, this.invalidVersion).failure());
	}

	@Test
	public void test023()
	{
		assertEquals(
				String.format(
						"Expected a GAV, but you provided: '%s'.\n\nPerhaps try --help?",
						this.newVersion),
				invoke("set-version", this.gav1.toText(), this.oldVersion, this.newVersion).failure());
	}

	@Test
	public void test024()
	{
		assertEquals(
				String.format(
						"Invalid GAV: '%s'.\n\nPerhaps try --help?",
						this.groupArtifact1.toText()),
				invoke(
						"set-version",
						this.groupArtifact1.toText(),
						this.groupArtifact2.toText(),
						this.oldVersion,
						this.newVersion)
								.failure());
	}

	@Test
	public void test025()
	{
		assertEquals(
				String.format(
						"Invalid GAV: '%s'.\n\nPerhaps try --help?",
						this.groupArtifact1.toText()),
				invoke(
						"set-version",
						this.groupArtifact1.toText(),
						this.gav1.toText())
								.failure());
	}

	@Test
	public void test026()
	{
		final ReleaseRequest request =
				invoke(
						"release",
						"-nix",
						this.groupArtifact1.toText())
								.success()
								.right();
		assertSame(INTERACTIVE, request.mode);
		assertSame(DRY_RUN, request.runType);
		assertEquals(1, request.exclusions.size());
		assertEquals(this.groupArtifact1, request.exclusions.get(0));
	}

	@Test
	public void test027()
	{
		final ReleaseRequest request =
				invoke(
						"release",
						"--exclude",
						this.groupArtifact1.toText(),
						this.groupArtifact2.toText())
								.success()
								.right();
		assertSame(AUTOMATIC, request.mode);
		assertSame(ACTUAL, request.runType);
		assertEquals(2, request.exclusions.size());
		assertEquals(this.groupArtifact1, request.exclusions.get(0));
		assertEquals(this.groupArtifact2, request.exclusions.get(1));
	}

	@Test
	public void test028()
	{
		final VersionChangeRequest request =
				invoke(
						"set-version",
						this.gav1.toText())
								.success()
								.left();
		assertSame(AUTOMATIC, request.mode);
		assertSame(ACTUAL, request.runType);
		assertEquals(this.gav1, request.versionChange.fromVersionToVersion.right().gav);
		assertEquals(0, request.versionChange.gavs.size());
	}

	@Test
	public void test029()
	{
		final VersionChangeRequest request =
				invoke(
						"set-version",
						this.gav1.toText(),
						this.gav2.toText())
								.success()
								.left();
		assertSame(AUTOMATIC, request.mode);
		assertSame(ACTUAL, request.runType);
		assertEquals(this.gav2, request.versionChange.fromVersionToVersion.right().gav);
		assertEquals(1, request.versionChange.gavs.size());
		assertEquals(this.gav1, request.versionChange.gavs.get(0));
	}

	@Test
	public void test030()
	{
		final VersionChangeRequest request =
				invoke(
						"set-version",
						this.groupArtifact1.toText(),
						this.oldVersion,
						this.newVersion)
								.success()
								.left();
		assertSame(AUTOMATIC, request.mode);
		assertSame(ACTUAL, request.runType);
		assertEquals(this.groupArtifact1, request.versionChange.fromVersionToVersion.left().groupArtifact);
		assertEquals(this.oldVersion, request.versionChange.fromVersionToVersion.left().oldVersion);
		assertEquals(this.newVersion, request.versionChange.fromVersionToVersion.left().newVersion);
		assertEquals(0, request.versionChange.gavs.size());
	}

	@Test
	public void test031()
	{
		final VersionChangeRequest request =
				invoke(
						"set-version",
						this.gav1.toText(),
						this.groupArtifact1.toText(),
						this.oldVersion,
						this.newVersion)
								.success()
								.left();
		assertSame(AUTOMATIC, request.mode);
		assertSame(ACTUAL, request.runType);
		assertEquals(this.groupArtifact1, request.versionChange.fromVersionToVersion.left().groupArtifact);
		assertEquals(this.oldVersion, request.versionChange.fromVersionToVersion.left().oldVersion);
		assertEquals(this.newVersion, request.versionChange.fromVersionToVersion.left().newVersion);
		assertEquals(1, request.versionChange.gavs.size());
		assertEquals(this.gav1, request.versionChange.gavs.get(0));
	}

	private Result<String, Either<VersionChangeRequest, ReleaseRequest>> invoke(final String... args)
	{
		return this.tool.run(ImmutableList.<String> copyOf(args));
	}
}
