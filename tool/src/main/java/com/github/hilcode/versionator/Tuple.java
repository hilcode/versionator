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

public abstract class Tuple<A>
{
	public final A _1;

	public Tuple(final A a)
	{
		this._1 = a;
	}

	public static class Duo<A, B>
		extends
			Tuple<A>
	{
		public final B _2;

		public Duo(final A a, final B b)
		{
			super(a);
			this._2 = b;
		}
	}

	public static class Triple<A, B, C>
		extends
			Duo<A, B>
	{
		public final C _3;

		public Triple(final A a, final B b, final C c)
		{
			super(a, b);
			this._3 = c;
		}
	}

	public static class Quartet<A, B, C, D>
		extends
			Triple<A, B, C>
	{
		public final D _4;

		public Quartet(final A a, final B b, final C c, final D d)
		{
			super(a, b, c);
			this._4 = d;
		}
	}
}
