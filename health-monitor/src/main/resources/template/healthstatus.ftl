<html>
<head>
	<title>Health Monitor</title>
	<!-- This goes in the document HEAD so IE7 and IE8 don't cry -->
	<!--[if lt IE 9]>
	<style type="text/css">
		table.gradienttable th {
			filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#d5e3e4', endColorstr='#b3c8cc',GradientType=0 );
			position: relative;
			z-index: -1;
		}
		table.gradienttable td {
			filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ebecda', endColorstr='#ceceb7',GradientType=0 );
			position: relative;
			z-index: -1;
		}
		table.gradienttable td.PASS {
			filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#9dd53a', endColorstr='#7cbc0a',GradientType=0 ); 
			position: relative;
			z-index: -1;
		}
		table.gradienttable td.FAIL {
			filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f85032', endColorstr='#e73827',GradientType=0 ); 
		}
	</style>
	<![endif]-->

	<!-- CSS goes in the document HEAD or added to your external stylesheet -->
	<style type="text/css">
		a {
			font-family: verdana,arial,sans-serif;
			font-size:11px;
		}
		div.summary {
			font-family: verdana,arial,sans-serif;
			font-size:11px;
			margin:0px;
			padding:8px;			
		}
		table.gradienttable {
			font-family: verdana,arial,sans-serif;
			font-size:11px;
			color:#333333;
			border-width: 1px;
			border-color: #999999;
			border-collapse: collapse;
		}
		table.gradienttable th {
			padding: 0px;
			background: #d5e3e4;
			background: url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/Pgo8c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgd2lkdGg9IjEwMCUiIGhlaWdodD0iMTAwJSIgdmlld0JveD0iMCAwIDEgMSIgcHJlc2VydmVBc3BlY3RSYXRpbz0ibm9uZSI+CiAgPGxpbmVhckdyYWRpZW50IGlkPSJncmFkLXVjZ2ctZ2VuZXJhdGVkIiBncmFkaWVudFVuaXRzPSJ1c2VyU3BhY2VPblVzZSIgeDE9IjAlIiB5MT0iMCUiIHgyPSIwJSIgeTI9IjEwMCUiPgogICAgPHN0b3Agb2Zmc2V0PSIwJSIgc3RvcC1jb2xvcj0iI2Q1ZTNlNCIgc3RvcC1vcGFjaXR5PSIxIi8+CiAgICA8c3RvcCBvZmZzZXQ9IjQwJSIgc3RvcC1jb2xvcj0iI2NjZGVlMCIgc3RvcC1vcGFjaXR5PSIxIi8+CiAgICA8c3RvcCBvZmZzZXQ9IjEwMCUiIHN0b3AtY29sb3I9IiNiM2M4Y2MiIHN0b3Atb3BhY2l0eT0iMSIvPgogIDwvbGluZWFyR3JhZGllbnQ+CiAgPHJlY3QgeD0iMCIgeT0iMCIgd2lkdGg9IjEiIGhlaWdodD0iMSIgZmlsbD0idXJsKCNncmFkLXVjZ2ctZ2VuZXJhdGVkKSIgLz4KPC9zdmc+);
			background: -moz-linear-gradient(top,  #d5e3e4 0%, #ccdee0 40%, #b3c8cc 100%);
			background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#d5e3e4), color-stop(40%,#ccdee0), color-stop(100%,#b3c8cc));
			background: -webkit-linear-gradient(top,  #d5e3e4 0%,#ccdee0 40%,#b3c8cc 100%);
			background: -o-linear-gradient(top,  #d5e3e4 0%,#ccdee0 40%,#b3c8cc 100%);
			background: -ms-linear-gradient(top,  #d5e3e4 0%,#ccdee0 40%,#b3c8cc 100%);
			background: linear-gradient(to bottom,  #d5e3e4 0%,#ccdee0 40%,#b3c8cc 100%);
			border: 1px solid #999999;
		}
		table.gradienttable td {
			padding: 0px;
			background: #ebecda;
			background: url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/Pgo8c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgd2lkdGg9IjEwMCUiIGhlaWdodD0iMTAwJSIgdmlld0JveD0iMCAwIDEgMSIgcHJlc2VydmVBc3BlY3RSYXRpbz0ibm9uZSI+CiAgPGxpbmVhckdyYWRpZW50IGlkPSJncmFkLXVjZ2ctZ2VuZXJhdGVkIiBncmFkaWVudFVuaXRzPSJ1c2VyU3BhY2VPblVzZSIgeDE9IjAlIiB5MT0iMCUiIHgyPSIwJSIgeTI9IjEwMCUiPgogICAgPHN0b3Agb2Zmc2V0PSIwJSIgc3RvcC1jb2xvcj0iI2ViZWNkYSIgc3RvcC1vcGFjaXR5PSIxIi8+CiAgICA8c3RvcCBvZmZzZXQ9IjQwJSIgc3RvcC1jb2xvcj0iI2UwZTBjNiIgc3RvcC1vcGFjaXR5PSIxIi8+CiAgICA8c3RvcCBvZmZzZXQ9IjEwMCUiIHN0b3AtY29sb3I9IiNjZWNlYjciIHN0b3Atb3BhY2l0eT0iMSIvPgogIDwvbGluZWFyR3JhZGllbnQ+CiAgPHJlY3QgeD0iMCIgeT0iMCIgd2lkdGg9IjEiIGhlaWdodD0iMSIgZmlsbD0idXJsKCNncmFkLXVjZ2ctZ2VuZXJhdGVkKSIgLz4KPC9zdmc+);
			background: -moz-linear-gradient(top,  #ebecda 0%, #e0e0c6 40%, #ceceb7 100%);
			background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#ebecda), color-stop(40%,#e0e0c6), color-stop(100%,#ceceb7));
			background: -webkit-linear-gradient(top,  #ebecda 0%,#e0e0c6 40%,#ceceb7 100%);
			background: -o-linear-gradient(top,  #ebecda 0%,#e0e0c6 40%,#ceceb7 100%);
			background: -ms-linear-gradient(top,  #ebecda 0%,#e0e0c6 40%,#ceceb7 100%);
			background: linear-gradient(to bottom,  #ebecda 0%,#e0e0c6 40%,#ceceb7 100%);
			border: 1px solid #999999;
		}		
		table.gradienttable td.PASS {
			padding: 0px;
			background: #9dd53a; 
			background: -moz-linear-gradient(top,  #9dd53a 0%, #a1d54f 50%, #80c217 51%, #7cbc0a 100%); 
			background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#9dd53a), color-stop(50%,#a1d54f), color-stop(51%,#80c217), color-stop(100%,#7cbc0a));
			background: -webkit-linear-gradient(top,  #9dd53a 0%,#a1d54f 50%,#80c217 51%,#7cbc0a 100%); 
			background: -o-linear-gradient(top,  #9dd53a 0%,#a1d54f 50%,#80c217 51%,#7cbc0a 100%); 
			background: -ms-linear-gradient(top,  #9dd53a 0%,#a1d54f 50%,#80c217 51%,#7cbc0a 100%);
			background: linear-gradient(to bottom,  #9dd53a 0%,#a1d54f 50%,#80c217 51%,#7cbc0a 100%);
			border: 1px solid #999999;
		}
		table.gradienttable td.FAIL {
			padding: 0px;
			background: #f85032; 
			background: -moz-linear-gradient(top,  #f85032 0%, #f16f5c 50%, #f6290c 51%, #f02f17 71%, #e73827 100%); 
			background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#f85032), color-stop(50%,#f16f5c), color-stop(51%,#f6290c), color-stop(71%,#f02f17), color-stop(100%,#e73827)); 
			background: -webkit-linear-gradient(top,  #f85032 0%,#f16f5c 50%,#f6290c 51%,#f02f17 71%,#e73827 100%); 
			background: -o-linear-gradient(top,  #f85032 0%,#f16f5c 50%,#f6290c 51%,#f02f17 71%,#e73827 100%); 
			background: -ms-linear-gradient(top,  #f85032 0%,#f16f5c 50%,#f6290c 51%,#f02f17 71%,#e73827 100%); 
			background: linear-gradient(to bottom,  #f85032 0%,#f16f5c 50%,#f6290c 51%,#f02f17 71%,#e73827 100%); 
			border: 1px solid #999999;
		}
		table.gradienttable th p{
			margin:0px;
			padding:8px;
			border-top: 1px solid #eefafc;
			border-bottom:0px;
			border-left: 1px solid #eefafc;
			border-right:0px;
		}
		table.gradienttable td p{
			margin:0px;
			padding:8px;
			border-top: 1px solid #fcfdec;
			border-bottom:0px;
			border-left: 1px solid #fcfdec;;
			border-right:0px;
		}
	<!-- TIP: Generate your own CSS Gradients using this tool: http://www.colorzilla.com/gradient-editor/ -->
	</style>
</had>
<body>
<div class="summary"><strong>Service Summary:</strong> ${statusSummary} service(s) has passed</div> 
<table class="gradienttable">
<tr>
	<th><p>Service</p></th>
	<th><p>Type</p></th>
	<th><p>Host</p></th>
	<th><p>Port</p></th>
	<th><p>Status</p></th>
	<th><p>Last Run</p></th>
	<th><p><a href="http://quartz-scheduler.org/documentation/quartz-2.x/tutorials/crontrigger" target="_new">Schedule</a></p></th>
</tr>
<#list statusList as status>
<tr>
	<td><p>${status.getServiceName()}</p></td>
	<td>
		<p>
		<#switch status.getClassName()>
			<#case "com.jstrgames.monitor.svc.impl.HttpService">
				HTTP
			<#break>
			<#case "com.jstrgames.monitor.svc.impl.HttpsService">
				HTTPS
			<#break>
			<#case "com.jstrgames.monitor.svc.impl.SocketService">
				Socket
			<#break>
			<#default>
				UNKNOWN
			<#break>
		</#switch>		
		</p>
	</td>
    <td><p>${status.getHostname()}</p></td>
    <td><p>${status.getPort()}</p></td>
    <td <#if status.getStatus()??>class="${status.getStatus().toString()}"</#if>>
    	<p><#if status.getStatus()??>
    		${status.getStatus().toString()}
    		<#else>
    		N/A
    		</#if>
    	</p>
    </td>  
    <td>
    	<p><#if status.getLastRunDate()??>
    		${status.getLastRunDate()?datetime?string.short}
    		<#else>
    		N/A
    		</#if>
    	</p>    	
    </td>
    <td><p>${status.getSchedule()}</p></td>
</tr>
</#list>
</table>
<br />
<a href="/service.json">view configuration</a>
</body>
</html>