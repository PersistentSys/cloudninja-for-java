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
package com.microsoft.cloudninja.service;

import com.microsoft.cloudninja.transferObjects.InstanceHealthKpiValueDTO;
import com.microsoft.cloudninja.transferObjects.InstanceHealthRolesKpiListDTO;
import com.microsoft.cloudninja.transferObjects.TopQueriesDTO;
import com.microsoft.cloudninja.transferObjects.InstanceHealthActiveUserDTO;
/**
 * Service Interface for instances health 
 *
 */
public interface InstanceHealthDataService {

    /**
     * Gets active user count between start and end time.
     * @param start is the start time.
     * @param end is the end time.
     * @return Data transfer object containing active user count.
     */
    public InstanceHealthActiveUserDTO getActiveUserCount(String start,String end);

    /**
     * Gets top 10 queries.
     * @return Data transfer object containing top queries.
     */
    public TopQueriesDTO getTopQueries();

    /**
     * Gets list of all roles and KPI.
     * @param start is the start time.
     * @param end is the end time.
     * @return Data transfer object containing roles and KPI information.
     */
    public InstanceHealthRolesKpiListDTO getKpiRoleList(String start,String end);

    /**
     * Gets the value for KPI's specific to role and instance between start and end time.
     * @param start The start time.
     * @param end The end time.
     * @param role The role ID to filter the data.
     * @param instance The instance to filter the data.
     * @return Data transfer object containing KPI value for specific role and instance.
     */
    public InstanceHealthKpiValueDTO getInstanceKPIs(String start, String end, int role,
                                          int instance);

}
