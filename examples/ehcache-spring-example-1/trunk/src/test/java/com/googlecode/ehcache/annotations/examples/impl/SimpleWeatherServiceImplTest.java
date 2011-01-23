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

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.googlecode.ehcache.annotations.examples.Weather;
import com.googlecode.ehcache.annotations.examples.WeatherService;

/**
 * Example unit test for the {@link SimpleWeatherServiceImpl}.
 * 
 * Things of importance:
 * <ul>
 * <li>This test makes use of JUnit's {@link RunWith} and Spring's {@link ContextConfiguration} annotations.</li>
 * <li>This test uses the same spring applicationContext (src/main/resources/contexts/applicationContext.xml) as
 * the webapp since the example is very simple. This may or may not be possible in your application; 
 * it's often desirable to create a separate applicationContext for your unit tests.</li>
 * <li>The {@link CacheManager} reference expected for this test is solely for demonstrating/verifying that
 * the ehcache-spring-annotations configuration results in the desired ehcache effects.</li>
 * </ul>
 * 
 * @author Nicholas Blair
 * @version $Id$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:contexts/applicationContext.xml")
public class SimpleWeatherServiceImplTest {

	private WeatherService weatherService;
	private CacheManager cacheManager;
	private String cacheName = "weatherCache";
	/**
	 * @param weatherService the weatherService to set
	 */
	@Autowired
	public void setWeatherService(@Qualifier("simple") WeatherService weatherService) {
		this.weatherService = weatherService;
	}
	/**
	 * @param cacheManager the cacheManager to set
	 */
	@Autowired
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	/**
	 * @param cacheName the cacheName to set
	 */
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	
	/**
	 * Simple control test; insert a few records, retrieve the same records, assert
	 * expected cache statistics.
	 */
	@Test
	public void testControl() {
		Cache weatherCache = this.cacheManager.getCache(cacheName);
		Assert.assertNotNull(weatherCache);
		
		Weather created = this.weatherService.createNewWeather("53711", 3.0);
		Assert.assertNotNull(created);
		
		Assert.assertEquals(0, weatherCache.getStatistics().getCacheHits());
		Assert.assertEquals(0, weatherCache.getStatistics().getCacheMisses());
		Assert.assertEquals(0, weatherCache.getStatistics().getMemoryStoreObjectCount());
		
		Weather retrieved = this.weatherService.getWeather("53711");
		Assert.assertEquals(created, retrieved);
		
		Assert.assertEquals(0, weatherCache.getStatistics().getCacheHits());
		Assert.assertEquals(1, weatherCache.getStatistics().getCacheMisses());
		Assert.assertEquals(1, weatherCache.getStatistics().getMemoryStoreObjectCount());
		
		retrieved = this.weatherService.getWeather("53711");
		Assert.assertEquals(1, weatherCache.getStatistics().getCacheHits());
		Assert.assertEquals(1, weatherCache.getStatistics().getCacheMisses());
		Assert.assertEquals(1, weatherCache.getStatistics().getMemoryStoreObjectCount());
		
		this.weatherService.createNewWeather("53706", 2.0);
		this.weatherService.getWeather("53706");
		
		Assert.assertEquals(1, weatherCache.getStatistics().getCacheHits());
		Assert.assertEquals(2, weatherCache.getStatistics().getCacheMisses());
		Assert.assertEquals(2, weatherCache.getStatistics().getMemoryStoreObjectCount());
		
		this.weatherService.getWeather("53706");
		this.weatherService.getWeather("53706");
		
		Assert.assertEquals(3, weatherCache.getStatistics().getCacheHits());
		Assert.assertEquals(2, weatherCache.getStatistics().getCacheMisses());
		Assert.assertEquals(2, weatherCache.getStatistics().getMemoryStoreObjectCount());
	}
	
	
}
