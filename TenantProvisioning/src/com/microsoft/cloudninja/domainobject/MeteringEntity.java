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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Metering")
public class MeteringEntity {
	
	private String tenantId;
	private Date snapshotTime;
	private long databaseSize;
	private long databaseBandwidth_Ingress;
	private long databaseBandwidth_Egress;
	private long webAppBandwithUse_SC;
	private long webAppBandwithUse_CS;
	private long webAppRequests;
	private long blobStoreUsage;
	/**
	 * @return the TenantId
	 */
	@Id
	@Column(name = "TenantId")
	public String getTenantId() {
		return tenantId;
	}
	/**
	 * @param TenantId the TenantId to set
	 */
	public void setTenantId(String TenantId) {
		this.tenantId = TenantId;
	}
	/**
	 * @return the snapshotTime
	 */
	@Column(name = "SnapshotTime")
	public Date getSnapshotTime() {
		return snapshotTime;
	}
	/**
	 * @param snapshotTime the snapshotTime to set
	 */
	public void setSnapshotTime(Date snapshotTime) {
		this.snapshotTime = snapshotTime;
	}
	/**
	 * @return the databaseSize
	 */
	@Column(name = "DatabaseSize")
	public long getDatabaseSize() {
		return databaseSize;
	}
	/**
	 * @param databaseSize the databaseSize to set
	 */
	public void setDatabaseSize(long databaseSize) {
		this.databaseSize = databaseSize;
	}
	/**
	 * @return the databaseBandwidth_Ingress
	 */
	@Column(name = "DatabaseBandwidth_Ingress")
	public long getDatabaseBandwidth_Ingress() {
		return databaseBandwidth_Ingress;
	}
	/**
	 * @param databaseBandwidth_Ingress the databaseBandwidth_Ingress to set
	 */
	public void setDatabaseBandwidth_Ingress(long databaseBandwidth_Ingress) {
		this.databaseBandwidth_Ingress = databaseBandwidth_Ingress;
	}
	/**
	 * @return the databaseBandwidth_Egress
	 */
	@Column(name = "DatabaseBandwidth_Egress")
	public long getDatabaseBandwidth_Egress() {
		return databaseBandwidth_Egress;
	}
	/**
	 * @param databaseBandwidth_Egress the databaseBandwidth_Egress to set
	 */
	public void setDatabaseBandwidth_Egress(long databaseBandwidth_Egress) {
		this.databaseBandwidth_Egress = databaseBandwidth_Egress;
	}
	/**
	 * @return the webAppBandwithUse_SC
	 */
	@Column(name = "WebAppBandwithUse_SC")
	public long getWebAppBandwithUse_SC() {
		return webAppBandwithUse_SC;
	}
	/**
	 * @param webAppBandwithUse_SC the webAppBandwithUse_SC to set
	 */
	public void setWebAppBandwithUse_SC(long webAppBandwithUse_SC) {
		this.webAppBandwithUse_SC = webAppBandwithUse_SC;
	}
	/**
	 * @return the webAppBandwithUse_CS
	 */
	@Column(name = "WebAppBandwithUse_CS")
	public long getWebAppBandwithUse_CS() {
		return webAppBandwithUse_CS;
	}
	/**
	 * @param webAppBandwithUse_CS the webAppBandwithUse_CS to set
	 */
	public void setWebAppBandwithUse_CS(long webAppBandwithUse_CS) {
		this.webAppBandwithUse_CS = webAppBandwithUse_CS;
	}
	/**
	 * @return the webAppRequests
	 */
	@Column(name = "WebAppRequests")
	public long getWebAppRequests() {
		return webAppRequests;
	}
	/**
	 * @param webAppRequests the webAppRequests to set
	 */
	public void setWebAppRequests(long webAppRequests) {
		this.webAppRequests = webAppRequests;
	}
	/**
	 * @return the blobStoreUsage
	 */
	@Column(name = "BlobStoreUsage")
	public long getBlobStoreUsage() {
		return blobStoreUsage;
	}
	/**
	 * @param blobStoreUsage the blobStoreUsage to set
	 */
	public void setBlobStoreUsage(long blobStoreUsage) {
		this.blobStoreUsage = blobStoreUsage;
	}
}
