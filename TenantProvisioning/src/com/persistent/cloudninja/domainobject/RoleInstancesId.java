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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Id for RoleInstancesEntity.
 *
 */
@Embeddable
public class RoleInstancesId implements Serializable {

	private static final long serialVersionUID = 1L;
	private Date snapshotTime;
	private String roleName;

	public RoleInstancesId() {
	}

	public RoleInstancesId(Date snapshotTime, String roleName) {
		this.snapshotTime = snapshotTime;
		this.roleName = roleName;
	}

	/**
	 * Gets the snapshotTime.
	 * 
	 * @return the snapshotTime.
	 */
	@Column(name = "SnapshotTime")
	public Date getSnapshotTime() {
		return this.snapshotTime;
	}

	/**
	 * Sets the snapshotTime.
	 * 
	 * @param id : the snapshotTime to set.
	 */
	public void setSnapshotTime(Date snapshotTime) {
		this.snapshotTime = snapshotTime;
	}

	/**
	 * Gets the roleName.
	 * 
	 * @return the roleName.
	 */
	@Column(name = "RoleName")
	public String getRoleName() {
		return this.roleName;
	}

	/**
	 * Sets the roleName.
	 * 
	 * @param roleName : the roleName to set.
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
