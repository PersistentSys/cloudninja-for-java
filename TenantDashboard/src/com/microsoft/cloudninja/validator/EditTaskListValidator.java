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
package com.microsoft.cloudninja.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.microsoft.cloudninja.transferObjects.EditManageListDTO;

@Component("editTaskListValidator")
public class EditTaskListValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return EditManageListDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		EditManageListDTO editManageListDTO = (EditManageListDTO)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "taskList.subject", "managetasklist.create.subject.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "taskList.startDate", "managetasklist.create.startdate.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "taskList.dueDate", "managetasklist.create.duedate.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "taskList.priority", "managetasklist.create.priority.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "taskList.details", "managetasklist.create.details.required");
		
		if(!DateValidator.isValidDate(editManageListDTO.getTaskList().getStartDate()))
		{
			errors.rejectValue("taskList.startDate", "Invalid Date", "Format is incorrect");
		}else if(!DateValidator.isValidDate(editManageListDTO.getTaskList().getDueDate()))
		{
			errors.rejectValue("taskList.dueDate", "Invalid Date", "Format is incorrect");
		}else if(!DateValidator.isValidDateRange(editManageListDTO.getTaskList().getStartDate(), editManageListDTO.getTaskList().getDueDate())){
			
			errors.rejectValue("taskList.startDate", "Invalid Date", "Due Date is incorrect with respect to start date");
			
		}
	}
}
