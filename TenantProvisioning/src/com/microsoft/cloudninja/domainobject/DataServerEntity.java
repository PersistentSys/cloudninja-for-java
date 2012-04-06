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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class for DataServer. 
 *
 */
@Entity
@Table(name = "DataServer")
public class DataServerEntity {

	private String server;
	private int locationId;
	private String user;
	private String password;
	private boolean primary;
	private int maxDatabases;

	/**
	 * Returns the server.
	 * 
	 * @return the server.
	 */
	@Id
	@Column(name = "Server")
	public String getServer() {
		return this.server;
	}

	/**
	 * Sets the server.
	 * 
	 * @param server : the server to set.
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * Returns the locationId.
	 * 
	 * @return the locationId.
	 */
	@Column(name = "LocationId")
	public int getLocationId() {
		return this.locationId;
	}

	/**
	 * Sets the locationId.
	 * 
	 * @param locationId : the locationId to set.
	 */
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	/**
	 * Returns the user.
	 * 
	 * @return the user.
	 */
	@Column(name = "[User]")
	public String getUser() {
		return this.user;
	}

	/**
	 * Sets the user.
	 * 
	 * @param user : the user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Returns the password.
	 * 
	 * @return the password.
	 */
	@Column(name = "Password")
	public String getPassword() {
		return this.password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password : the password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns true if database is primary else false.
	 * 
	 * @return true if database is primary else false.
	 */
	@Column(name = "IsPrimary")
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * Sets true if database is primary else false.
	 * 
	 * @param primary : true if database is primary else false.
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	/**
	 * Returns the maxDatabases.
	 * 
	 * @return the maxDatabases.
	 */
	@Column(name = "MaxDatabases")
	public int getMaxDatabases() {
		return this.maxDatabases;
	}

	/**
	 * Sets the maxDatabases.
	 * 
	 * @param maxDatabases : the maxDatabases to set.
	 */
	public void setMaxDatabases(int maxDatabases) {
		this.maxDatabases = maxDatabases;
	}

}
