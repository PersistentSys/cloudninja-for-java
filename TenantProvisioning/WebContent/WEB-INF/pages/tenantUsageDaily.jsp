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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script type="text/javascript" src="webresources/tablesorter/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="webresources/tablesorter/jquery.tablesorter.min.js"></script> 
<div id="main">
    <div id="mainContainer">
        <%@ include file = "submenu.jsp" %>
        <h2>Tenant Usage: ${usageDTO.tenant} </h2>
        <p>
        <a href="javascript:history.go(-1)">&lt;&lt; Back</a>
        </p>
        <div id="grid">
            <table class="data-table" id="tenantTable">
            <thead>
                 <tr class="head">
                 <th scope="col"><a href="#">Snapshot time</a></th>
                 <th scope="col"><a href="#">Database size (kb)</a></th>
                 <th scope="col"><a href="#">Database in - Int. (kb)</a></th>
                 <th scope="col"><a href="#">Database out - Int. (kb)</a></th>
                 <th scope="col"><a href="#">Web in (kb)</a></th>
                 <th scope="col"><a href="#">Web out (kb)</a></th>
                 <th scope="col"><a href="#">Web Requests</a></th>
                 <th scope="col"><a href="#">Blob size(kb)</a></th>
                 <th scope="col"><a href="#">Req. for Blob (kb)</a></th>
                 <th scope="col"><a href="#">Resp. for Blob (kb)</a></th>
                 <th scope="col"><a href="#">Transactions for Blob</a></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="list" items="${usageDTO.usage}" varStatus="varStatus" >
                  <tr> 
                    <td>${ list.month } ${ list.year } ${ list.day }</td>
                    <td>${ list.databaseSize }</td>
                    <td>${ list.databaseBandwidth_Ingress }</td>
                    <td>${ list.databaseBandwidth_Egress }</td>
                    <td>${ list.webAppBandwithUse_SC }</td>
                    <td>${ list.webAppBandwithUse_CS }</td>
                    <td>${ list.webAppRequests }</td>
                    <td>${ list.blobStoreUsage }</td>
                    <td>${ list.totalRequestPacketSize }</td>
                    <td>${ list.totalResponsePacketSize }</td>
                    <td>${ list.totalStorageTransactions }</td>
                  </tr>        
                </c:forEach>
            </tbody>
            </table>
        </div>
    </div>

    <script type="text/javascript" language="javascript">
    $(document).ready(function(){
        $("#tenantTable").tablesorter({
        	sortList: [[0,0]]}); 
        });
    </script>
</div>
