package com.jstrgames.monitor.svc.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jstrgames.monitor.cfg.ValidationException;
import com.jstrgames.monitor.rule.Rule;
import com.jstrgames.monitor.svc.Service;
import com.jstrgames.monitor.svc.ServiceUnavailableException;

/**
 * Test implementation of class used by TestConfigLoader
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 */
public class TestService implements Service {
	private final boolean isValid;
	private Status status;
	
	public TestService(boolean isValid) {
		super();
		this.isValid = isValid;
	}
	
	@Override
	public final boolean isValidService() {
		return this.isValid;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Override
	public void connectToService() throws ServiceUnavailableException {
		// NOTHING TO CONNECT TO. DO NOTHING
	}

	@Override
	public String getServiceName() {
		// DO NOTHING
		return null;
	}

	@Override
	public void setServiceName(String serviceName) {
		// DO NOTHING		
	}

	@Override
	public String getHostname() {
		// DO NOTHING
		return null;
	}

	@Override
	public void setHost(String hostname) {
		// DO NOTHING		
	}

	@Override
	public int getPort() {
		// DO NOTHING
		return 0;
	}

	@Override
	public void setPort(int port) {
		// DO NOTHING
	}

	@Override
	public String getClassName() {
		// DO NOTHING
		return null;
	}

	@Override
	public void setClassName(String className) {
		// DO NOTHING
	}

	@Override
	public String getSchedule() {
		// DO NOTHING
		return null;
	}

	@Override
	public void setSchedule(String schedule) {
		// DO NOTHING
	}

	@Override
	public List<Rule> getRules() {
		// DO NOTHING
		return null;
	}

	@Override
	public void setRules(List<Rule> rules) {
		// DO NOTHING
		
	}

	@Override
	public void addRule(Rule rules) {
		// DO NOTHING		
	}

	@Override
	public void fromMap(Map<String, Object> map) {
		// DO NOTHING
	}

	@Override
	public Date getLastRunDate() {
		// DO NOTHING
		return null;
	}

	@Override
	public void validate(Map<String, Object> map) throws ValidationException {
		// DO NOTHING
	}

}
