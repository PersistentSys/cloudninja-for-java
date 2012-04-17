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
package com.persistent.cloudninja.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.persistent.cloudninja.dao.RoleDao;
import com.persistent.cloudninja.dao.RoleInstancesDao;
import com.persistent.cloudninja.dao.TaskCompletionDao;
import com.persistent.cloudninja.domainobject.RoleEntity;
import com.persistent.cloudninja.domainobject.RoleInstancesEntity;
import com.persistent.cloudninja.domainobject.RoleInstancesId;

/**
 * Deployment monitor task retrieves the information regarding the roles
 * and their instances.
 *
 */
@Component
public class DeploymentMonitor implements IActivity {
	
	private static final Logger LOGGER = Logger.getLogger(DeploymentMonitor.class);
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private RoleInstancesDao roleInstancesDao;
	
	@Autowired
	private TaskCompletionDao taskCompletionDao;
	
	@Value("#{'${deployment.keystorepassword}'}")
    private String keyStorePassword;
	
	@Value("#{'${deployment.truststorepassword}'}")
    private String trustStorePassword;
	
	@Value("#{'${deployment.host}'}")
    private String host;
	
	@Value("#{'${deployment.subscriptionid}'}")
    private String subscriptionId;
	
	@Value("#{'${deployment.hostedservicename}'}")
    private String hostedServiceName;
	
	@Value("#{'${deployment.deploymenttype}'}")
    private String deploymentType;

	@Override
	public boolean execute() {
		boolean retVal = true;
		try {
			StopWatch watch = new StopWatch();
			watch.start();
			StringBuffer roleInfo = getRoleInfoForDeployment();
			parseRoleInfo(roleInfo);
			watch.stop();
			taskCompletionDao.updateTaskCompletionDetails(watch.getTotalTimeSeconds(),"ProcessMonitorInstanceCount","");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			retVal = false;
		}
		return retVal;
	}

	/**
	 * Parses the response received by making call to REST API.
	 * It parses and gets the total no. roles and their instances.
	 * 
	 * @param roleInfo
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws ParseException
	 */
	private void parseRoleInfo(StringBuffer roleInfo) 
			throws ParserConfigurationException, SAXException,
			IOException, XPathExpressionException, ParseException {
		DocumentBuilder docBuilder = null;
		Document doc = null;
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		docBuilder = docBuilderFactory.newDocumentBuilder();
		doc = docBuilder.parse(new InputSource(new StringReader(roleInfo.toString())));
		
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		NodeList roleList = (NodeList) xPath.evaluate(
				"/Deployment/RoleList/Role/RoleName",
				doc, XPathConstants.NODESET);
		//get all the roles
		String roleName = null;
		List<String> listRoleNames = new ArrayList<String>();
		for (int i = 0; i < roleList.getLength(); i++) {
			Element element = (Element) roleList.item(i);
			roleName = element.getTextContent();
			RoleEntity roleEntity = roleDao.findByRoleName(roleName);
			if (roleEntity == null) {
				roleEntity = new RoleEntity();
				roleEntity.setName(roleName);
				roleDao.add(roleEntity);
			}
			listRoleNames.add(roleName);
		}
		
		xPathFactory = XPathFactory.newInstance();
		xPath = xPathFactory.newXPath();
		RoleInstancesEntity roleInstancesEntity = null;
		RoleInstancesId roleInstancesId = null;
		for (String name : listRoleNames) {
			roleList = (NodeList) xPath.evaluate(
					"/Deployment/RoleInstanceList/RoleInstance[RoleName='" + name + "']",
					doc, XPathConstants.NODESET);
			//get no. of instances for WorkerRole
			int noOfInstances = roleList.getLength();
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			String date = dateFormat.format(calendar.getTime());
			roleInstancesId = new RoleInstancesId(dateFormat.parse(date), name);
			roleInstancesEntity = new RoleInstancesEntity(roleInstancesId, noOfInstances, "UPDATE");
			roleInstancesDao.add(roleInstancesEntity);
		}

	}

	/**
	 * Gets the information regarding the roles and their instances
	 * of the deployment. It makes a call to REST API and gets the XML response. 
	 * 
	 * @return XML response
	 * @throws IOException
	 */
	public StringBuffer getRoleInfoForDeployment() 
			throws IOException {
		StringBuffer response = new StringBuffer();
		System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
		
		StringBuffer keyStore = new StringBuffer();
		keyStore.append(System.getProperty("java.home"));
		LOGGER.debug("java.home : " + keyStore.toString());
		if (keyStore.length() == 0) {
			keyStore.append(System.getenv("JRE_HOME"));
			LOGGER.debug("JRE_HOME : " + keyStore.toString());
		}
		keyStore.append(File.separator + "lib\\security\\CloudNinja.pfx");
		System.setProperty("javax.net.ssl.keyStore", keyStore.toString());
		System.setProperty("javax.net.debug", "ssl");
		System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
		System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
		
		SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		// form the URL which will return the response
		// containing info of roles and their instances.
		StringBuffer strURL = new StringBuffer(host);
		strURL.append(subscriptionId);
		strURL.append("/services/hostedservices/");
		strURL.append(hostedServiceName);
		strURL.append("/deploymentslots/");
		strURL.append(deploymentType);
		
		URL url = new URL(strURL.toString());
		
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setSSLSocketFactory(sslSocketFactory);
		connection.setRequestMethod("GET");
		connection.setAllowUserInteraction(false);
		// set the x-ms-version in header which is a compulsory parameter to get response 
		connection.setRequestProperty( "x-ms-version", "2011-10-01" );
		connection.setRequestProperty( "Content-type", "text/xml" );
		connection.setRequestProperty( "accept", "text/xml" );
		// get the response as input stream
		InputStream inputStream = connection.getInputStream();
		InputStreamReader streamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(streamReader);
		String string = null;
		while ((string = bufferedReader.readLine()) != null) {
			response.append(string);
		}
		return response;
	}

}
