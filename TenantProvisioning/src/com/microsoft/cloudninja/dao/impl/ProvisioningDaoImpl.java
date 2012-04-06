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
package com.microsoft.cloudninja.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.microsoft.cloudninja.dao.ProvisioningDao;
import com.microsoft.cloudninja.domainobject.DataServerEntity;
import com.microsoft.cloudninja.domainobject.IdentityProviderEntity;
import com.microsoft.cloudninja.domainobject.TenantDataConnectionEntity;
import com.microsoft.cloudninja.transferObjects.ProvisioningTenantDTO;
import com.microsoft.cloudninja.utils.PasswordGenerator;
import com.microsoft.cloudninja.utils.ProvisionUtility;

/**
 * Implementation class for Provisioning DAO.
 *
 */
@Repository("provisioningDao")
public class ProvisioningDaoImpl implements ProvisioningDao {

	private static final Logger LOGGER = Logger.getLogger(ProvisioningDaoImpl.class);
	
	@Autowired
	private ProvisionUtility provisionUtility;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Value("#{'${jdbc.url}'}")
	private String url;
	
	@Value("#{'${jdbc.username}'}")
	private String MASTER_DB_USER;
	
	@Value("#{'${jdbc.password}'}")
	private String MASTER_DB_PSWD;
	
	@Value("#{'${jdbc.masterdb}'}")
	private String MASTER_DB;
	
	@Value("#{'${jdbc.primarydb}'}")
	private String PRIMARY_DB;
	
	private final static int LOCATION_ID = 1;
	
	public void createSubscription(ProvisioningTenantDTO provisioningTenantDTO) throws Exception {
		String TENANT_ID = provisioningTenantDTO.getTenantId();
		final String INSERTINTO_TNT_ID_MASTER = 
			"INSERT INTO TenantIdMaster (TenantId,LocationId) VALUES ('" 
			+ TENANT_ID + "'," + LOCATION_ID + ")";
		final String INSERTINTO_TENANT = 
			"INSERT INTO Tenant (TenantId,CompanyName,LocationId) VALUES ('" 
			+ TENANT_ID + "','" + provisioningTenantDTO.getCompanyName() + "'," + LOCATION_ID + ")"; 
		//create subscription
		provisionUtility.executeQuery(url, INSERTINTO_TNT_ID_MASTER, PRIMARY_DB,
				MASTER_DB_USER, MASTER_DB_PSWD);
		provisionUtility.executeQuery(url, INSERTINTO_TENANT, PRIMARY_DB,
				MASTER_DB_USER, MASTER_DB_PSWD);
	}
	
	public void createLoginAndDB(ProvisioningTenantDTO provisioningTenantDTO) throws Exception {
		String TENANT_ID = provisioningTenantDTO.getTenantId();
		String TENANT_PASSWORD = PasswordGenerator.generatePassword();
		String DB_NAME ="tnt_" + TENANT_ID;
		String TENANT_LOGIN = TENANT_ID;
		
		final String CREATE_LOGIN = "CREATE LOGIN " +  TENANT_LOGIN + " WITH password='" + TENANT_PASSWORD + "'";
		final String CREATE_DATABASE = "CREATE DATABASE " + DB_NAME;
		final String CREATE_USER = "CREATE USER " +  TENANT_LOGIN + "User" + " FROM LOGIN " +  TENANT_LOGIN;
		final String DB_DATA_WRITER = "EXEC sp_addrolemember db_datareader, " + TENANT_LOGIN + "User";
		final String DB_DATA_READER = "EXEC sp_addrolemember db_datawriter, " + TENANT_LOGIN + "User";
		final String CREATE_TABLE_TASKLIST = "CREATE TABLE " + "TaskList (" +
																						"TaskId INTEGER PRIMARY KEY CLUSTERED, " +
																						"Subject NVARCHAR(128), " +
																						"StartDate DATETIME, "+
																						"DueDate DATETIME, "+
																						"Priority INTEGER, "+
																						"Details NTEXT )";
		
		DataServerEntity dataServerEntity = getDataServer(LOCATION_ID);
		String serverUser = dataServerEntity.getUser();
		String serverPassword = dataServerEntity.getPassword();
		StringBuffer strBufServerURL = new StringBuffer("jdbc:sqlserver://");
		strBufServerURL.append(dataServerEntity.getServer());
		strBufServerURL.append(";databaseName=");
		strBufServerURL.append(MASTER_DB);
		strBufServerURL.append(";");
		
		String serverURL = strBufServerURL.toString();
		
		//sql azure provisioning
		provisionUtility.executeQuery(serverURL, CREATE_LOGIN, MASTER_DB,
				serverUser, serverPassword);
		provisionUtility.executeQuery(serverURL, CREATE_DATABASE, MASTER_DB,
				serverUser, serverPassword);
		provisionUtility.executeQuery(serverURL, CREATE_USER, DB_NAME,
				serverUser, serverPassword);
		provisionUtility.executeQuery(serverURL, DB_DATA_WRITER, DB_NAME,
				serverUser, serverPassword);
		provisionUtility.executeQuery(serverURL, DB_DATA_READER, DB_NAME,
				serverUser, serverPassword);
		provisionUtility.executeQuery(serverURL, CREATE_TABLE_TASKLIST, DB_NAME,
				serverUser, serverPassword);
		
		TenantDataConnectionEntity dataConnectionEntity = 
			new TenantDataConnectionEntity(TENANT_ID, dataServerEntity.getServer(),
					DB_NAME, serverUser, serverPassword, "");
		createEntryInTenantDataConnection(dataConnectionEntity);
	}
	
	public void deleteLoginAndTenantDataConnectionEntry(String tenantId) {
		String TENANT_LOGIN = tenantId;
		
		final String DROP_LOGIN = "DROP LOGIN " +  TENANT_LOGIN ;
		
		DataServerEntity dataServerEntity = getDataServer(LOCATION_ID);
		String serverUser = dataServerEntity.getUser();
		String serverPassword = dataServerEntity.getPassword();
		StringBuffer strBufServerURL = new StringBuffer("jdbc:sqlserver://");
		strBufServerURL.append(dataServerEntity.getServer());
		strBufServerURL.append(";databaseName=");
		strBufServerURL.append(MASTER_DB);
		strBufServerURL.append(";");
		
		String serverURL = strBufServerURL.toString();
		
		//sql azure provisioning
		try {
			provisionUtility.executeQuery(serverURL, DROP_LOGIN, MASTER_DB,
					serverUser, serverPassword);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}	
		
		try {
			TenantDataConnectionEntity dataConnectionEntity = 
				findEntryInTenantDataConnection(tenantId);
			deleteEntryInTenantDataConnection(dataConnectionEntity);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	/**
	 * Creates an entry of TenantDataConnectionEntity in TenantDataConnection table.
	 * 
	 * @param dataConnectionEntity
	 */
	private void createEntryInTenantDataConnection(
			TenantDataConnectionEntity dataConnectionEntity) {
		try {
			hibernateTemplate.save(dataConnectionEntity);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Deletes an entry of TenantDataConnectionEntity in TenantDataConnection table.
	 * 
	 * @param dataConnectionEntity
	 */
	public void deleteEntryInTenantDataConnection(
			TenantDataConnectionEntity dataConnectionEntity) {
		try {
			hibernateTemplate.delete(dataConnectionEntity);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Finds an entry of TenantDataConnectionEntity in TenantDataConnection table.
	 * 
	 * @param dataConnectionEntity
	 */
	public TenantDataConnectionEntity findEntryInTenantDataConnection(String tenantId) {
		TenantDataConnectionEntity tenantDataConnectionEntity = new TenantDataConnectionEntity();
		try {
			tenantDataConnectionEntity = hibernateTemplate.get(TenantDataConnectionEntity.class, tenantId);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return tenantDataConnectionEntity;
	}
	
	public void createMember(ProvisioningTenantDTO provisioningTenantDTO) throws Exception {
		String TENANT_ID = provisioningTenantDTO.getTenantId();
		
		final String INSERTINTO_MEMBER = "INSERT INTO Member (TenantId,MemberId,Name,Password,Role,LiveInvitationCode) VALUES ('" + TENANT_ID 
		+ "','" + provisioningTenantDTO.getMemberId() + "','" + provisioningTenantDTO.getMemberName() + "','" 
		+ provisioningTenantDTO.getMemberPassword() + "'," + "'Administrator'" + ",'" + provisioningTenantDTO.getInvitationCode() + "')";
		
		
		//identity provisioning
		provisionUtility.executeQuery(url, INSERTINTO_MEMBER, PRIMARY_DB,
				MASTER_DB_USER, MASTER_DB_PSWD);
	}
	
	/**
	 * Returns the data server corresponding to specified locationId.
	 * 
	 * @param locationId : the locationId for which data server is returned.
	 * @return the DataServerEntity.
	 */
	private DataServerEntity getDataServer(int locationId) {
		DataServerEntity dataServerEntity = null;
		try {
			@SuppressWarnings("unchecked")
			List<DataServerEntity> listDataServerEntity = hibernateTemplate.find(
					"from DataServerEntity where LocationId=?", locationId);
			dataServerEntity = listDataServerEntity.get(0);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return dataServerEntity;
	}

	/**
	 * Returns the IdentityProviderEntity List.
	 * 
	 * @return IdentityProviderEntity List.
	 */
	@SuppressWarnings("unchecked")
	public List<IdentityProviderEntity> getIdentityProviderList() {
		List<IdentityProviderEntity> identityProviderList = new ArrayList<IdentityProviderEntity>();
		try{
			identityProviderList = hibernateTemplate.find("from IdentityProviderEntity");
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		return identityProviderList;
	}
}
