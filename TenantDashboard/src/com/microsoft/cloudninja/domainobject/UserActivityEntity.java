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
 * Entity for UserActivity table. 
 *
 */
@Entity
@Table(name = "UserActivity")
public class UserActivityEntity {

	private UserActivityId id;

	public UserActivityEntity() {
	}

	public UserActivityEntity(UserActivityId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "timeInterval", column = @Column(name = "TimeInterval")),
			@AttributeOverride(name = "tenantId", column = @Column(name = "TenantId")),
			@AttributeOverride(name = "memberId", column = @Column(name = "MemberId")),
			@AttributeOverride(name = "requests", column = @Column(name = "Requests")) })
	public UserActivityId getId() {
		return this.id;
	}

	public void setId(UserActivityId id) {
		this.id = id;
	}

}
