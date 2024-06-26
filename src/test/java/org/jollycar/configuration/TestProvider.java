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

import java.util.Properties;

/**
 * @author sven
 * 
 */
public class TestProvider implements ConfigurationProvider {
	
	private Properties properties = new Properties();
	
	public TestProvider(){
		properties.setProperty("key", "value");
	}
	
	@Override
	public Properties getProperties() {
		return properties;
	}
}
