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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link href="webresources/Site.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="webresources/tablesorter/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="webresources/tablesorter/jquery.tablesorter.min.js"></script> 
</head>
<body>
   <script type="text/javascript" language="javascript">
   $(document).ready(function(){
       $("#taskScheduleTable").tablesorter({
      	 headers: { 
	            0: { 
	                sorter: false 
	               }
          },
      	sortList: [[1,0]]}); 
     });
   </script>
   <div id="main">
         <div id="mainContainer">
            <%@ include file = "submenu.jsp" %>
           <h2>Tasks Schedule</h2>
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
            <div id="grid">
              <table class="data-table" id="taskScheduleTable">
                <thead>
                   <tr class="head">
                      <th scope="col">Actions</th>
                      <th scope="col"><a href="#">TaskId</a></th>
                      <th scope="col"><a href="#">Frequency (mins)</a></th>
                      <th scope="col"><a href="#">Next Scheduled Start Time</a></th>
                    </tr>
                </thead>
                <tbody>
                   <c:if test="${empty taskScheduleListDTO.taskScheduleList}">No List found.</c:if>
                   <c:if test="${not empty taskScheduleListDTO.taskScheduleList}">
                      <c:forEach items="${taskScheduleListDTO.taskScheduleList}" var="taskSchedule" varStatus="varStatus">
                         <tr>
                            <form:form action="editTaskSchedulePage.htm" commandName="taskScheduleListDTO.taskScheduleList[${varStatus.index}]" method="POST" id="form${varStatus.index}">
                               <td> <a href="#" onclick="document.getElementById('form${varStatus.index}').submit()">Edit</a> </td>
                               <td>${taskSchedule.taskId}</td>
                               <td>${taskSchedule.frequency}</td>
                               <td><fmt:formatDate value="${taskSchedule.nextScheduledStartTime}" pattern="MM/dd/yyyy HH:mm" /></td>
                               
                               <spring:bind path="taskId">
                                  <input type="hidden" value="${taskSchedule.taskId}" name="taskId" />
                               </spring:bind>
                               <spring:bind path="frequency">
                                  <input type="hidden" value="${taskSchedule.frequency}" name="frequency" />
                               </spring:bind>
                               <spring:bind path="nextScheduledStartTime">
                                  <input type="hidden" value='<fmt:formatDate value="${taskSchedule.nextScheduledStartTime}" pattern="MM/dd/yyyy HH:mm" />' name="nextScheduledStartTime" />
                               </spring:bind>
                            </form:form>
                         </tr>
                      </c:forEach>
                   </c:if>
                </tbody>
              </table>
            </div>
            <div id="footer"></div>
         </div>
   </div>
</body>
</html>
