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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.persistent.cloudninja.domainobject.InstanceHealthActiveUserEntity;
import com.persistent.cloudninja.domainobject.InstanceHealthKPIEntity;
import com.persistent.cloudninja.domainobject.InstanceHealthKpiValueEntity;
import com.persistent.cloudninja.domainobject.InstanceHealthRoleInstanceEntity;
import com.persistent.cloudninja.service.InstanceHealthDataService;
import com.persistent.cloudninja.transferObjects.InstanceHealthActiveUserDTO;
import com.persistent.cloudninja.transferObjects.InstanceHealthKpiSeriesDTO;
import com.persistent.cloudninja.transferObjects.InstanceHealthKpiValueDTO;
import com.persistent.cloudninja.transferObjects.InstanceHealthRolesKpiListDTO;
import com.persistent.cloudninja.transferObjects.TopQueriesDTO;

/**
 * Controller for Instance Health Tab.
 */
@Controller
public class InstanceHealthDataController {
	
	@Autowired
	private InstanceHealthDataService instanceHealthJSONDataService;
	
    /**
     * Gets Roles information and KPI list.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @return ModelAndView containing roleKpiListJSON
     * @throws ParseException
     */
    @RequestMapping(value = "/getRoleKpiList.htm", method = RequestMethod.GET)
    public ModelAndView getRoleKpiListLastHour(HttpServletRequest request,
                                               HttpServletResponse response) throws ParseException {
        String hourStr = request.getParameter("hours");
        
        List<String> errors = new ArrayList<String>();
        if ((hourStr == null) || (hourStr.trim().length() == 0)) {
            errors.add("hours is required");
        }
        
        ModelAndView modelAndView = new ModelAndView();
        if (errors.size() > 0) {
            modelAndView.setViewName("jsonErrorPage");
            modelAndView.addObject("errorList", errors);
        } else {
            int hours = Integer.parseInt(hourStr);
            String callback = request.getParameter("callback");

            List<String> startEndTimeList = getStartEndTime(hours);
            InstanceHealthRolesKpiListDTO kpiDTO =
                    instanceHealthJSONDataService.getKpiRoleList(startEndTimeList.get(0), startEndTimeList.get(1));

            List<InstanceHealthRoleInstanceEntity> roles = kpiDTO.getKpiRoles();
            List<InstanceHealthKPIEntity> kpiTypes = kpiDTO.getKpiType();
            modelAndView.setViewName("roleInstanceKpiChartData");
            modelAndView.addObject("kpiTypes", kpiTypes);
            modelAndView.addObject("kpiRoles", roles);
            modelAndView.addObject("callback", callback);
        }
        return modelAndView;
    }
	
	/**
	 * Gets the Active users.
	 * @param request  the HTTP request
	 * @param response the HTTP response
	 * @return ModelAndView containing uniqueUserChartJSON
	 * @throws ParseException
	 */
	@RequestMapping (value = "/getUniqueUsers.htm", method = RequestMethod.GET)
    public ModelAndView getUniqueUserChart(HttpServletRequest request,
                                           HttpServletResponse response) throws ParseException {
        String hourStr = request.getParameter("hours");

        List<String> errors = new ArrayList<String>();
        if ((hourStr == null) || (hourStr.trim().length() == 0)) {
            errors.add("hours is required");
        }

        ModelAndView modelAndView = new ModelAndView();
        if (errors.size() > 0) {
            modelAndView.setViewName("jsonErrorPage");
            modelAndView.addObject("errorList", errors);
        } else {
            int hours = Integer.parseInt(hourStr);
            String callback = request.getParameter("callback");

            DateFormat dateFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
            List<String> startEndTimeList = getStartEndTime(hours);
            InstanceHealthActiveUserDTO uniqueUserDTO = instanceHealthJSONDataService.getActiveUserCount(startEndTimeList.get(0), startEndTimeList.get(1));

            List<Integer> count = new ArrayList<Integer>();
            List<String> timestamp = new ArrayList<String>();
            int listSize = uniqueUserDTO.getUniqueUser().size();
            List<InstanceHealthActiveUserEntity> uniqueUsersList = uniqueUserDTO.getUniqueUser();
            for (int index = 0; index < listSize; index++) {
                Date nowDate =
                        dateFormat.parse(dateFormat.format(uniqueUsersList.get(index).getTimeStamp()));
                count.add(uniqueUsersList.get(index).getCount());
                timestamp.add(dateFormat.format(nowDate));
            }
            modelAndView.setViewName("activeUserChartData");
            modelAndView.addObject("count", count);
            modelAndView.addObject("timestamp", timestamp);
            modelAndView.addObject("callback", callback);
            modelAndView.addObject("startTime", startEndTimeList.get(0));
            modelAndView.addObject("endTime", startEndTimeList.get(1));
        }
        return modelAndView;
    }
	
	/**
	 * Gets the Top running queries from database.
	 * @return ModelAndView 
	 */
    @RequestMapping("/HealthPage.htm")
    public ModelAndView healthPage() {
        TopQueriesDTO topQueriesDTO = instanceHealthJSONDataService.getTopQueries();
        return new ModelAndView("instanceHealthPage", "topQueriesDTO",topQueriesDTO);
    }

    /**
     * Gets all KPI values for specific role and instance.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @return ModelAndView
     */
    @RequestMapping("/getMeteringChart.htm")
    public ModelAndView meteringChartExLastHoursPage(HttpServletRequest request,
                                                     HttpServletResponse response) {
        String hourStr = request.getParameter("hours");
        String roleStr = request.getParameter("role");
        String instanceStr = request.getParameter("instance");
        String kpiList = request.getParameter("kpiList");

        List<String> errors = new ArrayList<String>();
        if ((hourStr == null) || (hourStr.trim().length() == 0)) {
            errors.add("hours is required");
        } else if ((roleStr == null) || (roleStr.trim().length() == 0)) {
            errors.add("role is required");
        } else if ((instanceStr == null) || (instanceStr.trim().length() == 0)) {
            errors.add("instance is required");
        } else if ((kpiList == null) || (kpiList.trim().length() == 0)) {
            errors.add("kpilist is required");
        }

        ModelAndView modelAndView = new ModelAndView();
        if (errors.size() > 0) {
            modelAndView.setViewName("jsonErrorPage");
            modelAndView.addObject("errorList", errors);
        } else {
            int hours = Integer.parseInt(hourStr);
            int role = Integer.parseInt(roleStr);
            int instance = Integer.parseInt(instanceStr);
            String callback = request.getParameter("callback");

            String[] kpiIds = kpiList.split(",");
            int kpiListSize = kpiIds.length;

            InstanceHealthKpiValueDTO instanceKpiDTO =
                    new InstanceHealthKpiValueDTO();
            List<String> startEndTimeList = getStartEndTime(hours);
            instanceKpiDTO = instanceHealthJSONDataService.getInstanceKPIs( startEndTimeList.get(0), startEndTimeList.get(1),role, instance);

            int size = instanceKpiDTO.getInstanceKPI().size();
            List<InstanceHealthKpiSeriesDTO> instanceKPIValueDTO = new ArrayList<InstanceHealthKpiSeriesDTO>();
            List<InstanceHealthKpiValueEntity> instanceHealthKpiValueList = instanceKpiDTO.getInstanceKPI();
            for (int outIndex = 0; outIndex < kpiListSize; outIndex++) {
                List<String> timestamp = new ArrayList<String>();
                List<Double> value = new ArrayList<Double>();
                InstanceHealthKpiSeriesDTO instancekpiValue =
                        new InstanceHealthKpiSeriesDTO();
                for (int index = 0; index < size; index++) {
                    if (instanceHealthKpiValueList.get(index).getKpiTypeId() == Integer.parseInt(kpiIds[outIndex])) {
                        timestamp.add(instanceHealthKpiValueList.get(index).getTimestamp());
                        value.add(instanceHealthKpiValueList.get(index).getValue());
                    }
                }
                instancekpiValue.setTimestamp(timestamp);
                instancekpiValue.setValue(value);
                instancekpiValue.setKpiTypeId(Integer.parseInt(kpiIds[outIndex]));
                instanceKPIValueDTO.add(instancekpiValue);
            }
            modelAndView.setViewName("workerRoleChartData");
            modelAndView.addObject("instanceKPIValueDTO", instanceKPIValueDTO);
            modelAndView.addObject("startTime", startEndTimeList.get(0));
            modelAndView.addObject("endTime", startEndTimeList.get(1));
            modelAndView.addObject("callback", callback);
        }
        return modelAndView;
    }
	
	/**
	 * Get the start and end time.
	 * @param hour the number of hour difference between start and end time.
	 * @return List containing start and end date.
	 */
    private List<String> getStartEndTime(int hour) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar cal = Calendar.getInstance();
        String end = dateFormat.format(cal.getTime());
        cal.add(Calendar.HOUR, -hour);
        String start = dateFormat.format(cal.getTime());

        List<String> time = new ArrayList<String>();
        time.add(start);
        time.add(end);
        return time;
    }
}
