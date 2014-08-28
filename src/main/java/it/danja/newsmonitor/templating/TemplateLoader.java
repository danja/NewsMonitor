package it.danja.newsmonitor.templating;

import java.util.Map;

import freemarker.template.Template;

public interface TemplateLoader {

	public void init();

	/*
	 * TODO ResourceLister was introduced when trying to load dir from bundle jar - it's no longer needed for 
	 * that so should be reverted to folder.listFiles(); approach below
	 */
	public void loadTemplates();
	
	public Map<String, Template> getTemplateMap();

	// public void loadTemplate(String filename);

}