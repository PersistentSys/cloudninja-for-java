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
package com.persistent.cloudninja.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

/*
 *  Utility class for common functions and Constants
 * 
 * */
public class CommonUtility {
    
    public static final Logger LOGGER = Logger.getLogger(CommonUtility.class);
    
    /* Simple Date Formatter for Tomcat Logs Date*/
    public static final SimpleDateFormat SDF_TOMCAT_LOGS = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");
    
    /* Simple Date Formatter for SQL Server Date*/
    public static final SimpleDateFormat SDF_DB_TABLE_LOGS =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /* Simple Date Formatter forWebBandwidth logs*/
    public static final SimpleDateFormat SDF_LOGS_QUEUE =new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
    
    /* Minimum Date for comparison*/
    public static final String MIN_DATE = "01/Jan/1753:00:00:00";
    
    
    
    /*
     * Converting Tomcat String Date to SQL Server Date Format
     * 
     * @param Date (TomcatLog)
     * @returns Date (SQL Server) 
     * 
     * */
    public static Date getDate(String date) {
        
        Date rs_date = null;

        try {
               rs_date = SDF_DB_TABLE_LOGS.parse(SDF_DB_TABLE_LOGS.format(SDF_TOMCAT_LOGS.parse(date)));
            
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return rs_date;
    }
    
    /*
     * Method for UTC Date 
     * 
     * @returns String : UTC Date
     * 
     * */
    public static String getUTCDate(){
 	   SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm");
 	   Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
 	   return sdf.format(cal.getTime());
  }

}
