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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.persistent.cloudninja.transferObjects.EditTaskScheduleDTO;

/**
 * Class to validate operation key on edit task schedule page 
 * @author 
 *
 */
@Component("editTaskScheduleDTOValidator")
public class EditTaskScheduleDTOValidator implements Validator {
    
    @Value("#{'${key.operationskey}'}")
    private String expectedOperationKey;

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return EditTaskScheduleDTO.class.isAssignableFrom(clazz);
	}
    

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		EditTaskScheduleDTO editScheduledTaskDTO = (EditTaskScheduleDTO) target;

		String operationKey = editScheduledTaskDTO.getOperationKey();

		if ((null == operationKey || operationKey.trim().length() == 0) || !operationKey.trim().equals(expectedOperationKey)) {
			
			errors.rejectValue("operationKey","Invalid key" ,"Valid Operations Key is Required");
		}
	}
}
