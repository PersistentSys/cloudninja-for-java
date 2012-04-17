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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.persistent.cloudninja.dao.RunningInstancesJSONDataDao;
import com.persistent.cloudninja.mapper.RunningInstancesRowMapper;
import com.persistent.cloudninja.transferObjects.RunningInstanceDTO;
/**
 * Implementation of RunningInstancesJSONDataDao
 */
@Repository("runningInstancesJSONDataDao")
public class RunningInstancesJSONDataDaoImpl implements RunningInstancesJSONDataDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public RunningInstanceDTO getRunningInstancesFromDB(String start, String end) {
        String query="WITH hourlyInstances ([Hour], [Day], [Month], [Year], [RoleName], [HourlyMaxInstance])" +
        		" AS (SELECT DATEPART(HH, SnapShotTime) as [Hour], DATEPART(DD, SnapShotTime) AS [Day]" +
        		",DATEPART(MM, SnapShotTime) AS [Month]" +
        		",DATEPART(YY, SnapShotTime) AS [Year]" +
        		",[RoleName]" +
        		",MAX([InstanceCount]) as [HourlyMaxInstance] " +
        		"FROM [RoleInstances]" +
        		" WHERE SnapshotTime >= '"+start+"' AND SNapshotTime <= '"+end+"'" +
        		" GROUP BY [RoleName], DATEPART(HH, SnapShotTime), DATEPART(DD, SnapShotTime), DATEPART(MM, SnapShotTime), DATEPART(YY, SnapShotTime))" +
        		" SELECT  [Day], [Month], [Year], [RoleName], SUM([HourlyMaxInstance]) AS [InstanceCountSum]" +
        		" FROM hourlyInstances" +
        		" GROUP BY [Year],[Month],[Day],   [RoleName]" +
        		" ORDER BY [Year], [Month],[Day],  [RoleName] DESC";
        
        RunningInstanceDTO runningInstanceDTO = new RunningInstanceDTO();
        runningInstanceDTO.setRunningInstance(jdbcTemplate.query(query, new RunningInstancesRowMapper()));
        return runningInstanceDTO;
    }
 }
