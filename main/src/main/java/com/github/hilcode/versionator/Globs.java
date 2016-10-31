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

	static final class Glob
	{
		public final String pattern;

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
				final List<Integer> globOffsetsNext = Lists.newArrayList();
				final char nextCharInText = text.charAt(i);
				for (final Integer globOffset : globOffsets)
				{
					final int globIndex = globOffset.intValue();
					if (globIndex >= this.pattern.length())
					{
						continue;
					}
					final char globChar = this.pattern.charAt(globIndex);
					switch (globChar)
					{
						case '?':
							handleSingleCharacterGlob(bits, globOffsetsNext, globIndex);
							break;
						case '*':
							handleMultiCharacterGlob(bits, globOffsetsNext, nextCharInText, globOffset, globIndex);
							break;
						default:
							if (globChar == nextCharInText)
							{
								handleSingleCharacterGlob(bits, globOffsetsNext, globIndex);
							}
							break;
					}
				}
				globOffsets = globOffsetsNext;
			}
			return determineResult(globOffsets);
		}

		public void handleSingleCharacterGlob(
				final BitSet bits,
				final List<Integer> globOffsets,
				final int globIndex)
		{
			if (!bits.get(globIndex + 1))
			{
				bits.set(globIndex + 1);
				globOffsets.add(Integer.valueOf(globIndex + 1));
			}
		}

		public void handleMultiCharacterGlob(
				final BitSet bits,
				final List<Integer> globOffsets,
				final char nextCharInText,
				final Integer globOffset,
				final int globIndex)
		{
			if (!bits.get(globIndex))
			{
				bits.set(globIndex);
				globOffsets.add(globOffset);
			}
			bits.set(globIndex + 1);
			globOffsets.add(Integer.valueOf(globIndex + 1));
			if (globIndex + 1 < this.pattern.length())
			{
				if (this.pattern.charAt(globIndex + 1) == '?' || this.pattern.charAt(globIndex + 1) == nextCharInText)
				{
					bits.set(globIndex + 2);
					globOffsets.add(Integer.valueOf(globIndex + 2));
				}
			}
		}

		public boolean determineResult(final List<Integer> globOffsets)
		{
			for (final Integer globOffset : globOffsets)
			{
				if (globOffset.intValue() == this.pattern.length())
				{
					return true;
				}
				if (globOffset.intValue() + 1 == this.pattern.length() && this.pattern.endsWith("*"))
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
}
