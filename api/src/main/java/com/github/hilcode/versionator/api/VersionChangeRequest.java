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
package com.github.hilcode.versionator.api;

public final class VersionChangeRequest
{
	public interface Builder
	{
		VersionChangeRequest build(RunType runType, Mode mode, RequestedVersionChange versionChange);
	}

	public static final Builder BUILDER = new Builder()
	{
		@Override
		public VersionChangeRequest build(final RunType runType, final Mode mode, final RequestedVersionChange versionChange)
		{
			return new VersionChangeRequest(runType, mode, versionChange);
		}
	};

	public final RunType runType;

	public final Mode mode;

	public final RequestedVersionChange versionChange;

	public VersionChangeRequest(final RunType runType, final Mode mode, final RequestedVersionChange versionChange)
	{
		this.runType = runType;
		this.mode = mode;
		this.versionChange = versionChange;
	}
}
