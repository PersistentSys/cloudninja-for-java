/**
 * 
 */
package com.persistent.cloudninja.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.persistent.cloudninja.transferObjects.CreateMangeUserDTO;
import com.persistent.cloudninja.utils.CommonUtility;

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
