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
package com.github.hilcode.versionator.impl;

import com.github.hilcode.versionator.api.FlagExtractor;
import com.github.hilcode.versionator.api.Flags;
import com.github.hilcode.versionator.api.GavExtractor;
import com.github.hilcode.versionator.api.Injector;
import com.github.hilcode.versionator.api.Matchers;
import com.github.hilcode.versionator.api.ReleaseExtractor;
import com.github.hilcode.versionator.api.ReleaseRequest;
import com.github.hilcode.versionator.api.ReleaseRequestExtractor;
import com.github.hilcode.versionator.api.RequestExtractor;
import com.github.hilcode.versionator.api.VersionChangeExtractor;
import com.github.hilcode.versionator.api.VersionChangeRequest;
import com.github.hilcode.versionator.api.VersionChangeRequestExtractor;
import com.github.hilcode.versionator.maven.Gav;
import com.github.hilcode.versionator.maven.GroupArtifact;

public final class DefaultInjector
	implements
		Injector
{
	private static final class Default
	{
		public final Gav.Builder gavBuilder;

		public final GroupArtifact.Builder groupArtifactBuilder;

		public final Flags.Builder flagsBuilder;

		public final VersionChangeRequest.Builder versionChangeRequestBuilder;

		public final ReleaseRequest.Builder releaseRequestBuilder;

		public final FlagExtractor flagExtractor;

		public final GavExtractor gavExtractor;

		public final Matchers matchers;

		public final VersionChangeExtractor versionChangeExtractor;

		public final VersionChangeRequestExtractor versionChangeRequestExtractor;

		public final ReleaseExtractor releaseExtractor;

		public final ReleaseRequestExtractor releaseRequestExtractor;

		public final RequestExtractor requestExtractor;

		Default()
		{
			this.gavBuilder = Gav.BUILDER;
			this.groupArtifactBuilder = GroupArtifact.BUILDER;
			this.flagsBuilder = Flags.BUILDER;
			this.versionChangeRequestBuilder = VersionChangeRequest.BUILDER;
			this.releaseRequestBuilder = ReleaseRequest.BUILDER;
			this.flagExtractor = new DefaultFlagExtractor(this.flagsBuilder);
			this.gavExtractor = new DefaultGavExtractor();
			this.matchers = new DefaultMatchers(this.gavBuilder, this.groupArtifactBuilder);
			this.versionChangeExtractor = new DefaultVersionChangeExtractor(this.gavExtractor, this.matchers);
			this.versionChangeRequestExtractor = new DefaultVersionChangeRequestExtractor(this.versionChangeExtractor, this.versionChangeRequestBuilder);
			this.releaseExtractor = new DefaultReleaseExtractor(this.matchers);
			this.releaseRequestExtractor = new DefaultReleaseRequestExtractor(this.releaseExtractor, this.releaseRequestBuilder);
			this.requestExtractor = new DefaultRequestExtractor(this.flagExtractor, this.versionChangeRequestExtractor, this.releaseRequestExtractor);
		}
	}

	private static final Default DEFAULTS = new Default();

	public static final DefaultInjector INSTANCE = new DefaultInjector();

	private final Gav.Builder gavBuilder;

	private final GroupArtifact.Builder groupArtifactBuilder;

	private final Flags.Builder flagsBuilder;

	private final VersionChangeRequest.Builder versionChangeRequestBuilder;

	private final ReleaseRequest.Builder releaseRequestBuilder;

	private final Matchers matchers;

	private final FlagExtractor flagExtractor;

	private final GavExtractor gavExtractor;

	private final VersionChangeExtractor versionChangeExtractor;

	private final VersionChangeRequestExtractor versionChangeRequestExtractor;

	private final ReleaseExtractor releaseExtractor;

	private final ReleaseRequestExtractor releaseRequestExtractor;

	private final RequestExtractor requestExtractor;

	private DefaultInjector()
	{
		this(
				DEFAULTS.gavBuilder,
				DEFAULTS.groupArtifactBuilder,
				DEFAULTS.flagsBuilder,
				DEFAULTS.versionChangeRequestBuilder,
				DEFAULTS.releaseRequestBuilder,
				DEFAULTS.flagExtractor,
				DEFAULTS.gavExtractor,
				DEFAULTS.matchers,
				DEFAULTS.versionChangeExtractor,
				DEFAULTS.versionChangeRequestExtractor,
				DEFAULTS.releaseExtractor,
				DEFAULTS.releaseRequestExtractor,
				DEFAULTS.requestExtractor);
	}

	DefaultInjector(
			final Gav.Builder gavBuilder,
			final GroupArtifact.Builder groupArtifactBuilder,
			final Flags.Builder flagsBuilder,
			final VersionChangeRequest.Builder versionChangeRequestBuilder,
			final ReleaseRequest.Builder releaseRequestBuilder,
			final FlagExtractor flagExtractor,
			final GavExtractor gavExtractor,
			final Matchers matchers,
			final VersionChangeExtractor versionChangeExtractor,
			final VersionChangeRequestExtractor versionChangeRequestExtractor,
			final ReleaseExtractor releaseExtractor,
			final ReleaseRequestExtractor releaseRequestExtractor,
			final RequestExtractor requestExtractor)
	{
		this.gavBuilder = gavBuilder;
		this.groupArtifactBuilder = groupArtifactBuilder;
		this.flagsBuilder = flagsBuilder;
		this.versionChangeRequestBuilder = versionChangeRequestBuilder;
		this.releaseRequestBuilder = releaseRequestBuilder;
		this.flagExtractor = flagExtractor;
		this.gavExtractor = gavExtractor;
		this.matchers = matchers;
		this.versionChangeExtractor = versionChangeExtractor;
		this.versionChangeRequestExtractor = versionChangeRequestExtractor;
		this.releaseExtractor = releaseExtractor;
		this.releaseRequestExtractor = releaseRequestExtractor;
		this.requestExtractor = requestExtractor;
	}

	@Override
	public final Gav.Builder gavBuilder()
	{
		return this.gavBuilder;
	}

	@Override
	public final GroupArtifact.Builder groupArtifactBuilder()
	{
		return this.groupArtifactBuilder;
	}

	@Override
	public final Flags.Builder flagsBuilder()
	{
		return this.flagsBuilder;
	}

	@Override
	public final VersionChangeRequest.Builder versionChangeRequest()
	{
		return this.versionChangeRequestBuilder;
	}

	@Override
	public final ReleaseRequest.Builder releaseRequest()
	{
		return this.releaseRequestBuilder;
	}

	@Override
	public final Matchers matchers()
	{
		return this.matchers;
	}

	@Override
	public final FlagExtractor flagExtractor()
	{
		return this.flagExtractor;
	}

	@Override
	public final GavExtractor gavExtractor()
	{
		return this.gavExtractor;
	}

	@Override
	public final VersionChangeExtractor versionChangeExtractor()
	{
		return this.versionChangeExtractor;
	}

	@Override
	public VersionChangeRequestExtractor versionChangeRequestExtractor()
	{
		return this.versionChangeRequestExtractor;
	}

	@Override
	public final ReleaseExtractor releaseExtractor()
	{
		return this.releaseExtractor;
	}

	@Override
	public ReleaseRequestExtractor releaseRequestExtractor()
	{
		return this.releaseRequestExtractor;
	}

	@Override
	public RequestExtractor requestExtractor()
	{
		return this.requestExtractor;
	}
}
