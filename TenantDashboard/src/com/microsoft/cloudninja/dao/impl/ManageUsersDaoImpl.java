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
package com.microsoft.cloudninja.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.microsoft.cloudninja.dao.ManageUsersDao;
import com.microsoft.cloudninja.domainobject.IdentityProviderEntity;
import com.microsoft.cloudninja.domainobject.Member;
import com.microsoft.cloudninja.exception.SystemException;

@Repository("manageUsersDao")
public class ManageUsersDaoImpl implements ManageUsersDao {
	
	private static final Logger LOGGER = Logger.getLogger(ManageUsersDaoImpl.class);
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<Member> getManageUsersList(String tenantId) throws SystemException {
		LOGGER.info("ManageUsersDaoImpl: getManageUsersList Start");
		List<Member> memberList=null;
		try {
			memberList = hibernateTemplate.find(
					"from Member where TenantId=?", tenantId);
		
			
		//	memberList = hibernateTemplate.find("from Member");
			
		}catch (DataAccessException exception) {
			LOGGER.error(exception);
			throw new SystemException(exception);
		}
		LOGGER.info("ManageUsersDaoImpl: getManageUsersList End");
		return memberList;
	}

	@Override
	public void createUser(Member member) throws SystemException {
		LOGGER.info("ManageUsersDaoImpl: createUser Start");
		try {
				hibernateTemplate.save(member);
		
		}catch (DataAccessException exception) {
			LOGGER.error(exception);
			throw new SystemException(exception);
		}
		LOGGER.info("ManageUsersDaoImpl: createUser End");
	}

	@Override
	public void deleteUser(Member member) throws SystemException {
		LOGGER.info("ManageUsersDaoImpl: deleteUser Start");
		try {
				hibernateTemplate.delete(member);
	
		}catch (DataAccessException exception) {
			LOGGER.error(exception);
			throw new SystemException(exception);
		}
		LOGGER.info("ManageUsersDaoImpl: deleteUser End");
	}

	@Override
	public void updateUser(Member member) throws SystemException {
		LOGGER.info("ManageUsersDaoImpl: updateUser Start");
		try {
				hibernateTemplate.saveOrUpdate(member);
				
		}catch (DataAccessException exception) {
			LOGGER.error(exception);
			throw new SystemException(exception);
		}
		LOGGER.info("ManageUsersDaoImpl: updateUser End");
	}
	
	

	/**
	 * Get the member from database
	 * @param tenentId
	 * @param memberId
	 * @return Member object if found else returns null
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Member getMemberFromDb(String tenantId, String memberId) throws SystemException {
		LOGGER.info("ManageUsersDaoImpl: getMemberFromDb Start");
		Member member = null;
		List<Member> memberList = null;
		
		try {
			memberList = hibernateTemplate.find("from Member where TenantId=? AND MemberId=?", tenantId, memberId);
			if (null != memberList && memberList.size() > 0) {
			   member = memberList.get(0);
			}
		} catch (DataAccessException de) {
			LOGGER.error(de.getMessage(), de);
			member = null;
		}
		LOGGER.info("ManageUsersDaoImpl: getMemberFromDb End");
		return member;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Member getMemberFromDbUsingGUID(String tenantId, String GUID) {
		LOGGER.info("ManageUsersDaoImpl: getMemberFromDbUsingGUID Start");
		Member member = null;
		List<Member> memberList = null;
		
		try {
			memberList = hibernateTemplate.find("from Member where TenantId=? AND LiveGUID=?", tenantId, GUID);
			if (null != memberList && memberList.size() > 0) {
			   member = memberList.get(0);
			}
		} catch (DataAccessException de) {
			LOGGER.error(de.getMessage(), de);
			member = null;
		}
		LOGGER.info("ManageUsersDaoImpl: getMemberFromDbUsingGUID End");
		return member;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean findAndUpdateMember(String invitationCode, String liveGuid) {
		boolean isPresent = false;
		List<Member> memberList=null;
		memberList = hibernateTemplate.find("from Member where LiveInvitationCode=?", invitationCode);
		if(null != memberList && memberList.size() > 0 ){
			Member member = memberList.get(0);
			member.setLiveInvitationCode(null);
			member.setLiveGUID(liveGuid);
			hibernateTemplate.saveOrUpdate(member);
			isPresent=true;
		}
		return isPresent;
	}
	
	/**
	 * Returns the IdentityProviderEntity List.
	 * 
	 * @return IdentityProviderEntity List.
	 */
	@SuppressWarnings("unchecked")
	public List<IdentityProviderEntity> getIdentityProviderList() {
		List<IdentityProviderEntity> identityProviderList = new ArrayList<IdentityProviderEntity>();
		try{
			identityProviderList = hibernateTemplate.find("from IdentityProviderEntity");
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return identityProviderList;
	}
}
