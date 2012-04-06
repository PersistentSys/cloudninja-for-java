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
package com.microsoft.cloudninja.acs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.restlet.Request;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Parameter;
import org.restlet.engine.http.header.ChallengeWriter;
import org.restlet.engine.security.AuthenticatorHelper;
import org.restlet.util.Series;

public class ACSAuthenticationHelper extends AuthenticatorHelper {
	// Note: The technical name must be Bearer
	public static ChallengeScheme SCHEME_ACS = new ChallengeScheme(
			"ChallengeScheme for ACS", "Bearer");

	// ACS URLs
	public static String AcsTokenServiceUrl = "https://%s.accesscontrol.windows.net/v2/OAuth2-13/";
	public static String AcsODataServiceUrl = "https://%s.accesscontrol.windows.net/v2/mgmt/service/";
	
	private String managementUserName;

	/**
	 * Constructor.
	 */
	public ACSAuthenticationHelper(String userName) {
		super(ACSAuthenticationHelper.SCHEME_ACS, true, false);
		managementUserName = userName;
	}

	/**
     * Formats a challenge response as a HTTP Authorization header value 
     * needed by ACS Management OData Service
     */ 
	@Override
	public String formatResponse(ChallengeResponse challenge, Request request,
			Series<Parameter> httpHeaders) {
		ChallengeWriter hb = new ChallengeWriter();
		hb.append(challenge.getScheme().getTechnicalName()).appendSpace();

		if (challenge.getRawValue() != null) {
			hb.append(challenge.getRawValue());
		} else {
			formatRawResponse(hb, challenge, request, httpHeaders);
		}

		// Get ACS credentails
		String acsNameSpace = challenge.getIdentifier();
		String acsMgmtPassword = new String(challenge.getSecret());

		// Get token from ACS
		String swtToken = getTokenFromACS(acsNameSpace, acsMgmtPassword);

		// Append ACS token to the ChallengeWriter. Restlet library will
		// automatically
		// add this as Authorization HTTP header and use scheme technical name
		// ("bearer").
		return hb.toString() + swtToken;
	}

	/**
	 * Get OAuth SWT token from ACS
	 */
	private String getTokenFromACS(String acsNamespace, String acsMgmtPassword) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(String.format(
					ACSAuthenticationHelper.AcsTokenServiceUrl, acsNamespace));

			// Prepare POST data for the HTTP request
			List<NameValuePair> listNameValuePairs = new ArrayList<NameValuePair>();
			listNameValuePairs.add(new BasicNameValuePair("grant_type",
					"client_credentials"));
			listNameValuePairs.add(new BasicNameValuePair("client_id",
					managementUserName));
			listNameValuePairs.add(new BasicNameValuePair("client_secret",
					acsMgmtPassword));
			listNameValuePairs.add(new BasicNameValuePair("scope", String
					.format(ACSAuthenticationHelper.AcsODataServiceUrl,
							acsNamespace)));

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					listNameValuePairs);
			httpPost.setEntity(formEntity);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();

			InputStream inputStream = entity.getContent();
			InputStreamReader streamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(streamReader);
			String string = null;
			StringBuffer response = new StringBuffer();
			while ((string = bufferedReader.readLine()) != null) {
				response.append(string);
			}

			// Fetch token from the JSON response
			JSONObject obj = new JSONObject(response.toString());
			return obj.getString("access_token");
		} catch (Exception e) {
			// Could not fetch ACS Token
			e.printStackTrace();
			return null;
		}
	}
}
