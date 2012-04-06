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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.microsoft.cloudninja.dao.ProvisioningDao;
import com.microsoft.cloudninja.utils.ProvisionUtility;

@Repository("provisioningDao")
public class ProvisioningDaoImpl implements ProvisioningDao {

	@Autowired
	private ProvisionUtility provisionUtility;
	
	@Value("#{'${jdbc.username}'}")
    private String username;
    
    @Value("#{'${jdbc.password}'}")
    private String password;
	
	@Value("#{'${jdbc.masterdb}'}")
    private String master;
	
	/*
	 * method will retrive details from login window 
	 * 
	 * */
	public void getDetails(String tenantId,String tenantLogin,String tenantPassword){
		String TENANT_ID = tenantId;
		String TENANT_LOGIN = tenantLogin;
		String TENANT_PASSWORD = tenantPassword;
		String DB_NAME ="tnt_" + TENANT_ID;
		
		String MASTER_DB = master;
		String MASTER_DB_USER = username;
		String MASTER_DB_PSWD = password;
		
		
		final String CREATE_LOGIN = "CREATE LOGIN " +  TENANT_LOGIN + " WITH password='" + TENANT_PASSWORD + "'";
		final String CREATE_DATABASE = "CREATE DATABASE " + DB_NAME;
		final String CREATE_USER = "CREATE USER " +  TENANT_LOGIN + "User" + " FROM LOGIN " +  TENANT_LOGIN;
		final String DB_DATA_WRITER = "EXEC sp_addrolemember db_datareader, " + TENANT_LOGIN + "User";
		final String DB_DATA_READER = "EXEC sp_addrolemember db_datawriter, " + TENANT_LOGIN + "User";
		final String CREATE_TABLE_TASKLIST = "CREATE TABLE " + "TaskList (" +
																						"TaskId INTEGER, " +
																						"Subject NVARCHAR(128), " +
																						"StartDate DATETIME, "+
																						"DueDate DATETIME, "+
																						"Priority INTEGER, "+
																						"Details NTEXT )";
		
		System.out.println("query-------------------------------------->" + CREATE_LOGIN);
		System.out.println("db_name-------------------------------------->" + MASTER_DB);
		int loginFlag = provisionUtility.executeQuery(CREATE_LOGIN,MASTER_DB,MASTER_DB_USER,MASTER_DB_PSWD);
		System.out.println("loginFlag--->" + loginFlag);
		
		System.out.println("query-------------------------------------->" + CREATE_DATABASE);
		System.out.println("db_name-------------------------------------->" + MASTER_DB);
		int dbFlag = provisionUtility.executeQuery(CREATE_DATABASE,MASTER_DB,MASTER_DB_USER,MASTER_DB_PSWD);
		System.out.println("dbFlag--->" +dbFlag);
		
		System.out.println("query-------------------------------------->" + CREATE_USER);
		System.out.println("db_name-------------------------------------->" + DB_NAME);
		int userFlag = provisionUtility.executeQuery(CREATE_USER,DB_NAME,MASTER_DB_USER,MASTER_DB_PSWD);
		System.out.println("userFlag--->" +userFlag);
		
		System.out.println("query-------------------------------------->" + DB_DATA_WRITER);
		System.out.println("db_name-------------------------------------->" + DB_NAME);
		int dataWriterFlag = provisionUtility.executeQuery(DB_DATA_WRITER,DB_NAME,MASTER_DB_USER,MASTER_DB_PSWD);
		System.out.println("dataWriterFlag--->" +dataWriterFlag);
		
		System.out.println("query-------------------------------------->" + DB_DATA_READER);
		System.out.println("db_name-------------------------------------->" + DB_NAME);
		int dataReaderFlag = provisionUtility.executeQuery(DB_DATA_READER,DB_NAME,MASTER_DB_USER,MASTER_DB_PSWD);
		System.out.println("dataReaderFlag--->" +dataReaderFlag);
		
		System.out.println("query-------------------------------------->" + CREATE_TABLE_TASKLIST);
		System.out.println("db_name-------------------------------------->" + DB_NAME);
		int tasklistFlag = provisionUtility.executeQuery(CREATE_TABLE_TASKLIST,DB_NAME,MASTER_DB_USER,MASTER_DB_PSWD);
		System.out.println("tasklistFlag--->" +tasklistFlag);
		
	}
	

	
}
