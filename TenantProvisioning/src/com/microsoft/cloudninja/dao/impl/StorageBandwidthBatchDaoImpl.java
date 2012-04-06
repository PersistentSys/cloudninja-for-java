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

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.microsoft.cloudninja.dao.StorageBandwidthBatchDao;
import com.microsoft.cloudninja.domainobject.StorageBandwidthBatchEntity;

/**
 * 
 * This is DAO implementation class for StorageBandwidthBatchDao
 *
 */
@Repository("storageBandwidthBatchDao")
public class StorageBandwidthBatchDaoImpl implements StorageBandwidthBatchDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    private static final Logger LOGGER = Logger
	    .getLogger(StorageBandwidthBatchDaoImpl.class);

	@Override
	public StorageBandwidthBatchEntity getStorageBandwidthBatch(String uri) {
		StorageBandwidthBatchEntity entity = null;
		try {
			entity = hibernateTemplate.get(StorageBandwidthBatchEntity.class, uri);
		} catch (Exception e) {
		    LOGGER.error(e.getMessage(),e);
		}
		return entity;
	}

	@Override
	public void insertStorageBandwidthBatch(
			StorageBandwidthBatchEntity storageBandwidthBatchEntity) {
		try {
		    hibernateTemplate.saveOrUpdate(storageBandwidthBatchEntity);
		} catch (Exception e) {
		    LOGGER.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void updateStorageBandwidthBatch(
			Collection<StorageBandwidthBatchEntity> storageBandwidthBatchEntity) {
		try {
		    hibernateTemplate.saveOrUpdateAll(storageBandwidthBatchEntity);
		} catch (Exception e) {
		    LOGGER.error(e.getMessage(),e);
		}
	}

}
