package com.jstrgames.monitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;

import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstrgames.monitor.cfg.ConfigLoader;
import com.jstrgames.monitor.cfg.ConfigurationException;
import com.jstrgames.monitor.net.HealthConfigHandler;
import com.jstrgames.monitor.net.HealthStatusHandler;
import com.jstrgames.monitor.svc.Service;

public class Command {
	private final static Logger LOG = LoggerFactory.getLogger(Command.class);
	
	private static Options buildCommandOptions() {
		Options options = new Options();
		options.addOption( "h", "help", false, "list help" );
		options.addOption( 
				OptionBuilder.withLongOpt("config-file")
                			 .withDescription( "health monitor configuration file. If not specified, will look for monitor-cfg.json in classpath" )
                			 .hasArg()
                			 .withArgName("FILE")
                			 .create());
		return options;
	}
	
	private static InputStream loadConfigFromDefault() {
		return ClassLoader.getSystemResourceAsStream("monitor-cfg.json");
	}
	
	public static void main(String[] args) {
		CommandLineParser cmdParser = new BasicParser(); 
		Options options = buildCommandOptions();
		InputStream in = null;
		try {			
			CommandLine cmd = cmdParser.parse(options, args);
			if(cmd.hasOption('h')) {
				HelpFormatter fmt = new HelpFormatter();
				fmt.printHelp("java com.jstrgames.monitor.Command [OPTIONS]", options);				
			} else {
				
				if(cmd.hasOption("config-file")) {
					String fileName = cmd.getOptionValue("config-file");
					in = new FileInputStream(fileName);
				} else {
					in = loadConfigFromDefault();
				}
				ConfigLoader cfgLoader = new ConfigLoader(in);
				SchedulerFactory factory = new StdSchedulerFactory();
				List<Service> list = cfgLoader.getServices();
				JobManager jobMgr = new JobManager(factory.getScheduler(), list);
				jobMgr.start();
				
				HttpServer server = HttpServer.createSimpleServer(
						"/", cfgLoader.getHostname(), cfgLoader.getPort());
				HealthStatusHandler statusHandler = new HealthStatusHandler(cfgLoader, jobMgr);
				HealthConfigHandler cfgHandler = new HealthConfigHandler(cfgLoader);
				server.getServerConfiguration().addHttpHandler(statusHandler, "/status.html");
				server.getServerConfiguration().addHttpHandler(cfgHandler, "/service.json");
				server.start();

			}
		} catch (ParseException e) {
			LOG.error("Failed to parse options", e);
		} catch (ConfigurationException e) {
			LOG.error("Failed to read configuration file", e);
		} catch (FileNotFoundException e) {
			LOG.error("Failed to locate configuration file", e);
		} catch (SchedulerException e) {
			LOG.error("Failed to schedule service", e);
		} catch (IOException e) {
			LOG.error("Failed to start gizzly service", e);
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					LOG.error("Failed to close input stream", e);
				}
			}
		}
	}
}