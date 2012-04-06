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
package com.microsoft.cloudninja.model;

import java.util.List;

/**
 * 
 *  Model Class representing metering data to be displayed on metering totals
 *
 */
public class MeteringTotalsChartModel {
    
    private String kpi;
    private List<String> xSeries;
    private List<Long> ySeries;

    /**
     * @return the kpi
     */
    public String getKpi() {
        return kpi;
    }

    /**
     * @param kpi the kpi to set
     */
    public void setKpi(String kpi) {
        this.kpi = kpi;
    }

    /**
     * @return the xSeries
     */
    public List<String> getxSeries() {
        return xSeries;
    }

    /**
     * @param xSeries the xSeries to set
     */
    public void setxSeries(List<String> xSeries) {
        this.xSeries = xSeries;
    }

    /**
     * @return the ySeries
     */
    public List<Long> getySeries() {
        return ySeries;
    }

    /**
     * @param ySeries the ySeries to set
     */
    public void setySeries(List<Long> ySeries) {
        this.ySeries = ySeries;
    }
}
