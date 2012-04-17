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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity for Member Table.
 */
@Entity
@Table(name = "Member")
public class Member implements Serializable {

	private static final long serialVersionUID = -1156820323257897323L;

	private MemberCompoundKey memberCompoundKey;
	
	private String name;
	
	private String password;
	
	private String role;
	
	private Date created;
	
	private boolean enabled;

	private String liveInvitationCode;
	
	private String liveGUID;
	
	/**
	 * Gets MemberCompundKey.
	 * @return the memberCompoundKey
	 */
   @Id
	public MemberCompoundKey getMemberCompoundKey() {
		return memberCompoundKey;
	}

   /**
    * Sets member compound key.
    * @param memberCompoundKey
    */
	public void setMemberCompoundKey(MemberCompoundKey memberCompoundKey) {
		this.memberCompoundKey = memberCompoundKey;
	}
	
	/**
	 * Gets the Name.
	 * @return the name
	 */
	@Column(name = "Name")
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the Name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the Password
	 * @return password
	 */
	@Column(name = "Password")
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the Password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the Role
	 * @return the role
	 */
	@Column(name = "Role")
	public String getRole() {
		return role;
	}

	/**
	 * Sets the Role
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Gets the Date when member is created.
	 * @return created the date of creation
	 */
	@Column(name = "Created")
	public Date getCreated() {
		return created;
	}

	/**
	 * Sets the Date of member creation.
	 * @param created
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * Gets the enable status
	 * @return enable
	 */
	@Column(name = "IsEnabled")
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the enable status.
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Sets the live invitation code in case of live Id
	 * @param the liveInvitationCode
	 */
	@Column(name="LiveInvitationCode")
	public void setLiveInvitationCode(String liveInvitationCode) {
		this.liveInvitationCode = liveInvitationCode;
	}

	/**
	 * Gets the live invitation code in case of live Id
	 * @return the liveInvitationCode
	 */
	public String getLiveInvitationCode() {
		return liveInvitationCode;
	}
	
	/**
	 * Sets the live GUID in case of live Id
	 * @param liveGUID
	 */
	@Column(name="LiveGUID")
	public void setLiveGUID(String liveGUID) {
		this.liveGUID = liveGUID;
	}

	/**
	 * Gets live GUID in case of live Id.
	 * @return the liveGUID
	 */
	public String getLiveGUID() {
		return liveGUID;
	}

}
