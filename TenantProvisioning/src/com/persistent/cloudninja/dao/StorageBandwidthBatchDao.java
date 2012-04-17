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

import java.util.Collection;

import com.persistent.cloudninja.domainobject.StorageBandwidthBatchEntity;

/**
 * DAO Interface for StorageBandwidthBatch Entity
 *
 */
public interface StorageBandwidthBatchDao {

    /**
     * Gets StorageBandwidthBatch entity for specified URI from database.
     * @param uri
     * @return StorageBandwidthBatchEntity
     */
	public StorageBandwidthBatchEntity getStorageBandwidthBatch(String uri);

    /**
     * Inserts StorageBandwidthBatch entity in database.
     * @param storageBandwidthBatchEntity
     */
	public void insertStorageBandwidthBatch(
		StorageBandwidthBatchEntity storageBandwidthBatchEntity);

	/**
	 * Batch update the StorageBandwidthBatch table.
	 * @param collection List of StorageBandwidthBatchEntity
	 */
	void updateStorageBandwidthBatch(
			Collection<StorageBandwidthBatchEntity> collection);
}
