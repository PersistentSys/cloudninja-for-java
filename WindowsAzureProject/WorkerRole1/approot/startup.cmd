@REM *** Sample startup script containing the steps for starting Apache Tomcat and deploying a WAR file. 
@REM *** (Last tested with Apache Tomcat 7.0.22)

@REM To use the sample, follow these steps:
@REM *** 1) Copy all this content into approot/startup.cmd in the role folder, close this file, and edit the copy
@REM *** 2) Place a JRE distribution as jre.zip under approot
@REM *** 3) Place an Apache Tomcat 7.x distribution as tomcat7.zip under approot in your project
@REM *** 3.1) If you want to download the server into Azure directly from a URL instead, then
@REM          uncomment the next line and modify the URL as appropriate:
@REM cscript "util\download.vbs" "http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.22/bin/apache-tomcat-7.0.22.zip" "tomcat7.zip"

@REM *** 4) Update SERVER_DIR_NAME below as appropriate:
@REM *** (IMPORTANT: There must be no trailing nor leading whitespace around the setting)

SET SERVER_DIR_NAME=apache-tomcat-7.0.22

@REM *** 5) To deploy your own WAR file, place it in approot and update WAR_NAME below:
@REM *** (IMPORTANT: There must be no trailing nor leading whitespace around the setting)

SET WAR_NAME=TenantDashBoard.war
SET WAR_NAME_1=TenantProvisioning.war

			
@REM **************************************************************			
@REM *** Do not make changes below unless you know what you're doing.
rd "\%ROLENAME%"
mklink /D "\%ROLENAME%" "%ROLEROOT%\approot"
cd /d "\%ROLENAME%"

@REM Remove tomcat and jre directories if already exists
IF EXIST %SERVER_DIR_NAME% rmdir /S /Q %SERVER_DIR_NAME%
IF EXIST jre7 rmdir /S /Q jre7

@REM Download apache tomcat and jre from the blob container. Following URLs will be replaced by your storage account.
cscript "util\download.vbs" "https://cloudninjaforjavastorage.blob.core.windows.net/binaries/apache-tomcat-7.0.22-windows-x64.zip" "apache-tomcat-7.0.22.zip"
cscript "util\download.vbs" "https://cloudninjaforjavastorage.blob.core.windows.net/binaries/jre7-64bit.zip" "jre7.zip"

@REM Unzip the zip files
cscript util\unzip.vbs jre7.zip "%CD%"
cscript util\unzip.vbs apache-tomcat-7.0.22.zip "%CD%"

@REM Replace the existing server.xml
copy /Y server.xml "%SERVER_DIR_NAME%\conf\server.xml"

@REM Copy the war files to webapps
copy %WAR_NAME% "%SERVER_DIR_NAME%\webapps\%WAR_NAME%"
copy %WAR_NAME_1% "%SERVER_DIR_NAME%\webapps\%WAR_NAME_1%"

set JRE_HOME=\%ROLENAME%\jre7
cd %JRE_HOME%\bin

@REM Import the CloudNinja.cer into the JAVA keystore
keytool -noprompt -import -v -alias cloudninjacert -file "\%ROLENAME%\CloudNinja.cer" -keypass cloudninja -keystore "%JRE_HOME%\lib\security\cacerts" -storepass changeit

copy "\%ROLENAME%\CloudNinja.pfx" %JRE_HOME%\lib\security\CloudNinja.pfx
cd "\%ROLENAME%\%SERVER_DIR_NAME%\bin"
cmd /c startup.bat

@ECHO OFF
if %ERRORLEVEL%==0 exit %ERRORLEVEL%
choice /d y /t 5 /c Y /N /M "*** Windows Azure startup failed - exiting..."
exit %ERRORLEVEL%