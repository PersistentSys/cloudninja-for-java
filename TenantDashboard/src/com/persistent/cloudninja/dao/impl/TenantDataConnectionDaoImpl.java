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
package com.persistent.cloudninja.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.persistent.cloudninja.dao.TenantDataConnectionDao;
import com.persistent.cloudninja.domainobject.TenantDataConnectionEntity;

/**
 * Implementation class for TenantDataConnection DAO.
 *
 */
@Repository("tenantDataConnectionDao")
public class TenantDataConnectionDaoImpl implements TenantDataConnectionDao {

	private static final Logger LOGGER = Logger.getLogger(TenantDataConnectionDaoImpl.class);
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public TenantDataConnectionEntity find(String tenantId) {
		TenantDataConnectionEntity connectionEntity = null;
		try {
			connectionEntity = hibernateTemplate.get(TenantDataConnectionEntity.class, tenantId);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return connectionEntity;
	}

}
