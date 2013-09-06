package com.jstrgames.monitor.tpl;

/**
 * this exception is thrown the specified Template Directory does
 * not exists
 * 
 * @author ra
 *
 */
public class TemplateDirNotFoundException extends Exception {
	private static final long serialVersionUID = 5628195880836386419L;
	
	public TemplateDirNotFoundException() {
		super();
	}
	
	public TemplateDirNotFoundException(String msg) {
		super(msg);
	}

}
