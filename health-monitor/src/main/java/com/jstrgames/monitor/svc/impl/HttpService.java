package com.jstrgames.monitor.svc.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstrgames.monitor.cfg.ValidationException;
import com.jstrgames.monitor.rule.FailedRuleException;
import com.jstrgames.monitor.rule.HttpResponseBody;
import com.jstrgames.monitor.rule.HttpResponseCode;
import com.jstrgames.monitor.rule.Rule;
import com.jstrgames.monitor.svc.ServiceUnavailableException;

/**
 * this class will validate the status of HTTP server status based on
 * rules defined
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class HttpService extends BaseService {
	private final static Logger LOG = LoggerFactory.getLogger(HttpService.class);
			
	public final static String EXTENDEDMAPKEY_URI = "uri";
	public final static String EXTENDEDMAPKEY_AUTHTYPE = "authtype";
	public final static String EXTENDEDMAPKEY_USERNAME = "username";
	public final static String EXTENDEDMAPKEY_PASSWORD = "password";
	
	private String uri;
	private String authType;
	private String username;
	private String password;
		
	public void setURI(String uri) {
		this.uri = uri;
	}
	
	public String getURI() {
		return this.uri;
	}
	
	public boolean hasAuthType() {
		return (authType != null);
	}
	
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	
	public String getAuthType() {
		return this.authType;
	}
	
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
	
	/**
	 * helper method to build the target URL
	 * 
	 * @return
	 */
	public String getURL() {
		StringBuilder builder = new StringBuilder("http://");
		builder.append(this.getHostname());
		if( this.getPort() != 80 ) {
			builder.append(":");
			builder.append(this.getPort());
		}
		builder.append(this.getURI());
		return builder.toString();
	}
	
	@Override
	public void fromExtendedMap(Map<String, Object> map) {
		this.setURI(map.get(EXTENDEDMAPKEY_URI).toString());
		if(map.containsKey(EXTENDEDMAPKEY_AUTHTYPE)) {
			this.setAuthType((String) map.get(EXTENDEDMAPKEY_AUTHTYPE));
			this.setUsername((String) map.get(EXTENDEDMAPKEY_USERNAME));
			this.setPassword((String) map.get(EXTENDEDMAPKEY_PASSWORD));
		}
	}

	@Override
	public void connectToService() throws ServiceUnavailableException {
		HttpClient client = this.getHttpClient();
		HttpUriRequest request = new HttpGet(this.getURL());
		try {
			HttpResponse response;
			if(this.hasAuthType() && "Basic".equalsIgnoreCase(this.getAuthType())) {
				HttpHost targetHost = new HttpHost(this.getHostname(), this.getPort(), this.getProtocol()); 
				// Create AuthCache instance
				AuthCache authCache = new BasicAuthCache();
				// Generate BASIC scheme object and add it to the local auth cache
				BasicScheme basicAuth = new BasicScheme();
				authCache.put(targetHost, basicAuth);
				BasicHttpContext localcontext = new BasicHttpContext();
				localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);
				response = client.execute(targetHost, request, localcontext);
			} else {
				response = client.execute(request);
			}
			addResponseToRules(response);
		} catch (ClientProtocolException e) {
			LOG.info("Failed to connect to service", e);
			throw new ServiceUnavailableException("Failed to connect to service");
		} catch (IOException e) {
			LOG.info("Failed to read from service", e);
			throw new ServiceUnavailableException("Failed to read from service");
		}
	}
	
	/**
	 * helper method to retrieve protocol based on its instance
	 * 
	 * @return
	 */
	private String getProtocol() {
		String protocol = "http";
		if( this instanceof HttpsService ) {
			protocol = "https";
		}		
		return protocol;
	}
	
	@Override
	public void validate(Map<String, Object> map) throws ValidationException {
		if(!map.containsKey(EXTENDEDMAPKEY_URI)){
			throw new ValidationException("Missing URI");
		}
		
		if(map.containsKey(EXTENDEDMAPKEY_AUTHTYPE)) {
			if(!map.containsKey(EXTENDEDMAPKEY_USERNAME)) {
				throw new ValidationException("Auth specified but missing username");
			}
			
			if(!map.containsKey(EXTENDEDMAPKEY_PASSWORD)) {
				throw new ValidationException("Auth specified but missing password");
			}
		}
	}
	
	/**
	 * helper method to retrieve httpclient for non-SSL. This will be overwritten
	 * by subclass for SSL. 
	 * 
	 * @return
	 */
	protected HttpClient getHttpClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		if(this.hasAuthType() && "Basic".equalsIgnoreCase(this.getAuthType())) {
			client.getCredentialsProvider().setCredentials(
			        new AuthScope(this.getHostname(), this.getPort()), 
			        new UsernamePasswordCredentials(this.getUsername(), this.getPassword()));
		}		
		return client;
	}
	
	/**
	 * helper method to parse response and set actual to rules 
	 */
	protected void addResponseToRules(HttpResponse response) {
		final int statusCode = getResponseStatusCode(response);
		final String responseBody = getResponseBody(response); 
		
		List<Rule> rules = this.getRules();
		for(Rule rule : rules) {
			if (rule instanceof HttpResponseCode) {
				rule.setActual(new Integer(statusCode));
			} else if( rule instanceof HttpResponseBody) {
				rule.setActual(responseBody);
			} else {
				rule.setActual(responseBody);
			}
		}
	}
	
	/**
	 * helper method to retrieve the response body
	 * @param response
	 * @return
	 */
	protected String getResponseBody(HttpResponse response) {
		StringWriter writer = new StringWriter();
		HttpEntity entity = response.getEntity();
		try {
			InputStream in = entity.getContent();
			IOUtils.copy(in, writer);
		} catch (IllegalStateException e) {
			LOG.info("Failed to read response", e);
		} catch (IOException e) {
			LOG.info("Failed to read response stream", e);
		}
		
		return writer.toString();
	}
	
	/**
	 * helper method to retrieve status code
	 * 
	 * @param response
	 * @return
	 */
	protected int getResponseStatusCode(HttpResponse response) {
		StatusLine status = response.getStatusLine();
		return status.getStatusCode();
	}

}
