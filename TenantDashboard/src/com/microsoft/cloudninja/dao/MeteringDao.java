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
package com.microsoft.cloudninja.dao;

import java.util.List;

import com.microsoft.cloudninja.domainobject.Metering;
import com.microsoft.cloudninja.exception.SystemException;
/**
 * DAO Interface to get Metering List.
 *
 */
public interface MeteringDao {

	/**
	 * Gets tenant Metering List.
	 * @param tenantId is the Id to get the list
	 * @return Tenant Metering list
	 * @throws SystemException
	 */
	List<Metering> getMeteringList(String tenantId, int month, int year) throws SystemException;


}
