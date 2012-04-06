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
package com.microsoft.cloudninja.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.restlet.ext.odata.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.microsoft.cloud.accesscontrol.management.IdentityProvider;
import com.microsoft.cloud.accesscontrol.management.MicrosoftCloudAccessControlManagementService;
import com.microsoft.cloud.accesscontrol.management.RelyingParty;
import com.microsoft.cloud.accesscontrol.management.RelyingPartyAddress;
import com.microsoft.cloud.accesscontrol.management.RelyingPartyIdentityProvider;
import com.microsoft.cloud.accesscontrol.management.RelyingPartyKey;
import com.microsoft.cloud.accesscontrol.management.RelyingPartyRuleGroup;
import com.microsoft.cloud.accesscontrol.management.RuleGroup;
import com.microsoft.cloudninja.dao.ProvisioningDao;
import com.microsoft.cloudninja.dao.ProvisioningLogDao;
import com.microsoft.cloudninja.dao.TenantDao;
import com.microsoft.cloudninja.dao.TenantIdMasterDao;
import com.microsoft.cloudninja.dao.TenantProfileDao;
import com.microsoft.cloudninja.domainobject.IdentityProviderEntity;
import com.microsoft.cloudninja.domainobject.ProvisioningLogEntity;
import com.microsoft.cloudninja.domainobject.TenantEntity;
import com.microsoft.cloudninja.domainobject.TenantIdMasterEntity;
import com.microsoft.cloudninja.service.ProvisioningService;
import com.microsoft.cloudninja.transferObjects.ProvisioningTenantDTO;
import com.microsoft.cloudninja.utils.StorageClientUtility;

/**
 * Implementation class for Provision Service.
 *
 */
@Service("provisionService")
public class ProvisioningServiceImpl implements ProvisioningService {

	private static final Logger LOGGER = Logger.getLogger(ProvisioningServiceImpl.class);

	@Autowired
	private ProvisioningDao provisioningDao;
	
	@Autowired
	private TenantProfileDao tenantProfileDao;
	
	@Autowired
	private TenantIdMasterDao tenantIdMasterDao;
	
	@Autowired
	private TenantDao tenantDao;
	
	@Autowired
	private ProvisioningLogDao provisioningLogDao;
	
	@Autowired
	private StorageClientUtility storageClientUtility;
	
	// ACS Details
	@Value("#{'${acs.namespace}'}")
	private String acsNameSpace;

	@Value("#{'${acs.management.username}'}")
	private String acsMgmtUserName;
	
	@Value("#{'${acs.management.password}'}")
	private String acsMgmtPassword;
	
	@Value("#{'${acs.rulegroup}'}")
	private String ruleGroupName;
	
	private String passwordPrefix = "cloudninja%s";
	
	@Value("#{'${acs.returnurl}'}")
	private String returnURL;
	
	@Value("#{'${acs.applicationdomain}'}")
	private String domain;
	
	private MicrosoftCloudAccessControlManagementService managementService;
	
	@Override
	public boolean provisionTenant(ProvisioningTenantDTO provisioningTenantDTO) {
		boolean returnVal = true;
		String tenantId = null;
		try {
			tenantId = provisioningTenantDTO.getTenantId();
		
		provisioningDao.createSubscription(provisioningTenantDTO);
		
		ProvisioningLogEntity provisioningLogEntity = 
			new ProvisioningLogEntity(tenantId, "Database", "Started provisioning database");
		provisioningLogDao.add(provisioningLogEntity);
		provisioningDao.createLoginAndDB(provisioningTenantDTO);
		provisioningLogEntity = 
			new ProvisioningLogEntity(tenantId, "Database", "Finished provisioning database");
		provisioningLogDao.add(provisioningLogEntity);
		
		provisioningLogEntity = 
			new ProvisioningLogEntity(tenantId, "Storage", "Started provisioning storage");
		provisioningLogDao.add(provisioningLogEntity);
			storageClientUtility.createBlobContainers(tenantId);
		provisioningLogEntity = 
			new ProvisioningLogEntity(tenantId, "Storage", "Finished provisioning storage");
		provisioningLogDao.add(provisioningLogEntity);
		
		provisioningLogEntity = 
				new ProvisioningLogEntity(tenantId, "Identity", "Started provisioning identity");
		provisioningLogDao.add(provisioningLogEntity);
		provisioningDao.createMember(provisioningTenantDTO);
			
			managementService = 
				new MicrosoftCloudAccessControlManagementService(acsNameSpace, acsMgmtUserName,
						acsMgmtPassword);
			provisionIdentity(tenantId);
			
		provisioningLogEntity = 
				new ProvisioningLogEntity(tenantId, "Identity", "Finished provisioning identity");
		provisioningLogDao.add(provisioningLogEntity);
		
		TenantEntity tenantEntity = tenantDao.find(tenantId);
		tenantEntity.setStatus("active");
		tenantDao.update(tenantEntity);
		provisioningLogEntity = 
			new ProvisioningLogEntity(tenantId, "System", "Provisioning Complete");
		provisioningLogDao.add(provisioningLogEntity);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			removeTenant(tenantId);
			returnVal = false;
		}
		return returnVal;
	}

	/**
	 * Creates a relying party application for tenant.
	 * 
	 * @param tenantId
	 * @throws Exception
	 */
	private void provisionIdentity(String tenantId) throws Exception {
		createCertificate(tenantId);
		
		TenantEntity tenantEntity = tenantDao.find(tenantId);
		tenantEntity.setThumbprint(getThumbprint(tenantId));
		tenantDao.update(tenantEntity);
		
		RelyingParty relyingParty = createRelyingParty(tenantId);
		assignIdentityProvider(relyingParty);
		assignRuleGroup(relyingParty);
		assignRelyingPartyKeyAndAddresses(relyingParty, tenantId);
	}
	
	/**
	 * Creates a self signed certificate for tenant.
	 * 
	 * @param tenantId
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void createCertificate(String tenantId) throws IOException, InterruptedException {
		String certificateValidity = "365";
		String destination = "";
		
		StringBuffer keytoolPath = new StringBuffer();
		keytoolPath.append(System.getProperty("java.home"));
        if (keytoolPath.length() == 0) {
        	keytoolPath.append(System.getenv("JRE_HOME"));
        }

        destination = keytoolPath.toString();
        destination = destination + File.separator + "lib" + File.separator + "security" + File.separator;
        
        keytoolPath.append(File.separator).append("bin");
        keytoolPath.append(File.separator);
        
        Runtime runtime = Runtime.getRuntime();
        
        String genCertCommand = "cmd /c cd /d \"" + keytoolPath.toString() + "\" && keytool.exe -genkeypair -alias "+ tenantId
			+ " -keystore \"" + destination + tenantId + "_keystore.pfx\" -storepass "
			+ String.format(passwordPrefix, tenantId) + " -validity " + certificateValidity 
			+ " -keyalg RSA -keysize 2048 -storetype pkcs12 -dname \"cn="+ "tnt_" + tenantId + "\"";
        // Creating a Self Signed Certificate
        Process p = runtime.exec(genCertCommand);
        p.waitFor();
	}
	
	/**
	 * Returns the thumbprint of the certificate.
	 * 
	 * @param tenantId
	 * @return the thumbprint.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private String getThumbprint(String tenantId) throws IOException, InterruptedException {
		String destination = "";
		
		// Creating a Self Signed Certificate
		StringBuffer keytoolPath = new StringBuffer();
		keytoolPath.append(System.getProperty("java.home"));
        if (keytoolPath.length() == 0) {
        	keytoolPath.append(System.getenv("JRE_HOME"));
        }

        destination = keytoolPath.toString();
        destination = destination + File.separator + "lib" + File.separator + "security" + File.separator;
        
        keytoolPath.append(File.separator).append("bin");
        keytoolPath.append(File.separator);
		
		Runtime runtime = Runtime.getRuntime();

        String getThumbprint = "cmd /c cd /d \"" + keytoolPath.toString() + "\" && keytool.exe -list -keystore \""
        		+ destination + tenantId + "_keystore.pfx\" -storepass " + String.format(passwordPrefix, tenantId) 
        		+ " -storetype pkcs12";
        Process p = runtime.exec(getThumbprint);
        p.waitFor();
        
        BufferedReader in = new BufferedReader(  
                new InputStreamReader(p.getInputStream()));  
		String line = null;  
		while ((line = in.readLine()) != null) { 
		    if (line.contains("Certificate fingerprint (SHA1)")) {
		    	break;
		    }
		}
		String thumbprint = line.replace("Certificate fingerprint (SHA1)", "");
		thumbprint = thumbprint.trim();
		thumbprint = thumbprint.replaceAll(":", "");
		return thumbprint.trim();
	}
	
	/**
	 * Creates a relying party for tenant.
	 * 
	 * @param tenantId
	 * @return the relying party.
	 * @throws Exception
	 */
	private RelyingParty createRelyingParty(String tenantId) throws Exception {
		RelyingParty relyingParty = new RelyingParty();
		relyingParty.setName(tenantId);
		relyingParty.setDisplayName("CloudNinja Tenant " + tenantId);
		relyingParty.setDescription("CloudNinja Tenant " + tenantId);
		relyingParty.setTokenType("SAML_2_0");
		relyingParty.setTokenLifetime(600);
		relyingParty.setAsymmetricTokenEncryptionRequired(false);
		managementService.addEntity(relyingParty);
		Query<RelyingParty> queryRP =
			managementService.createRelyingPartyQuery("/RelyingParties").filter(
					"Name eq '" + tenantId + "'");
		relyingParty = queryRP.iterator().next();
		return relyingParty;
	}
	
	/**
	 * Assigns the identity providers to the relying party.
	 * 
	 * @param relyingParty
	 * @throws Exception
	 */
	private void assignIdentityProvider(RelyingParty relyingParty) throws Exception {
		// Get IdentityProviders from the OData service
		Query<IdentityProvider> queryIP = 
			managementService.createIdentityProviderQuery("/IdentityProviders");
		RelyingPartyIdentityProvider relyingPartyIdentityProvider = null;
		for (IdentityProvider identityProvider : queryIP) {
			relyingPartyIdentityProvider =
				new RelyingPartyIdentityProvider();
			relyingPartyIdentityProvider.setIdentityProviderId(identityProvider.getId());
			relyingPartyIdentityProvider.setRelyingPartyId(relyingParty.getId());
			managementService.addEntity(relyingPartyIdentityProvider);
		}
	}
	
	/**
	 * Assigns the rule group to the relying party.
	 * 
	 * @param relyingParty
	 * @throws Exception
	 */
	private void assignRuleGroup(RelyingParty relyingParty) throws Exception {
		Query<RuleGroup> queryRG =
			managementService.createRuleGroupQuery("/RuleGroups").filter(
					"Name eq '" + ruleGroupName + "'");
		RuleGroup ruleGroup = queryRG.iterator().next();
		
		RelyingPartyRuleGroup relyingPartyRuleGroup =
			new RelyingPartyRuleGroup();
		relyingPartyRuleGroup.setRuleGroupId(ruleGroup.getId());
		relyingPartyRuleGroup.setRelyingPartyId(relyingParty.getId());
		managementService.addEntity(relyingPartyRuleGroup);
	}
	
	/**
	 * Assigns relying party key and addresses to the relying party.
	 * 
	 * @param relyingParty
	 * @param tenantId
	 * @throws Exception
	 */
	private void assignRelyingPartyKeyAndAddresses(
			RelyingParty relyingParty, String tenantId) throws Exception {
		//create relying party key
		RelyingPartyKey relyingPartyKey =
			new RelyingPartyKey();
		relyingPartyKey.setDisplayName("RPKey" + tenantId);
		relyingPartyKey.setType("X509Certificate");
		relyingPartyKey.setUsage("Signing");
		
		relyingPartyKey.setValue(getCertificateBytes(tenantId));
		relyingPartyKey.setRelyingPartyId(relyingParty.getId());
		//set start time
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		String startDate = dateFormat.format(calendar.getTime());
		relyingPartyKey.setStartDate(dateFormat.parse(startDate));
		
		//set end time
		//The certificate is valid for 1 year.
		calendar.add(Calendar.DAY_OF_YEAR, 364);
		String endDate = dateFormat.format(calendar.getTime());
		relyingPartyKey.setEndDate(dateFormat.parse(endDate));
		
		relyingPartyKey.setPassword(String.format(passwordPrefix, tenantId).getBytes());
		relyingPartyKey.setIsPrimary(true);
		managementService.addEntity(relyingPartyKey);
		
		assignRealm(relyingParty, tenantId);
		assignReturnURL(relyingParty, tenantId);
	}
	
	/**
	 * Returns the byte array of the certificate file.
	 * 
	 * @param tenantId
	 * @return byte array of certificate file.
	 * @throws IOException
	 */
	private byte[] getCertificateBytes(String tenantId) throws IOException {
		StringBuffer keytoolPath = new StringBuffer();
		keytoolPath.append(System.getProperty("java.home"));
        if (keytoolPath.length() == 0) {
        	keytoolPath.append(System.getenv("JRE_HOME"));
        }

        String destination = keytoolPath.toString();
        destination = destination + File.separator + "lib" + File.separator + "security" + File.separator;
		
		File certificateFile = new File(destination + tenantId + "_keystore.pfx");
		byte[] byteArr = new byte[(int) certificateFile.length()];
		FileInputStream ipStream = new FileInputStream(certificateFile);
		ipStream.read(byteArr);
		return byteArr;
	}
		
	/**
	 * Assigns realm to the relying party.
	 * 
	 * @param relyingParty
	 * @param tenantId 
	 * @throws Exception
	 */
	private void assignRealm(RelyingParty relyingParty, String tenantId) throws Exception {
		RelyingPartyAddress realm =
			new RelyingPartyAddress();
		int index = returnURL.lastIndexOf("/");
		String strRealm = returnURL.substring(0, index);
		realm.setAddress(String.format(strRealm, domain, tenantId));
		realm.setEndpointType("Realm");
		realm.setRelyingPartyId(relyingParty.getId());
		managementService.addEntity(realm);
	}
	
	/**
	 * Assigns return URL to the relying party.
	 * 
	 * @param relyingParty
	 * @param tenantId 
	 * @throws Exception
	 */
	private void assignReturnURL(RelyingParty relyingParty, String tenantId) throws Exception {
		RelyingPartyAddress returnURL =
			new RelyingPartyAddress();
		returnURL.setAddress(String.format(this.returnURL, domain, tenantId));
		returnURL.setEndpointType("Reply");
		returnURL.setRelyingPartyId(relyingParty.getId());
		managementService.addEntity(returnURL);
	}

	@Override
	public void removeTenant(String tenantId) {
		// Deleting all provisioning log 
		provisioningLogDao.deleteAll(tenantId);
		//Delete LOGIN and entry in TenantDataConnection
		provisioningDao.deleteLoginAndTenantDataConnectionEntry(tenantId);
		//drop tenant DB and remove members from Member
		tenantProfileDao.deleteTenant(tenantId);
		//remove entry from TenantIdMaster
		TenantIdMasterEntity tenantIdMasterEntity = tenantIdMasterDao.find(tenantId);
		tenantIdMasterDao.delete(tenantIdMasterEntity);
		//remove entry from Tenant
		TenantEntity tenantEntity = tenantDao.find(tenantId);
		tenantDao.delete(tenantEntity);
		//delete public and private blob containers
		storageClientUtility.deleteBlobContainers(tenantId);
	}

    @Override
    public List<String> getProvisioningStatusList(String tenantId) {
        List<String> statusList = new ArrayList<String>();
        List<ProvisioningLogEntity> logEntityList = provisioningLogDao.getLogEntityListById(tenantId);

        if (logEntityList != null) {
            for (int i = 0; i < logEntityList.size(); i++) {
                statusList.add(logEntityList.get(i).getMessage());
            }
        }
        return statusList;
    }

	@Override
    public String getDashBoardAccessUrl(String tenantId) {
    	int index = returnURL.lastIndexOf("/");
		String strRealm = returnURL.substring(0, index);
    	String tenantUrl = String.format(strRealm, domain, tenantId);
    	return tenantUrl;
    }

	@Override
	public List<IdentityProviderEntity> getIdentityProviderList() {
		List<IdentityProviderEntity> identityProviderList = provisioningDao.getIdentityProviderList();
		return identityProviderList;
	}
}
