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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.persistent.cloudninja.domainobject.IdentityProviderEntity;
import com.persistent.cloudninja.scheduler.StorageUtility;
import com.persistent.cloudninja.service.ProvisioningService;
import com.persistent.cloudninja.transferObjects.ProvisioningTenantDTO;
import com.persistent.cloudninja.utils.SchedulerSettings;
import com.persistent.cloudninja.validator.ProvisioningTenantDTOValidator;

@Controller
public class ProvisionController {
	
	@Autowired
	private ProvisioningTenantDTOValidator provisioningTenantDTOValidator;
	
	@Autowired
	private StorageUtility storageUtility;
	
	@Autowired
	private ProvisioningService provisionService;

	@RequestMapping("/homePage.htm")
	public String showHomePage() {
		return "homePage";
	}
	
	@RequestMapping("/showLoginPage.htm")
	public ModelAndView showLoginPage(@ModelAttribute("provisioningTenantDTO") ProvisioningTenantDTO provisioningTenantDTO,
			BindingResult result) {
		List<IdentityProviderEntity> providerList = new ArrayList<IdentityProviderEntity>();		
		providerList = provisionService.getIdentityProviderList();
		
		provisioningTenantDTO.setIdentityProviderList(providerList);
		
		return new ModelAndView("loginPage", "provisioningTenantDTO", provisioningTenantDTO);
	}

	@RequestMapping(value = "/provisionTenant.htm", method = RequestMethod.POST)
	public ModelAndView addTenant(
			@ModelAttribute("provisioningTenantDTO") ProvisioningTenantDTO provisioningTenantDTO,
			BindingResult result) {
		// In case of errors the provisioningTenantDTO should have this list
		provisioningTenantDTO.setIdentityProviderList(provisionService.getIdentityProviderList());
		provisioningTenantDTOValidator.validate(provisioningTenantDTO, result);
		if (!result.hasErrors()) {
		    String tenantId = provisioningTenantDTO.getTenantId();
			StringBuffer stringBuffer = new StringBuffer(tenantId);
			stringBuffer.append(",");
			stringBuffer.append(provisioningTenantDTO.getCompanyName());
			stringBuffer.append(",");
			stringBuffer.append(provisioningTenantDTO.getMemberId());
			stringBuffer.append(",");
			stringBuffer.append(provisioningTenantDTO.getMemberName());
			stringBuffer.append(",");
			stringBuffer.append(provisioningTenantDTO.getMemberPassword());
			stringBuffer.append(",");
			stringBuffer.append(provisioningTenantDTO.getInvitationCode());
			
			try {
				//put the message in TenantCreationQueue to provision the tenant.
				storageUtility.putMessage(SchedulerSettings.TenantCreationQueue, stringBuffer.toString());
                // Redirect to tenant provisioning status page this page will periodically check the status
				return new ModelAndView( new RedirectView("ProvisioningStatus.htm?tenantId="+tenantId));
			} catch (Exception e) {
				provisioningTenantDTO.setSuccessMessage("Error occurred while registering.");
				e.printStackTrace();
			}
			
		}
		
	    return new ModelAndView("loginPage", "provisioningTenantDTO", provisioningTenantDTO);
	}
	
	/**
	 * This the REST API to return the status of the tenant in JSON format
	 * @param request The HttpServletRequest
	 * @param response The HttpServletResponse
	 * @return JSON representing provisioning status messages
	 */
	@RequestMapping("getProvisioningStatusData.htm")
	public ModelAndView getProvisioningStatus(HttpServletRequest request, HttpServletResponse response) {
	       // get the parameter for tenantId from request
	    ArrayList<String> errorList = new ArrayList<String>();
	    String tenantId = request.getParameter("tenantId");
	    String callback=request.getParameter("callback");
	    String tenantDashboardAccessUrl = "";
	    ModelAndView mv = new ModelAndView();
	    
	    if (tenantId == null || tenantId.trim().length() == 0) {
	        errorList.add("tenantId is required");
	    }
        if (errorList.size() == 0) {
            List<String> provisioningStatusList = provisionService.getProvisioningStatusList(tenantId);
            if (provisioningStatusList == null) {
                provisioningStatusList = new ArrayList<String>();
            } else if(provisioningStatusList.size()==7) {
            	tenantDashboardAccessUrl = provisionService.getDashBoardAccessUrl(tenantId);
            }
            
            mv.setViewName("provisioningStatusDataPage");
            mv.addObject("provisioningStatusList", provisioningStatusList);
            mv.addObject("tenantUrl", tenantDashboardAccessUrl);
            mv.addObject("callback", callback);
        } else {
            mv.setViewName("jsonErrorPage");
            mv.addObject("errorList", errorList);
        }
	    return mv;
	}
	
	/**
	 * Act as a redirector to Provisioning Status Page
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return provisioningStatusPage view
	 */
    @RequestMapping("ProvisioningStatus.htm")
    public ModelAndView showProvisioningStatusPage(HttpServletRequest request, HttpServletResponse response) {
       String tenantId = request.getParameter("tenantid");
       ModelAndView modelAndView = new ModelAndView();
       modelAndView.setViewName("provisioningStatusPage");
       modelAndView.addObject("tenantId", tenantId);
       return new ModelAndView("provisioningStatusPage");
    }
}
