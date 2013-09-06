package com.jstrgames.monitor.svc;

import java.util.Date;

import com.jstrgames.monitor.cfg.ServiceConfig;

/**
 * this interface extends the ServiceConfig and represents status
 * of service 
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public interface Service extends ServiceConfig {
	public static enum Status { PASS, FAIL }
	
	Status getStatus();
	void setStatus(Status status);	
		
	Date getLastRunDate();
	
	boolean checkService();	
}
