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

public final class Tuple
{
	public static final class _2<A, B>
	{
		public final A _1;

		public final B _2;

		public _2(final A a, final B b)
		{
			this._1 = a;
			this._2 = b;
		}
	}

	public static final class _3<A, B, C>
	{
		public final A _1;

		public final B _2;

		public final C _3;

		public _3(final A a, final B b, final C c)
		{
			this._1 = a;
			this._2 = b;
			this._3 = c;
		}
	}

	public static final class _4<A, B, C, D>
	{
		public final A _1;

		public final B _2;

		public final C _3;

		public final D _4;

		public _4(final A a, final B b, final C c, final D d)
		{
			this._1 = a;
			this._2 = b;
			this._3 = c;
			this._4 = d;
		}
	}
}
