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


public class Metering {
	private String tenantId;
	private Integer year;
	private Integer month;
	private Integer day;
	private long databaseSize;
	private long databaseBandwidth_Ingress;
	private long databaseBandwidth_Egress;
	private long webAppBandwithUse_SC;
	private long webAppBandwithUse_CS;
	private long webAppRequests;
	private long blobStoreUsage;
	private long totalRequestPacketSize;
	private long totalResponsePacketSize;
	private long totalStorageTransactions;
	/**
	 * @return the tenantId
	 */
	
	public String getTenantId() {
		return tenantId;
	}
	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * @return the year
	 */

	public Integer getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}
	/**
	 * @return the month
	 */

	public Integer getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(Integer month) {
		this.month = month;
	}
	/**
	 * @return the day
	 */

	public Integer getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(Integer day) {
		this.day = day;
	}
	/**
	 * @return the databaseSize
	 */

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

	public long getBlobStoreUsage() {
		return blobStoreUsage;
	}
	/**
	 * @param blobStoreUsage the blobStoreUsage to set
	 */
	public void setBlobStoreUsage(long blobStoreUsage) {
		this.blobStoreUsage = blobStoreUsage;
	}
    /**
     * @return the totalRequestPacketSize
     */
    public long getTotalRequestPacketSize() {
        return totalRequestPacketSize;
    }
    /**
     * @param totalRequestPacketSize the totalRequestPacketSize to set
     */
    public void setTotalRequestPacketSize(long totalRequestPacketSize) {
        this.totalRequestPacketSize = totalRequestPacketSize;
    }
    /**
     * @return the totalResponsePacketSize
     */
    public long getTotalResponsePacketSize() {
        return totalResponsePacketSize;
    }
    /**
     * @param totalResponsePacketSize the totalResponsePacketSize to set
     */
    public void setTotalResponsePacketSize(long totalResponsePacketSize) {
        this.totalResponsePacketSize = totalResponsePacketSize;
    }
    /**
     * @return the totalStorageTransactions
     */
    public long getTotalStorageTransactions() {
        return totalStorageTransactions;
    }
    /**
     * @param totalStorageTransactions the totalStorageTransactions to set
     */
    public void setTotalStorageTransactions(long totalStorageTransactions) {
        this.totalStorageTransactions = totalStorageTransactions;
    }
}
