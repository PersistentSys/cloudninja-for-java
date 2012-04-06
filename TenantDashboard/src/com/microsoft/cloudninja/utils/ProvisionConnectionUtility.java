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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

@Component
public class ProvisionConnectionUtility {

	/*
	 * ConnectionDetailsCall
	 */
	public int getConnectionDetails(String query, String db_name,
			String db_user, String db_user_pwd) {

		Connection connection = null;
		Statement statement = null;
		int update_flag = 0;

		try {

			connection = getConnection(db_name, db_user, db_user_pwd);
			statement = connection.createStatement();
			update_flag = statement.executeUpdate(query);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStatement(statement);
			closeConnection(connection);
		}
		return update_flag;

	}
	
	

	/*
	 * Retrieve connection
	 */
	public Connection getConnection(String db_name, String db_user,
			String db_user_pwd) {

		Connection connection = null;
		String url = "jdbc:sqlserver://localhost:1433;databaseName=" + db_name
				+ ";";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
					.newInstance();
			connection = DriverManager.getConnection(url, db_user, db_user_pwd);
		} catch (InstantiationException instantiationException) {
			instantiationException.printStackTrace();
		} catch (IllegalAccessException illegalAccessException) {
			illegalAccessException.printStackTrace();
		} catch (ClassNotFoundException classNotFoundException) {
			classNotFoundException.printStackTrace();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return connection;

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
