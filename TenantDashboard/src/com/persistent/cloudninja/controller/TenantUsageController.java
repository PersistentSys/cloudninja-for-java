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

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.persistent.cloudninja.domainobject.Metering;
import com.persistent.cloudninja.exception.SystemException;
import com.persistent.cloudninja.service.MeteringService;
import com.persistent.cloudninja.transferObjects.MeteringListDTO;
import com.persistent.cloudninja.web.security.CloudNinjaConstants;

/**
 * TenantUsageController manages to list tenant Usage from database.
 */
@Controller
public class TenantUsageController {

	@Autowired
	private MeteringService meteringService;


	//******************************************* Tenant Usage Info ****************************************************/
	/**
	 * List the tenant Metering information.
	 * @param cookie containing tenant ID
	 * @throws SystemException
	 * @return ModelAndView mapped to Tenant Metering List
	 */
	@RequestMapping("/showTenantMeteringPage.htm")
	public ModelAndView showTenantMeteringPage(
			HttpServletRequest request,
			@CookieValue(value = "CLOUDNINJAAUTH", required = false) String cookie,
			Model model) throws SystemException {
		if (cookie == null) {
			cookie = request.getAttribute("cookieNameAttr").toString();
		}
		String tenantId = AuthFilterUtils.getFieldValueFromCookieString(
			CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		
		List<Metering> list = meteringService.getMeteringService(tenantId, month, year);
		MeteringListDTO meteringDTO = new MeteringListDTO();
		meteringDTO.setMeteringList(list);

		return new ModelAndView("tenantMeteringPage","meteringDTO",meteringDTO);
	}
}
