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
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.persistent.cloudninja.transferObjects.LogoFileDTO;

/**
 * Validator class for LogoFile. 
 *
 */
@Component("logoFileDTOValidator")
public class LogoFileDTOValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return LogoFileDTO.class.isAssignableFrom(clazz);
	}

	/**
	 * validates the multipart file.
	 * 
	 * @param target : LogoFile object.
	 * @param errors : stores and exposes validation errors.
	 * 
	 */
	@Override
	public void validate(Object target, Errors errors) {
		LogoFileDTO logoFile = (LogoFileDTO) target;
		MultipartFile file = logoFile.getFile(); // file should be of gif, png, bmp, jpeg, jpg type
		if (file.isEmpty()) {
			errors.rejectValue("file", "Invalid file", "File cannot be empty");
        } else if (!(file.getOriginalFilename().endsWith(".gif")
                | file.getOriginalFilename().endsWith(".png")
                | file.getOriginalFilename().endsWith(".bmp")
                | file.getOriginalFilename().endsWith(".jpeg")
                | file.getOriginalFilename().endsWith(".jpg"))) {
            errors.rejectValue("file", "Invalid file", "File should be a valid gif,png, bmp, jpeg or jpg file.");
		} else if( logoFile.getFile().getSize() > 200*1024)
		{
		    errors.rejectValue("file", "Invalid file", "File size should be less then 200KB.");
		}
	}

}
