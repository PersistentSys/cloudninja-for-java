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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.microsoft.windowsazure.services.queue.client.CloudQueueMessage;
import com.persistent.cloudninja.utils.SchedulerSettings;

/**
 * Schedules the generator and processor for
 * tenant blob size calculation task.
 *
 */
@Component
public class TenantBlobSizeScheduler {
	
	private static final Logger LOGGER = Logger.getLogger(TenantBlobSizeScheduler.class);
	
	@Autowired
	private TenantBlobSizeGenerator tenantBlobSizeGenerator;
	
	@Autowired
	private TenantBlobSizeProcessor tenantBlobSizeProcessor;
	
	@Autowired
	private StorageUtility storageUtility;

	/**
	 * This is a generator task which will listen to taskscheduler-tenantblobsize 
	 * for a message, if message exists then call execute() and delete the message 
	 * from taskscheduler-tenantblobsize 
	 */
	@Scheduled(fixedDelay=SchedulerSettings.GeneratorInterval)
	public void generator() {
		LOGGER.debug("SCH-generator : enter");
		CloudQueueMessage message;
		try {
			message = storageUtility.getMessage("taskscheduler-" +
					SchedulerSettings.BlobStorageSizeTask.toLowerCase());
			if (message != null) {
				LOGGER.debug("SCH-generator : msg is not null");
				if(tenantBlobSizeGenerator.execute()) {
					storageUtility.deleteMessage("taskscheduler-" +
							SchedulerSettings.BlobStorageSizeTask.toLowerCase(), message);
					LOGGER.debug("SCH-generator : msg deleted");
				}
			}
			LOGGER.debug("SCH-generator : exit");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * This will call execute() on processor. 
	 * If execute returns true then delete the message from the 
	 * queue shared between generator and processor.
	 */
	@Scheduled(fixedDelay=SchedulerSettings.ProcessorInterval)
	public void processor() {
		LOGGER.debug("SCH-processor : enter");
		if (tenantBlobSizeProcessor.execute()) {
			try {
				LOGGER.debug("SCH-processor : execute");
				ActivityWorkQueue workQueue = tenantBlobSizeProcessor.getWorkQueue();
				workQueue.complete();
				LOGGER.debug("SCH-processor : complete");
			} catch (StorageException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		LOGGER.debug("SCH-processor : exit");
	}
	
}
