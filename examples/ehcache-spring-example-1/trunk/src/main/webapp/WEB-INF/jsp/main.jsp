<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%-- 
  Copyright 2011 Nicholas Blair, Eric Dalquist.

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
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Spring-Ehcache-Annotations Weather Service Example - Main</title>
</head>

<body>

<div id="controls">
<c:url var="createUrl" value="weather.html">
<c:param name="action" value="create"/>
</c:url>
<form action="${createUrl }" method="post">
<label for="zipCode">Zip Code:&nbsp;</label>
<input type="text" name="zipCode"/>
<br/>
<label for="currentTemperature">Current Temperature:&nbsp;</label>
<input type="text" name="currentTemperature"/>
<br/>
<input type="submit" value="Submit"/>
</form>
</div>

<hr/>

<div id="currentData">
<c:choose>
<c:when test="${empty knownWeather}">
<p>No weather data available.</p>
</c:when>
<c:otherwise>
<table border="1" style="border-collapse:collapse;">
<thead>
<tr>
<th>Zip Code</th>
<th>Details</th>
<th>Update Temperature?</th>
<th>Delete Record?</th>
</tr>
</thead>
<tbody>
<c:forEach items="${knownWeather}" var="weather">
<tr>
<td>${weather.zipCode}</td>

<c:url var="detailUrl" value="weather.html">
<c:param name="zipCode" value="${weather.zipCode}"></c:param>
</c:url>
<td><a href="${detailUrl}">${weather.zipCode} detail</a></td>

<c:url var="updateUrl" value="weather.html">
<c:param name="action" value="update"/>
<c:param name="zipCode" value="${weather.zipCode}"></c:param>
</c:url>
<td><form action="${updateUrl }" method="post"><input type="text" name="newTemperature"/><input type="submit" value="Update"/></form></td>

<c:url var="deleteUrl" value="weather.html">
<c:param name="action" value="delete"/>
<c:param name="zipCode" value="${weather.zipCode}"></c:param>
</c:url>
<td><form action="${deleteUrl }" method="post"><input type="submit" value="Delete"/></form></td>
</tr>
</c:forEach>
</tbody>
</table>
</c:otherwise>
</c:choose>
</div>
<hr/>
<div id="cacheInformation">
<h3>weatherCache Statistics</h3>
<ul>
<li>Memory store object count: ${statistics.memoryStoreObjectCount}</li>
<li>Hits: ${statistics.cacheHits}</li>
<li>Misses: ${statistics.cacheMisses}</li>
</ul>
</div>
</body>
</html>