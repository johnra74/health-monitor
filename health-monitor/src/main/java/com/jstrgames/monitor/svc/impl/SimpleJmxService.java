package com.jstrgames.monitor.svc.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstrgames.monitor.cfg.ValidationException;
import com.jstrgames.monitor.rule.Rule;
import com.jstrgames.monitor.rule.SimpleJmxResult;
import com.jstrgames.monitor.svc.ServiceUnavailableException;

/**
 * this class will validate the MBeans of Simple JMX query based on
 * rules defined. Attributes and their associated value of these MBeans 
 * will stored in a simple 1-level key/value Map to be validated. 
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class SimpleJmxService extends BaseService {
	private final static Logger LOG = LoggerFactory.getLogger(SimpleJmxService.class);
			
	public final static String EXTENDEDMAPKEY_JMXQUERY = "jmxquery";
	public final static String EXTENDEDMAPKEY_USERNAME = "username";
	public final static String EXTENDEDMAPKEY_PASSWORD = "password";
	
	private String username;
	private String password;
	private String jmxquery;
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setJmxQuery(String jmxquery) {
		this.jmxquery = jmxquery;
	}
	
	public String getJmxQuery() {
		return this.jmxquery;
	}
	
	@Override
	public void validate(Map<String, Object> map) throws ValidationException {
		if(!map.containsKey(EXTENDEDMAPKEY_JMXQUERY)){
			throw new ValidationException("Missing jmxquery");
		}
		
		if(!map.containsKey(EXTENDEDMAPKEY_USERNAME)){
			throw new ValidationException("Missing username");
		}
		
		if(!map.containsKey(EXTENDEDMAPKEY_PASSWORD)){
			throw new ValidationException("Missing password");
		}

	}

	@Override
	public void fromExtendedMap(Map<String, Object> map) {
		if(map.containsKey(EXTENDEDMAPKEY_JMXQUERY)) {
			this.jmxquery = (String) map.get(EXTENDEDMAPKEY_JMXQUERY);
		}
		
		if(map.containsKey(EXTENDEDMAPKEY_USERNAME)) {
			this.username = (String) map.get(EXTENDEDMAPKEY_USERNAME);
		}
		
		if(map.containsKey(EXTENDEDMAPKEY_PASSWORD)) {
			this.password = (String) map.get(EXTENDEDMAPKEY_PASSWORD);
		}
	}

	@Override
	public void connectToService() throws ServiceUnavailableException {
		final String jmxURL = getServiceURL();
		final Map<String,String[]> credEnv = getCredentialEnvironment();
		
		try {
			JMXServiceURL url = new JMXServiceURL(jmxURL);			
			JMXConnector jmxConn = JMXConnectorFactory.connect(url, credEnv);
			MBeanServerConnection mbConn = jmxConn.getMBeanServerConnection();
			
			ObjectName queryName = new ObjectName(this.getJmxQuery());
			Set<ObjectInstance> result = mbConn.queryMBeans(queryName, null);
			Map<String,Object> mbeanMap = new HashMap<String,Object>();
			for(ObjectInstance instance : result) {
				MBeanInfo info = mbConn.getMBeanInfo( instance.getObjectName() );
				for(MBeanAttributeInfo attr : info.getAttributes()) {
					Object value = mbConn.getAttribute(instance.getObjectName(), attr.getName());
					mbeanMap.put(
							String.format("%s:%s", instance.getObjectName(), attr.getName()),
							value);
				}
			}
			addResponseToRules(mbeanMap);			
		} catch (MalformedURLException e) {
			LOG.error("malformed jmx url: " + jmxURL, e);
			throw new ServiceUnavailableException("malformed jmx url: " + jmxURL, e);
		} catch (IOException e) {
			LOG.error("failed to connect to remote jmx", e);
			throw new ServiceUnavailableException("failed to connect to remote jmx", e);
		} catch (MalformedObjectNameException e) {
			LOG.error("failed to parse jmx object", e);
			throw new ServiceUnavailableException("failed to parse jmx object", e);
		} catch (InstanceNotFoundException e) {
			LOG.error("failed to retrieve jmx object", e);
			throw new ServiceUnavailableException("failed to retrieve jmx object", e);
		} catch (IntrospectionException e) {
			LOG.error("failed to inspect jmx object", e);
			throw new ServiceUnavailableException("failed to inspect jmx object", e);
		} catch (ReflectionException e) {
			LOG.error("failed to retrieve jmx object", e);
			throw new ServiceUnavailableException("failed to retrieve jmx object", e);
		} catch (AttributeNotFoundException e) {
			LOG.error("failed to retrieve jmx attribute", e);
			throw new ServiceUnavailableException("failed to retrieve jmx attribute", e);
		} catch (MBeanException e) {
			LOG.error("failed to prase jmx mbean", e);
			throw new ServiceUnavailableException("failed to prase jmx mbean", e);
		}
	}
	
	/**
	 * helper method to parse response and set actual to rules 
	 */
	private void addResponseToRules(Map<String,Object> mbeanMap) {
		List<Rule> rules = this.getRules();
		for(Rule rule : rules) {
			if( rule instanceof SimpleJmxResult ) {
				((SimpleJmxResult)rule).setJmxResult(mbeanMap);
			}
		}
	}
	
	/**
	 * helper method to compose the remote jmx url
	 * 
	 * @return
	 */
	private String getServiceURL() {
		StringBuffer sb = new StringBuffer("service:jmx:rmi:///jndi/rmi://");
		sb.append(this.getHostname());
		sb.append(":");
		sb.append(this.getPort());
		sb.append("/jmxrmi");
		
		return sb.toString();
	}
	
	/**
	 * helper method to setup crendentials
	 * 
	 * @return
	 */
	private Map<String, String[]> getCredentialEnvironment() {
		Map<String, String[]> credEnv = new HashMap<String, String[]>();
		String[] credentials = {this.getUsername(), this.getPassword()};
		credEnv.put(JMXConnector.CREDENTIALS, credentials);
		
		return credEnv;
	}
}
