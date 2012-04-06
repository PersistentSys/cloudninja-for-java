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

import com.microsoft.cloudninja.domainobject.TopQueriesEntity;

/**
 * Data transfer object for getting top queries executed.
 */
public class TopQueriesDTO {
    
    private List<TopQueriesEntity> topQueries;

    /**
     * @return the topQueries
     */
    public List<TopQueriesEntity> getTopQueries() {
        return topQueries;
    }

    /**
     * @param topQueries the topQueries to set
     */
    public void setTopQueries(List<TopQueriesEntity> topQueries) {
        this.topQueries = topQueries;
    }
        

}
