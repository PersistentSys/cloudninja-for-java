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
package com.persistent.cloudninja.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.persistent.cloudninja.dao.TaskCompletionDao;
import com.persistent.cloudninja.domainobject.TaskCompletion;
import com.persistent.cloudninja.service.TaskCompletionService;

/*
 * Implementation class for Task Completion Service Interface
 * 
 * */
@Service("taskCompletionService")
public class TaskCompletionServiceImpl implements TaskCompletionService {

	private static final Logger LOGGER = Logger
			.getLogger(TaskCompletionServiceImpl.class);

	@Autowired
	private TaskCompletionDao taskCompletionDao;

	@Override
	public List<TaskCompletion> getTaskCompletionList() {
		LOGGER.info("Start of  TaskCompletionServiceImpl : getTaskCompletionList ");
		List<TaskCompletion> taskCompletionList = new ArrayList<TaskCompletion>();
		taskCompletionList = this.taskCompletionDao.getTaskCompletionList();
		LOGGER.info("End of  TaskCompletionServiceImpl : getTaskCompletionList ");
		return taskCompletionList;
	}

}
