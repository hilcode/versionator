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

import com.google.common.base.Preconditions;

public final class Either<L, R>
{
	public static final <L, R> Either<L, R> left(final L value)
	{
		return new Either<>(value, null);
	}

	public static final <T, L, R> Either<L, R> asLeft(final Either<L, T> either)
	{
		Preconditions.checkArgument(either.isLeft(), "Invalid cast, this Either represents a Right.");
		@SuppressWarnings("unchecked")
		final Either<L, R> either_ = (Either<L, R>) either;
		return either_;
	}

	public static final <L, R> Either<L, R> right(final R value)
	{
		return new Either<>(null, value);
	}

	public static final <T, L, R> Either<L, R> asRight(final Either<T, R> either)
	{
		Preconditions.checkArgument(either.isRight(), "Invalid cast, this Either represents a Left.");
		@SuppressWarnings("unchecked")
		final Either<L, R> either_ = (Either<L, R>) either;
		return either_;
	}

	private final L left;

	private final R right;

	private Either(final L left, final R right)
	{
		Preconditions.checkArgument(
				left != null && right == null || left == null && right != null,
				"Invalid left/right.");
		this.left = left;
		this.right = right;
	}

	public final boolean isLeft()
	{
		return this.left != null;
	}

	public final boolean isRight()
	{
		return this.right != null;
	}

	public final L left()
	{
		Preconditions.checkNotNull(this.left, "This Either represents a Right.");
		return this.left;
	}

	public final R right()
	{
		Preconditions.checkNotNull(this.right, "This Result represents a Left.");
		return this.right;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("(Either");
		if (isLeft())
		{
			builder.append(" left=<").append(this.left).append('>');
		}
		else
		{
			builder.append(" right=<").append(this.right).append('>');
		}
		builder.append(")");
		return builder.toString();
	}
}
