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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.persistent.cloudninja.transferObjects.ProvisioningTenantDTO;

/**
 * Validator class for ProvisioningTenantDTO.
 *
 */
@Component("provisioningTenantDTOValidator")
public class ProvisioningTenantDTOValidator implements Validator {
	
	private static String CLOUDNINJASTS = "CloudNinjaSts";
    
    @Value("#{'${key.demokey}'}")
    private String expectedDemoKey;

	@Override
	public boolean supports(Class<?> clas) {
		return ProvisioningTenantDTO.class.isAssignableFrom(clas);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ProvisioningTenantDTO provisioningTenantDTO = (ProvisioningTenantDTO) target;
		
		String identityProvider = provisioningTenantDTO.getIdentityProvider();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "demoKey", "field is required", "The Demo Key field is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tenantId", "field is required", "The Service Alias field is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "companyName", "field is required", "The Company Name field is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "memberId", "field is required", "The User id field is required.");
		 // Validate only if STS
		if(identityProvider.equals(CLOUDNINJASTS)) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "memberName", "field is required", "The Name field is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "memberPassword", "field is required", "The Password field is required.");
		}
		
        String demoKey = provisioningTenantDTO.getDemoKey();
        String tenantId = provisioningTenantDTO.getTenantId();
        String companyName = provisioningTenantDTO.getCompanyName();
        String memberId = provisioningTenantDTO.getMemberId();
        String memberName = provisioningTenantDTO.getMemberName();
        String memberPassword = provisioningTenantDTO.getMemberPassword();

        String numberAndLetterRegex = "^[a-zA-Z0-9]+$";
        
        if (!demoKey.isEmpty() && !demoKey.equals(expectedDemoKey)) {
            errors.rejectValue("demoKey", "Invalid Demo Key", "Demo Key is not valid.");
        }
        // validations for tenant-id field
        if (!tenantId.isEmpty()) {
            Pattern p = Pattern.compile(numberAndLetterRegex);
            Matcher m = p.matcher(tenantId);
            if (!m.matches()) {
                errors.rejectValue("tenantId", "", "Only letters and numbers are allowed.");
            }
            if (!validLength(3, 20, tenantId)) {
                errors.rejectValue(
                        "tenantId",
                        "", "The field Service Alias must be a string with a minimum length of 3 and a maximum length of 20.");
            }
        }
       // validations for Company name
       if(!companyName.isEmpty()) {
           if(!validLength(3, 30, companyName )) {
               errors.rejectValue(
                       "companyName",
                       "", "The field Company Name must be a string with a minimum length of 3 and a maximum length of 30.");
           }
        }
        // validate user id
        if (!memberId.isEmpty()) {
            if (!validLength(4, 150, memberId)) {
                errors.rejectValue(
                        "memberId",
                        "", "The field User id must be a string with a minimum length of 4 and a maximum length of 150.");
            }
        }
        // Validate only if STS
        if(identityProvider.equals(CLOUDNINJASTS)) {
	        // Validate Name
	        if (!memberName.isEmpty()) {
	            if(!validLength(4, 30, memberName)) {
	                errors.rejectValue(
	                        "memberName",
	                        "", "The field Name must be a string with a minimum length of 4 and a maximum length of 30.");
	            }   
	        }
	        // Validate password
	        if(!memberPassword.isEmpty()) {
	            String msg =
	                    "The field Password must contain at least one digit, special symbols '@#$%'," +
	                    " lower and uppercase characters with a minimum length of 6 and a maximum length of 20.";
	            
	            Pattern pattern;
	            Matcher matcher;
	            String PASSWORD_PATTERN =  "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
	            pattern = Pattern.compile(PASSWORD_PATTERN);
	            
	            matcher = pattern.matcher(memberPassword);
	            if(!matcher.matches()) {
	                errors.rejectValue(
	                        "memberPassword",
	                        "", msg);  
	            }
	        }
        }
    }
	
	/**
	 * Validate the length of given string
	 * @param min  The minimum length
	 * @param max  The maximum length
	 * @param message The input
	 * @return boolean true if valid false otherwise
	 */
    private boolean validLength(int min, int max, String message) {
        boolean isValid = true;
        if (message != null) {
            message = message.trim();
            int length = message.length();
            if (length < min || length > max) {
                isValid = false;
            }
        } else {
            isValid = false;
        }
        return isValid;
    }
}
