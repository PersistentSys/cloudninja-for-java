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
package com.persistent.cloudninja.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class SAMLParser extends DefaultHandler {
    boolean subjectElement = false;
    boolean nameIDElement = false;
    boolean wsuCreatedElement = false;
    boolean wsuExpiresElement = false;
    boolean attributeStatementElement = false;
    boolean attributeElement = false;
    private boolean attributeValue = false;
    private String attributeName = "";
    private static Map<String, String> userPropertiesMap = null;

    @Override
    public void startDocument() throws SAXException {

    }

    public void startElement(String namespaceURI, String localName,
	    String qName, Attributes attributes) throws SAXException {

	if ("wsu:Created".equalsIgnoreCase(qName)) {
	    wsuCreatedElement = true;
	}
	if ("wsu:Expires".equalsIgnoreCase(qName)) {
	    wsuExpiresElement = true;
	}
	if ("Subject".equalsIgnoreCase(qName)) {
		subjectElement = true;
	}
	if (subjectElement && "NameID".equalsIgnoreCase(qName)) {
		nameIDElement = true;
	}
	if ("AttributeStatement".equalsIgnoreCase(qName)) {
	    attributeStatementElement = true;
	}
	if (attributeStatementElement && "Attribute".equalsIgnoreCase(qName)) {
	    attributeElement = true;
	    attributeName = attributes.getValue("Name").substring(
		    attributes.getValue("Name").lastIndexOf("/") + 1);
	}
	if (attributeElement && "AttributeValue".equalsIgnoreCase(qName)) {
	    attributeValue = true;
	}
    }

    public void characters(char ch[], int start, int length)
	    throws SAXException {
	if (wsuCreatedElement) {
	    String startTime = new String(ch, start, length);
	    populateUserPropertiesMap("logonTime", startTime);
	    wsuCreatedElement = false;
	}
	if (wsuExpiresElement) {
	    String sessionExpiryTime = new String(ch, start, length);
	    populateUserPropertiesMap("sessionExpiresAt", sessionExpiryTime);
	    wsuExpiresElement = false;
	}
	if (nameIDElement) {
		String nameID = new String(ch, start, length);
	    populateUserPropertiesMap("nameID", nameID);
	    nameIDElement = false;
	}
	if (attributeValue) {
	    String attributeInfo = new String(ch, start, length);
	    populateUserPropertiesMap(attributeName, attributeInfo);
	    attributeValue = false;
	}
    }

    public void endDocument() throws SAXException {
	this.printMap();

    }

    private void printMap() {


    }

    public Map<String, String> populateUserPropertiesMap(String key,
	    String value) {
	if (userPropertiesMap == null) {
	    userPropertiesMap = new HashMap<String, String>();
	}
	userPropertiesMap.put(key, value);
	return userPropertiesMap;
    }

    private static class MyErrorHandler implements ErrorHandler {
	private PrintStream out;

	MyErrorHandler(PrintStream out) {
	    this.out = out;
	}

	private String getParseExceptionInfo(SAXParseException spe) {
	    String systemId = spe.getSystemId();
	    if (systemId == null) {
		systemId = "null";
	    }

	    String info = "URI=" + systemId + " Line=" + spe.getLineNumber()
		    + ": " + spe.getMessage();
	    return info;
	}

	public void warning(SAXParseException spe) throws SAXException {
	    out.println("Warning: " + getParseExceptionInfo(spe));
	}

	public void error(SAXParseException spe) throws SAXException {
	    String message = "Error: " + getParseExceptionInfo(spe);
	    throw new SAXException(message);
	}

	public void fatalError(SAXParseException spe) throws SAXException {
	    String message = "Fatal Error: " + getParseExceptionInfo(spe);
	    throw new SAXException(message);
	}
    }

    public Map<String, String> parse(String acsResponse) {
	ByteArrayInputStream stream = new ByteArrayInputStream(
		acsResponse.getBytes());

	SAXParserFactory spf = SAXParserFactory.newInstance();

	try {
	    spf.setNamespaceAware(true);
	    SAXParser saxParser = spf.newSAXParser();
	    XMLReader xmlReader = saxParser.getXMLReader();

	    SAMLParser samlParser = new SAMLParser();

	    xmlReader.setContentHandler(samlParser);
	    xmlReader.setErrorHandler(new MyErrorHandler(System.err));
	    xmlReader.parse(new InputSource(stream));
	    Map<String, String> copyOfMap = Collections
		    .unmodifiableMap(userPropertiesMap);
	    userPropertiesMap = null;
	    return copyOfMap;
	} catch (SAXException se) {
	    se.printStackTrace();
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	} catch (ParserConfigurationException e) {
	    e.printStackTrace();
	}
	return null;
    }

}
