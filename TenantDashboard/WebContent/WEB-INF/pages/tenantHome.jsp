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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../resources/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../resources/tablesorter/jquery.tablesorter.min.js"></script>
<title>List</title>
</head>
<script type="text/javascript" language="javascript">
    $(document).ready(function(){
        $("table")
        .tablesorter({ headers: {0:{sorter: false}},sortList: [[1,0]]});
    });
</script> 

    <div id="main">
        <div id="mainContainer">
            <h2>List</h2>
            <p>
            <a href="createNewList.htm">Create New</a>
            </p>
            <div id="grid">
                <center>
                    <table class="data-table" >
                        <thead>
                            <tr class="head" >
                                <th scope="col" >Actions</th>
                                <th scope="col" ><a href="#">TaskId</a></th>
                                <th scope="col"><a href="#">Subject</a></th>
                                <th scope="col"><a href="#">StartDate</a></th>
                                <th scope="col"><a href="#">DueDate</a></th>
                                <th scope="col"><a href="#">Priority</a></th>
                                <th scope="col"><a href="#">Details</a></th>
                            </tr>
                        </thead>
                        <tbody >
                            <c:if test="${not empty taskListDTO.taskList}">
                                <c:forEach items="${taskListDTO.taskList}" var="task" varStatus="varStatus">
                                <tr>
                                    <td>
                                        <form:form action="editListPage.htm" commandName="taskListDTO.taskList[${varStatus.index}]" method="POST" id="formedit${varStatus.index}">
                                            <spring:bind path="taskId">
                                                <input type="hidden" value="${task.taskId}" name="taskId" />
                                            </spring:bind>
                                            <spring:bind path="subject">
                                                <input type="hidden" value="${ task.subject }" name="subject" />
                                            </spring:bind>
                                            <spring:bind path="startDate">
                                                <input type="hidden" value="${ task.startDate }" name="startDate" />
                                            </spring:bind>
                                            <spring:bind path="dueDate">
                                                <input type="hidden" value="${ task.dueDate }" name="dueDate" />
                                            </spring:bind>
                                            <spring:bind path="priority">
                                                <input type="hidden" value="${ task.priority }" name="priority" />
                                            </spring:bind>
                                            <spring:bind path="details">
                                                <input type="hidden" value="${ task.details }" name="details" />
                                            </spring:bind>

                                            <a href="#" onclick="document.getElementById('formedit${varStatus.index}').submit()">Edit</a>|<a href="#" onclick="document.getElementById('formdetail${varStatus.index}').submit()">Details</a>|	<a href="#" onclick="document.getElementById('formdelete${varStatus.index}').submit()">Delete</a>
                                        </form:form>
                                        <form:form action="listDetails.htm" commandName="taskListDTO.taskList[${varStatus.index}]" method="POST" id="formdetail${varStatus.index}">
                                            <spring:bind path="taskId">
                                                <input type="hidden" value="${task.taskId}" name="taskId" />
                                            </spring:bind>
                                            <spring:bind path="subject">
                                                <input type="hidden" value="${ task.subject }" name="subject" />
                                            </spring:bind>
                                            <spring:bind path="startDate">
                                                <input type="hidden" value="${ task.startDate }" name="startDate" />
                                            </spring:bind>
                                            <spring:bind path="dueDate">
                                                <input type="hidden" value="${ task.dueDate }" name="dueDate" />
                                            </spring:bind>
                                            <spring:bind path="priority">
                                                <input type="hidden" value="${ task.priority }" name="priority" />
                                            </spring:bind>
                                            <spring:bind path="details">
                                                <input type="hidden" value="${ task.details }" name="details" />
                                            </spring:bind>

                                        </form:form>
                                        <form:form action="deleteList.htm" commandName="taskListDTO.taskList[${varStatus.index}]" method="POST" id="formdelete${varStatus.index}" >
                                            <spring:bind path="taskId">
                                                <input type="hidden" value="${task.taskId}" name="taskId" />
                                            </spring:bind>
                                            <spring:bind path="subject">
                                                <input type="hidden" value="${task.subject}" name="subject" />
                                            </spring:bind>
                                        </form:form>
                                    </td>
                                    <td>${ task.taskId }</td>
                                    <td>${ task.subject }</td>
                                    <td>${ task.startDate }</td>
                                    <td>${ task.dueDate }</td>
                                    <td>${ task.priority }</td>
                                    <td>${ task.details }</td>
                                </tr>
                                </c:forEach>
                            </c:if>
                        </tbody>
                    </table>
                </center>
            </div>
            <div id="footer"></div>
        </div>
    </div>
