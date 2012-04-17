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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.persistent.cloudninja.dao.TaskCompletionDao;
import com.persistent.cloudninja.utils.SchedulerSettings;
import com.persistent.cloudninja.utils.StorageClientUtility;

/**
 * Generator for tenant DB bandwidth usage task. It generates the
 * messages to be processed by processor.
 *
 */
@Component
public class TenantDBBandwidthGenerator extends TaskActivityBase {
    
    @Autowired
    private TaskCompletionDao taskCompletionDao;

	private static final Logger LOGGER = Logger.getLogger(TenantDBBandwidthGenerator.class);

	public TenantDBBandwidthGenerator() throws InvalidKeyException, URISyntaxException {
		//tenantdbbandwidthqueue which is shared between processor and generator.
		super(new TenantDBBandwidthQueue(SchedulerSettings.DBBandwidthQueue, StorageClientUtility.getCloudStorageAccount().createCloudQueueClient()));
	}
	
	@Override
	public boolean execute() {
	    StopWatch watch = new StopWatch();
		boolean retVal = true;
		try {
		    watch.start();
			LOGGER.debug("In generator");
			TenantDBBandwidthQueue queue = (TenantDBBandwidthQueue) getWorkQueue();
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			String currentTime = dateFormat.format(calendar.getTime());
			queue.enqueue(currentTime);
			LOGGER.info("Generator : msg added is " + currentTime);
			watch.stop();
			taskCompletionDao.updateTaskCompletionDetails(watch.getTotalTimeSeconds(), "GenerateMeterTenantDBBandwidthWork", "");
		} catch (StorageException e) {
			retVal = false;
			LOGGER.error(e.getMessage(), e);
		}
		return retVal;
	}

}
