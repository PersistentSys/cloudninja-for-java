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

import com.persistent.cloudninja.dao.ProvisioningDao;
import com.persistent.cloudninja.service.ProvisioningService;

@Service("provisionService")
public class ProvisioningServiceImpl implements ProvisioningService {

	@Autowired
	private ProvisioningDao provisioningDao;
	
	@Override
	public boolean provisionTenant(String id, String login, String password) {
		provisioningDao.getDetails(id, login, password);
		return false;
	}

}
