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
<html>
<head>
    <link rel="stylesheet" type="text/css" href="webresources/jqplot/jquery.jqplot.min.css" />
    
      <!--[if lt IE 9]><script language="javascript" type="text/javascript" src="webresources/jqplot/excanvas.js"></script><![endif]-->
    <script type="text/javascript" src="webresources/jqplot/jquery.min.js"></script>
    <script type="text/javascript" src="webresources/jqplot/jquery.jqplot.min.js"></script>
    
    <script type="text/javascript" src="webresources/jqplot/plugins/jqplot.logAxisRenderer.min.js"></script>
    <script type="text/javascript" src="webresources/jqplot/plugins/jqplot.canvasTextRenderer.min.js"></script>
    <script type="text/javascript" src="webresources/jqplot/plugins/jqplot.canvasAxisLabelRenderer.min.js"></script>
    <script type="text/javascript" src="webresources/jqplot/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
    <script type="text/javascript" src="webresources/jqplot/plugins/jqplot.dateAxisRenderer.min.js"></script>
    <script type="text/javascript" src="webresources/jqplot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
    <script type="text/javascript" src="webresources/jqplot/plugins/jqplot.barRenderer.min.js"></script>
    <script type="text/javascript" src="webresources/jqplot/plugins/jqplot.enhancedLegendRenderer.min.js"></script>
    <script type="text/javascript" src="webresources/jqplot/plugins/jqplot.highlighter.min.js"></script>
 	
 	<style type="text/css">
    div.jqplot-target {
        height: 325px;
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
   
   <h2>Metering Totals</h2>
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
   <p>The following charts reflect cumultive tenant metering data over the selected time period</p>
	   <div id="div1">
	       <h3>Web Bandwidth</h3>
	   </div>
	  <div id="chart1"></div>
	  
	   <div id="div2">		    
	       <h3>Database Size</h3>
	   </div>
	   <div id="chart2"></div>
	
	   <div id="div3">
	       <h3>Blob Storage Size</h3>
	   </div>
	   <div id="chart3"></div>
	
	   <div id="div4">
	       <h3>Database Bandwidth (Internal)</h3>
	   </div>
	   <div id="chart4"></div>
	  <div id="footer"></div>
   </div>
</div>

<script type="text/javascript" language="javascript">
    $(document).ready(function(){
        
        var url = "contextPath/getMeteringTotalsData.htm";
        var contextPath = "<%= request.getContextPath()%>";
        url = url.replace('contextPath', contextPath);
        
        var d=new Date();
        var month = (d.getUTCMonth() + 1).toString();
        var year= d.getUTCFullYear().toString();

        $("#month").val(month).attr('selected', true);

        $('#month').change(function () {
            updateCharts(url, year, $('#month').val());
        });  
      // Method to display the charts
      updateCharts(url, year, month);
    });
   // function to update the chart data
   function updateCharts(url, year, month) {
	   clearDiv("chart1");
       displaychart("chart1", "Bandwidth Use in (kb)", year, month, "WebAppBandwithUse_CS,WebAppBandwithUse_SC,WebAppRequests", url);
       clearDiv("chart2");
       displaychart("chart2", "Size in (kb)", year, month, "DatabaseSize", url);
       clearDiv("chart3");
       displaychart("chart3", "Size in (kb)", year, month, "BlobStoreUsage", url);
       clearDiv("chart4");
       displaychart("chart4", "Bandwidth Use in (kb)", year, month, "DatabaseBandwidth_Ingress,DatabaseBandwidth_Egress", url);
   };
   // Clears the given div element by id
   function clearDiv(divId) {
	   $("#"+divId).html('');
	   };
   
   function displaychart(divId, yaxisLabel, year, month, kpi, url){
       $.ajax({
           dataType: "jsonp",
           url: url,
           data: {'year':year, 'month':month, 'kpi':kpi},
           success: function(result){
               var xValueArr = new Array();
               var yValueArr = new Array();
               var kpiValueArr= new Array();
               jQuery.each(result.series, function(index, value) {
                   xValueArr[index]= value.xSeries;
                   yValueArr[index]=value.ySeries;
                   kpiValueArr[index]=value.kpi;
                });
               var lineArray = new Array();
               for (var i=0; i < xValueArr.length; i++) {
                    lineArray[i] = getChartData(xValueArr[i], yValueArr[i]);
                }
               var arrrr = xValueArr[0];
               var minDate = arrrr[0];
               var maxDate =  arrrr[arrrr.length - 1];
               // plot the chart in respective div
               plotChart(divId, lineArray, kpiValueArr, yaxisLabel, minDate, maxDate);
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
   
   //code to display the chart
   function plotChart(divId, lineArray, labelsArray, yaxisLabel,  minDate, maxDate) {
       var plot2 = $.jqplot(divId, lineArray, {
	seriesColors: [ "#EAA228", "#579575", "#839557", "#958c12",
        "#953579", "#4b5de4", "#d8b83f", "#ff5800", "#0085cc"],
		// show tool tips
        highlighter: {
            show: true,
            sizeAdjust: 1,
            tooltipOffset: 9
        },
		grid: {
			background: '#DEDEDE',
			drawBorder: false,
			shadow: false,
			gridLineColor: '#666666',
			gridLineWidth: 1
        },
        // Show the legend and put it outside the grid, but inside the
        // plot container, shrinking the grid to accomodate the legend.
        // A value of "outside" would not shrink the grid and allow
        // the legend to overflow the container.
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
          renderer: $.jqplot.DateAxisRenderer,
          labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
          tickRenderer: $.jqplot.CanvasAxisTickRenderer,
          tickOptions: {
              //labelPosition: 'middle',
			  formatString: "%m/%d/%Y",
              angle: -90,
              fontSize: '9pt'
          },
          min: minDate,
          max: maxDate,
		  tickInterval: '1 day'
        },
        yaxis: {
          label: yaxisLabel,
		  min: 0,
          labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
          tickOptions: {
            formatString: "%d",
            fontSize: '9pt'   
            
            }
        }
      }
    });
   };

    function getChartData(xarray, yarray) {
        var data = [];
        for (var i=0; i<xarray.length; i++) {
          data.push([xarray[i], yarray[i]]);
        }
        return data;
    };

</script>
</body>
</html>
