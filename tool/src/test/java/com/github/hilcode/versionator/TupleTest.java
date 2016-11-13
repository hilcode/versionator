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

import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;

public class TupleTest
{
	private Integer a;

	private String b;

	private Float c;

	private Character d;

	@Before
	public final void setUp()
	{
		this.a = Integer.valueOf(100);
		this.b = "Hello";
		this.c = Float.valueOf(3.14f);
		this.d = Character.valueOf('c');
	}

	@Test
	public final void test1()
	{
		final Tuple.Duo<Integer, String> tuple = new Tuple.Duo<>(this.a, this.b);
		assertSame(this.a, tuple._1);
		assertSame(this.b, tuple._2);
	}

	@Test
	public final void test2()
	{
		final Tuple.Triple<Integer, String, Float> tuple = new Tuple.Triple<>(this.a, this.b, this.c);
		assertSame(this.a, tuple._1);
		assertSame(this.b, tuple._2);
		assertSame(this.c, tuple._3);
	}

	@Test
	public final void test3()
	{
		final Tuple.Quartet<Integer, String, Float, Character> tuple =
				new Tuple.Quartet<>(this.a, this.b, this.c, this.d);
		assertSame(this.a, tuple._1);
		assertSame(this.b, tuple._2);
		assertSame(this.c, tuple._3);
		assertSame(this.d, tuple._4);
	}
}
