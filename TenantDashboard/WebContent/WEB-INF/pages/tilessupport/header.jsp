<%--
Copyright 2012 Persistent Systems Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.microsoft.cloudninja.controller.AuthFilterUtils"%>
<%@ page import="com.microsoft.cloudninja.web.security.CloudNinjaConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link href="resources/Site.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="BLACK">

<%
String cookieName = "CLOUDNINJAAUTH";
Cookie cookies [] = request.getCookies ();

Cookie authCookie = null;
String userName = "";
String role = "";
if (cookies != null)
  {
    for (int i = 0; i < cookies.length; i++) 
    {
       if (cookies [i].getName().equals (cookieName))
         {
    	   authCookie = cookies[i];
           String [] valArray = authCookie.getValue().split("!");
           userName = valArray[0];
		   userName = AuthFilterUtils.getFieldValueFromCookie(authCookie,CloudNinjaConstants.COOKIE_USERNAME_PREFIX);
           role =  AuthFilterUtils.getRoleFromCookie(authCookie,CloudNinjaConstants.COOKIE_AUTHORITIES_PREFIX);
           break;
        }
   }
 }

String logoCookieName = "CLOUDNINJALOGO";

Cookie logoCookie = null;
String logoFile = "";
if (cookies != null)
  {
    for (int i = 0; i < cookies.length; i++) 
    {
       if (cookies [i].getName().equals (logoCookieName))
         {
    	   logoCookie = cookies[i];
           logoFile = logoCookie.getValue();
           break;
        }
   }
 }
if (logoFile.isEmpty())
{
	logoFile = "resources/images/cloudninja-logo.gif";
}

%>
    <div id="page">
        <div id="header">
        <div id="title">
				
                <img src=<%= logoFile %> id="logoImage" alt="The Cloud Ninja" class="logo" />
        </div>			
			
            <div id="logindisplay">
            <p>
            <!-- ${userName}--> <%= userName %>    &nbsp;-&nbsp;
            Welcome <b></b>!
            [ <a href="<c:url value="/logout.htm"/>">Log Off</a> ]
            </p>

            </div>
            <div id="menucontainer">
                <ul class="menu">
                    <li class="current"><a href="showTenantHomePageList.htm">Home</a></li>
                            <li><a href="showTenantProfilePage.htm">Profile</a></li>
                            <li><a href="showTenantMeteringPage.htm">Usage</a></li>
                            <li><a href="showManageUsersList.htm">Manage Users</a></li>
                </ul>
            </div>
        </div>
        <div class="clear"></div>
    </div>
