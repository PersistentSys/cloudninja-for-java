/*******************************************************************************
 * Copyright 2012 Persistent Systems Ltd.
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
 ******************************************************************************/
package com.microsoft.cloudninja.domainobject;

/**
 * Entity for query which returns database bandwidth usage.
 *
 */
public class DBBandwidthUsageEntity {

	private String usageDay;
	private String databaseName;
	private BandwidthUsageDirection direction;
	private long kiloBytes;
	
	/**
	 * Returns the usage day.
	 * 
	 * @return the usageDay.
	 */
	public String getUsageDay() {
		return usageDay;
	}
	
	/**
	 * Sets the usage day.
	 * 
	 * @param usageDay
	 */
	public void setUsageDay(String usageDay) {
		this.usageDay = usageDay;
	}
	
	/**
	 * Returns the database name.
	 * 
	 * @return the database name.
	 */
	public String getDatabaseName() {
		return databaseName;
	}
	
	/**
	 * Sets the database name.
	 * 
	 * @param databaseName : database name to be set.
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	/**
	 * Returns the direction.
	 * 
	 * @return the direction.
	 */
	public BandwidthUsageDirection getDirection() {
		return direction;
	}
	
	/**
	 * Sets the direction.
	 * 
	 * @param direction : the direction to be set.
	 */
	public void setDirection(BandwidthUsageDirection direction) {
		this.direction = direction;
	}
	
	/**
	 * Returns the kilo bytes.
	 * 
	 * @return the kilo bytes.
	 */
	public long getKiloBytes() {
		return kiloBytes;
	}
	
	/**
	 * Sets the kilo bytes.
	 * 
	 * @param kiloBytes : the kiloBytes to be set.
	 */
	public void setKiloBytes(long kiloBytes) {
		this.kiloBytes = kiloBytes;
	}
	
}
