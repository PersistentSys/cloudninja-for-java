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
package com.persistent.cloudninja.transferObjects;

import com.persistent.cloudninja.domainobject.TaskScheduleEntity;

/**
 * The class represents the data transfer object from edit task scheduled page
 * @author 
 *
 */
public class EditTaskScheduleDTO {
	
	private TaskScheduleEntity taskSchedule;
	private String operationKey;
    /**
     * Get TaskSchedule object
     * @return
     */
	public TaskScheduleEntity getTaskSchedule() {
		return taskSchedule;
	}
    /**
     * Set TaskSchedule entity object
     * @param taskSchedule
     */
	public void setTaskSchedule(TaskScheduleEntity taskSchedule) {
		this.taskSchedule = taskSchedule;
	}
    /**
     * Get OperationKey
     * @return
     */
	public String getOperationKey() {
		return operationKey;
	}
    /**
     * Set OperationKey
     * @param operationKey
     */
	public void setOperationKey(String operationKey) {
		this.operationKey = operationKey;
	}
}
