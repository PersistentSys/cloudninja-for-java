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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.persistent.cloudninja.domainobject.Member;
import com.persistent.cloudninja.domainobject.MemberRole;
import com.persistent.cloudninja.domainobject.TenantBean;
import com.persistent.cloudninja.exception.CloudNinjaException;
import com.persistent.cloudninja.service.ManageUsersService;
import com.persistent.cloudninja.transferObjects.CreateMangeUserDTO;
import com.persistent.cloudninja.transferObjects.DeleteManageUsersDTO;
import com.persistent.cloudninja.transferObjects.ManageUsersDTO;
import com.persistent.cloudninja.transferObjects.UpdateManageUsersDTO;
import com.persistent.cloudninja.validator.ManageUsersCreateValidator;
import com.persistent.cloudninja.web.security.CloudNinjaConstants;

@Controller
public class MangeUsersController {

	private static final Logger LOGGER = Logger
			.getLogger(MangeUsersController.class);

	@Autowired
	private ManageUsersService manageUsersService;
	
	@Autowired
	private ManageUsersCreateValidator manageUsersCreateValidator;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));

	}

	/********************************* USER LIST FLOW *************************************************************************/
	@RequestMapping(value = "{tenantId}/showManageUsersList.htm", method = RequestMethod.GET)
	public ModelAndView getManageUsersList(
			HttpServletRequest request,
			@CookieValue(value = "CLOUDNINJAAUTH", required = false) String cookie,
			@ModelAttribute("tenantBean") TenantBean tenantbean,
			BindingResult result) throws CloudNinjaException {

		LOGGER.info("Start of method getManageUsersList");
		if (cookie == null) {
			cookie = request.getAttribute("cookieNameAttr").toString();
		}
		String tenantId = AuthFilterUtils.getFieldValueFromCookieString(
			CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);

		List<Member> memberList = null;
		memberList = manageUsersService.getManageUsersList(tenantId);
		ManageUsersDTO manageUsersDTO = new ManageUsersDTO();
		manageUsersDTO.setManageUsersList(memberList);
		LOGGER.info("End of method getManageUsersList");

		return new ModelAndView("manageUsersListView", "manageUsersDTO",
				manageUsersDTO);

	}

	/********************************* CREATE USER FLOW *************************************************************************/
	@RequestMapping(value = "{tenantId}/createManageUser.htm")
	public ModelAndView createMangeUserPage() throws CloudNinjaException {

		LOGGER.info("Start of method createMangeUserPage");
		List<MemberRole> rolesList = new ArrayList<MemberRole>();
		// Currently Hard coding Roles
		MemberRole memberAdmin = new MemberRole();
		memberAdmin.setRoleId(1);
		memberAdmin.setName("Administrator");
		rolesList.add(memberAdmin);
		MemberRole memberUser = new MemberRole();
		memberUser.setRoleId(2);
		memberUser.setName("User");
		rolesList.add(memberUser);
		
		CreateMangeUserDTO createMangeUserDTO = new CreateMangeUserDTO();
		createMangeUserDTO.setRoleList(rolesList);
		createMangeUserDTO.setMember(new Member());
		//Set identity provider list
		createMangeUserDTO.setIdentityProviderList(manageUsersService.getIdentityProviderList());
		LOGGER.info("End of method createMangeUserPage");

		return new ModelAndView("createUserPageView",
				"createManageUserPageModel", createMangeUserDTO);
	}

	@RequestMapping(value = "{tenantId}/createUser.htm", method = RequestMethod.POST)
	public ModelAndView createManageUser(
			HttpServletRequest request,
			@CookieValue(value = "CLOUDNINJAAUTH", required = false) String cookie,
			@Valid @ModelAttribute("createManageUserPageModel") CreateMangeUserDTO createMangeUserDTO,
			BindingResult result) throws CloudNinjaException {

		LOGGER.info("Start of method createManageUser");
		if (cookie == null) {
			cookie = request.getAttribute("cookieNameAttr").toString();
		}
		String tenantId = AuthFilterUtils.getFieldValueFromCookieString(
			CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);

		// create user
		manageUsersCreateValidator.validate(createMangeUserDTO, result);
		if (!result.hasErrors()) {
			createMangeUserDTO.getMember().setEnabled(true);
			createMangeUserDTO.getMember().setCreated(new Date());
			createMangeUserDTO.getMember().getMemberCompoundKey().setTenantId(tenantId);
			createMangeUserDTO.getMember().setLiveInvitationCode(createMangeUserDTO.getInvitationCode());
			manageUsersService.createUser(createMangeUserDTO.getMember());
			createMangeUserDTO.setMember(new Member());
			createMangeUserDTO.setIdentityProviderList(manageUsersService.getIdentityProviderList());
			LOGGER.info("End of method createManageUser");

			// Retrieve User List
			return new ModelAndView(new RedirectView("showManageUsersList.htm"));
		} else {
			// Error
		    List<MemberRole> rolesList = new ArrayList<MemberRole>();
		 // Currently Hard coding Roles
			MemberRole memberAdmin = new MemberRole();
			memberAdmin.setRoleId(1);
			memberAdmin.setName("Administrator");
			rolesList.add(memberAdmin);
			MemberRole memberUser = new MemberRole();
			memberUser.setRoleId(2);
			memberUser.setName("User");
			rolesList.add(memberUser);
			createMangeUserDTO.setRoleList(rolesList);
			createMangeUserDTO.setIdentityProviderList(manageUsersService.getIdentityProviderList());
			createMangeUserDTO.setIdentityProvider(createMangeUserDTO.getIdentityProvider());
			createMangeUserDTO.setMember(createMangeUserDTO.getMember());
			return new ModelAndView("createUserPageView",
					"createManageUserPageModel", createMangeUserDTO);
		}
	}

	/********************************* DELETE USER FLOW *************************************************************************/
	@RequestMapping(value = "{tenantId}/deleteUserPage.htm")
	public ModelAndView getDeleteUserPage(@ModelAttribute Member member) {
		LOGGER.info("Start of method getDeleteUserPage");
		DeleteManageUsersDTO deleteManageUsersDTO = new DeleteManageUsersDTO();
		deleteManageUsersDTO.setMember(member);
		LOGGER.info("End of method getDeleteUserPage");

		return new ModelAndView("deleteUserPageView",
				"deleteManageUserPageModel", deleteManageUsersDTO);
	}

	@RequestMapping(value = "{tenantId}/deleteUser.htm", method = RequestMethod.POST)
	public ModelAndView deleteUser(
			HttpServletRequest request,
			@CookieValue(value = "CLOUDNINJAAUTH", required = false) String cookie,
			@Valid @ModelAttribute("deleteManageUserPageModel") DeleteManageUsersDTO deleteManageUsersDTO,
			BindingResult result) throws CloudNinjaException {
		LOGGER.info("Start of method deleteUser");
		if (cookie == null) {
			cookie = request.getAttribute("cookieNameAttr").toString();
		}
		String tenantId = AuthFilterUtils.getFieldValueFromCookieString(
			CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);

		// Delete User
		deleteManageUsersDTO.getMember().getMemberCompoundKey()
				.setTenantId(tenantId);
		manageUsersService.deleteUser(deleteManageUsersDTO.getMember());
		LOGGER.info("End of method deleteUser");

		// Retrieve User List
		return new ModelAndView(new RedirectView("showManageUsersList.htm"));
	}

	/********************************* UPDATE USER FLOW *************************************************************************/
	@RequestMapping(value = "{tenantId}/updateUserPage.htm")
	public ModelAndView getUpdateUserPage(@ModelAttribute Member member)
			throws CloudNinjaException {
		LOGGER.info("Start of method getUpdateUserPage");
		 List<MemberRole> rolesList = new ArrayList<MemberRole>();
	        // Currently Hard coding Roles
			MemberRole memberAdmin = new MemberRole();
			memberAdmin.setRoleId(1);
			memberAdmin.setName("Administrator");
			rolesList.add(memberAdmin);
			MemberRole memberUser = new MemberRole();
			memberUser.setRoleId(2);
			memberUser.setName("User");
			rolesList.add(memberUser);
		UpdateManageUsersDTO updateManageUsersDTO = new UpdateManageUsersDTO();
		updateManageUsersDTO.setRoleList(rolesList);
		updateManageUsersDTO.setMember(member);
		LOGGER.info("End of method getUpdateUserPage");

		return new ModelAndView("updateUserPageView",
				"updateManageUserPageModel", updateManageUsersDTO);
	}

	@RequestMapping(value = "{tenantId}/updateUser.htm", method = RequestMethod.POST)
	public ModelAndView updateUser(
			HttpServletRequest request,
			@CookieValue(value = "CLOUDNINJAAUTH", required = false) String cookie,
			@Valid @ModelAttribute("updateManageUserPageModel") UpdateManageUsersDTO updateManageUsersDTO,
			BindingResult result) throws CloudNinjaException {
		LOGGER.info("Start of method updateUser");
		if (cookie == null) {
			cookie = request.getAttribute("cookieNameAttr").toString();
		}
		String tenantId = AuthFilterUtils.getFieldValueFromCookieString(
			CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);

		// Update User
		updateManageUsersDTO.getMember().getMemberCompoundKey()
				.setTenantId(tenantId);
		manageUsersService.updateUser(updateManageUsersDTO.getMember());
		LOGGER.info("End of method updateUser");

		// Retrieve User List
		return new ModelAndView(new RedirectView("showManageUsersList.htm"));
	}
}
