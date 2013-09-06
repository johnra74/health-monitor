package com.jstrgames.monitor.rule;

/**
 * this exception is thrown when services fails the defined rule 
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class FailedRuleException extends Exception {
	private static final long serialVersionUID = 606514160362842659L;
	
	public FailedRuleException() {
		super();
	}
	
	public FailedRuleException(String msg) {
		super(msg);
	}

}
