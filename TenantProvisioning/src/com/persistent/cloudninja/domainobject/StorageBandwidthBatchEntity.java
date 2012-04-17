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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity for StorageBandwidthBatch table
 */
@Entity
@Table(name = "StorageBandwidthBatch")
public class StorageBandwidthBatchEntity {

    private Date lastUpdatedTime;
    private String logUri;
    private int lastLineProcessed;

    /**
     * Gets lastUpdatedTime
     * @return lastUpdatedTime
     */
	@Column(name = "LastUpdatedTime")
    public Date getLastUpdatedTime() {
    	return lastUpdatedTime;
    }

	/**
	 * Sets lastUpdatedTime
	 * @param lastUpdatedTime
	 */
    public void setLastUpdatedTime(Date lastUpdatedTime) {
    	this.lastUpdatedTime = lastUpdatedTime;
    }

    /**
     * Gets logUri
     * @return logUri
     */
    @Id
	@Column(name = "LogUri")
    public String getLogUri() {
    	return logUri;
    }

    /**
     * Sets logUri
     * @param logUri
     */
    public void setLogUri(String logUri) {
    	this.logUri = logUri;
    }

    /**
     * Gets lastLineProcessed
     * @return lastLineProcessed
     */
	@Column(name = "LastLineProcessed")
    public int getLastLineProcessed() {
    	return lastLineProcessed;
    }

	/**
	 * Sets lastLineProcessed
	 * @param lastLineProcessed
	 */
    public void setLastLineProcessed(int lastLineProcessed) {
    	this.lastLineProcessed = lastLineProcessed;
    }
}
