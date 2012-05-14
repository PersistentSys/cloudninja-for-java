Introduction:

Windows Azure Diagnostics enables you to collect diagnostic data from an application running in Windows Azure.
You can use diagnostic data for debugging, troubleshooting, measuring performance, monitoring resource usage, traffic analysis, capacity planning, and auditing.

Dependencies:

The Azure diagnostics tool has dependency on following DLLs : 

1) Microsoft.WindowsAzure.Diagnostics.dll
2) Microsoft.WindowsAzure.StorageClient.dll

These DLLs can be obtained after installing Windows Azure SDK for .NET from location below
http://www.microsoft.com/download/en/details.aspx?displaylang=en&id=28045.

For 32-bit machine installation use WindowsAzureSDK-x86.msi and for 64-bit machine use WindowsAzureSDK-x64.msi

After installation of SDK, navigate to installation directory, Windows Azure SDK folder -> folder with name as SDK version number -> ref (e.g. C:\Program Files\Windows Azure SDK\v1.6\ref). 
Here you will find the two DLLs
* Microsoft.WindowsAzure.Diagnostics.dll 
* Microsoft.WindowsAzure.StorageClient.dll

You need to copy the two DLLs to Windows Azure project's approot folder.