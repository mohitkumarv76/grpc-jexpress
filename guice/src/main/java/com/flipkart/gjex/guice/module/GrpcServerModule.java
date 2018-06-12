/*
 * Copyright (c) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.flipkart.gjex.guice.module;

import com.flipkart.gjex.grpc.service.GrpcServer;
import com.google.inject.AbstractModule;

/**
 * <code>GrpcServerModule</code> is a Guice {@link AbstractModule} implementation used for wiring Grpc Server and Services.
 * 
 * @author regunath.balasubramanian
 *
 */

public class GrpcServerModule extends AbstractModule {

	public GrpcServerModule() {}
	
	@Override
    protected void configure() {		
		bind(GrpcServer.class);
	}
}
