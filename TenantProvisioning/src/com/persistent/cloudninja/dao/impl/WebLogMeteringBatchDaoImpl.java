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
package com.persistent.cloudninja.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.persistent.cloudninja.dao.WebLogMeteringBatchDao;
import com.persistent.cloudninja.domainobject.MeteringWebBandWidth;
import com.persistent.cloudninja.domainobject.WebLogMeteringBatch;
import com.persistent.cloudninja.mapper.WebLogMeteringBatchRowMapper;

/**
 * 
 * This is DAO implementation class for WebLogMeteringBatchDao
 * Data will be inserted into Metering Table as well as WebLogMeteringBatch Table
 * Web bandwidth usage is checked using tomcat logs
 *
 */
@Repository("webLogMeteringBatchDao")
public class WebLogMeteringBatchDaoImpl implements WebLogMeteringBatchDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = Logger
	    .getLogger(WebLogMeteringBatchDaoImpl.class);

    @Override
    public List<WebLogMeteringBatch> getMeteringBatchServiceDetails(String uri) {
	List<WebLogMeteringBatch> list = new ArrayList<WebLogMeteringBatch>();
	String query = "SELECT LogUri,LastLineProcessed,LastUpdatedTime FROM WebLogMeteringBatch WHERE LogUri ='"
		+ uri + "'";
	try {
	    list = jdbcTemplate
		    .query(query, new WebLogMeteringBatchRowMapper());
	} catch (Exception e) {
	    LOGGER.error(e);
	}

	return list;
    }

    @Override
    public void insertMeteringDetails(MeteringWebBandWidth meteringWebBandWidth) {
	try {
	    this.jdbcTemplate
		    .update("insert into Metering (TenantId, SnapshotTime,WebAppBandwithUse_CS,WebAppBandwithUse_SC,WebAppRequests) "
			    + "values (?,?,?,?,?)", new Object[] {
			    meteringWebBandWidth.getTenantId(),
			    meteringWebBandWidth.getSnapShotTime(),
			    meteringWebBandWidth.getWebAppBandwithUse_CS(),
			    meteringWebBandWidth.getWebAppBandwithUse_SC(),
			    meteringWebBandWidth.getWebAppRequests() });
	} catch (Exception e) {
	    LOGGER.error(e);
	}

    }

    @Override
    public void insertWebLogMeteringDetails(
	    WebLogMeteringBatch webLogMeteringBatch) {
	try {
	    this.jdbcTemplate
		    .update("insert into WebLogMeteringBatch (BatchID, LastUpdatedTime,LogUri,LastLineProcessed) "
			    + "values (?,?,?,?)", new Object[] {
			    webLogMeteringBatch.getBatchID(),
			    webLogMeteringBatch.getLastUpdatedTime(),
			    webLogMeteringBatch.getLogUri(),
			    webLogMeteringBatch.getLastLineProcessed() });
	} catch (Exception e) {
	    LOGGER.error(e);
	}

    }

}
