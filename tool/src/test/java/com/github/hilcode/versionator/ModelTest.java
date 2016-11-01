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

import static org.junit.Assert.assertEquals;
import java.io.File;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.google.common.collect.ImmutableList;

@Ignore
public class ModelTest
{
	private File baseDir;

	private PomFinder pomFinder;

	@Before
	public void setUp()
	{
		this.baseDir = new File("/home/hilco/workspaces/open-source/versionator/integration-tests/src/test/integration-test-data");
		final PomParser pomParser = new DefaultPomParser();
		this.pomFinder = new DefaultPomFinder(pomParser);
	}

	@Test
	public void test0001()
	{
		final Model model = Model.BUILDER.build(this.pomFinder.findAllPoms(new File(this.baseDir, "test-0001/original")));
		model.apply(ImmutableList.of(Gav.BUILDER.build("com.github.hilcode:versionator-it:1.0-SNAPSHOT")));
		assertEquals("com.github.hilcode:versionator-it:1.0-SNAPSHOT", model.poms.get(0).gav.toText());
	}

	@Test
	public void test0002()
	{
		final Model model = Model.BUILDER.build(this.pomFinder.findAllPoms(new File(this.baseDir, "test-0002/original")));
		model.apply(ImmutableList.of(Gav.BUILDER.build("com.github.hilcode:versionator-it-parent:2-SNAPSHOT")));
		final Pom parent = model.poms.get(1);
		assertEquals("com.github.hilcode", parent.gav.groupArtifact.groupId);
		assertEquals("versionator-it-parent", parent.gav.groupArtifact.artifactId);
		assertEquals("2-SNAPSHOT", parent.gav.version.toText());
		final Pom pom = model.poms.get(0);
		assertEquals(parent, pom.parent.get());
		assertEquals("com.github.hilcode", pom.gav.groupArtifact.groupId);
		assertEquals("versionator-it", pom.gav.groupArtifact.artifactId);
		assertEquals("1.0.1-SNAPSHOT", pom.gav.version.toText());
	}

	@Test
	public void test0003()
	{
		final Model model = Model.BUILDER.build(this.pomFinder.findAllPoms(new File(this.baseDir, "test-0003/original")));
		model.apply(ImmutableList.of(Gav.BUILDER.build("com.github.hilcode:versionator-it-grandparent:11-SNAPSHOT")));
		final Pom grandParent = model.poms.get(1);
		assertEquals("com.github.hilcode", grandParent.gav.groupArtifact.groupId);
		assertEquals("versionator-it-grandparent", grandParent.gav.groupArtifact.artifactId);
		assertEquals("11-SNAPSHOT", grandParent.gav.version.toText());
		final Pom parent = model.poms.get(2);
		assertEquals(parent.parent.get(), grandParent);
		assertEquals("com.github.hilcode", parent.gav.groupArtifact.groupId);
		assertEquals("versionator-it-parent", parent.gav.groupArtifact.artifactId);
		assertEquals("101-SNAPSHOT", parent.gav.version.toText());
		final Pom pom = model.poms.get(0);
		assertEquals(pom.parent.get(), parent);
		assertEquals("com.github.hilcode", pom.gav.groupArtifact.groupId);
		assertEquals("versionator-it", pom.gav.groupArtifact.artifactId);
		assertEquals("1.2.4-SNAPSHOT", pom.gav.version.toText());
	}

	@Test
	public void test0004()
	{
		final Model model = Model.BUILDER.build(this.pomFinder.findAllPoms(new File(this.baseDir, "test-0004/original")));
		model.apply(ImmutableList.of(Gav.BUILDER.build("GROUP:ARTIFACT:3.0")));
		final Pom pom = model.poms.get(0);
		assertEquals("com.github.hilcode", pom.gav.groupArtifact.groupId);
		assertEquals("versionator-it", pom.gav.groupArtifact.artifactId);
		assertEquals("0.0.1-SNAPSHOT", pom.gav.version.toText());
		assertEquals(5, pom.dependencies.size());
		for (int i = 0; i < 4; i++)
		{
			final Dependency dependency = pom.dependencies.get(i);
			assertEquals("GROUP", dependency.gav.groupArtifact.groupId);
			assertEquals("ARTIFACT", dependency.gav.groupArtifact.artifactId);
			assertEquals("3.0", dependency.gav.version.toText());
		}
	}

	@Test
	public void test0005()
	{
		final Model model = Model.BUILDER.build(this.pomFinder.findAllPoms(new File(this.baseDir, "test-0005/original")));
		model.apply(ImmutableList.of(Gav.BUILDER.build("NO_GROUP:NO_ARTIFACT:1.0")));
		final Pom pom = model.poms.get(0);
		assertEquals("com.github.hilcode", pom.gav.groupArtifact.groupId);
		assertEquals("versionator-it", pom.gav.groupArtifact.artifactId);
		assertEquals("0.0.1-SNAPSHOT", pom.gav.version.toText());
	}

	@Test
	public void test0006()
	{
		final Model model = Model.BUILDER.build(this.pomFinder.findAllPoms(new File(this.baseDir, "test-0006/original")));
		model.apply(ImmutableList.of(Gav.BUILDER.build("GROUP:ARTIFACT:4.0")));
		final Pom grandParent = model.poms.get(1);
		assertEquals("com.github.hilcode", grandParent.gav.groupArtifact.groupId);
		assertEquals("versionator-it-grandparent", grandParent.gav.groupArtifact.artifactId);
		assertEquals("11-SNAPSHOT", grandParent.gav.version.toText());
		final Pom parent = model.poms.get(2);
		assertEquals(parent.parent.get(), grandParent);
		assertEquals("com.github.hilcode", parent.gav.groupArtifact.groupId);
		assertEquals("versionator-it-parent", parent.gav.groupArtifact.artifactId);
		assertEquals("101-SNAPSHOT", parent.gav.version.toText());
		final Pom pom = model.poms.get(0);
		assertEquals(pom.parent.get(), parent);
		assertEquals("com.github.hilcode", pom.gav.groupArtifact.groupId);
		assertEquals("versionator-it", pom.gav.groupArtifact.artifactId);
		assertEquals("1.2.4-SNAPSHOT", pom.gav.version.toText());
	}
	//	@Test
	//	public void test1001()
	//	{
	//		final Model model = new Model(this.pomFinder, new File(this.baseDir, "test-1001/original"));
	//		model.release();
	//		final Pom pom = model.poms.get(0);
	//		assertEquals("com.github.hilcode", pom.gav.groupArtifact.groupId);
	//		assertEquals("versionator-it", pom.gav.groupArtifact.artifactId);
	//		assertEquals("0.0.1", pom.gav.version.toText());
	//	}
	//
	//	@Test
	//	public void test1002()
	//	{
	//		final Model model = new Model(this.pomFinder, new File(this.baseDir, "test-1002/original"));
	//		model.release();
	//		final Pom parent = model.poms.get(1);
	//		assertEquals("com.github.hilcode", parent.gav.groupArtifact.groupId);
	//		assertEquals("versionator-it-parent", parent.gav.groupArtifact.artifactId);
	//		assertEquals("1", parent.gav.version.toText());
	//		final Pom pom = model.poms.get(0);
	//		assertEquals(parent, pom.parent.get());
	//		assertEquals("com.github.hilcode", pom.gav.groupArtifact.groupId);
	//		assertEquals("versionator-it", pom.gav.groupArtifact.artifactId);
	//		assertEquals("1.0", pom.gav.version.toText());
	//	}
	//
	//	@Test
	//	public void test1003()
	//	{
	//		final Model model = new Model(this.pomFinder, new File(this.baseDir, "test-1003/original"));
	//		model.release();
	//		final Pom parent = model.poms.get(1);
	//		assertEquals("com.github.hilcode", parent.gav.groupArtifact.groupId);
	//		assertEquals("versionator-it-parent", parent.gav.groupArtifact.artifactId);
	//		assertEquals("1", parent.gav.version.toText());
	//		final Pom pom = model.poms.get(0);
	//		assertEquals(parent, pom.parent.get());
	//		assertEquals("com.github.hilcode", pom.gav.groupArtifact.groupId);
	//		assertEquals("versionator-it", pom.gav.groupArtifact.artifactId);
	//		assertEquals("1.0", pom.gav.version.toText());
	//		final Dependency dependency = pom.dependencies.get(0);
	//		assertEquals("GROUP", dependency.gav.groupArtifact.groupId);
	//		assertEquals("ARTIFACT", dependency.gav.groupArtifact.artifactId);
	//		assertEquals("2.1", dependency.gav.version.toText());
	//	}
}
