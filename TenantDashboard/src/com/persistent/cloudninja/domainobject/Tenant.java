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

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Tenant")
public class Tenant {

	private String tenantId;
	private String status;
	private String companyName;
	private String logoFileName;
	private Date created;
	private String thumbprint;
	private int locationId;
	
	@Id
	@Column(name = "TenantId")
	public String getTenantId() {
		return tenantId;
	}
	
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	@Column(name = "Status")
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "CompanyName")
	public String getCompanyName() {
		return companyName;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Column(name = "LogoFileName")
	public String getLogoFileName() {
		return logoFileName;
	}
	
	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}
	
	@Column(name = "Created")
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	@Column(name = "Thumbprint")
	public String getThumbprint() {
		return thumbprint;
	}
	
	public void setThumbprint(String thumbprint) {
		this.thumbprint = thumbprint;
	}
	
	@Column(name = "LocationId")
	public int getLocationId() {
		return locationId;
	}
	
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
}
