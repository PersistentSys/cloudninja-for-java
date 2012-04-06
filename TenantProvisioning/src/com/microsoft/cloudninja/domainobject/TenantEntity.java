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

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Entity class for Tenant table.
 *
 */
@Entity
@Table(name = "Tenant")
public class TenantEntity {

	private String tenantId;
	private String status;
	private String companyName;
	private String logoFileName;
	private Date created;
	private String thumbprint;
	private int locationId;
	
	/**
	 * Returns the tenantId.
	 * 
	 * @return tenantId
	 */
	@Id
	@Column(name = "TenantId")
	public String getTenantId() {
		return tenantId;
	}
	
	/**
	 * Sets the tenantId.
	 * 
	 * @param tenantId : the tenantId to set.
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	/**
	 * Returns the status.
	 * 
	 * @return status
	 */
	@Column(name = "Status")
	public String getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 * 
	 * @param status : the status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Returns the companyName.
	 * 
	 * @return companyName
	 */
	@Column(name = "CompanyName")
	public String getCompanyName() {
		return companyName;
	}
	
	/**
	 * Sets the companyName.
	 * 
	 * @param companyName : the companyName to set.
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	/**
	 * Returns the logoFileName.
	 * 
	 * @return logoFileName
	 */
	@Column(name = "LogoFileName")
	public String getLogoFileName() {
		return logoFileName;
	}
	
	/**
	 * Sets the logoFileName.
	 * 
	 * @param logoFileName : the logoFileName to set.
	 */
	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}
	
	/**
	 * Returns the created date.
	 * 
	 * @return created date.
	 */
	@Column(name = "Created")
	public Date getCreated() {
		return created;
	}
	
	/**
	 * Sets the created date.
	 * 
	 * @param created : the created date to set.
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	
	/**
	 * Returns the thumbprint.
	 * 
	 * @return thumbprint
	 */
	@Column(name = "Thumbprint")
	public String getThumbprint() {
		return thumbprint;
	}
	
	/**
	 * Sets the thumbprint.
	 * 
	 * @param thumbprint : the thumbprint to set.
	 */
	public void setThumbprint(String thumbprint) {
		this.thumbprint = thumbprint;
	}
	
	/**
	 * Returns the locationId.
	 * 
	 * @return locationId
	 */
	@Column(name = "LocationId")
	public int getLocationId() {
		return locationId;
	}
	
	/**
	 * Sets the locationId.
	 * 
	 * @param locationId : the locationId to set.
	 */
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
}
