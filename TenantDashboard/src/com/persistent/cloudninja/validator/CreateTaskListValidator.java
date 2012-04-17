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
package com.persistent.cloudninja.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.persistent.cloudninja.transferObjects.CreateManageListDTO;

@Component("createTaskListValidator")
public class CreateTaskListValidator implements Validator {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class<?> clazz) {
		return CreateManageListDTO.class.isAssignableFrom(clazz);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		
		CreateManageListDTO createManageListDTO = (CreateManageListDTO)target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "taskList.subject", "managetasklist.create.subject.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "taskList.startDate", "managetasklist.create.startdate.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "taskList.dueDate", "managetasklist.create.duedate.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "taskList.priority", "managetasklist.create.priority.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "taskList.details", "managetasklist.create.details.required");
		
		if(!DateValidator.isValidDate(createManageListDTO.getTaskList().getStartDate()))
		{
			errors.rejectValue("taskList.startDate", "Invalid Date", "Format is incorrect");
		}else if(!DateValidator.isValidDate(createManageListDTO.getTaskList().getDueDate()))
		{
			errors.rejectValue("taskList.dueDate", "Invalid Date", "Format is incorrect");
		}else if(!DateValidator.isValidDateRange(createManageListDTO.getTaskList().getStartDate(), createManageListDTO.getTaskList().getDueDate())){
			
			errors.rejectValue("taskList.startDate", "Invalid Date", "Due Date is incorrect with respect to start date");
			
		}
	}



}
