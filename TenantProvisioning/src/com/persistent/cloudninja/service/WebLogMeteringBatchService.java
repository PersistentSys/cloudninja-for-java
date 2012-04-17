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
package com.persistent.cloudninja.service;

import java.util.List;

import com.persistent.cloudninja.domainobject.MeteringWebBandWidth;
import com.persistent.cloudninja.domainobject.WebLogMeteringBatch;

/** 
 * Service Interface  for  Tenant Web Band Width Usage Flow
 *
 */
public interface WebLogMeteringBatchService {

    public List<WebLogMeteringBatch> getMeteringBatchServiceDetails(String uri);

    /**
     * Insert Data into Metering Table
     * 
     * @param MeteringWebBandWidth
     *            meteringWebBandWidth
     */

    public void insertMeteringDetails(MeteringWebBandWidth meteringWebBandWidth);

    /**
     * Insert Data for Web Band Width Log Details
     * 
     * @param WebLogMeteringBatch
     *            webLogMeteringBatch
     */
    public void insertWebLogMeteringDetails(
	    WebLogMeteringBatch webLogMeteringBatch);

}
