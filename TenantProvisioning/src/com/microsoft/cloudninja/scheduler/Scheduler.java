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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.microsoft.cloudninja.dao.TaskScheduleDao;
import com.microsoft.cloudninja.domainobject.TaskScheduleEntity;
import com.microsoft.cloudninja.utils.SchedulerSettings;
import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.microsoft.windowsazure.services.queue.client.CloudQueueMessage;

/**
 * This class schedules the task depending on the schedule time.
 *
 */
public class Scheduler {

	private static final Logger LOGGER = Logger.getLogger(Scheduler.class);
	
	private StorageUtility storageUtility;
	
	private TaskScheduleDao taskScheduleDao;
	
	private List<String> taskList;

	@Autowired
	private TenantDeletionTask tenantDeletionTask;
	
	@Autowired
	private TenantCreationTask tenantCreationTask;
	
	@Autowired
	private PerformanceMonitor performanceMonitor;
	
	@Autowired
	private DeploymentMonitor deploymentMonitor;

	@Autowired
	private AzureStorageCleanupTask azureStorageCleanupTask;
	
	private static final String TASK_SCHEDULER_PREFIX = "taskscheduler-";
	
	/**
	 * Constructor which initializes the tasks.
	 * 
	 * @param storageUtility
	 * @param taskScheduleDao
	 */
	public Scheduler(StorageUtility storageUtility, TaskScheduleDao taskScheduleDao) {
		this.storageUtility = storageUtility;
		this.taskScheduleDao = taskScheduleDao;
		initializeTaskScheduleContainer();
		storageUtility.enableLoggingForBlobs();
		taskList = new ArrayList<String>();
		registerTask(SchedulerSettings.DatabaseSizeTask, 20);
		registerTask(SchedulerSettings.BlobStorageSizeTask, 20);
		registerTask(SchedulerSettings.DBBandwidthTask, 20);
		registerTask(SchedulerSettings.WebBandWidthUsageTask, 20);
		registerTask(SchedulerSettings.MonitorPerformanceTask, 10);
		registerTask(SchedulerSettings.MonitorDeploymentTask, 60);
		registerTask(SchedulerSettings.AzureStorageCleanupTask, 60);
		registerTask(SchedulerSettings.StorageBandwidthTask, 10);
	}
	
	/**
	 * Creates blob container 'taskscheduler' if not exists.
	 * 
	 */
	private void initializeTaskScheduleContainer() {
		try {
			// container for page blobs required while acquiring lease.
			storageUtility.initializeBlobContainer("taskscheduler");
			// container to store poison messages
			storageUtility.initializeBlobContainer("poison-messages");
		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (StorageException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Register a task in TaskSchedule table.
	 * 
	 * @param taskId : ID of task.
	 * @param frequencyInMin : frequency in minutes.
	 */
	private void registerTask(String taskId, long frequencyInMin) {
		try {
			TaskScheduleEntity taskScheduleEntity = new TaskScheduleEntity();
			taskScheduleEntity.setTaskId(taskId);
			taskScheduleEntity.setFrequency(frequencyInMin);
			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, (int) frequencyInMin * 60);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			String nextScheduledStartTime = dateFormat.format(calendar.getTime());
			
			taskScheduleEntity.setNextScheduledStartTime(dateFormat.parse(nextScheduledStartTime));
			taskScheduleDao.updateTaskSchedule(taskScheduleEntity);
			taskList.add(taskId);
			
			storageUtility.initializeQueue(TASK_SCHEDULER_PREFIX + taskId.toLowerCase());
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (StorageException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * This method will query the TaskSchedule table to find out the
	 * eligible tasks for scheduling.
	 * 
	 */
	@Scheduled(fixedDelay=SchedulerSettings.SchedulerInterval)
	public void doSchedule() {
		LOGGER.debug("Start schedule");
		String taskId = "";
		
		List<TaskScheduleEntity> listTaskSchedule = taskScheduleDao.getTaskScheduledList();
		
		for (TaskScheduleEntity taskScheduleEntity : listTaskSchedule) {
			try {
				Date nextScheduledTime = taskScheduleEntity.getNextScheduledStartTime();
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
				dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				String nextScheduledStartTime = dateFormat.format(calendar.getTime());
				Date currentTime = dateFormat.parse(nextScheduledStartTime);
				if (taskList.contains(taskScheduleEntity.getTaskId()) 
						&& nextScheduledTime.getTime() < currentTime.getTime()) {
					taskId = taskScheduleEntity.getTaskId();
					String leaseId = "";
					leaseId = acquireLock(taskId);
					LOGGER.debug("doSchedule : lease " + leaseId);
					if (!leaseId.isEmpty()) {
						try {
							storageUtility.putMessage(TASK_SCHEDULER_PREFIX + taskId.toLowerCase(), taskId);
							LOGGER.debug("doSchedule : put message in Q taskscheduler-" + taskId.toLowerCase());
							
							calendar = Calendar.getInstance();
							calendar.add(Calendar.SECOND, (int) taskScheduleEntity.getFrequency() * 60);
							dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
							dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
							nextScheduledStartTime = dateFormat.format(calendar.getTime());
							
							taskScheduleEntity.setNextScheduledStartTime(dateFormat.parse(nextScheduledStartTime));
							taskScheduleDao.updateTaskSchedule(taskScheduleEntity);
							
							storageUtility.releaseLease(taskId, leaseId);
							LOGGER.debug("doSchedule : lease released");
						} catch (Exception e) {
							LOGGER.error(e.getMessage(), e);
						}
					}
				}
			} catch (ParseException e) {
				LOGGER.error(e.getMessage(), e);
			}

		}
		LOGGER.debug("End schedule");
	}
	
	/**
	 * Acquire lease on page blob if acquired then return leaseId 
	 * else empty string.
	 * 
	 * @param id : task ID.
	 * @return : leaseId if lease is acquired else empty.
	 */
	private String acquireLock(String id) {
		String leaseId = "";
		
		try {
			leaseId = storageUtility.acquireLease(id.toLowerCase());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return leaseId;
	}
	
	/**
	 * Remove Tenant task.
	 */
	@Scheduled(fixedDelay=SchedulerSettings.ProcessorInterval)
	public void removeTenant() {
		if (tenantDeletionTask.execute()) {
			try {
				ActivityWorkQueue activityWorkQueue = tenantDeletionTask.getWorkQueue();
				activityWorkQueue.complete();
			} catch (StorageException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Register Tenant task.
	 */
	@Scheduled(fixedDelay=SchedulerSettings.ProcessorInterval)
	public void registerTenant() {
		if (tenantCreationTask.execute()) {
			try {
				ActivityWorkQueue activityWorkQueue = tenantCreationTask.getWorkQueue();
				activityWorkQueue.complete();
			} catch (StorageException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Monitor performance task.
	 */
	@Scheduled(fixedDelay=SchedulerSettings.ProcessorInterval)
	public void monitorPerformance() {
		LOGGER.debug("SCH-monitorPerformance : enter");
		CloudQueueMessage message = null;
		try {
			message = storageUtility.getMessage(TASK_SCHEDULER_PREFIX +
					SchedulerSettings.MonitorPerformanceTask.toLowerCase());
			if (message != null) {
				LOGGER.debug("SCH-monitorPerformance : msg is not null");
				if(performanceMonitor.execute()) {
					storageUtility.deleteMessage(TASK_SCHEDULER_PREFIX +
							SchedulerSettings.MonitorPerformanceTask.toLowerCase(), message);
					LOGGER.debug("SCH-monitorPerformance : msg deleted");
				}
			}
			LOGGER.debug("SCH-monitorPerformance : exit");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Monitor deployment task.
	 */
	@Scheduled(fixedDelay=SchedulerSettings.ProcessorInterval)
	public void monitorDeployment() {
		LOGGER.debug("SCH-monitorDeployment : enter");
		CloudQueueMessage message = null;
		try {
			message = storageUtility.getMessage(TASK_SCHEDULER_PREFIX +
					SchedulerSettings.MonitorDeploymentTask.toLowerCase());
			if (message != null) {
				LOGGER.debug("SCH-monitorDeployment : msg is not null");
				if(deploymentMonitor.execute()) {
					storageUtility.deleteMessage(TASK_SCHEDULER_PREFIX +
							SchedulerSettings.MonitorDeploymentTask.toLowerCase(), message);
					LOGGER.debug("SCH-monitorDeployment : msg deleted");
				}
			}
			LOGGER.debug("SCH-monitorDeployment : exit");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Azure Cleanup task.
	 */
	@Scheduled(fixedDelay=SchedulerSettings.ProcessorInterval)
	public void azureStorageCleanup() {
		LOGGER.debug("SCH-azureStorageCleanup : enter");
		CloudQueueMessage message = null;
		try {
			message = storageUtility.getMessage(TASK_SCHEDULER_PREFIX +
					SchedulerSettings.AzureStorageCleanupTask.toLowerCase());
			if (message != null) {
				LOGGER.debug("SCH-azureStorageCleanup : msg is not null");
				if(azureStorageCleanupTask.execute()) {
					storageUtility.deleteMessage(TASK_SCHEDULER_PREFIX +
							SchedulerSettings.AzureStorageCleanupTask.toLowerCase(), message);
					LOGGER.debug("SCH-azureStorageCleanup : msg deleted");
				}
			}
			LOGGER.debug("SCH-azureStorageCleanup : exit");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
