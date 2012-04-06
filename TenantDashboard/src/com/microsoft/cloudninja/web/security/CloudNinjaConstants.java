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
package com.microsoft.cloudninja.web.security;


public class CloudNinjaConstants {
	
	public static final String AUTH_COOKIE_NAME ="CLOUDNINJAAUTH";
	
	public static final String COOKIE_USERNAME_PREFIX = "username";
	public static final String COOKIE_TENANTID_PREFIX = "tenantId";
	public static final String COOKIE_AUTHORITIES_PREFIX = "authorities";
	public static final String COOKIE_AUTH_SESSION_START_PREFIX = "AUTH_SESSION_START";
	public static final String COOKIE_AUTH_SESSION_END_PREFIX = "AUTH_SESSION_END";
	public static final String COOKIE_FIELDS_SEPARATOR = "!";
	public static final String COOKIE_FIELD_AND_VALUE_SEPARATOR = ":";
	public static final String ROLE_PREFIX = "ROLE_";
	public static final String UTF_8_FORMAT="UTF-8";
	public static final String DUMMY_TENANT_ID="dummytenant";
	public static final String DUMMY_USER_ID="dummyuser";
	
	
	private CloudNinjaConstants(){
		
	}
}
