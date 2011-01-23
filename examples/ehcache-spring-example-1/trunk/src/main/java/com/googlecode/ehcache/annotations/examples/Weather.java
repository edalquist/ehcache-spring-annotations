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

/**
 * Simple weather bean.
 * 
 * @author Nicholas Blair
 * @version $Id$
 */
public class Weather {

	private String zipCode;
	private double currentTemperature;
	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	/**
	 * @return the currentTemperature
	 */
	public double getCurrentTemperature() {
		return currentTemperature;
	}
	/**
	 * @param currentTemperature the currentTemperature to set
	 */
	public void setCurrentTemperature(double currentTemperature) {
		this.currentTemperature = currentTemperature;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(currentTemperature);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Weather)) {
			return false;
		}
		Weather other = (Weather) obj;
		if (Double.doubleToLongBits(currentTemperature) != Double
				.doubleToLongBits(other.currentTemperature)) {
			return false;
		}
		if (zipCode == null) {
			if (other.zipCode != null) {
				return false;
			}
		} else if (!zipCode.equals(other.zipCode)) {
			return false;
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Weather [currentTemperature=");
		builder.append(currentTemperature);
		builder.append(", zipCode=");
		builder.append(zipCode);
		builder.append("]");
		return builder.toString();
	}
	
}
