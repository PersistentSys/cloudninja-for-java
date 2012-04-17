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
package com.persistent.cloudninja.domainobject;

/**
 * Holds data related to roles and instances.
 */
public class InstanceHealthRoleInstanceEntity {
    
    private int roleId;
    private int instance;
    private String roleName;
    private String instanceStatus;
    private String instanceName;
    /**
     * @return the instanceStatus
     */
    public String getInstanceStatus() {
        return instanceStatus;
    }
    /**
     * @param instanceStatus the instanceStatus to set
     */
    public void setInstanceStatus(String instanceStatus) {
        this.instanceStatus = instanceStatus;
    }
    /**
     * @return the instanceName
     */
    public String getInstanceName() {
        return instanceName;
    }
    /**
     * @param instanceName the instanceName to set
     */
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
    /**
     * @return the roleId
     */
    public int getRoleId() {
        return roleId;
    }
    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    /**
     * @return the instance
     */
    public int getInstance() {
        return instance;
    }
    /**
     * @param instance the instance to set
     */
    public void setInstance(int instance) {
        this.instance = instance;
    }
    /**
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }
    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
