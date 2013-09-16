package com.jstrgames.monitor;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstrgames.monitor.cfg.ConfigurationException;
import com.jstrgames.monitor.cfg.NotificationConfig;
import com.sun.mail.util.MailConnectException;

/**
 * This is the notification implementations of quartz-scheduler job. For
 * based on notification configuration, email will be sent accordingly
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class NotificationJob implements Job {
	private final static Logger LOG = LoggerFactory.getLogger(NotificationJob.class);
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		NotificationConfig notifyCfg = (NotificationConfig) 
				context.getMergedJobDataMap().get("notifyCfg");		
		JobManager jobMgr = (JobManager) 
				context.getMergedJobDataMap().get("jobMgr");
		Session session = getSession(notifyCfg, jobMgr);		
		try {
			Message msg = notifyCfg.createMessage(session, jobMgr);
			sendmail(notifyCfg, jobMgr, msg);
		} catch (ConfigurationException e) {
			throw new JobExecutionException("failed to create default message",e);
		} catch (MailServerUnavailableException e) {
			LOG.error("failed to connect to mail server", e);
		}
	}
	
	/**
	 * method will send email containing overall services status
	 * 
	 * @param notifyCfg
	 * @param jobMgr
	 * @param session
	 * @return
	 * @throws MailServerUnavailableException
	 */
	public boolean sendmail(NotificationConfig notifyCfg, JobManager jobMgr, Message msg)
		throws MailServerUnavailableException {
		boolean hasSent = false;
		if(! notifyCfg.shouldSendNotification(jobMgr)) {
			LOG.debug("Not sending email as configured");
		} else {				
			try {
				Transport.send(msg);
				hasSent = true;
			} catch (MailConnectException e) {
				LOG.error("Unable to connect to SMTP server", e);
				throw new MailServerUnavailableException("Unable to connect to SMTP server", e);
			} catch (MessagingException e) {
				LOG.error("Failed to send message!", e);
			}
		}
		
		return hasSent;
	}
	
	/**
	 * method will retrieve an Mail session based on configuration
	 *  
	 * @param notifyCfg
	 * @param jobMgr
	 * @return
	 */
	protected Session getSession(NotificationConfig notifyCfg, JobManager jobMgr) {
		final Session session;
		final Properties props = notifyCfg.getMailProperty();
		
		if(notifyCfg.isSmtpAuthEnabled()) {
			final Authenticator authn = notifyCfg.getAuthenticator();
			session = Session.getInstance(props, authn);
		} else {
			session = Session.getInstance(props);
		}
		
		return session;
	}
	
}
