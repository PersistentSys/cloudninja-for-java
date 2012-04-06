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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.microsoft.cloudninja.dao.ProvisioningLogDao;
import com.microsoft.cloudninja.domainobject.ProvisioningLogEntity;
import com.microsoft.cloudninja.mapper.ProvisioningLogRowMapper;

/**
 * Implementation class for ProvisioningLog DAO.
 *
 */
@Repository("provisioningLogDao")
public class ProvisioningLogDaoImpl implements ProvisioningLogDao {

	private static final Logger LOGGER = Logger.getLogger(ProvisioningLogDaoImpl.class);
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void add(ProvisioningLogEntity entity) {
		hibernateTemplate.save(entity);
	}

	@Override
	public void deleteAll(String tenantId) {
		try {
			hibernateTemplate.bulkUpdate("delete ProvisioningLogEntity where TenantId=?", tenantId);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

    @Override
    public List<ProvisioningLogEntity> getLogEntityListById(String tenantId) {
        
        List<ProvisioningLogEntity> entityList = null;
        String query = "SELECT TenantId, Task, Message, Created FROM ProvisioningLog WHERE TenantId='"+tenantId+"'";
        entityList = jdbcTemplate.query(query, new ProvisioningLogRowMapper());
        return entityList;
    }
}
