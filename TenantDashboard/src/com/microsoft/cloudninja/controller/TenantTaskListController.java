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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.microsoft.cloudninja.domainobject.TaskList;
import com.microsoft.cloudninja.domainobject.Tenant;
import com.microsoft.cloudninja.exception.CloudNinjaException;
import com.microsoft.cloudninja.exception.SystemException;
import com.microsoft.cloudninja.service.ManageUsersService;
import com.microsoft.cloudninja.service.TenantTaskListService;
import com.microsoft.cloudninja.transferObjects.CreateManageListDTO;
import com.microsoft.cloudninja.transferObjects.EditManageListDTO;
import com.microsoft.cloudninja.transferObjects.TaskListDTO;
import com.microsoft.cloudninja.transferObjects.TrafficSimulationDTO;
import com.microsoft.cloudninja.validator.CreateTaskListValidator;
import com.microsoft.cloudninja.validator.EditTaskListValidator;
import com.microsoft.cloudninja.web.security.CloudNinjaConstants;

/**
 * TenantTaskListController manages Tenant Task List.
 */
@Controller
public class TenantTaskListController {
	/**
	 * taskListService performs operation like create, edit and delete task list.
	 */
	@Autowired
	private TenantTaskListService taskListService;
	
	@Autowired
	private ManageUsersService manageUsersService;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	/**
	 * createTaskListValidator validates the inputs while creating task list.
	 */
	@Autowired
	private CreateTaskListValidator createTaskListValidator;

	/**
	 * editTaskListValidator validates the inputs while editing task list.
	 */
	@Autowired
	private EditTaskListValidator editTaskListValidator;
	
	private static ResourceBundle rsBundle;
	
    @RequestMapping(value = "/redirectToHomePage.htm")
    public ModelAndView showRedirectToTenantHomePage(
	    HttpServletRequest request,
	    @CookieValue("CLOUDNINJAAUTH") String cookie) {

	if (cookie == null) {
	    cookie = request.getAttribute("cookieNameAttr").toString();
	}

	String tenantId = AuthFilterUtils.getFieldValueFromCookieString(
		CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);

	ModelAndView model = new ModelAndView(new RedirectView("/" + tenantId
		+ "/showTenantHomePage.htm", true));
	return model;
    }

	//****************************** Traffic Simulation*************************************************************************************/
    /**
	  * Method added to generate simulated data for bytes sent and received
	  * @return String : Random generated String
	  */
	 @RequestMapping(value="/showTrafficSimulationPage.htm")
	 public ModelAndView showTrafficsimulation( HttpServletRequest request,
			    HttpServletResponse response,
			    @CookieValue(value = "CLOUDNINJAAUTH", required = false) String cookie) {
		 
		    int START_INDEX=2;
			int END_INDEX=100;
			TrafficSimulationDTO trafficSimulationDTO = new TrafficSimulationDTO();
			
			if (cookie == null) {
					cookie = request.getAttribute("cookieNameAttr").toString();
			}
			
			String tenantId = AuthFilterUtils.getFieldValueFromCookieString(CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);
		    response.addCookie(getTenantLogoCookieInResponse(tenantId, cookie));
			
			Random randomGen = new Random();
			int randomNum = randomGen.nextInt(END_INDEX - START_INDEX + 1) + START_INDEX;
						
			StringBuilder strbldr = new StringBuilder();
			for(int i=0;i<randomNum;i++){
				strbldr.append(Character.toChars((int) (Math.floor(26*randomGen.nextDouble()) + 65)));
			}
			
			trafficSimulationDTO.setTenantId(tenantId);
			trafficSimulationDTO.setRandomString(strbldr.toString());
		    return new ModelAndView("trafficSimulation", "trafficSimulationDTO",trafficSimulationDTO);
		   
	}

	//******************************************** Tenant Task List ****************************************************/
	/**
	 * List Tenant Tasks.
	 * @param cookie used to retrieve Tenant ID
	 * @return MadelAndView mapped to tenantHomePage view
	 * @throws CloudNinjaException
	 */
	
	
    @RequestMapping(value = "{tenantId}/showTenantHomePage.htm")
    public ModelAndView showTenantHomePage(
	    HttpServletRequest request,
	    HttpServletResponse response,
	    @CookieValue(value = "CLOUDNINJAAUTH", required = false) String cookie)
	    throws CloudNinjaException {

	if (cookie == null) {
		cookie = request.getAttribute("cookieNameAttr").toString();
	}
	String tenantId = AuthFilterUtils.getFieldValueFromCookieString(
			CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);
	if (null == rsBundle) {
		rsBundle = ResourceBundle.getBundle("storageAcc");
	}
	response.addCookie(getTenantLogoCookieInResponse(tenantId, cookie));	
	
	ModelAndView model =  new ModelAndView(new RedirectView("/" + tenantId + "/showTenantHomePageList.htm", true));
	return model;
    }

	@RequestMapping(value = "/logout.htm")
	public ModelAndView logout(
			HttpServletRequest request,
			HttpServletResponse response,
			@CookieValue(value = "CLOUDNINJAAUTH", required = false) String cookie)
			throws CloudNinjaException {

		if (cookie != null) {
			cookie = null;
			Cookie c = new Cookie("CLOUDNINJAAUTH", null);
			c.setPath("/");
			response.addCookie(c);
			response.setHeader("Cache-Control", "no-cache,no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);
		}
		if (request.getAttribute("cookieNameAttr") != null) {
			request.setAttribute("cookieNameAttr", null);
		}

		return new ModelAndView("logoutsuccess");
	}

	/**
	 * This method is used to show user a page where he can provide the invitation code
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ModelAndView
	 * @throws CloudNinjaException
	 */
	@RequestMapping(value = "/codeVerification.htm")
	public ModelAndView showCodeVerificationPage(
			HttpServletRequest request,
			HttpServletResponse response) throws CloudNinjaException {
		    
		    String liveGuid = (String) request.getAttribute("guid");
		    String encodedToken="";
			try {
				// encode as this XML snippet is to be set as hidden parameter in the JSP
				encodedToken = URLEncoder.encode(request.getParameter("wresult"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		    ModelAndView mv = new ModelAndView("codeVerificationPage");
		    mv.addObject("acsToken", encodedToken);
		    mv.addObject("liveGuid", liveGuid);	    
		return mv;
	}
	
	@RequestMapping(value = "/validateInvitationCode.htm")
	public ModelAndView validateInvitationCode(
			HttpServletRequest request,
			HttpServletResponse response) throws CloudNinjaException {
		    
		String invitationCode = request.getParameter("invitationCode");
		String wresult = request.getParameter("wresult");
		String liveGuid = request.getParameter("liveGuid");
		invitationCode = invitationCode.trim();
		
		// find and update the member
		boolean isMemberFoundAndUpdated = manageUsersService.findAndUpdateMember(invitationCode,liveGuid);
		if(isMemberFoundAndUpdated){
			 return  new ModelAndView("autoSubmitPage","wresult", wresult);
		}else{
			 // As no token will be found user will be directed to login 
			 return  new ModelAndView("autoSubmitPage","wresult", "");
		}
	}
    
    @RequestMapping(value="{tenantId}/showTenantHomePageList.htm")
    public ModelAndView preAuthenticate(HttpServletRequest request,HttpServletResponse response,
    		@CookieValue(value = "CLOUDNINJAAUTH", required = false) String cookie,
    		@PathVariable ("tenantId") String tenantId) throws SystemException {
	   
	    
	    if (cookie == null) {
			cookie = request.getAttribute("cookieNameAttr").toString();
		}

	    
	    response.addCookie(getTenantLogoCookieInResponse(tenantId, cookie));
	    String tenentDbString = "tnt_" + AuthFilterUtils.getFieldValueFromCookieString(CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);
	    String userName = AuthFilterUtils.getFieldValueFromCookieString(CloudNinjaConstants.COOKIE_USERNAME_PREFIX, cookie);;
	    
	    //Listing the task list
	    List<TaskList> viewTaskList = taskListService.showList(tenentDbString);
	    TaskListDTO taskListDTO = new TaskListDTO();
	    taskListDTO.setTaskList(viewTaskList);
	    ModelAndView model =  new ModelAndView("showTenantHomePageList", "taskListDTO", taskListDTO);
	    model.addObject("userName", userName);
	    return model;
	}
	
	
	private Cookie getTenantLogoCookieInResponse(String tenantId, String cookieName) {
		String logoUrl = "";
		Tenant tenant = hibernateTemplate.get(Tenant.class, tenantId);
		String logoFilename = tenant.getLogoFileName();
		if (null == logoFilename || logoFilename.trim().length() == 0) {
			logoUrl = "";
		} else {
			
			//create logo URL from config property file
			logoUrl = getLogoUrlFromConfig(logoFilename,tenantId);
		}
		Cookie logoCokie = new Cookie("CLOUDNINJALOGO", logoUrl);
		logoCokie.setMaxAge(-1);
		logoCokie.setPath("/");
		return logoCokie;
		
	}
	
	/**
	 * Constructs the URL for tenant's logo file.
	 * 
	 * @param logoFilename
	 * @param tenantId
	 * @return
	 */
	private String getLogoUrlFromConfig(String logoFilename, String tenantId) {
		StringBuffer urlBuffer = new StringBuffer();

		urlBuffer.append("https://");
		urlBuffer.append(rsBundle.getString("storage.accName").trim());
		urlBuffer.append(rsBundle.getString("storage.tntsPrefix").trim());
		urlBuffer.append(tenantId.toLowerCase());
		urlBuffer.append("/" + logoFilename);

		return urlBuffer.toString();
	}

	//******************************************** Create Task List ****************************************************/
	 /**
	  * GET method for creating tenant task.
	  * @return MadelAndView mapped to createNewList view
	  */
	 @RequestMapping(value="{tenantId}/createNewList.htm",method = RequestMethod.GET)
	public ModelAndView createNewListPage() {
		CreateManageListDTO createListDTO = new CreateManageListDTO();
		createListDTO.setTaskList(new TaskList());
		return new ModelAndView("createNewList", "createListModel",createListDTO);
	}

	/**
	 * POST method while creating tenant task.
	 * @param cookie retrieves tenant Id
	 * @param createListDTO has the new task information to be updated into database
	 * @param result
	 * @return MadelAndView mapped to createNewList view if no input errors else to tenantHomePage view
	 * @throws CloudNinjaException
	 */
    @RequestMapping(value = "{tenantId}/createList.htm", method = RequestMethod.POST)
    public ModelAndView createListPage(
	    HttpServletRequest request,
	    @CookieValue("CLOUDNINJAAUTH") String cookie,
	    @PathVariable("tenantId") String tenantId,
	    @Valid @ModelAttribute("createListModel") CreateManageListDTO createListDTO,
	    BindingResult result) throws CloudNinjaException {

	if (cookie == null) {
	    cookie = request.getAttribute("cookieNameAttr").toString();
	}

	getTenantLogoCookieInResponse(tenantId, cookie);
	String tenentDbString = "tnt_"
		+ AuthFilterUtils.getFieldValueFromCookieString(
			CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);
	// Creating the list
	// Validating the Fields
	createTaskListValidator.validate(createListDTO, result);
	if (!result.hasErrors()) {
	    taskListService.createTaskList(createListDTO.getTaskList(),
		    tenentDbString);
	    createListDTO.setTaskList(new TaskList());

	    // Retrieving the task list
	    return new ModelAndView(new RedirectView("showTenantHomePageList.htm"));
	} else {
	    // error
	    return new ModelAndView("createNewList", "createListModel",
		    createListDTO);
	}
    }

	//******************************************** Delete Task List ****************************************************/
	 /**
	  * Delete Task List.
	  * @param cookie retrieves tenant Id
	  * @param taskList is a task to be deleted.
	  * @return ModelAndView mapped to tenantHomePage view
	  * @throws CloudNinjaException
	  */
	 @RequestMapping(value="{tenantId}/deleteList.htm")
	 public ModelAndView deleteList(
		 	HttpServletRequest request,
			@CookieValue("CLOUDNINJAAUTH") String cookie,
			@PathVariable("tenantId") String tenantId,
			@ModelAttribute TaskList taskList,
			BindingResult result) throws CloudNinjaException {
		
	     if (cookie == null) {
		    cookie = request.getAttribute("cookieNameAttr").toString();
		}

		getTenantLogoCookieInResponse(tenantId, cookie);
		String tenentDbString = "tnt_"
			+ AuthFilterUtils.getFieldValueFromCookieString(
				CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);

		//Deleting the List
		if (taskList.getTaskId() != 0) {
			taskListService.deleteList(taskList, tenentDbString);
		}

		//Retrieving the task list
		return new ModelAndView(new RedirectView("showTenantHomePageList.htm"));
	}


	//******************************************** Details of Task List ****************************************************/

	/**
	 * Shows Details for the selected Task.
	 * @param taskList is the task to be detailed
	 * @return ModelAndView mapped to listDetail view
	 */
	 @RequestMapping(value="{tenantId}/listDetails.htm")
	 public ModelAndView listDetails(@ModelAttribute TaskList taskList,BindingResult result) {
		TaskListDTO taskListDTO = new TaskListDTO();
		List<TaskList> list = new ArrayList<TaskList>();
		list.add(taskList);
		taskListDTO.setTaskList(list);
		return new ModelAndView("listDetails","taskListDTO",taskListDTO);
	}

	//******************************************** Edit Task List ****************************************************/

	/**
	 * Page for editing selected task list.
	 * @param taskList is the task to be edited.
	 * @return ModelAndView mapped to editListPage view
	 */
	 @RequestMapping(value="{tenantId}/editListPage.htm")
	 public ModelAndView editList(Model model,@ModelAttribute TaskList taskList,BindingResult result) {
		EditManageListDTO editManageListDTO = new EditManageListDTO();
		editManageListDTO.setTaskList(taskList);
		return new ModelAndView("editListPage","editListModel",editManageListDTO);
	}

	/**
	 * Save edited task list.
	 * @param cookie retrieves tenant Id
	 * @param editManageListDTO contains edited information
	 * @return ModelAndView mapped to editListPage view if no input errors else to tenantHomePage view
	 */
	 @RequestMapping(value = "{tenantId}/editList.htm")
	 public ModelAndView updateUser(HttpServletRequest request,
			@CookieValue("CLOUDNINJAAUTH") String cookie,
			@PathVariable("tenantId") String tenantId,
			@Valid @ModelAttribute("editListModel") EditManageListDTO editManageListDTO,
			BindingResult result) throws CloudNinjaException {

	     if (cookie == null) {
		    cookie = request.getAttribute("cookieNameAttr").toString();
		}

		getTenantLogoCookieInResponse(tenantId, cookie);
		String tenentDbString = "tnt_"
			+ AuthFilterUtils.getFieldValueFromCookieString(
				CloudNinjaConstants.COOKIE_TENANTID_PREFIX, cookie);

        // Update TaskList
		editTaskListValidator.validate(editManageListDTO, result);
		if (!result.hasErrors()) {
			taskListService.createTaskList(editManageListDTO.getTaskList(),tenentDbString);
			//Retrieving the task list
			return new ModelAndView(new RedirectView("showTenantHomePageList.htm"));
		} else {
			return new ModelAndView("editListPage","editListModel",editManageListDTO);
		}
	}

}
