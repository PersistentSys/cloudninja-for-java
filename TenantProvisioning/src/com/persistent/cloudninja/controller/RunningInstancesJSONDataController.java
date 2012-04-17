/*******************************************************************************
 * Copyright 2012 Persistent Systems Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
 package com.persistent.cloudninja.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import com.persistent.cloudninja.domainobject.InstanceHealthRoleInstanceEntity;
import com.persistent.cloudninja.service.RunningInstancesJSONDataService;
import com.persistent.cloudninja.transferObjects.RunningInstanceDTO;


/**
 * Controller to get Running Instances.
 */
@Controller
public class RunningInstancesJSONDataController {
    
    @Autowired
    RunningInstancesJSONDataService runningInstancesJSONDataService;
    
    /**
     * Running Instance Page
     * @return
     */
    @RequestMapping("/RunningInstance.htm")
    public String getRunningInstance()
    {
        return "runningInstancePage";
    }
    
    /**
     * Gets the Role Name, Instance Name and Instance Status from deployment.  
     * @param request The Http request
     * @param response The Http response
     * @return ModelAndView 
     * @throws IOException
     * @throws XPathExpressionException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    @RequestMapping("/getRoleInstanceListFromDeployment.htm")
    public ModelAndView getRoleInstanceListFromDeployment(HttpServletRequest request, HttpServletResponse response) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException
    {
        String callback = request.getParameter("callback");
        List <InstanceHealthRoleInstanceEntity> roleInstanceList =  runningInstancesJSONDataService.getRoleAndInstanceFromDeployment();
        
        ModelAndView modelAndView = new ModelAndView("roleInstanceDataFromDeployment","roleInstanceList",roleInstanceList);
        modelAndView.addObject("callback", callback);
        return modelAndView;
    }
    
    /**
     * Gets the instance count to be displayed on graph.
     * @param request the Http request
     * @param response the Http response
     * @return ModelAndView corresponding to roleKpiListJSON view
     */
    @RequestMapping("/getInstanceChartData.htm")
    public ModelAndView healthPage(HttpServletRequest request, HttpServletResponse response)
    {
        String yearStr = request.getParameter("year");
        String monthStr = request.getParameter("month");
        
        List<String> errors = new ArrayList<String>();
        if ((yearStr == null) || (yearStr.trim().length() == 0)) {
            errors.add("year is required");
        } else if ((monthStr == null) || (monthStr.trim().length() == 0)) {
            errors.add("month is required");
        }
        
        ModelAndView modelAndView = new ModelAndView();
        if (errors.size() > 0) {
            modelAndView.setViewName("jsonErrorPage");
            modelAndView.addObject("errorList", errors);
        } else {
            int year = Integer.parseInt(yearStr);
            int month = Integer.parseInt(monthStr);
            String callback = request.getParameter("callback");
            
            List<String> startAndEndtime = generateStartAndEndTime(year, month);
            
            RunningInstanceDTO runningInstanceDTO = new RunningInstanceDTO();
            runningInstanceDTO = runningInstancesJSONDataService.getRunningInstances(startAndEndtime.get(0),startAndEndtime.get(1));
            
            modelAndView.setViewName("runningInstanceChartData");
            modelAndView.addObject("runningInstanceDTO",runningInstanceDTO);
            modelAndView.addObject("callback", callback);
        }
        return modelAndView;
    }
    
    public List<String> generateStartAndEndTime(int year, int month)
    {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.sss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        Date currentDate = cal.getTime();
        int currentMonth = cal.get(Calendar.MONTH)+1;
        
        String start = month+"/01/"+year+" 00:00:00.000";
        String end = null;
        
        if(month == currentMonth) {
            end = dateFormat.format(currentDate);
        } else {
            try {
                cal.setTime(dateFormat.parse(start));
                int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                end = month+"/"+maxDay+"/"+year+" 23:59:59.000";
                             
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
        }
        List <String> time = new ArrayList<String>();
        time.add(start);
        time.add(end);
        return time;
    }  
}
