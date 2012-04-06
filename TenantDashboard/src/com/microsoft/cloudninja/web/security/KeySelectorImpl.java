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
package com.microsoft.cloudninja.web.security;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;
import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.CertPathBuilder;

public class KeySelectorImpl extends KeySelector{ 
	
	public static String keyStorePassword ;
	public static String distinguishedName;
	public static String keyStorePath;
	
	@SuppressWarnings("static-access")
	public KeySelectorImpl(String keyStorePassword, String distinguishedName, String keyStorePath){
		this.keyStorePassword = keyStorePassword;
		this.distinguishedName = distinguishedName;
		this.keyStorePath	= 	keyStorePath;
		
	}
	
	
	@Override
	public KeySelectorResult select(KeyInfo keyInfo, Purpose arg1,
			AlgorithmMethod arg2, XMLCryptoContext arg3)
			throws KeySelectorException {
		
		System.out.println("KeySelectorImpl ,select , start ");
		
		if(keyInfo == null)	{
			throw new KeySelectorException("keyInfo is not found");
		}
		
		@SuppressWarnings("rawtypes")
		List keyInfoContent = keyInfo.getContent();
		for(Object content : keyInfoContent) {

			XMLStructure xmlStructureContent = (XMLStructure)content;
			if(xmlStructureContent instanceof X509Data)
			{
				X509Data x509Data = (X509Data)xmlStructureContent;
				
				@SuppressWarnings("rawtypes")
				List x509Contents = x509Data.getContent();
				for(Object x509 : x509Contents)
				{
					
					List<X509Certificate> x509Certificates = new ArrayList<X509Certificate>();
					if(x509 instanceof X509Certificate)	{
						X509Certificate certificate = (X509Certificate) x509;
						x509Certificates.add(certificate);						
					}
					
					if(x509Certificates.size() == 0) {
						throw new KeySelectorException("X509 certificate not found in X509Data of KeyInfo");
					}
					
					//Building a certificate path
					PKIXCertPathBuilderResult buildResult = buildValidatedCertPath(x509Certificates);
										
					return new BasicKeySelectorResult(buildResult.getPublicKey());	
				}
				break;
			}
			
			throw new KeySelectorException("X509Data not found in KeyInfo");
			
		
			
		}
		
		return null;
	
		
	}
	
	
	PKIXCertPathBuilderResult buildValidatedCertPath(List<? extends X509Certificate> certificates) throws KeySelectorException
	{
			
		try
		{
			System.out.println("Loading cdertificate from keystore");
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			BufferedInputStream bis_trust_store = new BufferedInputStream(new FileInputStream(keyStorePath));
			trustStore.load(bis_trust_store, keyStorePassword.toCharArray());
			bis_trust_store.close();
			

			System.out.println("Buildind certificate path");
			CertPathBuilder pathBuilder = CertPathBuilder.getInstance("PKIX");
			// Create a selector to search for the signing suject
			X509CertSelector certSelector = new X509CertSelector();
			certSelector.setSubject(new X500Principal(distinguishedName));
			
			
			PKIXBuilderParameters builderParams = new PKIXBuilderParameters(trustStore, certSelector);
			builderParams.setRevocationEnabled(false);
			//Create cert store with the given certificates
			CollectionCertStoreParameters storeParams = new CollectionCertStoreParameters(certificates);
			CertStore certStore = CertStore.getInstance("Collection", storeParams);
			builderParams.addCertStore(certStore);
			
			PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult)pathBuilder.build(builderParams);
			return result;		
			
		}
		catch(CertPathBuilderException cpbe) {
			
			throw new KeySelectorException("An exception occured while validating the certificate path given in the xml signature.", cpbe);
		}
		catch(Exception e)	{
			throw new KeySelectorException("An exception occured while validating the certificate path given in the xml signature.", e);
		}	
		
	}

	private static class BasicKeySelectorResult implements KeySelectorResult	{
		private Key publicKey;
		
		public BasicKeySelectorResult(Key publicKey) {
			this.publicKey = publicKey;
		}
		@Override
		public Key getKey() {			
			return publicKey;
		}
		
	}

}

