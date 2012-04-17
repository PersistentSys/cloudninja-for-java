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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity for KpiValueTemp.
 *
 */
@Entity
@Table(name = "KpiValueTemp")
public class KpiValueTempEntity {

	private KpiValueTempId id;

	public KpiValueTempEntity() {
	}

	public KpiValueTempEntity(KpiValueTempId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "roleId", column = @Column(name = "RoleId")),
			@AttributeOverride(name = "instance", column = @Column(name = "Instance")),
			@AttributeOverride(name = "instanceNo", column = @Column(name = "InstanceNo")),
			@AttributeOverride(name = "timeStamp", column = @Column(name = "TimeStamp")),
			@AttributeOverride(name = "kpiTypeId", column = @Column(name = "KpiTypeId")),
			@AttributeOverride(name = "value", column = @Column(name = "Value")) })
	public KpiValueTempId getId() {
		return this.id;
	}

	public void setId(KpiValueTempId id) {
		this.id = id;
	}

}
