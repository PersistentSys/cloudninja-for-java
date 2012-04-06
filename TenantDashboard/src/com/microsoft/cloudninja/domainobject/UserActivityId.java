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

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Id for UserActivity table.
 *
 */
@Embeddable
public class UserActivityId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Date timeInterval;
	private String tenantId;
	private String memberId;
	private int requests;

	public UserActivityId() {
	}

	public UserActivityId(Date timeInterval, String tenantId,
			String memberId, int requests) {
		this.timeInterval = timeInterval;
		this.tenantId = tenantId;
		this.memberId = memberId;
		this.requests = requests;
	}

	/**
	 * Returns the timeInterval.
	 * 
	 * @return the timeInterval.
	 */
	@Column(name = "TimeInterval")
	public Date getTimeInterval() {
		return this.timeInterval;
	}

	/**
	 * Sets the timeInterval.
	 * 
	 * @param timeInterval : the timeInterval to set.
	 */
	public void setTimeInterval(Date timeInterval) {
		this.timeInterval = timeInterval;
	}

	/**
	 * Returns the tenantId.
	 * 
	 * @return the tenantId.
	 */
	@Column(name = "TenantId")
	public String getTenantId() {
		return this.tenantId;
	}

	/**
	 * Sets the tenantId.
	 * 
	 * @param tenantId : the tenantId to set.
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * Returns the memberId.
	 * 
	 * @return the memberId.
	 */
	@Column(name = "MemberId")
	public String getMemberId() {
		return this.memberId;
	}

	/**
	 * Sets the memberId.
	 * 
	 * @param memberId : the memberId to set.
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	/**
	 * Returns the requests.
	 * 
	 * @return the requests.
	 */
	@Column(name = "Requests")
	public int getRequests() {
		return this.requests;
	}

	/**
	 * Sets the requests.
	 * 
	 * @param requests : the requests to set.
	 */
	public void setRequests(int requests) {
		this.requests = requests;
	}

}
