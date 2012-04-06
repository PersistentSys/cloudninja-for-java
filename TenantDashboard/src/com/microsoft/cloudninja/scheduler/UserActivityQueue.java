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

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.stereotype.Component;

/**
 * In-memory queue for monitoring the user activity.
 *
 */
@Component
public class UserActivityQueue {

	private static Queue<UserActivityQueueMessage> userActivity;
	private static UserActivityQueue userActivityQueue;
	
	/**
	 * Default constructor.
	 */
	public UserActivityQueue() {
		userActivity = new LinkedList<UserActivityQueueMessage>();
	}
	
	public static UserActivityQueue getUserActivityQueue() {
		if (userActivityQueue == null) {
			userActivityQueue = new UserActivityQueue();
		}
		return userActivityQueue;
	}
	
	/**
	 * Adds an UserActivityQueueMessage.
	 * 
	 * @param user : userActivityQueueMessage to add.
	 */
	public void add(UserActivityQueueMessage user) {
		userActivity.offer(user);
	}
	
	/**
	 * Returns the UserActivityQueueMessage and removes it from queue.
	 * 
	 * @return the UserActivityQueueMessage.
	 */
	public UserActivityQueueMessage fetch() {
		return userActivity.poll();
	}
}
