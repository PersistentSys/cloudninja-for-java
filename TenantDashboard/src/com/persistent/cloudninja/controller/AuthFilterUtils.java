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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.persistent.cloudninja.dao.ManageUsersDao;
import com.persistent.cloudninja.dao.TenantDao;
import com.persistent.cloudninja.domainobject.Member;
import com.persistent.cloudninja.domainobject.Tenant;
import com.persistent.cloudninja.exception.SystemException;
import com.persistent.cloudninja.web.security.CloudNinjaConstants;
import com.persistent.cloudninja.web.security.CloudNinjaUser;

/**
 * Utility class for authentication.
 */
@Component
public class AuthFilterUtils {
	
	@Autowired
	private static TenantDao tenantDao;
	private static ManageUsersDao manageUsersDao;
	
	@Autowired(required=true)
	public void setTenantDao(TenantDao tenantDao) {
		AuthFilterUtils.tenantDao = tenantDao;
	}
	
	@Autowired(required=true)
	public void setManageUsersDao(ManageUsersDao manageUsersDao) {
		AuthFilterUtils.manageUsersDao = manageUsersDao;
	}
	
    private AuthFilterUtils() {
    }

    /**
     * 
     * @param HttpServletRequest
     *            object
     * @return cookie, if present, null otherwise.
     */
    public static Cookie checkForPreExistentCookie(HttpServletRequest request,
	    String cookieName) {
	// Check whether the browser already has a SSO cookie
	Cookie[] cookies = request.getCookies();
	Cookie currentCookie = null;

	if (cookies != null && cookies.length > 0) {
	    for (Cookie c : cookies) {
		if (cookieName.equals(c.getName())) {
		    currentCookie = c;
		    break;
		}
	    }
	}
	return currentCookie;
    }

    /**
     * 
     * @param cookie
     * @return
     */
    public static boolean checkValidityOfCookie(Cookie cookie) {
	return isACSSessionValidNow(cookie);
    }

    /**
     * @param cookie
     * @return
     */
    private static boolean isACSSessionValidNow(Cookie cookie) {
	String authSessionStartTimeStr = getFieldValueFromCookie(cookie,
		CloudNinjaConstants.COOKIE_AUTH_SESSION_START_PREFIX);
	String authSessionEndTimeStr = getFieldValueFromCookie(cookie,
		CloudNinjaConstants.COOKIE_AUTH_SESSION_END_PREFIX);

	Long currentTime = new Date().getTime();
	boolean retVal = false;
	if(authSessionStartTimeStr==null || authSessionEndTimeStr==null ){
	    return false;
	}
	if (Long.parseLong(authSessionEndTimeStr) > currentTime.longValue()) {
	    retVal = true;
	} 
	return retVal;
    }

    /**
     * 
     * @param cookie
     * @param fieldName
     * @return
     */
    public static String getFieldValueFromCookie(Cookie cookie, String fieldName) {
	if (cookie == null || fieldName == null) {
	}

	String cookieValue = cookie.getValue();
	return getFieldValueFromCookieString(fieldName, cookieValue);
    }

    /**
     * 
     * @param fieldName
     * @param cookieValue
     * @return
     */
    public static String getFieldValueFromCookieString(String fieldName,
	    String cookieValue) {
	String retVal = null;
	String fieldNameAndSeparatorString = fieldName
		+ CloudNinjaConstants.COOKIE_FIELD_AND_VALUE_SEPARATOR;
	StringTokenizer tokenizer = new StringTokenizer(cookieValue,
		CloudNinjaConstants.COOKIE_FIELDS_SEPARATOR);
	while (tokenizer.hasMoreTokens()) {
	    String nextToken = tokenizer.nextToken();
	    if (nextToken.startsWith(fieldNameAndSeparatorString)) {
		retVal = nextToken.substring(fieldNameAndSeparatorString
			.length());
	    }
	}
	return retVal;
    }

    
    public static String getRoleFromCookie(Cookie cookie, String authotitiesPrefix) {
    	String cookieValue = cookie.getValue();
    	String roleArray = getFieldValueFromCookieString(authotitiesPrefix, cookieValue);
    	String role = roleArray.substring(1, roleArray.length()-1);
    	return role;
        }

    /**
     * @param sessionExpiresAt
     * @return
     */
    public static Date convertISOTimeStringToDate(String timeString) {
	Calendar calendar = DatatypeConverter.parseDateTime(timeString);
	return calendar.getTime();
    }

    /**
     * 
     * @param cloudNinjaUser
     * @param cookieName
     * @return
     */
    public static Cookie createNewCookieForACSAuthenticatedUser(
	    CloudNinjaUser cloudNinjaUser, String cookieName) {
	Collection<GrantedAuthority> authorities = cloudNinjaUser.getUser()
		.getAuthorities();
	if (authorities != null) {
	    GrantedAuthority[] grantedAuthorities = new GrantedAuthority[authorities
		    .size()];
	    authorities.toArray(grantedAuthorities);
	}

	StringBuffer sb = new StringBuffer(5);

	sb.append(CloudNinjaConstants.COOKIE_TENANTID_PREFIX)
		.append(CloudNinjaConstants.COOKIE_FIELD_AND_VALUE_SEPARATOR)
		.append(cloudNinjaUser.getTenantId())
		.append(CloudNinjaConstants.COOKIE_FIELDS_SEPARATOR);

	sb.append(CloudNinjaConstants.COOKIE_USERNAME_PREFIX)
		.append(CloudNinjaConstants.COOKIE_FIELD_AND_VALUE_SEPARATOR)
		.append(cloudNinjaUser.getUser().getUsername())
		.append(CloudNinjaConstants.COOKIE_FIELDS_SEPARATOR);

	sb.append(CloudNinjaConstants.COOKIE_AUTHORITIES_PREFIX)
		.append(CloudNinjaConstants.COOKIE_FIELD_AND_VALUE_SEPARATOR)
		.append(authorities.toString())
		.append(CloudNinjaConstants.COOKIE_FIELDS_SEPARATOR);

	sb.append(CloudNinjaConstants.COOKIE_AUTH_SESSION_START_PREFIX)
		.append(CloudNinjaConstants.COOKIE_FIELD_AND_VALUE_SEPARATOR)
		.append(cloudNinjaUser.getAuthenticatedSessionStartTime()
			.getTime())
		.append(CloudNinjaConstants.COOKIE_FIELDS_SEPARATOR);

	sb.append(CloudNinjaConstants.COOKIE_AUTH_SESSION_END_PREFIX)
		.append(CloudNinjaConstants.COOKIE_FIELD_AND_VALUE_SEPARATOR)
		.append(cloudNinjaUser.getAuthenticatedSessionExpiryTime()
			.getTime())
		.append(CloudNinjaConstants.COOKIE_FIELDS_SEPARATOR);

	String newCookieValue = sb.toString();

	Cookie newCookie = new Cookie(cookieName, newCookieValue);
	newCookie.setPath("/");
	return newCookie;
    }

    /**
     * 
     * @param preExistentCookie
     *            , cloudNinjaUser
     */
    public static Cookie updateExistingCookie(Cookie preExistentCookie,
	    CloudNinjaUser cloudNinjaUser) {
	String cookieValue = createNewCookieForACSAuthenticatedUser(
		cloudNinjaUser, preExistentCookie.getName()).getValue();
	preExistentCookie.setValue(cookieValue);
	return preExistentCookie;
    }

    /**
     * Creates a User object using details in map. This is for a custom STS provider. 
     * 
     * @param Map
     *            acsUserDetailsMap
     * @return user
     */
    public static User createUserFromACSUserDetailsMap(
	    Map<String, String> acsUserDetailsMap) {
	String userName = (acsUserDetailsMap.get("givenname") == null) ? CloudNinjaConstants.DUMMY_USER_ID
		: acsUserDetailsMap.get("givenname");

	String role = (acsUserDetailsMap.get("role") == null) ? "USER"
		: acsUserDetailsMap.get("role");

	String userPassword = "";
	List<GrantedAuthority> userAuthorities = new ArrayList<GrantedAuthority>();
	userAuthorities.add(new GrantedAuthorityImpl(
		CloudNinjaConstants.ROLE_PREFIX + role));

	return new User(userName, userPassword, true, true, true, true,
		userAuthorities);
    }
    
    /**
     * Creates a User object using tenantId and email address.
     * This is for identity provider which returns email address. 
     * 
     * @param tenantId
     * @param emailAddress
     * @return
     * @throws SystemException
     */
    public static User createUserForIPUsingEmail(
    	    String tenantId, String emailAddress, String friendlyName) throws SystemException {
    	User user = null;
    	Member member = manageUsersDao.getMemberFromDb(tenantId, emailAddress);
    	if (null != member) {
	    	String role = member.getRole();
	    	String userName = friendlyName;
	
	    	String userPassword = "";
	    	List<GrantedAuthority> userAuthorities = new ArrayList<GrantedAuthority>();
	    	userAuthorities.add(new GrantedAuthorityImpl(
	    		CloudNinjaConstants.ROLE_PREFIX + role));
	    	user = new User(userName, userPassword, true, true, true, true,
	        		userAuthorities);
    	}
    	return user; 
    }
    
    /**
     * Creates a User object using tenantId and GUID.
     * This is for identity provider which returns a GUID. 
     * 
     * @param tenantId
     * @param GUID
     * @return
     * @throws SystemException
     */
    public static User createUserForIPUsingGUID(
    	    String tenantId, String GUID) throws SystemException {
    	User user = null;
    	Member member = manageUsersDao.getMemberFromDbUsingGUID(tenantId, GUID);
    	if (null != member) {
	    	String role = member.getRole();
	    	String userName = member.getMemberCompoundKey().getMemberId();
	
	    	String userPassword = "";
	    	List<GrantedAuthority> userAuthorities = new ArrayList<GrantedAuthority>();
	    	userAuthorities.add(new GrantedAuthorityImpl(
	    		CloudNinjaConstants.ROLE_PREFIX + role));
	    	user = new User(userName, userPassword, true, true, true, true,
	        		userAuthorities);
    	}
    	return user; 
    }
    
    /**
     * Checks the validity of certificate retrieved from given token
     * @param acsToken the ACS token
     * @return true if certificate is valid otherwise false.
     */
    public static boolean checkCertificateValidity(String acsToken) {
    	Map<String,String> certificateDataMap = new HashMap<String, String>();
    	if( (acsToken == null) || (acsToken.length() == 0 ) )
			return false;
			
		try {
			certificateDataMap = getCertificateThumbPrintAndIssuerName(acsToken);
			if(certificateDataMap == null) {
				return false;
			}
            String issuerName = certificateDataMap.get("IssuerName");
            String certThumbprint = certificateDataMap.get("Thumbprint");
            
        	//Getting tenant
        	Tenant tenantEntity = tenantDao.getTenantEntityUsingCertificate(certThumbprint);
        	if(tenantEntity == null)
        	{
        		return false;
        	}
        	String tenantId = tenantEntity.getTenantId();
        	if((issuerName.equals("tnt_" + tenantId))) {
        		return true;
        	}
        	else {
        		return false;
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
        
    /**
     * Get Certificate thumb print and Issuer Name from the ACS token.
     * @param acsToken the acs token
     * @return returnData the Map containing Thumb print and issuer name of X509Certiificate
     * @throws NoSuchAlgorithmException
     * @throws CertificateEncodingException
     */
	public static Map<String,String> getCertificateThumbPrintAndIssuerName(String acsToken)  
	throws NoSuchAlgorithmException, CertificateEncodingException {
		byte[] acsTokenByteArray = null ;
		Map <String,String> returnData = new HashMap<String, String>();
		
		try {
			acsTokenByteArray = acsToken.getBytes("UTF-8");
		}catch (UnsupportedEncodingException e) {
			return null;
		}
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setNamespaceAware(true);
		DocumentBuilder docBuilder;
		String issuerName = null;
		StringBuffer thumbprint = null;
		
		try {
			docBuilder = builderFactory.newDocumentBuilder();			
			Document resultDoc = docBuilder.parse(new ByteArrayInputStream(acsTokenByteArray));		 
			Element keyInfo = (Element) resultDoc.getDocumentElement().getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "KeyInfo").item(0);
			
			NodeList x509CertNodeList = keyInfo.getElementsByTagName("X509Certificate");
			Element x509CertNode = (Element) x509CertNodeList.item(0); 
			if(x509CertNode == null)	{
				return null;
			}
			//generating Certificate to retrieve its detail.
			String x509CertificateData = x509CertNode.getTextContent();
			InputStream inStream = new Base64InputStream(new ByteArrayInputStream(x509CertificateData.getBytes()));
	        CertificateFactory x509CertificateFactory = CertificateFactory.getInstance("X.509"); 
	        X509Certificate x509Certificate = (X509Certificate)x509CertificateFactory.generateCertificate(inStream); 
	        String issuerDN = x509Certificate.getIssuerDN().toString();
	        String [] issuerDNData = issuerDN.split("=");
	        issuerName = issuerDNData[1];	
			
			
			
		    MessageDigest md = MessageDigest.getInstance("SHA-1"); 
		    byte[] der = x509Certificate.getEncoded(); 
		    md.update(der); 
		    thumbprint = new StringBuffer();
		    thumbprint.append(Hex.encodeHex(md.digest()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	    returnData.put("IssuerName", issuerName);
	    returnData.put("Thumbprint", thumbprint.toString().toUpperCase());
	    return returnData;
    }
}
