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

import com.microsoft.cloudninja.domainobject.TaskCompletion;

/**
 *  DAO Interface for Task Completion Flow
 *  
 * */
public interface TaskCompletionDao {

	/**
	 * Retrieves Task Completion List
	 * 
	 * @returns List : List<TaskCompletion>
	 */

	public List<TaskCompletion> getTaskCompletionList();

	/**
	 * Updates Task Completion details after the Task completes.
	 * @param elapsedTime The time between start and end of the task.
	 * @param taskName Name of the Task.
	 * @param taskDetails Details related to the task.
	 */
    public void updateTaskCompletionDetails(double elapsedTime,
                                            String taskName, String taskDetails);

}
