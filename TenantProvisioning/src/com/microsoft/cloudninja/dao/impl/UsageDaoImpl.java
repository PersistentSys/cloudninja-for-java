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
package com.microsoft.cloudninja.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.microsoft.cloudninja.dao.UsageDao;
import com.microsoft.cloudninja.domainobject.Usage;
import com.microsoft.cloudninja.mapper.MeteringTotalsEntityRowMapper;
import com.microsoft.cloudninja.mapper.TenantUsageRowMapper;
import com.microsoft.cloudninja.mapper.UsageRowMapper;
import com.microsoft.cloudninja.transferObjects.UsageDTO;


@Repository("usageDao")
public class UsageDaoImpl implements UsageDao {
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public UsageDTO getDashboardMetering(int month, String year) {
	
		UsageDTO usageDTO= new UsageDTO();
		String query ="SELECT ME.[TenantId] AS TenantId," +
		"			ME.[Year] AS Year," +
		"			ME.[Month] AS Month," +
		"			[DatabaseSize]," +
		"			[DatabaseBandwidth_Ingress]," +
		"			[DatabaseBandwidth_Egress], " +
		"			[WebAppBandwithUse_SC], " +
		"			[WebAppBandwithUse_CS], " +
		"			[WebAppRequests], " +
		"			[BlobStoreUsage]," +
		"			[TotalRequestPacketSize], " +
		"			[TotalResponsePacketSize], " +
		"			[TotalStorageTransactions]  " +
		"			FROM ALL_METERING_MONTHLY_VIEW ME " +
		"			LEFT OUTER JOIN STORAGEMETERING_MONTHLY_VIEW SM " +
		"			ON ME.TenantId = SM.Tenant AND ME.YEAR = SM.YEAR AND ME.MONTH = SM.MONTH" +
		"			WHERE ME.[Month] = "+month+" " +
		"           AND ME.[Year] = "+year+"";
		try{
		usageDTO.setUsage(jdbcTemplate.query(query,new UsageRowMapper()));

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return usageDTO;
		
	}

	@Override
	public UsageDTO getTenantSpecificMetering(String tenantId,int month,int year) {
		UsageDTO usageDTO= new UsageDTO();
		
		try{
			String query ="	SELECT ME.[TenantId] AS TenantId," +
			"			ME.[Year] AS Year," +
			"			ME.[Month] AS Month," +
			"			ME.[Day] AS Day," +
			"			[DatabaseSize]," +
			"			[DatabaseBandwidth_Ingress]," +
			"			[DatabaseBandwidth_Egress], " +
			"			[WebAppBandwithUse_SC], " +
			"			[WebAppBandwithUse_CS], " +
			"			[WebAppRequests], " +
			"			[BlobStoreUsage]," +
			"			[TotalRequestPacketSize], " +
			"			[TotalResponsePacketSize], " +
			"			[TotalStorageTransactions]  " +
			"			FROM ALL_METERING_DAILY_VIEW ME " +
			"			LEFT OUTER JOIN STORAGEMETERING_DAILY_VIEW SM " +
			"			ON ME.TenantId = SM.Tenant AND ME.YEAR = SM.YEAR AND ME.MONTH = SM.MONTH" +
			"			WHERE TenantId = '"+tenantId+"' "+
			"           AND ME.[Year] = "+year+" " +
			"			AND ME.[Month] = "+month+" ";
		usageDTO.setUsage(jdbcTemplate.query(query,new TenantUsageRowMapper()));

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return usageDTO;
	}

    @Override
    public List<Usage> getMeteringTotalsList(String month, String year) {
        
        List<Usage> meteringTotalsList = null;
        
        String query =  "SELECT ME.[Year] AS Year,  ME.[Month] AS Month, ME.[Day] AS Day, " +                                             
        " sum([DatabaseSize]) AS DatabaseSize, " + 
        " sum([DatabaseBandwidth_Ingress]) as DatabaseBandwidth_Ingress, " +
        " sum([DatabaseBandwidth_Egress]) as DatabaseBandwidth_Egress, " +
        " sum([WebAppBandwithUse_SC]) as WebAppBandwithUse_SC, " +
        " sum([WebAppBandwithUse_CS]) as WebAppBandwithUse_CS, " +
        " sum([WebAppRequests]) as WebAppRequests, " +
        " sum([BlobStoreUsage]) as BlobStoreUsage " +
        " FROM ALL_METERING_DAILY_VIEW ME " +
        " WHERE  ME.[Year] = " +year+ " AND ME.[Month] = " + month +
        " GROUP BY ME.[Year], ME.[Month], ME.[Day] " +
        " ORDER BY  ME.[Year], ME.[Month], ME.[Day] " ;
        
        meteringTotalsList = (jdbcTemplate.query(query,new MeteringTotalsEntityRowMapper()));
        
        return meteringTotalsList;
    }
	
	

}
	
