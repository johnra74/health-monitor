package com.jstrgames.monitor.cfg;

/**
 * this class is thrown when Configuration cannot be loaded
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class ConfigurationException extends Exception {
	private static final long serialVersionUID = 5969636277069403580L;
	
	public ConfigurationException() {
		super();
	}
	
	public ConfigurationException(String msg) {
		super(msg);
	}
	
	public ConfigurationException(String msg, Throwable t) {
		super(msg, t);
	}
}
