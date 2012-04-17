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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity for RoleInstances table.
 *
 */
@Entity
@Table(name = "RoleInstances")
public class RoleInstancesEntity {

	private RoleInstancesId id;
	private int instanceCount;
	private String reason;

	public RoleInstancesEntity() {
	}

	public RoleInstancesEntity(RoleInstancesId id, int instanceCount,
			String reason) {
		this.id = id;
		this.instanceCount = instanceCount;
		this.reason = reason;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id.
	 */
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "snapshotTime", column = @Column(name = "SnapshotTime")),
			@AttributeOverride(name = "roleName", column = @Column(name = "RoleName")) })
	public RoleInstancesId getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id : the id to set.
	 */
	public void setId(RoleInstancesId id) {
		this.id = id;
	}

	/**
	 * Gets the instanceCount.
	 * 
	 * @return the instanceCount.
	 */
	@Column(name = "InstanceCount", nullable = false)
	public int getInstanceCount() {
		return this.instanceCount;
	}

	/**
	 * Sets the instanceCount.
	 * 
	 * @param instanceCount : the instanceCount to set.
	 */
	public void setInstanceCount(int instanceCount) {
		this.instanceCount = instanceCount;
	}

	/**
	 * Gets the reason.
	 * 
	 * @return the reason.
	 */
	@Column(name = "Reason", nullable = false)
	public String getReason() {
		return this.reason;
	}

	/**
	 * Sets the reason.
	 * 
	 * @param reason : the reason to set.
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

}
