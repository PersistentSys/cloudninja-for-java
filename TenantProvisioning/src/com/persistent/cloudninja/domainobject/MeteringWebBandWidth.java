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

import java.util.Date;
/*
 * Domain Object to populate Web Band Width Usage fields
 *  using TomCat Logs
 * */
public class MeteringWebBandWidth implements Comparable<MeteringWebBandWidth> {

    private String tenantId;
    private long webAppBandwithUse_SC;
    private long webAppBandwithUse_CS;
    private Date snapShotTime;
    private long webAppRequests;

    public String getTenantId() {
	return tenantId;
    }

    public void setTenantId(String tenantId) {
	this.tenantId = tenantId;
    }

    public long getWebAppBandwithUse_SC() {
	return webAppBandwithUse_SC;
    }

    public void setWebAppBandwithUse_SC(long webAppBandwithUse_SC) {
	this.webAppBandwithUse_SC = webAppBandwithUse_SC;
    }

    public long getWebAppBandwithUse_CS() {
	return webAppBandwithUse_CS;
    }

    public void setWebAppBandwithUse_CS(long webAppBandwithUse_CS) {
	this.webAppBandwithUse_CS = webAppBandwithUse_CS;
    }

    public Date getSnapShotTime() {
	return snapShotTime;
    }

    public void setSnapShotTime(Date snapShotTime) {
	this.snapShotTime = snapShotTime;
    }
   
    public long getWebAppRequests() {
		return webAppRequests;
	}

	public void setWebAppRequests(long webAppRequests) {
		this.webAppRequests = webAppRequests;
	}

	@Override
    public int compareTo(MeteringWebBandWidth obj) {
	return getTenantId().compareTo(obj.getTenantId());
    }

}
