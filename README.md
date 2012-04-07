### CloudNinja for Java
Persistent Systems has developed multi-tenant sample application (called as CloudNinja) in Java for the Windows Azure 
platform which would help the Java community to develop Java applications for Windows Azure. The CloudNinja for Java 
application will help Java developers to implement best practices and design patterns that are required for Java 
multi-tenant applications on Windows Azure.
 
The CloudNinja for Java application encompasses the following points:
 Tenant on boarding
 Tenant level customization (for example, managing logos)
 Data isolation for Tenant
 Metering for Tenant
 Providing support for log-in via different identity providers (for example, Yahoo!, Google, Windows Live ID)
 
This application also demonstrates how to use the various Windows Azure Services such as:
 ACS
 Windows Azure Storage
 SQL Azure database and so on.

### License Information
 Copyright 2012 Persistent Systems Ltd.
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
   http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.


###  Master branch
  The purpose of this branch is to access the Dashboard application by having the tenantID in the application URL. i.e. no need to register the domain name for the deployment(cloudninja.cloudapp.net).
  e.g. http://cloudninja.cloudapp.net/TenantDashBoard/{tenantId}

### Project Description

The ClounNinja for Java Project is a Windows Azure multi-tenant sample application demonstrating common multi-tenant features (provisioning, monitoring, metering, customization and third party identity providers). A multi-tenant application serves multiple clients while providing a virtual space for isolating each client (tenant). The isolation is achieved by preventing the tenants from seeing each other�s data.

### Key Features
CloudNinja sample will demonstrate how to design a multi-tenant application for managing tasks by leveraging windows azure platform and provide the following features:

1. Tenant Provisioning
  *       Register new tenants.
  *       Remove an existing tenant.
2. Monitoring
                *       Collect key performance indicators to report the performance and health of the application.
3. Customization
                *        Allow tenants to include branding information.
                *        Allow each tenant to manage its list of users.
4. Metering Charts
5. Support for third party identity providers including Windows Live ID, Google, Yahoo! and CustomSTS.
     
The sample will use a control database for registering tenants, members, metering and monitoring. Each tenant will be provided with a dedicated database at the time of provisioning.

### Deployment and Development notes
   Deployment and development instruction for master branch are summarized at [document](https://github.com/PersistentSys/cloudninja-for-java/blob/master/Documents/Deployment%20&%20Development%20Environment%20document.docx)
