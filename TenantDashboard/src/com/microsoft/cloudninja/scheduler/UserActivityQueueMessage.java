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
package com.microsoft.cloudninja.scheduler;

import java.util.Date;

/**
 * Message for UserActivityQueue.
 * It contains tenantId, userId and the timestamp when user logged in.
 *
 */
public class UserActivityQueueMessage {
	
	private String tenantId;
	private String userId;
	private Date timestamp;
	
	public UserActivityQueueMessage(String tenantId, String userId, Date date) {
		this.tenantId = tenantId;
		this.userId = userId;
		this.timestamp = date;
	}

	/**
	 * Returns the tenantId.
	 * 
	 * @return the tenantId.
	 */
	public String getTenantId() {
		return tenantId;
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
	 * Returns the userId.
	 * 
	 * @return the userId.
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * Sets the userId.
	 * 
	 * @param userId : the userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * Returns the timestamp.
	 * 
	 * @return the timestamp.
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Sets the timestamp.
	 * 
	 * @param timestamp : the timestamp to set.
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return tenantId + "_" + userId;
	}
	
}
