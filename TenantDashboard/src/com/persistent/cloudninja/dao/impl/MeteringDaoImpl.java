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

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.persistent.cloudninja.dao.MeteringDao;
import com.persistent.cloudninja.domainobject.Metering;
import com.persistent.cloudninja.exception.SystemException;
import com.persistent.cloudninja.mapper.TenantUsageRowMapper;


@Repository("meteringDao")
public class MeteringDaoImpl implements MeteringDao{
	private static final Logger LOGGER = Logger.getLogger(ManageUsersDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Metering> getMeteringList(String tenantId, int month, int year) throws SystemException {
		List<Metering> list =null;
		try{
		    String query =
			     " SELECT ME.[TenantId] AS TenantId, ME.[Year] AS Year, ME.[Month] AS Month,"
			     + " ME.[Day] AS Day,"
			     + "[DatabaseSize]," 
			     + "[DatabaseBandwidth_Ingress]," 
			     + "[DatabaseBandwidth_Egress]," 
			     + "[WebAppBandwithUse_SC]," 
			     + "[WebAppBandwithUse_CS]," 
			     + "[WebAppRequests]," 
			     + "[BlobStoreUsage],"
			     + "[TotalRequestPacketSize]," 
			     + "[TotalResponsePacketSize]," 
			     + "[TotalStorageTransactions]"  
			+ " FROM ALL_METERING_DAILY_VIEW ME"
			+ " LEFT OUTER JOIN STORAGEMETERING_DAILY_VIEW SM" 
			+ " ON ME.TenantId = SM.Tenant AND ME.YEAR = SM.YEAR AND ME.MONTH = SM.MONTH AND ME.DAY = SM.DAY"
			+ " WHERE TenantId = '"+ tenantId + "' AND"
			+ " ME.[Year] ="+ year + " AND ME.[Month] =" + month +" ";
			    		    
			list=jdbcTemplate.query(query,new TenantUsageRowMapper());
		}catch(DataAccessException exception){
			LOGGER.error(exception);
			throw new SystemException(exception);
		}
		return list;
	}
	

}
