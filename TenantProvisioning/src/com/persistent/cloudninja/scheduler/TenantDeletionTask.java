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
package com.persistent.cloudninja.scheduler;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.persistent.cloudninja.dao.TaskCompletionDao;
import com.persistent.cloudninja.service.ProvisioningService;
import com.persistent.cloudninja.utils.SchedulerSettings;
import com.persistent.cloudninja.utils.StorageClientUtility;

/**
 * Task which listens to TenantDeletionQueue. After receiving a message it deletes
 * the tenant specified in message.
 */
@Component
public class TenantDeletionTask extends TaskActivityBase {

	private static final Logger LOGGER = Logger.getLogger(TenantDeletionTask.class);
	
	@Autowired
    private TaskCompletionDao taskCompletionDao;
	
	@Autowired
	private ProvisioningService provisioningService;

	public TenantDeletionTask() throws InvalidKeyException, URISyntaxException {
		super(new TenantDeletionQueue(SchedulerSettings.TenantDeletionQueue, StorageClientUtility.getCloudStorageAccount().createCloudQueueClient()));
	}
	
	@Override
	public boolean execute() {
	    boolean retval = true;
		try {
		    TenantDeletionQueue tntDeletionQueue = (TenantDeletionQueue) getWorkQueue();
			String tenantId = tntDeletionQueue.dequeue(SchedulerSettings.MessageVisibilityTimeout);
			if (tenantId == null) {
				LOGGER.debug("Msg is null");
				retval = false;
			} else {
			    StopWatch watch = new StopWatch();
			    watch.start();
				provisioningService.removeTenant(tenantId);
				LOGGER.debug("tenant deleted :" + tenantId);
				watch.stop();
	            taskCompletionDao.updateTaskCompletionDetails(watch.getTotalTimeSeconds(), "DeleteTenant", "Tenant Id "+tenantId+" is deleted.");
	        }
		} catch (StorageException e) {
			retval = false;
			LOGGER.error(e.getMessage(), e);
		}
		return retval;
	}

}
