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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.microsoft.cloudninja.domainobject.WebLogMeteringBatch;
import com.microsoft.cloudninja.utils.CommonUtility;
import com.microsoft.cloudninja.utils.SchedulerSettings;
import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.microsoft.windowsazure.services.queue.client.CloudQueueClient;


/**
 * Queue shared between generator and processor of
 * tenant web bandwidth usage task.
 *
 */

public class TenantWebBandWidthUsageQueue  extends ActivityWorkQueue {
    
    private static final Logger LOGGER = Logger.getLogger(TenantWebBandWidthUsageQueue.class);
   
    	public TenantWebBandWidthUsageQueue(String queueName, CloudQueueClient storageClient) {
    		super(queueName, storageClient);
    	}

	
    	/*
    	 * Method adds list with comma separated values
    	 * 
    	 * */
    	public void enqueue(List<WebLogMeteringBatch> batchToProcess) throws StorageException {
    	    	LOGGER.info("Start of TenantWebBandWidthUsageQueue:enqueue ");
    	        StringBuilder sb = new StringBuilder();
    	
        	for(int i=0;i<batchToProcess.size();i++){
        	 //Comma separated appended line of each record
           	 sb.append(batchToProcess.get(i).getLogUri()).append(",").append(batchToProcess.get(i).getLastUpdatedTime()).append(",").append(batchToProcess.get(i).getLastLineProcessed()).append(";");
        	}
        	//Remove trailing
        	sb.delete(sb.length()-1,sb.length());
        	super.enqueue(sb.toString());
        	LOGGER.info("End of TenantWebBandWidthUsageQueue:enqueue ");
    	}
    	
    	/*
    	 * Method to dequeue the log elements
    	 * 
    	 * */
    	public List<WebLogMeteringBatch> dequeue() throws StorageException, ParseException{
    	   
    	    LOGGER.info("Start of TenantWebBandWidthUsageQueue:dequeue ");
    	    String message = super.dequeue(SchedulerSettings.MessageVisibilityTimeout);
    	    List<WebLogMeteringBatch> webLogMeteringBatchList = new ArrayList<WebLogMeteringBatch>();
    	   
    	    if(null!=message){
    		String [] logBatchArray = message.split(";");
    		for(int i=0;i<logBatchArray.length;i++){
    		        String [] logEntityArray =logBatchArray[i].split(",");
    		        WebLogMeteringBatch batchElement = new  WebLogMeteringBatch();
    			batchElement.setLogUri(logEntityArray[0]);
    			LOGGER.debug("URI :" + logEntityArray[0]);
    			LOGGER.debug("Date :" + logEntityArray[1]);
    			LOGGER.debug("Line :" + logEntityArray[2]);
    			batchElement.setLastUpdatedTime(CommonUtility.SDF_LOGS_QUEUE.parse(logEntityArray[1]));
    			batchElement.setLastLineProcessed(Integer.parseInt(logEntityArray[2]));
    			webLogMeteringBatchList.add(batchElement);
    		}
    	   }
    	    LOGGER.info("End of TenantWebBandWidthUsageQueue:dequeue ");
	    return webLogMeteringBatchList;
    	}
}
