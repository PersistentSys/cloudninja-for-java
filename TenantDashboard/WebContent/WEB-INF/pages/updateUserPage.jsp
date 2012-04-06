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

<div id="main">
<div id="mainContainer">
<h2>Edit User</h2>
<p> Use the form below to update user. </p>
  <form:form commandName="updateManageUserPageModel" action="updateUser.htm">
    <div>
          <fieldset>
            <legend>Member</legend>
            <div class="editor-label">
                <label for="MemberUserName"><font color="black">User Name</font></label>
            </div>
            
            <div class="editor-field">
                 ${member.memberCompoundKey.memberId}
                  <form:hidden path="member.memberCompoundKey.memberId"/>
            </div>  		

            <div class="editor-label">
                <label for="MemberRealName"><font color="black">Real Name</font></label>
            </div>
            <div class="editor-field">
            	 ${member.name}
            </div>
            
            <div class="editor-label">
                <label for="MemberRoles"><font color="black">Role</font></label>
            </div>
            <div class="editor-field">
              	<form:select path="member.role">
					<form:options items="${updateManageUserPageModel.roleList}" itemValue="name" itemLabel="name"/>
				</form:select>
			</div>
			
			<div class="editor-label">
                <label for="MemberRoles"><font color="black">isEnabled</font></label>
            </div>
            <div class="editor-field">
              	<form:checkbox path="member.enabled"/>
			</div>
			<form:hidden path="member.created"/>
			<form:hidden path="member.password"/>
			<form:hidden path="member.liveInvitationCode"/>
			<form:hidden path="member.liveGUID"/>
         </fieldset>
        <p>
            <input value="Save" type="submit">  <a href="showManageUsersList.htm">Back to List</a>
        </p>
    </div>
  </form:form>
 <div>
 <div id="footer">
   </div>
   </div>
</div>
   </div>
