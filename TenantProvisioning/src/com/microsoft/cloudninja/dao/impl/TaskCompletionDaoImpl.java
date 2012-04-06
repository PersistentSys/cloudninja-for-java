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

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.microsoft.cloudninja.dao.TaskCompletionDao;
import com.microsoft.cloudninja.domainobject.TaskCompletion;

/**
 * Implementation class for Task Completion DAO Interface
 * 
 **/
@Repository("taskCompletionDao")
public class TaskCompletionDaoImpl implements TaskCompletionDao {

	private static final Logger LOGGER = Logger
			.getLogger(TaskCompletionDaoImpl.class);

	@Autowired
	private HibernateTemplate hibernateTemplate;

	/**
	 * Method retrieves the list for completed tasks
	 * 
	 * @returns List : TaskCompletion
	 **/
	@SuppressWarnings("unchecked")
	@Override
	public List<TaskCompletion> getTaskCompletionList() {
		LOGGER.info("Start of TaskCompletionDaoImpl : getTaskCompletionList ");
		List<TaskCompletion> list = null;
		SessionFactory sessionFactory = hibernateTemplate.getSessionFactory();
		Session session = sessionFactory.openSession();
		try {
			org.hibernate.Query query = session.createQuery("from TaskCompletion order by CompletionTime desc");
			query.setFirstResult(0);
			query.setMaxResults(500);
			list = query.list();			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				if (session != null) {
					session.close();
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
			try {
				sessionFactory.close();
			} catch (Exception e) {
				LOGGER.error(e.getMessage(),e);
			}
		}
		LOGGER.info("End of TaskCompletionDaoImpl : getTaskCompletionList ");
		return list;
	}

    @Override
    public void updateTaskCompletionDetails(double elapsedTime,
                                            String taskName, String taskDetails) {
        LOGGER.info("Start of TaskCompletionDaoImpl : updateTaskCompletionDetails ");
        TaskCompletion taskCompletion = new TaskCompletion();
        taskCompletion.setTaskName(taskName);
        taskCompletion.setElapsedTime(elapsedTime);
        taskCompletion.setDetails(taskDetails);
        try{
            hibernateTemplate.saveOrUpdate(taskCompletion);
        } catch(Exception e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("End of TaskCompletionDaoImpl : updateTaskCompletionDetails ");
    }

}
