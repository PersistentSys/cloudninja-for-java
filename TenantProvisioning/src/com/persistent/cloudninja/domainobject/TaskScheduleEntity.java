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
package com.persistent.cloudninja.domainobject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The DAO entity class for TaskSchedule table
 * @author 
 *
 */
@Entity
@Table(name = "TaskSchedule")
public class TaskScheduleEntity {

	private String taskId;
	private long frequency;
	private Date nextScheduledStartTime;

	/**
	 * Get the taskId
	 * 
	 * @return
	 */
	@Id
	@Column(name = "TaskId")
	public String getTaskId() {
		return taskId;
	}

	/**
	 * Set taskId
	 * @param taskId
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * Get Frequency
	 * @return
	 */
	@Column(name = "Frequency")
	public long getFrequency() {
		return frequency;
	}
    /**
     * set Frequency
     * @param frequency
     */
	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}

	/**
	 * Get NextScheduledStartTime
	 * @return
	 */
	@Column(name = "NextScheduledStartTime")
	public Date getNextScheduledStartTime() {
		return nextScheduledStartTime;
	}

	/**
	 * Set NextScheduledStartTime
	 * @param nextScheduledStartTime
	 */
	public void setNextScheduledStartTime(Date nextScheduledStartTime) {
		this.nextScheduledStartTime = nextScheduledStartTime;
	}

}
