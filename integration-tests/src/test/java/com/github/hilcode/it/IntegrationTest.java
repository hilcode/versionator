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
package com.github.hilcode.it;

import static com.google.common.io.Files.fileTreeTraverser;
import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

public abstract class IntegrationTest
{
	public static final class DevNull
		extends
			OutputStream
	{
		@Override
		public void write(final int b)
		{
			// Ignore.
		}
	}

	public static final PrintStream DEV_NULL = new PrintStream(new DevNull());

	private static PrintStream original;

	@BeforeClass
	public static void setUpBeforeClass()
	{
		original = System.out;
		System.setOut(DEV_NULL);
	}

	@AfterClass
	public static void setUpAfterClass()
	{
		System.setOut(original);
	}

	public static final File SOURCE_DIR = new File("src/test/integration-test-data");

	public static final File TARGET_DIR = new File("target/generated-sources/its");

	public static final void copyFile(final File fromFile, final File toFile)
	{
		try
		{
			Files.copy(fromFile, toFile);
		}
		catch (final Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public static final class FileCopier
		implements
			Consumer<File>
	{
		private final File sourceDir;

		private final File targetDir;

		public FileCopier(final File sourceDir, final File targetDir)
		{
			this.sourceDir = sourceDir;
			this.targetDir = targetDir;
		}

		@Override
		public void accept(final File file)
		{
			if (file.equals(this.sourceDir))
			{
				this.targetDir.mkdirs();
				return;
			}
			final File file_ = new File(file.getPath().substring(this.sourceDir.getPath().length() + 1));
			if (file.isDirectory())
			{
				new File(this.targetDir, file_.getPath()).mkdirs();
			}
			else
			{
				copyFile(file, new File(this.targetDir, file_.getPath()));
			}
		}
	}

	public static final class FileComparator
		implements
			Consumer<File>
	{
		private final File sourceDir;

		private final File targetDir;

		public FileComparator(final File sourceDir, final File targetDir)
		{
			this.sourceDir = sourceDir;
			this.targetDir = targetDir;
		}

		@Override
		public void accept(final File file)
		{
			if (file.equals(this.sourceDir))
			{
				return;
			}
			if (file.isFile())
			{
				final File targetFile = new File(this.targetDir, file.getPath().substring(this.sourceDir.getPath().length() + 1));
				final HashCode source = hash(file);
				final HashCode target = hash(targetFile);
				assertEquals(source, target);
			}
		}

		public static final HashCode hash(final File file)
		{
			try
			{
				return Files.hash(file, Hashing.murmur3_128());
			}
			catch (final IOException e)
			{
				throw new IllegalStateException(e.getMessage(), e);
			}
		}
	}

	public static final File copyTree(final IntegrationTest test)
	{
		final String testDir_ = test.getClass().getSimpleName().toLowerCase().replace('_', '-');
		final File testDir = new File(testDir_, "original");
		final File sourceDir = new File(SOURCE_DIR, testDir.getPath());
		final File targetDir = new File(TARGET_DIR, testDir_);
		fileTreeTraverser()
				.breadthFirstTraversal(sourceDir)
				.forEach(new FileCopier(sourceDir, targetDir));
		return targetDir;
	}

	public static final void compareTree(final IntegrationTest test)
	{
		final String testDir_ = test.getClass().getSimpleName().toLowerCase().replace('_', '-');
		final File testDir = new File(testDir_, "result");
		final File sourceDir = new File(SOURCE_DIR, testDir.getPath());
		final File targetDir = new File(TARGET_DIR, testDir_);
		fileTreeTraverser()
				.breadthFirstTraversal(sourceDir)
				.forEach(new FileComparator(sourceDir, targetDir));
	}

	public File testDir;

	@Before
	public void setUp()
	{
		this.testDir = copyTree(this);
	}

	@After
	public void tearDown()
	{
		compareTree(this);
	}
}
