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
package com.microsoft.cloudninja.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.cloudninja.dao.UsageDao;
import com.microsoft.cloudninja.domainobject.Usage;
import com.microsoft.cloudninja.model.MeteringTotalsChartModel;
import com.microsoft.cloudninja.service.MeteringTotalsDataService;

/**
 * 
 * MeteringTotalsDataService implementation to get the data for metering totals
 *
 */
@Service("meteringTotalsDataService")
public class MeteringTotalsDataServiceImpl implements MeteringTotalsDataService {

    @Autowired
    private UsageDao usageDao;

    private static String DatabaseSize = "DatabaseSize"; 
    private static String DatabaseBandwidth_Ingress = "DatabaseBandwidth_Ingress"; 
    private static String DatabaseBandwidth_Egress = "DatabaseBandwidth_Egress"; 
    private static String WebAppBandwithUse_SC = "WebAppBandwithUse_SC"; 
    private static String WebAppBandwithUse_CS = "WebAppBandwithUse_CS"; 
    private static String WebAppRequests = "WebAppRequests"; 
    private static String BlobStoreUsage = "BlobStoreUsage";

    @Override
    public List<MeteringTotalsChartModel> getMeteringTotalsChartModelList(String year,
                                                                          String month,
                                                                          String kpi) {

        List<MeteringTotalsChartModel> chartModelList = new ArrayList<MeteringTotalsChartModel>();
        // Map to hold the KPI as key and value the String array for yaxis
        Map<String, long[]> kpiYAarrayMap = new HashMap<String, long[]>();
        // Get the 
        List<String> xSeriesList = getAllMonthDates(month, year);
        String[] kpiArray = kpi.split(",");
        List<String> kpiList = new ArrayList<String>();
        kpiList = Arrays.asList(kpiArray);

        // Get the data in list form database
        List<Usage> meteringEntityList = usageDao.getMeteringTotalsList(month, year);
        Usage meteringEntity = null;

        // iterate through kpi list and make map object with number of days in
        // month
        // iterate through the meteringEntity
         
        int DaysInMonth = getDaysInMonth(month, year);
        for (int i = 0; i < kpiList.size(); i++) {
            kpiYAarrayMap.put(kpiList.get(i), new long[DaysInMonth]);
        }
        long[] tempYArray = null;
        String tempKpiName = null;

        for (int i = 0; i < meteringEntityList.size(); i++) {
            meteringEntity = meteringEntityList.get(i);
            // iterate through the KPI list to populate respective ydata
            for (int k = 0; k < kpiList.size(); k++) {
                tempKpiName = kpiList.get(k);
                tempYArray = kpiYAarrayMap.get(kpiList.get(k));
                populateYarray(tempKpiName, meteringEntity, tempYArray);
            }
        }

        for (int i = 0; i < kpiList.size(); i++) {
            tempKpiName = kpiList.get(i);
            tempYArray = kpiYAarrayMap.get(tempKpiName);
            MeteringTotalsChartModel cm = new MeteringTotalsChartModel();
            cm.setKpi(tempKpiName);
            cm.setxSeries(xSeriesList);
            cm.setySeries(Arrays.asList(ArrayUtils.toObject(tempYArray)));
            chartModelList.add(cm);

        }
        return chartModelList;
    }
    
    /**
     * This method populates the yarray respective to the given Kpi name
     * @param kpiName The name of the KPI
     * @param meteringEntity The entity form database
     * @param yArray The yarray respective to the KPI
     */
   private  void populateYarray(String kpiName, Usage meteringEntity, long[] yArray) {

        int day = meteringEntity.getDay();
        // because array index starts at 0
        day = day - 1;

        if (kpiName.equalsIgnoreCase(DatabaseSize)) {
            yArray[day] = meteringEntity.getDatabaseSize();
        } else if (kpiName.equalsIgnoreCase(DatabaseBandwidth_Ingress)) {
            yArray[day] = meteringEntity.getDatabaseBandwidth_Ingress();
        } else if (kpiName.equalsIgnoreCase(DatabaseBandwidth_Egress)) {
            yArray[day] = meteringEntity.getDatabaseBandwidth_Egress();
        } else if (kpiName.equalsIgnoreCase(WebAppBandwithUse_SC)) {
            yArray[day] = meteringEntity.getWebAppBandwithUse_SC();
        } else if (kpiName.equalsIgnoreCase(WebAppBandwithUse_CS)) {
            yArray[day] = meteringEntity.getWebAppBandwithUse_CS();
        } else if (kpiName.equalsIgnoreCase(WebAppRequests)) {
            yArray[day] = meteringEntity.getWebAppRequests();
        } else if (kpiName.equalsIgnoreCase(BlobStoreUsage)) {
            yArray[day] = meteringEntity.getBlobStoreUsage();
        }
    }

    /**
     * Returns list of all the dates in the given year and month
     * 
     * @param month
     * @param year
     * @return
     */
    private List<String> getAllMonthDates(String month, String year) {

        List<String> dateList = new ArrayList<String>();

        String date = month + "/" + 1 + "/" + year;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date calDate = null;

        Calendar calendar = Calendar.getInstance();
        try {
            calDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(calDate);
        // Add first date to list
        dateList.add(formatter.format(calDate));
        // Get number of days in the given month
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String dateStr;
        for (int i = 0; i < maxDay - 1; i++) {
            calendar.add(Calendar.DATE, 1);
            Date newDate = calendar.getTime();
            dateStr = formatter.format(newDate);
            dateList.add(dateStr);
        }
        return dateList;
    }
    
    /**
     * For the given year and month this method returns the number of days
     * @param month
     * @param year
     * @return the number of days in month
     */
    private int getDaysInMonth(String month, String year) {
        
        String date = month + "/" + 1 + "/" + year;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date calDate = null;

        Calendar calendar = Calendar.getInstance();
        try {
            calDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(calDate);

        // Get number of days in the given month
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return (daysInMonth);
        
    }
}
