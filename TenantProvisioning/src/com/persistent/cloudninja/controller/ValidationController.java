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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.persistent.cloudninja.dao.TenantDao;
import com.persistent.cloudninja.domainobject.TenantEntity;
/**
 * 
 * Controller class to validate various fields
 *
 */
@Controller
public class ValidationController {

    @Autowired
    TenantDao tenantDao;
    /**
     *  The method to validate the tenant id while registering a new tenant
     * @param request The Http request
     * @param response the Http response
     * @throws IOException
     */
    @RequestMapping("isTenantIdAvailable.htm")
    public void isTenantIdAvailable(HttpServletRequest request,
                                    HttpServletResponse response)
                                                                 throws IOException {
        TenantEntity tenantEntity;
        String tenantId = request.getParameter("tenantId");

        if (tenantId != null && tenantId.trim().length() > 0) {
            request.setCharacterEncoding("UTF-8");
            tenantEntity = tenantDao.find(tenantId);
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            if (tenantEntity != null) {
                response.getWriter().write("<valid>true</valid>");
            } else {
                response.getWriter().write("<valid>false</valid>");
            }
        }
    }
}
