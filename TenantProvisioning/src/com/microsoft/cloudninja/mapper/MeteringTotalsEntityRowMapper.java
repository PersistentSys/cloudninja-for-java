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
package com.microsoft.cloudninja.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.microsoft.cloudninja.domainobject.Usage;

/**
 * 
 * This class is used to map the rows columns as a result of query on ALL_METERING_DAILY_VIEW ME & 
 * STORAGEMETERING_DAILY_VIEW
 *
 */
public class MeteringTotalsEntityRowMapper implements  RowMapper<Usage> {

    @Override
    public Usage mapRow(ResultSet rs, int row) throws SQLException {

        Usage mte = new Usage();

        mte.setYear(rs.getInt("Year"));

        mte.setMonth(rs.getInt("Month"));

        mte.setDay(rs.getInt("Day"));

        mte.setDatabaseSize(rs.getLong("DatabaseSize"));

        mte.setDatabaseBandwidth_Ingress(rs.getLong("DatabaseBandwidth_Ingress"));

        mte.setDatabaseBandwidth_Egress(rs.getLong("DatabaseBandwidth_Egress"));

        mte.setWebAppBandwithUse_SC(rs.getLong("WebAppBandwithUse_SC"));

        mte.setWebAppBandwithUse_CS(rs.getLong("WebAppBandwithUse_CS"));

        mte.setWebAppRequests(rs.getLong("WebAppRequests"));

        mte.setBlobStoreUsage(rs.getLong("BlobStoreUsage"));

        return mte;
    }

}
