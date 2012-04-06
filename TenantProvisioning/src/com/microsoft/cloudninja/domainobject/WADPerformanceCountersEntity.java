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

import java.util.Date;

import org.soyatec.windowsazure.table.AbstractTableServiceEntity;

/**
 * Entity for WADPerformanceCountersTable azure table.
 *
 */
public class WADPerformanceCountersEntity extends AbstractTableServiceEntity {

	public WADPerformanceCountersEntity(String partitionKey, String rowKey) {
		super(partitionKey, rowKey);
	}

	private String role;
	private String roleInstance;
	private String instanceNo;
	private Long eventTickCount;
	private String counterName;
	private String counterValue;
	private String deploymentId;
	private Date date;

	/**
	 * Returns the role.
	 * 
	 * @return the role.
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 * 
	 * @param role : the role to set.
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Returns the roleInstance.
	 * 
	 * @return the roleInstance.
	 */
	public String getRoleInstance() {
		return roleInstance;
	}

	/**
	 * Sets the roleInstance.
	 * 
	 * @param roleInstance : the roleInstance to set.
	 */
	public void setRoleInstance(String roleInstance) {
		this.roleInstance = roleInstance;
	}

	/**
	 * Returns the instanceNo.
	 * 
	 * @return the instanceNo.
	 */
	public String getInstanceNo() {
		return instanceNo;
	}

	/**
	 * Sets the instanceNo.
	 * 
	 * @param instanceNo : the instanceNo to set.
	 */
	public void setInstanceNo(String instanceNo) {
		this.instanceNo = instanceNo;
	}

	/**
	 * Returns the eventTickCount.
	 * 
	 * @return the eventTickCount.
	 */
	public Long getEventTickCount() {
		return eventTickCount;
	}

	/**
	 * Sets the eventTickCount.
	 * 
	 * @param eventTickCount : the eventTickCount to set.
	 */
	public void setEventTickCount(Long eventTickCount) {
		this.eventTickCount = eventTickCount;
	}

	/**
	 * Returns the counterName.
	 * 
	 * @return the counterName.
	 */
	public String getCounterName() {
		return counterName;
	}

	/**
	 * Sets the counterName.
	 * 
	 * @param counterName : the counterName to set.
	 */
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	/**
	 * Returns the counterValue.
	 * 
	 * @return the counterValue.
	 */
	public String getCounterValue() {
		return counterValue;
	}

	/**
	 * Sets the counterValue.
	 * 
	 * @param counterValue : the counterValue to set.
	 */
	public void setCounterValue(String counterValue) {
		this.counterValue = counterValue;
	}

	/**
	 * Returns the deploymentId.
	 * 
	 * @return the deploymentId.
	 */
	public String getDeploymentId() {
		return deploymentId;
	}

	/**
	 * Sets the deploymentId.
	 * 
	 * @param deploymentId : the deploymentId to set.
	 */
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	/**
	 * Returns the date.
	 * 
	 * @return the date.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date : the date to set.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
}
