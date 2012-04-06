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
package com.microsoft.cloudninja.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.cloudninja.dao.TaskListDao;
import com.microsoft.cloudninja.domainobject.TaskList;
import com.microsoft.cloudninja.exception.SystemException;
import com.microsoft.cloudninja.service.TenantTaskListService;

@Service("taskListService")
public class TenantTaskListServiceImpl implements TenantTaskListService {

	@Autowired
	private TaskListDao tenantTasklistDao;

	@Override
	public List<TaskList> showList(String tenantDb) throws SystemException {
		List<TaskList> viewTaskList  = null;
		viewTaskList = tenantTasklistDao.getTaskList(tenantDb);
		if (viewTaskList !=null) {
		    Iterator<TaskList> it = viewTaskList.iterator();
		    TaskList taskList;
		    while(it.hasNext()) {
		        taskList = it.next();
		        formatDateString(taskList);
		    }
		}
		return viewTaskList;
	}

	@Override
	public void createTaskList(TaskList taskList,String tenantDb) throws SystemException {
		tenantTasklistDao.createList(taskList,tenantDb);
	}

	@Override
	public void deleteList(TaskList taskList,String tenantDb) throws SystemException {
		tenantTasklistDao.deleteTaskList(taskList,tenantDb);
	}
    /**
     * Method to format the start and due date to yyyy/MM/dd format
     * @param taskList
     */
	private void formatDateString(TaskList taskList) {
	    String startDateStr="";
	    String dueDateStr="";
	    startDateStr = taskList.getStartDate();
	    dueDateStr = taskList.getDueDate();
	    
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            Date startDate = sdf.parse(startDateStr);
            taskList.setStartDate(sdf1.format(startDate));
            
            Date dueDate = sdf.parse(dueDateStr);
            taskList.setDueDate(sdf1.format(dueDate));
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
	}
}
