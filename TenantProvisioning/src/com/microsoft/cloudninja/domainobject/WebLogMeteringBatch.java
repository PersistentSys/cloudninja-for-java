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

import java.util.Date;

public class WebLogMeteringBatch implements Comparable<WebLogMeteringBatch> {

    private String batchID;
    private Date lastUpdatedTime;
    private String LogUri;
    private int LastLineProcessed;

    public String getBatchID() {
	return batchID;
    }

    public void setBatchID(String batchID) {
	this.batchID = batchID;
    }

    public Date getLastUpdatedTime() {
	return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
	this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getLogUri() {
	return LogUri;
    }

    public void setLogUri(String logUri) {
	LogUri = logUri;
    }

    public int getLastLineProcessed() {
	return LastLineProcessed;
    }

    public void setLastLineProcessed(int lastLineProcessed) {
	LastLineProcessed = lastLineProcessed;
    }

    @Override
    public int compareTo(WebLogMeteringBatch obj) {
	return getLastUpdatedTime().compareTo(obj.getLastUpdatedTime());
    }

}
