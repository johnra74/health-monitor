package com.jstrgames.monitor.svc;

import java.util.Date;

import com.jstrgames.monitor.cfg.ServiceConfig;

public interface Service extends ServiceConfig {
	public static enum Status { PASS, FAIL }
	
	Status getStatus();
	void setStatus(Status status);	
		
	Date getLastRunDate();
	
	boolean checkService();	
}
