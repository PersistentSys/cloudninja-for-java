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
package com.persistent.cloudninja.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.persistent.cloudninja.dao.FileUploadDao;
import com.persistent.cloudninja.service.FileUploadService;
import com.persistent.cloudninja.utils.StorageClientUtility;

@Service("fileUploadService")
public class FileUploadserviceImpl implements FileUploadService{

	@Autowired
	private FileUploadDao fileUploadDao;
	
	@Autowired
	private StorageClientUtility storageClientUtility;
	
	@Override
	public String fileUploadService(MultipartFile logo, String tenantId) {
		String logoFileURL =  storageClientUtility.uploadLogoBlob(logo,tenantId);
		fileUploadDao.insertLogoFileName(logo,tenantId);
		return logoFileURL;
	}

}
