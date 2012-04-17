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
package com.persistent.cloudninja.transferObjects;

import java.util.List;

import com.persistent.cloudninja.domainobject.Usage;

public class UsageDTO {
	
	private List<Usage> usage;
	private String tenant;

	/**
	 * @return the tenant
	 */
	public String getTenant() {
		return tenant;
	}

	/**
	 * @param tenant the tenant to set
	 */
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	/** Get the monthly overall Usage
	 * @return the usage
	 */
	public List<Usage> getUsage() {
		return usage;
	}

	/**Set the monthly overall Usage
	 * @param usage the usage to set
	 */
	public void setUsage(List<Usage> usage) {
		this.usage = usage;
	}

	
	
}
