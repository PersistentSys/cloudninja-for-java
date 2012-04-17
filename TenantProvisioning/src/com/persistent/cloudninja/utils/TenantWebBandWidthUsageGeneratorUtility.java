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
package com.persistent.cloudninja.utils;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microsoft.windowsazure.services.blob.client.CloudBlob;
import com.microsoft.windowsazure.services.blob.client.CloudBlobClient;
import com.microsoft.windowsazure.services.blob.client.CloudBlobContainer;
import com.microsoft.windowsazure.services.blob.client.ListBlobItem;
import com.microsoft.windowsazure.services.core.storage.CloudStorageAccount;
import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.persistent.cloudninja.domainobject.WebLogMeteringBatch;
import com.persistent.cloudninja.service.WebLogMeteringBatchService;


/**
 * 
 * This Utility class is used for generating web bandwidth usage logs
 * which are added in queue for processing
 * 
 *
 */
@Component
public class TenantWebBandWidthUsageGeneratorUtility {

    @Autowired
    private WebLogMeteringBatchService webLogMeteringBatchService;

    private static final Logger LOGGER = Logger.getLogger(TenantWebBandWidthUsageGeneratorUtility.class);

       
    public List<WebLogMeteringBatch> retrieveTomcatLogs() throws URISyntaxException {
	
	    LOGGER.info("Start of WebLogMeteringBatchService:retrieveTomcatLogs ");
	    CloudStorageAccount cloudAcc = StorageClientUtility.getDiagnosticCloudStorageAccount();
	    //CloudStorageAccount cloudAcc = CloudStorageAccount.getDevelopmentStorageAccount(
	    //	    URI.create("http://127.0.0.1:10001/devstoreaccount1"));

	    CloudBlobClient blobClient = cloudAcc.createCloudBlobClient();
	    CloudBlobContainer container;
	    int lastProcessedLine = 0;
	    Date lastProcessedTime = null;
	    int lineNum = 0;
	    List<WebLogMeteringBatch> webLogMeteringBatchResultList = new ArrayList<WebLogMeteringBatch>();
	   
	    try {
		  container = blobClient.getContainerReference(SchedulerSettings.WebTomcatLogsContainer);
		  
		  for (ListBlobItem item : container.listBlobs("", true, null, null, null)) {

			CloudBlob blob = container.getBlockBlobReference(item.getUri().toString());
			LOGGER.info("BLOB NAME:retrieveTomcatLogs :" + blob.getName());
			LOGGER.info("BLOB URI :retrieveTomcatLogs :" +blob.getUri().toString());
			blob.downloadAttributes();
			Date latestModifiedDate = blob.getProperties().getLastModified();
			
			// Check  date stamp and line number from WebLogMeteringBatch
			List<WebLogMeteringBatch> webLogMeteringBatchList = webLogMeteringBatchService.getMeteringBatchServiceDetails(item.getUri().toString());
			
			if(!webLogMeteringBatchList.isEmpty()){
        			// Set lastProcessedTime to largest LastUpdatedTime
        			lastProcessedTime = webLogMeteringBatchList.get(webLogMeteringBatchList.size()-1).getLastUpdatedTime();
        			// Set lastProcessedLine to largest LastLineProcessed
        			lastProcessedLine = webLogMeteringBatchList.get(webLogMeteringBatchList.size()-1).getLastLineProcessed();
			}else{
			        lastProcessedTime = CommonUtility.getDate(CommonUtility.MIN_DATE);
			}

			if (latestModifiedDate.getTime()>lastProcessedTime.getTime()) {
			    // Set line and date
			    WebLogMeteringBatch webLogMeteringBatch = new WebLogMeteringBatch();
			    lastProcessedTime = latestModifiedDate;
			    lineNum = lastProcessedLine;
			    
			    //Create WebLogMeteringBatch object and set the values
			    webLogMeteringBatch.setLastLineProcessed(lineNum);
			    webLogMeteringBatch.setLastUpdatedTime(lastProcessedTime);
			    webLogMeteringBatch.setLogUri(item.getUri().toString());
			    LOGGER.info("WebLogMeteringBatchService:retrieveTomcatLogs LOGURI " + item.getUri().toString());
			    webLogMeteringBatchResultList.add(webLogMeteringBatch);
			}
		    }
		
	    } catch (URISyntaxException e) {
		LOGGER.error("URISyntaxException ", e);
	    } catch (StorageException e) {
		LOGGER.error("StorageException", e);
	    }
	    LOGGER.info("End of WebLogMeteringBatchService:retrieveTomcatLogs ");
	    return webLogMeteringBatchResultList;
   }
}
