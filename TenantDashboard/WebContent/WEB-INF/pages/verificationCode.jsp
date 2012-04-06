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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
body {
    font-family: Arial,Helvetica,sans-serif;
    height: 100%;
    text-align: center;
    width: 100%;
}
.SignInContent {
    border: 1px solid #BBBBBB;
    height: 150px;
    margin-left: auto;
    margin-right: auto;
    position: relative;
    text-align: center;
    width: 340px;
}
.Banner{
    background: none repeat scroll 0 0 #EEEEEE;
    border-left: 1px solid #BBBBBB;
    border-right: 1px solid #BBBBBB;
    border-top: 1px solid #BBBBBB;
    margin-left: auto;
    margin-right: auto;
    padding-bottom: 10px;
    padding-top: 10px;
    text-align: center;
    width: 340px;
}

.Header {
    margin-bottom: 10px;
    margin-left: auto;
    margin-right: auto;
    padding: 10px;
    text-align: center;
}
.LeftArea {
    height: 50%;
    left: 0;
    padding: 15px;
    position: absolute;
    top: 0;
    width: 320px;
}
</style>
<script type="text/javascript">
function trim(x){
	return x.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
	};
function validateEmpty(){
		var invitationCodeText=document.getElementById("invitationCode").value;
		if(invitationCodeText!=null){
			invitationCodeText=trim(invitationCodeText);
		}

	if(invitationCodeText==null || invitationCodeText.trim()=="") {
		alert("Please provide valid code");
	    return false;
	} 
	 return true;
}
</script>
</head>
<body>
<div id="Main" style="">
    <div id="Banner" class="Banner"><b>Sign in for the first time</b></div>
    <div id="SignInContent" class="SignInContent">
        <div id="LeftArea" class="LeftArea" style="">
            <div class="Header">Enter invitation code </div>
            <form method="post" action="validateInvitationCode.htm">
                <input type="text" name="invitationCode" id="invitationCode"/>
                <input type="hidden" name="wresult" value="${acsToken}"/>
                <input type="hidden" name="liveGuid" value="${liveGuid}"/>
                <br><br>
                <input type="submit" value="Continue" onclick="return(validateEmpty())"/>
            </form>
        </div> 
    </div>
</div>
</body>
</html>
