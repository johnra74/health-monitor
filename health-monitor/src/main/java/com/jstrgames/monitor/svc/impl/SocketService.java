package com.jstrgames.monitor.svc.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstrgames.monitor.cfg.ValidationException;
import com.jstrgames.monitor.svc.ServiceUnavailableException;

/**
 * this class is used to verify a server is running at the
 * specified port and host
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class SocketService extends BaseService {
	private final static Logger LOG = LoggerFactory.getLogger(SocketService.class);
			
	@Override
	public void validate(Map<String, Object> map) throws ValidationException {
		// DO NOTHING... BaseService has all required attributes
	}

	@Override
	public void fromExtendedMap(Map<String, Object> map) {
		// DO NOTHING... BaseService has all required attributes
	}

	@Override
	public void connectToService() throws ServiceUnavailableException {
		Socket client = null;
		try {
			client = new Socket(this.getHostname(), this.getPort());
		} catch (UnknownHostException e) {
			LOG.info("Failed to connect to host", e);
			throw new ServiceUnavailableException("Failed to connect to host", e);
		} catch (IOException e) {
			LOG.info("Failed to read from socket", e);
			throw new ServiceUnavailableException("Failed to read from socket", e);
		} finally {
			if(client != null) {
				try {
					client.close();
				} catch (IOException e) {
					LOG.warn("Failed to close socket!", e);
				}
			}
		}
	}

}
