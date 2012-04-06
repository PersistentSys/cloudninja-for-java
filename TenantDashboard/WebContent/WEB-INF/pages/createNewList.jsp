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
<%@ page errorPage="/resources/jsp/errorpage.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div id="main">
<div id="mainContainer">
<h2>Create</h2>
<form:form commandName="createListModel" action="createList.htm">
 <fieldset>
        
     
        <legend>My Task</legend>
        <div class="editor-label">
            <label for="Subject">Subject</label>
        </div>
        <div class="editor-field">
        <form:input path="taskList.subject"/>
         <form:errors id="subject" path="taskList.subject" cssClass="error"/>
        </div>

        <div class="editor-label">
            <label for="StartDate">StartDate (yyyy-mm-dd)</label>
        </div>
        <div class="editor-field" >
        <form:input path="taskList.startDate"/>
         <form:errors id="startDate" path="taskList.startDate" cssClass="error"/>
        </div>

        <div class="editor-label">
            <label for="DueDate">DueDate (yyyy-mm-dd)</label>
        </div>
        <div class="editor-field">
            <form:input path="taskList.dueDate"/>
             <form:errors id="dueDate" path="taskList.dueDate" cssClass="error"/>
        </div>

        <div class="editor-label">
            <label for="Priority">Priority</label>
        </div>
        <div class="editor-field">
            <form:input path="taskList.priority"/>
             <form:errors id="priority" path="taskList.priority" cssClass="error"/>
        </div>

        <div class="editor-label">
            <label for="Details">Details</label>
        </div>
        <div class="editor-field">
            <form:input path="taskList.details"/>
             <form:errors id="details" path="taskList.details" cssClass="error"/>
        </div>

        <p>
            <input value="Create" type="submit">
        </p>
        </fieldset>
</form:form>

<div>
    <a href="showTenantHomePageList.htm">Back to List</a>
</div>
</div>
</div>



         
