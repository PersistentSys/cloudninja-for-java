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
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *  Entity for StorageAnalyticsLogsSummary table
 */
@Entity
@Table (name="StorageAnalyticsLogsSummary")
public class StorageAnalyticsLogsSummaryEntity {
	private float e2eLatency;
	private float serverLatency;
	private long requestPacketSize;
	private long responsePacketSize;
	private int count;
	private boolean billable;
	private StorageAnalyticsLogsSummaryId id;
	
	public StorageAnalyticsLogsSummaryEntity() {
	}
	
	public StorageAnalyticsLogsSummaryEntity (StorageAnalyticsLogsSummaryId id) {
		this.id = id;
	}

	/**
	 * Gets id
	 * @return id
	 */
	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "snapshotTime", column = @Column(name = "SnapshotTime")),
		@AttributeOverride(name = "operationType", column = @Column(name = "OperationType")),
		@AttributeOverride(name = "requestStatus", column = @Column(name = "RequestStatus")),
		@AttributeOverride(name = "httpStatusCode", column = @Column(name = "HttpStatusCode")),
		@AttributeOverride(name = "tenant", column = @Column(name = "Tenant")),
		@AttributeOverride(name = "serviceType", column = @Column(name = "ServiceType")) })
	public StorageAnalyticsLogsSummaryId getId() {
		return id;
	}
	
	/**
	 * Sets id
	 * @param id
	 */
	public void setId(StorageAnalyticsLogsSummaryId id) {
		this.id = id;
	}
	
	/**
	 * Gets e2eLatency
	 * @return e2eLatency
	 */
	@Column (name = "E2ELatency")
	public float getE2eLatency() {
		return e2eLatency;
	}
	
	/**
	 * Sets e2eLatency
	 * @param e2eLatency
	 */
	public void setE2eLatency(float e2eLatency) {
		this.e2eLatency = e2eLatency;
	}
	
	/**
	 * Gets serverLatency
	 * @return serverLatency
	 */
	@Column (name = "ServerLatency")
	public float getServerLatency() {
		return serverLatency;
	}
	
	/**
	 * Sets serverLatency
	 * @param serverLatency
	 */
	public void setServerLatency(float serverLatency) {
		this.serverLatency = serverLatency;
	}
	
	/**
	 * Gets requestPacketSize
	 * @return requestPacketSize
	 */
	@Column (name = "RequestPacketSize")
	public long getRequestPacketSize() {
		return requestPacketSize;
	}
	
	/**
	 * Sets requestPacketSize
	 * @param requestPacketSize
	 */
	public void setRequestPacketSize(long requestPacketSize) {
		this.requestPacketSize = requestPacketSize;
	}
	
	/**
	 * Gets responsePacketSize
	 * @return responsePacketSize
	 */
	@Column (name = "ResponsePacketSize")
	public long getResponsePacketSize() {
		return responsePacketSize;
	}
	
	/**
	 * Sets responsePacketSize
	 * @param responsePacketSize
	 */
	public void setResponsePacketSize(long responsePacketSize) {
		this.responsePacketSize = responsePacketSize;
	}
	
	/**
	 * Gets count
	 * @return count
	 */
	@Column (name = "Count")
	public int getCount() {
		return count;
	}
	
	/**
	 * Sets count
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * Gets billable
	 * @return billable
	 */
	@Column (name = "Billable")
	public boolean isBillable() {
		return billable;
	}
	
	/**
	 * Sets billable
	 * @param billable
	 */
	public void setBillable(boolean billable) {
		this.billable = billable;
	} 
}
