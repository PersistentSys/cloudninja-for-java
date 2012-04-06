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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.cloudninja.dao.FileUploadDao;
import com.microsoft.cloudninja.domainobject.Tenant;

@Repository("fileUploadDao")
public class FileUploadDaoImpl implements FileUploadDao{

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Override
	public void insertLogoFileName(MultipartFile logo, String tenantId) {
		try {
			Tenant tenant = hibernateTemplate.get(Tenant.class, tenantId);
			tenant.setLogoFileName("logo.gif");
			hibernateTemplate.saveOrUpdate(tenant);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	

}
