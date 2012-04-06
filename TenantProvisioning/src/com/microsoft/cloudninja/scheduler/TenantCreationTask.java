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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.microsoft.cloudninja.dao.TaskCompletionDao;
import com.microsoft.cloudninja.service.ProvisioningService;
import com.microsoft.cloudninja.transferObjects.ProvisioningTenantDTO;
import com.microsoft.cloudninja.utils.SchedulerSettings;
import com.microsoft.cloudninja.utils.StorageClientUtility;
import com.microsoft.windowsazure.services.core.storage.StorageException;

/**
 * Task which listens to TenantCreationQueue. After receiving a message it provisions
 * a tenant specified in the message.
 */
@Component
public class TenantCreationTask extends TaskActivityBase {

	private static final Logger LOGGER = Logger.getLogger(TenantCreationTask.class);
	
	@Autowired
	private ProvisioningService provisioningService;
	
	@Autowired
    private TaskCompletionDao taskCompletionDao;
	
	public TenantCreationTask() throws InvalidKeyException, URISyntaxException {
		super(new TenantCreationQueue(SchedulerSettings.TenantCreationQueue, StorageClientUtility.getCloudStorageAccount().createCloudQueueClient()));
	}
	
	@Override
	public boolean execute() {
	    boolean retval = true;
		try {
		    TenantCreationQueue tntCreationQueue = (TenantCreationQueue) getWorkQueue();
			String message = tntCreationQueue.dequeue(SchedulerSettings.MessageVisibilityTimeout);
			if (message == null) {
				LOGGER.debug("Msg is null");
				retval = false;
			} else {
			    StopWatch watch = new StopWatch();
			    watch.start();
				ProvisioningTenantDTO provisioningTenantDTO = createDTOfromMessage(message);
				boolean tenantCreationSuccess = provisioningService.provisionTenant(provisioningTenantDTO);
				if(tenantCreationSuccess) {
				LOGGER.debug("tenant created :" + provisioningTenantDTO.getTenantId());
				} else {
					LOGGER.debug("tenant creation for tenant ID :" + provisioningTenantDTO.getTenantId()+" failed.");
				}
				watch.stop();
	            taskCompletionDao.updateTaskCompletionDetails(watch.getTotalTimeSeconds(), "ProvisionTenantWork", "Tenant "+provisioningTenantDTO.getTenantId()+" is created.");
			}
		} catch (StorageException e) {
			retval = false;
			LOGGER.error(e.getMessage(), e);
		}
		return retval;
	}

	private ProvisioningTenantDTO createDTOfromMessage(String message) {
		ProvisioningTenantDTO provisioningTenantDTO = new ProvisioningTenantDTO();
		String[] strArr = message.split(",");
		String hexPassword;
		
		provisioningTenantDTO.setTenantId(strArr[0]);
		provisioningTenantDTO.setCompanyName(strArr[1]);
		provisioningTenantDTO.setMemberId(strArr[2]);
		if (strArr.length > 3) {
			//this scenario is for CustomSTS which returns
			//member name and password
		provisioningTenantDTO.setMemberName(strArr[3]);
		// Get the SHA1 encoded password
			String password = strArr[4];
			if (!password.isEmpty()) {
	    hexPassword = getSHA1hexPassword(strArr[4]);
		provisioningTenantDTO.setMemberPassword(hexPassword);
			} else {
				provisioningTenantDTO.setMemberPassword("");
			}
		}
		if (strArr.length > 5) {
			//this scenario is for WindowsLiveID which returns
			//invitation code
			provisioningTenantDTO.setInvitationCode(strArr[5]);
		}
		
		return provisioningTenantDTO;
	}

	/**
	 * This method returns the SHA-1 encoded HexPassword
	 * 
	 * @param password
	 *            the plain text password
	 * @return hexpassword The SHA-1 encoded Hex password.
	 */
	private String getSHA1hexPassword(String password) {

		String hexPassword = "";

		if (password != null && password.length() > 0) {

			try {
				MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
				sha1Digest.update(password.getBytes());
				hexPassword = new String(Hex.encodeHex(sha1Digest.digest())).toUpperCase();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return hexPassword;
	}
}
