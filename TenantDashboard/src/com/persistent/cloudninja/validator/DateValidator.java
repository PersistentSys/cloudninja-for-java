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
package com.persistent.cloudninja.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator {
	 /**
     * validate the Date entered.
     * @param startDate - the start date of task
     * @param endDate - the end date of task
     * @return boolean - true if startDate < endDate otherwise return false;
     */
    public static boolean isValidDateRange( String startDate,String endDate) {
    	Date end = null;
    	Date start = null;
    	start = (Date) stringToDate(startDate);
		end = (Date) stringToDate(endDate);
        if (start == null || end == null)
            return false;
        if (start.getTime() <= end.getTime())
            return true;
        else
            return false;
    }

	public static Date stringToDate(String startDate) {
		DateFormat formatter ; 
		Date date = null ;
		try{  
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			date = (Date)formatter.parse(startDate);
			
		  } catch (ParseException e){
		  }
		return date;   
	}
	/**
	 * Validate the format of Date entered
	 * @param enteredDate is the date entered by the user 
	 * @return false is the date format is invalid
	 */
	public static boolean isValidDate(String enteredDate) {
		DateFormat formatter ; 
		boolean result=false;
		try{  
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			@SuppressWarnings("unused")
			Date date = formatter.parse(enteredDate);
			result=true;
		} catch (ParseException e){
			result=false;	  
		}
		return result;  
	}
}

