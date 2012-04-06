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
package com.microsoft.cloudninja.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.microsoft.cloudninja.dao.UserActivityDao;
import com.microsoft.cloudninja.domainobject.UserActivityEntity;
import com.microsoft.cloudninja.domainobject.UserActivityId;

/**
 * UserActivityMonitor which keeps track of the logged in users.
 *
 */
@Component
public class UserActivityMonitor {

	private static final Logger LOGGER = Logger .getLogger(UserActivityMonitor.class);
	
	@Autowired
	private UserActivityDao userActivityDao;
	
	@Scheduled(fixedDelay=60*1000)
	public void monitorUserActivity() {
		try {
			LOGGER.debug("Start monitorUserActivity");
			UserActivityQueueMessage user = null;
			List<UserActivityQueueMessage> listUsers = 
				new ArrayList<UserActivityQueueMessage>();
			
			UserActivityQueue userActivityQueue = 
				UserActivityQueue.getUserActivityQueue();
			
			// fetch the info for all the users from queue.
			// queue message will contain the UserActivityQueueMessage.
			user = userActivityQueue.fetch();
			while (user != null) {
				listUsers.add(user);
				user = userActivityQueue.fetch();
			}
			
			// the map contains tenantId_userId as the key and 
			// UserActivityInfo as the value.
			Map<String, UserActivityInfo> mapUserActivityInfo = 
				new HashMap<String, UserActivityMonitor.UserActivityInfo>();
			String tenantIdAndUserId = null;
			UserActivityInfo userActivityInfo = null;
			// for each message make an entry in the map if not present,
			// if present update the timestamp and no. of requests
			// for the entry.
			for (UserActivityQueueMessage userActivityQueueMessage : listUsers) {
				tenantIdAndUserId = userActivityQueueMessage.toString(); 
				if (mapUserActivityInfo.containsKey(
						tenantIdAndUserId)) {
					userActivityInfo = mapUserActivityInfo.get(tenantIdAndUserId);
					userActivityInfo.updateDateAndRequests(
							userActivityQueueMessage.getTimestamp());
				} else {
					userActivityInfo = 
						new UserActivityInfo(userActivityQueueMessage.getTimestamp());
					mapUserActivityInfo.put(tenantIdAndUserId, userActivityInfo);
				}
			}
			
			// Retrieve the entries from map and put them into UserActivity table
			UserActivityEntity userActivityEntity = null;
			UserActivityId userActivityId = null;
			String[] arrTntUser = null;
			for (Iterator<String> iterator = mapUserActivityInfo.keySet().iterator();
					iterator.hasNext();) {
				tenantIdAndUserId = iterator.next();
				// split tenantIdAndUserId to get tenantId and userId.
				arrTntUser = tenantIdAndUserId.split("_");
				userActivityInfo = mapUserActivityInfo.get(tenantIdAndUserId);
				userActivityId = 
					new UserActivityId(userActivityInfo.minDate,
							arrTntUser[0],
							arrTntUser[1],
							userActivityInfo.requests);
				userActivityEntity = new UserActivityEntity(userActivityId);
				userActivityDao.add(userActivityEntity);
			}
			
			LOGGER.debug("Exit monitorUserActivity");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Keeps track of minimum of login times and total
	 * no. of requests for a tenantId and userId combination.
	 *
	 */
	private class UserActivityInfo {
		
		private Date minDate;
		private int requests;
		
		/**
		 * Constructor.
		 * 
		 * @param date
		 */
		public UserActivityInfo(Date date) {
			minDate = date;
			requests = 1;
		}
		
		/**
		 * Updates the minDate if the date parameter is less than minDate,
		 * and increments the requests by one.
		 * 
		 * @param date
		 */
		public void updateDateAndRequests(Date date) {
			if (date.getTime() < minDate.getTime()) {
				minDate = date;
			}
			requests++;
		}
	}
}
