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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.persistent.cloudninja.domainobject.TaskScheduleEntity;
import com.persistent.cloudninja.service.TaskScheduleService;
import com.persistent.cloudninja.transferObjects.EditTaskScheduleDTO;
import com.persistent.cloudninja.transferObjects.TaskScheduleListDTO;
import com.persistent.cloudninja.validator.EditTaskScheduleDTOValidator;

/**
 * Controller class for TaskSchedule page
 * @author 
 *
 */
@Controller
public class TaskScheduleController {
	
	@Autowired
	TaskScheduleService taskScheduleService;
	@Autowired
	EditTaskScheduleDTOValidator editTaskScheduleDTOValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class,new CustomDateEditor(dateFormat, true));

	}
	/**
	 * Get the task scheduled list to be displayed on view
	 * @return ModelAndView mapped to task schedule list Page
	 */
	@RequestMapping ("/TaskScheduleList.htm")
	public ModelAndView showScheduleTaskList() {
		List<TaskScheduleEntity> taskList = taskScheduleService.getTaskScheduleList();		
		TaskScheduleListDTO taskScheduleListDTO = new TaskScheduleListDTO();
		taskScheduleListDTO.setTaskScheduleList(taskList);
		
		//return "TaskScheduleListPage";
		return new ModelAndView("TaskScheduleListPage", "taskScheduleListDTO", taskScheduleListDTO);
	}
	
	/**
	 * This method is called to edit an scheduled task
	 * @param task the task to be edited
	 * @return the view mapping to task editing page
	 */
	@RequestMapping(value = "/editTaskSchedulePage.htm")
	public ModelAndView showTaskEditPage(@ModelAttribute TaskScheduleEntity task)
	{
		EditTaskScheduleDTO  editTaskScheduleDTO = new EditTaskScheduleDTO();
		editTaskScheduleDTO.setTaskSchedule(task);
		return new ModelAndView("editTaskSchedulePage", "editTaskScheduleDTO",editTaskScheduleDTO);
	}
	
	/**
	 *  This method is called for updating the edited task
	 * @param editTaskScheduleDTO the data transfer object from edit page
	 * @param result the binding result to see for errors
	 * @return On successful edit shows scheduled task list page
	 */
	@RequestMapping(value = "/updateTaskSchedule.htm", method = RequestMethod.POST)
	public ModelAndView updatetaskSchedule(
			@ModelAttribute("editTaskScheduleDTO") EditTaskScheduleDTO editTaskScheduleDTO, BindingResult result) {
		
		editTaskScheduleDTOValidator.validate(editTaskScheduleDTO, result);
		if (!result.hasErrors()) {
		// Update User
		taskScheduleService.updateTaskSchedule(editTaskScheduleDTO.getTaskSchedule());
		// Retrieve User List
		List<TaskScheduleEntity> taskList = null;
		// This value to be taken from cookie
		taskList = taskScheduleService.getTaskScheduleList();
		TaskScheduleListDTO taskScheduleListDTO = new TaskScheduleListDTO();
		taskScheduleListDTO.setTaskScheduleList(taskList);

		return new ModelAndView("TaskScheduleListPage", "taskScheduleListDTO", taskScheduleListDTO);
		}
		else {
			return new ModelAndView("editTaskSchedulePage", "editTaskScheduleDTO",editTaskScheduleDTO);
		}
	}
}
