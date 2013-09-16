package com.jstrgames.monitor;

public class MailServerUnavailableException extends Exception {

	private static final long serialVersionUID = -1608320246135351122L;

	public MailServerUnavailableException() {
		super();
	}
	
	public MailServerUnavailableException(String msg) {
		super(msg);
	}
	
	public MailServerUnavailableException(String msg, Throwable t) {
		super(msg, t);
	}

}
