package com.jstrgames.monitor.rule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.jstrgames.monitor.rule.Rule.Condition;

public class TestResponseBody {

	@Test
	public void testEqualsConditionRule() {
		HttpResponseBody rule = new HttpResponseBody();
		
		rule.setActual("this is a mock test");
		rule.setExpected("this is a mock test");
		rule.setCondition(Condition.EQUALS);
		
		try {
			rule.validate();			
		} catch (FailedRuleException e) {
			fail("validation failed");
		}
	}
	
	@Test
	public void testNegativeEqualsConditionRule() {
		HttpResponseBody rule = new HttpResponseBody();
		
		rule.setActual("this is the mock test");
		rule.setExpected("this is a mock test");
		rule.setCondition(Condition.EQUALS);
		
		try {
			rule.validate();
			fail("validation failed");
		} catch (FailedRuleException e) {
			// should fail!
		}
	}
	
	@Test
	public void testContainsConditionRule() {
		HttpResponseBody rule = new HttpResponseBody();
		
		rule.setActual("this mock test should contain 'this is a mock test'");
		rule.setExpected("this is a mock test");
		rule.setCondition(Condition.CONTAINS);
		
		try {
			rule.validate();			
		} catch (FailedRuleException e) {
			fail("validation failed");
		}
	}
	
	@Test
	public void testNegativeContainsConditionRule() {
		HttpResponseBody rule = new HttpResponseBody();
		
		rule.setActual("this mock test should fail as it doesn't contain expected");
		rule.setExpected("this is a mock test");
		rule.setCondition(Condition.CONTAINS);
		
		try {
			rule.validate();
			fail("validation failed");
		} catch (FailedRuleException e) {
			// should fail!
		}
	}

}
