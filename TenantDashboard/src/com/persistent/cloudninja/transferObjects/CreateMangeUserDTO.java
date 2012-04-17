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

import java.util.List;

import com.persistent.cloudninja.domainobject.IdentityProviderEntity;
import com.persistent.cloudninja.domainobject.Member;
import com.persistent.cloudninja.domainobject.MemberRole;

/*
 *  User DTO for Create User Flow
 * */
public class CreateMangeUserDTO {

    private Member member;

    private List<MemberRole> roleList;
    
    private String invitationCode;
    
    private String identityProvider;
    
    private List<IdentityProviderEntity> identityProviderList;
    
    

	/**
	 * @return the member
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * @param member the member to set
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * @return the roleList
	 */
	public List<MemberRole> getRoleList() {
		return roleList;
	}

	/**
	 * @param roleList the roleList to set
	 */
	public void setRoleList(List<MemberRole> roleList) {
		this.roleList = roleList;
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
 
}
