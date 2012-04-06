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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.microsoft.cloudninja.domainobject.Tenant;
import com.microsoft.cloudninja.scheduler.UserActivityQueue;
import com.microsoft.cloudninja.scheduler.UserActivityQueueMessage;

public class CNAuthenticationProcessingFilter extends	UsernamePasswordAuthenticationFilter {

    private CloudNinjaUserDetailsService userDetailsService;
    private String cookieName;
    
    @Value("#{'${storage.accName}'}")
	private String storageAccName;
	
	@Value("#{'${storage.tntsPrefix}'}")
	private String storageTntsPrefix;

    @Autowired
	private HibernateTemplate hibernateTemplate;	
    
    @Autowired
    private UserActivityQueue userActivityQueue;
    
    @Override
    protected String obtainPassword(HttpServletRequest request)
    {
        String password = super.obtainPassword(request);
        userDetailsService.setPassword(password);
        return password;
    }

    public String getCookieName()
    {
        return cookieName;
    }

    public void setCookieName(String cookieName)
    {
        this.cookieName = cookieName;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            Authentication authResult) throws ServletException, IOException
    {
        User user = userDetailsService.getCurrentUser();
        String currentCookie = getCookie(request);
        Cookie newCookie = createCookie(user, currentCookie);
        
        String [] cookievalArray = newCookie.getValue().split("!");
        // get the tenant id
        String tenantId = cookievalArray[1];
        
        synchronized (userActivityQueue) {
        	try {
        	Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			String date = dateFormat.format(calendar.getTime());
			
        	UserActivityQueueMessage message;
			
			message = new UserActivityQueueMessage(cookievalArray[1],
        			cookievalArray[0],
        			dateFormat.parse(date));
			
        	userActivityQueue.add(message);
        	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
        
       // create Cookie containing logo url
        Cookie logoCookie = createLogoCookie(tenantId);

        response.addCookie(newCookie);
        response.addCookie(logoCookie);
        super.successfulAuthentication(request, response, authResult);
    }
    
    /**
     * This method queries the database for the respective member and tries to get the logo url.<br/>
     * if found create the cookie with value as URL returns cookie with empty string
     * 
     * 
     * @param memberId
     * @return
     */
    private Cookie createLogoCookie(String tenantId) {
    	
    	String logoUrl="";
    	// DAO to get the URL
    	Tenant tenant = hibernateTemplate.get(Tenant.class, tenantId);
    	String logoFilename = tenant.getLogoFileName();
    	if(null == logoFilename || logoFilename.trim().length() == 0) {
    		// 
    		logoUrl = "";
    	} else {
    		// create logo URL from config property file
    		logoUrl = getUrlFromConfig(logoFilename, tenantId);
    	}
    	Cookie logoCokie = new Cookie("CLOUDNINJALOGO", logoUrl );
    	logoCokie.setMaxAge(-1);
    	logoCokie.setPath("/");
    	return logoCokie;
    }
    
    private String getUrlFromConfig(String logoFilename, String tenantId){
    	StringBuffer urlBuffer = new StringBuffer();
		
		urlBuffer.append("https://");
		urlBuffer.append(storageAccName);
		urlBuffer.append(storageTntsPrefix);
		urlBuffer.append(tenantId.toLowerCase());
		urlBuffer.append("/" + logoFilename);
        	
    	return urlBuffer.toString();
    }

    private String getCookie(HttpServletRequest request)
    {
        // Check whether the browser already has a SSO cookie
        String currentCookie = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (int i = 0; i < cookies.length; i++)
                if (cookieName.equals(cookies[i].getName()))
                {
                    currentCookie = cookies[i].getValue();
                    break;
                }
        return currentCookie;
    }

    private Cookie createCookie(User user, String currentCookie)
    {
        String newCookieValue = userDetailsService.createCookieValueFromUser(user);
        String cookieToUse = currentCookie;
        if (!currentCookie.equals(newCookieValue))
            cookieToUse = newCookieValue;
        Cookie cookie = new Cookie(cookieName, cookieToUse);
        cookie.setMaxAge(-1);
        cookie.setPath("/");
        return cookie;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws ServletException, IOException
    {
        userDetailsService.setCurrentUser(null);
        userDetailsService.setPassword(null);
        super.unsuccessfulAuthentication(request, response, failed);
    }

    public CloudNinjaUserDetailsService getUserDetailsService()
    {
        return userDetailsService;
    }

    public void setUserDetailsService(CloudNinjaUserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }

}
