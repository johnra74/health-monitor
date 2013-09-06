package com.jstrgames.monitor.svc;

import java.util.Date;

import com.jstrgames.monitor.cfg.ServiceConfig;
import com.jstrgames.monitor.rule.FailedRuleException;

/**
 * this interface extends the ServiceConfig and represents status
 * of service 
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public interface Service extends ServiceConfig {
	public static enum Status { PASS, FAIL, ERROR }
	
	Status getStatus();
	void setStatus(Status status);	
		
	Date getLastRunDate();
	
	void connectToService() throws ServiceUnavailableException;
	boolean isValidService() throws FailedRuleException;
}
