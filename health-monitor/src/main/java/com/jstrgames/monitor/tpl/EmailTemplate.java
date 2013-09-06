package com.jstrgames.monitor.tpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jstrgames.monitor.svc.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * this class is used to generate status email content base on Freemarker template 
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class EmailTemplate extends BaseTemplate {
	public EmailTemplate(String directory, List<Service> svcList) throws TemplateDirNotFoundException {
		super(directory, svcList);
	}	

	@Override
	protected Template getTemplate(Configuration fmtpl) throws IOException {
		return fmtpl.getTemplate("email.ftl");
	}

	@Override
	protected Map<String, Object> getTemplateData() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("statusList", this.svcList);
		return data;
	}
}
