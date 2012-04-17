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
package com.persistent.cloudninja.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.persistent.cloudninja.dao.TenantDataConnectionDao;
import com.persistent.cloudninja.domainobject.TaskList;
import com.persistent.cloudninja.domainobject.TenantDataConnectionEntity;

@Component
public class SessionFactoryConfigurer {

	@Value("#{'${jdbc.driverClassName}'}")
	private String driverClassName;
	
	@Autowired
	private TenantDataConnectionDao tenantDataConnectionDao;
	
	public SessionFactory createSessionFactoryForTenant(String tenantDB){
		TenantDataConnectionEntity dataConnectionEntity =
			tenantDataConnectionDao.find(tenantDB.substring(4));
		StringBuffer strBufServerURL = new StringBuffer("jdbc:sqlserver://");
		strBufServerURL.append(dataConnectionEntity.getServer());
		strBufServerURL.append(";databaseName=");
		strBufServerURL.append(tenantDB);
		strBufServerURL.append(";");
		
		String userName = dataConnectionEntity.getUser();
		String password = dataConnectionEntity.getPassword();
		
		Configuration cfg = new Configuration();
		cfg.addClass(TaskList.class);
		cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
		cfg.setProperty("hibernate.show_sql", "true");
		cfg.setProperty("hibernate.hbm2ddl.auto", "update");
		cfg.setProperty("hibernate.connection.driver_class", driverClassName);
		cfg.setProperty("hibernate.connection.url", strBufServerURL.toString());
		cfg.setProperty("hibernate.connection.username", userName);
		cfg.setProperty("hibernate.connection.password", password);
		
	   return  cfg.buildSessionFactory();
	}
	
}
