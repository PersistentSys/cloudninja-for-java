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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.persistent.cloudninja.dao.KpiValueTempDao;
import com.persistent.cloudninja.dao.TaskCompletionDao;
import com.persistent.cloudninja.domainobject.KpiValueTempEntity;
import com.persistent.cloudninja.domainobject.KpiValueTempId;
import com.persistent.cloudninja.domainobject.WADPerformanceCountersEntity;
import com.persistent.cloudninja.utils.SchedulerSettings;

/**
 * The Performance monitor task gets the performance counters from WADPerformanceCountersTable,
 * puts it into KpiValueTemp and then calls ProcessKpiValues stored procedure.
 *
 */
@Component
public class PerformanceMonitor implements IActivity {

	private static final Logger LOGGER = Logger.getLogger(PerformanceMonitor.class);
	
	@Autowired
	private KpiValueTempDao kpiValueTempDao;
	
	@Autowired
	private StorageUtility storageUtility;
	
	@Autowired
	private TaskCompletionDao taskCompletionDao;
	
	@Override
	public boolean execute() {
	    StopWatch watch = new StopWatch();
		try {
			LOGGER.debug("PerformanceMonitor : Execute");
		    watch.start();
			Date lastReadTime = kpiValueTempDao.getMaxTimestamp();
			Date lastBatchTime = null;
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			String date = null;
			if (lastReadTime == null) {
				date = dateFormat.format(calendar.getTime());
				lastReadTime = dateFormat.parse(date);
			}
			calendar = Calendar.getInstance();
			date = dateFormat.format(calendar.getTime());
			Date currentUTCTime = dateFormat.parse(date);
			List<KpiValueTempEntity> listKpiValueTempEntities = null;
			while (lastReadTime.getTime() <= currentUTCTime.getTime()) {
				LOGGER.debug("PerformanceMonitor : Process performance counters");
				calendar.setTime(lastReadTime);
				calendar.add(Calendar.MINUTE, 30);
				date = dateFormat.format(calendar.getTime());
				lastBatchTime = dateFormat.parse(date);
				
				WADPerformanceCountersEntity[] arrPerfCounter =
					storageUtility.getPerfCounterEntities(
							lastReadTime.getTime(), lastBatchTime.getTime());
				
				listKpiValueTempEntities =
					convertAzureEntityToDBEntity(arrPerfCounter);
				
				kpiValueTempDao.addAll(listKpiValueTempEntities);
				kpiValueTempDao.executeProcessKpiValues();
				LOGGER.debug("PerformanceMonitor : Process complete");
				lastReadTime = lastBatchTime;
			}
			watch.stop();
			taskCompletionDao.updateTaskCompletionDetails(watch.getTotalTimeSeconds(),"ProcessPerformanceCounters","");
			LOGGER.debug("PerformanceMonitor : Finish");
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return true;
	}

	/**
	 * Converts WADPerformanceCountersTable azure table entity to
	 * KpiValueTemp entity.
	 * 
	 * @param arrPerfCounter : array of performance counters.
	 * @return the kpiValueTemp entity list.
	 */
	private List<KpiValueTempEntity> convertAzureEntityToDBEntity(
			WADPerformanceCountersEntity[] arrPerfCounter) {
		WADPerformanceCountersEntity perfCntrEntity = null;
		List<KpiValueTempEntity> listKpiValueTempEntities = 
			new ArrayList<KpiValueTempEntity>();
		KpiValueTempId kpiValueTempId = null;
		KpiValueTempEntity kpiValueTempEntity = null;
		Date date = null;
		try {
			for (int i = 0; i < arrPerfCounter.length; i++) {
				perfCntrEntity = arrPerfCounter[i];
				long eventTickCount = perfCntrEntity.getEventTickCount() 
					- SchedulerSettings.TicksConversionConstant;
				date = new Date(eventTickCount/10000);
				kpiValueTempId = 
					new KpiValueTempId(perfCntrEntity.getRole(),
							perfCntrEntity.getRoleInstance(),
							getInstanceNo(perfCntrEntity.getRoleInstance()),
							date,
							perfCntrEntity.getCounterName(),
							Math.round(
							Double.parseDouble(perfCntrEntity.getCounterValue())));
				kpiValueTempEntity = new KpiValueTempEntity(kpiValueTempId);
				listKpiValueTempEntities.add(kpiValueTempEntity);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return listKpiValueTempEntities;
	}

	/**
	 * Returns the instance no. from role instance.
	 * 
	 * @param roleInstance : the role instance.
	 * @return
	 */
	private int getInstanceNo(String roleInstance) {
		int instanceNo = 0;
		int indexForInstNo = 0;
		String strInstNo = "";
		if (roleInstance.contains("_IN_")) {
			indexForInstNo = roleInstance.lastIndexOf("_IN_") + 4;
			strInstNo = roleInstance.substring(indexForInstNo);
			instanceNo = Integer.parseInt(strInstNo);
		} else {
			indexForInstNo = roleInstance.lastIndexOf(".") + 1;
			strInstNo = roleInstance.substring(indexForInstNo);
			instanceNo = Integer.parseInt(strInstNo);
		}
		return instanceNo;
	}

}
