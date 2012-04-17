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
package com.persistent.cloudninja.domainobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity for Role table.
 *
 */
@Entity
@Table(name = "Role")
public class RoleEntity {

	private int roleId;
	private String name;

	public RoleEntity() {
	}

	public RoleEntity(int roleId, String name) {
		this.roleId = roleId;
		this.name = name;
	}

	/**
	 * Gets the roleId.
	 * 
	 * @return the roleId.
	 */
	@Id
	@Column(name = "RoleId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getRoleId() {
		return this.roleId;
	}

	/**
	 * Sets the roleId.
	 * 
	 * @param roleId : the roleId to set.
	 */
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	/**
	 * Gets the role name.
	 * 
	 * @return the role name.
	 */
	@Column(name = "Name", nullable = false)
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the role name.
	 * 
	 * @param name : the role name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

}
