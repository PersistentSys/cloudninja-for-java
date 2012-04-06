CREATE TABLE [dbo].[Metering]
(
    [TenantId] [nvarchar](20) NOT NULL,
    [SnapshotTime] [datetime] NOT NULL,
    [DatabaseSize] [bigint]  default(0),
    [DatabaseBandwidth_Ingress] [bigint]  NULL default(0),
    [DatabaseBandwidth_Egress] [bigint]  NULL default(0),
    [WebAppBandwithUse_SC] [bigint] NULL default(0),
    [WebAppBandwithUse_CS] [bigint] NULL default(0),
    [WebAppRequests] [bigint] NULL default (0),
    [BlobStoreUsage] [bigint] NULL default(0),
    CONSTRAINT [PrimaryKey_Metering] PRIMARY KEY CLUSTERED 
    (
        [TenantId] ASC,
        [SnapshotTime] ASC
    )
) 


CREATE TABLE [dbo].[IISLogMeteringBatch]
(
    [BatchID] uniqueidentifier NOT NULL,
    [LastUpdatedTime] [datetime] NOT NULL,
    [LogUri] [nvarchar](250) NOT NULL,
    [LastLineProcessed] int,
    
    CONSTRAINT [PrimaryKey_MeteringBatch] PRIMARY KEY CLUSTERED 
    (
        [BatchID] ASC,
        [LogUri] ASC
    )
) 

ALTER TABLE [dbo].[IISLogMeteringBatch] ADD  CONSTRAINT [DF_IISLogMeteringBatch]  DEFAULT ((0)) FOR [LastLineProcessed]

CREATE TABLE [dbo].[WebLogMeteringBatch]
(
    [BatchID] uniqueidentifier NOT NULL,
    [LastUpdatedTime] [datetime] NOT NULL,
    [LogUri] [nvarchar](250) NOT NULL,
    [LastLineProcessed] int default(0),
    
    CONSTRAINT [PrimaryKey_WebLogMeteringBatch] PRIMARY KEY CLUSTERED 
    (
        [BatchID] ASC,
        [LogUri] ASC
    )
)

CREATE TABLE [dbo].[RoleInstances]
(
    [SnapshotTime] [datetime] NOT NULL default (getutcdate()),
    [RoleName] nvarchar(50) NOT NULL,
    [InstanceCount] int NOT NULL,
    [Reason] nvarchar(6) NOT NULL

        CONSTRAINT [PrimaryKey_RoleInstances] PRIMARY KEY CLUSTERED 
    (
        [SnapshotTime] ASC,
        [RoleName] ASC
    )
)

CREATE TABLE [dbo].[StorageAnalyticsLogsSummary]
(
    [SnapshotTime] [datetime] NOT NULL default (getutcdate()),
    [OperationType] nvarchar(60) NOT NULL,
    [RequestStatus] nvarchar(60) NOT NULL,
    [HttpStatusCode] nvarchar(20) NOT NULL,
    [E2ELatency] float NOT NULL,
    [ServerLatency] float NOT NULL,
    [ServiceType] nvarchar(10) NOT NULL,
    [Tenant] nvarchar(256) NOT NULL,
    [RequestPacketSize] [bigint] NOT NULL,
    [ResponsePacketSize] [bigint] NOT NULL,
    [Count] [int] NOT NULL,
    [Billable] [bit] NOT NULL,
    CONSTRAINT [PrimaryKey_StorageAnalytics] PRIMARY KEY CLUSTERED 
    (
        [Tenant] ASC,
        [ServiceType] ASC,
        [SnapshotTime] ASC,
        [OperationType] ASC,
        [RequestStatus] ASC,
        [HttpStatusCode] ASC		
        )
)

CREATE TABLE [dbo].[StorageBandwidthBatch]
(
    [LogUri] [nvarchar](250) NOT NULL,
    [LastUpdatedTime] [datetime] NOT NULL,
    [LastLineProcessed] int default(0),
    
    CONSTRAINT [PrimaryKey_StorageBandwidthBatch] PRIMARY KEY CLUSTERED 
    (
        [LogUri] ASC
    )
)

GO
create view STORAGEMETERING_MONTHLY_VIEW as
SELECT [Tenant],
DATEPART(YY, [SnapshotTime]) AS [Year]
,DATEPART(MM, [SnapshotTime]) AS [Month]
      ,SUM([RequestPacketSize]) AS [TotalRequestPacketSize]
      ,SUM([ResponsePacketSize]) AS [TotalResponsePacketSize]
      ,SUM([Count]) AS [TotalStorageTransactions]      
  FROM StorageAnalyticsLogsSummary
  where [Billable] = 1
group by [Tenant], DATEPART(MM, [SnapshotTime]),DATEPART(YY, [SnapshotTime])
  
GO

go
CREATE VIEW ALL_METERING_MONTHLY_VIEW AS 
SELECT TenantId
,DATEPART(YY, SnapShotTime) AS [Year]
,DATEPART(MM, SnapShotTime) AS [Month]
,AVG([DatabaseSize]) AS [DatabaseSize]
,SUM([DatabaseBandwidth_Ingress]) AS [DatabaseBandwidth_Ingress]
,SUM([DatabaseBandwidth_Egress]) AS [DatabaseBandwidth_Egress]
,SUM([WebAppBandwithUse_SC]) AS [WebAppBandwithUse_SC]
,SUM([WebAppBandwithUse_CS]) AS [WebAppBandwithUse_CS]
,SUM([WebAppRequests]) AS [WebAppRequests]
,AVG([BlobStoreUsage]) AS [BlobStoreUsage]
FROM Metering
GROUP BY TenantId,  DATEPART(YY, SnapShotTime), DATEPART(MM, SnapShotTime)

GO

create view STORAGEMETERING_DAILY_VIEW as
SELECT [Tenant]
,DATEPART(YY, [SnapshotTime]) AS [Year]
,DATEPART(MM, [SnapshotTime]) AS [Month]
,DATEPART(DD, [SnapshotTime]) AS [Day]
      ,SUM([RequestPacketSize]) AS [TotalRequestPacketSize]
      ,SUM([ResponsePacketSize]) AS [TotalResponsePacketSize]
      ,SUM([Count]) AS [TotalStorageTransactions]      
  FROM StorageAnalyticsLogsSummary
  where [Billable] = 1
group by [Tenant], DATEPART(YY, [SnapshotTime]), DATEPART(MM, [SnapshotTime]), DATEPART(DD, [SnapshotTime])
  
GO


CREATE VIEW ALL_METERING_DAILY_VIEW AS 
SELECT TenantId
,DATEPART(YY, SnapShotTime) AS [Year]
,DATEPART(MM, SnapShotTime) AS [Month]
,DATEPART(DD, SnapShotTime) AS [Day]
,AVG([DatabaseSize]) AS [DatabaseSize]
,SUM([DatabaseBandwidth_Ingress]) AS [DatabaseBandwidth_Ingress]
,SUM([DatabaseBandwidth_Egress]) AS [DatabaseBandwidth_Egress]
,SUM([WebAppBandwithUse_SC]) AS [WebAppBandwithUse_SC]
,SUM([WebAppBandwithUse_CS]) AS [WebAppBandwithUse_CS]
,SUM([WebAppRequests]) AS [WebAppRequests]
,AVG([BlobStoreUsage]) AS [BlobStoreUsage]
FROM Metering
GROUP BY TenantId, DATEPART(YY, SnapShotTime), DATEPART(MM, SnapShotTime), DATEPART(DD, SnapShotTime)

GO