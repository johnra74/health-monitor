<html>
<head>
	<title>Health Monitor</title>
	<#include "style.ftl">
</head>
<body>
<div class="summary"><strong>Service Summary:</strong> ${statusSummary} service(s) has passed</div> 
<#include "table.ftl">
<br />
<a href="/service.json">view configuration</a>
</body>
</html>