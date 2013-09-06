package com.jstrgames.monitor.cfg;

import java.util.List;
import java.util.Map;

import com.jstrgames.monitor.cfg.ValidationException;
import com.jstrgames.monitor.rule.Rule;

/**
 * this interface representing the default service configurations 
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public interface ServiceConfig {	
	public final static String SERVICE_CLASSNAME = "classname";
	public final static String SERVICE_HOSTNAME = "hostname";
	public final static String SERVICE_PORT = "port";
	public final static String SERVICE_SCHEDULE = "schedule";
	public final static String SERVICE_NAME = "servicename";
	
	String getServiceName();
	void setServiceName(String serviceName);
	
	String getHostname();
	void setHost(String hostname);
	
	int getPort();
	void setPort(int port);
	
	String getClassName();
	void setClassName(String className);
	
	String getSchedule();
	void setSchedule(String schedule);
	
	List<Rule> getRules();
	void setRules(List<Rule> rules);
	void addRule(Rule rules);
	
	void fromMap(Map<String,Object> map);
	void validate(Map<String, Object> map) throws ValidationException;
}
