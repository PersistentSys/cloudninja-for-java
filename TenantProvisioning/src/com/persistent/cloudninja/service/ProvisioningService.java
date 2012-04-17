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
package com.persistent.cloudninja.service;

import java.util.List;

import com.persistent.cloudninja.domainobject.IdentityProviderEntity;
import com.persistent.cloudninja.transferObjects.ProvisioningTenantDTO;

/**
 * Interface for Provision Service.
 *
 */
public interface ProvisioningService {

	/**
	 * Creates a tenant.
	 * 
	 * @param provisioningTenantDTO
	 *            : DTO containing tenant details.
	 * @return
	 */
	public boolean provisionTenant(ProvisioningTenantDTO provisioningTenantDTO);

	/**
	 * Removes a tenant.
	 * 
	 * @param tenantId
	 */
	public void removeTenant(String tenantId);
	
	/**
	 * Returns the list of provisioning status messages for the given tenant id
	 * 
	 * @param tenantId
	 *            the tenant id for which the status is to be determined
	 * @return List of string containing provision status
	 */
	public List<String> getProvisioningStatusList(String tenantId);

	/**
	 * Returns the list of Identity Providers
	 * 
	 * @return IdentityProviderEntity List
	 */
	public List<IdentityProviderEntity> getIdentityProviderList();
	
	/**
	 * Returns tenant Dashboard Access URL.
	 * @param tenantId
	 * @return URL for tenant to login to dashboard
	 */
	public String getDashBoardAccessUrl(String tenantId);
}
