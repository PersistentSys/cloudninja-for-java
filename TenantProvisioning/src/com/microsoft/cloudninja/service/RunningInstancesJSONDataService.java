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

import java.util.List;

import com.microsoft.cloudninja.domainobject.InstanceHealthRoleInstanceEntity;
import com.microsoft.cloudninja.transferObjects.RunningInstanceDTO;

/**
 * Service Interface for get running instances 
 *
 */
public interface RunningInstancesJSONDataService {

    /**
     * Gets running instances between start and end date. 
     * @param start The start date
     * @param end The end date
     * @return Data transfer object containing running instance object
     */
    public RunningInstanceDTO getRunningInstances(String start, String end);

    /**
     * Gets the Role Name, Instance Name and Instance Status from deployment.
     * @return List of InstanceHealthRoleInstanceEntity
     */
    public List<InstanceHealthRoleInstanceEntity> getRoleAndInstanceFromDeployment();
}
