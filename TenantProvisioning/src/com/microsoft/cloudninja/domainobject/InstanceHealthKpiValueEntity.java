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
package com.microsoft.cloudninja.domainobject;

/**
 * Holds data from KpiValue table in database.
 */
public class InstanceHealthKpiValueEntity {
    
    private int instance;
    private String timestamp;
    private int kpiTypeId;
    private double value;
    private String kpiTypeName;
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
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }
    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    /**
     * @return the kpiTypeId
     */
    public int getKpiTypeId() {
        return kpiTypeId;
    }
    /**
     * @param kpiTypeId the kpiTypeId to set
     */
    public void setKpiTypeId(int kpiTypeId) {
        this.kpiTypeId = kpiTypeId;
    }
    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }
    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }
    /**
     * @return the kpiTypeName
     */
    public String getKpiTypeName() {
        return kpiTypeName;
    }
    /**
     * @param kpiTypeName the kpiTypeName to set
     */
    public void setKpiTypeName(String kpiTypeName) {
        this.kpiTypeName = kpiTypeName;
    }
}
