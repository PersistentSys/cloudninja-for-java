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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.microsoft.cloudninja.dao.KpiValueTempDao;
import com.microsoft.cloudninja.domainobject.KpiValueTempEntity;

/**
 * Implementation of KpiValueTemp DAO.
 *
 */
@Repository("kpiValueTempDao")
public class KpiValueTempDaoImpl implements KpiValueTempDao {
	
	private static final Logger LOGGER = Logger.getLogger(KpiValueTempDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Override
	public Date getMaxTimestamp() {
		Date timestamp = null;
		try {
			timestamp = jdbcTemplate.queryForObject(
					"SELECT DISTINCT MAX (TimeStamp) AS LastTimeStamp FROM KpiValueTemp", Date.class);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return timestamp;
	}

	@Override
	public void addAll(List<KpiValueTempEntity> listKpiValueTempEntities) {
		try {
			hibernateTemplate.saveOrUpdateAll(listKpiValueTempEntities);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public void executeProcessKpiValues() {
		try {
			jdbcTemplate.call(new CallableStatementCreator() {
				
				@Override
				public CallableStatement createCallableStatement(Connection con)
						throws SQLException {
					CallableStatement cs = con.prepareCall("{ call ProcessKpiValues() }");
					return cs;
				}
			}, new ArrayList<SqlParameter>());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
