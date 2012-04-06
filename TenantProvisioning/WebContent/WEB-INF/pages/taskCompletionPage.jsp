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
    <script type="text/javascript" src="webresources/tablesorter/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="webresources/tablesorter/jquery.tablesorter.min.js"></script>
    <script type="text/javascript" src="webresources/tablesorter/jquery.tablesorter.pager.js"></script>

</head>
    <script type="text/javascript" language="javascript">
	    $(document).ready(function(){
	        $("table")
		        .tablesorter({ headers: {2:{sorter: false}}})
		        .tablesorterPager({container: $("#pager"),size:40});
	        });
    </script> 

    <div id="main">
        <div id="mainContainer"><%@ include file="submenu.jsp"%>
            <h2>Results of the Completed Tasks</h2>
            <p>UTC Time: ${taskCompletionDTO.utcDate}</p>
            <div id="grid">
                <center>
                    <table class="data-table">
                        <thead>
                            <tr class="head" bgcolor="black">
                                <th scope="col"><a href="#">Task</a></th>
                                <th scope="col"><a href="#">Finished at</a></th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty taskCompletionDTO.taskCompletionList}"> No List found.</c:if>
                            <c:if test="${not empty taskCompletionDTO.taskCompletionList}">

                            <c:forEach items="${taskCompletionDTO.taskCompletionList}"
                            var="taskCompletionList" varStatus="varStatus">
                            <tr>
                                <form:form action="taskCompletionDetailsPage.htm"
                                    commandName="taskCompletionDTO.taskCompletionList[${varStatus.index}]"
                                    method="POST" id="formTaskCompletionDetails${varStatus.index}">

                                    <spring:bind path="id">
                                        <input type="hidden" value="${taskCompletionList.id}" name="id" />
                                    </spring:bind>
                                    <spring:bind path="taskName">
                                        <input type="hidden" value="${taskCompletionList.taskName}"
                                            name="taskName" />
                                    </spring:bind>
                                    <spring:bind path="completionTime">
                                        <input type="hidden"
                                            value='<fmt:formatDate value="${taskCompletionList.completionTime}" pattern="MM-dd-yyyy hh:mm:SS" />'
                                            name="completionTime" />
                                    </spring:bind>
                                    <spring:bind path="elapsedTime">
                                        <input type="hidden" value="${taskCompletionList.elapsedTime}"
                                            name="elapsedTime" />
                                    </spring:bind>
                                    <spring:bind path="details">
                                        <input type="hidden" value="${taskCompletionList.details}"
                                            name="details" />
                                    </spring:bind>

                                </form:form>
                                <td>${ taskCompletionList.taskName}</td>
                                <td><fmt:formatDate
                                    value="${taskCompletionList.completionTime}"
                                    pattern="MM-dd-yyyy hh:mm:SS" /></td>
                                <td><a href="#"	onclick="document.getElementById('formTaskCompletionDetails${varStatus.index}').submit()">Details</a></td>
                            </tr>
                            </c:forEach>

                            </c:if>
                        </tbody>
                    </table>
                </center>
            </div>
            <div id="footer">
                <div id="pager">
                    <form>
                        <img src="webresources/tablesorter/icons/first.png" class="first"/>
                        <img src="webresources/tablesorter/icons/prev.png" class="prev"/>
                        <input type="text" class="pagedisplay"/>
                        <img src="webresources/tablesorter/icons/next.png" class="next"/>
                        <img src="webresources/tablesorter/icons/last.png" class="last"/>
                        <select class="pagesize">
                            <option value="20">20</option>
                            <option selected='selected'value="40">40</option>
                            <option value="60">60</option>
                        </select>
                    </form>
                </div>
            </div>
        </div>
    </div>
