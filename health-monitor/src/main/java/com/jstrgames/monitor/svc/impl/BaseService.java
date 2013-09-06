package com.jstrgames.monitor.svc.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jstrgames.monitor.cfg.ServiceConfig;
import com.jstrgames.monitor.rule.FailedRuleException;
import com.jstrgames.monitor.rule.Rule;
import com.jstrgames.monitor.svc.Service;

public abstract class BaseService implements Service {
	private String servicename;
	private String classname;
	private String hostname;
	private int port;	
	private String schedule;
	private Status status;
	private Date lastrundate;
	private List<Rule> rules;
	
	@Override
	public String getServiceName() {
		return this.servicename;
	}
	
	@Override
	public void setServiceName(String servicename) {
		this.servicename = servicename;
	}
	
	@Override
	public String getHostname() {
		return this.hostname;
	}

	@Override
	public void setHost(String hostname) {
		this.hostname = hostname;
	}

	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String getClassName() {
		return this.classname;
	}

	@Override
	public void setClassName(String classname) {
		this.classname = classname;
	}

	@Override
	public String getSchedule() {
		return this.schedule;
	}

	@Override
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	
	@Override
	public Status getStatus() {
		return this.status;
	}
	
	@Override
	public void setStatus(Status status) {
		this.status = status;
		this.lastrundate = new Date(System.currentTimeMillis());
	}
	
	@Override
	public Date getLastRunDate() {
		return this.lastrundate;
	}
	
	@Override
	public List<Rule> getRules(){
		return this.rules;
	}
	
	@Override
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	
	@Override
	public void addRule(Rule rule){
		if(this.rules == null) this.rules = new LinkedList<Rule>();
		rules.add(rule);
	}
	
	protected final void validateRules() throws FailedRuleException {
		if(this.rules != null) {
			for(Rule rule: this.rules) {
				rule.validate();
			}
		}
	}
	
	@Override
	public void fromMap(Map<String, Object> map) {
		this.setServiceName(map.get(ServiceConfig.SERVICE_NAME).toString());
		this.setClassName(map.get(ServiceConfig.SERVICE_CLASSNAME).toString());
		this.setHost(map.get(ServiceConfig.SERVICE_HOSTNAME).toString());
		this.setPort(Integer.parseInt(map.get(ServiceConfig.SERVICE_PORT).toString()));
		this.setSchedule(map.get(ServiceConfig.SERVICE_SCHEDULE).toString());
		
		fromExtendedMap(map);
	}
	
	public abstract void fromExtendedMap(Map<String, Object> map);
	public abstract boolean checkService();
}
