package com.jstrgames.monitor.svc.impl;

import java.util.Map;

import com.jstrgames.monitor.cfg.ValidationException;
import com.jstrgames.monitor.svc.impl.BaseService;

/**
 * Test implementation of class used by TestConfigLoader
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 */
public class TestService extends BaseService {
	
	@Override
	public boolean checkService() {
		return true;
	}

	@Override
	public void validate(Map<String, Object> map) throws ValidationException {
		// NOTHING TO VALIDATE. DO NOTHING.		
	}

	@Override
	public void fromExtendedMap(Map<String, Object> map) {
		// NO EXTENDED PROPERTIES. DO NOTHING.	
		
	}

}
