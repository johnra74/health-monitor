package com.jstrgames.monitor.tpl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstrgames.monitor.svc.Service;
import com.jstrgames.monitor.svc.Service.Status;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * this abstract class is used to generate html content based on freemarker
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public abstract class BaseTemplate {
	private final static Logger LOG = LoggerFactory.getLogger(BaseTemplate.class);
	protected final Template template;
	protected final List<Service> svcList;
	
	public BaseTemplate(String directory, List<Service> svcList) throws TemplateDirNotFoundException {
		this.svcList = svcList;
		try {
			Configuration fmtpl = new Configuration();
			fmtpl.setDirectoryForTemplateLoading(new File(directory));
			this.template = getTemplate(fmtpl);
		} catch (IOException e) {
			LOG.error("Template directory [" + directory + "] not found!", e);
			throw new TemplateDirNotFoundException(
					"Template directory [" + directory + "] not found!");
		}
	}	

	/**
	 * method will return html content as a result of template that has 
	 * been merged with data
	 * 
	 * @return
	 */
	public final String getHtml() {		
		StringWriter writer = new StringWriter();
		try {
			Map<String, Object> data = getTemplateData();
			template.process(data, writer);
		} catch (TemplateException e) {
			LOG.error("failed to process template", e);
		} catch (IOException e) {
			LOG.error("failed to write template", e);
		}
		return writer.toString();
	}
	
	protected abstract Map<String, Object> getTemplateData();
	protected abstract Template getTemplate(Configuration fmtpl) throws IOException;
	
	/**
	 * helper method to retrieve status count
	 * 
	 * @return
	 */
	protected String getStatusCount() {
		int successCnt = 0;
		for(Service service: this.svcList) {
			if(service.getStatus() == Status.PASS) {
				successCnt++;
			}
		}
		
		return String.format("%d of %d", successCnt, this.svcList.size());
	}
}
