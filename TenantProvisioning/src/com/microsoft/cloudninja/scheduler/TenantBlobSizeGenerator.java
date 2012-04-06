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
package com.microsoft.cloudninja.scheduler;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.microsoft.cloudninja.dao.TaskCompletionDao;
import com.microsoft.cloudninja.dao.TenantIdMasterDao;
import com.microsoft.cloudninja.domainobject.TenantIdMasterEntity;
import com.microsoft.cloudninja.utils.SchedulerSettings;
import com.microsoft.cloudninja.utils.StorageClientUtility;
import com.microsoft.windowsazure.services.core.storage.StorageException;

/**
 * Generator for tenant blob size calculation task. It generates the
 * messages to be processed by processor.
 *
 */
@Component
public class TenantBlobSizeGenerator extends TaskActivityBase {
	
	private static final Logger LOGGER = Logger.getLogger(TenantBlobSizeGenerator.class);

	@Autowired
	private TenantIdMasterDao tenantIdMasterDao;
	
	@Autowired
	private TaskCompletionDao taskCompletionDao;

	public TenantBlobSizeGenerator() throws InvalidKeyException, URISyntaxException {
		//task-queue which is shared between processor and generator.
		super(new TenantBlobSizeQueue(SchedulerSettings.BlobStorageSizeQueue, StorageClientUtility.getCloudStorageAccount().createCloudQueueClient()));
	}

	@Override
	public boolean execute() {
		boolean retVal = true;
		StopWatch watch = new StopWatch();
		try {
		    watch.start();
			LOGGER.debug("In generator");
			TenantBlobSizeQueue queue = (TenantBlobSizeQueue) getWorkQueue();
			//get all tenantIds
			List<TenantIdMasterEntity> listTenant = tenantIdMasterDao.getAllTenants();
			for (Iterator<TenantIdMasterEntity> iterator = listTenant.iterator(); iterator.hasNext();) {
				TenantIdMasterEntity tenant = (TenantIdMasterEntity) iterator.next();
				String tenantId = tenant.getTenantId();
				//put each tenant Id in queue
				queue.enqueue(tenantId);
				LOGGER.info("Generator : msg added is " + tenantId);
			}
			watch.stop();
			taskCompletionDao.updateTaskCompletionDetails(watch.getTotalTimeSeconds(),"GenerateMeterBlobSizeWork","Measure blob size for "+listTenant.size()+" tenants");
		} catch (StorageException e) {
			retVal = false;
			LOGGER.error(e.getMessage(), e);
		}
		return retVal;
	}

}
