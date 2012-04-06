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
package com.microsoft.cloudninja.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.microsoft.cloudninja.controller.AuthFilterUtils;
import com.microsoft.cloudninja.web.security.CloudNinjaConstants;

/**
 * 
 * Class for checking Role BAsed Authorization
 */
public class RoleBasedAccessControlInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger.getLogger(RoleBasedAccessControlInterceptor.class);
	private static ResourceBundle rsBundle;

	private Map<String, String> userurls;
	private Map<String, String> adminurls;
	
	public Map<String, String> getUserurls() {
	    return Collections.unmodifiableMap(userurls);
	}

	public void setUserurls(Map<String, String> userurls) {
	    this.userurls = userurls;
	}

	public Map<String, String> getAdminurls() {
	    return Collections.unmodifiableMap(adminurls);
	}

	public void setAdminurls(Map<String, String> adminurls) {
	    this.adminurls = adminurls;
	}


	static {
		BasicConfigurator.configure();
	}

    @Override
    public boolean preHandle(HttpServletRequest request,
	    HttpServletResponse response, Object handler) throws Exception {
	logger.info("Before handling the request");

	boolean isRequestURLAccessAllowed = false;
	String cookieName = CloudNinjaConstants.AUTH_COOKIE_NAME;
	String userRole = "ROLE_USER";
	String tenantId = "dummyTenantID";
	String requestURI = "";
	String path = "";

	Cookie reqCookie = getCookie(request, cookieName);

	if (reqCookie != null) {
	    tenantId = AuthFilterUtils.getFieldValueFromCookie(reqCookie,
		    CloudNinjaConstants.COOKIE_TENANTID_PREFIX);
	    userRole = AuthFilterUtils.getRoleFromCookie(reqCookie,
		    CloudNinjaConstants.COOKIE_AUTHORITIES_PREFIX).trim();
	    requestURI = request.getRequestURI();
	
	}
	if ("ROLE_USER".equalsIgnoreCase(userRole)) {
	    path = getRequestURIMappingPath(requestURI, tenantId);
	    if (userurls.containsValue(path)) {
	    	isRequestURLAccessAllowed = true;
	    } 
	}else if ("ROLE_ADMINISTRATOR".equalsIgnoreCase(userRole)) {
	    path = getRequestURIMappingPath(requestURI, tenantId);
	    if (adminurls.containsValue(path)) {
	    	isRequestURLAccessAllowed = true;
	    } 
	}
	if (isRequestURLAccessAllowed) {
	    return super.preHandle(request, response, handler);
	} else {
	    try {
	    	rsBundle = ResourceBundle.getBundle("acs");
	    	String accessDeniedPage = rsBundle.getString("tenant.dashboard.accessdenied.page.url").trim();
		String redirectUrl = request.getContextPath().concat(accessDeniedPage);
		response.sendRedirect(redirectUrl);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    isRequestURLAccessAllowed = true;
	    return false;
	}

    }

	private String getRequestURIMappingPath(String requestURI, String tenantId) {
		StringTokenizer tokenizer = new StringTokenizer(requestURI, "/");
		String token ="";
		String forwardSlash="/";
		while (tokenizer.hasMoreTokens()){
		    token = tokenizer.nextToken();
		    token=forwardSlash+token; 
		    if(token.endsWith(".htm")){
			break;			
		    }
		}
		return token;
	
    }

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.info("After handling the request");
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.info("After rendering the view");
		super.afterCompletion(request, response, handler, ex);
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
