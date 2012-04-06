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
<div id="mainContainer"><%@ include file="submenu.jsp"%>
<h2>Completed Task Details</h2>
<p><a href="javascript:history.go(-1)"> Back</a></p>
<form:form commandName="taskCompletionDTO">
	<div>
	<fieldset><legend>TaskCompletionRecord</legend>
	<div class="editor-label"><label for="Task Name">Task
	Name</label></div>
	<div class="editor-field">${taskCompletion.taskName}</div>

	<div class="editor-label"><label for="Completion Time">Completion
	Time</label></div>
	<div class="editor-field"><fmt:formatDate
		value="${taskCompletion.completionTime}" pattern="dd-MM-yyyy hh:mm:SS" />
	</div>

	<div class="editor-label"><label for="MemberRole">Elapsed
	Time(seconds)</label></div>
	<div class="editor-field">${taskCompletion.elapsedTime}</div>

	<div class="editor-label"><label for="MemberName">Details</label>
	</div>
	<div class="editor-field">${taskCompletion.details}</div>
	</fieldset>
	<p><a href="showTaskCompletionList.htm">Back to List</a></p>
	</div>
</form:form>
<div>
<div id="footer"></div>
</div>
</div>
</div>
