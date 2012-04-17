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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtility {

	
	private static Matcher matcher;
	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
	private static final String EMAIL_PATTERN = "([_A-Za-z0-9-]+)(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})";
	private static Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
	private static Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
	


	
	

	/**
	 * Retrieve UTC Date Format
	 * 
	 * @return String : Date string as UTC Date
	 */
	public static String getUTCDate() {

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");

		Calendar tzCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		sdf.setCalendar(tzCal);

		return sdf.format(new Date());
	}
	

	/**
	 * Validate password with regular expression
	 * 
	 * @param password
	 *            password for validation
	 * @return true valid password, false invalid password
	 * 
	 * 
	 * Password  
	 * 1.Must contains one digit from 0-9  
	 * 2.Must contains one lowercase characters  
	 * 3.Must contains one special symbols in the list "@#$%"  
	 * 4.Match anything with previous condition checking 
	 * 5.Length at least 6 characters and maximum of 20
	 */
	public static boolean passwordValidate(String password) {

		matcher = pattern.matcher(password);
		return matcher.matches();
	}
	
	
	/**
	   * Validate Email Format
	   * @param email String for validation
	   * @return true valid email,else flase for invalid email id
	   */
	  public static boolean emailValidation(String email){

		  matcher = emailPattern.matcher(email);
		  return matcher.matches();

	  }
}
