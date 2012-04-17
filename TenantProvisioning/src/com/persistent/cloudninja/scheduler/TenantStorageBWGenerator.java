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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.persistent.cloudninja.dao.StorageBandwidthBatchDao;
import com.persistent.cloudninja.dao.TaskCompletionDao;
import com.persistent.cloudninja.domainobject.StorageBandwidthBatchEntity;
import com.persistent.cloudninja.utils.SchedulerSettings;
import com.persistent.cloudninja.utils.StorageClientUtility;

/**
 * Generator for storage bandwidth calculation task. It generates the
 * messages to be processed by processor.
 *
 */
@Component
public class TenantStorageBWGenerator extends TaskActivityBase {
    
    @Autowired
    private TaskCompletionDao taskCompletionDao;
	
	private static final Logger LOGGER = Logger.getLogger(TenantStorageBWGenerator.class);
	
	@Autowired
	private StorageUtility storageUtility;
	
	@Autowired
	private StorageBandwidthBatchDao storageBandwidthBatchDao;

	public TenantStorageBWGenerator() throws InvalidKeyException, URISyntaxException {
		super(new TenantStorageBWQueue(SchedulerSettings.StorageBandwidthQueue,
				StorageClientUtility.getCloudStorageAccount().createCloudQueueClient()));
	}

	/**
	 * Adds message to storagebandwidthqueue which is shared between processor
	 * and generator.
	 */
	@Override
	public boolean execute() {
	    StopWatch watch = new StopWatch();
		boolean retVal = true;
		try {
		    watch.start();
			LOGGER.debug("In generator");
			TenantStorageBWQueue queue = (TenantStorageBWQueue) getWorkQueue();
			List<StorageBandwidthBatchEntity> batchesToProcess = getLogsToProcess();
			queue.enqueue(batchesToProcess);
			watch.stop();
			taskCompletionDao.updateTaskCompletionDetails(
					watch.getTotalTimeSeconds(), "GenerateStorageBandwidth", 
					"BatchSize = " + batchesToProcess.size());
		} catch (Exception e) {
			retVal = false;
			LOGGER.error(e.getMessage(), e);
		}
		return retVal;
	}

	/**
	 * Gets StorageBandwidthBatchEntity from database to process the logs
	 * @return batchesToProcess : List of StorageBandwidthBatchEntity
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @throws ParseException
	 */
	private List<StorageBandwidthBatchEntity> getLogsToProcess() 
	throws URISyntaxException, StorageException, ParseException {
		Map<String, String> mapAnalyticsLogs = storageUtility.getStorageAnalyticsLogs();
		StorageBandwidthBatchEntity batchEntity = null;
		List<StorageBandwidthBatchEntity> batchesToProcess = new ArrayList<StorageBandwidthBatchEntity>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = null;
		for (String blobURI : mapAnalyticsLogs.keySet()) {
			batchEntity = storageBandwidthBatchDao.getStorageBandwidthBatch(blobURI);
			date = dateFormat.parse(mapAnalyticsLogs.get(blobURI));
			if (null == batchEntity) {
				batchEntity = new StorageBandwidthBatchEntity();
				batchEntity.setLastLineProcessed(0);
				batchEntity.setLastUpdatedTime(date);
				batchEntity.setLogUri(blobURI);
				batchesToProcess.add(batchEntity);
			} else if (batchEntity.getLastUpdatedTime().getTime() != 
				date.getTime()) {
				batchEntity.setLastUpdatedTime(date);
				batchesToProcess.add(batchEntity);
			}
		}
		return batchesToProcess;
	}
}
