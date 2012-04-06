<%--
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
--%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>
${callback}(<json:object>
  <json:property name="success" value="true"></json:property>
  <json:array name="series" var="chartModel" items="${meteringTotalsChartModelList}">
    <json:object>
      <json:property name="kpi" value="${chartModel.kpi}"/>
      <json:array name="xSeries" items="${chartModel.xSeries}"/>
      <json:array name="ySeries" items="${chartModel.ySeries}"/>
    </json:object>
  </json:array>
</json:object>);
