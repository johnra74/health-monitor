package com.jstrgames.monitor.rule;

import java.util.Map;


public class SimpleJmxResult extends BaseRule {
	public final static String EXTENDEDMAPKEY_ATTRIBUTE = "attribute";
	
	private String attribute;
	private Map<String,Object> jmxresult;
	
	public void setJmxResult(Map<String,Object> jmxresult) {
		this.jmxresult = jmxresult;
	}
	
	public Map<String,Object> getJmxResult() {
		return this.jmxresult;
	}
	
	@Override
	public void validate() throws FailedRuleException {
		if(this.attribute == null) {
			throw new FailedRuleException("attribute not specified");
		}
		
		final Object jmxActual = jmxresult.get(this.attribute);
		if(jmxActual == null) {
			throw new FailedRuleException("jmx attribute does not exists!");
		}
		
		Object jmxExpected = this.getExpected();
		if(jmxActual instanceof Long && jmxExpected instanceof Integer ) {
			jmxExpected = new Long(((Integer)jmxExpected).longValue());
		} else {
			jmxExpected = this.getExpected();
		}
		switch(this.getCondition()) {
			case EQUALS:				
				if(! jmxActual.equals(jmxExpected)) {
					throw new FailedRuleException("jmx value does not equal expected");
				}
				break;
				
			case CONTAINS:
				if(!(jmxActual instanceof String)) {
					throw new FailedRuleException("jmx value must be a string to perform contain check");
				}
				if(!(jmxExpected instanceof String)) {
					throw new FailedRuleException("expected value must be a string to perform contain check");
				}
				if(! ((String)jmxActual).contains((String)jmxExpected)) {
					throw new FailedRuleException("jmx value does not contain expected");
				}
				
				break;
				
			case GREATERTHAN:
				if(jmxActual instanceof Integer && 
						jmxExpected instanceof Integer) {
					if(((Integer)jmxActual).intValue() <= ((Integer)jmxExpected).intValue()) {
						throw new FailedRuleException("actual is less than expected");
					}
				} else if(jmxActual instanceof Long && 
						jmxExpected instanceof Long) {
					if(((Long)jmxActual).longValue() <= ((Long)jmxExpected).longValue()) {
						throw new FailedRuleException("actual is greater than expected");
					}
				} else if(jmxActual instanceof Double && 
						jmxExpected instanceof Double) {
					if(((Double)jmxActual).intValue() <= ((Double)jmxExpected).intValue()) {
						throw new FailedRuleException("actual is greater than expected");
					}
				} else  {
					throw new FailedRuleException("actual and expected are not of same type");
				}
				
				break;
			
			case LESSTHAN:
				if(jmxActual instanceof Integer && 
						jmxExpected instanceof Integer) {
					if(((Integer)jmxActual).intValue() >= ((Integer)jmxExpected).intValue()) {
						throw new FailedRuleException("actual is greater than expected");
					}
				} else if(jmxActual instanceof Long && 
						jmxExpected instanceof Long) {
					if(((Long)jmxActual).longValue() >= ((Long)jmxExpected).longValue()) {
						throw new FailedRuleException("actual is greater than expected");
					}
				} else if(jmxActual instanceof Double && 
						jmxExpected instanceof Double) {
					if(((Double)jmxActual).doubleValue() >= ((Double)jmxExpected).doubleValue()) {
						throw new FailedRuleException("actual is greater than expected");
					}
				} else  {
					throw new FailedRuleException("actual and expected are not of same type");
				}
				
				break;
			default:
				throw new FailedRuleException("unsupported response condition appeared");
		}

	}

	@Override
	public void setExtendedProperties(Map<String, Object> map) {
		if(map.containsKey(EXTENDEDMAPKEY_ATTRIBUTE)) {
			this.attribute = (String) map.get(EXTENDEDMAPKEY_ATTRIBUTE);
		}
	}
}
