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
package com.persistent.cloudninja.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.persistent.cloudninja.web.security.CloudNinjaConstants;

@Component
public class LogoutFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
	    FilterChain chain) throws IOException, ServletException {
	HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	HttpServletResponse httpServletResponse = (HttpServletResponse) response;
	
	removeCookie(httpServletRequest, httpServletResponse, CloudNinjaConstants.AUTH_COOKIE_NAME);
	
	//request.getRequestDispatcher("/logoutsuccess").forward(httpServletRequest, httpServletResponse);
	chain.doFilter(httpServletRequest, httpServletResponse);

    }

    private void removeCookie(HttpServletRequest httpServletRequest,
	    HttpServletResponse httpServletResponse, String authCookieName) {
    	
	Cookie[] cookies = httpServletRequest.getCookies();
	Cookie currentCookie = null;

	if (cookies != null && cookies.length > 0) {
	    for (Cookie c : cookies) {
		if (authCookieName.equals(c.getName())) {
		    currentCookie = c;
			currentCookie.setMaxAge(0);
			currentCookie.setValue("");
			currentCookie.setPath("/");
			
			httpServletResponse.addCookie(currentCookie);
		}
		
	    }
	}

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
