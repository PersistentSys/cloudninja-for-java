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
package com.microsoft.cloudninja.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.microsoft.cloudninja.dao.TenantDao;
import com.microsoft.cloudninja.domainobject.Tenant;
import com.microsoft.cloudninja.exception.SystemException;
import com.microsoft.cloudninja.scheduler.UserActivityQueue;
import com.microsoft.cloudninja.scheduler.UserActivityQueueMessage;
import com.microsoft.cloudninja.utils.SAMLParser;
import com.microsoft.cloudninja.web.security.CloudNinjaConstants;
import com.microsoft.cloudninja.web.security.CloudNinjaUser;

@Component
public class CloudNinjaAuthFilter implements Filter {

	private static final Logger logger = Logger.getLogger(CloudNinjaAuthFilter.class);
	private static ResourceBundle rsBundle;
	
	@Autowired
	private static TenantDao tenantDao;
	
	@Autowired(required=true)
	public void setTenantDao(TenantDao tenantDao) {
		CloudNinjaAuthFilter.tenantDao = tenantDao;
	}
	
	/**
	 * This method filters every incoming request.
	 * If request contains cookie, it checks whether the cookie is valid.
	 * 	A. If request cookie is present and is valid, forwards the request 
	 * 			to next page.
	 * 	B. If cookie is not valid and request is not coming from ACS, this
	 * 			method redirects the request to ACS login page.
	 * If request does not contain a cookie, but contains an ACS token,
	 * this method, creates or updates cookie and 
	 * forwards the request to landing page.
	 */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
	    FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		// capture ACS response
		String acsToken = httpServletRequest.getParameter("wresult");
		if (null != acsToken && acsToken.trim().length() == 0) {
			acsToken = null;
		}
		String isEncodedWresult = httpServletRequest.getParameter("isEncodedWresult");
		String decodedTokenString = null;
		if (null != acsToken && null != isEncodedWresult
				&& isEncodedWresult.trim().equalsIgnoreCase("true")) {
			decodedTokenString = new String (URLDecoder.decode(acsToken, "UTF-8"));
			acsToken = decodedTokenString;
		}
		
		// by pass the url access validation validateInvitationCode
		if (httpServletRequest.getRequestURI().contains("/validateInvitationCode")) {
			request.getRequestDispatcher("/validateInvitationCode.htm").forward(httpServletRequest, httpServletResponse);
		} else {
		
			CloudNinjaUser cloudNinjaUser = null;
		
			boolean isValidCookiePresent = false;
			String cookieName = CloudNinjaConstants.AUTH_COOKIE_NAME;
		
			Cookie preExistentCookie = AuthFilterUtils.checkForPreExistentCookie(
				httpServletRequest, cookieName);
		
			if (preExistentCookie != null &&  StringUtils.isNotBlank(preExistentCookie.getValue())) {
			    isValidCookiePresent = AuthFilterUtils
				    .checkValidityOfCookie(preExistentCookie);
			}
		
			if (isValidCookiePresent) {
			    Cookie cookieToUse = AuthFilterUtils.checkForPreExistentCookie(
				    httpServletRequest, cookieName);
			    cookieToUse.setPath("/");
			    httpServletResponse.addCookie(cookieToUse);
		
			    // Add cookie userNames, etc to request attributes
			    httpServletRequest.setAttribute("cookieNameAttr",
				    cookieToUse.getValue());
		
			    forwardToNextPage(httpServletRequest, httpServletResponse, chain);
			} else if (!isValidCookiePresent && (acsToken == null)) {
			    redirectToACSPage(httpServletRequest, httpServletResponse);
			    return;
			} else if (acsToken != null) {
			    
			    acsToken = new String(acsToken.getBytes(), CloudNinjaConstants.UTF_8_FORMAT);
	    boolean isValidCertificate = AuthFilterUtils.checkCertificateValidity(acsToken); 
				if(!isValidCertificate) {
					redirectToACSPage(httpServletRequest,httpServletResponse);
					return ;
				}
			    
			    try {
					cloudNinjaUser = parseSAMLResponseAndCreateCNUser(acsToken);
				} catch (CertificateEncodingException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			    String liveGuid=null;
			    
			    //  GUID is present and user is null it means that user is from windowsLiveId
			    // and is login-in in for the first time so we need to ask for verification code
			    if(cloudNinjaUser != null && cloudNinjaUser.getUser() == null) {
			       liveGuid = cloudNinjaUser.getLiveGUID();
			       cloudNinjaUser = null;
			       forwardToVerificationPage(httpServletRequest, httpServletResponse, liveGuid, acsToken);
			       return;
			    }
			    // if user is null and no GUID is present
			    // redirect to ACS page
			    
			    if (null == cloudNinjaUser) {
			    	redirectToACSPage(httpServletRequest, httpServletResponse);
				    return;
			    }
		
			    Cookie cookieToUse;
			    if (preExistentCookie == null) {
				cookieToUse = AuthFilterUtils
					.createNewCookieForACSAuthenticatedUser(cloudNinjaUser,
						cookieName);
			    } else {
				cookieToUse = AuthFilterUtils.updateExistingCookie(
					preExistentCookie, cloudNinjaUser);
			    }
			    cookieToUse.setMaxAge(getCookieMaxAge());
			    cookieToUse.setPath("/");
			    httpServletResponse.addCookie(cookieToUse);
			    httpServletRequest.setAttribute("cookieNameAttr",
				     cookieToUse.getValue());
		
			    forwardToLandingPage(httpServletRequest, httpServletResponse, chain,
			    		cloudNinjaUser);
			}
		}
    }

    private int getCookieMaxAge() {
    	int cookieMaxAge =0;
    	try{
    		cookieMaxAge = Integer.parseInt(rsBundle.getString("browser.cookie.max.age.in.seconds").trim());
    	}catch(Exception exception){
    		logger.error(exception.getMessage());
    		exception.printStackTrace();
    	}
    	return cookieMaxAge;
	}

	private void forwardToLandingPage(HttpServletRequest httpServletRequest,
	    HttpServletResponse httpServletResponse, FilterChain chain, CloudNinjaUser cloudNinjaUser) {
		UserActivityQueue userActivityQueue =
			UserActivityQueue.getUserActivityQueue();
		
		synchronized (userActivityQueue) {
	    	try {
	    	Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			String date = dateFormat.format(calendar.getTime());
			
	    	UserActivityQueueMessage message;
	    	String cookie = httpServletRequest.getAttribute("cookieNameAttr").toString();
	    	String tenantId = AuthFilterUtils.getFieldValueFromCookieString(
	    			CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);
			
			message = new UserActivityQueueMessage(tenantId,
	    			cloudNinjaUser.getMemberId(),
	    			dateFormat.parse(date));
			
	    	userActivityQueue.add(message);
	    	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		try {
			rsBundle  = ResourceBundle.getBundle("acs");
			String landingPageURL = rsBundle.getString("tenant.dashboard.landing.page.url").trim();
		    httpServletRequest.getRequestDispatcher(landingPageURL)
			    .forward(httpServletRequest, httpServletResponse);
		} catch (ServletException exception) {
			logger.error(exception.getMessage());
			exception.printStackTrace();
		} catch (IOException exception) {
			logger.error(exception.getMessage());
			exception.printStackTrace();
		}

    }

    /**
     * Parses the ACS response.
     * @param acsToken
     * @return CloudNinjaUser
     * @throws NoSuchAlgorithmException 
     * @throws CertificateEncodingException 
     */
    private CloudNinjaUser parseSAMLResponseAndCreateCNUser(String acsToken) throws CertificateEncodingException, NoSuchAlgorithmException {
		CloudNinjaUser cloudNinjaUser = null;
		// Parse the SAML response received from ACS.
		SAMLParser samlParser = new SAMLParser();
		Map<String, String> userDetailsMap = samlParser.parse(acsToken);
		Map<String,String> certificateDataMap = null;
		String certThumbprint = null;
		Tenant tenantEntity = new Tenant();
	
		// Create userDetails object from userDetailsMap
		if (userDetailsMap != null) {
			User acsAuthenticatedUser = null;
			CloudNinjaUser userForLiveIdHavingGuid= null;
			String tenantId = null;
			String memberId = null;
			String identityProvider = userDetailsMap.get("identityprovider");
			
			if (identityProvider.contains("WindowsLiveID")) {
				// this scenario is for WindowsLiveID
				String GUID = userDetailsMap.get("nameID");
				memberId = GUID;
				//fetch tenantId from certificate
				certificateDataMap = AuthFilterUtils.getCertificateThumbPrintAndIssuerName(acsToken);
				certThumbprint = certificateDataMap.get("Thumbprint");
				tenantEntity = tenantDao.getTenantEntityUsingCertificate(certThumbprint);
				tenantId = tenantEntity.getTenantId();

				try {
					acsAuthenticatedUser = 
						AuthFilterUtils.createUserForIPUsingGUID(tenantId, GUID);
					if(null == acsAuthenticatedUser) {
						userForLiveIdHavingGuid = new CloudNinjaUser();
						userForLiveIdHavingGuid.setLiveGUID(GUID);
					}
				} catch (SystemException e) {
					e.printStackTrace();
				}
			} else if (!userDetailsMap.containsKey("role") 
					&& !userDetailsMap.containsKey(CloudNinjaConstants.COOKIE_TENANTID_PREFIX)) {
				// this scenario is for Identity providers returning
				// emailaddress in token such as Yahoo, Google.
				String emailAddress = userDetailsMap.get("emailaddress");
				String name = userDetailsMap.get("name");
				//fetch tenantId from certificate
				certificateDataMap = AuthFilterUtils.getCertificateThumbPrintAndIssuerName(acsToken);
	            certThumbprint = certificateDataMap.get("Thumbprint");
				tenantEntity = tenantDao.getTenantEntityUsingCertificate(certThumbprint);
				tenantId = tenantEntity.getTenantId();
				memberId = emailAddress;
				try {
					acsAuthenticatedUser = 
						AuthFilterUtils.createUserForIPUsingEmail(tenantId, emailAddress,name);
				} catch (SystemException e) {
					e.printStackTrace();
				}
			} else {
				// this scenario is for Custom STS.
			    acsAuthenticatedUser = AuthFilterUtils
				    .createUserFromACSUserDetailsMap(userDetailsMap);
			    String tenanatId = userDetailsMap
				    .get(CloudNinjaConstants.COOKIE_TENANTID_PREFIX);
			    tenantId = (tenanatId == null) ? CloudNinjaConstants.DUMMY_TENANT_ID : tenanatId;
			    memberId = userDetailsMap.get("nameID");
			}
	
		    Date logonTime = AuthFilterUtils
			    .convertISOTimeStringToDate(userDetailsMap.get("logonTime"));
		    Date authSessionExpiresAt = AuthFilterUtils
			    .convertISOTimeStringToDate(userDetailsMap
				    .get("sessionExpiresAt"));
		    
		    if (null != acsAuthenticatedUser) {
			    cloudNinjaUser = new CloudNinjaUser();
			    cloudNinjaUser.setUser(acsAuthenticatedUser);
			    cloudNinjaUser.setTenantId(tenantId);
			    cloudNinjaUser.setMemberId(memberId);
			    cloudNinjaUser.setAuthenticatedSessionStartTime(logonTime);
			    cloudNinjaUser
				    .setAuthenticatedSessionExpiryTime(authSessionExpiresAt);
		    }
		    
		    // Check if request came for LIVEID and user with GUID not found
		    // if yes then return user having only LIVE id attribute set
		    if (acsAuthenticatedUser == null && null != userForLiveIdHavingGuid) {
		    	cloudNinjaUser = userForLiveIdHavingGuid;
		    }
		}
	    return cloudNinjaUser;
    }

    private void redirectToACSPage(HttpServletRequest httpServletRequest,
	    HttpServletResponse httpServletResponse) throws IOException {
    	
    	rsBundle  = ResourceBundle.getBundle("acs");
    	String acsIDPUrl = rsBundle.getString("acs.idp.url");
    	String wtRealm = httpServletRequest.getRequestURL().toString();
    	int index = 0;
    	int count = 0;
    	//get the URL up to application context
    	while (count < 4) {
    		index = wtRealm.indexOf("/", index + 1);
    		count++;
    	}
    	wtRealm = wtRealm.substring(0, index);
    	String waValue = rsBundle.getString("wa.acs");
    	
    	String acsURL = new StringBuilder().append(acsIDPUrl).append("?wa=").append(waValue).append("&wtrealm=").append(wtRealm).toString();
    	httpServletResponse.sendRedirect(acsURL);
    }

    private void forwardToNextPage(ServletRequest request,
	    ServletResponse response, FilterChain filterChain) {
	try {
	    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	    HttpServletResponse httpServletResponse= (HttpServletResponse) response;
	    if(httpServletRequest.getRequestURI().contains("/logout")){
	    	removeCookie(httpServletRequest, httpServletResponse,  CloudNinjaConstants.AUTH_COOKIE_NAME);
	    	request.getRequestDispatcher("/logout.htm").forward(httpServletRequest, httpServletResponse);
	    }else{
		filterChain.doFilter(request, response);
	    }
	} catch (ServletException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }
    
	public void forwardToVerificationPage(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String guid,
			String acsToken) {

	    	try {
	    		httpServletRequest.setAttribute("guid", guid);
	    		httpServletRequest.setAttribute("acsToken", acsToken);
				httpServletRequest.getRequestDispatcher("/codeVerification.htm").forward(httpServletRequest, httpServletResponse);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    }


    private void removeCookie(HttpServletRequest httpServletRequest,
	    HttpServletResponse httpServletResponse, String authCookieName) {

//	Cookie cookieToBeRemoved = AuthFilterUtils.checkForPreExistentCookie(httpServletRequest, authCookieName);
	Cookie cookie = new Cookie(authCookieName, null);
	  cookie.setMaxAge(0);
	  cookie.setPath("/");
	  httpServletResponse.addCookie(cookie);
    }

    @Override
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {

	}

    @Override
    public void destroy() {

    }

}
