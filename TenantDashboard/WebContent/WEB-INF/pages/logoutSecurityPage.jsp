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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--<head>-->
<!--<meta http-equiv="content-type" content="text/html; charset=UTF-8">-->
<!--<link href="resources/Site.css" rel="stylesheet" type="text/css">-->
<!--</head>-->
<!--<c:redirect url="/j_spring_security_logout"></c:redirect>-->

<html>
<head>
<script type="text/javascript">
function fireLogout()
{
	document.submitForm.submit();
}
</script>
</head>
<body onload="fireLogout()" >
<form name="submitForm" action="/logout.htm">
</form>
</body>
</html> 

