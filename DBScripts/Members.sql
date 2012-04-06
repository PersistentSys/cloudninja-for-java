CREATE TABLE [dbo].[Member]
(
	[TenantId] nvarchar(20) NOT NULL,
	[MemberId] nvarchar(150) NOT NULL,
	[Name] nvarchar(30) NULL,
	[Password] nvarchar(50) NULL,
    [Role] nvarchar(30) NOT NULL Default('User'),
	[Created] DateTime NOT NULL Default(GETDATE()),
	[IsEnabled] bit NOT NULL Default(1),
	[LiveInvitationCode] nvarchar(10) NULL,
    [LiveGUID] nvarchar(100) NULL
	CONSTRAINT [PK_Member_TenantIdMemberId] PRIMARY KEY CLUSTERED 
	(
		[TenantId] ASC,
		[MemberId] ASC
	)
)