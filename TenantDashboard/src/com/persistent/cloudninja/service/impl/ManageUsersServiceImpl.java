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
package com.persistent.cloudninja.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.persistent.cloudninja.dao.ManageUsersDao;
import com.persistent.cloudninja.domainobject.IdentityProviderEntity;
import com.persistent.cloudninja.domainobject.Member;
import com.persistent.cloudninja.exception.SystemException;
import com.persistent.cloudninja.service.ManageUsersService;

@Service("manageUsersService")
public class ManageUsersServiceImpl implements ManageUsersService {
	
	private static final Logger LOGGER = Logger.getLogger(ManageUsersServiceImpl.class);
	
	@Autowired
	private ManageUsersDao manageUsersDao;

	@Override
	public List<Member> getManageUsersList(String tenantId) throws SystemException{
		LOGGER.info("ManageUsersServiceImpl: getManageUsersList Start");
		List<Member> memberList = manageUsersDao.getManageUsersList(tenantId);
		LOGGER.info("ManageUsersServiceImpl: getManageUsersList End");
		return memberList;
		
	}

	@Override
	public void createUser(Member member) throws SystemException {
		LOGGER.info("ManageUsersServiceImpl: createUser Start");
		String sha1Password = getSHA1hexPassword(member.getPassword());
		member.setPassword(sha1Password);
		manageUsersDao.createUser(member);
		LOGGER.info("ManageUsersServiceImpl createUser End");
		
	}

	@Override
	public void deleteUser(Member member) throws SystemException{
		LOGGER.info("ManageUsersServiceImpl: deleteUser Start");
		manageUsersDao.deleteUser(member);
		LOGGER.info("ManageUsersServiceImpl: deleteUser End");
		
	}

	@Override
	public void updateUser(Member member) throws SystemException {
		LOGGER.info("ManageUsersServiceImpl: updateUser Start");
		manageUsersDao.updateUser(member);
		LOGGER.info("ManageUsersServiceImpl: updateUser End");
		
	}
	
	/**
	 * This method returns the SHA-1 encoded HexPassword
	 * 
	 * @param password
	 *            the plain text password
	 * @return hexpassword The SHA-1 encoded Hex password.
	 */
	private String getSHA1hexPassword(String password) {

		String hexPassword = "";

		if (password != null && password.trim().length() > 0) {

			try {
				MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
				sha1Digest.update(password.trim().getBytes());
				hexPassword = new String(Hex.encodeHex(sha1Digest.digest())).toUpperCase();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return hexPassword;
	}

	@Override
	public boolean findAndUpdateMember(String invitationCode, String liveGuid) {
		LOGGER.info("ManageUsersServiceImpl: findAndUpdateMember Start");
		return manageUsersDao.findAndUpdateMember(invitationCode,liveGuid);
	}
	
	
	@Override
	public List<IdentityProviderEntity> getIdentityProviderList() {
		List<IdentityProviderEntity> identityProviderList = manageUsersDao.getIdentityProviderList();
		return identityProviderList;
	}

}
