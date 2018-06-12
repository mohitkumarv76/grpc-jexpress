/*
 * Copyright 2012-2016, the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flipkart.gjex.guice.module;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import com.flipkart.gjex.Constants;
import com.flipkart.gjex.core.config.FileLocator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * <code>ContainerModule</code> is a Guice {@link AbstractModule} implementation used for wiring GJEX container components.
 * 
 * @author regunath.balasubramanian
 */
public class ContainerModule extends AbstractModule {

	/**
	 * Creates a Jetty {@link WebAppContext} for the GJEX dashboard
	 * @return Jetty WebAppContext
	 */
	@Named("DashboardContext")
	@Provides
	@Singleton
	WebAppContext getDashboardWebAppContext() {
		String path = null;
        File[] files = FileLocator.findDirectories("packaged/webapps/dashboard/WEB-INF", null);
        for (File file : files) {
			// we need only WEB-INF from runtime project 
			String fileToString = file.toString();
			if (fileToString.contains(".jar!") && fileToString.startsWith("file:/")) {
				fileToString = fileToString.replace("file:/","jar:file:/");
				if (fileToString.contains("runtime-")) {
					path = fileToString;
					break;
				}
			} else {
				if (fileToString.contains(Constants.DASHBOARD)) {
					path = fileToString;
					break;
				}
			}
		}
		// trim off the "WEB-INF" part as the WebAppContext path should refer to the parent directory
		if (path.endsWith("WEB-INF")) {
			path = path.replace("WEB-INF", "");
		}
		WebAppContext webAppContext = new WebAppContext(path, Constants.DASHBOARD_CONTEXT_PATH);
		return webAppContext;
	}
	
	/**
	 * Creates the Jetty server instance for the admin Dashboard and configures it with the @Named("DashboardContext").
	 * @param port where the service is available
	 * @param acceptorThreads no. of acceptors
	 * @param maxWorkerThreads max no. of worker threads
	 * @return Jetty Server instance
	 */
	@Named("DashboardJettyServer")
	@Provides
	@Singleton
	Server getDashboardJettyServer(@Named("Dashboard.service.port") int port,
			@Named("Dashboard.service.acceptors") int acceptorThreads,
			@Named("Dashboard.service.selectors") int selectorThreads,
			@Named("Dashboard.service.workers") int maxWorkerThreads,
			@Named("DashboardContext") WebAppContext webappContext) {
		QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(maxWorkerThreads);
		Server server = new Server(threadPool);
		ServerConnector http = new ServerConnector(server, acceptorThreads, selectorThreads);
		http.setPort(port);
		server.addConnector(http);
		server.setHandler(webappContext);
		server.setStopAtShutdown(true);
		return server;
	}

}
