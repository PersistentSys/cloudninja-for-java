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
package com.microsoft.cloudninja.dao;

import java.util.List;

import com.microsoft.cloudninja.domainobject.IdentityProviderEntity;
import com.microsoft.cloudninja.domainobject.Member;
import com.microsoft.cloudninja.exception.SystemException;

public interface ManageUsersDao {
	
	/*
	 *   Manage Users List
	 *   @param String TenantID
	 *   
	 * */
	List<Member> getManageUsersList(String tenantId) throws SystemException;
	
	
	
	/*
	 *   Create User
	 *   @param Member Object
	 *   
	 * */
	void createUser(Member member)throws SystemException;
	
	

	/*
	 *   Delete User
	 *   @param Member Object
	 * */
	void deleteUser(Member member)throws SystemException;
	
	
	
	/*
	 *   Update User
	 *   @param Member Object
	 * */

	void updateUser(Member member)throws SystemException;
	
	
	
	/**
	 * Get the member from database
	 * @param tenentId
	 * @param memberId
	 * @return Member object if found else returns null
	 */
	Member getMemberFromDb(String tenantId, String memberId)throws SystemException;


	/**
	 * Get the member from database using the tenantId and LiveGUID.
	 * @param tenentId
	 * @param gUID
	 * @return Member object if found else returns null
	 */
	Member getMemberFromDbUsingGUID(String tenantId, String gUID);

	/**
	 *   Find User for given Invitation Code and 
	 *   if found update the Live GUID
	 *   @param String invitationCode
	 *   @param String liveGuid
	 *   @return boolean
	 * 
	 */
	boolean findAndUpdateMember(String invitationCode, String liveGuid);
	
	
	/**
	 * Returns the list of Identity Providers
	 * 
	 * @return IdentityProviderEntity List
	 */
	public List<IdentityProviderEntity> getIdentityProviderList();

}
