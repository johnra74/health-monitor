package com.jstrgames.monitor.rule;

public class FailedRuleException extends Exception {
	private static final long serialVersionUID = 606514160362842659L;
	
	public FailedRuleException() {
		super();
	}
	
	public FailedRuleException(String msg) {
		super(msg);
	}

}
