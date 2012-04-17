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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.soyatec.windowsazure.blob.internal.RetryPolicies;
import org.soyatec.windowsazure.internal.util.TimeSpan;
import org.soyatec.windowsazure.table.ICloudTableColumn;
import org.soyatec.windowsazure.table.ITable;
import org.soyatec.windowsazure.table.ITableServiceEntity;
import org.soyatec.windowsazure.table.TableServiceContext;
import org.soyatec.windowsazure.table.TableStorageClient;
import org.soyatec.windowsazure.table.internal.CloudTableQuery;
import org.springframework.stereotype.Component;

import com.microsoft.windowsazure.services.blob.client.BlobProperties;
import com.microsoft.windowsazure.services.blob.client.CloudBlob;
import com.microsoft.windowsazure.services.blob.client.CloudBlobClient;
import com.microsoft.windowsazure.services.blob.client.CloudBlobContainer;
import com.microsoft.windowsazure.services.blob.client.CloudPageBlob;
import com.microsoft.windowsazure.services.blob.client.ListBlobItem;
import com.microsoft.windowsazure.services.core.storage.AccessCondition;
import com.microsoft.windowsazure.services.core.storage.CloudStorageAccount;
import com.microsoft.windowsazure.services.core.storage.LoggingOperations;
import com.microsoft.windowsazure.services.core.storage.LoggingProperties;
import com.microsoft.windowsazure.services.core.storage.RetryLinearRetry;
import com.microsoft.windowsazure.services.core.storage.RetryPolicyFactory;
import com.microsoft.windowsazure.services.core.storage.ServiceProperties;
import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.microsoft.windowsazure.services.queue.client.CloudQueue;
import com.microsoft.windowsazure.services.queue.client.CloudQueueClient;
import com.microsoft.windowsazure.services.queue.client.CloudQueueMessage;
import com.persistent.cloudninja.domainobject.WADPerformanceCountersEntity;
import com.persistent.cloudninja.utils.SchedulerSettings;
import com.persistent.cloudninja.utils.StorageClientUtility;

/**
 * Utility class to perform operations on azure storage. 
 *
 */
@Component
public class StorageUtility {
	
	private static final Logger LOGGER = Logger.getLogger(StorageUtility.class);
	
	private CloudStorageAccount storageAccount;
	private CloudQueueClient queueClient;
	private CloudBlobClient blobClient;
	// Diagnostic Tables
	private CloudStorageAccount storageAccountDiagnostic;
	private CloudBlobClient blobClientDiagnostic;
	private TableStorageClient tableClient;
	
	private static final int RETRY_INTERVAL = 5000;
	private static final int RETRY_ATTEMPTS = 3;
	private static final int MAX_DEQUEUE_COUNT = 3;
	private static final int VISIBILITY_TIMEOUT_SEC = 30;
	
	/**
	 *   Initialize parameters in Default Constructor
	 */
	public StorageUtility() {
		//create retry policy
		RetryPolicyFactory retryPolicyFactory = new RetryLinearRetry(RETRY_INTERVAL, RETRY_ATTEMPTS); 
		storageAccount = StorageClientUtility.getCloudStorageAccount();
		queueClient = storageAccount.createCloudQueueClient();
		queueClient.setRetryPolicyFactory(retryPolicyFactory);
		blobClient = storageAccount.createCloudBlobClient();
		blobClient.setRetryPolicyFactory(retryPolicyFactory);
		// Diagnostic tables
		storageAccountDiagnostic = StorageClientUtility.getDiagnosticCloudStorageAccount();
     	blobClientDiagnostic = storageAccountDiagnostic.createCloudBlobClient();
     	blobClientDiagnostic.setRetryPolicyFactory(retryPolicyFactory);
     	tableClient = TableStorageClient.create(false,StorageClientUtility.getDiagnosticAccName(),
     			StorageClientUtility.getDiagnosticAccKey());
     	tableClient.setRetryPolicy(RetryPolicies.retryN(RETRY_ATTEMPTS, TimeSpan.fromSeconds(5)));
	}

	/**
	 * @return the storageAccount
	 */
	public CloudStorageAccount getStorageAccount() {
		return storageAccount;
	}

	/**
	 * @param storageAccount the storageAccount to set
	 */
	public void setStorageAccount(CloudStorageAccount storageAccount) {
		this.storageAccount = storageAccount;
	}

	

	/**
	 * @return the queueClient
	 */
	public CloudQueueClient getQueueClient() {
		return queueClient;
	}


	/**
	 * @param queueClient the queueClient to set
	 */
	public void setQueueClient(CloudQueueClient queueClient) {
		this.queueClient = queueClient;
	}


	/**
	 * @return the blobClient
	 */
	public CloudBlobClient getBlobClient() {
		return blobClient;
	}

	/**
	 * @param blobClient the blobClient to set
	 */
	public void setBlobClient(CloudBlobClient blobClient) {
		this.blobClient = blobClient;
	}

	/**
	 * @return the storageAccountDiagnostic
	 */
	public CloudStorageAccount getStorageAccountDiagnostic() {
		return storageAccountDiagnostic;
	}

	/**
	 * @param storageAccountDiagnostic the storageAccountDiagnostic to set
	 */
	public void setStorageAccountDiagnostic(
			CloudStorageAccount storageAccountDiagnostic) {
		this.storageAccountDiagnostic = storageAccountDiagnostic;
	}
	
	/**
	 * @return the blobClientDiagnostic
	 */
	public CloudBlobClient getBlobClientDiagnostic() {
		return blobClientDiagnostic;
	}

	/**
	 * @param blobClientDiagnostic the blobClientDiagnostic to set
	 */
	public void setBlobClientDiagnostic(CloudBlobClient blobClientDiagnostic) {
		this.blobClientDiagnostic = blobClientDiagnostic;
	}

	/**
	 * @return the tableClient
	 */
	public TableStorageClient getTableClient() {
		return tableClient;
	}

	/**
	 * @param tableClient the tableClient to set
	 */
	public void setTableClient(TableStorageClient tableClient) {
		this.tableClient = tableClient;
	}


	/**
	 * Puts a message in queue.
	 * 
	 * @param queueName
	 * @param messageContent
	 * @throws InvalidKeyException
	 * @throws URISyntaxException
	 * @throws StorageException
	 */
	public void putMessage(String queueName, String messageContent) throws InvalidKeyException, URISyntaxException, StorageException {
		//this queue will be different for diff tasks 
		CloudQueue queue = queueClient.getQueueReference(queueName.toLowerCase());
		// Put message = taskId
		CloudQueueMessage message = new CloudQueueMessage(messageContent);
		queue.addMessage(message);
	}
	
	/**
	 * Retrieves a message from queue.
	 * 
	 * @param queueName
	 * @return : the message contents.
	 * @throws InvalidKeyException
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @throws IOException 
	 */
	public CloudQueueMessage getMessage(String queueName) 
		throws InvalidKeyException, URISyntaxException, StorageException, IOException {
		//this queue will be different for diff tasks 
		CloudQueue queue = queueClient.getQueueReference(queueName.toLowerCase());
		CloudQueueMessage message = queue.retrieveMessage(
				VISIBILITY_TIMEOUT_SEC, null, null);
		//if dequeue count is greater than 3 then delete the message
		if (message != null && message.getDequeueCount() > MAX_DEQUEUE_COUNT) {
			// make the entry of the message as a poison message
			CloudBlobContainer container = 
				blobClient.getContainerReference("poison-messages");
			CloudBlob blob = container.getBlockBlobReference(message.getId());
			InputStream stream = new ByteArrayInputStream(
					message.getMessageContentAsByte());
			blob.upload(stream, stream.available());
			// delete the message
			queue.deleteMessage(message);
			message = null;
		}
		return message;
	}
	
	/**
	 * Deletes a message from queue.
	 * 
	 * @param queueName
	 * @param message
	 * @throws InvalidKeyException
	 * @throws URISyntaxException
	 * @throws StorageException
	 */
	public void deleteMessage(String queueName, CloudQueueMessage message) throws InvalidKeyException, URISyntaxException, StorageException {
		CloudQueue queue = queueClient.getQueueReference(queueName.toLowerCase());
		// If deleting message at the top, need to peek message before deleting in case
		// if the delete operation is being performed on different
		// queue reference than the one used for retrieving.
		queue.peekMessage();
		queue.deleteMessage(message);
	}
	
	/**
	 * Acquires a lease for a task.
	 * 
	 * @param taskId
	 * @return : lease ID if lease is acquired else empty string.
	 * @throws InvalidKeyException
	 * @throws URISyntaxException
	 * @throws StorageException
	 */
	public String acquireLease(String taskId) throws InvalidKeyException, URISyntaxException, StorageException {
		String leaseId = "";
		CloudBlobContainer container = blobClient.getContainerReference("taskscheduler");
		CloudPageBlob pageBlob = container.getPageBlobReference(taskId.toLowerCase());
		if (!pageBlob.exists()) {
			pageBlob.create(0);
		}
		leaseId = pageBlob.acquireLease();
		return leaseId;
	}

	/**
	 * Releases the lease acquired by task.
	 * 
	 * @param taskId : id of the task.
	 * @param leaseId : id of the lease to be released.
	 * @throws InvalidKeyException
	 * @throws URISyntaxException
	 * @throws StorageException
	 */
	public void releaseLease(String taskId, String leaseId) throws InvalidKeyException, URISyntaxException, StorageException {
		CloudBlobContainer container = blobClient.getContainerReference("taskscheduler");
		CloudPageBlob pageBlob = container.getPageBlobReference(taskId.toLowerCase());
		if (pageBlob.exists()) {
			AccessCondition accessCondition = new AccessCondition();
			accessCondition.setLeaseID(leaseId);
			pageBlob.releaseLease(accessCondition);
		}
	}
	
	/**
	 * Calculates the size of all the blobs in the container.
	 * 
	 * @param containerURI
	 * @return Size of blobs in the container.
	 * @throws InvalidKeyException
	 * @throws URISyntaxException
	 * @throws StorageException
	 */
	public long getContainerSize(String containerURI) throws InvalidKeyException, URISyntaxException, StorageException {
		CloudBlobContainer container = blobClient.getContainerReference(containerURI);
		container.createIfNotExist();
		
		CloudBlob blob = null;
		long containerSize = 0;
		for (ListBlobItem blobItem : container.listBlobs()) {
			blob = getBlob(container, blobItem.getUri());
			BlobProperties properties = blob.getProperties();
			containerSize = containerSize + properties.getLength();
		}
		return containerSize;
	}
	
	/**
	 * Returns the CloudBlob reference for the given blob in the container.
	 * 
	 * @param container : blob container.
	 * @param uri : URI of blob.
	 * @return
	 */
	private CloudBlob getBlob(CloudBlobContainer container, URI uri) {
		CloudBlob blob = null;
		String blobURI = uri.toString();
		try {
			blob = container.getBlockBlobReference(blobURI);
			blob.downloadAttributes();
		} catch (StorageException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage(), e);
		}
		try {
			if (blob == null) {
				blob = container.getPageBlobReference(blobURI);
				blob.downloadAttributes();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return blob;
	}

	/**
	 * Returns the Reader Stream for given String uri
	 * 
	 * @param String URI
	 * @return BufferedReader
	 */
	public BufferedReader getReaderStreamForBlob(String uri){
	    BufferedReader in = null;
	    try{
	    	CloudBlob blob = getBlobFromStringURI(uri);  
        	blob.downloadAttributes();
        	OutputStream stream = new ByteArrayOutputStream();
        	blob.download(stream);
        	InputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) stream).toByteArray());
        	in = new BufferedReader(new InputStreamReader(inputStream));
	    }catch (StorageException strgException) {
		   LOGGER.error(strgException.getMessage(), strgException);
	    } catch (IOException IOException) {
		    LOGGER.error(IOException.getMessage(), IOException);
	    }catch (Exception e) {
		    LOGGER.error(e.getMessage(), e);
	    }
	return in;
	}
	
	/**
	 * Returns the Reader Stream for given String URI from Analytics blob
	 * 
	 * @param String URI
	 * @return BufferedReader
	 */
	public BufferedReader getReaderStreamForAnalyticsBlob(String uri){
	    BufferedReader in = null;
	    try{
	    	CloudBlobContainer container = blobClient.getContainerReference(SchedulerSettings.StorageAnalyticsLogsContainer); 
	    	CloudBlob blob = container.getBlockBlobReference(uri);
        	blob.downloadAttributes();
        	OutputStream stream = new ByteArrayOutputStream();
        	blob.download(stream);
        	InputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) stream).toByteArray());
        	in = new BufferedReader(new InputStreamReader(inputStream));
	    }catch (StorageException strgException) {
		   LOGGER.error(strgException.getMessage(), strgException);
	    } catch (IOException IOException) {
		    LOGGER.error(IOException.getMessage(), IOException);
	    }catch (Exception e) {
		    LOGGER.error(e.getMessage(), e);
	    }
	return in;
	}
	
	/**
	 * Returns the Blob for given String URI
	 * 
	 * @param String URI
	 * @return BufferedReader
	 */
	public CloudBlob getBlobFromStringURI(String uri){
	     CloudBlob blob = null;
	     try{
	    	CloudBlobContainer container = blobClientDiagnostic.getContainerReference(SchedulerSettings.WebTomcatLogsContainer);
         	blob = container.getBlockBlobReference(uri);
    	    } catch (URISyntaxException uriSyntaxException) {
    		    LOGGER.error(uriSyntaxException.getMessage(), uriSyntaxException);
    	    } catch (StorageException strgException) {
    		   LOGGER.error(strgException.getMessage(), strgException);
    	    } catch (Exception e) {
    		    LOGGER.error(e.getMessage(), e);
    	    } 
	    
	    return blob;
	 }
	
	
	/**
	 * Returns the latest modified date for blob
	 * 
	 * @param BLOB
	 * @return Latest Modified Date
	 */
	public Date getLastModifiedDateFromBlob(String uri){
	    CloudBlob blob = getBlobFromStringURI(uri);
	    try {
		blob.downloadAttributes();
	    } catch (StorageException e) {
		LOGGER.error(e.getMessage(), e);
	    }
	    return blob.getProperties().getLastModified();
	}
	
	/**
	 * Returns the latest modified date for Analytics blob
	 * 
	 * @param BLOB
	 * @return Latest Modified Date
	 * @throws StorageException 
	 * @throws URISyntaxException 
	 */
	public Date getLastModifiedDateFromAnalyticsBlob(String uri){
		Date date = null;
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
		try {
			CloudBlobContainer container = blobClient.getContainerReference(
					SchedulerSettings.StorageAnalyticsLogsContainer); 
	    	CloudBlob blob = container.getBlockBlobReference(uri);
	    	blob.downloadAttributes();
			String lastTimestamp = blob.getMetadata().get("EndTime");
			date = dateFormat.parse(lastTimestamp);
		    } catch (StorageException e) {
		    	LOGGER.error(e.getMessage(), e);
		    } catch (URISyntaxException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (ParseException e) {
				LOGGER.error(e.getMessage(), e);
			}
	    return date;
	}
	
	/**
	 * Gets the performance counters from WADPerformanceCountersTable.
	 * For performing operations on Azure Table storage, Soyatec APIs are being used,
	 * as the Microsoft SDK version - microsoft-windowsazure-api-0.1.0
	 * doesn't have support for Table.
	 * 
	 * @param lastReadTimeinMs
	 * @param lastBatchTimeinMs
	 * @return array of performance counter entities.
	 */
	public WADPerformanceCountersEntity[] getPerfCounterEntities(long lastReadTimeinMs, long lastBatchTimeinMs) {
		WADPerformanceCountersEntity[] perfArr = null;
		try {
			
			// For populating data on local database
		    //TableStorageClient tableClient = TableStorageClient.create(URI.create("http://127.0.0.1:10002"), true,
	        //      "devstoreaccount1", "Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==");

			ITable table = tableClient.getTableReference("WADPerformanceCountersTable");
			TableServiceContext context = table.getTableServiceContext();
			
			Calendar calendar = GregorianCalendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			String strDate = null;
			
			calendar.setTimeInMillis(lastReadTimeinMs);
			strDate = dateFormat.format(calendar.getTime());
			long ticks = SchedulerSettings.TicksConversionConstant
				+ (dateFormat.parse(strDate).getTime()*10000);
			CloudTableQuery queryLastReadTime = new CloudTableQuery();
			queryLastReadTime.le("PartitionKey", "0" + String.valueOf(ticks));
			
			calendar.setTimeInMillis(lastBatchTimeinMs);
			strDate = dateFormat.format(calendar.getTime());
			ticks = SchedulerSettings.TicksConversionConstant
				+ (dateFormat.parse(strDate).getTime()*10000);
			CloudTableQuery queryLastBatchTime = new CloudTableQuery();
			queryLastBatchTime.lt("PartitionKey", "0" + String.valueOf(ticks));
			
			CloudTableQuery query = CloudTableQuery.and(queryLastReadTime, queryLastBatchTime);
			
			List<ITableServiceEntity> listEntities = context.retrieveEntities(query,
					WADPerformanceCountersEntity.class);
			
			perfArr = new WADPerformanceCountersEntity[listEntities.size()];
			perfArr = listEntities.toArray(perfArr);
			
			for (WADPerformanceCountersEntity entity : perfArr) {
				
				for (ICloudTableColumn column : entity.getValues()) {
					if (column.getName().equalsIgnoreCase("EventTickCount")) {
						entity.setEventTickCount(Long.parseLong(column.getValue()));
					} else if (column.getName().equalsIgnoreCase("DeploymentId")) {
						entity.setDeploymentId(column.getValue());
					} else if (column.getName().equalsIgnoreCase("Role")) {
						entity.setRole(column.getValue());
					} else if (column.getName().equalsIgnoreCase("RoleInstance")) {
						entity.setRoleInstance(column.getValue());
					} else if (column.getName().equalsIgnoreCase("CounterName")) {
						entity.setCounterName(column.getValue());
					} else if (column.getName().equalsIgnoreCase("CounterValue")) {
						entity.setCounterValue(column.getValue());
					} 
				}
			}
			LOGGER.debug("Get Performance counter entities : " + listEntities.size());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return perfArr;
	}

	/**
	 * Creates a queue if not exists.
	 * 
	 * @param queueName
	 * @throws URISyntaxException
	 * @throws StorageException
	 */
	public void initializeQueue(String queueName) throws URISyntaxException, StorageException {
		CloudQueue queue = queueClient.getQueueReference(queueName.toLowerCase());
		queue.createIfNotExist();
	}
	
	/**
	 * Creates a blob container if not exists.
	 * 
	 * @param containerName
	 * @throws URISyntaxException
	 * @throws StorageException
	 */
	public void initializeBlobContainer(String containerName) throws URISyntaxException, StorageException {
		CloudBlobContainer container = blobClient.getContainerReference(containerName);
		container.createIfNotExist();
	}

	public Map<String, String> getStorageAnalyticsLogs() throws URISyntaxException, StorageException {
		Map<String, String> mapLogs = new HashMap<String, String>();
		String endTime = null;
		String blobURI = null;
		CloudBlobContainer container = blobClient.getContainerReference(
				SchedulerSettings.StorageAnalyticsLogsContainer);
		for (ListBlobItem blobItem : container.listBlobs("", true, null, null, null)) {
			blobURI = blobItem.getUri().toString();
			endTime = getEndTimeFromMetadata(container, blobURI);
			mapLogs.put(blobURI, endTime);
		}
		return mapLogs;
	}

	private String getEndTimeFromMetadata(CloudBlobContainer container, String blobURI) 
	throws URISyntaxException, StorageException {
		CloudBlob blob = container.getBlockBlobReference(blobURI);
    	blob.downloadAttributes();
    	return blob.getMetadata().get("EndTime");
	}

	/**
	 * Enables logging for blobs.
	 */
	public void enableLoggingForBlobs() {
		try {
			ServiceProperties properties = blobClient.downloadServiceProperties();
			//Checks if logging is already enabled
			if (properties.getLogging().getLogOperationTypes().isEmpty()) {
				LOGGER.debug("Enabling logging on blob");

				ServiceProperties serviceProperties = new ServiceProperties();

				LoggingProperties logging = new LoggingProperties();
				logging.setVersion("1.0");
				logging.setRetentionIntervalInDays(1);
				EnumSet<LoggingOperations> logOperationTypes = EnumSet.of(LoggingOperations.READ,
						LoggingOperations.WRITE, LoggingOperations.DELETE);
				logging.setLogOperationTypes(logOperationTypes);

				serviceProperties.setLogging(logging);
				blobClient.uploadServiceProperties(serviceProperties);
			}
		} catch (StorageException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
