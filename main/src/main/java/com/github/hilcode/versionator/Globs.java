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

import java.util.BitSet;
import java.util.List;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public final class Globs
{
	public static final Glob create(final String pattern)
	{
		return new Glob(pattern);
	}

	public static final class Glob
	{
		private final String pattern;

		public Glob(final String pattern)
		{
			Preconditions.checkNotNull(pattern, "Missing 'pattern'.");
			final String pattern_ = pattern.replaceAll("\\*\\*+", "*");
			this.pattern = pattern_;
		}

		public boolean match(final String text)
		{
			if (text == null)
			{
				return false;
			}
			List<Integer> globOffsets = Lists.newArrayList();
			globOffsets.add(Integer.valueOf(0));
			for (int i = 0; i < text.length(); i++)
			{
				if (globOffsets.isEmpty())
				{
					return false;
				}
				final BitSet bits = new BitSet(this.pattern.length() + 1);
				final List<Integer> globOffsets_ = Lists.newArrayList();
				final char ch = text.charAt(i);
				for (final Integer globOffset_ : globOffsets)
				{
					final int globOffset = globOffset_.intValue();
					if (globOffset >= this.pattern.length())
					{
						continue;
					}
					final char globChar = this.pattern.charAt(globOffset);
					switch (globChar)
					{
						case '?':
							if (!bits.get(globOffset + 1))
							{
								bits.set(globOffset + 1);
								globOffsets_.add(Integer.valueOf(globOffset + 1));
							}
							break;
						case '*':
							if (!bits.get(globOffset))
							{
								bits.set(globOffset);
								globOffsets_.add(globOffset_);
							}
							if (!bits.get(globOffset + 1))
							{
								bits.set(globOffset + 1);
								globOffsets_.add(Integer.valueOf(globOffset + 1));
							}
							if (!bits.get(globOffset + 2) && globOffset + 1 < this.pattern.length())
							{
								if (this.pattern.charAt(globOffset + 1) == '?' || this.pattern.charAt(globOffset + 1) == ch)
								{
									bits.set(globOffset + 2);
									globOffsets_.add(Integer.valueOf(globOffset + 2));
								}
							}
							break;
						default:
							if (globChar == ch)
							{
								if (!bits.get(globOffset + 1))
								{
									bits.set(globOffset + 1);
									globOffsets_.add(Integer.valueOf(globOffset + 1));
								}
							}
							break;
					}
				}
				globOffsets = globOffsets_;
			}
			for (final Integer globOffset : globOffsets)
			{
				if (globOffset.intValue() == this.pattern.length() || this.pattern.endsWith("*") && globOffset.intValue() + 1 == this.pattern.length())
				{
					return true;
				}
			}
			return false;
		}

		@Override
		public String toString()
		{
			return String.format("(Glob pattern='%s')", this.pattern);
		}
	}

	public static void main(final String[] args)
	{
		Preconditions.checkState(Globs.create("").match(""));
		Preconditions.checkState(!Globs.create("a").match(""));
		Preconditions.checkState(!Globs.create("").match("a"));
		Preconditions.checkState(Globs.create("Hello world!").match("Hello world!"));
		Preconditions.checkState(Globs.create("?").match("1"));
		Preconditions.checkState(!Globs.create("?").match(""));
		Preconditions.checkState(!Globs.create("?").match("12"));
		Preconditions.checkState(!Globs.create("??").match("1"));
		Preconditions.checkState(Globs.create("??").match("12"));
		Preconditions.checkState(!Globs.create("??").match("123"));
		Preconditions.checkState(Globs.create("1?3").match("123"));
		Preconditions.checkState(Globs.create("1?").match("12"));
		Preconditions.checkState(!Globs.create("1?").match("22"));
		Preconditions.checkState(Globs.create("?2").match("12"));
		Preconditions.checkState(!Globs.create("?2").match("23"));
		Preconditions.checkState(Globs.create("*").match(""));
		Preconditions.checkState(Globs.create("*").match("1"));
		Preconditions.checkState(Globs.create("*").match("12"));
		Preconditions.checkState(Globs.create("*").match("123"));
		Preconditions.checkState(!Globs.create("1*").match(""));
		Preconditions.checkState(Globs.create("1*").match("1"));
		Preconditions.checkState(Globs.create("1*").match("12"));
		Preconditions.checkState(Globs.create("1*").match("123"));
		Preconditions.checkState(!Globs.create("*1").match(""));
		Preconditions.checkState(Globs.create("*1").match("1"));
		Preconditions.checkState(Globs.create("*12").match("12"));
		Preconditions.checkState(Globs.create("*123").match("123"));
		Preconditions.checkState(Globs.create("1*2").match("12"));
		Preconditions.checkState(Globs.create("1*3").match("123"));
		Preconditions.checkState(Globs.create("1*?").match("12"));
		Preconditions.checkState(Globs.create("1*?").match("123"));
		Preconditions.checkState(Globs.create("1*?3").match("123"));
		System.out.println("Ready.");
	}
}
