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
package com.github.hilcode.versionator.misc;

import java.util.Random;
import java.util.UUID;
import com.github.hilcode.versionator.maven.Gav;
import com.github.hilcode.versionator.maven.GroupArtifact;

public final class Generators
{
	public static final UUID uuid(final Random rnd)
	{
		final byte[] bytes = new byte[16];
		rnd.nextBytes(bytes);
		bytes[6] &= 0x0f; /* clear version        */
		bytes[6] |= 0x40; /* set to version 4     */
		bytes[8] &= 0x3f; /* clear variant        */
		bytes[8] |= 0x80; /* set to IETF variant  */
		long mostSignificantBits = 0L;
		long leastSignificantBits = 0L;
		for (int i = 0; i < 8; i++)
		{
			mostSignificantBits |= Byte.toUnsignedLong(bytes[i]) << 56 - 8 * i;
			leastSignificantBits |= Byte.toUnsignedLong(bytes[8 + i]) << 56 - 8 * i;
		}
		return new UUID(mostSignificantBits, leastSignificantBits);
	}

	public static final String groupId(final Random rnd)
	{
		return "group-" + uuid(rnd);
	}

	public static final String artifactId(final Random rnd)
	{
		return "artifact-" + uuid(rnd);
	}

	public static final String version(final Random rnd)
	{
		final String version;
		if (rnd.nextInt(4) == 0)
		{
			version = String.valueOf(rnd.nextInt(100) + 1);
		}
		else
		{
			if (rnd.nextInt(2) == 0)
			{
				version = String.format(
						"%s.%s",
						String.valueOf(rnd.nextInt(100)),
						String.valueOf(rnd.nextInt(100) + 1));
			}
			else
			{
				version = String.format(
						"%s.%s.%s",
						String.valueOf(rnd.nextInt(100)),
						String.valueOf(rnd.nextInt(100)),
						String.valueOf(rnd.nextInt(100) + 1));
			}
		}
		return rnd.nextBoolean() ? version : version + "-SNAPSHOT";
	}

	public static final GroupArtifact groupArtifact(final Random rnd)
	{
		return new GroupArtifact(groupId(rnd), artifactId(rnd));
	}

	public static final Gav gav(final Random rnd)
	{
		return new Gav(groupArtifact(rnd), version(rnd));
	}
}
