package com.jstrgames.monitor.cfg;

/**
 * this class is thrown when mandatory attributes are missing from 
 * each service
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class ValidationException extends Exception {
	private static final long serialVersionUID = 3721849933173637458L;
	
	public ValidationException() {
		super();
	}
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	public ValidationException(String msg, Throwable t) {
		super(msg, t);
	}
	
}
