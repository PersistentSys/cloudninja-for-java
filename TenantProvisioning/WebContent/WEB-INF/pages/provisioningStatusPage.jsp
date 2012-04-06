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
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "_http://www.w3.org/TR/html4/loose.dtd">
<head>
    <script type="text/javascript" src="webresources/tablesorter/jquery-1.7.1.min.js"></script>
</head>
<body>
    <script type="text/javascript">
        var count;
        var maxCount;
        var intervalId=0;
        var tenantId;
        var prevArrayMessageSize=0;

        $(document).ready(function(){
            count = 0;
            maxCount = 60;
            tenantId =  "<%= request.getParameter("tenantId")%>";
            if (tenantId != "null" && tenantId.length > 0) {
                getProvisioningStatus();
                intervalId = setInterval("getProvisioningStatus()", 2000);
            } else {
                alert("Invalid request");
            }
        });
    </script>
    
    <script type="text/javascript">
        function getProvisioningStatus(){
            var url = "contextPath/getProvisioningStatusData.htm";
            var contextPath = "<%= request.getContextPath()%>";
            url = url.replace('contextPath', contextPath);
            $.ajax({
                dataType: "jsonp",
                url: url,
                data: {'tenantId':tenantId},
                success: function(result){
                    // Display stats respective div
                    displayStatus(result.statusMessageArray,result.tenantUrl);
                    count = count+1;
                    prevArrayMessageSize = result.statusMessageArray.length;
                },
                error:function(x,e){
                    if(x.status==0){
                        alert('You are offline!!\n Please Check Your Network.');
                    }else if(x.status==404){
                        alert('Requested URL not found.');
                    }else if(x.status==500){
                        alert('Internel Server Error.');
                    }else if(e=='timeout'){
                        alert('Request Time out.');
                    }else if(e=='parsererror'){
                        alert('Error: \nParsing JSON Request failed.' + x.responseText);
                    } else {
                        alert('Unknow Error.\n'+x.responseText);
                    }
                }
            });
        };

        function displayStatus(statusArray,tenantUrl) {

            var statusDiv = document.getElementById("statusDiv");
            //This is the special case when provisioning fails and entries are removed.
            if(statusArray.length < prevArrayMessageSize) {
            	checkStopTimer(statusArray);
            } else {
            $("#statusDiv").empty();
            jQuery.each(statusArray, function(index, value) {
                var newPara = document.createElement('p');
                newPara.innerHTML=value;
                statusDiv.appendChild(newPara);
            });
            if(statusArray.length == 7) {
                var newPara = document.createElement('p');
                newPara.setAttribute("style","color:#FFFFFF");
                    newPara.innerHTML="Your application is ready at URL below: </br> <a href='" + tenantUrl + "'>" + tenantUrl + "</a>";
                statusDiv.appendChild(newPara);
            }
            checkStopTimer(statusArray);
            }
        };
        
        function checkStopTimer(statusArray){
            var provisioned = false;
            if ((count >= maxCount) || (statusArray.length == 7)) {
                clearInterval(intervalId);
                provisioned = true;
            }
            if ( ((count >= maxCount) && (statusArray.length < 7)) || (statusArray.length < prevArrayMessageSize)) {
            	clearInterval(intervalId);
            	var registrationDiv = document.getElementById('RegistrationInProgress');
            	if(registrationDiv != null) {
            		document.getElementById("statusDiv").removeChild(registrationDiv);
            	}
                var newPara = document.createElement('p');
                newPara.setAttribute("style","color:#FF0000");
                newPara.innerHTML="problems occurred while registering, please try again after sometime.";
                document.getElementById("statusDiv").appendChild(newPara);
                
            } else {
	                if(!provisioned) {
	                var newPara = document.createElement('p');
	                newPara.setAttribute('id','RegistrationInProgress');
	                newPara.setAttribute("style","color:#FFFFFF");
	                var modolus = count%2;
	                if (modolus== 0) {
	                 newPara.innerHTML="Registration is in progress... Please wait! ";
	                } else {
	                	newPara.innerHTML="Registration is in progress...";
		                }
	                document.getElementById("statusDiv").appendChild(newPara);
	                }
                }
        }
    </script>
    <div id="main">
        <div id="mainContainer"> 
            <h2>Provisioning Status</h2>
            <div id="statusDiv"></div>
            <div id="footer"></div>
        </div>
    </div>
</body>
