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
package com.persistent.cloudninja.transferObjects;

import java.util.List;

import com.persistent.cloudninja.domainobject.RunningInstanceEntity;

/**
 * Data transfer object for running instance page
 */
public class RunningInstanceDTO {
    private List<RunningInstanceEntity> runningInstance;
    private List<String> roles;
    private List<String> monthDates;
    private List<List<Long>> instanceCountSeries;
    
    /**
     * @return the roles
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * @return the monthDates
     */
    public List<String> getMonthDates() {
        return monthDates;
    }

    /**
     * @param monthDates the monthDates to set
     */
    public void setMonthDates(List<String> monthDates) {
        this.monthDates = monthDates;
    }

    /**
     * @return the instanceCountSeries
     */
    public List<List<Long>> getInstanceCountSeries() {
        return instanceCountSeries;
    }

    /**
     * @param instanceCountSeries the instanceCountSeries to set
     */
    public void setInstanceCountSeries(List<List<Long>> instanceCountSeries) {
        this.instanceCountSeries = instanceCountSeries;
    }

    /**
     * @return the runningInstance
     */
    public List<RunningInstanceEntity> getRunningInstance() {
        return runningInstance;
    }

    /**
     * @param runningInstance the runningInstance to set
     */
    public void setRunningInstance(List<RunningInstanceEntity> runningInstance) {
        this.runningInstance = runningInstance;
    }
}
