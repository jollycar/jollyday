/**
 * Copyright 2012 Sven Diedrichsen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jollycar.configuration;

import org.jollycar.ManagerParameter;
import org.jollycar.configuration.impl.DefaultConfigurationProvider;
import org.jollycar.configuration.impl.URLConfigurationProvider;
import org.jollycar.util.ClassLoadingUtil;

import java.util.logging.Logger;

/**
 * Manages the configuration provider implementations and thus delivering the
 * jollyday configuration.
 *
 * @author Sven Diedrichsen
 */
public class ConfigurationProviderManager {

	private static final Logger LOG = Logger.getLogger(ConfigurationProviderManager.class.getName());

	private ConfigurationProvider defaultConfigurationProvider = new DefaultConfigurationProvider();
	private ConfigurationProvider urlConfigurationProvider = new URLConfigurationProvider();
	private ClassLoadingUtil classLoadingUtil = new ClassLoadingUtil();

	/**
	 * Reads the jollyday configuration from the
	 * {@link DefaultConfigurationProvider}, the
	 * {@link URLConfigurationProvider} and any configuration provider specified
	 * by the system property 'org.jollycar.config.provider'.
	 *
	 * @param parameter
	 *            the configuration {@link ManagerParameter} to use
	 */
	public void mergeConfigurationProperties(ManagerParameter parameter) {
		addInternalConfigurationProviderProperties(parameter);
		addCustomConfigurationProviderProperties(parameter);
	}

	private void addInternalConfigurationProviderProperties(ManagerParameter parameter) {
		parameter.mergeProperties(urlConfigurationProvider.getProperties());
		parameter.mergeProperties(defaultConfigurationProvider.getProperties());
	}

	private void addCustomConfigurationProviderProperties(ManagerParameter parameter) {
		String providersStrList = System.getProperty(ConfigurationProvider.CONFIG_PROVIDERS_PROPERTY);
		if (providersStrList != null) {
			String[] providersClassNames = providersStrList.split(",");
			for (String providerClassName : providersClassNames) {
				if (providerClassName == null || "".equals(providerClassName))
					continue;
				try {
					Class<?> providerClass = Class.forName(providerClassName.trim(), true,
							classLoadingUtil.getClassloader());
					ConfigurationProvider configurationProvider = ConfigurationProvider.class.cast(providerClass.getDeclaredConstructor().newInstance());
					parameter.mergeProperties(configurationProvider.getProperties());
				} catch (Exception e) {
					LOG.warning("Cannot load configuration from provider class '" + providerClassName + "'. "
							+ e.getClass().getSimpleName() + " (" + e.getMessage() + ").");
				}
			}
		}
	}

}
