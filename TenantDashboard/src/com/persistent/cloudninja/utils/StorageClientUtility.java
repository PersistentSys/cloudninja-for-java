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

import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.windowsazure.services.blob.client.CloudBlob;
import com.microsoft.windowsazure.services.blob.client.CloudBlobClient;
import com.microsoft.windowsazure.services.blob.client.CloudBlobContainer;
import com.microsoft.windowsazure.services.core.storage.CloudStorageAccount;
import com.microsoft.windowsazure.services.core.storage.RetryLinearRetry;
import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.microsoft.windowsazure.services.queue.client.CloudQueue;
import com.microsoft.windowsazure.services.queue.client.CloudQueueClient;
import com.microsoft.windowsazure.services.queue.client.CloudQueueMessage;

@Component
public class StorageClientUtility {
	
	private static final Logger LOGGER = Logger.getLogger(StorageClientUtility.class);
	
	@Value("#{'${storage.endpoint}'}")
	private String endptProtocol;
	
	@Value("#{'${storage.accName}'}")
	private String accName;
	
	@Value("#{'${storage.accKey}'}")
	private String accKey;
	
	@Value("#{'${storage.tntsPrefix}'}")
	private String storageTntsPrefix;
	
	private CloudStorageAccount cloudAcc;
	    
	private CloudBlobClient blobClient;
	
	private CloudQueueClient queueClient;
	
	

	/**
	 * @return the cloudAcc
	 */
	public CloudStorageAccount getCloudAcc() {
		return cloudAcc;
	}

	/**
	 * @param cloudAcc the cloudAcc to set
	 */
	public void setCloudAcc(CloudStorageAccount cloudAcc) {
		this.cloudAcc = cloudAcc;
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
	 * Upload logo file to a blob. 
	 * 
	 * @param logo : the logo file to upload.
	 * @param tenantId
	 * @return
	 */
	public String uploadLogoBlob(MultipartFile logo, String tenantId) {
		StringBuffer urlBuffer = new StringBuffer();
		try {
			CloudBlobClient blobClient = getBlobClientForStorageAccount();
		
			CloudBlobContainer container = blobClient.getContainerReference("tnts-" + tenantId.toLowerCase());
			container.createIfNotExist();
		
			//upload blob
		
			CloudBlob blob = container.getBlockBlobReference("logo.gif");
		
			InputStream stream = logo.getInputStream();
			blob.upload(stream, stream.available());
			
			urlBuffer.append("https://");
			urlBuffer.append(accName);
			urlBuffer.append(storageTntsPrefix);
			urlBuffer.append(tenantId.toLowerCase());
			urlBuffer.append("/logo.gif");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return urlBuffer.toString();
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
		CloudQueueClient queueClient = getQueueClientForStorageAccount();
		
		//this queue will be different for diff tasks 
		CloudQueue queue = queueClient.getQueueReference(queueName);
		queue.createIfNotExist();
		CloudQueueMessage message = new CloudQueueMessage(messageContent);
		queue.addMessage(message);
	}
	
	
	/*
	 *  Method added to check if Cloud Storage Account is already created.
	 *  If it is not present a new account will be created.
	 *  
	 *  @return CloudStorageAccount
	 *  
	 * */
	public CloudStorageAccount getCloudStorageAccount(){
		
			try {
				if(null== cloudAcc){
				cloudAcc = CloudStorageAccount.parse(
						"DefaultEndpointsProtocol=" + endptProtocol +
						";AccountName=" + accName +
						";AccountKey=" + accKey);
				}
			} catch (InvalidKeyException invalidKeyException) {
				LOGGER.error("InvalidKeyException", invalidKeyException);
			} catch (URISyntaxException uriSyntaxException) {
				LOGGER.error("URISyntaxException", uriSyntaxException);
			}
		
		return cloudAcc;
	}
	
	/*
	 *  Method added to check if CloudBlobClient is already present.
	 *  If it is not present a new CloudBlobClient will be created. 
	 *  
	 *  @return CloudBlobClient
	 *  
	 * */
	public CloudBlobClient getBlobClientForStorageAccount() {
		if(null==blobClient){
			cloudAcc = getCloudStorageAccount();
			blobClient = cloudAcc.createCloudBlobClient();
			blobClient.setRetryPolicyFactory(new RetryLinearRetry(5000, 3));
		}
		return blobClient;
	}
	
	/*
	 *  Method added to check if CloudQueueClient is already present.
	 *  If it is not present a new CloudQueueClient will be created.
	 *  
	 *  @return CloudQueueClient
	 *  
	 * */
	public CloudQueueClient getQueueClientForStorageAccount() {
		if(null==queueClient){
			cloudAcc = getCloudStorageAccount();
			queueClient = cloudAcc.createCloudQueueClient();
			queueClient.setRetryPolicyFactory(new RetryLinearRetry(5000, 3));
		}
		return queueClient;
	}
}
