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
package com.persistent.cloudninja.transferObjects;

import java.util.ArrayList;
import java.util.List;

import com.persistent.cloudninja.domainobject.IdentityProviderEntity;

/**
 * This class is used as a data transfer object while 
 * provisioning a tenant.
 *
 */
public class ProvisioningTenantDTO {

	private String demoKey;
	private String tenantId;
	private String companyName;
	private String memberId;
	private String memberName;
	private String memberPassword;
	private String successMessage;
	private String identityProvider;
	private List<IdentityProviderEntity> identityProviderList;
	private String invitationCode;

	public ProvisioningTenantDTO() {
		demoKey = "";
		tenantId = "";
		companyName = "";
		memberId = "";
		memberName = "";
		memberPassword = "";
		identityProvider = "";
		identityProviderList = new ArrayList<IdentityProviderEntity>();
		invitationCode = "";
	}	
	
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
	 * @return the Demo Key
	 */
	public String getDemoKey() {
		return demoKey;
	}

	/**
	 * @param demoKey the demoKey to set
	 */
	public void setDemoKey(String demoKey) {
		this.demoKey = demoKey;
	}

	/**
	 * @return the Company Name
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the Member Id
	 */
	public String getMemberId() {
		return memberId;
	}

	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	/**
	 * @return the Member name
	 */
	public String getMemberName() {
		return memberName;
	}

	/**
	 * @param memberName the memberName to set
	 */
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	/**
	 * @return the member password
	 */
	public String getMemberPassword() {
		return memberPassword;
	}

	/**
	 * @param memberPassword the memberPassword to set
	 */
	public void setMemberPassword(String memberPassword) {
		this.memberPassword = memberPassword;
	}

	/**
	 * @return the successMessage
	 */
	public String getSuccessMessage() {
		return successMessage;
	}

	/**
	 * @param successMessage the successMessage to set
	 */
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	/**
	 * @return the identityProvider
	 */
	public String getIdentityProvider() {
		return identityProvider;
	}

	/**
	 * @param identityProvider the identityProvider to set
	 */
	public void setIdentityProvider(String identityProvider) {
		this.identityProvider = identityProvider;
	}

	

	/**
	 * @return the identityProviderList
	 */
	public List<IdentityProviderEntity> getIdentityProviderList() {
		return identityProviderList;
	}

	/**
	 * @param identityProviderList the identityProviderList to set
	 */
	public void setIdentityProviderList(
			List<IdentityProviderEntity> identityProviderList) {
		this.identityProviderList = identityProviderList;
	}

	/**
	 * @return the invitationCode
	 */
	public String getInvitationCode() {
		return invitationCode;
	}

	/**
	 * @param invitationCode the invitationCode to set
	 */
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
	
}
