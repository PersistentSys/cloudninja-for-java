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
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Index</title>
    <link href="resources/Site.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="BLACK">
    <div id="page">
        <div id="header">
    <div id="loginPanel">
    <div id="loginBackground">
    <form:form commandName="tenantBean" action="showTanentHomePage.htm">        
    <div id="loginHeader"><img src="resources/images/cloudninja.png" alt="The Cloud Ninja" class="loginLogo" width="478"></div>
        <div class="row"><label class="col1">Tenant Id &nbsp;&nbsp;</label>
           <span class="col2">
           		  <form:input path="tenantId"/>
<!--              <input id="TenantId" name="TenantId" type="text">-->
           </span>
        </div>
        <div class="row"><label class="col1">User Id &nbsp;&nbsp;</label>
           <span class="col2">
           	  <form:input path="MemberId"/>
<!--              <input id="MemberId" name="MemberId" type="text">-->
           </span>
        </div>
        <div class="row"><label class="col1">Password &nbsp;&nbsp;</label>
           <span class="col2">
                  <form:input path="Password"/>
<!--              <input id="Password" name="Password" type="password">-->
           </span>
        </div>
        <div class="row">
           <div class="col1">
           </div>
           <span class="col2">
              <input value="Log On" class="submit" type="submit">
           </span>
        </div>
   </form:form>
    </div>
    </div>
    </div>
    </div>
</body>
</html>
