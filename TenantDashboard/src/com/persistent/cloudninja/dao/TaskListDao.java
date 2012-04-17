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

import com.persistent.cloudninja.domainobject.TaskList;
import com.persistent.cloudninja.exception.SystemException;
/**
 * DAO Interface for manipulating Tenant TaskList.
 *
 */
public interface TaskListDao {

	/**
	 * Gets tenant task list.
	 * @param tenantDb is the tenant specific database name
	 * @return Tenant task list
	 * @throws SystemException
	 */
	public List<TaskList> getTaskList(String tenantDb) throws SystemException;

	/**
	 * Create or Edit tenant task list.
	 * @param tenantDb is the tenant specific database name
	 * @param taskList is the list to be edited
	 * @throws SystemException
	 */
	public void createList(TaskList taskList,String tenantDb) throws SystemException;

	/**
	 * Delete tenant Metering List.
	 * @param tenantDb is the tenant specific database name
	 * @param taskList is the list to be deleted
	 * @throws SystemException
	 */
	public void deleteTaskList(TaskList taskList,String tenantDb) throws SystemException;

}
