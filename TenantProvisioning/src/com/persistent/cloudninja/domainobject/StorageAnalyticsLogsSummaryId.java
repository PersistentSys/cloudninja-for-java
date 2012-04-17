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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
/**
 * Composite key for StorageAnalyticsLogsSummary entity
 */
@Embeddable
public class StorageAnalyticsLogsSummaryId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Date snapshotTime;
	private String operationType;
	private String requestStatus;
	private String httpStatusCode;
	private String tenant;
	private String serviceType;
	
	public StorageAnalyticsLogsSummaryId() {
	}
	
	public StorageAnalyticsLogsSummaryId(
			Date snapshotTime,String operationType, 
			String requestStatus, String httpStatusCode, 
			String tenant, String serviceType ) {
		this.tenant = tenant;
		this.snapshotTime = snapshotTime;
		this.operationType = operationType;
		this.requestStatus = requestStatus;
		this.httpStatusCode = httpStatusCode;
		this.serviceType = serviceType;
	}
	
	/**
	 * Gets snapshotTime
	 * @return snapshotTime
	 */
	@Column (name = "SnapshotTime")
	public Date getSnapshotTime() {
		return snapshotTime;
	}
	
	/**
	 * Sets snapshotTime
	 * @param snapshotTime
	 */
	public void setSnapshotTime(Date snapshotTime) {
		this.snapshotTime = snapshotTime;
	}
	
	/**
	 * Gets operationType
	 * @return operationType
	 */
	@Column (name = "OperationType")
	public String getOperationType() {
		return operationType;
	}
	
	/**
	 * Sets operationType
	 * @param operationType
	 */
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	/**
	 * Gets requestStatus
	 * @return requestStatus
	 */
	@Column (name = "RequestStatus")
	public String getRequestStatus() {
		return requestStatus;
	}
	
	/**
	 * Sets requestStatus
	 * @param requestStatus
	 */
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	
	/**
	 * Gets httpStatusCode
	 * @return httpStatusCode
	 */
	@Column (name = "HttpStatusCode")
	public String getHttpStatusCode() {
		return httpStatusCode;
	}
	
	/**
	 * Sets httpStatusCode
	 * @param httpStatusCode
	 */
	public void setHttpStatusCode(String httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}
	/**
	 * Gets serviceType
	 * @return serviceType
	 */
	@Column (name = "ServiceType")
	public String getServiceType() {
		return serviceType;
	}
	
	/**
	 * Sets serviceType
	 * @param serviceType
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	/**
	 * Gets tenant
	 * @return tenant
	 */
	@Column (name = "Tenant")
	public String getTenant() {
		return tenant;
	}
	
	/**
	 * Sets tenant
	 * @param serviceType
	 */
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

}
