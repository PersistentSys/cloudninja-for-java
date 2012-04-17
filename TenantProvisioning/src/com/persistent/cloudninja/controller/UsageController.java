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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.persistent.cloudninja.domainobject.Filter;
import com.persistent.cloudninja.domainobject.Usage;
import com.persistent.cloudninja.service.UsageService;
import com.persistent.cloudninja.transferObjects.UsageDTO;

@Controller
public class UsageController {
	String[] monthName = {"January", "February",
			"March", "April", "May", "June", "July",
			"August", "September", "October", "November",
			"December"};
	
	@Autowired
	private UsageService usageService;
	
	
	/**
	 *  Get the Metering information for all Tenants for current month
	 * @param filter is filtering the whole Metering list with respect to the current moth or selected month by user
	 * @return ModelAndView mapped to Usage Page. 
	 */
	@RequestMapping("Metering.htm")
	public ModelAndView meteringPage(@ModelAttribute ("GetDetails")Filter filter,BindingResult result, HttpServletRequest request) {
		Integer month;
        Calendar cal = Calendar.getInstance();
        String year = "" + cal.get(Calendar.YEAR);
		if(filter.getMonth()==null)
		{
			String monthname = monthName[cal.get(Calendar.MONTH)];
			month=cal.get(Calendar.MONTH)+1;
			filter.setMonthName(monthname);	
			}
		else
		{
			 month=filter.getMonth();
	         year= request.getParameter("year").toString();
		}
		UsageDTO usageDTO = usageService.getMetering(month, year);
		return new ModelAndView("usagePage","usageDTO",usageDTO);
	}
	
	/**
	 *  Run metering tasks manually.
	 * @return ModelAndView mapped to Usage Page. 
	 */
	@RequestMapping("RunMeteringManually.htm")
	public ModelAndView runMeteringTasks() {
		usageService.runMeteringTasksManually();
		return new ModelAndView(new RedirectView("Metering.htm"));
	}
	
	/**
	 * Get the Metering information for specific Tenant 
	 * @param usage is the list containing Metering information to be displayed
	 * @return ModelAndView mapped to Tenant Usage Page.
	 */
	@RequestMapping(value="viewTenantMetering.htm", method = RequestMethod.POST)
	public ModelAndView viewTenantMetering(@ModelAttribute Usage usage,BindingResult result) {
		UsageDTO usageDTO = usageService.getTenantMetering(usage.getTenantId(),usage.getMonth(),usage.getYear());
		if(usageDTO!=null)
		{
		usageDTO.setTenant(usageDTO.getUsage().get(0).getTenantId());
		}
		return new ModelAndView("tenantUsagePage","usageDTO",usageDTO);
	}
}
