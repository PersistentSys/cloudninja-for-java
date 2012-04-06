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
package com.microsoft.cloudninja.transferObjects;

import java.util.List;

import com.microsoft.cloudninja.domainobject.TaskCompletion;

/*
 * DTO class for Task Completion Flow .
 * Transfers Result List to be displayed on page
 * 
 * */
public class TaskCompletionDTO {

	private List<TaskCompletion> taskCompletionList;

	private TaskCompletion taskCompletion;

	private String utcDate;

	public List<TaskCompletion> getTaskCompletionList() {
		return taskCompletionList;
	}

	public void setTaskCompletionList(List<TaskCompletion> taskCompletionList) {
		this.taskCompletionList = taskCompletionList;
	}

	public String getUtcDate() {
		return utcDate;
	}

	public void setUtcDate(String utcDate) {
		this.utcDate = utcDate;
	}

	public TaskCompletion getTaskCompletion() {
		return taskCompletion;
	}

	public void setTaskCompletion(TaskCompletion taskCompletion) {
		this.taskCompletion = taskCompletion;
	}

}
