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
package com.microsoft.cloudninja.utils;
import java.util.Random;

/**
 * This class is generate the SQL Server Password, policy defined at
 * http://msdn.microsoft.com/en-us/library/ms161959.aspx
 * 
 */
public class PasswordGenerator {
    
    private static String PASSWORD_UPPER_CASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String PASSWORD_LOWER_CASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static String PASSWORD_NUMERIC_CHARS = "0123456789";
    private static String PASSWORD_SPECIAL_CHARS = "!#$%";
    private static int passwordLength = 15;
        
   /**
    * This method create password as per SQL Server Password, policy defined at
    * http://msdn.microsoft.com/en-us/library/ms161959.aspx
    * @return Password string of length 15
    */
    public static String generatePassword() {
   
        StringBuffer generatedPassword = new StringBuffer();
        StringBuffer passChars = new StringBuffer();
        
        passChars.append(PASSWORD_UPPER_CASE_CHARS);
        passChars.append(PASSWORD_LOWER_CASE_CHARS);
        passChars.append(PASSWORD_NUMERIC_CHARS);
        passChars.append(PASSWORD_SPECIAL_CHARS);
        
        int passCharlength = passChars.length();
        
        Random rand = new Random();
        
        int uc = rand.nextInt(26);
        int lc = rand.nextInt(26);
        int nc = rand.nextInt(10);
        int sc = rand.nextInt(4);
        
        generatedPassword.append(PASSWORD_UPPER_CASE_CHARS.charAt(uc));
        generatedPassword.append(PASSWORD_LOWER_CASE_CHARS.charAt(lc));
        generatedPassword.append(PASSWORD_NUMERIC_CHARS.charAt(nc));
        generatedPassword.append(PASSWORD_SPECIAL_CHARS.charAt(sc));
        
        for (int i=0; i < passwordLength - 4; i++) {
            generatedPassword.append(passChars.charAt(rand.nextInt(passCharlength)));
        }
        return generatedPassword.toString();
    } 
}
