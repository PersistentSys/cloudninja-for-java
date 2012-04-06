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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div id="main">
<div id="mainContainer">
<h2>Create Random String</h2>
<p>Use the form below to generate a Random String.</p>
<form:form commandName="trafficSimulationDTO"
	action="showTrafficSimulationPage.htm">
	<div>
	<fieldset><legend>Tenant</legend>
	<div class="editor-label">
		<label>TenantID :</label>
		<label>&nbsp;&nbsp;${trafficSimulationDTO.tenantId}</label>
	</div>
	<div class="editor-label">
		<label>Random String :</label>
		<label>&nbsp;&nbsp;${trafficSimulationDTO.randomString}</label>
	</div>
	</fieldset>
	<p><input value="Generate Request" type="submit"></p>
	</div>
</form:form>
<div>
<div id="footer"></div>
</div>
</div>
</div>
