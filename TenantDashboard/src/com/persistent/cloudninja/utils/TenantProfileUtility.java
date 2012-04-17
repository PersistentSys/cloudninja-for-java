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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TenantProfileUtility {

	@Value("#{'${jdbc.url}'}")
	private String url;
	
	@Value("#{'${jdbc.username}'}")
	private String username;
	
	@Value("#{'${jdbc.password}'}")
	private String password;
	
	@Value("#{'${jdbc.masterdb}'}")
    private String master;
	
	@Value("#{'${jdbc.primarydb}'}")
    private String cloudNinjaPrimary;
	
	
	public void deleteTenant(String tenantDB) {
		
		Connection conn = null;
		Statement stmt = null;
		final String MASTER_DB = master;
		final String DROP_TENANT = "DROP DATABASE " +  tenantDB;
		int index = url.indexOf("=");
		String connUrl = url.substring(0, index + 1) + MASTER_DB + ";";
				
		try{
		      //JDBC driver
		      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		      //Open connection
		      conn = DriverManager.getConnection(connUrl,username,password);
		      //Query Execution
		      stmt = conn.createStatement();
		      stmt.executeUpdate(DROP_TENANT);
		   }catch(SQLException se){
		      se.printStackTrace();
		   }catch(Exception e){
		       e.printStackTrace();
		   }finally{
			    closeStatement(stmt);
				closeConnection(conn);
		   }
		}
	
	
	public void deleteMemberRecords(String tenantId,String tenantDB) {
		
		Connection conn = null;
		Statement stmt = null;
		final String PRIMARY_DB = cloudNinjaPrimary;
		final String DELETE_MEMBER_TABLE_RECORDS = "DELETE FROM Member WHERE TenantId='" + tenantId + "'";
		int index = url.indexOf("=");
		String connUrl = url.substring(0, index + 1) + PRIMARY_DB + ";";
		
		
		
		try{
		      //JDBC driver
		      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		      //Open connection
		      conn = DriverManager.getConnection(connUrl,username,password);
		      //Query Execution
		      stmt = conn.createStatement();
		      stmt.executeUpdate(DELETE_MEMBER_TABLE_RECORDS);
		   }catch(SQLException se){
		      se.printStackTrace();
		   }catch(Exception e){
		      e.printStackTrace();
		   }finally{
			    closeStatement(stmt);
				closeConnection(conn);
		   }//end try
		}
	
	
	/*
	 * Close Statement
	 */
	public void closeStatement(Statement statement) {

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
		}
	}
	
	
	
	/*
	 * Close Connection
	 */
	public void closeConnection(Connection connection) {

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
		}
	}
}
