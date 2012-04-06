
CREATE TABLE [KpiType]
(
      [KpiTypeId] int IDENTITY (1,1)  PRIMARY KEY CLUSTERED,
      [Name] nvarchar(128) NOT NULL
)

CREATE TABLE [Role]
(
      [RoleId] int IDENTITY (1,1) PRIMARY KEY CLUSTERED,
      [Name] nvarchar(128) NOT NULL
)

CREATE TABLE [KpiValue]
(		
      [RoleId] int NOT NULL,
      [Instance] int NOT NULL,
      [TimeStamp] datetime NOT NULL,
      [KpiTypeId] int NOT NULL,
      [Value] bigint NOT NULL Default(0),
      CONSTRAINT [PrimaryKey_KpiValue] PRIMARY KEY CLUSTERED 
      (
            [RoleId] ASC,
            [Instance] ASC,
            [TimeStamp] ASC,
			[KpiTypeId] ASC
      ),
      
      CONSTRAINT [FK_KpiValue_KpiTypeId_KpiType_KpiTypeId]
      FOREIGN KEY (KpiTypeId) REFERENCES KpiType(KpiTypeId),
      
      CONSTRAINT [FK_KpiValue_RoleId_Role_RoleId]
      FOREIGN KEY (RoleId) REFERENCES Role(RoleId)
)


CREATE TABLE [KpiValueTemp]
(		
      [RoleId] nvarchar(128) NOT NULL,
      [Instance] nvarchar(128) NOT NULL,
	  [InstanceNo] int NOT NULL,
      [TimeStamp] datetime NOT NULL,
      [KpiTypeId] nvarchar(128) NOT NULL,
      [Value] bigint NOT NULL Default(0)
)
CREATE CLUSTERED INDEX [IdxC_KpiValueTemp] ON [KpiValueTemp](RoleId, Instance,InstanceNo,[TimeStamp],KpiTypeId)

CREATE TABLE [UserActivity]
(
	  [TimeInterval] DateTime NOT NULL,
	  [TenantId] nvarchar(20) NOT NULL,
      [MemberId] nvarchar(150) NOT NULL,
	  [Requests] int NOT NULL
)

CREATE CLUSTERED INDEX [IdxC_UserActivity_TimeInterval] ON [UserActivity](TimeInterval)

CREATE TYPE ActiveUserType AS TABLE 
(
	RequestTime datetime,
	TenantId nvarchar(20),
	MemberId nvarchar(150),
	Requests int
)

go

CREATE PROCEDURE ProcessKpiValues AS
	
BEGIN

	SET NOCOUNT ON
		
	-- Add any KpiType that may not be in the measure
	INSERT INTO [KpiType] ([Name])
		SELECT DISTINCT [KpiValueTemp].[KpiTypeId] FROM [KpiValueTemp] 
					WHERE [KpiValueTemp].[KpiTypeId] NOT IN (SELECT [KpiType].[Name] FROM [KpiType])
	

	-- Add any Role that may not be in the measure
	INSERT INTO [Role] ([Name])
		SELECT DISTINCT [KpiValueTemp].[RoleId] FROM [KpiValueTemp] 
					WHERE [KpiValueTemp].[RoleId] NOT IN (SELECT [Role].[Name] FROM [Role])
					
DECLARE @latestRecord datetime
SELECT @latestRecord = DATEADD(mi, DATEDIFF(mi, 0, MAX([TimeStamp])) - 2, 0) FROM [KpiValueTemp]
			
					
	INSERT INTO [KpiValue] ([RoleId],[Instance], [TimeStamp], [KpiTypeId], [Value])	
SELECT [Role].[RoleId], 
		 [KpiValueTemp] .[InstanceNo], 
		convert(datetime, 				
				cast(DATEPART(YY, [KpiValueTemp].[TimeStamp]) as nvarchar(4)) + '-' +
				CASE WHEN LEN(cast(DATEPART(MM, [KpiValueTemp].[TimeStamp]) as nvarchar(2))) = 1 THEN '0'
				ELSE '' END + cast(DATEPART(MM, [KpiValueTemp].[TimeStamp]) as nvarchar(2)) + '-' +
				CASE WHEN LEN(cast(DATEPART(DD, [KpiValueTemp].[TimeStamp]) as nvarchar(2))) = 1 THEN '0'
				ELSE '' END + cast(DATEPART(DD, [KpiValueTemp].[TimeStamp]) as nvarchar(2)) + ' ' +					
				CASE WHEN LEN(cast(DATEPART(hh, [KpiValueTemp].[TimeStamp]) as nvarchar(2))) = 1 THEN '0'
				ELSE '' END + cast(DATEPART(hh, [KpiValueTemp].[TimeStamp]) as nvarchar(2)) + ':' +			
				CASE WHEN LEN(cast(DATEPART(mi, [KpiValueTemp].[TimeStamp]) as nvarchar(2))) = 1 THEN '0'
				ELSE '' END + cast(DATEPART(mi, [KpiValueTemp].[TimeStamp]) as nvarchar(2))
				 , 120) AS [NewTimeStamp], 	
				[KpiType].[KpiTypeId],									
				avg([KpiValueTemp].[Value]) AS [Value] 
				FROM [KpiValueTemp] 
		JOIN [Role] ON [KpiValueTemp] .[RoleId] = [Role].[Name] 
		JOIN [KpiType] ON [KpiValueTemp].[KpiTypeId] = [KpiType].[Name]	
			-- Don't get the minute boundry from this batch into the aggregate, wait for the next batch
		WHERE [KpiValueTemp].[TimeStamp] < @latestRecord
			
		GROUP BY [Role].[RoleId], 
				[KpiValueTemp] .[InstanceNo], 
				DATEPART(mi, [KpiValueTemp].[TimeStamp]), 
				DATEPART(hh, [KpiValueTemp].[TimeStamp]), 
				DATEPART(DD, [KpiValueTemp].[TimeStamp]), 
				DATEPART(MM, [KpiValueTemp].[TimeStamp]), 
				DATEPART(YY, [KpiValueTemp].[TimeStamp]), 
				[KpiType].[KpiTypeId]


					
	DELETE FROM [KpiValueTemp] WHERE [KpiValueTemp].[TimeStamp] < @latestRecord
	
	if datepart(hh, getutcdate()) = 0
	BEGIN
		DELETE FROM [KpiValue] WHERE [KpiValue].[TimeStamp] < dateadd(mm, -3, getutcdate())
	END

END