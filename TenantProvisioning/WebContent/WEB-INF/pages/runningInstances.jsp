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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!--[if lt IE 9]><script language="javascript" type="text/javascript" src="webresources/jqplot/excanvas.js"></script><![endif]-->
<script type="text/javascript" src="webresources/jqplot/jquery.min.js"></script>
<script type="text/javascript" src="webresources/jqplot/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="webresources/jqplot/plugins/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="webresources/jqplot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="webresources/jqplot/plugins/jqplot.canvasTextRenderer.min.js"></script>
<script type="text/javascript" src="webresources/jqplot/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
<script type="text/javascript" src="webresources/jqplot/plugins/jqplot.enhancedLegendRenderer.min.js"></script>
<link rel="stylesheet" type="text/css" href="webresources/jqplot/jquery.jqplot.min.css" />
	<style type="text/css">
        div.jqplot-target {
            height: 160px;
            width: 1000px;
            margin: 10px;
        }
        td.jqplot-table-legend {
            border: 0px;
        }
        .jqplot-target  {
            font-size: 1.35em;
        }
        </style>
        
</head>
<body>
<div id="main">
    <div id="mainContainer">
        <%@ include file = "submenu.jsp" %>
        <br>
        <h2>Application's running instances</h2>
        <br>
        <select id="month">
            <option value="1">January</option>
            <option value="2">February</option>
            <option value="3">March</option>
            <option value="4">April</option>
            <option value="5">May</option>
            <option value="6">June</option>
            <option value="7">July</option>
            <option value="8">August</option>
            <option value="9">September</option>
            <option value="10">October</option>
            <option value="11">November</option>
            <option value="12">December</option>
        </select>
        <h3>Total compute hours per day </h3>
        <p>This is a stacked area chart that shows the total number of instances aggregated daily, over a month.</p>
        <br>
        <div id="running-instance-chart" align="center" style="width:900px;height:360px;"></div>
        <br>
       	<div id="grid">
         <table id=dataTable class="data-table" >
            <thead>
                <tr>
                    <th>Role</th>
                    <th>Instance</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
        </div>
        <script type="text/javascript">
            $(document).ready(function(){
            	var d=new Date();
                var month = (d.getUTCMonth() + 1).toString();
                var year= d.getUTCFullYear().toString();
                
                $("#month").val(month).attr('selected', true);

                $('#month').change(function () {
                    generateInstanceChart($("#month").val(),year);
                });
                generateInstanceChart($("#month").val(),year);
                generateRoleInstanceTable(); 
            });

            //Table of running instances from the deployment information
            function generateRoleInstanceTable()
            {
            	var url = "contextPath/getRoleInstanceListFromDeployment.htm";
                var contextPath = "<%= request.getContextPath()%>";
                url = url.replace('contextPath', contextPath);
                $.ajax(
                        { 
                            // Basic JSON properties.
                            url: url,
                            dataType: "jsonp",
                            // The success call back.
                            success: function( result ){
                               var tableRows = '';
                               if(result.roleInstanceList.length>0)
                               {
	                               for(var i=0;i<result.roleInstanceList.length;i++){
	                                 tableRows += "<tr>";
	                                 tableRows += '<td>'+result.roleInstanceList[i].RoleName+'</td>';
	                                 tableRows += '<td>'+result.roleInstanceList[i].InstanceName+'</td>';
	                                 tableRows += '<td>'+result.roleInstanceList[i].InstanceStatus+'</td>';
	                                 tableRows += "</tr>";
	                               }
	                               $("table#dataTable tbody").html(tableRows);
                               } else { alert("Running Instance Information is currently not available"); }                      
                            },
                            error: function(x,e){
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

            // Plots running instances chart
            function generateInstanceChart(month,year)
            {
                var url = "contextPath/getInstanceChartData.htm";
                var contextPath = "<%= request.getContextPath()%>";
                url = url.replace('contextPath', contextPath);
                $.ajax(
                { 
                    // Basic JSON properties.
                    url: url,
                    dataType: "jsonp",
                    data: {'year':year,'month':month},
                    // The success call back.
                    success: function( result ){
                        ///alert("success");
                        var xvalues = result.dates;
                        var roles = [];
                        roles = result.roles;
                        var yvalues = [];
                        var labelsArray = roles;
                        clearDiv('running-instance-chart');
                        if(result.instanceCountSeries.length>0)
                        {
                            for (var i=0; i < roles.length;i++)
                            {
                                yvalues.push(result.instanceCountSeries[i].count);
                            }
                            var plot1b = $.jqplot('running-instance-chart',yvalues,
                            {
                                stackSeries: true,
                                showMarker: false,
                                seriesDefaults: {
                                fill: true
                                },
                                grid: {
                        			background: '#DEDEDE',
                        			drawBorder: false,
                        			shadow: false,
                        			gridLineColor: '#666666',
                        			gridLineWidth: 1
                                },
                                legend: {
                                    show: true,
                                    // Remove this renderer to remove the unselect behavoir
                                    renderer: $.jqplot.EnhancedLegendRenderer,
                                    labels: labelsArray,
                                    placement: 'outside',
                                    //// compass direction, nw, n, ne, e, se, s, sw, w
                                    location: 'ne',
                                    border: '0',
                                    fontSize: '11px',
                                    background: 'none'
                                },
                                axes: {
                                    xaxis: {
                                        renderer: $.jqplot.CategoryAxisRenderer,
                                        ticks: xvalues,
                                        tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
                                        tickOptions: {
                                        angle: -90}
                                    },
                                    yaxis:{
                                        min:0,
                                        tickOptions: {
                                        formatString: "%d"}
                                    }
                                }
                            });

                            $('#running-instance-chart').bind('jqplotDataHighlight',
                                function (ev, seriesIndex, pointIndex, data) {
                                    $('#info1b').html('series: '+seriesIndex+', point: '+pointIndex+', data: '+data);
                                }
                            );

                            $('#running-instance-chart').bind('jqplotDataUnhighlight',
                                function (ev) {
                                    $('#info1b').html('Nothing');
                                }
                            );
                        }
                    },
                    error: function(x,e){
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
            function clearDiv(divId) {
                $("#"+divId).html('');
            };
        </script>
        <div id="footer"></div>
     </div>
</div>
</body>
</html>
