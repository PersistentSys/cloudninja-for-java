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

import org.soyatec.windowsazure.table.AbstractTableServiceEntity;

public class WADDirectoriesTableEntity extends AbstractTableServiceEntity {
    
    public WADDirectoriesTableEntity(String partitionKey, String rowKey) {
	super(partitionKey, rowKey);
    }
    
    	private Date timeStamp;
	private Long eventTickCount;
	private String deploymentId;
	private String role;
	private String roleInstance;
	private String absolutePath;
	private String relativePath;
	private String container;
	private String rootDirectory;
	
	
	public Date getTimeStamp() {
	    return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
	    this.timeStamp = timeStamp;
	}
	public Long getEventTickCount() {
	    return eventTickCount;
	}
	public void setEventTickCount(Long eventTickCount) {
	    this.eventTickCount = eventTickCount;
	}
	public String getDeploymentId() {
	    return deploymentId;
	}
	public void setDeploymentId(String deploymentId) {
	    this.deploymentId = deploymentId;
	}
	public String getRole() {
	    return role;
	}
	public void setRole(String role) {
	    this.role = role;
	}
	public String getRoleInstance() {
	    return roleInstance;
	}
	public void setRoleInstance(String roleInstance) {
	    this.roleInstance = roleInstance;
	}
	public String getAbsolutePath() {
	    return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
	    this.absolutePath = absolutePath;
	}
	public String getRelativePath() {
	    return relativePath;
	}
	public void setRelativePath(String relativePath) {
	    this.relativePath = relativePath;
	}
	public String getContainer() {
	    return container;
	}
	public void setContainer(String container) {
	    this.container = container;
	}
	public String getRootDirectory() {
	    return rootDirectory;
	}
	public void setRootDirectory(String rootDirectory) {
	    this.rootDirectory = rootDirectory;
	}
	
	
}
