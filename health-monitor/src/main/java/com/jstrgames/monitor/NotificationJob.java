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
		
		sendmail(notifyCfg, jobMgr);
	}
	
	/**
	 * method will send email containing overall services status
	 * 
	 * @param notifyCfg
	 * @param jobMgr
	 * @return
	 */
	public boolean sendmail(NotificationConfig notifyCfg, JobManager jobMgr) {
		boolean hasSent = false;
		if(! notifyCfg.shouldSendNotification(jobMgr)) {
			LOG.debug("Not sending email as configured");
		} else {				
			final Properties props = notifyCfg.getMailProperty();
			final Session session;
			if(notifyCfg.isSmtpAuthEnabled()) {
				final Authenticator authn = notifyCfg.getAuthenticator();
				session = Session.getInstance(props, authn);
			} else {
				session = Session.getInstance(props);
			}
			try {
				Message msg = notifyCfg.createMessage(session, jobMgr);		
				Transport.send(msg);
				hasSent = true;
			} catch (MessagingException e) {
				LOG.error("Failed to send message!", e);
			} catch (ConfigurationException e) {
				LOG.error("Failed to create message!", e);
			}		
		}
		
		return hasSent;
	}
	
}
