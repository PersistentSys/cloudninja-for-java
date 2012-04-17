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
package com.persistent.cloudninja.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.persistent.cloudninja.dao.InstanceHealthDataDao;
import com.persistent.cloudninja.service.InstanceHealthDataService;
import com.persistent.cloudninja.transferObjects.InstanceHealthActiveUserDTO;
import com.persistent.cloudninja.transferObjects.InstanceHealthKpiValueDTO;
import com.persistent.cloudninja.transferObjects.InstanceHealthRolesKpiListDTO;
import com.persistent.cloudninja.transferObjects.TopQueriesDTO;

@Service("instanceHealthJSONDataService")
public class InstanceHealthDataServiceImpl implements InstanceHealthDataService{
    
    @Autowired
    InstanceHealthDataDao instanceHealthJSONDataDao;

    @Override
    public InstanceHealthActiveUserDTO getActiveUserCount(String start,String end) {
        return instanceHealthJSONDataDao.getActiveUser(start,end);
    }

    @Override
    public TopQueriesDTO getTopQueries() {
        return instanceHealthJSONDataDao.getTopQueriesFromDB();
    }

    @Override
    public InstanceHealthRolesKpiListDTO getKpiRoleList(String start, String end) {
        return instanceHealthJSONDataDao.getKpiRoles(start,end);
    }

    @Override
    public InstanceHealthKpiValueDTO getInstanceKPIs(String start, String end, int role,
                                          int instance) {
        return instanceHealthJSONDataDao.getInstanceKPIsFromDB(start,end,role,instance);
    }

}
