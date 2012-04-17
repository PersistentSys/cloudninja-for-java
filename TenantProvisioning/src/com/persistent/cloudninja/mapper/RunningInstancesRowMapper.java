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

import com.persistent.cloudninja.domainobject.RunningInstanceEntity;
/**
 * Maps runningInstanceEntity to Database roleInstances 
 */
public class RunningInstancesRowMapper implements RowMapper<RunningInstanceEntity> {

    @Override
    public RunningInstanceEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        RunningInstanceEntity runningInstance = new RunningInstanceEntity();
        runningInstance.setDay(rs.getInt("Day"));
        runningInstance.setMonth(rs.getInt("Month"));
        runningInstance.setYear(rs.getInt("Year"));
        runningInstance.setRoleName(rs.getString("RoleName"));
        runningInstance.setInstanceCountSum(rs.getInt("InstanceCountSum"));
        return runningInstance;
    }

}
