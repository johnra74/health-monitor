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
			<#case "com.jstrgames.monitor.svc.impl.SimpleJmxService">
				JMX
			<#break>
			<#default>
				UNKNOWN
			<#break>
		</#switch>		
		</p>
	</td>
    <td><p>${status.getHostname()}</p></td>
    <td><p>${status.getPort()?string.computer}</p></td>
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