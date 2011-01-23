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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContextException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.PartialCacheKey;
import com.googlecode.ehcache.annotations.Property;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.examples.Weather;
import com.googlecode.ehcache.annotations.examples.WeatherService;

/**
 * Spring JDBC backed example implementation of {@link WeatherService}.
 * 
 * @author Nicholas Blair
 * @version $Id$
 */
@Service
@Qualifier("jdbc")
public class SpringJdbcWeatherServiceImpl implements WeatherService, InitializingBean {

	private SimpleJdbcTemplate simpleJdbcTemplate;
	/**
	 * 
	 * @param dataSource
	 */
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if(null == this.simpleJdbcTemplate) {
			throw new ApplicationContextException("dataSource is required");
		}
		
		this.simpleJdbcTemplate.getJdbcOperations().execute("CREATE TABLE WEATHER (ZIPCODE varchar (10) NOT NULL, CURRENT_TEMP real not null)");
	}
	/* (non-Javadoc)
	 * @see com.googlecode.ehcache.annotations.examples.WeatherService#createNewWeather(java.lang.String, double)
	 */
	public Weather createNewWeather(String zipCode, double currentTemperature) {
		this.simpleJdbcTemplate.update("insert into WEATHER (ZIPCODE, CURRENT_TEMP) values (?, ?)", zipCode, currentTemperature);
		return getWeather(zipCode);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.ehcache.annotations.examples.WeatherService#deleteWeather(java.lang.String)
	 */
	@TriggersRemove(cacheName="weatherCache", 
			keyGenerator=@KeyGenerator(
			name="ListCacheKeyGenerator",properties=@Property(name="includeMethod",value="false")))
	public boolean deleteWeather(String zipCode) {
		int rows = this.simpleJdbcTemplate.update("delete from WEATHER where ZIPCODE=?", zipCode);
		return rows == 1;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.ehcache.annotations.examples.WeatherService#getAllKnownWeather()
	 */
	public Collection<Weather> getAllKnownWeather() {
		List<Weather> results = this.simpleJdbcTemplate.query("select * from WEATHER", new WeatherRowMapper());
		return results;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.ehcache.annotations.examples.WeatherService#getWeather(java.lang.String)
	 */
	@Cacheable(cacheName="weatherCache", keyGenerator=@KeyGenerator(
			name="ListCacheKeyGenerator",properties=@Property(name="includeMethod",value="false")))
	public Weather getWeather(String zipCode) {
		List<Weather> results = this.simpleJdbcTemplate.query("select * from WEATHER where ZIPCODE=?", new WeatherRowMapper(), zipCode);
		return DataAccessUtils.singleResult(results);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.ehcache.annotations.examples.WeatherService#updateWeather(java.lang.String, double)
	 */
	@TriggersRemove(cacheName="weatherCache", keyGenerator=@KeyGenerator(
			name="ListCacheKeyGenerator",properties=@Property(name="includeMethod",value="false")))
	public Weather updateWeather(@PartialCacheKey String zipCode, double newTemperature) {
		this.simpleJdbcTemplate.update("update weather set CURRENT_TEMP=? where ZIPCODE=?", newTemperature, zipCode);		
		return getWeather(zipCode);
	}
	
	/**
	 * {@link RowMapper} for mapping {@link Weather} objects.
	 * 
	 * @author Nicholas Blair
	 * @version $Id$
	 */
	private static class WeatherRowMapper implements RowMapper<Weather> {
		public Weather mapRow(ResultSet rs, int rowNum) throws SQLException {
			Weather weather = new Weather();
			weather.setCurrentTemperature(rs.getDouble("CURRENT_TEMP"));
			weather.setZipCode(rs.getString("ZIPCODE"));
			return weather;
		}
	}

}
