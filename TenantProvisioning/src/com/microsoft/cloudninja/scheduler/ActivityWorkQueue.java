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

import java.net.URISyntaxException;

import org.apache.log4j.Logger;

import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.microsoft.windowsazure.services.queue.client.CloudQueue;
import com.microsoft.windowsazure.services.queue.client.CloudQueueClient;
import com.microsoft.windowsazure.services.queue.client.CloudQueueMessage;

/**
 * This class is a wrapper over CloudQueue.
 *
 */
public class ActivityWorkQueue {
	
	private static final Logger LOGGER = Logger.getLogger(ActivityWorkQueue.class);
	
	private CloudQueue cloudQueue;
	private CloudQueueMessage lastMessage;
	
	/**
	 * Constructor which creates the queue.
	 * 
	 * @param queueName : name of queue.
	 * @param storageClient : cloud queue client.
	 */
	public ActivityWorkQueue(String queueName, CloudQueueClient storageClient) {
		try {
			cloudQueue = storageClient.getQueueReference(queueName);
			cloudQueue.createIfNotExist();
		} catch (StorageException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Adds a message to queue.
	 * 
	 * @param messageBody : content to be added to queue.
	 * @throws StorageException
	 */
	public void enqueue(String messageBody) throws StorageException {
		CloudQueueMessage message = new CloudQueueMessage(messageBody);
		cloudQueue.addMessage(message);
	}
	
	/**
	 * Retrieves a message from queue with some visibility timeout period.
	 * 
	 * @param timespan : visibility timeout period.
	 * @return : message content.
	 * @throws StorageException
	 */
	public String dequeue(int timespan) throws StorageException {
		String retMessage = null;
		CloudQueueMessage message = cloudQueue.retrieveMessage(timespan, null, null);
		lastMessage = message;
		if (message != null) {
			retMessage = message.getMessageContentAsString();
		}
		return retMessage;
	}
	
	/**
	 * Deletes a message from queue.
	 * 
	 * @throws StorageException
	 */
	public void complete() throws StorageException {
		if (lastMessage != null) {
			cloudQueue.deleteMessage(lastMessage);
			lastMessage = null;
		}
	}
}
