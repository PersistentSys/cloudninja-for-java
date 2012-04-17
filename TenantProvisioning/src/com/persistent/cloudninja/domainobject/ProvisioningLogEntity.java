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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.ForeignKey;

/**
 * Entity class for ProvisioningLog. 
 *
 */
@Entity
@Table(name = "ProvisioningLog")
public class ProvisioningLogEntity {
	private static final Logger LOGGER = Logger.getLogger(ProvisioningLogEntity.class);
	
	private String tenantId;
	private String task;
	private String message;
	private Date created;
	
	public ProvisioningLogEntity() {
		
	}
	
	public ProvisioningLogEntity(String tenantId, String task, String message) {
		this.tenantId = tenantId;
		this.task = task;
		this.message = message;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		String date = dateFormat.format(calendar.getTime());
		try {
			this.created = dateFormat.parse(date);
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Returns the tenant ID.
	 * 
	 * @return the tenant Id.
	 */
	@Id
	@ManyToOne(targetEntity=TenantEntity.class)
	@ForeignKey(name="FK_ProvisionStatus_Tenant_TenantId")
	@JoinColumn(name="TenantId")
	public String getTenantId() {
		return tenantId;
	}
	
	/**
	 * Sets the tenant Id.
	 * 
	 * @param tenantId : the id of the tenant to be set.
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	/**
	 * Returns the task.
	 * 
	 * @return the task.
	 */
	@Column(name = "Task")
	public String getTask() {
		return task;
	}
	
	/**
	 * Sets the task.
	 * 
	 * @param task : the task to be set.
	 */
	public void setTask(String task) {
		this.task = task;
	}
	
	/**
	 * Returns the message.
	 * 
	 * @return the message.
	 */
	@Column(name = "Message")
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the message.
	 * 
	 * @param message : the message to be set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Returns the created date.
	 * 
	 * @return the created date.
	 */
	@Column(name = "Created")
	public Date getCreated() {
		return created;
	}
	
	/**
	 * Sets the created date.
	 * 
	 * @param created : the created date to be set.
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
}
