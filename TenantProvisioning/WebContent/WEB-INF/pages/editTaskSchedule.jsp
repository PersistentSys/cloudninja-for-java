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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
<script type="text/javascript" src="webresources/tablesorter/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="webresources/tablesorter/date.js"></script>
<script type="text/javascript">
    function validateFields()
    { 
        var vaild = true;
        var freq = $("#frequency").val();
        var scheduledTime = $("#scheduledTime").val();	  
        var freqerrDiv = document.getElementById('frequencyErrDiv');
        freqerrDiv.innerHTML='';
        var timeErrDiv=  document.getElementById('scheduledTimeErrDiv');
        timeErrDiv.innerHTML='';
        // check for frequency in minutes
        if(freq){
            freq = lrtrim(freq);
            if (freq.length == 0) {
                freqerrDiv.innerHTML='The frequency is required';
                vaild=false;
            } else if (!isDecimal(freq)) {
                freqerrDiv.innerHTML='Invalid minutes specified';
                vaild=false;
            }
        } else {
            freqerrDiv.innerHTML='The frequency is required';
            vaild=false;
        }
        // Check for next schedule time
        if (scheduledTime) {
            scheduledTime = lrtrim(scheduledTime);
            if (scheduledTime.length == 0){
                timeErrDiv.innerHTML='Scheduled time is required';
                vaild=false;
            } else {
                var validDate = Date.parseExact(scheduledTime, "mm/dd/yyyy HH:mm");
                if (validDate == null) {
                    timeErrDiv.innerHTML='Invalid date format mm/dd/yyyy HH:mm';
                    vaild=false;
                }
            }
        } else {
            timeErrDiv.innerHTML='Scheduled time is required';
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
</head>
<body>
   <div id="main">
      <div id="mainContainer">
         <h2>Edit</h2>
         <p>
           <script type="text/javascript">
                var d=new Date();
                var sep = "/";
                var col = ":";
                var date = d.getUTCDate().toString();
                var month = (d.getUTCMonth() + 1).toString();
                var year= d.getUTCFullYear().toString();
                
                var hours =  d.getUTCHours().toString();
                var minutes = d.getUTCMinutes().toString();
                
                if (date.length == 1) {
                  date= "0" + date;
                }
                if (month.length == 1) {
                  month = "0" + month ;
                }
                if (hours.length == 1) {
                  hours = "0" + hours;
                }
                if (minutes.length == 1) {
                  minutes= "0" + minutes;
                }
                document.write("UTC Time: " + month + sep + date + sep + year);
                document.write(" " + hours + col + minutes);
            </script>
         </p>
         <form:form commandName="editTaskScheduleDTO" action="updateTaskSchedule.htm" onsubmit="return validateFields()">
            <fieldset>
               <legend>ScheduledTaskModel</legend>
               <div class="editor-label">
                  <label for="TaskId">TaskID</label>
               </div>
               <div class="editor-field">
                   <form:input path="taskSchedule.taskId" readonly="true"/>    
               </div>
               <div class="editor-label">
                   <label for="OperationsKey">Operations Key</label>
               </div>
               <div class="editor-field">
                   <form:password path="operationKey" />
                   <form:errors id="operationKey" path="operationKey" cssClass="error"/>
               </div>
               <div class="editor-label">
                   <label for="Frequency">Frequency (mins)</label>
               </div>
               <div class="editor-field">
                   <form:input path="taskSchedule.frequency" id="frequency"/>
               </div>
               <div class="error" id="frequencyErrDiv"></div>
               <div class="editor-label">
                   <label for="NextScheduledStartTime">Next scheduled time</label>
               </div>
               <div class="editor-field">
                   <form:input path="taskSchedule.nextScheduledStartTime" id="scheduledTime"/>
               </div>
               <div id="scheduledTimeErrDiv" class="error"></div>
              <p>
                 <input value="Save" type="submit">
              </p>
            </fieldset>
         </form:form>
         <a href="TaskScheduleList.htm">Back to List</a>
      </div>
      <div id="footer"></div>
   </div>
</body>
</html>
