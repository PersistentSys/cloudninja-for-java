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

import com.persistent.cloudninja.domainobject.TaskScheduleEntity;

/**
 * Interface for operations on taskSchedule table
 * @author 
 *
 */
public interface TaskScheduleService {
    
	/**
	 *  Get the lit of scheduled task 
	 * @return List, of scheduled task 
	 */
	public List<TaskScheduleEntity> getTaskScheduleList();
	
    /**
     * Update the task scheduled 
     * @param taskScheduleEntity The entity mapped tp DB
     */
	public void updateTaskSchedule(TaskScheduleEntity taskScheduleEntity);

}
