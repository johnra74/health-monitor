package com.jstrgames.monitor.cfg;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstrgames.monitor.JobManager;
import com.jstrgames.monitor.tpl.EmailTemplate;
import com.jstrgames.monitor.tpl.TemplateDirNotFoundException;

/**
 * this class will hold the notification configurations as well as logic
 * to compose message based on configuration
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class NotificationConfig {
	private final static Logger LOG = LoggerFactory.getLogger(NotificationConfig.class);
			
	protected final static String NOTIFICATION_SMTP_HOSTNAME = "smtp.hostname";
	protected final static String NOTIFICATION_SMTP_PORT = "smtp.port";
	protected final static String NOTIFICATION_SMTP_USERNAME = "smtp.username";
	protected final static String NOTIFICATION_SMTP_PASSWORD = "smtp.password";
	
	protected final static String NOTIFICATION_MAIL_FROM = "mail.from";
	protected final static String NOTIFICATION_MAIL_TO = "mail.to";
	protected final static String NOTIFICATION_MAIL_SUBJECT = "mail.subject";
	protected final static String NOTIFICATION_MAIL_NAME = "name";
	protected final static String NOTIFICATION_MAIL_EMAIL = "email";
	
	protected final static String NOTIFICATION_ONFAILURE = "notify.onfailure";
	protected final static String NOTIFICATION_ONLYFIRST = "notify.onlyfirst";
	protected final static String NOTIFICATION_ONCHANGE = "notify.onchange";
	protected final static String NOTIFICATION_SCHEDULE = "notify.schedule";
	
	private int lastSuccessCnt;
	
	private String smtpHostname;
	private String smtpPort;
	private String smtpUsername;
	private String smtpPassword;
	
	private InternetAddress[] mailFromAddress;
	private InternetAddress[] mailToAddress;
	private String mailSubject;
	
	private boolean notifyOnFailure;
	private boolean notifyOnlyFirst;
	private boolean notifyOnChange;
	private boolean notifySend;
	
	private String notifySchedule;	
	
	private final String tplDirectory;
	
	public NotificationConfig(Map<String,Object> map, String tplDirectory) 
			throws ConfigurationException {
		this.tplDirectory = tplDirectory;
		lastSuccessCnt = 0;		
		notifyOnFailure = false;
		notifyOnlyFirst = false;
		notifyOnChange = false;
		notifySend = false;
		setup(map);
	}
	
	/**
	 * helper method to read configuration map to populate this class properties
	 * 
	 * @param cfgMap
	 * @throws ConfigurationException
	 */
	private void setup(Map<String,Object> cfgMap) throws ConfigurationException {
		// get host/port
		if(!cfgMap.containsKey(NOTIFICATION_SMTP_HOSTNAME)) {
			throw new ConfigurationException("missing smtp.hostname");
		}
		this.smtpHostname = (String)cfgMap.get(NOTIFICATION_SMTP_HOSTNAME);
		if(!cfgMap.containsKey(NOTIFICATION_SMTP_PORT)) {
			throw new ConfigurationException("missing smtp.port");
		}
		this.smtpPort = (String)cfgMap.get(NOTIFICATION_SMTP_PORT);
		// get username/password
		if(cfgMap.containsKey(NOTIFICATION_SMTP_USERNAME)) {
			this.smtpUsername = (String)cfgMap.get(NOTIFICATION_SMTP_USERNAME);		
			if(!cfgMap.containsKey(NOTIFICATION_SMTP_PASSWORD)) {
				throw new ConfigurationException("missing smtp.password");
			}
			this.smtpPassword = (String)cfgMap.get(NOTIFICATION_SMTP_PASSWORD);			
		}
		
		// get from address
		if(!cfgMap.containsKey(NOTIFICATION_MAIL_FROM)) {
			throw new ConfigurationException("missing mail.from");
		}		
		try {
			@SuppressWarnings("unchecked")
			Map<String,String> mailFromMap = 
				(Map<String,String>) cfgMap.get(NOTIFICATION_MAIL_FROM);
			this.mailFromAddress = createFromAddress(mailFromMap);
		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationException("failed to parse from address");
		}
		
		// get from address
		if(!cfgMap.containsKey(NOTIFICATION_MAIL_TO)) {
			throw new ConfigurationException("missing mail.to");
		}		
		try {
			@SuppressWarnings("unchecked")
			List<Map<String,String>> mailToList = 
				(List<Map<String,String>>) cfgMap.get(NOTIFICATION_MAIL_TO);
			this.mailToAddress = createToAddress(mailToList);
		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationException("failed to parse from address");
		}
		
		// get subject
		if(!cfgMap.containsKey(NOTIFICATION_MAIL_SUBJECT)) {
			throw new ConfigurationException("missing mail.subject");
		}
		this.mailSubject = (String)cfgMap.get(NOTIFICATION_MAIL_SUBJECT);
		
		// get schedule
		if(!cfgMap.containsKey(NOTIFICATION_SCHEDULE)) {
			throw new ConfigurationException("missing notify.schedule");
		}
		this.notifySchedule = (String)cfgMap.get(NOTIFICATION_SCHEDULE);
		
		// get on failure flag
		if(cfgMap.containsKey(NOTIFICATION_ONFAILURE)) {
			this.notifyOnFailure = ((Boolean)cfgMap.get(NOTIFICATION_ONFAILURE)).booleanValue();
		}		
		// get on first flag
		if(cfgMap.containsKey(NOTIFICATION_ONLYFIRST)) {
			this.notifyOnlyFirst = ((Boolean)cfgMap.get(NOTIFICATION_ONLYFIRST)).booleanValue();
		}		
		// get on change flag
		if(cfgMap.containsKey(NOTIFICATION_ONCHANGE)) {
			this.notifyOnChange = ((Boolean)cfgMap.get(NOTIFICATION_ONCHANGE)).booleanValue();
		}
	}
	
	/**
	 * method will return the cron schedule
	 * 
	 * @return
	 */
	public String getSchedule() {
		return this.notifySchedule;
	}
	
	/**
	 * method will determine if user should be notified
	 * 
	 * @param jobMgr
	 * @return
	 */
	public boolean shouldSendNotification(JobManager jobMgr) {
		final int successCnt = jobMgr.getSuccessCount();
		final int totalCnt = jobMgr.getTotalCount();
		
		boolean shouldSend = false;
		if(successCnt != totalCnt) {
			// failure has occurred
			if(this.notifyOnFailure) {
				// user wish to be notified
				if(!this.notifyOnlyFirst) {
					// user always want to be notified.. this can SPAM user
					shouldSend = true;
					this.notifySend = true;
				} else {
					// user only wants to be notified on first failure
					if(this.notifyOnChange &&				
					   this.lastSuccessCnt != successCnt) {				
						// user also wants to be notified when status changes
						shouldSend = true;
						this.notifySend = true;
					} else if(!this.notifySend) {
						// user should only be notified on first failure
						shouldSend = true;
						this.notifySend = true;
					}					
				}				
			}
		} else {
			if(this.notifySend) {
				// user wish to be notified on first failure.. and we have
				this.notifySend = false;
				// system is back on line.. so send them an update
				shouldSend = true;
			}
		}
		
		this.lastSuccessCnt = successCnt;		
		return shouldSend;
	}
	
	/**
	 * this method will create a message with status details to target users 
	 * 
	 * @param session
	 * @param jobMgr
	 * @return
	 */
	public Message createMessage(Session session, JobManager jobMgr)
		throws ConfigurationException {		
		Message message = new MimeMessage(session);
		
		try {
			message.addFrom(this.mailFromAddress);
			message.setRecipients(
					Message.RecipientType.TO,
					this.mailToAddress);
			message.setSubject(
					String.format("%s: %d of %d", 
							this.mailSubject, 
							jobMgr.getSuccessCount(),
							jobMgr.getTotalCount()));
			EmailTemplate body = new EmailTemplate(this.tplDirectory, jobMgr.getServices());
			message.setContent(body.getHtml(), "text/html; charset=utf-8");
			
		} catch (MessagingException e) {
			LOG.error("failed to setup mail message", e);
			throw new ConfigurationException("failed to setup mail message", e);
		} catch (TemplateDirNotFoundException e) {
			LOG.error("template directory specified does not exists!", e);
			throw new ConfigurationException("template directory specified does not exists!", e);
		}
		return message;
	}
	
	/**
	 * this method will determine if SMTP server requires authentication
	 * 
	 * @return
	 */
	public boolean isSmtpAuthEnabled() {
		return (this.smtpUsername != null && this.smtpPassword != null);
	}
	
	/**
	 * this method will construct the necessare SMTP authenticator
	 * 
	 * @return
	 */
	public Authenticator getAuthenticator() {
		final String username = this.smtpUsername;
		final String password = this.smtpPassword;
		
		return new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  };
	}

	/**
	 * this method will set and return properties for javamail 
	 * 
	 * @return
	 */
	public Properties getMailProperty() {
		Properties props = new Properties();
		if(this.isSmtpAuthEnabled()) {
			props.put("mail.smtp.auth", "true");
		}
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", this.smtpHostname);
		props.put("mail.smtp.port", this.smtpPort);
		
		return props;
	}
	
	/**
	 * helper method will setup mail from address list
	 *  
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private InternetAddress[] createFromAddress(Map<String,String> map) 
			throws UnsupportedEncodingException {
		InternetAddress[] addresses = new InternetAddress[1];
		String address = map.get(NOTIFICATION_MAIL_EMAIL);
		String personName = map.get(NOTIFICATION_MAIL_NAME);
		addresses[0] = new InternetAddress(address, personName);
		return addresses;
	}
	
	/**
	 * helper emthod will setup mail to address list
	 * @param list
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private InternetAddress[] createToAddress(List<Map<String,String>> list) 
			throws UnsupportedEncodingException {
		InternetAddress[] addresses = new InternetAddress[list.size()];
		int idx = 0;
		for(Map<String,String> map : list) {
			String address = map.get(NOTIFICATION_MAIL_EMAIL);
			String personName = map.get(NOTIFICATION_MAIL_NAME);
			addresses[idx++] = new InternetAddress(address, personName);
		}
		return addresses;
	}
}
