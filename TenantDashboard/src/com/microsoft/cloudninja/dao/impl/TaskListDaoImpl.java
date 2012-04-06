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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.microsoft.cloudninja.dao.TaskListDao;
import com.microsoft.cloudninja.domainobject.TaskList;
import com.microsoft.cloudninja.exception.SystemException;
import com.microsoft.cloudninja.utils.SessionFactoryConfigurer;
/**
 * DAO Implementation for manipulating Tenant TaskList.
 */
@Repository("tenantProvisioningDao")
public class TaskListDaoImpl implements TaskListDao {

	private static final Logger LOGGER = Logger.getLogger(ManageUsersDaoImpl.class);

	@Autowired
	private SessionFactoryConfigurer sessionFactoryConfigurer;

	/**
	 * Gets tenant task list.
	 * @param tenantDb is the tenant specific database name
	 * @return list of TaskList that is Tenant task list
	 * @throws SystemException 
	 */
	@SuppressWarnings("unchecked")
	public List<TaskList> getTaskList(String tenantDb) throws SystemException {

		Session session = null;
		SessionFactory sessionfactory = null;
		List<TaskList> list = null;
		try {
			sessionfactory = sessionFactoryConfigurer.createSessionFactoryForTenant(tenantDb);
			session = sessionfactory.openSession();
			session.beginTransaction();

			try {
				Query query = session.createQuery("from TaskList");
				list = query.list();
				session.getTransaction().commit();
			} catch (DataAccessException exception) {
				LOGGER.error(exception);
				throw new SystemException(exception);
			}

		} catch (DataAccessException exception) {
			LOGGER.error(exception);
			throw new SystemException(exception);
		} finally {

			try {
				if (session != null) {
					session.close();
				}
			} catch (DataAccessException exception) {
				LOGGER.error(exception);
				throw new SystemException(exception);
			}
			try {
				sessionfactory.close();
			} catch (DataAccessException exception) {
				LOGGER.error(exception);
				throw new SystemException(exception);
			}

		}
		return list;
	}

	@Override
	public void createList(TaskList taskList, String tenantDb) throws SystemException {

		Session session = null;
		SessionFactory sessionfactory = null;
		try {
			sessionfactory = sessionFactoryConfigurer.createSessionFactoryForTenant(tenantDb);
			session = sessionfactory.openSession();
			session.beginTransaction();
			session.saveOrUpdate(taskList);
			session.getTransaction().commit();
		} catch (DataAccessException exception) {
			LOGGER.error(exception);
			throw new SystemException(exception);
		}
		finally {

			try {
				if (session != null) {
					session.close();
				}
			} catch (DataAccessException exception) {
				LOGGER.error(exception);
				throw new SystemException(exception);
			}

			try {
				sessionfactory.close();
			} catch (DataAccessException exception) {
				LOGGER.error(exception);
				throw new SystemException(exception);
			}

		}

	}

	@Override
	public void deleteTaskList(TaskList taskList, String tenantDb) throws SystemException {

		Session session = null;
		SessionFactory sessionfactory = null;
		try {
			sessionfactory = sessionFactoryConfigurer.createSessionFactoryForTenant(tenantDb);
			session = sessionfactory.openSession();
			session.beginTransaction();
			session.delete(taskList);
			session.getTransaction().commit();
		} catch (DataAccessException exception) {
			LOGGER.error(exception);
			throw new SystemException(exception);
		}

		finally {

			try {
				if (session != null) {
					session.close();
				}
			} catch (DataAccessException exception) {
				LOGGER.error(exception);
				throw new SystemException(exception);
			}

			try {
				sessionfactory.close();
			} catch (DataAccessException exception) {
				LOGGER.error(exception);
				throw new SystemException(exception);
			}

		}
	}

}
