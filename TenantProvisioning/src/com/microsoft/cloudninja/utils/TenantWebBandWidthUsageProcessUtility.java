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
package com.microsoft.cloudninja.utils;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microsoft.cloudninja.dao.TenantIdMasterDao;
import com.microsoft.cloudninja.domainobject.MeteringWebBandWidth;
import com.microsoft.cloudninja.domainobject.TenantIdMasterEntity;
import com.microsoft.cloudninja.domainobject.WebLogMeteringBatch;
import com.microsoft.cloudninja.scheduler.StorageUtility;
import com.microsoft.cloudninja.service.WebLogMeteringBatchService;

/**
 * 
 * This Utility class is used for processing Tenant Web Band Width Usage Data
 * will be inserted into Metering Table as well as WebLogMeteringBatch Table
 * 
 * 
 */
@Component
public class TenantWebBandWidthUsageProcessUtility {

	@Autowired
	private WebLogMeteringBatchService webLogMeteringBatchService;

	@Autowired
	private TenantIdMasterDao tenantIdMasterDao;

	@Autowired
	private StorageUtility storageUtility;

	private static final Logger LOGGER = Logger
			.getLogger(TenantWebBandWidthUsageProcessUtility.class);

	private static final long BYTES_TO_KB_CONSTANT = 1024;

	public void getTenantWebBandWidthUsage(
			WebLogMeteringBatch logMeteringBatchElement) {
		LOGGER.info("Start of TenantWebBandWidthUsageProcessUtility:getTenantWebBandWidthUsage ");
		try {
			int lineNum = 0;
			int lastProcessedLine = 0;
			Date lastProcessedTime = CommonUtility
					.getDate(CommonUtility.MIN_DATE);
			List<MeteringWebBandWidth> meteringWebBandWidthList = new ArrayList<MeteringWebBandWidth>();
			WebLogMeteringBatch webLogMeteringBatch = new WebLogMeteringBatch();
			String read_line;
			Date latestModifiedDate = storageUtility
					.getLastModifiedDateFromBlob(logMeteringBatchElement
							.getLogUri());
			BufferedReader in = storageUtility
					.getReaderStreamForBlob(logMeteringBatchElement.getLogUri());

			// Get the tenantList
			List<TenantIdMasterEntity> listTenant = tenantIdMasterDao
					.getAllTenants();
			List<String> tenantIdList = new ArrayList<String>();
			if (null != listTenant) {
				for (TenantIdMasterEntity listTenantResult : listTenant) {
					tenantIdList.add(listTenantResult.getTenantId());
				}
			}

			// Check date stamp from WebLogMeteringBatch
			List<WebLogMeteringBatch> webLogMeteringBatchList = webLogMeteringBatchService
					.getMeteringBatchServiceDetails(logMeteringBatchElement
							.getLogUri());
			if (!webLogMeteringBatchList.isEmpty()) {

				// Set lastProcessedTime to largest LastUpdatedTime
				lastProcessedTime = webLogMeteringBatchList.get(0)
						.getLastUpdatedTime();
				// Set lastProcessedLine to largest LastLineProcessed
				lastProcessedLine = webLogMeteringBatchList.get(0)
						.getLastLineProcessed();
			}

			// Insertion only if it is new record.We need not check record which
			// is already inserted
			LOGGER.info("latestModifiedDate.getTime() : "
					+ latestModifiedDate.getTime());
			LOGGER.info("lastProcessedTime() : " + lastProcessedTime.getTime());
			if (latestModifiedDate.getTime() > lastProcessedTime.getTime()) {

				// Set last lastProcessedTime to modified time
				lastProcessedTime = latestModifiedDate;
				// Skip lines if log file is updated.Set to line number till
				// which it was processed previously
				if (lastProcessedLine > 0) {
					for (int i = 0; i < lastProcessedLine; i++) {
						read_line = in.readLine();
						lineNum++;
					}
				}

				// Process records in Blob
				while ((read_line = in.readLine()) != null) {

					MeteringWebBandWidth meteringWebBandWidth = processRecord(
							read_line, tenantIdList);
					if (StringUtils.isNotBlank(meteringWebBandWidth
							.getTenantId())) {
						meteringWebBandWidthList.add(meteringWebBandWidth);
						lineNum++;
					}
				}

				lastProcessedLine = lineNum;
				in.close();

				// If latestModifiedDate is equal to lastProcessedTime we do not
				// perform any operations
				if (!meteringWebBandWidthList.isEmpty()) {
					// Insert into metering table
					populateMeteringTable(meteringWebBandWidthList);

					// Insert into WebLogMeteringBatch
					populateWebLogMeteringBatchTable(webLogMeteringBatch, UUID
							.randomUUID().toString(), lastProcessedLine,
							lastProcessedTime,
							logMeteringBatchElement.getLogUri());
				}
			}

		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}
		LOGGER.info("End of TenantWebBandWidthUsageProcessUtility:getTenantWebBandWidthUsage ");
	}

	private MeteringWebBandWidth processRecord(String read_line,
			List<String> tenantIdList) {
		MeteringWebBandWidth meteringWebBandWidth = new MeteringWebBandWidth();
		String[] temp = read_line.split(",");
		String tenatId = getTenantId(temp[1], tenantIdList);
		String request_bytes = temp[2];
		String response_bytes = temp[3];
		long webBWUseCS = 0;
		long webBWUseSC = 0;
		if (StringUtils.isNotBlank(tenatId)) {
			meteringWebBandWidth.setTenantId(tenatId);
			if (request_bytes.equals("-")) {
				meteringWebBandWidth.setWebAppBandwithUse_CS(0);
			} else {
				webBWUseCS = Long.parseLong(request_bytes);
				// convert bytes to kilobytes
				webBWUseCS = webBWUseCS / BYTES_TO_KB_CONSTANT;
				meteringWebBandWidth.setWebAppBandwithUse_CS(webBWUseCS);
			}
			if (response_bytes.equals("-")) {
				meteringWebBandWidth.setWebAppBandwithUse_SC(0);
			} else {
				webBWUseSC = Long.parseLong(response_bytes);
				// convert bytes to kilobytes
				webBWUseSC = webBWUseSC / BYTES_TO_KB_CONSTANT;
				meteringWebBandWidth.setWebAppBandwithUse_SC(webBWUseSC);
			}
		}
		return meteringWebBandWidth;
	}

	/*
	 * Insert Data into Metering table
	 * 
	 * @param List:MeteringWebBandWidth
	 */
	public void populateMeteringTable(
			List<MeteringWebBandWidth> meteringWebBandWidthList) {
		Map<String, MeteringWebBandWidth> testMap = new HashMap<String, MeteringWebBandWidth>();
		// Summation for request and response bytes
		for (int i = 0; i < meteringWebBandWidthList.size(); i++) {
			MeteringWebBandWidth meteringWebBandWidth = meteringWebBandWidthList
					.get(i);
			if (testMap.containsKey((meteringWebBandWidth.getTenantId()))) {
				MeteringWebBandWidth meteringWebBandWidth_map = testMap
						.get(meteringWebBandWidth.getTenantId());
				meteringWebBandWidth_map
						.setWebAppRequests(meteringWebBandWidth_map
								.getWebAppRequests() + 1);
				meteringWebBandWidth_map
						.setWebAppBandwithUse_CS(meteringWebBandWidth_map
								.getWebAppBandwithUse_CS()
								+ meteringWebBandWidth
										.getWebAppBandwithUse_CS());
				meteringWebBandWidth_map
						.setWebAppBandwithUse_SC(meteringWebBandWidth_map
								.getWebAppBandwithUse_SC()
								+ meteringWebBandWidth
										.getWebAppBandwithUse_SC());
			} else {
				meteringWebBandWidth.setWebAppRequests(1);
				testMap.put(meteringWebBandWidth.getTenantId(),
						meteringWebBandWidth);
			}
		}
		// Insertion into metering table
		for (Entry<String, MeteringWebBandWidth> entry : testMap.entrySet()) {
			entry.getValue().setSnapShotTime(new Date());
			webLogMeteringBatchService.insertMeteringDetails(entry.getValue());
		}
	}

	/*
	 * Insert Data into WebLogMeteringBatchTable
	 * 
	 * @param List:MeteringWebBandWidth
	 */
	private void populateWebLogMeteringBatchTable(
			WebLogMeteringBatch webLogMeteringBatch, String UUID,
			int lastProcessedLine, Date lastProcessedTime, String LogUri) {

		webLogMeteringBatch.setBatchID(UUID);
		webLogMeteringBatch.setLastLineProcessed(lastProcessedLine);
		webLogMeteringBatch.setLastUpdatedTime(lastProcessedTime);
		webLogMeteringBatch.setLogUri(LogUri);
		webLogMeteringBatchService
				.insertWebLogMeteringDetails(webLogMeteringBatch);
	}

	/*
	 * Retrieve TenantId from request URL
	 * 
	 * @param String URL
	 * 
	 * @param List: String tenantId's from table
	 * 
	 * @returns String :TenantId
	 */
	public String getTenantId(String url, List<String> tenantIdList) {
		String result = "";
		String[] arr = url.split("\\.");
		String tenantId = arr[0];
		if (tenantIdList.contains(tenantId)) {
			result = tenantId;
		}
		return result;
	}
}
