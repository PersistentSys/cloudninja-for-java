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
package com.persistent.cloudninja.web.security;

import java.io.Serializable;
import java.util.Date;

import org.springframework.security.core.userdetails.User;

public class CloudNinjaUser implements Serializable {

	private static final long serialVersionUID = -957347657178177647L;
	
	private User user;
	private String tenantId;
	private String memberId;
	private Date authenticatedSessionStartTime;
	// will be used only in case of LIVEID
	private String liveGUID;

	private Date authenticatedSessionExpiryTime;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getTenantId() {
		return tenantId;
	}
	
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public String getMemberId() {
		return memberId;
	}
	
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public Date getAuthenticatedSessionStartTime() {
		return authenticatedSessionStartTime;
	}
	
	public void setAuthenticatedSessionStartTime(Date authenticatedSessionStartTime) {
		this.authenticatedSessionStartTime = authenticatedSessionStartTime;
	}
	
	public Date getAuthenticatedSessionExpiryTime() {
		return authenticatedSessionExpiryTime;
	}
	
	public void setAuthenticatedSessionExpiryTime(
			Date authenticatedSessionExpiryTime) {
		this.authenticatedSessionExpiryTime = authenticatedSessionExpiryTime;
	}

	/**
	 * @return the liveGUID
	 */
	public String getLiveGUID() {
		return liveGUID;
	}

	/**
	 * @param liveGUID the liveGUID to set
	 */
	public void setLiveGUID(String liveGUID) {
		this.liveGUID = liveGUID;
	}

	@Override
	public String toString() {
		String desc = "tenantId: "+ tenantId+" userId: "+user.getUsername()+" role: "+user.getAuthorities().toString();
		return desc;
	}
}
