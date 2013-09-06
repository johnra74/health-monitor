package com.jstrgames.monitor.rule;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jstrgames.monitor.rule.Rule.Condition;

public class TestResponseCode {

	@Test
	public void testEqualsConditionRule() {
		HttpResponseCode rule = new HttpResponseCode();
		
		rule.setActual(new Integer(200));
		rule.setExpected(new Integer(200));
		rule.setCondition(Condition.EQUALS);
		
		try {
			rule.validate();			
		} catch (FailedRuleException e) {
			fail("validation failed");
		}
	}
	
	@Test
	public void testNegativeEqualsConditionRule() {
		HttpResponseCode rule = new HttpResponseCode();
		
		rule.setActual(new Integer(302));
		rule.setExpected(new Integer(200));
		rule.setCondition(Condition.EQUALS);
		
		try {
			rule.validate();
			fail("validation failed");
		} catch (FailedRuleException e) {
			// should fail!
		}
	}
	
	@Test
	public void testNegativeContainsConditionRule() {
		
		HttpResponseCode rule = new HttpResponseCode();
		
		rule.setActual(new Integer(200));
		rule.setExpected(new Integer(200));
		rule.setCondition(Condition.CONTAINS);
		
		try {
			rule.validate();	
			fail("validation failed");
		} catch (FailedRuleException e) {
			// contains is not supported.. should always fail
		}
	}
	
}
