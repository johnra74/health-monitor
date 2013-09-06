package com.jstrgames.monitor.rule;

import java.util.Map;

/**
 * this abstract base class implements the required rule attributes
 * of the Rule interface
 *  
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public abstract class BaseRule implements Rule {
	private Object actual;
	private Object expected;
	private Condition condition;
	
	@Override
	public void setActual(Object actual) {
		this.actual = actual;
	}

	@Override
	public Object getActual() {
		return this.actual;
	}
	
	@Override
	public void setExpected(Object expected) {
		this.expected = expected;
	}

	@Override
	public Object getExpected() {
		return this.expected;
	}

	@Override
	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	@Override
	public Condition getCondition() {
		return this.condition;
	}

	@Override
	public void fromMap(Map<String,Object> map) {
		this.setCondition(Condition.fromString((String) map.get(Rule.RULES_CONDITION)));
		this.setExpected(map.get(Rule.RULES_EXPECTED));	
		
		setExtendedProperties(map);
	}
	
	public abstract void validate() throws FailedRuleException;
	public abstract void setExtendedProperties(Map<String,Object> map);

}
