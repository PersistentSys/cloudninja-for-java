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
    <h2>Tenant Usage</h2>

    <p> Run metering task  <a href="RunMeteringManually.htm">manually</a> </p>
    <script type="text/javascript" language="javascript">
	    $(document).ready(function(){
	        $("#usageTable").tablesorter({
	        	 headers: { 
		            0: { 
		                sorter: false 
		               }
                },
	        	sortList: [[2,0]]}); 
	        });
	</script>
    <script type="text/javascript">
    function submitForm()
    {
        var d=new Date();
        var yearStr= d.getUTCFullYear().toString();
        document.getElementById("meteringForm").year.value=yearStr;    
        document.getElementById("meteringForm").submit();
    }
    </script>

    <form:form commandName="GetDetails" action="Metering.htm" id="meteringForm">
	    <form:select id="GetDetails.month" path="month" onchange="submitForm()">
	    <form:option value="" label="">${GetDetails.monthName}</form:option>
	    <form:option value="1" label="1">January</form:option>
	    <form:option value="2" label="1">February</form:option>
	    <form:option value="3" label="1">March</form:option>
	    <form:option value="4" label="1">April</form:option>
	    <form:option value="5" label="1">May</form:option>
	    <form:option value="6" label="1">June</form:option>
	    <form:option value="7" label="1">July</form:option>
	    <form:option value="8" label="1">August</form:option>
	    <form:option value="9" label="1">September</form:option>
	    <form:option value="10" label="1">October</form:option>
	    <form:option value="11" label="1">November</form:option>
	    <form:option value="12" label="1">December</form:option>
	    </form:select>
	     <input type="hidden" name="year" id="hiddenYear"/>
    </form:form>
        <div id="grid">
            <table class="data-table" id="usageTable">
               <thead>
                    <tr class="head">
                    <th scope="col"></th>
                    <th scope="col"><a href="#">Tenant</a></th>
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
                            <td>
                                <form:form cssStyle="width:23px" action="viewTenantMetering.htm" commandName="usageDTO.usage[${varStatus.index}]"
                                 method="POST" id="form${varStatus.index}">
	                                <spring:bind path="tenantId">
	                                <input type="hidden" value="${list.tenantId}" name="tenantId" />
	                                </spring:bind>
	                                <spring:bind path="month">
	                                <input type="hidden" value="${list.month}" name="month" />
	                                </spring:bind>
	                                <spring:bind path="year">
	                                <input type="hidden" value="${list.year}" name="year" />
	                                </spring:bind>
	                                <a href="#" onclick="document.getElementById('form${varStatus.index}').submit()">View</a>
                                </form:form>
                            </td>
                            <td>${ list.tenantId }</td>
                            <td>${ list.month } ${ list.year }</td>
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
</div>
