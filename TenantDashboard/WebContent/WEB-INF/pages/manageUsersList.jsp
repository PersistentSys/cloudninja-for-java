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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
            <a href="createManageUser.htm">Create New</a>
            </p>
            <div id="grid">
                <center>
                    <table class="data-table" >
                        <thead>
                            <tr class="head">
                                <th scope="col" >Actions</th>
                                <th scope="col" ><a href="#">Name</a></th>
                                <th scope="col"><a href="#">UserName</a></th>
                                <th scope="col"><a href="#">Role</a></th>
                                <th scope="col"><a href="#">Created</a></th>
                                <th scope="col"><a href="#">Active</a></th>
                            </tr>
                        </thead>
                        <tbody >
                            <c:if test="${empty manageUsersDTO.manageUsersList}"> No List found.</c:if>
                            <c:if test="${not empty manageUsersDTO.manageUsersList}">
                                <c:forEach items="${manageUsersDTO.manageUsersList}" var="usersList" varStatus="varStatus">
                                    <tr>
                                        <td>
                                        <form:form action="deleteUserPage.htm" commandName="manageUsersDTO.manageUsersList[${varStatus.index}]" method="POST" id="formDelete${varStatus.index}">
                                            <spring:bind path="name">
                                                <input type="hidden" value="${usersList.name}" name="name" />
                                            </spring:bind>
                                            <spring:bind path="memberCompoundKey.memberId">
                                                <input type="hidden" value="${usersList.memberCompoundKey.memberId}" name="memberCompoundKey.memberId" />
                                            </spring:bind>
                                            <spring:bind path="role">
                                                <input type="hidden" value="${usersList.role}" name="role" />
                                            </spring:bind>
                                            <spring:bind path="created">
                                                <input type="hidden" value='<fmt:formatDate value="${usersList.created}" pattern="MM/dd/yyyy hh:mm a" />' name="created"/>
                                            </spring:bind>
                                            <a href="#" onclick="document.getElementById('formUpdate${varStatus.index}').submit()">Edit</a> | <a href="#" onclick="document.getElementById('formDelete${varStatus.index}').submit()">Delete</a>
                                        </form:form>

                                        <form:form action="updateUserPage.htm" commandName="manageUsersDTO.manageUsersList[${varStatus.index}]" method="POST" id="formUpdate${varStatus.index}"> 
                                            <td>${ usersList.name}</td>
                                            <td>${ usersList.memberCompoundKey.memberId }</td>
                                            <td>${ usersList.role }</td>
                                            <td><fmt:formatDate value="${usersList.created}" pattern="MM/dd/yyyy hh:mm a"/></td>
                                            <td>
                                                <c:choose> 
                                                    <c:when test="${usersList.enabled}">Yes </c:when> 
                                                    <c:otherwise>No</c:otherwise> 
                                                </c:choose> 
                                            </td>
                                            <spring:bind path="name">
                                                <input type="hidden" value="${usersList.name}" name="name" />
                                            </spring:bind>
                                            <spring:bind path="memberCompoundKey.memberId">
                                                <input type="hidden" value="${usersList.memberCompoundKey.memberId}" name="memberCompoundKey.memberId" />
                                            </spring:bind>
                                            <spring:bind path="role">
                                                <input type="hidden" value="${usersList.role}" name="role" />
                                            </spring:bind>
                                            <spring:bind path="created">
                                                <input type="hidden" value='<fmt:formatDate value="${usersList.created}" pattern="MM/dd/yyyy hh:mm a" />' name="created"/>
                                            </spring:bind>
                                            <spring:bind path="enabled">
                                                <input type="hidden" value="${usersList.enabled}" name="enabled" />
                                            </spring:bind>
                                            <spring:bind path="password">
                                                <input type="hidden" value="${usersList.password}" name="password" />
                                            </spring:bind>
											<spring:bind path="liveInvitationCode">
                                                <input type="hidden" value="${usersList.liveInvitationCode}" name="liveInvitationCode" />
                                            </spring:bind>
                                            <spring:bind path="liveGUID">
                                                <input type="hidden" value="${usersList.liveGUID}" name="liveGUID" />
                                            </spring:bind>
                                        </form:form>
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
