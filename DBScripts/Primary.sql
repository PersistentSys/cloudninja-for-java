CREATE TABLE [dbo].[Location]
(
	[LocationId] int IDENTITY(1,1) PRIMARY KEY CLUSTERED,
	[DisplayName] nvarchar(30) NOT NULL,
	[TenantUrlTemplate] nvarchar(60) NOT NULL,
	[StorageNamespace] nvarchar(30) NOT NULL Default('')
)

CREATE TABLE [dbo].[DataServer]
(
	[Server] nvarchar(128) NOT NULL PRIMARY KEY CLUSTERED,
	[LocationId] int NOT NULL,
	[User] nvarchar(30) NOT NULL Default(''),
	[Password] nvarchar(128) NULL Default(''),
	[IsPrimary] bit NOT NULL Default(0),
	[MaxDatabases] int NOT NULL Default(100)

	CONSTRAINT [FK_DataServer_Location_LocationId]
	FOREIGN KEY (LocationId) REFERENCES Location(LocationId)
)

CREATE TABLE [dbo].[TenantIdMaster]
(
	[TenantId] nvarchar(20) NOT NULL PRIMARY KEY CLUSTERED, 
	[Status] nvarchar(20) NOT NULL Default('hold'),
	[Created] DateTime NOT NULL Default(GETUTCDATE()),
	[LocationId] int NOT NULL,
)

CREATE TABLE [dbo].[Tenant]
(
	[TenantId] nvarchar(20) NOT NULL PRIMARY KEY CLUSTERED,
	[Status] nvarchar(20) NOT NULL Default('provisioning'),
	[CompanyName] nvarchar(50) NOT NULL,
    [LogoFileName] nvarchar(30) NOT NULL Default(''),
	[Created] DateTime NOT NULL Default(GETUTCDATE()),
	[Thumbprint] nvarchar(50) NOT NULL Default(''),
	[LocationId] int NOT NULL
)

CREATE TABLE [dbo].[TenantDataConnection]
(
    [TenantId] nvarchar(20) NOT NULL PRIMARY KEY CLUSTERED,
    [Server] nvarchar(128) NOT NULL,
    [Database] nvarchar(30) NOT NULL,
    [User] nvarchar(30) NOT NULL,
    [Password] nvarchar(128) NOT NULL,
	[PasswordAlt] nvarchar(32) NOT NULL Default('')

	CONSTRAINT [FK_TenantDataConnection_Tenant_TenantId]
	FOREIGN KEY (TenantId) REFERENCES Tenant(TenantId)
)

CREATE TABLE [dbo].[ProvisioningLog]
(
	[TenantId] nvarchar(20) NOT NULL,
	[Task] nvarchar(50) NOT NULL,
	[Message] nvarchar(200) NOT NULL,
	[Created] datetime NOT NULL Default(GETUTCDATE())
	CONSTRAINT [FK_ProvisionStatus_Tenant_TenantId]
	FOREIGN KEY (TenantId) REFERENCES Tenant(TenantId)
)

CREATE CLUSTERED INDEX [IdxC_ProvisioningLog_TenantId] ON [ProvisioningLog](TenantId)

CREATE TABLE [dbo].[SystemAlert]
(
	[AlertId] int IDENTITY(1,1) PRIMARY KEY CLUSTERED,
	[TenantId] nvarchar(20) NOT NULL Default(''),
	[Type] nvarchar(30) NOT NULL Default('System'),
	[Message] nvarchar(1024) NOT NULL Default(''),
	[Time] datetime NOT NULL Default(GETUTCDATE()),
	[Role] nvarchar(256) NOT NULL Default(''),
	[Instance] nvarchar(256) NOT NULL Default(''),
	[RequestUrl] nvarchar(1024) NOT NULL Default(''),
	[Details] ntext NULL
)


CREATE TABLE [dbo].[TaskSchedule]
(
	[TaskId] nvarchar(20) not null PRIMARY KEY CLUSTERED,
	[Frequency] int not null,
	[NextScheduledStartTime] datetime not null
)

CREATE TABLE TaskCompletion
(
	[Id] int IDENTITY(1,1) PRIMARY KEY CLUSTERED,
	[TaskName] nvarchar(200) NOT NULL Default('System'),
	[CompletionTime] datetime not null Default(GETUTCDATE()),
	[ElapsedTime] float NOT NULL DEFAULT(0),
	[Details] nvarchar(1024) NOT NULL DEFAULT('')
)

CREATE TABLE  AutoScaleTimes
(
	[Id] int IDENTITY(1,1) PRIMARY KEY CLUSTERED,
	[RoleName] nvarchar(50) NOT NULL,
	[StartMow] int NOT NULL DEFAULT(-1),
	[EndMow] int NOT NULL DEFAULT(-1),
	[MinInstances] smallint NOT NULL DEFAULT(1),
	[MaxInstances] smallint NOT NULL DEFAULT(2)
)


insert into autoscaletimes (RoleName, StartMow, mininstances, maxinstances) values ('CloudNinja.Web.HelloTask', -1, 2, 4)


CREATE TABLE [dbo].[AutoScaleKPI]
(
	[Id] int IDENTITY(1,1) PRIMARY KEY CLUSTERED,
	[RoleName] nvarchar(50)  not null ,
	[Metric] nvarchar(100) not null,
	[MetricType] nvarchar(15) not null,
	[Evaluation] nvarchar(3) not null,
	[Threshold] float not null,
	[Duration] int not null,
	[InstanceIncrement] int not null,
)

CREATE TABLE [dbo].[IdentityProvider]
(
	[Id] int NOT NULL IDENTITY(1,1) PRIMARY KEY CLUSTERED,
	[Name] nvarchar(20) NOT NULL 
)

INSERT INTO [IdentityProvider] ([Name])
 VALUES ('CloudNinjaSts')
 
INSERT INTO [IdentityProvider] ([Name])
 VALUES ('Google')
 
INSERT INTO [IdentityProvider] ([Name])
 VALUES ('WindowsLiveID')
 
INSERT INTO [IdentityProvider] ([Name])
 VALUES ('Yahoo!')

INSERT INTO [Location] ([DisplayName],[TenantUrlTemplate],[StorageNamespace])
 VALUES ('South Central US', 'http://{0}.thecloudninja.com', 'friday')

INSERT INTO [DataServer] ([Server], [LocationId], [User] , [Password])
 VALUES (CAST(SERVERPROPERTY('ServerName') AS VARCHAR(64)) + '.database.windows.net', 1, 'psladmin@xnifcmw0bm', 'password')

insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement]) 
 values ('CloudNinja.Web.HelloTask', '\ASP.NET\Application Restarts', 'PerfCounter', 'gte', 2, 20, 1)
insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement]) 
 values ('CloudNinja.Web.HelloTask', '\ASP.NET\Application Restarts', 'PerfCounter', 'lte', 0, 20, -1)


insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement])  
values ('CloudNinja.Web.HelloTask',    '\ASP.NET Applications(__Total__)\Requests/Sec', 'PerfCounter', 'gte', 20, 20, 1)
insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement])  
values ('CloudNinja.Web.HelloTask',    '\ASP.NET Applications(__Total__)\Requests/Sec', 'PerfCounter', 'lte', 10, 20, -1)


insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement]) 
 values ('CloudNinja.Web.HelloTask', '\ASP.NET Applications(__Total__)\Errors Total', 'PerfCounter', 'gte', 2, 20, 1)
insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement]) 
 values ('CloudNinja.Web.HelloTask', '\ASP.NET Applications(__Total__)\Errors Total', 'PerfCounter', 'lte', 0, 20, -1)


insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement]) 
 values ('CloudNinja.Web.HelloTask', '\ASP.NET\Request Execution Time', 'PerfCounter', 'gte', 5000, 20, 1)
insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement])  
values ('CloudNinja.Web.HelloTask', '\ASP.NET\Request Execution Time', 'PerfCounter', 'lte', 2000, 20, -1)


insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement]) 
 values ('CloudNinja.Web.HelloTask',  '\Processor(_Total)\% Processor Time', 'PerfCounter', 'gte', 60, 20, 1)
insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement]) 
 values ('CloudNinja.Web.HelloTask',  '\Processor(_Total)\% Processor Time', 'PerfCounter', 'lte', 40, 20, -1)


insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement]) 
 values ('CloudNinja.Web.HelloTask',  '\ASP.NET\Requests Queued','PerfCounter',  'gte', 10, 20, 1)
insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement]) 
 values ('CloudNinja.Web.HelloTask',  '\ASP.NET\Requests Queued', 'PerfCounter', 'lte', 2, 20, -1)


insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement])  
values ('CloudNinja.Web.HelloTask',  '\ASP.NET\Worker Process Restarts','PerfCounter',  'gte', 10, 20, 1)
insert into AutoScaleKPI ([RoleName],[Metric], [MetricType], [Evaluation], [Threshold], [Duration],[InstanceIncrement])  
values ('CloudNinja.Web.HelloTask',  '\ASP.NET\Worker Process Restarts', 'PerfCounter', 'lte', 2, 20, -1)
