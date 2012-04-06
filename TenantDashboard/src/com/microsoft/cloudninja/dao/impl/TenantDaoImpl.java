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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.microsoft.cloudninja.dao.TenantDao;
import com.microsoft.cloudninja.domainobject.Tenant;

/**
 * Implementation class for Tenant DAO.
 *
 */
@Repository("tenantDao")
public class TenantDaoImpl implements TenantDao {
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public Tenant getTenantEntityUsingCertificate(String certThumbprint) {
		Tenant tenantEntity = null;
		List<Tenant> tenantEntitylist = new ArrayList<Tenant>();
		tenantEntitylist = hibernateTemplate.find("from Tenant where Thumbprint=?", certThumbprint);
		if( tenantEntitylist.size() == 1 ) {
			tenantEntity = tenantEntitylist.get(0);
			return tenantEntity;
		} 
		return tenantEntity;
	}
}
