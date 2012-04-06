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
package com.microsoft.cloudninja.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.cloudninja.service.TenantProfileService;
import com.microsoft.cloudninja.utils.StorageClientUtility;

@Service("tenantProfileService")
public class TenantProfileServiceImpl implements TenantProfileService {
	
	private static final Logger LOGGER = Logger.getLogger(TenantProfileServiceImpl.class);
	
	@Autowired
	private StorageClientUtility storageClientUtility;
	
	@Override
	public void deleteTenant(String tenantId) {
		try {
			//put message as tenantId in "deletetenant" queue to delete the tenant.
			storageClientUtility.putMessage("deletetenant", tenantId);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
