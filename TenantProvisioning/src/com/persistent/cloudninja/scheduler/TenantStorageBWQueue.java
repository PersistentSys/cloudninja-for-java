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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.microsoft.windowsazure.services.queue.client.CloudQueueClient;
import com.persistent.cloudninja.domainobject.StorageBandwidthBatchEntity;
import com.persistent.cloudninja.utils.CommonUtility;
import com.persistent.cloudninja.utils.SchedulerSettings;


/**
 * Queue shared between generator and processor of
 * tenant storage bandwidth calculation task.
 *
 */
public class TenantStorageBWQueue extends ActivityWorkQueue {

	public TenantStorageBWQueue(String queueName, CloudQueueClient storageClient) {
		super(queueName, storageClient);
	}
	
	/**
	 * Method adds list with comma separated values
	 * 
	 * */
	public void enqueue(List<StorageBandwidthBatchEntity> batchToProcess) throws StorageException {
        StringBuffer strBuffer = new StringBuffer();
		for (int i=0; i<batchToProcess.size(); i++) {
			 //Comma separated appended line of each record
			strBuffer.append(batchToProcess.get(i).getLogUri()).append(
		   			 ",").append(batchToProcess.get(i).getLastUpdatedTime()).append(
   					 ",").append(batchToProcess.get(i).getLastLineProcessed()).append(";");
		}
		super.enqueue(strBuffer.toString());
	}

	/**
	 * Method to dequeue the batch elements
	 * 
	 * */
	public List<StorageBandwidthBatchEntity> dequeue() throws StorageException, ParseException{
		String message = super.dequeue(SchedulerSettings.MessageVisibilityTimeout);
		List<StorageBandwidthBatchEntity> storageBandwidthBatchEntities = 
			new ArrayList<StorageBandwidthBatchEntity>();
		StorageBandwidthBatchEntity batchElement = null;
		
		if (null != message) {
			String [] logBatchArray = message.split(";");
			for (int i = 0; i < logBatchArray.length; i++) {
				String [] logEntityArray = logBatchArray[i].split(",");
				batchElement = new  StorageBandwidthBatchEntity();
				batchElement.setLogUri(logEntityArray[0]);
				batchElement.setLastUpdatedTime(CommonUtility.SDF_LOGS_QUEUE.parse(logEntityArray[1]));
				batchElement.setLastLineProcessed(Integer.parseInt(logEntityArray[2]));
				storageBandwidthBatchEntities.add(batchElement);
			}
		}
		return storageBandwidthBatchEntities;
	}
}
