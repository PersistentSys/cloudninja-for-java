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
package com.persistent.cloudninja.dao;

import java.util.List;

import com.persistent.cloudninja.domainobject.IdentityProviderEntity;
import com.persistent.cloudninja.transferObjects.ProvisioningTenantDTO;

/**
 * Interface for Provisioning DAO.
 *
 */
public interface ProvisioningDao {
	
	
	/**
	 * Creates entry for tenant in TenantIdMaster and Tenant table.
	 * 
	 * @param provisioningTenantDTO : DTO containing tenant details.
	 * @throws Exception 
	 */
	public void createSubscription(ProvisioningTenantDTO provisioningTenantDTO) throws Exception;
	
	
	/**
	 * Performs following steps for a tenant :
	 * 1) Create Login,
	 * 2) create Database,
	 * 3) create User from Login,
	 * 4) add the User to roles ‘db_datareader’ and ‘db_datawriter’,
	 * 5) create table TaskList.
	 * 
	 * @param provisioningTenantDTO : DTO containing tenant details.
	 * @throws Exception 
	 */
	public void createLoginAndDB(ProvisioningTenantDTO provisioningTenantDTO) throws Exception;
	
	/**
	 * Creates an entry for tenant in Member table.
	 * 
	 * @param provisioningTenantDTO : DTO containing tenant details.
	 * @throws Exception 
	 */
	public void createMember(ProvisioningTenantDTO provisioningTenantDTO) throws Exception;

	
	/**
	 * Returns the list of Identity Providers
	 * @return IdentityProviderEntity List
	 */
	public List<IdentityProviderEntity> getIdentityProviderList();

	/**
	 * Performs following steps for a tenant :
	 * 1) Delete Login,
	 * 2) Remove tenant Entry from TenantDataConnection table 
	 * 
	 * @param tenantId : the Tenant ID
	 * @throws Exception 
	 */
	public void deleteLoginAndTenantDataConnectionEntry(String tenantId);
}
