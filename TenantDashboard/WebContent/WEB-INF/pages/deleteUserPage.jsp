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


<div id="main">
<div id="mainContainer">
<h2>Delete User</h2>
<p> Are you sure you want to delete this ? </p>
  <form:form commandName="deleteManageUserPageModel" action="deleteUser.htm">
    <div>
          <fieldset>
            <legend>Member</legend>
            <div class="editor-label">
                <label for="MemberId"><font color="black">Member ID</font></label>
            </div>
            <div class="editor-field">
            	${  member.memberCompoundKey.memberId }
                <form:hidden id="MemberId" path="member.memberCompoundKey.memberId"/>
            </div>
            
             <div class="editor-label">
                <label for="MemberCreated"><font color="black">Created</font></label>
            </div>
            <div class="editor-field">
            	<fmt:formatDate value="${member.created}" pattern="MM/dd/yyyy hh:mm a"/>
            	<form:hidden path="member.created"/>
            </div>
     
            <div class="editor-label">
                <label for="MemberRole"><font color="black">Role</font></label>
            </div>
           <div class="editor-field">
           		${ member.role }
                <form:hidden path="member.role"/>
            </div>
            
			<div class="editor-label">
                <label for="MemberName"><font color="black">Name</font></label>
            </div>
            <div class="editor-field">
            	${member.name }
                <form:hidden path="member.name"/>
            </div>
         </fieldset>
        <p>
         <input value="Delete User" type="submit"> <a href="showManageUsersList.htm">Back to List</a>
        </p>
    </div>
  </form:form>
 <div>
 <div id="footer">
   </div>
   </div>
</div>
   </div>
