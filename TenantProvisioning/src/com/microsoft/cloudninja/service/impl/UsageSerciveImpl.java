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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.cloudninja.dao.UsageDao;
import com.microsoft.cloudninja.scheduler.StorageUtility;
import com.microsoft.cloudninja.service.UsageService;
import com.microsoft.cloudninja.transferObjects.UsageDTO;
import com.microsoft.cloudninja.utils.SchedulerSettings;
@Service("usageService")
public class UsageSerciveImpl implements UsageService{

	@Autowired
	private UsageDao usageDao;
	
	@Autowired
	private StorageUtility storageUtility;

	@Override
	public UsageDTO getMetering(int month, String year) {
		return usageDao.getDashboardMetering(month, year);
	}

	@Override
	public UsageDTO getTenantMetering(String tenantId,int month,int year) {
		return usageDao.getTenantSpecificMetering(tenantId,month,year);
	}

	@Override
	public void runMeteringTasksManually() {
		try {
			String taskScheduler = "taskscheduler-";
			String taskId = SchedulerSettings.DatabaseSizeTask;
			storageUtility.putMessage(taskScheduler + taskId.toLowerCase(), taskId);
			taskId = SchedulerSettings.BlobStorageSizeTask;
			storageUtility.putMessage(taskScheduler + taskId.toLowerCase(), taskId);
			taskId = SchedulerSettings.DBBandwidthTask;
			storageUtility.putMessage(taskScheduler + taskId.toLowerCase(), taskId);
			taskId = SchedulerSettings.WebBandWidthUsageTask;
			storageUtility.putMessage(taskScheduler + taskId.toLowerCase(), taskId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
