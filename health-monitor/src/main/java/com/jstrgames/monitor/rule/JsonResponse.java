package com.jstrgames.monitor.rule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this rule class is used to validate if the JSON response 
 * meets condition defined
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class JsonResponse extends BaseRule {
	private final static Logger LOG = LoggerFactory.getLogger(JsonResponse.class);
	
	public final static String EXTENDEDMAPKEY_JSONKEY = "jsonkey";
	
	private String jsonKey;
	
	@Override
	public void validate() throws FailedRuleException {
		if(this.jsonKey == null) {
			throw new FailedRuleException("jsonKey not specified");
		}
		
		final Object jsonValue = this.getJsonValue();
		if(jsonValue == null) {
			throw new FailedRuleException(jsonKey + " does not exists in json response");
		}
		
		final Object expectValue = this.getExpected();		
		switch(this.getCondition()) {
			case EQUALS:				
				if(! jsonValue.equals(expectValue)) {
					throw new FailedRuleException("json value does not equal expected");
				}
				break;
				
			case CONTAINS:
				if(!(jsonValue instanceof String)) {
					throw new FailedRuleException("json value must be a string to perform contain check");
				}
				if(!(expectValue instanceof String)) {
					throw new FailedRuleException("expected value must be a string to perform contain check");
				}
				if(! ((String)jsonValue).contains((String)expectValue)) {
					throw new FailedRuleException("json value does not contain expected");
				}
				
				break;
				
			case GREATERTHAN:
				if(jsonValue instanceof Integer && 
				   expectValue instanceof Integer) {
					if(((Integer)jsonValue).intValue() <= ((Integer)expectValue).intValue()) {
						throw new FailedRuleException("actual is less than expected");
					}
				} else if(jsonValue instanceof Double && 
						  expectValue instanceof Double) {
					if(((Double)jsonValue).intValue() <= ((Double)expectValue).intValue()) {
						throw new FailedRuleException("actual is greater than expected");
					}
				} else  {
					throw new FailedRuleException("actual and expected are not of same type");
				}
				
				break;
			
			case LESSTHAN:
				if(jsonValue instanceof Integer && 
				   expectValue instanceof Integer) {
					if(((Integer)jsonValue).intValue() >= ((Integer)expectValue).intValue()) {
						throw new FailedRuleException("actual is greater than expected");
					}
				} else if(jsonValue instanceof Double && 
						  expectValue instanceof Double) {
					if(((Double)jsonValue).intValue() >= ((Double)expectValue).intValue()) {
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
		if(map.containsKey(EXTENDEDMAPKEY_JSONKEY)) {
			this.jsonKey = (String) map.get(EXTENDEDMAPKEY_JSONKEY);
		}
	}
	
	/**
	 * helper method to retrieve value assocaited with json Key
	 * @return
	 */
	private Object getJsonValue() {
		Object actual = null;		
		Map<String,Object> map = this.fromJSON((String)this.getActual());
		if(map.containsKey(this.jsonKey)) {
			actual = map.get(this.jsonKey);
		} 
		return actual;
	}

	/**
	 * helper method will read simple JSON into maps.
	 * 
	 * @param json
	 * @return
	 */
	private Map<String,Object> fromJSON(String json){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, new TypeReference<Map<String,Object>>(){});
		} catch (JsonParseException e) {
			LOG.warn("Failed to parse JSON", e);
		} catch (JsonMappingException e) {
			LOG.warn("Failed to map JSON", e);
		} catch (IOException e) {
			LOG.warn("Failed to read JSON", e);
		}
		
		// return empty map
		return new HashMap<String,Object>();
	}
}
