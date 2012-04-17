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
package com.persistent.cloudninja.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.persistent.cloudninja.domainobject.Usage;


public class TenantUsageRowMapper implements RowMapper<Usage>{

	@Override
	public Usage mapRow(ResultSet rs, int rowNum) throws SQLException {
		Usage usage = new Usage();
		usage.setTenantId(rs.getString("TenantId"));
		usage.setYear(rs.getInt("Year"));
		usage.setMonth(rs.getInt("Month"));
		usage.setDay(rs.getInt("Day"));
		usage.setDatabaseSize(rs.getLong("DatabaseSize"));
		usage.setDatabaseBandwidth_Ingress(rs.getLong("DatabaseBandwidth_Ingress"));
		usage.setDatabaseBandwidth_Egress(rs.getLong("DatabaseBandwidth_Egress"));
		usage.setWebAppBandwithUse_CS(rs.getLong("WebAppBandwithUse_CS"));
		usage.setWebAppBandwithUse_SC(rs.getLong("WebAppBandwithUse_SC"));
		usage.setWebAppRequests(rs.getLong("WebAppRequests"));
		usage.setBlobStoreUsage(rs.getLong("BlobStoreUsage"));
		usage.setTotalRequestPacketSize(rs.getLong("TotalRequestPacketSize"));
		usage.setTotalResponsePacketSize(rs.getLong("TotalResponsePacketSize"));
		usage.setTotalStorageTransactions(rs.getLong("TotalStorageTransactions"));
		return usage;
	}
}
