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
import java.util.List;
import java.util.Random;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public final class Generator
{
	public static final Key randomKey(final Random rnd)
	{
		return Key.BUILDER.build("key-" + rnd.nextInt(10000));
	}

	@SafeVarargs
	public static final Property randomProperty(final Random rnd, final Predicate<Property>... exclusions)
	{
		return randomAny(rnd, Generator::randomProperty, exclusions);
	}

	private static final Property randomProperty(final Random rnd)
	{
		return Property.BUILDER.build(randomKey(rnd), "Value " + rnd.nextInt(10000));
	}

	@SafeVarargs
	public static final ImmutableList<Property> randomProperties(final Random rnd, final Predicate<ImmutableList<Property>>... exclusions)
	{
		return randomListOfAny(rnd, Generator::randomProperty, exclusions);
	}

	@SafeVarargs
	public static final ArtifactId randomArtifactId(final Random rnd, final Predicate<ArtifactId>... exclusions)
	{
		return randomAny(rnd, Generator::randomArtifactId, exclusions);
	}

	private static final ArtifactId randomArtifactId(final Random rnd)
	{
		return ArtifactId.BUILDER.build("artifact-id-" + rnd.nextInt(10000));
	}

	@SafeVarargs
	public static final GroupId randomGroupId(final Random rnd, final Predicate<GroupId>... exclusions)
	{
		return randomAny(rnd, Generator::randomGroupId, exclusions);
	}

	private static final GroupId randomGroupId(final Random rnd)
	{
		return GroupId.BUILDER.build("group-id-" + rnd.nextInt(10000));
	}

	@SafeVarargs
	public static final GroupArtifact randomGroupArtifact(final Random rnd, final Predicate<GroupArtifact>... exclusions)
	{
		return randomAny(rnd, Generator::randomGroupArtifact, exclusions);
	}

	private static final GroupArtifact randomGroupArtifact(final Random rnd)
	{
		return GroupArtifact.BUILDER.build(randomGroupId(rnd), randomArtifactId(rnd));
	}

	@SafeVarargs
	public static final Version randomVersion(final Random rnd, final Predicate<Version>... exclusions)
	{
		return randomAny(rnd, Generator::randomVersion, exclusions);
	}

	private static final Version randomVersion(final Random rnd)
	{
		final String suffix = rnd.nextBoolean() ? "" : "-SNAPSHOT";
		final int p = rnd.nextInt(10);
		switch (p)
		{
			case 0:
			case 1:
				return Version.BUILDER.build(
						String.format(
								"%s%s",
								Integer.valueOf(1 + rnd.nextInt(100)),
								suffix));
			case 2:
			case 3:
			case 4:
				return Version.BUILDER.build(
						String.format(
								"%s.%s%s",
								Integer.valueOf(1 + rnd.nextInt(100)),
								Integer.valueOf(rnd.nextInt(100)),
								suffix));
			case 5:
			case 6:
			case 7:
				return Version.BUILDER.build(
						String.format(
								"%s.%s.%s%s",
								Integer.valueOf(1 + rnd.nextInt(100)),
								Integer.valueOf(rnd.nextInt(100)),
								Integer.valueOf(rnd.nextInt(100)),
								suffix));
			case 8:
				return Version.BUILDER.build(
						String.format(
								"alpha-%s%s",
								Integer.valueOf(1 + rnd.nextInt(100)),
								suffix));
			case 9:
			default:
				return Version.BUILDER.build(
						String.format(
								"beta-%s%s",
								Integer.valueOf(1 + rnd.nextInt(100)),
								suffix));
		}
	}

	@SafeVarargs
	public static final File randomFile(final Random rnd, final Predicate<File>... exclusions)
	{
		return randomAny(rnd, Generator::randomFile, exclusions);
	}

	private static final File randomFile(final Random rnd)
	{
		final File dir = new File("dir-" + rnd.nextInt(10000));
		return new File(dir, "pom.xml");
	}

	@SafeVarargs
	public static final Gav randomGav(final Random rnd, final Predicate<Gav>... exclusions)
	{
		return randomAny(rnd, Generator::randomGav, exclusions);
	}

	private static final Gav randomGav(final Random rnd)
	{
		return Gav.BUILDER.build(randomGroupArtifact(rnd), randomVersion(rnd));
	}

	@SafeVarargs
	public static final Dependency randomDependency(final Random rnd, final Predicate<Dependency>... exclusions)
	{
		return randomAny(rnd, Generator::randomDependency, exclusions);
	}

	private static final Dependency randomDependency(final Random rnd)
	{
		return Dependency.BUILDER.build(randomGav(rnd));
	}

	@SafeVarargs
	public static final ImmutableList<Dependency> randomDependencies(final Random rnd, final Predicate<ImmutableList<Dependency>>... exclusions)
	{
		return randomListOfAny(rnd, Generator::randomDependency, exclusions);
	}

	@SafeVarargs
	public static final GroupIdSource randomGroupIdSource(final Random rnd, final Predicate<GroupIdSource>... exclusions)
	{
		return randomAny(rnd, Generator::randomGroupIdSource, exclusions);
	}

	private static final GroupIdSource randomGroupIdSource(final Random rnd)
	{
		return GroupIdSource.values()[rnd.nextInt(GroupIdSource.values().length)];
	}

	@SafeVarargs
	public static final VersionSource randomVersionSource(final Random rnd, final Predicate<VersionSource>... exclusions)
	{
		return randomAny(rnd, Generator::randomVersionSource, exclusions);
	}

	private static final VersionSource randomVersionSource(final Random rnd)
	{
		return VersionSource.values()[rnd.nextInt(VersionSource.values().length)];
	}

	@SafeVarargs
	public static final Type randomType(final Random rnd, final Predicate<Type>... exclusions)
	{
		return randomAny(rnd, Generator::randomType, exclusions);
	}

	private static final Type randomType(final Random rnd)
	{
		return Type.values()[rnd.nextInt(Type.values().length)];
	}

	@SafeVarargs
	public static final String randomModule(final Random rnd, final Predicate<String>... exclusions)
	{
		return randomAny(rnd, Generator::randomModule, exclusions);
	}

	private static final String randomModule(final Random rnd)
	{
		return "module-" + rnd.nextInt(10000);
	}

	@SafeVarargs
	public static final ImmutableList<String> randomModules(final Random rnd, final Predicate<ImmutableList<String>>... exclusions)
	{
		return randomListOfAny(rnd, Generator::randomModule, exclusions);
	}

	@SafeVarargs
	public static final Pom randomPom(final Random rnd, final Predicate<Pom>... exclusions)
	{
		return randomAny(rnd, Generator::randomPom, exclusions);
	}

	private static final Pom randomPom(final Random rnd)
	{
		final Pom parent = rnd.nextInt(10) < 2
				? randomPom(rnd)
				: Pom.NONE;
		return Pom.BUILDER.build(
				randomGav(rnd, gav_ -> matchesParentGroupArtifact(parent, gav_)),
				parent == Pom.NONE ? GroupIdSource.POM : randomGroupIdSource(rnd),
				parent == Pom.NONE ? VersionSource.POM : randomVersionSource(rnd),
				randomFile(rnd),
				randomType(rnd),
				parent,
				randomModules(rnd),
				randomProperties(rnd),
				randomDependencies(rnd));
	}

	@SafeVarargs
	public static final PomAndGav randomPomAndGav(final Random rnd, final Predicate<PomAndGav>... exclusions)
	{
		return randomAny(rnd, Generator::randomPomAndGav, exclusions);
	}

	private static final PomAndGav randomPomAndGav(final Random rnd)
	{
		return PomAndGav.BUILDER.build(randomPom(rnd), randomGav(rnd));
	}

	@SafeVarargs
	public static final Model randomModel(final Random rnd, final Predicate<Model>... exclusions)
	{
		return randomAny(rnd, Generator::randomModel, exclusions);
	}

	private static final Model randomModel(final Random rnd)
	{
		final int pomCount = rnd.nextInt(10);
		final List<Pom> poms = Lists.newArrayList();
		final List<GroupArtifact> groupArtifacts = Lists.newArrayList();
		for (int i = 0; i < pomCount; i++)
		{
			final Pom pom = randomPom(rnd, pom_ -> groupArtifacts.contains(pom_.gav.groupArtifact));
			poms.add(pom);
			groupArtifacts.add(pom.gav.groupArtifact);
		}
		return Model.BUILDER.build(ImmutableList.copyOf(poms));
	}

	public static final boolean matchesParentGroupArtifact(final Pom parent, final Gav gav)
	{
		Pom parent_ = parent;
		while (true)
		{
			if (parent_ == Pom.NONE)
			{
				return false;
			}
			if (gav.groupArtifact == parent_.gav.groupArtifact)
			{
				return true;
			}
			parent_ = parent_.parent;
		}
	}

	@SafeVarargs
	private static final <T> T randomAny(final Random rnd, final Function<Random, T> generator, final Predicate<T>... exclusions)
	{
		while (true)
		{
			final T value = generator.apply(rnd);
			if (!isValueExcluded(value, exclusions))
			{
				return value;
			}
		}
	}

	@SafeVarargs
	private static final <T> ImmutableList<T> randomListOfAny(
			final Random rnd,
			final Function<Random, T> generator,
			final Predicate<ImmutableList<T>>... exclusions)
	{
		while (true)
		{
			final ImmutableList<T> value = randomListOfAny(rnd, generator);
			if (!isValueExcluded(value, exclusions))
			{
				return value;
			}
		}
	}

	private static final <T> ImmutableList<T> randomListOfAny(final Random rnd, final Function<Random, T> generator)
	{
		final int count = rnd.nextInt(5);
		final List<T> things = Lists.newArrayList();
		for (int i = 0; i < count; i++)
		{
			things.add(generator.apply(rnd));
		}
		return ImmutableList.copyOf(things);
	}

	@SafeVarargs
	private static final <T> boolean isValueExcluded(final T value, final Predicate<T>... exclusions)
	{
		for (final Predicate<T> exclusion : exclusions)
		{
			if (exclusion.apply(value))
			{
				return true;
			}
		}
		return false;
	}
}
