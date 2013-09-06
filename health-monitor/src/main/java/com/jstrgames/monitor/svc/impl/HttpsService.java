package com.jstrgames.monitor.svc.impl;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this class will validate the status of HTTPS server status based on
 * rules defined. It extends the HttpService (non-SSL) and overrides 
 * relevant methods to ensure proper SSL connects are made 
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class HttpsService extends HttpService {
	private final static Logger LOG = LoggerFactory.getLogger(HttpsService.class);
	
	/**
	 * method will return SSL URL
	 */
	@Override
	public String getURL() {
		StringBuilder builder = new StringBuilder("https://");
		builder.append(this.getHostname());
		if( this.getPort() != 443 ) {
			builder.append(":");
			builder.append(this.getPort());
		}
		builder.append(this.getURI());
		return builder.toString();
	}
	
	@Override
	protected HttpClient getHttpClient() {		
		SSLSocketFactory sf = null;
		try {
			sf = new SSLSocketFactory(
					new TrustStrategy() {
						@Override
						public boolean isTrusted(X509Certificate[] chain, String authType)
								throws CertificateException {
							return true; // trust everything
						}
					}, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (KeyManagementException e) {
			LOG.error("Failed to manage key", e);
		} catch (UnrecoverableKeyException e) {
			LOG.error("Failed to recover key", e);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Unsupported algorithm", e);
		} catch (KeyStoreException e) {
			LOG.error("Failed to get keystore", e);
		}
		
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("https", this.getPort(), sf));		
		ClientConnectionManager ccm = new PoolingClientConnectionManager(registry);
		DefaultHttpClient client = new DefaultHttpClient(ccm);
		if(this.hasAuthType() && "Basic".equalsIgnoreCase(this.getAuthType())) {
			client.getCredentialsProvider().setCredentials(
			        new AuthScope(this.getHostname(), this.getPort()), 
			        new UsernamePasswordCredentials(this.getUsername(), this.getPassword()));
		}
		
		return client;
	}
}
