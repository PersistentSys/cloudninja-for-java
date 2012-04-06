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
<link rel="stylesheet" type="text/css"	href="webresources/jqplot/jquery.jqplot.min.css" />
<!--[if lt IE 9]><script language="javascript" type="text/javascript" src="webresources/jqplot/excanvas.js"></script><![endif]-->
<script type="text/javascript"  src="webresources/jqplot/jquery.min.js"></script>
<script type="text/javascript"  src="webresources/jqplot/jquery.jqplot.min.js"></script>
<script type="text/javascript"	src="webresources/jqplot/plugins/jqplot.dateAxisRenderer.min.js"></script>
<script type="text/javascript"	src="webresources/jqplot/plugins/jqplot.dateAxisRenderer.js"></script>
<script type="text/javascript"	src="webresources/jqplot/plugins/jqplot.enhancedLegendRenderer.min.js"></script>
<script type="text/javascript"	src="webresources/jqplot/plugins/jqplot.highlighter.min.js"></script>
<script type="text/javascript"	src="webresources/jqplot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript"	src="webresources/jqplot/plugins/jqplot.barRenderer.min.js"></script>
<script type="text/javascript"	src="webresources/jqplot/plugins/jqplot.logAxisRenderer.min.js"></script>
<script type="text/javascript"	src="webresources/jqplot/plugins/jqplot.canvasTextRenderer.min.js"></script>
<script type="text/javascript"	src="webresources/jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js"></script>
<script type="text/javascript"	src="webresources/jqplot/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
 <style type="text/css">
    div.jqplot-target{
        height: 160px !important;
        width: 1000px !important;
        margin: 10px !important;
    }
    td.jqplot-table-legend{
    border: 0px !important;
    }
    .jqplot-target{
      font-size: 1.35em !important;
    }
    .headDiv{
        height: 30px !important;
        width: 130px !important;
    }
</style>
</head>
<body>
<div id="main">
    <div id="mainContainer">
        <%@ include file = "submenu.jsp" %>
        <br>
        <h2>Instance Health</h2>
        <p>
        The current page is a visual representation of performance counters
        collected from Windows Azure role instances using the Windows Azure
        Diagnostics features.
          The Windows Azure Diagnostics agent collects and writes performance
        counter data to Windows Azure Storage.
          A Cloud Ninja worker task then processes this data at a configurable
        interval; purging old data as well as transforming and loading
        diagnostics data in to SQL Azure for more advanced analysis and
        reporting purposes.
          When you select a range to view performance data, the page first makes
         a request via a JSON service call to the Health controller to determine
         which roles/instance have data to show for that selected time period.
          Once the response comes back the client script uses the data to
        dynamically create the page.  Image URLs are formated using the data and
         the controller generates the chart based on the parameters in the image
         url.
        </p>
        <p>
        The selected performance counters monitored in this sample application
        are based on recommended counters relevant to application server
         health.
        This could be implemented to show any number of performance counters as
        well as filtering, grouping, averages, sums, etc... based a number of
        different needs.
        The charts are currently designed to refresh on a 1 minute interval or
        when a Kpi selection is modified.
        The role information and charts are updated when the time period
        selection is modified, given the elastic dynamic nature of the cloud the
         role and instances will vary over time.
        </p>
        <select id="kpirange">
            <option value="1">Last Hour</option>
            <option value="2">Last 2 Hours</option>
            <option value="4">Last 4 Hours</option>
            <option value="8">Last 8 Hours</option>
            <option value="12">Last 12 Hours</option>
            <option value="24">Last 24 Hours</option>
            <option value="48">Last 2 Days</option>
            <option value="72">Last 3 Days</option>
            <option value="96">Last 4 Days</option>
            <option value="120">Last 5 Days</option>
            <option value="144">Last 6 Days</option>
            <option value="168">Last 7 Days</option>
        </select>
        <div id="meteringChartDiv"></div>
        <h3>Unique Active Users</h3>
        <div id="active-users-chart"></div>
        <br>
        <h3>Top Running Queries on Control Database</h3>
        <table>
            <tbody>
                <tr>
                    <th>Average CPU Time (miliseconds)</th>
                    <th>Query</th>
                </tr>
                <c:forEach items="${topQueriesDTO.topQueries}" var="topQueries"
                    varStatus="varStatus">
                    <tr>
                        <td>${topQueries.queryTime}</td>
                        <td>${topQueries.query}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <script type="text/javascript" language="javascript">
            var role = new Array();
            var roleName = new Array();
            var instance = new Array();
            var kpiId = new Array();
            var kpiName = new Array();
            $(document).ready(function () {
                var isLoaded=0;
                $('#kpirange').val(1).attr('selected',true);
                $('#kpirange').change(function () {
                    $('#meteringChartDiv').empty();
                    var hours= $('#kpirange').val();
                    getRoleKpiList(hours);
                    activeUserChart();
                });
                var hours= $('#kpirange').val();
                getRoleKpiList(hours);
                activeUserChart();
                var startTime;
                var endTime;
                setInterval(activeUserChart,60 * 1000);
                setInterval("getKpiValueForInstance($('#kpirange').val())",60 * 1000);
            });

            //Gets Role, Instance and KPI list and populates to global arrays
            function getRoleKpiList(hours){
                clearDiv('active-users-chart');
                var url = "contextPath/getRoleKpiList.htm";
                var contextPath = "<%= request.getContextPath()%>";
                url = url.replace('contextPath', contextPath);
                $.ajax({
                    url: url,
                    dataType: "jsonp",
                    data: {'hours':hours},
                    success: function( result ){
                        role = [];
                        roleName = [];
                        instance = [];
                        kpiId = [];
                        kpiName = [];
                        if(result.list.length>0) {
                            for(var i=0;i<result.list.length;i++) {
                                role[i]=result.list[i].Id;
                                instance[i] = result.list[i].Instance;
                                roleName[i] = result.list[i].Name;
                            }
                            for(var j=0;j<result.list[0].KpiType.length;j++) {
                                kpiId[j] = result.list[0].KpiType[j].Id;
                                kpiName[j] = result.list[0].KpiType[j].Name;
                            }
                        }
                        getKpiValueForInstance(hours);
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

            //Get values for all KPIs for specific role and instance.
            function getKpiValueForInstance(hours) {
            	for(var roleIndex=0;roleIndex<role.length;roleIndex++) {
                    var roleId = role[roleIndex];
                    var instanceValue = instance[roleIndex];
                    kpiValueForInstanceAjaxCall(hours,roleId,instanceValue,roleIndex);
                }
            };

            // Generates a comma saperated String from kpiId
            function generateKpiList() {
                var kpilist="";
                for(var i=0;i<kpiId.length;i++) {
                    kpilist = kpilist + kpiId[i] + ",";
                }
                return kpilist;
            };
            
          	//Ajax call to Get values for all KPIs for specific role and instance.
            function kpiValueForInstanceAjaxCall(hours,roleId,instanceValue,roleIndex)
            {
                var kpiIdList = generateKpiList();
                var url = "contextPath/getMeteringChart.htm";
                var contextPath = "<%= request.getContextPath()%>";
                url = url.replace('contextPath', contextPath);
                $.ajax({
                    url: url,
                    dataType: "jsonp",
                    cache: false,
                    data: {'hours':hours,'role':roleId,'instance':instanceValue,'kpiList':kpiIdList},     
                    success: function(result){
                        startTime=result.startTime;
                        endTime=result.endTime;
                        var  series = new Array();
                        var kpiLength=result.kpi.length;
                        var firstTime=1;
                        for(var i=0;i<kpiLength;i++) {
                            var yarray=result.kpi[i].value;
                            var xarray=result.kpi[i].timestamp;
                            if(yarray.length==0 && firstTime==1)
                            {
                            	yarray.push(0);
                            	xarray.push(endTime);
                            }
                            series.push(getChartData(xarray, yarray));
                            
                        }
                        plotKpiValueChart(hours,series,roleIndex);
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
            
            //Fuction to plot the worker Role kpi value charts
            function plotKpiValueChart(hours,series,roleIndex) {
                if(series.length>0) {
                    var chartIndex=roleIndex;
                    var divId='title'+chartIndex;
                    if(document.getElementById(divId)==null) {
                        divId=create_div_dynamic('title'+chartIndex);
                        document.getElementById(divId).setAttribute('class','headDiv');
                    }
                    document.getElementById(divId).innerHTML='<h3>'+ roleName[chartIndex]+'('+instance[chartIndex]+')</h3>';
                    divId='chart'+chartIndex;
                    clearDiv(divId);
                    if (document.getElementById(divId)==null) {
                        divId=create_div_dynamic('chart'+chartIndex);
                    }
                    var tick;
                    var format;
                    var value =hours*5;
                    if(value<=120){
                        tick=value+' minutes';
                        format='%I:%M';
                    } else {
                        tick='1 day';
                        format='%#m/%#d/%Y';
                    }
                    date = new Date();
                    var maxdate = date.getTime();
                    var labels= new Array();
                    for(var i=0;i<kpiName.length;i++) {
                        labels[i]=kpiName[i];
                    }
                    //alert("generating graph for"+roleName[chartIndex]+'('+instance[chartIndex]);
                    var plot2 = $.jqplot(divId, series, {
                    	seriesColors: ["Green","Orange","Red","Blue","Yellow","Megenta","Brown"],
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
                            labels: labels,
                            placement: 'outside',
                            //// compass direction, nw, n, ne, e, se, s, sw, w
                            location: 'e',
                            border: '0',
                            fontSize: '11px',
                            background: 'none'
                        },
                        axes: {
                            xaxis: {
                                renderer: $.jqplot.DateAxisRenderer,
                                tickOptions:{formatString:format},
                                min:startTime,
                                max:endTime,
                                tickInterval:tick//,
                                //labelRenderer: $.jqplot.CanvasAxisLabelRenderer
                            },
                            yaxis: {
                                min: 0//,
                                //labelRenderer: $.jqplot.CanvasAxisLabelRenderer
                            }
                        }, seriesDefaults: {
                        	showMarker:false,
                            show: true,     
                            lineWidth: 2
                        }
                    });
                    //alert("plotted");
                }
            };
            
            // Function to generate Active user chart.
            function activeUserChart() {
                var tick;
                var format;
                var value = $('#kpirange').val()*5;
                if(value<=120) {
                    tick=value+' minutes';
                    format='%I:%M';
                } else {
                    tick='1 day';
                    format='%#m/%#d/%Y';
                }
                var url = "contextPath/getUniqueUsers.htm";
                var contextPath = "<%= request.getContextPath()%>";
                url = url.replace('contextPath', contextPath);
                $.ajax({
                    url: url,
                    dataType: "jsonp",
                    cache: false,
                    data: {'hours':$('#kpirange').val()},
                    success: function( result ){
                        if(result.count.length>0) {
                            date = new Date();
                            var yarray=result.count;
                            var xarray=result.timeStamp;
                            var data = getChartData(xarray, yarray);
                            var mindate=result.startTime;
                            var maxdate = result.endTime;
                            clearDiv('active-users-chart');
                            var plot1 = $.jqplot('active-users-chart', [data], {
                            	seriesColors: ["Red"],
                            	axesDefaults: {
                             	    pad: 2.5		// a factor multiplied by the data range on the axis to give the
                                    				// axis range so that data points don't fall on the edges of the axis.
                    			},
                                axes:{
                                    xaxis:{
                                        renderer:$.jqplot.DateAxisRenderer,
                                        tickOptions:{formatString:format},
                                        min:mindate,
                                        max:maxdate,
                                        tickInterval:tick
                                    }
                                },
                                grid: {
                                    background: '#DEDEDE',
                                    drawBorder: false,
                                    shadow: false,
                                    gridLineColor: '#666666',
                                    gridLineWidth: 1
                                },
                                seriesDefaults: {
                                	showMarker:false,
                                    show: true,     
                                    lineWidth: 2
                                }
                            });
                        }
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
            
            //Generates div dynamically for chart header
            function create_div_dynamic(divId){
                dv = document.createElement('div');
                dv.setAttribute('id',divId);
                dv.style.pixelWidth=500;
                dv.style.pixelHeight=160;
                document.getElementById("meteringChartDiv").appendChild(dv);
                return divId;
            };
            
            // Clears the given div element by id
            function clearDiv(divId) {
                $("#"+divId).html('');
            };

            //converts data format as required by chart api
            function getChartData(xarray, yarray) {
                var data = [];
                for (var i=0; i<xarray.length; i++) {
                    data.push([xarray[i], yarray[i]]);
                }
                return data;
            };
        </script>
        <div id="footer"></div>
    </div>
</div>
</body>
</html>
