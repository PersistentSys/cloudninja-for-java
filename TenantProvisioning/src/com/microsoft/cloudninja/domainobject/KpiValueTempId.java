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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Class which forms a ID for KpiValueTempEntity.
 *
 */
@Embeddable
public class KpiValueTempId implements Serializable {

	private static final long serialVersionUID = 1L;
	private String roleId;
	private String instance;
	private int instanceNo;
	private Date timeStamp;
	private String kpiTypeId;
	private long value;

	public KpiValueTempId() {
	}

	public KpiValueTempId(String roleId, String instance,
			int instanceNo, Date timeStamp, String kpiTypeId, long value) {
		this.roleId = roleId;
		this.instance = instance;
		this.instanceNo = instanceNo;
		this.timeStamp = timeStamp;
		this.kpiTypeId = kpiTypeId;
		this.value = value;
	}

	/**
	 * Returns the roleId.
	 * @return the roleId.
	 */
	@Column(name = "RoleId")
	public String getRoleId() {
		return this.roleId;
	}

	/**
	 * Sets the roleId.
	 * @param roleId : the roleId to set.
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * Returns the instance.
	 * @return the instance.
	 */
	@Column(name = "Instance")
	public String getInstance() {
		return this.instance;
	}

	/**
	 * Sets the instance.
	 * @param instance : the instance to set.
	 */
	public void setInstance(String instance) {
		this.instance = instance;
	}

	/**
	 * Returns the instanceNo.
	 * @return the instanceNo.
	 */
	@Column(name = "InstanceNo")
	public int getInstanceNo() {
		return this.instanceNo;
	}

	/**
	 * Sets the instanceNo.
	 * @param instanceNo : the instanceNo to set.
	 */
	public void setInstanceNo(int instanceNo) {
		this.instanceNo = instanceNo;
	}

	/**
	 * Returns the timeStamp.
	 * @return the timeStamp.
	 */
	@Column(name = "TimeStamp")
	public Date getTimeStamp() {
		return this.timeStamp;
	}

	/**
	 * Sets the timeStamp.
	 * @param timeStamp : the timeStamp to set.
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Returns the kpiTypeId.
	 * @return the kpiTypeId.
	 */
	@Column(name = "KpiTypeId")
	public String getKpiTypeId() {
		return this.kpiTypeId;
	}

	/**
	 * Sets the kpiTypeId.
	 * @param kpiTypeId : the kpiTypeId to set.
	 */
	public void setKpiTypeId(String kpiTypeId) {
		this.kpiTypeId = kpiTypeId;
	}

	/**
	 * Returns the value.
	 * @return the value.
	 */
	@Column(name = "Value")
	public long getValue() {
		return this.value;
	}

	/**
	 * Sets the value.
	 * @param value : the value to set.
	 */
	public void setValue(long value) {
		this.value = value;
	}

}
