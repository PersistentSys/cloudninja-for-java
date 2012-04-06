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

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.microsoft.cloudninja.dao.TenantIdMasterDao;
import com.microsoft.cloudninja.domainobject.TenantIdMasterEntity;

@Repository("tenantIdMasterDao")
public class TenantIdMasterDaoImpl implements TenantIdMasterDao {

	private static final Logger LOGGER = Logger.getLogger(TenantIdMasterDaoImpl.class);
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TenantIdMasterEntity> getAllTenants() {
		List<TenantIdMasterEntity> listTenantIdMaster = hibernateTemplate.find("from TenantIdMasterEntity");
		return listTenantIdMaster;
	}

	@Override
	public TenantIdMasterEntity find(String tenantId) {
		TenantIdMasterEntity tenantIdMasterEntity = null;
		try {
			tenantIdMasterEntity = hibernateTemplate.get(TenantIdMasterEntity.class, tenantId);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return tenantIdMasterEntity;
	}

	@Override
	public void delete(TenantIdMasterEntity tenantIdMasterEntity) {
		try {
			hibernateTemplate.delete(tenantIdMasterEntity);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
