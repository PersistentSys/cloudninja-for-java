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

import com.microsoft.cloudninja.domainobject.TenantEntity;

/**
 * DAO for Tenant.
 *
 */
public interface TenantDao {
	
	/**
	 * Finds a Tenant Entity.
	 * 
	 * @param tenantId : the tenantId to find.
	 */
	public TenantEntity find(String tenantId);
	
	/**
	 * Deletes the TenantEntity.
	 */
	public void delete(TenantEntity tenantEntity);
	
	/**
	 * Updates the TenantEntity.
	 */
	public void update(TenantEntity tenantEntity);
}
