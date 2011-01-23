/**
 * Copyright 2011 Nicholas Blair, Eric Dalquist
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.ehcache.annotations.examples;

import java.util.Collection;

/**
 * Simple service interface for creating, retrieving, updating, and deleting
 * {@link Weather} instances.
 * 
 * @author Nicholas Blair
 * @version $Id$
 */
public interface WeatherService {

	Weather createNewWeather(String zipCode, double currentTemperature);
	
	Weather getWeather(String zipCode);
	
	Collection<Weather> getAllKnownWeather();
	
	Weather updateWeather(String zipCode, double newTemperature);
	
	boolean deleteWeather(String zipCode);
}
