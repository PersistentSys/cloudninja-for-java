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


/**
 *  Constant class for Scheduler operations configuration settings. 
 * 
 * */
public class SchedulerSettings {
    
    /* Constant for Tomcat Logs Container */
    public static final String WebTomcatLogsContainer = "wad-tomcat-logs";
    
    public static final String StorageAnalyticsLogsContainer = "$logs";
    
    /* Constant for Tomcat Logs Retention period in milliseconds(5 days) */
    public static final long WebTomcatLogsContainerRetentionPeriod = 5*24*60*60*1000;
    
    /* Constant for Diagnostic Tables Retention period in milliseconds(5 days) */
    public static final long DiagnosticsTablesRetentionPeriod = 5*24*60*60*1000;
    
    /*Enumeration for Diagnostic Tables Retention period in days */
    public static enum  DiagnosticsTables {WADPerformanceCountersTable, WADDirectoriesTable, WADLogsTable}
    
    /* Constant for Batch size in web band width usage */
    public static final int WEB_BANDWIDTH_USAGE_BATCH_SIZE = 5;

    /**
     * Message visibility timeout in seconds.
     */
    public static final int MessageVisibilityTimeout = 60;
    
    public static final long SchedulerInterval = 60*1000;
    public static final long GeneratorInterval = 60*1000;
    public static final long ProcessorInterval = 10*1000;
    
    public static final long TicksConversionConstant = 621355968000000000L;
    
    public static final String BlobStorageSizeTask = "tenantblobsize";
    public static final String BlobStorageSizeQueue = "tenantblobstoragesizequeue";

    public static final String DatabaseSizeTask = "tenantdbsize";
    public static final String DatabaseSizeQueue = "tenantdbsizequeue";
    
    public static final String DBBandwidthTask = "tenantdbbandwidth";
    public static final String DBBandwidthQueue = "tenantdbbandwidthqueue";
    
    public static final String StorageBandwidthTask = "storagebandwidth";
    public static final String StorageBandwidthQueue = "storagebandwidthqueue";
    
    public static final String MonitorPerformanceTask = "monitorperformance";
    public static final String MonitorDeploymentTask = "monitordeployment";
    
    public static final String TenantCreationQueue = "provisiontenant";
    public static final String TenantDeletionQueue = "deletetenant";
    
    public static final String WebBandWidthUsageTask = "tenantblobstorage";
    public static final String WebBandWidthUsageQueue = "tenantblobstoragequeue";
	public static final String AzureStorageCleanupTask = "azurestoragecleanup";

}
