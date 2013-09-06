package com.jstrgames.monitor.svc;

public class ServiceUnavailableException extends Exception {
	private static final long serialVersionUID = 8699849532903466312L;

	public ServiceUnavailableException() {
		super();
	}
	
	public ServiceUnavailableException(String msg) {
		super(msg);
	}
	
	public ServiceUnavailableException(String msg, Throwable t) {
		super(msg, t);
	}
}
