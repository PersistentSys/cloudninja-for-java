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
package com.microsoft.cloudninja.transferObjects;

import java.util.List;
/**
 * Data transfer object getting list of values for all KPIs
 */
public class InstanceHealthKpiSeriesDTO {
    
    private List<Double> value;
    private List<String> timestamp;
    private int kpiTypeId;
    
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
    public List<Double> getValue() {
        return value;
    }
    /**
     * @param value the value to set
     */
    public void setValue(List<Double> value) {
        this.value = value;
    }
    /**
     * @return the timestamp
     */
    public List<String> getTimestamp() {
        return timestamp;
    }
    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(List<String> timestamp) {
        this.timestamp = timestamp;
    }
    

}
