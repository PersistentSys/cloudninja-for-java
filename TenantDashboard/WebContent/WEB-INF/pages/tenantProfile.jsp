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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>OrganizationSettings</title>
</head>

<div id="main">
<div id="mainContainer">

<h2>Organization Settings</h2>
<p></p>
<form:form action="showTenantProfilePage.htm" commandName="logoFileDTO"
	enctype="multipart/form-data" method="POST">

	
	<label for="fileLogo">Company Logo:</label>
	<form:errors id="file" path="file" cssClass="error"/>
	<input id="file" name="file" type="file" accept="image/gif">
	<input title="Upload" value="Upload" type="submit">
</form:form>
<p></p>


<p></p>
<p></p>
<a href="deleteTenant.htm" onclick="return confirm('Do you really want to delete tenant?')";>Delete Tenant</a> 

<div id="footer"></div>
</div>
</div>
