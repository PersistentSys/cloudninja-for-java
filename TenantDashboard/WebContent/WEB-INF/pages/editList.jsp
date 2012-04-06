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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div id="main">
        <div id="mainContainer">
            

<h2>Edit</h2>

<script type="text/javascript" src="resources/js/date.js"></script>
<script type="text/javascript" src="resources/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript">
    function validateFields()
    { 
        var vaild = true;
        var startDate = $("#startInput").val();
        var dueDate = $("#dueInput").val();	 
        var priority = $("#priorityInput").val();
        
        var startDateErrDiv = document.getElementById('startDateErrDiv');
            startDateErrDiv.innerHTML='';
        var dueDateErrDiv=  document.getElementById('dueDateErrDiv');
            dueDateErrDiv.innerHTML='';
        var priorityErrDiv = document.getElementById('priorityErrDiv');
        	priorityErrDiv.innerHTML='';
            
        // Check for startDate
        if (startDate) {
        	startDate = lrtrim(startDate);
            if (startDate.length == 0){
            	startDateErrDiv.innerHTML='Start Date is required';
                vaild=false;
            } else {
                var validDate = Date.parseExact(startDate, "yyyy-mm-dd");
                if (validDate == null) {
                	startDateErrDiv.innerHTML='Invalid date format';
                    vaild=false;
                }
            }
        } else {
        	startDateErrDiv.innerHTML='Start Date is required';
            vaild=false;
        }

        // Check for dueDate
        if (dueDate) {
        	dueDate = lrtrim(dueDate);
            if (dueDate.length == 0){
            	dueDateErrDiv.innerHTML='Due Date is required';
                vaild=false;
            } else {
                var validDate = Date.parseExact(dueDate, "yyyy-mm-dd");
                if (validDate == null) {
                	dueDateErrDiv.innerHTML='Invalid date format';
                    vaild=false;
                }
            }
        } else {
        	dueDateErrDiv.innerHTML='Due Date is required';
            vaild=false;
        }
        // for priority
        
		if(priority) {
		    priority = lrtrim(priority);
		    if(priority.length==0)
		    {
		        priorityErrDiv.innerHTML='Priority is required';
		        vaild=false;
		    } else
		    {
		        if (!isDecimal(priority)) {
		            priorityErrDiv.innerHTML='Priority must be numeric';
		            vaild=false;
		        }
		    }
		} else {
		    priorityErrDiv.innerHTML='Priority is required';
		    vaild=false;
		}
        
        return vaild;
    }
    // Trims leading an trailing spaces
    function lrtrim( str ) 
    {
        return str.replace(/(?:(?:^|\n)\s+|\s+(?:$|\n))/g,'').replace(/\s+/g,' ');
    }
    // Checks if is decimal
    function isDecimal(str){
        if(isNaN(str) || str.indexOf(".")>0){
            return false;
        }
        return true;
    }
</script>

<form:form  commandName="editListModel" action="editList.htm" onsubmit="return validateFields()">  <fieldset>
        <legend>MyTask</legend>
        
         <div class="editor-label">
            <label for="TaskId">TaskId</label>
        </div>
        <div class="editor-field">
          ${ taskList.taskId }
            <form:hidden path="taskList.taskId"/>
        </div>

        <div class="editor-label">
            <label for="Subject">Subject </label>
        </div>
        <div class="editor-field">
            <form:input path="taskList.subject"/>
            <form:errors id="subject" path="taskList.subject" cssClass="error"/>
        </div>

        <div class="editor-label">
            <label for="StartDate">StartDate (yyyy-mm-dd)</label>
        </div>
        <div class="editor-field">
       	  <form:input path="taskList.startDate" id="startInput"/>
       	  <form:errors id="startDate" path="taskList.startDate" cssClass="error"/>
        </div>
        <div id="startDateErrDiv" class="error"></div>

        <div class="editor-label">
            <label for="DueDate">DueDate (yyyy-mm-dd)</label>
        </div>
        <div class="editor-field">
         	<form:input path="taskList.dueDate" id="dueInput"/>
         	<form:errors id="dueDate" path="taskList.dueDate" cssClass="error"/>
        </div>
        <div id="dueDateErrDiv" class="error"></div>

        <div class="editor-label">
            <label for="Priority">Priority</label>
        </div>
        <div class="editor-field">
       	  <form:input path="taskList.priority" id="priorityInput"/>
       	   <form:errors id="priority" path="taskList.priority" cssClass="error"/>
        </div>
        <div id="priorityErrDiv" class="error"></div>

        <div class="editor-label">
            <label for="Details">Details</label>
        </div>
        <div class="editor-field">
       	  <form:input path="taskList.details"/>
       	  <form:errors id="details" path="taskList.details" cssClass="error"/>
        </div>

        <p>
            <input value="Save" type="submit">
        </p>
    </fieldset>
</form:form>
<div>
    <a href="showTenantHomePageList.htm">Back to List</a>
</div>


            <div id="footer">
            </div>
            </div>
        </div>
