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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.persistent.cloudninja.dao.PartitionStatsAndBWUsageDao;
import com.persistent.cloudninja.domainobject.BandwidthUsageDirection;
import com.persistent.cloudninja.domainobject.DBBandwidthUsageEntity;
import com.persistent.cloudninja.utils.ProvisionConnectionUtility;

/**
 * Implementation class for PartitionStatsAndBWUsageDao.
 *
 */
@Repository("partitionStatsAndBWUsageDao")
public class PartitionStatsAndBWUsageDaoImpl implements
		PartitionStatsAndBWUsageDao {
	
	@Value("#{'${jdbc.url}'}")
	private String url;
	
	@Value("#{'${jdbc.username}'}")
	private String MASTER_DB_USER;
	
	@Value("#{'${jdbc.password}'}")
	private String MASTER_DB_PSWD;
	
	@Value("#{'${jdbc.masterdb}'}")
	private String MASTER_DB;
	
	@Autowired
	private ProvisionConnectionUtility provisionConnectionUtility;

	/**
	 * Get the DB size for a tenant.
	 * 
	 * @param tenantId.
	 * @return the DB size.
	 */
	@Override
	public long getDBSize(String tenantId) {
		long dbSize = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = provisionConnectionUtility.getConnection(url, "tnt_" + tenantId, MASTER_DB_USER, MASTER_DB_PSWD);
			preparedStatement = connection.prepareStatement("SELECT SUM(reserved_page_count)* 8 FROM sys.dm_db_partition_stats");
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				dbSize = resultSet.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			provisionConnectionUtility.closeStatement(preparedStatement);
			provisionConnectionUtility.closeConnection(connection);
		}
		return dbSize;
	}

	/**
	 * Get the database bandwidth usage for databases other than master.
	 * It includes the Egress and Ingress kilobytes.
	 */
	@Override
	public List<DBBandwidthUsageEntity> getBandwidthUsage() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		List<DBBandwidthUsageEntity> listBWUsageEntity = null;
		try {
			connection = provisionConnectionUtility.getConnection(url, MASTER_DB, MASTER_DB_USER, MASTER_DB_PSWD);
			preparedStatement = connection.prepareStatement("SELECT convert(varchar(8),sys.bandwidth_usage.time, 1) "
					+ "as usage_day, database_name, direction, sum(quantity) as kiloBytes " 
                    + "FROM sys.bandwidth_usage where class = 'Internal' and database_name <> '"+MASTER_DB+"' "  
                    + "and convert(varchar(8),sys.bandwidth_usage.time, 1) = convert(varchar(8),getutcdate(), 1) "
                    + "group by convert(varchar(8),sys.bandwidth_usage.time, 1), database_name, direction " 
                    + "order by convert(varchar(8),sys.bandwidth_usage.time, 1), database_name, direction");
			ResultSet resultSet = preparedStatement.executeQuery();
			DBBandwidthUsageEntity dbBandwidthUsageEntity = null;
			listBWUsageEntity = new ArrayList<DBBandwidthUsageEntity>();
			while (resultSet.next()) {
				dbBandwidthUsageEntity = new DBBandwidthUsageEntity();
				dbBandwidthUsageEntity.setUsageDay(resultSet.getString(1));
				dbBandwidthUsageEntity.setDatabaseName(resultSet.getString(2));
				dbBandwidthUsageEntity.setDirection(BandwidthUsageDirection.valueOf(resultSet.getString(3).toUpperCase()));
				dbBandwidthUsageEntity.setKiloBytes(resultSet.getLong(4));
				
				listBWUsageEntity.add(dbBandwidthUsageEntity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			provisionConnectionUtility.closeStatement(preparedStatement);
			provisionConnectionUtility.closeConnection(connection);
		}
		return listBWUsageEntity;
	}

}
