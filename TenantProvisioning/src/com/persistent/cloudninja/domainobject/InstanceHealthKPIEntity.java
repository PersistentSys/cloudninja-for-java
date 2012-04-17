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
 * Holds data from KpiType table in database.
 */
public class InstanceHealthKPIEntity {
    
    private int kpiId;
    private String kpiName;
    /**
     * @return the kpiId
     */
    public int getKpiId() {
        return kpiId;
    }
    /**
     * @param kpiId the kpiId to set
     */
    public void setKpiId(int kpiId) {
        this.kpiId = kpiId;
    }
    /**
     * @return the kpiName
     */
    public String getKpiName() {
        return kpiName;
    }
    /**
     * @param kpiName the kpiName to set
     */
    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }
}
