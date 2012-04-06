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
 * Entity for IdentityProvider Table. 
 */
@Entity
@Table(name="IdentityProvider")
public class IdentityProviderEntity {
	
	private int id;
	private String name;
	
	/**
	 * Gets the Id.
	 * 
	 * @return the Id.
	 */
	@Id
	@Column(name="Id")
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the Id.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the Identity Provider's name.
	 * 
	 * @return the name.
	 */
	@Column(name="Name")
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the Identity Provider's name.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
