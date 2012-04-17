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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.persistent.cloudninja.model.MeteringTotalsChartModel;
import com.persistent.cloudninja.service.MeteringTotalsDataService;

/**
 * Controller to get data for metering totals
 * 
 * @author 
 *
 */
@Controller
public class MeteringTotalsDataController {

    @Autowired
    MeteringTotalsDataService meteringTotalsDataService;
     
    // getMeteringTotalsData.htm?year=2011&month=12&kpi=WebAppBandwithUse_CS,WebAppBandwithUse_SC
    /**
     *  Gets the metering totals data needs to be displayed on charts
     * @param request the Http request
     * @param response the Http response
     * @return
     */
    @RequestMapping(value = "/getMeteringTotalsData.htm", method = RequestMethod.GET)
    public ModelAndView getMeteringTotalsData(HttpServletRequest request, HttpServletResponse response) {
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String kpi = request.getParameter("kpi");
        String callback=request.getParameter("callback");
        
        List<String> errors = new ArrayList<String>();
        // Check if required parameters are present
        if (year == null || year.trim().length() == 0) {
            errors.add("year is required");
        }
        if (month == null || month.trim().length() == 0) {
            errors.add("month is required");
        }
        if (kpi == null || kpi.trim().length() == 0) {
            errors.add("kpi is required");
        }

        ModelAndView mv = new ModelAndView();
        if (errors.size() == 0) {
            List<MeteringTotalsChartModel> MeteringTotalsChartModelList = new ArrayList<MeteringTotalsChartModel>();
            MeteringTotalsChartModelList = meteringTotalsDataService.getMeteringTotalsChartModelList(year, month, kpi);
            mv.setViewName("meteringTotalsChartDataPage");
            mv.addObject("meteringTotalsChartModelList", MeteringTotalsChartModelList);
            mv.addObject("callback", callback);
        } else {
            mv.setViewName("jsonErrorPage");
            mv.addObject("errorList", errors);
        }
        return mv;
    }
   
   /**
    * Handler to display metering totals page
    * @return View for displaying Metering totals page
    */
    @RequestMapping(value="/MeteringTotals.htm")
   public ModelAndView showMonitoringPage() {
        
       return new ModelAndView("meteringTotalsPage");
       
   }
}
