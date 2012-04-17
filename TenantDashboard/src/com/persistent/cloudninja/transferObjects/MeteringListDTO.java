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

import com.persistent.cloudninja.domainobject.Metering;


public class MeteringListDTO {
	private List<Metering> meteringList;

	/**
	 * @return the meteringList
	 */
	public List<Metering> getMeteringList() {
		return meteringList;
	}

	/**
	 * @param meteringList the meteringList to set
	 */
	public void setMeteringList(List<Metering> meteringList) {
		this.meteringList = meteringList;
	}
	
	
	

}
