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

<title>Spring-Ehcache-Annotations Weather Service Example - Create Weather Data Success</title>
</head>

<body>

<div id="result">
<p>Weather successfully created:</p>
<ul>
<li>Zip Code: ${weather.zipCode }</li>
<li>Current Temperature: ${weather.currentTemperature }</li>
</ul>
<c:url var="returnUrl" value="main.html"></c:url>
<a href="${returnUrl}">Return to main</a>
</div>

</body>
</html>