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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;


/**
 * A Spring Security service that provides user details based on cookie.
 * 
 * If a user who is not logged in tries to access a secure resource, Spring Security will use this service in an attempt
 * to authenticate the user before resorting to a login form.
 */
public class CloudNinjaRemembermeService extends AbstractRememberMeServices  {

	@Override
	protected void onLoginSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) {
	}

	@Override
	protected UserDetails processAutoLoginCookie(String[] tokens,
			HttpServletRequest request, HttpServletResponse response)
			throws RememberMeAuthenticationException, UsernameNotFoundException {

		CloudNinjaUserDetailsService userDetailsService = (CloudNinjaUserDetailsService) getUserDetailsService();
		UserDetails user = null;
		String cookieValue = getCookieValue(request, getCookieName());
		if (cookieValue != null)
			user = userDetailsService.loadUserByCookie(cookieValue);
		if (user != null) {
			return user;
		} else
			throw new UsernameNotFoundException(
					"Couldn't load user details via cookie: " + getCookieName());
	}
	
	/**
	 * Find the cookie in request and 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	
    protected String getCookieValue(HttpServletRequest request, String cookieName)
    {
        String cookieValue = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies)
                if (cookie.getName().equals(cookieName))
                {
                    cookieValue = cookie.getValue();
                    break;
                }
        return cookieValue;
    }
    
    /**
     * On logout, invalidate the cookie and send back in the response
     */
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    {
        String cookieName = getCookieName();
		Cookie cookieToInvalidate = getCoockieFromRequest(request, cookieName);
		if (null != cookieToInvalidate) {
			cookieToInvalidate.setMaxAge(0);
			cookieToInvalidate.setPath("/");
			response.addCookie(cookieToInvalidate);
		}
		cookieToInvalidate = getCoockieFromRequest(request, "CLOUDNINJALOGO");
		if (null != cookieToInvalidate) {
			cookieToInvalidate.setMaxAge(0);
			cookieToInvalidate.setPath("/");
			response.addCookie(cookieToInvalidate);
		}
    }
    
    private Cookie getCoockieFromRequest(HttpServletRequest request,
			String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(cookieName)) {
					return cookie;
				}
		}
		return null;
	}
    
    
}
