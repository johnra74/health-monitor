package com.jstrgames.monitor.tpl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstrgames.monitor.svc.Service;
import com.jstrgames.monitor.svc.Service.Status;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class WebTemplate {
	private final static Logger LOG = LoggerFactory.getLogger(WebTemplate.class);
	private final Template template;
	private final List<Service> svcList;
	
	public WebTemplate(String directory, List<Service> svcList) throws TemplateDirNotFoundException {
		this.svcList = svcList;
		try {
			Configuration fmtpl = new Configuration();
			fmtpl.setDirectoryForTemplateLoading(new File(directory));
			this.template = fmtpl.getTemplate("healthstatus.ftl");
		} catch (IOException e) {
			throw new TemplateDirNotFoundException("Template directory [" + directory + "] not found!");
		}
	}	

	public String getHtml() {		
		StringWriter writer = new StringWriter();
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("statusSummary", getStatusCount());
			data.put("statusList", this.svcList);
			template.process(data, writer);
		} catch (TemplateException e) {
			LOG.error("failed to process template", e);
		} catch (IOException e) {
			LOG.error("failed to write template", e);
		}
		return writer.toString();
	}
	
	private String getStatusCount() {
		int successCnt = 0;
		for(Service service: this.svcList) {
			if(service.getStatus() == Status.PASS) {
				successCnt++;
			}
		}
		
		return String.format("%d of %d", successCnt, this.svcList.size());
	}
}
