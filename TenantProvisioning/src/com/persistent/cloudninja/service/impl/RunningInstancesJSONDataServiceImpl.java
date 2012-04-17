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
package com.persistent.cloudninja.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.persistent.cloudninja.dao.RunningInstancesJSONDataDao;
import com.persistent.cloudninja.domainobject.InstanceHealthRoleInstanceEntity;
import com.persistent.cloudninja.domainobject.RunningInstanceEntity;
import com.persistent.cloudninja.scheduler.DeploymentMonitor;
import com.persistent.cloudninja.service.RunningInstancesJSONDataService;
import com.persistent.cloudninja.transferObjects.RunningInstanceDTO;

/**
 * Service Class for getting running instances 
 */
@Service("runningInstancesJSONDataService")
public class RunningInstancesJSONDataServiceImpl implements RunningInstancesJSONDataService{
    
    @Autowired
    private DeploymentMonitor deploymentMonitor;
    
    @Autowired
    private RunningInstancesJSONDataDao runningInstancesJSONDataDao;

    @Override
    public RunningInstanceDTO getRunningInstances(String start, String end) { 
        // role and List of yarray
        Map<String, List<Long>> roleInstanceCountMap = new HashMap<String, List<Long>>();
        RunningInstanceDTO runningInstanceDTO = runningInstancesJSONDataDao.getRunningInstancesFromDB(start, end);
        List<String> allDateList = getAllDates(start, end);
        List<String> roleList = getRoles(runningInstanceDTO);
        
        int roleListSize = roleList.size();
        int runningInstanceListSize = runningInstanceDTO.getRunningInstance().size();
        
        List<RunningInstanceEntity> runningInstanceList = runningInstanceDTO.getRunningInstance();
        
        for (int index = 0; index < roleListSize; index++) {
            String role = roleList.get(index);
            long[] instanceCountArray = new long[allDateList.size()];

            for (int innerIndex = 0; innerIndex < runningInstanceListSize; innerIndex++) {
                if (runningInstanceList.get(innerIndex).getRoleName().equals(role)) {
                    int day = runningInstanceList.get(innerIndex).getDay();
                    instanceCountArray[day - 1] = runningInstanceList.get(innerIndex).getInstanceCountSum();
                }
            }
            List<Long> instanceList = Arrays.asList(ArrayUtils.toObject(instanceCountArray));
            roleInstanceCountMap.put(role, instanceList);
        }
        List<List<Long>> instanceCountSeries = new ArrayList<List<Long>>();
        
        for (int index = 0; index < roleListSize; index++)
        {
            String role = roleList.get(index);
            List<Long> instanceCountList = new ArrayList<Long>();
            instanceCountList = (List<Long>) roleInstanceCountMap.get(role);
            instanceCountSeries.add(instanceCountList);
        }
        runningInstanceDTO.setRoles(roleList);
        runningInstanceDTO.setMonthDates(allDateList);
        runningInstanceDTO.setInstanceCountSeries(instanceCountSeries);
        return runningInstanceDTO;
    }
    
    @Override
    public List<InstanceHealthRoleInstanceEntity> getRoleAndInstanceFromDeployment() {
        StringBuffer stringBuffer;
        List<InstanceHealthRoleInstanceEntity> roleInstanceList = new ArrayList<InstanceHealthRoleInstanceEntity>();
        try {
            stringBuffer = deploymentMonitor.getRoleInfoForDeployment();
            roleInstanceList = parseResponse(stringBuffer);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
           e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return roleInstanceList;
    }

    /**
     * Get List of roles 
     * @param runningInstanceDTO 
     * @return Role List
     */
    private List<String> getRoles(RunningInstanceDTO runningInstanceDTO) {

        List<String> roleList = new ArrayList<String>();
        List<RunningInstanceEntity> runningInstanceList = runningInstanceDTO.getRunningInstance();
        int runinginstanceCount = runningInstanceList.size();

        String roleName;
        for (int i = 0; i < runinginstanceCount; i++) {
            roleName = runningInstanceList.get(i).getRoleName();
            if (!roleList.contains(roleName)) {
                roleList.add(roleName);
            }
        }
        return roleList;
    }
    
    /**
     * Get all dates between start and end date
     * @param start The start date
     * @param end The end date
     * @return List containing dates
     */
    private List<String> getAllDates(String start, String end) {

        List<String> dateList = new ArrayList<String>();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = new Date();
        Date endDate = new Date();

        try {
            startDate = formatter.parse(start);
            endDate = formatter.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        Date inBetweenDate = calendar.getTime();
        while (inBetweenDate.getTime() <= endDate.getTime()) {
            dateList.add(formatter.format(inBetweenDate));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            inBetweenDate = calendar.getTime();
        }
        return dateList;
    }

    /**
     * Parse the response from deployment monitoring and get role name, instance name and instance status.
     * @param response The XML response of deployment monitoring task.
     * @return List of InstanceHealthRoleInstanceEntity
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws XPathExpressionException
     */
    private List<InstanceHealthRoleInstanceEntity> parseResponse(StringBuffer response) 
        throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        DocumentBuilder documentBuilder = null;
        Document document = null;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.parse(new InputSource(new StringReader(response.toString())));

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        NodeList roleNameList = (NodeList) xPath.evaluate(
            "/Deployment/RoleInstanceList/RoleInstance/RoleName",
            document, XPathConstants.NODESET);
        NodeList instanceNameList = (NodeList) xPath.evaluate(
            "/Deployment/RoleInstanceList/RoleInstance/InstanceName",
            document, XPathConstants.NODESET);
        NodeList instanceStatusList = (NodeList) xPath.evaluate(
            "/Deployment/RoleInstanceList/RoleInstance/InstanceStatus",
            document, XPathConstants.NODESET);

        List<InstanceHealthRoleInstanceEntity> list = new ArrayList<InstanceHealthRoleInstanceEntity>();        
        for(int index=0;index<roleNameList.getLength();index++) {
            Element roleElement = (Element) roleNameList.item(index);
            Element instanceElement = (Element) instanceNameList.item(index);
            Element statusElement = (Element) instanceStatusList.item(index);

            InstanceHealthRoleInstanceEntity roleInstanceEntity = new InstanceHealthRoleInstanceEntity();
            roleInstanceEntity.setRoleName(roleElement.getTextContent());
            roleInstanceEntity.setInstanceName(instanceElement.getTextContent());
            roleInstanceEntity.setInstanceStatus(statusElement.getTextContent());
            list.add(roleInstanceEntity);
        }
        return list;
    }
}
