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
package com.microsoft.cloudninja.mapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.microsoft.cloudninja.domainobject.ProvisioningLogEntity;
/**
 * 
 * Class for mapping rows to ProvisioningLog table
 *
 */
public class ProvisioningLogRowMapper implements RowMapper<ProvisioningLogEntity>{

    @Override
    public ProvisioningLogEntity mapRow(ResultSet rs, int rownum)
                                                                 throws SQLException {
        
        ProvisioningLogEntity provisioningLogEntity = new ProvisioningLogEntity();
        provisioningLogEntity.setTenantId(rs.getString("TenantId"));
        provisioningLogEntity.setTask(rs.getString("Task"));
        provisioningLogEntity.setMessage(rs.getString("Message"));
        provisioningLogEntity.setCreated(rs.getDate("Created"));
        return provisioningLogEntity;
    }
}
