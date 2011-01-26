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
package com.googlecode.ehcache.annotations.examples.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.PartialCacheKey;
import com.googlecode.ehcache.annotations.Property;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.examples.Weather;
import com.googlecode.ehcache.annotations.examples.WeatherService;

/**
 * {@link WeatherService} implementation backed by a synchronized {@link Map}
 * but "slow" to simulate a poor performing remote system. 
 * 
 * @see Thread#sleep(long)
 * @author Nicholas Blair
 * @version $Id$
 */
@Service
@Qualifier("slow")
public class SlowWeatherServiceImpl implements WeatherService {

	private final Map<String, Weather> storage = Collections.synchronizedMap(new HashMap<String, Weather>());
	private long delay = 3000L;
	
	/**
	 * @param delay the delay to set
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.ehcache.annotations.examples.WeatherService#createNewWeather(java.lang.String, double)
	 */
	public Weather createNewWeather(String zipCode, double currentTemperature) {
		delay();
		Weather weather = new Weather();
		weather.setCurrentTemperature(currentTemperature);
		weather.setZipCode(zipCode);
		storage.put(zipCode, weather);
		
		return weather;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.ehcache.annotations.examples.WeatherService#deleteWeather(com.googlecode.ehcache.annotations.examples.Weather)
	 */
	@TriggersRemove(cacheName="weatherCache", 
			keyGenerator=@KeyGenerator(
			name="ListCacheKeyGenerator",properties=@Property(name="includeMethod",value="false")))
	public boolean deleteWeather(String zipCode) {
		delay();
		return storage.remove(zipCode) != null;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.ehcache.annotations.examples.WeatherService#getWeather(java.lang.String)
	 */
	@Cacheable(cacheName="weatherCache", selfPopulating=true,
			keyGenerator=@KeyGenerator(
			name="ListCacheKeyGenerator",properties=@Property(name="includeMethod",value="false")))
	public Weather getWeather(String zipCode) {
		delay();
		Weather result = storage.get(zipCode);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.ehcache.annotations.examples.WeatherService#updateWeather(java.lang.String, double)
	 */
	@TriggersRemove(cacheName="weatherCache", keyGenerator=@KeyGenerator(
			name="ListCacheKeyGenerator",properties=@Property(name="includeMethod",value="false")))
	public Weather updateWeather(@PartialCacheKey String zipCode, double newTemperature) {
		delay();
		Weather result = storage.get(zipCode);
		if(result == null) {
			return createNewWeather(zipCode, newTemperature);
		} else {
			result.setCurrentTemperature(newTemperature);
			return result;
		}
	}

	
	/* (non-Javadoc)
	 * @see com.googlecode.ehcache.annotations.examples.WeatherService#getAllKnownWeather()
	 */
	public Collection<Weather> getAllKnownWeather() {
		return this.storage.values();
	}

	/**
	 * The reason why this service is slow.
	 */
	protected void delay() {
		try {
			Thread.sleep(this.delay);
		} catch (InterruptedException e) { 
			// meh
		}
	}
}
