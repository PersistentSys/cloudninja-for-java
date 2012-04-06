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
 * Entity class for TenantDataConnection. 
 *
 */
@Entity
@Table(name = "TenantDataConnection")
public class TenantDataConnectionEntity {

	private String tenantId;
	private String server;
	private String database;
	private String user;
	private String password;
	private String passwordAlt;

	public TenantDataConnectionEntity() {
	}

	public TenantDataConnectionEntity(String tenantId, String server,
			String database, String user, String password,
			String passwordAlt) {
		this.tenantId = tenantId;
		this.server = server;
		this.database = database;
		this.user = user;
		this.password = password;
		this.passwordAlt = passwordAlt;
	}

	/**
	 * Returns the tenantId.
	 * 
	 * @return the tenantId.
	 */
	@Id
	@Column(name = "TenantId")
	public String getTenantId() {
		return this.tenantId;
	}

	/**
	 * Sets the tenantId.
	 * 
	 * @param tenantId : the id of the tenant to be set.
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * Returns the server.
	 * 
	 * @return the server.
	 */
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
	 * Returns the database.
	 * 
	 * @return the database.
	 */
	@Column(name = "[Database]")
	public String getDatabase() {
		return this.database;
	}

	/**
	 * Sets the database.
	 * 
	 * @param database : the database to set.
	 */
	public void setDatabase(String database) {
		this.database = database;
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
	 * Returns the passwordAlt.
	 * 
	 * @return the passwordAlt.
	 */
	@Column(name = "PasswordAlt")
	public String getPasswordAlt() {
		return this.passwordAlt;
	}

	/**
	 * Sets the passwordAlt.
	 * 
	 * @param passwordAlt : the passwordAlt to set.
	 */
	public void setPasswordAlt(String passwordAlt) {
		this.passwordAlt = passwordAlt;
	}

}
