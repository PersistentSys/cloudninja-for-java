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

import com.microsoft.cloudninja.transferObjects.CreateMangeUserDTO;
import com.microsoft.cloudninja.utils.CommonUtility;

@Component("manageUsersCreateValidator")
public class ManageUsersCreateValidator implements Validator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class<?> clazz) {
		return CreateMangeUserDTO.class.isAssignableFrom(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		
		CreateMangeUserDTO createMangeUserDTO = (CreateMangeUserDTO) target;


		// Google /Yahoo / LiveId
		if (!createMangeUserDTO.getIdentityProvider().equals("CloudNinjaSts")) {
			if (!CommonUtility.emailValidation(createMangeUserDTO.getMember()
					.getMemberCompoundKey().getMemberId())) {

				errors.rejectValue("member.memberCompoundKey.memberId",
						"Incorrect Email Format", "Incorrect Email Format");
			}
		} else {
				// member id required
				ValidationUtils.rejectIfEmptyOrWhitespace(errors,
						"member.memberCompoundKey.memberId",
						"manageusers.create.memberid.required");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "member.name",
						"manageusers.create.memberrealname.required");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors,
						"member.password",
						"manageusers.create.memberpassword.required");
		}
	}
}
