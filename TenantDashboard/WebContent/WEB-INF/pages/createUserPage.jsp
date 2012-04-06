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
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<script type="text/javascript">
	function hideDisplay(object){

		var idp = object.value;

		var memberIdErrElement = document.getElementById("memberIdError");
		if(null != memberIdErrElement) {
			memberIdErrElement.innerHTML = "";
			}
		
		if(idp == 'CloudNinjaSts') {
			  document.getElementById('namePasswordDiv').style.display= 'block';
		} else {
			  // The name password field should not be visible and should be empty
			  document.getElementById('namePasswordDiv').style.display= 'none';
			  document.getElementById('member.name').value="";
			  document.getElementById('member.password').value="";
			  document.getElementById('member.memberCompoundKey.memberId').value="";
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

	function loadPage() {
		var selectedOption=document.getElementById("identityProvider").value;
		
		if(selectedOption == 'CloudNinjaSts') {
			  document.getElementById('namePasswordDiv').style.display= 'block';
		} else {
			  // The name password field should not be visible and should be empty
			  document.getElementById('namePasswordDiv').style.display= 'none';
			  document.getElementById('member.name').value="";
			  document.getElementById('member.password').value="";
			  document.getElementById('member.memberCompoundKey.memberId').value="";
	    }
		if(selectedOption == "WindowsLiveID") {
		   document.getElementById('invitationCodeDiv').style.display= 'block';
		   document.getElementById('invitationCode').value=generateInvitationCode();
		} else {
		   //if not windows live id then should not be visible and should be empty
		   document.getElementById('invitationCodeDiv').style.display= 'none';
		   document.getElementById('invitationCode').value="";
		}
	}
</script>
</head>
<body onload="loadPage()">
<div id="main">
<div id="mainContainer">
<h2>Create User</h2>
<p> Use the form below to create a new account. </p>
  <form:form commandName="createManageUserPageModel" action="createUser.htm">
    <div>
          <fieldset>
	            <legend>Identity Provider</legend>
	            <div class="editor-label">
	                <label>Provider</label>
	            </div>
	            <div class="editor-field">
	              	<form:select path="identityProvider" onchange="hideDisplay(this)">
						<form:options items="${createManageUserPageModel.identityProviderList}" itemValue="name" itemLabel="name"/>
					</form:select>
	            </div>
	        </fieldset>
          <fieldset>
            <legend>Member</legend>
            <div class="editor-label">
                <label for="MemberUserName">User Id</label>
            </div>
            <div class="editor-field">
                <form:input path="member.memberCompoundKey.memberId"/>
                <form:errors id="memberIdError" path="member.memberCompoundKey.memberId" cssClass="error"/>
            </div>
            <div id="namePasswordDiv">
	             <div class="editor-label">
	                <label for="MemberRealName">Real Name</label>
	            </div>
	            <div class="editor-field">
	                <form:input path="member.name" />
	                <form:errors id="memberNameError" path="member.name" cssClass="error"/>
            </div>

            <div class="editor-label">
                <label for="MemberPassword">Password</label>
            </div>
            <div class="editor-field">
               <form:password path="member.password"/>
	               <form:errors id="passwordError" path="member.password" cssClass="error"/>
	            </div>
            </div>

            <div id='invitationCodeDiv'>
				<div class="editor-label" >
					<label>Invitation Code (read only)</label>
            </div>
            <div class="editor-field">
				<spring:bind path="invitationCode">
					<input id="invitationCode" name="invitationCode" value="${createManageUserPageModel.invitationCode}" type='text' readonly="readonly">
				</spring:bind>
				</div>
            </div>
            
            <div class="editor-label">
                <label for="MemberRoles">Roles</label>
            </div>
            <div class="editor-field">
              	<form:select path="member.role">
					<form:options items="${createManageUserPageModel.roleList}" itemValue="name" itemLabel="name"/>
				</form:select>
			</div>
         </fieldset>
        
        <p>
            <input value="Create" type="submit" onclick="return(confirmLiveId())"> <a href="showManageUsersList.htm">Back to List</a>
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
