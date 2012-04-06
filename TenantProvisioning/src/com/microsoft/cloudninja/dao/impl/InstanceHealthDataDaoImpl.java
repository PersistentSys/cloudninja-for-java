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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.microsoft.cloudninja.dao.InstanceHealthDataDao;
import com.microsoft.cloudninja.mapper.InstanceHealthKpiValueRowMapper;
import com.microsoft.cloudninja.mapper.InstanceHealthRoleInstanceRowMapper;
import com.microsoft.cloudninja.mapper.InstanceHealthKpiRowMapper;
import com.microsoft.cloudninja.mapper.TopQueriesRowMapper;
import com.microsoft.cloudninja.mapper.InstanceHealthActiveUserRowMapper;
import com.microsoft.cloudninja.transferObjects.InstanceHealthKpiValueDTO;
import com.microsoft.cloudninja.transferObjects.InstanceHealthRolesKpiListDTO;
import com.microsoft.cloudninja.transferObjects.TopQueriesDTO;
import com.microsoft.cloudninja.transferObjects.InstanceHealthActiveUserDTO;
/**
 * Implementation for Instance Health.
 */
@Repository("instanceHealthJSONDataDao")
public class InstanceHealthDataDaoImpl implements InstanceHealthDataDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * Gets active user count between start and end time.
     * @param start is the start time.
     * @param end is the end time.
     * @return uniqueUserDTO The DTO containing active user count.
     */
    @Override
    public InstanceHealthActiveUserDTO getActiveUser(String start,String end) {
        
        String query ="SELECT TimeInterval, COUNT(*) AS [ActiveUsers] " +
        "       FROM (SELECT DISTINCT TimeInterval, TenantId, MemberId FROM UserActivity) t " +
        "       WHERE TimeInterval > '"+ start +"' AND TimeInterval < '"+ end+"' " +  
        "       GROUP BY TimeInterval";

        InstanceHealthActiveUserDTO uniqueUserDTO = new InstanceHealthActiveUserDTO();
        uniqueUserDTO.setUniqueUser(jdbcTemplate.query(query,new InstanceHealthActiveUserRowMapper()));

        return uniqueUserDTO;
    }

    /**
     * Gets top 10 queries from database.
     * @return topQueriesDTO The DTO containing top queries.
     */
    @Override
    public TopQueriesDTO getTopQueriesFromDB() {
        String query ="SELECT TOP 10 SUM(query_stats.total_worker_time) / SUM(query_stats.execution_count) AS [Avg CPU Time],MIN(query_stats.statement_text) AS [Statement Text] FROM (SELECT QS.*,SUBSTRING(ST.text, (QS.statement_start_offset/2) + 1,((CASE statement_end_offset WHEN -1 THEN DATALENGTH(st.text) ELSE QS.statement_end_offset END - QS.statement_start_offset)/2) + 1) AS statement_text FROM sys.dm_exec_query_stats AS QS CROSS APPLY sys.dm_exec_sql_text(QS.sql_handle) as ST) as query_stats GROUP BY query_stats.query_hash ORDER BY 1 DESC";
        
        TopQueriesDTO topQueriesDTO = new TopQueriesDTO();
        topQueriesDTO.setTopQueries(jdbcTemplate.query(query, new TopQueriesRowMapper()));
        
        return topQueriesDTO;
    }

    /**
     * Gets list of all roles and KPI from database.
     * @param start is the start time.
     * @param end is the end time.
     * @return kpiDTO The DTO containing roles and KPI information.
     */
    @Override
    public InstanceHealthRolesKpiListDTO getKpiRoles(String start,String end) {
        String query = "SELECT DISTINCT KpiValue.RoleId, Instance, [Role].[Name] as " +
        		"RoleName from kpivalue LEFT OUTER JOIN [Role] ON [KpiValue].[RoleId] = [Role].[RoleId] " +
        		"WHERE [TimeStamp] > '"+ start +"'  AND [TimeStamp] < '"+ end+"' "+
        		"ORDER BY KpiValue.RoleId, Instance";
        
        InstanceHealthRolesKpiListDTO kpiDTO = new InstanceHealthRolesKpiListDTO();
        kpiDTO.setKpiRoles(jdbcTemplate.query(query, new InstanceHealthRoleInstanceRowMapper()));
        
        query = "SELECT [KpiTypeId], [Name] FROM KpiType";
        kpiDTO.setKpiType(jdbcTemplate.query(query, new InstanceHealthKpiRowMapper()));
        
        return kpiDTO;
    }

    /**
     * Gets the value for KPI's specific to role and instance between start and end time.
     * @param start The start time.
     * @param end The end time.
     * @param role The role ID to filter the data.
     * @param instance The instance to filter the data.
     * @return instanceKpiDTO The DTO containing KPI value for specific role and instance.
     */
    @Override
    public InstanceHealthKpiValueDTO getInstanceKPIsFromDB(String start, String end,
                                                int role, int instance) {
        String query="SELECT [Instance],[TimeStamp],[KpiTypeId],[Value]" +
        		" FROM KpiValue WHERE [TimeStamp] > '"+ start +"' " +
        		"AND [TimeStamp] < '"+ end+"' AND [RoleId] = "+role+" " +
        		"AND [Instance] = "+instance+" " +
        		"ORDER BY [Instance],[KpiTypeId],[TimeStamp]";
        InstanceHealthKpiValueDTO instanceKpiDTO = new InstanceHealthKpiValueDTO();
        instanceKpiDTO.setInstanceKPI(jdbcTemplate.query(query, new InstanceHealthKpiValueRowMapper()));
        
        return instanceKpiDTO;
    }
}
