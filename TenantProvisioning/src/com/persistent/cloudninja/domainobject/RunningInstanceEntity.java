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
 * Holds data from RoleInstance Table operation.
 */
public class RunningInstanceEntity {
    
    private int day;
    private int month;
    private int year;
    private String roleName;
    private int instanceCountSum;
    /**
     * @return the day
     */
    public int getDay() {
        return day;
    }
    /**
     * @param day the day to set
     */
    public void setDay(int day) {
        this.day = day;
    }
    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }
    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }
    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }
    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
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
    /**
     * @return the instanceCountSum
     */
    public int getInstanceCountSum() {
        return instanceCountSum;
    }
    /**
     * @param instanceCountSum the instanceCountSum to set
     */
    public void setInstanceCountSum(int instanceCountSum) {
        this.instanceCountSum = instanceCountSum;
    }
    
    
    

}
