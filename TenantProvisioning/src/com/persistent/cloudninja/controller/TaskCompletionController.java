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
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.persistent.cloudninja.domainobject.TaskCompletion;
import com.persistent.cloudninja.service.TaskCompletionService;
import com.persistent.cloudninja.transferObjects.TaskCompletionDTO;
import com.persistent.cloudninja.utils.CommonUtility;

/*
 * Controller class for Provisioning Task Completion Flow
 * 
 * */
@Controller
public class TaskCompletionController {

	private static final Logger LOGGER = Logger
			.getLogger(TaskCompletionController.class);

	@Autowired
	private TaskCompletionService taskCompletionService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"MM-dd-yyyy hh:mm:SS");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));

	}

	/*
	 *  Method for displaying Task Completed Result List
	 *  
	 * */
	@RequestMapping(value = "/showTaskCompletionList.htm", method = RequestMethod.GET)
	public ModelAndView getTaskCompletionList() {
		LOGGER.info("Start of  TaskCompletionController : getTaskCompletionList ");
		List<TaskCompletion> taskCompletionList = null;
		TaskCompletionDTO taskCompletionDTO = new TaskCompletionDTO();
		taskCompletionDTO.setUtcDate(CommonUtility.getUTCDate());
		taskCompletionList = this.taskCompletionService.getTaskCompletionList();
		taskCompletionDTO.setTaskCompletionList(taskCompletionList);
		LOGGER.info("End of  TaskCompletionController : getTaskCompletionList ");

		return new ModelAndView("taskCompletionPage", "taskCompletionDTO",
				taskCompletionDTO);
	}

	/*
	 *  Method for displaying details of particular completed task
	 *  
	 * */
	@RequestMapping(value = "/taskCompletionDetailsPage.htm")
	public ModelAndView getTaskCompletionDetails(
			@ModelAttribute TaskCompletion taskCompletion) {
		LOGGER.info("Start of  TaskCompletionController : getTaskCompletionDetails ");
		TaskCompletionDTO taskCompletionDTO = new TaskCompletionDTO();
		taskCompletionDTO.setTaskCompletion(taskCompletion);
		LOGGER.info("End of  TaskCompletionController : getTaskCompletionDetails");

		return new ModelAndView("taskCompletionDetailsPage",
				"taskCompletionDTO", taskCompletionDTO);
	}

}
