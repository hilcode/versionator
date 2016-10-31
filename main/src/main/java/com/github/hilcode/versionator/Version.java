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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.hilcode.versionator.maven.Type;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public interface Version
{
	String toText();

	boolean snapshot();

	Version toRelease();

	Version toSnapshot();

	Version next();

	public interface Number
		extends
			Version
	{
		// Empty.
	}

	public interface Common
		extends
			Version
	{
		// Empty.
	}

	public interface Unusual
		extends
			Version
	{
		// Empty.
	}

	public static final class Builder
	{
		private static final String SNAPSHOT_SUFFIX;

		private static final Pattern MAJOR;

		private static final Pattern MAJOR_MINOR;

		private static final Pattern MAJOR_MINOR_MICRO;
		static
		{
			SNAPSHOT_SUFFIX = "-SNAPSHOT";
			final String digits = "([0-9]+)";
			final String moreDigits = "\\." + digits;
			final String snapshot = "((?:" + SNAPSHOT_SUFFIX + ")?)";
			MAJOR = Pattern.compile(String.format("^%s%s$", digits, snapshot));
			MAJOR_MINOR = Pattern.compile(String.format("^%s%s%s$", digits, moreDigits, snapshot));
			MAJOR_MINOR_MICRO = Pattern.compile(String.format("^%s%s%s%s$", digits, moreDigits, moreDigits, snapshot));
		}

		public static final Version instance(final String versionAsText, final Type type)
		{
			final Optional<Version> maybeMajorVersion = matchMajor(versionAsText, type);
			if (maybeMajorVersion.isPresent())
			{
				return maybeMajorVersion.get();
			}
			final Optional<Version> maybeMajorMinorVersion = matchMajorMinor(versionAsText);
			if (maybeMajorMinorVersion.isPresent())
			{
				return maybeMajorMinorVersion.get();
			}
			final Optional<Version> maybeMajorMinorMicroVersion = matchMajorMinorMicro(versionAsText);
			if (maybeMajorMinorMicroVersion.isPresent())
			{
				return maybeMajorMinorMicroVersion.get();
			}
			return new DefaultUnusual(versionAsText);
		}

		public static final Optional<Version> matchMajor(final String versionAsText, final Type type)
		{
			final Matcher matcher = MAJOR.matcher(versionAsText);
			if (matcher.matches())
			{
				final int major = Integer.valueOf(matcher.group(1)).intValue();
				final boolean snapshot = !matcher.group(2).isEmpty();
				final Version version = type == Type.POM
						? new DefaultCommon(major, snapshot)
						: new DefaultNumber(major, snapshot);
				return Optional.of(version);
			}
			else
			{
				return Optional.absent();
			}
		}

		public static final Optional<Version> matchMajorMinor(final String versionAsText)
		{
			final Matcher matcher = MAJOR_MINOR.matcher(versionAsText);
			if (matcher.matches())
			{
				final int major = Integer.valueOf(matcher.group(1)).intValue();
				final int minor = Integer.valueOf(matcher.group(2)).intValue();
				final boolean snapshot = !matcher.group(3).isEmpty();
				final Version version = new DefaultCommon(major, minor, snapshot);
				return Optional.of(version);
			}
			else
			{
				return Optional.absent();
			}
		}

		public static final Optional<Version> matchMajorMinorMicro(final String versionAsText)
		{
			final Matcher matcher = MAJOR_MINOR_MICRO.matcher(versionAsText);
			if (matcher.matches())
			{
				final int major = Integer.valueOf(matcher.group(1)).intValue();
				final int minor = Integer.valueOf(matcher.group(2)).intValue();
				final int micro = Integer.valueOf(matcher.group(3)).intValue();
				final boolean snapshot = !matcher.group(4).isEmpty();
				final Version version = new DefaultCommon(major, minor, micro, snapshot);
				return Optional.of(version);
			}
			else
			{
				return Optional.absent();
			}
		}

		static final class DefaultNumber
			implements
				Version.Number
		{
			private final int major;

			private final boolean snapshot;

			public DefaultNumber(final int major, final boolean snapshot)
			{
				Preconditions.checkArgument(
						major >= 0,
						"Invalid 'major' version number: " + major + "; must be nonnegative.");
				this.major = major;
				this.snapshot = snapshot;
			}

			@Override
			public String toText()
			{
				return major + (snapshot ? SNAPSHOT_SUFFIX : "");
			}

			@Override
			public Version toRelease()
			{
				return snapshot ? new DefaultNumber(major, false) : this;
			}

			@Override
			public Version toSnapshot()
			{
				return snapshot ? this : new DefaultNumber(major, true);
			}

			@Override
			public Version next()
			{
				return new DefaultNumber(major + 1, snapshot);
			}

			@Override
			public boolean snapshot()
			{
				return snapshot;
			}

			@Override
			public int hashCode()
			{
				final int prime = 31;
				int result = 1;
				result = prime * result + major;
				result = prime * result + (snapshot ? 1231 : 1237);
				return result;
			}

			@Override
			public boolean equals(final Object object)
			{
				if (this == object)
				{
					return true;
				}
				if (object == null || getClass() != object.getClass())
				{
					return false;
				}
				final DefaultNumber other = (DefaultNumber) object;
				return major == other.major &&
						snapshot == other.snapshot;
			}

			@Override
			public String toString()
			{
				return "(DefaultNumber major='" + major + "' snapshot=" + snapshot + ")";
			}
		}

		static final class DefaultCommon
			implements
				Version.Common
		{
			private final int major;

			private final int minor;

			private final int micro;

			private final boolean snapshot;

			public DefaultCommon(final int major, final boolean snapshot)
			{
				this(major, 0, 0, snapshot);
			}

			public DefaultCommon(final int major, final int minor, final boolean snapshot)
			{
				this(major, minor, 0, snapshot);
			}

			public DefaultCommon(final int major, final int minor, final int micro)
			{
				this(major, minor, micro, false);
			}

			public DefaultCommon(final int major, final int minor, final int micro, final boolean snapshot)
			{
				Preconditions.checkArgument(
						major >= 0,
						"Invalid 'major' version number: " + major + "; must be nonnegative.");
				Preconditions.checkArgument(
						minor >= 0,
						"Invalid 'minor' version number: " + minor + "; must be nonnegative.");
				Preconditions.checkArgument(
						micro >= 0,
						"Invalid 'micro' version number: " + micro + "; must be nonnegative.");
				this.major = major;
				this.minor = minor;
				this.micro = micro;
				this.snapshot = snapshot;
			}

			@Override
			public String toText()
			{
				return micro == 0
						? major + "." + minor + (snapshot ? SNAPSHOT_SUFFIX : "")
						: major + "." + minor + "." + micro + (snapshot ? SNAPSHOT_SUFFIX : "");
			}

			@Override
			public Version toRelease()
			{
				return snapshot ? new DefaultCommon(major, minor, micro) : this;
			}

			@Override
			public Version toSnapshot()
			{
				return snapshot ? this : new DefaultCommon(major, minor, micro, true);
			}

			@Override
			public Version next()
			{
				return new DefaultCommon(major, minor, micro + 1, snapshot);
			}

			@Override
			public boolean snapshot()
			{
				return snapshot;
			}

			@Override
			public int hashCode()
			{
				final int prime = 31;
				int result = 1;
				result = prime * result + major;
				result = prime * result + minor;
				result = prime * result + micro;
				result = prime * result + (snapshot ? 1231 : 1237);
				return result;
			}

			@Override
			public boolean equals(final Object object)
			{
				if (this == object)
				{
					return true;
				}
				if (object == null || getClass() != object.getClass())
				{
					return false;
				}
				final DefaultCommon other = (DefaultCommon) object;
				return major == other.major &&
						minor == other.minor &&
						micro == other.micro &&
						snapshot == other.snapshot;
			}

			@Override
			public String toString()
			{
				return "(DefaultCommon major='" + major + "' minor='" + minor + "' micro='" + micro + "' snapshot=" + snapshot + ")";
			}
		}

		static final class DefaultUnusual
			implements
				Version.Unusual
		{
			private final String versionAsText;

			private final boolean snapshot;

			public DefaultUnusual(final String versionAsText)
			{
				this(versionAsText, versionAsText.endsWith(SNAPSHOT_SUFFIX));
			}

			public DefaultUnusual(final String versionAsText, final boolean snapshot)
			{
				Preconditions.checkNotNull(versionAsText, "Missing 'versionAsText'.");
				Preconditions.checkArgument(versionAsText.length() > 0, "Empty 'versionAsText'.");
				final boolean snapshot_ = versionAsText.endsWith(SNAPSHOT_SUFFIX);
				this.versionAsText = snapshot_
						? versionAsText.substring(0, versionAsText.length() - SNAPSHOT_SUFFIX.length())
						: versionAsText;
				this.snapshot = snapshot;
			}

			@Override
			public String toText()
			{
				return versionAsText + (snapshot ? SNAPSHOT_SUFFIX : "");
			}

			@Override
			public Version toRelease()
			{
				return snapshot ? new DefaultUnusual(versionAsText, false) : this;
			}

			@Override
			public Version toSnapshot()
			{
				return snapshot ? this : new DefaultUnusual(versionAsText, true);
			}

			@Override
			public Version next()
			{
				int firstLastDigitOffset = versionAsText.length();
				while (true)
				{
					final char ch = versionAsText.charAt(firstLastDigitOffset - 1);
					if (ch >= '0' && ch <= '9' && firstLastDigitOffset > 0)
					{
						firstLastDigitOffset--;
					}
					else
					{
						break;
					}
				}
				if (firstLastDigitOffset == versionAsText.length())
				{
					return new DefaultUnusual(versionAsText + "-1", snapshot);
				}
				else
				{
					final int number = Integer.valueOf(versionAsText.substring(firstLastDigitOffset)).intValue() + 1;
					return new DefaultUnusual(versionAsText.substring(0, firstLastDigitOffset) + number, snapshot);
				}
			}

			@Override
			public boolean snapshot()
			{
				return snapshot;
			}

			@Override
			public int hashCode()
			{
				final int prime = 31;
				int result = 1;
				result = prime * result + versionAsText.hashCode();
				result = prime * result + (snapshot ? 1231 : 1237);
				return result;
			}

			@Override
			public boolean equals(final Object object)
			{
				if (this == object)
				{
					return true;
				}
				if (object == null || getClass() != object.getClass())
				{
					return false;
				}
				final DefaultUnusual other = (DefaultUnusual) object;
				return versionAsText.equals(other.versionAsText) &&
						snapshot == other.snapshot;
			}

			@Override
			public String toString()
			{
				return "(DefaultUnusual versionAsText='" + versionAsText + "' snapshot=" + snapshot + ")";
			}
		}
	}
}
