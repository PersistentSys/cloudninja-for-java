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
</head>
<%
String cookieName = "CLOUDNINJAAUTH";
Cookie cookies [] = request.getCookies ();

Cookie authCookie = null;
String userName = "";
String role = "";
String tenantId= "";
if (cookies != null)
  {
    for (int i = 0; i < cookies.length; i++) 
    {
       if (cookies [i].getName().equals (cookieName))
         {
    	   authCookie = cookies[i];
           String [] valArray = authCookie.getValue().split("!");
           userName = valArray[0];
           tenantId = (userName.split(":"))[1];
           break;
        }
   }
 }
%>
<script type="text/javascript" language="javascript">
    $(document).ready(function(){
        $("table")
        .tablesorter({sortList: [[0,0]]});
    });
</script> 
    <div id="main">
        <div id="mainContainer">
            <h2>
            Daily Metering results for <%=tenantId %></h2>
            <div id="grid"> 
                <table class="data-table">
                    <thead>
                        <tr class="head">
                            <th scope="col">
                                <a href="#">Tenant</a>
                            </th>
                            <th scope="col">
                                <a href="#">Date</a>
                            </th>
                            <th scope="col">
                                <a href="#">Database size (kb)</a>
                            </th>
                            <th scope="col">
                                <a href="#">Database in - Int. (kb)</a>
                            </th>
                            <th scope="col">
                                <a href="#">Database out - Int. (kb)</a>
                            </th>
                            <th scope="col">
                                <a href="#">Web in (kb)</a>
                            </th>
                            <th scope="col">
                                <a href="#">Web out (kb)</a>
                            </th>
                            <th scope="col">
                                <a href="#">Blob store use (kb)</a>
                            </th>
                            <th scope="col">
                                <a href="#">Req. for Blob (kb)</a>
                            </th>
                            <th scope="col">
                                <a href="#">Resp. for Blob (kb)</a>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        
                            <c:if test="${empty meteringDTO.meteringList}"> No orders found.</c:if>
                            <c:if test="${not empty meteringDTO.meteringList}">
                                <c:forEach items="${meteringDTO.meteringList}" var="list">
                                    <tr>
                                        <td>${ list.tenantId }</td>
                                        <td>${ list.day }/${ list.month }/${ list.year } </td>
                                        <td>${ list.databaseSize }</td>
                                        <td>${ list.databaseBandwidth_Ingress }</td>
                                        <td>${ list.databaseBandwidth_Egress }</td>
                                        <td>${ list.webAppBandwithUse_SC }</td>
                                        <td>${ list.webAppBandwithUse_CS }</td>
                                        <td>${ list.blobStoreUsage }</td>
                                        <td>${ list.totalRequestPacketSize }</td>
                                        <td>${ list.totalResponsePacketSize }</td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                    </tbody>
                </table>
            </div>
            <div id="footer"></div>
        </div>
    </div>
