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
package com.microsoft.cloudninja.dao;

import com.microsoft.cloudninja.transferObjects.InstanceHealthKpiValueDTO;
import com.microsoft.cloudninja.transferObjects.InstanceHealthRolesKpiListDTO;
import com.microsoft.cloudninja.transferObjects.TopQueriesDTO;
import com.microsoft.cloudninja.transferObjects.InstanceHealthActiveUserDTO;

/**
 * Dao Interface for instance health.
 */
public interface InstanceHealthDataDao {

    public InstanceHealthActiveUserDTO getActiveUser(String start, String end);
    public TopQueriesDTO getTopQueriesFromDB();
    public InstanceHealthRolesKpiListDTO getKpiRoles(String start, String end);
    public InstanceHealthKpiValueDTO getInstanceKPIsFromDB(String start, String end,
                                                int role, int instance);

}
