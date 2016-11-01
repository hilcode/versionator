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

import com.google.common.base.Preconditions;

public final class Result<F, S>
{
	public static final <F, S> Result<F, S> success(final S value)
	{
		return new Result<>(null, value);
	}

	public static final <ANY, F, S> Result<F, S> asSuccess(final Result<ANY, S> result)
	{
		Preconditions.checkArgument(result.isSuccess(), "Invalid cast, this Result represents a Failure.");
		@SuppressWarnings("unchecked")
		final Result<F, S> result_ = (Result<F, S>) result;
		return result_;
	}

	public static final <F, S> Result<F, S> failure(final F value)
	{
		return new Result<>(value, null);
	}

	public static final <ANY, F, S> Result<F, S> asFailure(final Result<F, ANY> result)
	{
		Preconditions.checkArgument(result.isFailure(), "Invalid cast, this Result represents a Success.");
		@SuppressWarnings("unchecked")
		final Result<F, S> result_ = (Result<F, S>) result;
		return result_;
	}

	private final F failure;

	private final S success;

	private Result(final F failure, final S success)
	{
		Preconditions.checkArgument(
				failure != null && success == null || failure == null && success != null,
				"Invalid failure/success.");
		this.failure = failure;
		this.success = success;
	}

	public boolean isFailure()
	{
		return this.failure != null;
	}

	public boolean isSuccess()
	{
		return this.success != null;
	}

	public F failure()
	{
		Preconditions.checkNotNull(this.failure, "This Result represents a success.");
		return this.failure;
	}

	public S success()
	{
		Preconditions.checkNotNull(this.success, "This Result represents a failure.");
		return this.success;
	}
}
