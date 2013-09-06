package com.jstrgames.monitor.rule;

import java.util.Map;

public interface Rule {	
	public final static String RULES = "rules";
	public final static String RULES_CLASSNAME = "classname";
	public final static String RULES_CONDITION = "condition";
	public final static String RULES_EXPECTED = "expected";	
	
	public static final String EXPECTED = "expected";
	public static final String CONDITION = "condition";
		
	public static enum Condition {
		EQUALS("equals"), 
		CONTAINS("contains"), 
		GREATERTHAN("greaterthan"), 
		LESSTHAN("lessthan");
		
		private String condition;
		
		Condition(String text) {
			this.condition = text;
		}
		
		public String toString() {
			return this.condition;
		}
		
		public static Condition fromString(String text) {
			if(text != null) {
				for (Condition cond : Condition.values()) {
					if (text.equalsIgnoreCase(cond.condition)) {
						return cond;
			        }
				}
			}
			
			return null;
		}
	}
	
	void setActual(Object actual);
	Object getActual();
	
	void setExpected(Object expected);
	Object getExpected();
	
	void setCondition(Condition condition);
	Condition getCondition();
	
	void validate() throws FailedRuleException;
	void fromMap(Map<String,Object> map);
}
