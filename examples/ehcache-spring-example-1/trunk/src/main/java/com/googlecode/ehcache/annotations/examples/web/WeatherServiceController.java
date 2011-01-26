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
package com.googlecode.ehcache.annotations.examples.web;

import java.util.Collection;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.googlecode.ehcache.annotations.examples.Weather;
import com.googlecode.ehcache.annotations.examples.WeatherService;

/**
 * {@link Controller} for interacting with the {@link WeatherService}.
 * 
 * @author Nicholas Blair
 * @version $Id$
 */
@Controller
public class WeatherServiceController {

	private WeatherService weatherService;
	private CacheManager cacheManager;
	private String cacheName = "weatherCache";
	/**
	 * To switch between the different {@link WeatherService} implementations,
	 * change the value of '@Qualifier("jdbc")' to the value of the '@Qualifier' annotation
	 * on the desired implementation (e.g. "simple" or "slow").
	 * 
	 * @param weatherService the weatherService to set
	 */
	@Autowired
	public void setWeatherService(@Qualifier("jdbc") WeatherService weatherService) {
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
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="main.html", method=RequestMethod.GET)
	public String displayAllKnownWeather(ModelMap model) {
		Collection<Weather> weathers = this.weatherService.getAllKnownWeather();
		model.put("knownWeather", weathers);
		Cache cache = this.cacheManager.getCache(cacheName);
		Statistics stats = cache.getStatistics();
			
		model.put("statistics", stats);
		return "main";
	}
	
	@RequestMapping(value="weather.html", method=RequestMethod.POST, params="action=create")
	public String createWeather(@RequestParam String zipCode, @RequestParam double currentTemperature,
			ModelMap model) {
		Weather result = this.weatherService.createNewWeather(zipCode, currentTemperature);
		model.put("weather", result);
		return "create-success";
	}
	
	@RequestMapping(value="weather.html", method=RequestMethod.POST, params="action=update")
	public String updateWeather(@RequestParam String zipCode, @RequestParam double newTemperature,
			ModelMap model) {
		Weather result = this.weatherService.updateWeather(zipCode, newTemperature);
		model.put("weather", result);
		return "update-success";
	}
	
	@RequestMapping(value="weather.html", method=RequestMethod.POST, params="action=delete")
	public String deleteWeather(@RequestParam String zipCode, ModelMap model) {
		boolean result = this.weatherService.deleteWeather(zipCode);
		model.put("zipCode", zipCode);
		model.put("removed", result);
		return "delete-success";
	}
	
	@RequestMapping(value="weather.html", method=RequestMethod.GET)
	public String getSpecificWeather(@RequestParam String zipCode, ModelMap model) {
		model.put("zipCode", zipCode);
		Weather weather = this.weatherService.getWeather(zipCode);
		model.put("weather", weather);
		return "zipcode-detail";
	}
}
