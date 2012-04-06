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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProvisionUtility {

	@Autowired
	private ProvisionConnectionUtility provisionConnectionUtility;
	

	/*
	 * Execute Query for ProvisionUtility
	 */
	public int executeQuery(String url, String query, String db_name, String db_user,
			String db_user_pwd) throws Exception {

		int update_flag = 0;		
		// Method call for ProvisonDetails
		update_flag = provisionConnectionUtility.getConnectionDetails(url, query,
				db_name, db_user, db_user_pwd);

		return update_flag;

	}

}
