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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page errorPage="/resources/jsp/errorpage.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "_http://www.w3.org/TR/html4/loose.dtd">
<head>
<script type="text/javascript" src="webresources/tablesorter/jquery-1.7.1.min.js"></script>
<script type="text/javascript">
function AJAXInteraction(url, callback) {

    var req = init();
    req.onreadystatechange = processRequest;
        
    function init() {
      if (window.XMLHttpRequest) {
        return new XMLHttpRequest();
      } else if (window.ActiveXObject) {
        return new ActiveXObject("Microsoft.XMLHTTP");
      }
    }
    
    function processRequest () {
      // readyState of 4 signifies request is complete
      if (req.readyState == 4) {
        // status of 200 signifies sucessful HTTP call
        if (req.status == 200) {
          if (callback) callback(req.responseXML);
        }
      }
    }

    this.doGet = function() {
      req.open("GET", url, true);
      req.send(null);
    };
}

function validateUserId() {
    var target = document.getElementById("tenantId");
    var val = target.value;
    var url = "isTenantIdAvailable.htm?tenantId=" + encodeURIComponent(target.value);    
    var target = document.getElementById("tenantId");
    var ajax = new AJAXInteraction(url, validateCallback);
    ajax.doGet();
}

function validateCallback(responseXML) {
   var msg = responseXML.getElementsByTagName("valid")[0].firstChild.nodeValue;
	var tenantIdErrDiv = document.getElementById("tenantIdErrDiv");
   if (msg == "false"){
	   tenantIdErrDiv.innerHTML='';
    } else {
    	tenantIdErrDiv.innerHTML='Service Alias Unavailable';
    }  
}

function hideDisplay(Object){

	var idp = Object.value;

	if(idp == 'CloudNinjaSts') {
		  document.getElementById('namePasswordDiv').style.display= 'block';
	} else {
		  // The name password field should not be visible and should be empty
		  document.getElementById('namePasswordDiv').style.display= 'none';
		  document.getElementById('memberName').value="";
		  document.getElementById('memberPassword').value="";
    }
	if(idp == "WindowsLiveID") {
	   document.getElementById('invitationCodeDiv').style.display= 'block';
	   document.getElementById('invitationCode').value=generateInvitationCode();
	} else {
	   //if not windows live id then should not be visible and should be empty
	   document.getElementById('invitationCodeDiv').style.display= 'none';
	   document.getElementById('invitationCode').value="";
	}
}

function generateInvitationCode() {
 // removed numeric 0 , alphabets 'l', 'o','O','I' to avoid confusion between numbers and alphabets
	chars = new Array ('1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k',
	'm','n','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z');
	charCount = chars.length;

	stringLength = 5;
	var invitationCode="";
    i = 0;
    do {
	   random = Math.floor(Math.random()*charCount);
	   char = chars[random];
	   invitationCode += char;
	   i++;
    }
	while (i<stringLength);
	return invitationCode;
}

function confirmLiveId() {
	var selectedOption=document.getElementById("identityProvider").value;

	if(selectedOption == "WindowsLiveID") {
		var r=confirm("Have you copied latest Invitation Code?");
	    return r;
		}
	return true;
}
</script>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
    // On first visit the first option will be selected & in case of errors previous selection will be retained
    var selectedOption=document.getElementById("identityProvider").value;  
     
    $("#identityProvider").val(selectedOption).attr('selected', true);
    document.getElementById('identityProvider').onchange();    
  });
</script>
<div id="main">
<div id="mainContainer">
  <form:form  commandName="provisioningTenantDTO" action="provisionTenant.htm" >
  	<p style="color:green"><font size="+1.5">${provisioningTenantDTO.successMessage}</font></p>
	<h2>Create a New Account</h2>
	<p> Use the form below to create a new account. </p>
    <div>
        <fieldset>
            <legend>Subscription Key</legend>
            <div class="editor-label">
                <label for="DemoKey">Demo Key</label>
            </div>
            <div class="editor-field">
            	<form:password path="demoKey"/>
                <form:errors path="demoKey" cssClass="error"></form:errors>
            </div>
        </fieldset>
        <fieldset>
            <legend>Company Information</legend>
            <div class="editor-label">
                <label for="TenantId">Service Alias</label>
            </div>
            <div class="editor-field">
            	<spring:bind path="tenantId">
					<input type="text" value="${provisioningTenantDTO.tenantId}" name="tenantId" id="tenantId" onkeyup="validateUserId()"/>
				</spring:bind>
				<div id="tenantIdErrDiv" class="error"></div>
                <form:errors path="tenantId" cssClass="error"></form:errors>
            </div>
            <div id="tenantIdError" class="error"></div>

            <div class="editor-label">
                <label for="CompanyName">Company Name</label>
            </div>
            <div class="editor-field">
            	<spring:bind path="companyName">
					<input type="text" value="${provisioningTenantDTO.companyName}" name="companyName" />
				</spring:bind>
                <form:errors path="companyName" cssClass="error"></form:errors>
            </div>
        </fieldset>
        
        <fieldset>
            <legend>Identity Provider</legend>
            <div class="editor-label">
                <label>Provider</label>
            </div>
            <div class="editor-field">
              	<form:select path="identityProvider" onchange="hideDisplay(this)">
					<form:options items="${provisioningTenantDTO.identityProviderList}" itemValue="name" itemLabel="name"/>
				</form:select>
            </div>
        </fieldset>
        
        <fieldset>
            <legend>Account Information</legend>
            <div class="editor-label">
                <label for="MemberId">User id</label>
            </div>

            <div class="editor-field">
            	<spring:bind path="memberId">
					<input type="text" value="${provisioningTenantDTO.memberId}" name="memberId" id="memberId" />
				</spring:bind>
                <form:errors path="memberId" cssClass="error"></form:errors>
            </div>
	        <div id="namePasswordDiv">
	            <div class="editor-label">
	                <label for="MemberName">Name</label>
	            </div>
	            <div class="editor-field">
	            	<spring:bind path="memberName">
						<input type="text" value="${provisioningTenantDTO.memberName}" name="memberName" id="memberName"/>
					</spring:bind>
	                <form:errors path="memberName" cssClass="error"></form:errors>
	            </div>
	
	            <div class="editor-label">
	                <label for="MemberPassword">Password</label>
	            </div>
	            <div class="editor-field">
	            	<form:password path="memberPassword"/>
	                <form:errors path="memberPassword" cssClass="error"></form:errors>
	            </div>
            </div>
       		<div id='invitationCodeDiv'>
				<div class="editor-label" >
					<label>Invitation Code (read only)</label>
				</div>
				<div class="editor-field">
				<spring:bind path="invitationCode">
					<input id="invitationCode" name="invitationCode" value="${provisioningTenantDTO.invitationCode}" type='text' readonly="readonly">
				</spring:bind>
				</div>
		   </div>	
        </fieldset>
        <p>
            <input value="Register" type="submit" onclick="return(confirmLiveId())">
        </p>
    </div>
  </form:form>
 <div>
 <div id="footer">
   </div>
   </div>
 </div>
</div>
</body>
