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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class CNLogoutFilter extends LogoutFilter {

	public CNLogoutFilter(String logoutSuccessUrl, LogoutHandler[] handlers) {
		super(logoutSuccessUrl, handlers);
	}
	
    @Override
	protected boolean requiresLogout(HttpServletRequest request, HttpServletResponse response) {
		// Normal logout processing (i.e. detect logout URL)
		if (super.requiresLogout(request, response))
			return true;
		String cookieName = "CLOUDNINJAAUTH";
		Cookie reqCookie = getCookie(request, cookieName);
		if (reqCookie == null) {
			String url = request.getRequestURI().toString();
			if (url.contains("showTenantLoginPage") || !url.endsWith(".htm")) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
    /**
     *  Returns the cookie based on name otherwise null object
     * @param request
     * @param cookieName
     * @return Auth cookie
     */
    protected Cookie getCookie(HttpServletRequest request, String cookieName)
    {
        Cookie reqCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies)
                if (cookie.getName().equals(cookieName))
                {
                    reqCookie = cookie;
                    break;
                }
        return reqCookie;
    }    
}
