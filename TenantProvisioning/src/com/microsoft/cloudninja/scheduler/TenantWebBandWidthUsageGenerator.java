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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.microsoft.cloudninja.dao.TaskCompletionDao;
import com.microsoft.cloudninja.domainobject.WebLogMeteringBatch;
import com.microsoft.cloudninja.utils.SchedulerSettings;
import com.microsoft.cloudninja.utils.StorageClientUtility;
import com.microsoft.cloudninja.utils.TenantWebBandWidthUsageGeneratorUtility;
import com.microsoft.windowsazure.services.core.storage.StorageException;

/**
 * Generator for tenant web bandwidth usage task. It generates the
 * messages to be processed by processor.
 *
 */
@Component
public class TenantWebBandWidthUsageGenerator extends TaskActivityBase{
    
    private static final Logger LOGGER = Logger.getLogger(TenantWebBandWidthUsageGenerator.class);
    
    @Autowired
    private TaskCompletionDao taskCompletionDao;
    
    @Autowired
    private TenantWebBandWidthUsageGeneratorUtility tenantWebBandWidthUsageGeneratorUtility;

    public TenantWebBandWidthUsageGenerator() throws InvalidKeyException, URISyntaxException {
	//task-queue which is shared between processor and generator.
	super(new TenantWebBandWidthUsageQueue(SchedulerSettings.WebBandWidthUsageQueue, StorageClientUtility.getCloudStorageAccount().createCloudQueueClient()));
    }

    @Override
    public boolean execute() {
        StopWatch watch = new StopWatch();
        LOGGER.info("Start of TenantWebBandWidthUsageGenerator:execute");
        // Generate Queue
        TenantWebBandWidthUsageQueue queue = (TenantWebBandWidthUsageQueue)getWorkQueue();
        boolean returnFlag=true;
        int batchSize = SchedulerSettings.WEB_BANDWIDTH_USAGE_BATCH_SIZE;
        int numBatchesPrepared = 0;
        int webLogMeteringBatchListSize = 0;
	 
         try{
            watch.start();
            List<WebLogMeteringBatch> webLogMeteringBatchList = 
                tenantWebBandWidthUsageGeneratorUtility.retrieveTomcatLogs();
            webLogMeteringBatchListSize = webLogMeteringBatchList.size();
            for (int i = 0; i < webLogMeteringBatchListSize; i = i + batchSize) {

                List<WebLogMeteringBatch> batchToProcess =
                        new ArrayList<WebLogMeteringBatch>();
                for (int j = 0; j < batchSize
                        && ((numBatchesPrepared * batchSize) + j) < webLogMeteringBatchList
                                .size(); j++) {

                    batchToProcess.add(webLogMeteringBatchList.get(j + i));
                }
                // Enqueue
                queue.enqueue(batchToProcess);
                numBatchesPrepared++;
            }

		LOGGER.debug("TenantWebBandWidthUsageGenerator:execute ---> Number of batched processed" + numBatchesPrepared);
		LOGGER.info("End of TenantWebBandWidthUsageGenerator:execute");
		watch.stop();
		taskCompletionDao.updateTaskCompletionDetails(watch.getTotalTimeSeconds(), "GenerateMeterWebAppBandwidthWork", "Measure "+webLogMeteringBatchListSize+" logs in "+numBatchesPrepared+" batches.");
	 }catch (StorageException stgException) {
	     LOGGER.error(stgException.getMessage(), stgException);
	}catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	}
	return returnFlag;
     }
}
