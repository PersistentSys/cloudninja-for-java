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

import com.microsoft.cloudninja.transferObjects.RunningInstanceDTO;
/**
 * Dao Interface to get running instances.
 */
public interface RunningInstancesJSONDataDao {

    /**
     * Gets running instance count from database.
     * @param start is the start time.
     * @param end is the end time to get the instances in the specific range
     * @return runningInstanceDTO has running instance count
     */
    public RunningInstanceDTO getRunningInstancesFromDB(String start, String end);
}
