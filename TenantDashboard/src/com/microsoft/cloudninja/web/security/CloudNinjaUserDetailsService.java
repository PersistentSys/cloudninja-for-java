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
package com.microsoft.cloudninja.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.microsoft.cloudninja.dao.ManageUsersDao;
import com.microsoft.cloudninja.domainobject.Member;
import com.microsoft.cloudninja.exception.SystemException;

public class CloudNinjaUserDetailsService implements UserDetailsService {
	
	private static final Logger LOGGER = Logger.getLogger(CloudNinjaUserDetailsService.class);
    
    private static final String ROLE_PREFIX = "ROLE_";
	private ThreadLocal<User> currentUser = new ThreadLocal<User>();
	private ThreadLocal<String> currentPassword = new ThreadLocal<String>();
	@Autowired
	private ManageUsersDao manageUsersDao;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {

		Member member = null;
		User user = null;
		
		String[] userNameIdArr = username.split("!");
		if (userNameIdArr.length == 2) {
			String memberId = userNameIdArr[0];
			String tenantId = userNameIdArr[1];

			try {
				member = manageUsersDao.getMemberFromDb(tenantId, memberId);
			} catch (SystemException exception) {
				LOGGER.error(exception);
			}
			
			if (null != member) {
				// get user authorities
				String []roleArray = member.getRole().split(",");
				List <GrantedAuthority> userAuthorities = new ArrayList<GrantedAuthority>();
				for(int i = 0 ; i < roleArray.length; i++) {
					userAuthorities.add(new GrantedAuthorityImpl(ROLE_PREFIX + roleArray[i]));
				}	
				// Create user
				user = new User(username, member.getPassword(), true, true, true, true, userAuthorities);
				currentUser.set(user);
				
			} else {
				throw new UsernameNotFoundException("User id " + memberId + " not found!");
			}
		}
		return user;
	}

	public String createCookieValueFromUser(User user)
    {
	   Collection<GrantedAuthority> authorities=  user.getAuthorities();
	   int size = authorities.size();
        
       String role="";
       StringBuffer sb = new StringBuffer();
       int i = -1;
       
       for(GrantedAuthority grantedAuthority: authorities){
    	   role = grantedAuthority.getAuthority();
    	   i = i + 1;
    	   if (i == 0 & size > 1) {
    		   role = role + ",";
    	   }
    	   sb.append(role);
       }
       String newCookieValue = user.getUsername()+"!"+sb.toString();
       return newCookieValue;
    }
	
	public UserDetails loadUserByCookie(String cookieValue) throws UsernameNotFoundException, DataAccessException {

		String[] cookieStrings = cookieValue.split("!");
		List<GrantedAuthority> userAuthorities = new ArrayList<GrantedAuthority>();
		String[] roleArray = cookieStrings[2].split(",");
		for (String role : roleArray) {
			userAuthorities.add(new GrantedAuthorityImpl(role));
		}
		User user = new User(cookieStrings[0], "", true, true, true, true, userAuthorities);
		return user;
	}
	
	public User getCurrentUser() {
		return currentUser.get();
	}

	public void setCurrentUser(User user) {
		currentUser.set(user);
	}
	
    public void setPassword(String password)
    {
        currentPassword.set(password);
    }
    
}
