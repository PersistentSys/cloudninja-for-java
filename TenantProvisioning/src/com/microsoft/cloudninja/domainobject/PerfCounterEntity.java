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

public class PerfCounterEntity extends AbstractTableServiceEntity {


	public PerfCounterEntity(String partitionKey, String rowKey) {
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRoleInstance() {
		return roleInstance;
	}

	public void setRoleInstance(String roleInstance) {
		this.roleInstance = roleInstance;
	}

	public String getInstanceNo() {
		return instanceNo;
	}

	public void setInstanceNo(String instanceNo) {
		this.instanceNo = instanceNo;
	}

	public Long getEventTickCount() {
		return eventTickCount;
	}

	public void setEventTickCount(Long eventTickCount) {
		this.eventTickCount = eventTickCount;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public String getCounterValue() {
		return counterValue;
	}

	public void setCounterValue(String counterValue) {
		this.counterValue = counterValue;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
