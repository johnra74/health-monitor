package com.jstrgames.monitor.cfg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstrgames.monitor.rule.Rule;
import com.jstrgames.monitor.svc.Service;

/**
 * this class will load from (JSON) configuration file and return a list of 
 * services that needs to be monitored 
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class ConfigLoader {
	private final static Logger LOG = LoggerFactory.getLogger(ConfigLoader.class);
	
	private final static String CFG_KEY_HOSTNAME = "hostname";
	private final static String CFG_KEY_PORT = "port";
	private final static String CFG_KEY_SERVICES_JSON = "services.json";
	private final static String CFG_KEY_TEMPLATE_HOME = "template.home";
	private final static String CFG_KEY_NOTIFICATION = "notification";
	
	private List<Map<String,Object>> cfgList;
	private Map<String,Object> cfgMap;
	private NotificationConfig notifyCfg;
	private String json;	
	
	/**
	 * method to setup services from JSON string
	 * 
	 * @param json
	 * @throws ConfigurationException
	 */
	public ConfigLoader(String json) throws ConfigurationException {
		loadConfiguration(json);
	}
	
	/**
	 * helper method to load Health Configuration
	 * 
	 * @param json
	 * @throws ConfigurationException
	 */
	
	private void loadConfiguration(String json) throws ConfigurationException {
		List<Map<String,Object>> mainCfg = fromJSON(json);
		this.cfgMap = mainCfg.get(0);
		if(this.cfgMap.containsKey(CFG_KEY_SERVICES_JSON)) {
			String fileName = (String) this.cfgMap.get(CFG_KEY_SERVICES_JSON);
			try {
				setServiceJSON(new FileInputStream(fileName));
			} catch (FileNotFoundException e) {
				throw new ConfigurationException("failed to read monitor-cfg.json file");
			}
			this.cfgList = fromJSON(this.json);
		} else {
			throw new ConfigurationException("monitor-cfg.json missing services.json");
		}
		
		if(this.cfgMap.containsKey(CFG_KEY_NOTIFICATION)) {
			@SuppressWarnings("unchecked")
			final Map<String,Object> map = (Map<String,Object>) this.cfgMap.get(CFG_KEY_NOTIFICATION);
			this.notifyCfg = new NotificationConfig(map, this.getTemplateDirectory());			
		}
	}
		
	/**
	 * method to setup services from input stream
	 * 
	 * @param in
	 * @throws ConfigurationException
	 */
	public ConfigLoader(InputStream in) throws ConfigurationException {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(in, writer);
			loadConfiguration(writer.toString());
		} catch (IOException e) {
			LOG.error("failed to load configuration!", e);
			throw new ConfigurationException();
		}
	}	
	
	/**
	 * method returns the notification config
	 * 
	 * @return
	 */
	public NotificationConfig getNotificationConfig() {
		return this.notifyCfg;
	}
		
	/**
	 * method will return a list of services to monitor
	 * 
	 * @return
	 */
	public List<Service> getServices() { 
		List<Service> list = new LinkedList<Service>();
		
		for(Map<String,Object> map : this.cfgList) {
			try {
				validateBase(map);
				String className = map.get(ServiceConfig.SERVICE_CLASSNAME).toString();
				
				@SuppressWarnings("unchecked")
				Class<Service> clazz = (Class<Service>) Class
											.forName(className)
											.asSubclass(Service.class);
				Service svc = clazz.newInstance();
				validateAndLoadFromMap(svc, map);				
				list.add(svc);
			} catch (ValidationException e) {
				LOG.warn("missing required field! skipping service", e);
			} catch (ClassNotFoundException e) {
				LOG.warn("can't find classname! skipping service", e);
			} catch (InstantiationException e) {
				LOG.warn("can't instantiate! skipping service", e);
			} catch (IllegalAccessException e) {
				LOG.warn("can't instantiate! skipping service", e);
			}
		}
		
		return list;		
	}
	
	/**
	 * method will return the host bind for the health monitor website
	 * 
	 * @return
	 */
	public String getHostname() {
		String hostname;
		if(this.cfgMap.containsKey(CFG_KEY_HOSTNAME)) {
			hostname = (String) this.cfgMap.get(CFG_KEY_HOSTNAME);
		} else {
			// bind to only localhost
			hostname = "127.0.0.1";
		}
		return hostname;
	}
	
	/**
	 * method will return the port bind for the health monitor website
	 * 
	 * @return
	 */
	public int getPort() {
		Integer port;
		if(this.cfgMap.containsKey(CFG_KEY_PORT)) {
			port = (Integer) this.cfgMap.get(CFG_KEY_PORT);
		} else {
			// bind to default port
			port = new Integer(8080);
		}
		return port.intValue();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTemplateDirectory() {
		String tplDir;
		if(this.cfgMap.containsKey(CFG_KEY_TEMPLATE_HOME)) {
			tplDir = (String) this.cfgMap.get(CFG_KEY_TEMPLATE_HOME);
		} else {
			// default template location
			tplDir = "./template";
		}
		
		return tplDir;
	}
	
	/**
	 * method will return the service configuration JSON as String
	 * 
	 * @return
	 */
	public String getServiceConfig() {
		return this.json;
	}
	
	/**
	 * method used to validate services configuration and load from map 
	 * 
	 * @param svc
	 * @param map
	 * @throws ValidationException
	 */
	private void validateAndLoadFromMap(Service svc, Map<String,Object> map)
			throws ValidationException {
		svc.validate(map);
		svc.fromMap(map);
		
		if(!map.containsKey(Rule.RULES)) return;
		
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = (List<Map<String,Object>>) map.get(Rule.RULES);
		for(Map<String,Object> ruleMap : list) {
			if(!ruleMap.containsKey(Rule.RULES_CLASSNAME)) continue; 
			String className = ruleMap.get(Rule.RULES_CLASSNAME).toString();
			try {
				@SuppressWarnings("unchecked")
				Class<Rule> clazz = (Class<Rule>) Class
										.forName(className)
										.asSubclass(Rule.class);
				Rule rule = clazz.newInstance();
				rule.fromMap(ruleMap);
				svc.addRule(rule);
				
			} catch (ClassNotFoundException e) {
				LOG.warn("can't find classname! skipping service", e);
			} catch (InstantiationException e) {
				LOG.warn("can't instantiate! skipping service", e);
			} catch (IllegalAccessException e) {
				LOG.warn("can't instantiate! skipping service", e);
			}
			
		}
	}
	
	/**
	 * method used to read services.json from input stream
	 * 
	 * @param in
	 * @throws ConfigurationException
	 */
	private void setServiceJSON(InputStream in) throws ConfigurationException {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(in, writer);
			this.json = writer.toString();
		} catch (IOException e) {
			LOG.error("failed to load configuration!", e);
			throw new ConfigurationException();
		}
	}
	
	/**
	 * helper method will validate that all required attributes has been
	 * set
	 * 
	 * @param map
	 * @throws ValidationException
	 */
	private void validateBase(Map<String,Object> map) throws ValidationException {
		if(!map.containsKey(ServiceConfig.SERVICE_CLASSNAME)){
			throw new ValidationException("Missing classname");
		}
		if(!map.containsKey(ServiceConfig.SERVICE_HOSTNAME)){
			throw new ValidationException("Missing hostname");
		}
		if(!map.containsKey(ServiceConfig.SERVICE_PORT)){
			throw new ValidationException("Missing port");
		}
		if(!map.containsKey(ServiceConfig.SERVICE_SCHEDULE)){
			throw new ValidationException("Missing schedule");
		}	
		if(!map.containsKey(ServiceConfig.SERVICE_NAME)){
			throw new ValidationException("Missing servicename");
		}
	}
	
	/**
	 * helper method will read JSON into array of maps. Each map represents
	 * a service
	 * 
	 * @param json
	 * @return
	 * @throws ConfigurationException
	 */
	private List<Map<String,Object>> fromJSON(String json)
			throws ConfigurationException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, new TypeReference<List<Map<String,Object>>>(){});
		} catch (JsonParseException e) {
			LOG.warn("Failed to parse JSON", e);
			throw new ConfigurationException();
		} catch (JsonMappingException e) {
			LOG.warn("Failed to map JSON", e);
			throw new ConfigurationException();
		} catch (IOException e) {
			LOG.warn("Failed to read JSON", e);
			throw new ConfigurationException();
		}
	}
	
}
