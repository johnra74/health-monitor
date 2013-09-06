package com.jstrgames.monitor.rule;

import java.util.Map;

/**
 * this rule class is used to validate if the response body meets
 * condition defined
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class HttpResponseBody extends BaseRule {
	
	@Override
	public void validate() throws FailedRuleException {		
		// both actual and expected should be string for response body
		final String actualBody = (String) this.getActual();
		final String expectBody = (String) this.getExpected();
		
		switch(this.getCondition()) {
			case EQUALS:
				if(! actualBody.equals(expectBody)) {
					throw new FailedRuleException("response body does not equal expected");
				}
				break;
				
			case CONTAINS:
				if(! actualBody.contains(expectBody)) {
					throw new FailedRuleException("response body does not contain expected");
				}
				break;
				
			case GREATERTHAN:
				throw new FailedRuleException("invalid condition appeared!");
				
			case LESSTHAN:
				throw new FailedRuleException("invalid condition appeared!");
			
			default:
				throw new FailedRuleException("unsupported response condition appeared");
		}

	}
	
	@Override
	public void setExtendedProperties(Map<String, Object> map) {
		// DO NOTHING
	}

}
