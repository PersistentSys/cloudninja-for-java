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

public class TenantBean {
	
	private String tenantId;
	
	private String tenantPassword;
	
	private String tenantLogin;

	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the tenantPassword
	 */
	public String getTenantPassword() {
		return tenantPassword;
	}

	/**
	 * @param tenantPassword the tenantPassword to set
	 */
	public void setTenantPassword(String tenantPassword) {
		this.tenantPassword = tenantPassword;
	}

	/**
	 * @return the tenantLogin
	 */
	public String getTenantLogin() {
		return tenantLogin;
	}

	/**
	 * @param tenantLogin the tenantLogin to set
	 */
	public void setTenantLogin(String tenantLogin) {
		this.tenantLogin = tenantLogin;
	}
	
	
}
