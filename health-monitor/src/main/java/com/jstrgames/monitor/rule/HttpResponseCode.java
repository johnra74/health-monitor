package com.jstrgames.monitor.rule;

import java.util.Map;

public class HttpResponseCode extends BaseRule {
	
	@Override
	public void validate() throws FailedRuleException {
		// actual should be be Integer
		int actualStatusCode = ((Integer) this.getActual()).intValue();		
		// expected could be String if quoted.. otherwise Integer
		int expectStatusCode;
		if(this.getExpected() instanceof Integer ) {
			expectStatusCode = ((Integer)this.getExpected()).intValue();
		} else {
			expectStatusCode = Integer.parseInt(this.getExpected().toString());
		}
		
		switch(this.getCondition()) {
			case EQUALS:
				if(actualStatusCode != expectStatusCode) {
					throw new FailedRuleException("response code does not equal expected");
				}
				break;
				
			case CONTAINS:
				throw new FailedRuleException("response condition (CONTAINS) is not supported");
				
			default:
				throw new FailedRuleException("unsupported response condition appeared");
		}
	}

	@Override
	public void setExtendedProperties(Map<String, Object> map) {
		// DO NOTHING
	}
	
}
