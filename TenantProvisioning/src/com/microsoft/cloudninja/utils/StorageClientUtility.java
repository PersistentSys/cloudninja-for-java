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
package com.microsoft.cloudninja.utils;

import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microsoft.cloudninja.scheduler.TenantBlobSizeGenerator;
import com.microsoft.windowsazure.services.blob.client.BlobContainerPermissions;
import com.microsoft.windowsazure.services.blob.client.BlobContainerPublicAccessType;
import com.microsoft.windowsazure.services.blob.client.CloudBlobClient;
import com.microsoft.windowsazure.services.blob.client.CloudBlobContainer;
import com.microsoft.windowsazure.services.core.storage.CloudStorageAccount;
import com.microsoft.windowsazure.services.core.storage.RetryLinearRetry;
import com.microsoft.windowsazure.services.core.storage.StorageException;

@Component
public class StorageClientUtility {
    
    private static final Logger LOGGER = Logger.getLogger(TenantBlobSizeGenerator.class);
	
    /**
     * Getting the values for the Static variable endptProtocol injected from bean of StorageClientUtility in componentContext.xml
     */
    private static String endptProtocol;
    /**
     * Getting the values for the Static variable accName injected from bean of StorageClientUtility in componentContext.xml
     */
    private static String accName;
    /**
     * Getting the values for the Static variable accKey injected from bean of StorageClientUtility in componentContext.xml
     */
    private static String accKey;
    /**
     * Getting the values for the Static variable diagnosticAccName injected from bean of StorageClientUtility in componentContext.xml
     */
    private static String diagnosticAccName;
    /**
     * Getting the values for the Static variable diagnosticAccKey injected from bean of StorageClientUtility in componentContext.xml
     */
    private static String diagnosticAccKey;
   
    
    /**
     *  CloudStorageAccount
     */
    private CloudStorageAccount cloudAcc;
    
    /**
     * CloudBlobClient
     */
    private CloudBlobClient blobClient;
    
    
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
	 * @return the diagnosticAccName
	 */
	public static String getDiagnosticAccName() {
		return diagnosticAccName;
	}


	/**
	 * @return the diagnosticAccKey
	 */
	public static String getDiagnosticAccKey() {
		return diagnosticAccKey;
	}

	/**
     * @return the endptProtocol
     */
    public static String getEndptProtocol() {
        return endptProtocol;
    }


    /**
     * @return the accName
     */
    public static String getAccName() {
        return accName;
    }


    /**
     * @return the accKey
     */
    public static String getAccKey() {
        return accKey;
    }


    /**
     * @param defaultEndpointsProtocol the defaultEndpointsProtocol to set
     */
    @Autowired(required=true)
    public void setDefaultEndpointsProtocol(String defaultEndpointsProtocol) {
        StorageClientUtility.endptProtocol = defaultEndpointsProtocol;
    }


    /**
     * @param accountName the accountName to set
     */
    @Autowired(required=true)
    public void setAccountName(String accountName) {
        StorageClientUtility.accName = accountName;
    }


    /**
     * @param accountKey the accountKey to set
     */
    @Autowired(required=true)
    public void setAccountKey(String accountKey) {
        StorageClientUtility.accKey = accountKey;
    }
    
    
    /**
	 * @param diagnosticAccName the diagnosticAccName to set
	 */
    @Autowired(required=true)
	public void setDiagnosticAccName(String diagnosticAccountName) {
		StorageClientUtility.diagnosticAccName = diagnosticAccountName;
	}
	
	
	/**
	 * @param diagnosticAccKey the diagnosticAccKey to set
	 */
    @Autowired(required=true)
	public void setDiagnosticAccKey(String diagnosticAccountKey) {
		StorageClientUtility.diagnosticAccKey = diagnosticAccountKey;
	}
	
    
    public static CloudStorageAccount getCloudStorageAccount(){
        CloudStorageAccount cloudAcc = null;
        try {
            
            cloudAcc = CloudStorageAccount
            .parse("DefaultEndpointsProtocol="+StorageClientUtility.endptProtocol+";"
            + "AccountName="+StorageClientUtility.accName+";"
            + "AccountKey="+StorageClientUtility.accKey+"");
        } catch (URISyntaxException uriSyntaxException) {
            LOGGER.error("URISyntaxException", uriSyntaxException);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return cloudAcc;
       
   }
    
    public static CloudStorageAccount getDiagnosticCloudStorageAccount(){
        CloudStorageAccount cloudAcc = null;
        try {
            
            cloudAcc = CloudStorageAccount
            .parse("DefaultEndpointsProtocol="+StorageClientUtility.endptProtocol+";"
            + "AccountName="+StorageClientUtility.diagnosticAccName+";"
            + "AccountKey="+StorageClientUtility.diagnosticAccKey+"");
        } catch (URISyntaxException uriSyntaxException) {
            LOGGER.error("URISyntaxException", uriSyntaxException);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return cloudAcc;
       
   }


	
	public void createBlobContainers(String tntId) throws StorageException, URISyntaxException {
		    LOGGER.debug("In createBlobcontainers()");
			    
		    CloudBlobClient blobClient = getBlobClientForStorageAccount();
		    
			CloudBlobContainer publicContainer = blobClient.getContainerReference("tnts-" + tntId.toLowerCase());
			publicContainer.createIfNotExist();
			BlobContainerPermissions permissions = new BlobContainerPermissions();
			permissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
			publicContainer.uploadPermissions(permissions);
			
			CloudBlobContainer privateContainer = blobClient.getContainerReference("tntp-" + tntId.toLowerCase());
			privateContainer.createIfNotExist();
			permissions = new BlobContainerPermissions();
			permissions.setPublicAccess(BlobContainerPublicAccessType.OFF);
			privateContainer.uploadPermissions(permissions);	
	}
	
	public void deleteBlobContainers(String tenantId) {
		try {
		
			CloudBlobClient blobClient = getBlobClientForStorageAccount();
			
			CloudBlobContainer publicContainer = blobClient.getContainerReference("tnts-" + tenantId.toLowerCase());
			publicContainer.deleteIfExists();
			
			CloudBlobContainer privateContainer = blobClient.getContainerReference("tntp-" + tenantId.toLowerCase());
			privateContainer.deleteIfExists();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 *  Method added to check if Cloud Storage Account
	 *  and Blob Client is already created . 
	 *  @return CloudBlobClient
	 * */
	public CloudBlobClient getBlobClientForStorageAccount(){
			if(null== cloudAcc){
				cloudAcc =getCloudStorageAccount();
			}
			if(null==blobClient){
				blobClient = cloudAcc.createCloudBlobClient();
				blobClient.setRetryPolicyFactory(new RetryLinearRetry(5000, 3));
			}
		return blobClient;
	}
}
