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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.hilcode.versionator.api.Injector;
import com.github.hilcode.versionator.api.ReleaseRequest;
import com.github.hilcode.versionator.api.RequestExtractor;
import com.github.hilcode.versionator.api.Tool;
import com.github.hilcode.versionator.api.VersionChangeRequest;
import com.github.hilcode.versionator.misc.Either;
import com.github.hilcode.versionator.misc.Result;
import com.google.common.collect.ImmutableList;

public final class DefaultTool
	implements
		Tool
{
	public static final void main(final String[] args)
	{
		new DefaultTool().run(ImmutableList.copyOf(args));
	}

	private final Logger logger;

	private final RequestExtractor requestExtractor;

	public DefaultTool()
	{
		this(LoggerFactory.getLogger(DefaultTool.class), com.github.hilcode.versionator.impl.DefaultInjector.INSTANCE);
	}

	DefaultTool(final Logger logger, final Injector injector)
	{
		this.logger = logger;
		this.requestExtractor = injector.requestExtractor();
	}

	@Override
	public Result<String, Either<VersionChangeRequest, ReleaseRequest>> run(final ImmutableList<String> arguments)
	{
		final Result<String, Either<VersionChangeRequest, ReleaseRequest>> result =
				this.requestExtractor.extract(arguments);
		if (result.isFailure())
		{
			this.logger.error(result.failure());
		}
		else
		{
			final Either<VersionChangeRequest, ReleaseRequest> request = result.success();
			if (request.isLeft())
			{
				execute(request.left());
			}
			else
			{
				execute(request.right());
			}
		}
		return result;
	}

	void execute(final VersionChangeRequest request)
	{
		this.logger.info("SET VERSION");
		this.logger.info("Run Type      : " + request.runType);
		this.logger.info("Mode          : " + request.mode);
		this.logger.info("Version Change: " + request.versionChange);
	}

	void execute(final ReleaseRequest request)
	{
		this.logger.info("RELEASE");
		this.logger.info("Run Type      : " + request.runType);
		this.logger.info("Mode          : " + request.mode);
		this.logger.info("Exclusions    : " + request.exclusions);
	}
}
