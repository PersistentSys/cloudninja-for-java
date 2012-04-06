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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.soyatec.windowsazure.table.ITable;
import org.soyatec.windowsazure.table.ITableServiceEntity;
import org.soyatec.windowsazure.table.TableServiceContext;
import org.soyatec.windowsazure.table.TableStorageClient;
import org.soyatec.windowsazure.table.internal.CloudTableQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microsoft.cloudninja.domainobject.PerfCounterEntity;
import com.microsoft.cloudninja.domainobject.WADDirectoriesTableEntity;
import com.microsoft.cloudninja.domainobject.WADLogsTableEntity;
import com.microsoft.cloudninja.domainobject.WebLogMeteringBatch;
import com.microsoft.cloudninja.service.WebLogMeteringBatchService;
import com.microsoft.cloudninja.utils.CommonUtility;
import com.microsoft.cloudninja.utils.SchedulerSettings;
import com.microsoft.cloudninja.utils.SchedulerSettings.DiagnosticsTables;
import com.microsoft.cloudninja.utils.StorageClientUtility;
import com.microsoft.windowsazure.services.blob.client.CloudBlob;
import com.microsoft.windowsazure.services.blob.client.CloudBlobClient;
import com.microsoft.windowsazure.services.blob.client.CloudBlobContainer;
import com.microsoft.windowsazure.services.blob.client.ListBlobItem;
import com.microsoft.windowsazure.services.core.storage.CloudStorageAccount;
import com.microsoft.windowsazure.services.core.storage.StorageException;

/*
 *  This utility class is used for cleanup tasks for following entities . 
 *  1. Cleaning the older Blobs of Tomcat logs which have been already processed . 
 *  2. Cleaning the entity records for following Diagnostic tables 
 *     a.WADPerformanceCountersTable 
 *     b.WADLogsTable 
 *     c.WADDirectoriesTable 
 * 
 * */
@Component
public class AzureStorageCleanupTask implements IActivity{
    
    private static final Logger LOGGER = Logger.getLogger(AzureStorageCleanupTask.class);
    
    @Autowired
    private WebLogMeteringBatchService webLogMeteringBatchService;

    @Override
    public boolean execute() {
	boolean result = true;
	try {
	    	// Delete Diagnostic table entries
	    	for(DiagnosticsTables table:DiagnosticsTables.values()){
	    	    deleteDiagnosticTableEntities(table.name());
	    	}
		// Delete Logs
	    	deletePurgedTomcatLogs();
	    	
	}catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	    result=false;
	}
	return result;
    }
    
    
    /*
     * 
     * Delete Blobs for purged logs 
     * 
     * */
    private void deletePurgedTomcatLogs() {
	
	    LOGGER.info("Start of method deletePurgedTomcatLogs ");
	    CloudStorageAccount cloudAcc = StorageClientUtility.getDiagnosticCloudStorageAccount();
	    CloudBlobClient blobClient = cloudAcc.createCloudBlobClient();
	    CloudBlobContainer container;
	    int deletedLogs=0;
	    Date lastProcessedTime =null;
	   
	    try {
		  container = blobClient.getContainerReference(SchedulerSettings.WebTomcatLogsContainer);
		  for (ListBlobItem item : container.listBlobs("", true, null, null, null)) {
			CloudBlob blob = container.getBlockBlobReference(item.getUri().toString());
			blob.downloadAttributes();
			Date latestModifiedDate = blob.getProperties().getLastModified();
			
			// Check  date stamp from WebLogMeteringBatch
			List<WebLogMeteringBatch> webLogMeteringBatchList = webLogMeteringBatchService.getMeteringBatchServiceDetails(item.getUri().toString());
			// Set lastProcessedTime to largest LastUpdatedTime
			if(!webLogMeteringBatchList.isEmpty()){
			    lastProcessedTime = webLogMeteringBatchList.get( webLogMeteringBatchList.size() - 1).getLastUpdatedTime();
			}else{
			    lastProcessedTime = CommonUtility.getDate(CommonUtility.MIN_DATE);
			}
			
			if (latestModifiedDate.getTime()<=lastProcessedTime.getTime()) {
			     //Delete Logs
			     blob.delete();
			     deletedLogs++;
			     LOGGER.debug("Deleted BLOB :" + blob.getUri().toString());
			}
		    }
		     LOGGER.debug("Total DeletedLogs :" + deletedLogs);
		
	    } catch (URISyntaxException e) {
		LOGGER.error("URISyntaxException ", e);
	    } catch (StorageException e) {
		LOGGER.error("StorageException", e);
	    }
	    LOGGER.info("End of method deletePurgedTomcatLogs ");
   }


    /*
     * 
     * Delete Entities from Diagnostic table 
     * 
     * @param String : Diagnostic Table name
     * 
     * 
     * */
    private void deleteDiagnosticTableEntities(String tableName) {
	LOGGER.info("Start of method deleteDiagnosticTableEntities ");
	try{
	        TableStorageClient tableClient = TableStorageClient.create(false,StorageClientUtility.getDiagnosticAccName(), StorageClientUtility.getDiagnosticAccKey());
	 
        	ITable diagnstTable = tableClient.getTableReference(tableName);
        	
        	if (diagnstTable.isTableExist()) {
        	    LOGGER.info("Diagnostic Table exists : " + diagnstTable.getTableName());
        	
        	TableServiceContext context = diagnstTable.getTableServiceContext();
        	
        	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        	long retentionTime = cal.getTimeInMillis()- SchedulerSettings.DiagnosticsTablesRetentionPeriod;
        	long ticks = SchedulerSettings.TicksConversionConstant + (retentionTime*10000);
        		
        	CloudTableQuery query = new CloudTableQuery();
        	query.le("PartitionKey", "0" + ticks);
        	
        	//WADPerformanceCountersTable
        	if(tableName.equalsIgnoreCase(DiagnosticsTables.WADPerformanceCountersTable.name())){
        	    	LOGGER.info("Start of Deletion of  WADPerformanceCountersTable");
        	        int deletedRows=0;
        	        while(!context.retrieveEntities(query,PerfCounterEntity.class).isEmpty()){
	        	        List<ITableServiceEntity> listEntities = context.retrieveEntities(query,PerfCounterEntity.class);
	                	PerfCounterEntity[] perfArr = new PerfCounterEntity[listEntities.size()];
	                	perfArr = listEntities.toArray(perfArr);
	                	LOGGER.debug("WADPerformanceCountersTable: List size :" + perfArr.length);
	                	if(perfArr.length>0){
                        	for (PerfCounterEntity entity : perfArr) {
                        	    // Delete Entity
                        	    context.deleteEntity(entity);
                        	    deletedRows ++;
                        	    LOGGER.debug("Deleting rows WADPerformanceCountersTable: Rownum :" + deletedRows);
                        	}
	                	}
        	        }
                	LOGGER.debug("Total Rows Deleted for WADPerformanceCountersTable:" + deletedRows);
                	LOGGER.info("End of Deletion of  WADPerformanceCountersTable");
            	}
        	//WADLogsTable
        	else if(tableName.equalsIgnoreCase(DiagnosticsTables.WADLogsTable.name())){
        	    	LOGGER.info("Start of Deletion of  WADLogsTable");
        	        int deletedRows=0;
        	        while(!context.retrieveEntities(query,WADLogsTableEntity.class).isEmpty()){
	        	        List<ITableServiceEntity> listEntities = context.retrieveEntities(query,WADLogsTableEntity.class);
	        	        WADLogsTableEntity[] wadLogsfArr = new WADLogsTableEntity[listEntities.size()];
	        	        wadLogsfArr = listEntities.toArray(wadLogsfArr);
	                	LOGGER.debug("WADLogsTable:  List size :" + wadLogsfArr.length);
	                	if(wadLogsfArr.length>0){
                        	for (WADLogsTableEntity entity : wadLogsfArr) {
                        	    // Delete Entity
                        	    context.deleteEntity(entity);
                        	    deletedRows ++;
                        	    LOGGER.debug("Deleting rows WADLogsTable : Rownum :" + deletedRows);
                        	}
	                	}
        	        }
                	LOGGER.debug("Total Rows Deleted for WADLogsTable:" + deletedRows);
                	LOGGER.info("End of Deletion of  WADLogsTable");
        	}
        	//WADDirectoriesTable
        	else if(tableName.equalsIgnoreCase(DiagnosticsTables.WADDirectoriesTable.name())){
        	    	LOGGER.info("Start of Deletion of  WADDirectoriesTable");
        	        int deletedRows=0;
        	        while(!context.retrieveEntities(query,WADDirectoriesTableEntity.class).isEmpty()){
	        	        List<ITableServiceEntity> listEntities = context.retrieveEntities(query,WADDirectoriesTableEntity.class);
	        	        WADDirectoriesTableEntity[] wadDirArr = new WADDirectoriesTableEntity[listEntities.size()];
	        	        wadDirArr = listEntities.toArray(wadDirArr);
	                	LOGGER.debug(" WADDirectoriesTable: List size :" + wadDirArr.length);
	                	if(wadDirArr.length>0){
                        	for (WADDirectoriesTableEntity entity : wadDirArr) {
                        	    // Delete Entity
                        	    context.deleteEntity(entity);
                        	    deletedRows ++;
                        	    LOGGER.debug("Deleting rows WADDirectoriesTable : Rownum :" + deletedRows);
                        	}
	                	}
        	        }
                	LOGGER.debug("Total Rows Deleted for WADDirectoriesTable:" + deletedRows);
                	LOGGER.info("End of Deletion of  WADDirectoriesTable");
        	}
           }else{
                  LOGGER.debug("Diagnostic Table : "  + diagnstTable.getTableName() + " does not exist" );
           }
	}catch(Exception e){
	    LOGGER.error(e.getMessage(), e);
	}
	LOGGER.info("End of method deleteDiagnosticTableEntities ");
    }
}
