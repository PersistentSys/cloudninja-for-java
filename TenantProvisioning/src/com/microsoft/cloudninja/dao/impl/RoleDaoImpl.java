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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.microsoft.cloudninja.dao.RoleDao;
import com.microsoft.cloudninja.domainobject.RoleEntity;

/**
 * Implementation for RoleDao.  
 *
 */
@Repository("roleDao")
public class RoleDaoImpl implements RoleDao {
	
	private static final Logger LOGGER = Logger.getLogger(RoleDaoImpl.class);

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public RoleEntity findByRoleName(String roleName) {
		RoleEntity roleEntity = null;
		try {
			@SuppressWarnings("unchecked")
			List<RoleEntity> listRoleEntities =
				hibernateTemplate.find("from RoleEntity where Name=?", roleName);
			if (listRoleEntities.size() != 0) {
				roleEntity = listRoleEntities.get(0);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return roleEntity;
	}

	@Override
	public void add(RoleEntity roleEntity) {
		try {
			hibernateTemplate.save(roleEntity);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	


}
