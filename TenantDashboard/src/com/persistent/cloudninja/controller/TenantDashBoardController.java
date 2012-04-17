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

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.persistent.cloudninja.web.security.CloudNinjaConstants;

/**
 * Tenant DashBoard Login Page.
 */
@Controller
public class TenantDashBoardController {
//***********Tenant Login
	/**
	 * Login Page
	 * @return tenantLoginPage View
	 */
	@RequestMapping("/showTenantLoginPage.htm")
	public String showLoginPage() {
		return "tenantLoginPage";
	}

	/**
	 * Access Denied Page
	 * @param cookie
	 * @return ModelAndView mapped to access denied
	 */
	@RequestMapping("{tenantId}/showAccessDenied.htm")
	public ModelAndView showAccessDenied(
		HttpServletRequest request,
		@CookieValue("CLOUDNINJAAUTH") String cookie, @PathVariable ("tenantId") String tenantId) {
	    if (cookie == null) {
		cookie = request.getAttribute("cookieNameAttr").toString();
	    }

	    String userName = AuthFilterUtils.getFieldValueFromCookieString(
		CloudNinjaConstants.COOKIE_USERNAME_PREFIX, cookie);
	    ModelAndView model = new ModelAndView("accessDenied","userName", userName );
		return model;
	}

}
