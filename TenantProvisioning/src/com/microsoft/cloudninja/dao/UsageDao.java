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
package com.microsoft.cloudninja.dao;

import java.util.List;

import com.microsoft.cloudninja.domainobject.Usage;
import com.microsoft.cloudninja.transferObjects.UsageDTO;
/**
 * 
 * Interface for Usage DAO
 * @author 
 *
 */
public interface UsageDao {
	
	/**
	 * Get the Metering information 
	 * 
	 * @return The Metering list form database
	 */
	public UsageDTO getDashboardMetering(int month, String year);
	
	/**
	 * Get the Tenant specific Metering 
	 * 
	 * @return The Tenant specific Metering list form database
	 */
	public UsageDTO getTenantSpecificMetering(String tenantId,int month,int year);
	
	/**
	 * Get the list of items to be displayed on metering totals page
	 * @param month
	 * @param year
	 * @return The List
	 */
	public List<Usage> getMeteringTotalsList(String month, String year);
	
}
