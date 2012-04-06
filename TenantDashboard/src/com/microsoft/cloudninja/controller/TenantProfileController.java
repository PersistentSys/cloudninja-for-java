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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.microsoft.cloudninja.service.FileUploadService;
import com.microsoft.cloudninja.service.TenantProfileService;
import com.microsoft.cloudninja.transferObjects.LogoFileDTO;
import com.microsoft.cloudninja.validator.LogoFileDTOValidator;
import com.microsoft.cloudninja.web.security.CloudNinjaConstants;

@Controller
public class TenantProfileController {

	@Autowired
	private TenantProfileService tenantProfileService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private LogoFileDTOValidator logoFileDTOValidator;
	
/********************************* USER LIST  FLOW *************************************************************/
	@RequestMapping(value = "/deleteTenant.htm", method = RequestMethod.GET)
	public String deleteTenant(HttpServletRequest request,
			@CookieValue(value = "CLOUDNINJAAUTH", required = false) String cookie) {
		
		if (cookie == null) {
			cookie = request.getAttribute("cookieNameAttr").toString();
		}
		String tenantId = AuthFilterUtils.getFieldValueFromCookieString(
			CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);

		tenantProfileService.deleteTenant(tenantId);
		return "logoutSecurityPage";
	}
	
/******************************************** Tenant Profile ****************************************************/
	
	@RequestMapping(value="/showTenantProfilePage.htm", method=RequestMethod.GET)
	public ModelAndView showTenantProfilePage() {
		return new ModelAndView("tenantProfilePage");
	}
	
	@RequestMapping(value="/showTenantProfilePage.htm", method=RequestMethod.POST)
	public ModelAndView showProfilePage(
			HttpServletRequest request,
			HttpServletResponse response,
			@CookieValue(value = "CLOUDNINJAAUTH", required = false) String cookie,
			@ModelAttribute("logoFileDTO") LogoFileDTO logoFileDTO, BindingResult result) {
		// validate the file uploaded for logo
		logoFileDTOValidator.validate(logoFileDTO, result);
		// if no errors in validation then only process the request
		if (!result.hasErrors()) {
			if (cookie == null) {
				cookie = request.getAttribute("cookieNameAttr").toString();
			}
			String tenantId = AuthFilterUtils.getFieldValueFromCookieString(
				CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);
			
			String logoFileName = fileUploadService.fileUploadService(logoFileDTO.getFile(), tenantId);
			
			String logoCookieName = "CLOUDNINJALOGO";
	
			// update the logo cookie with the new logo file
			Cookie cookies [] = request.getCookies ();
			Cookie logoCookie = null;
			if (cookies != null)
			  {
			    for (int i = 0; i < cookies.length; i++) 
			    {
			       if (cookies[i].getName().equals (logoCookieName))
			         {
			    	   logoCookie = cookies[i];
			    	   logoCookie.setValue(logoFileName);
			    	   logoCookie.setMaxAge(-1);
			    	   logoCookie.setPath("/");
			           response.addCookie(logoCookie);
			           break;
			        }
			   }
			 }
		}
		
		return new ModelAndView("tenantProfilePage", "logoFileDTO", logoFileDTO);
	}
}
