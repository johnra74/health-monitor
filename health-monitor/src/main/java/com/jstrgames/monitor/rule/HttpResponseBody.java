package com.jstrgames.monitor.rule;

import java.util.Map;

public class HttpResponseBody extends BaseRule {
	
	@Override
	public void validate() throws FailedRuleException {		
		// both actual and expected should be string for response body
		final String actualBody = (String) this.getActual();
		final String expectBody = (String) this.getExpected();
		
		switch(this.getCondition()) {
			case EQUALS:
				if(! actualBody.equals(expectBody)) {
					throw new FailedRuleException("response code does not equal expected");
				}
				break;
				
			case CONTAINS:
				if(! actualBody.contains(expectBody)) {
					throw new FailedRuleException("response code does not contain expected");
				}
				break;
				
			default:
				throw new FailedRuleException("unsupported response condition appeared");
		}

	}
	
	

	@Override
	public void setExtendedProperties(Map<String, Object> map) {
		// DO NOTHING
	}

}
