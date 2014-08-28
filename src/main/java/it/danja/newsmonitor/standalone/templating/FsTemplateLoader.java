/**
 * NewsMonitor
 *
 * Templater.java
 *
 * @author danja
 * @date Apr 26, 2014
 *
 */
package it.danja.newsmonitor.standalone.templating;

import it.danja.newsmonitor.io.ResourceLister;
import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.templating.TemplateLoaderBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Utility for caching/applying Freemarker templates
 */
public class FsTemplateLoader extends TemplateLoaderBase {

	private static Logger log = LoggerFactory.getLogger(FsTemplateLoader.class);

	private Properties config = null;

//	public void setBundleContext(BundleContext bundleContext) {
//		this.bundleContext = bundleContext;
//	}
	
	public FsTemplateLoader(Properties config) {
		this.config  = config;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Templater templater = new Templater();
//		templater.init();
//		// Template template = Templater.getTemplate("sample");
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("place", "Mozzanella");
//
//		log.info(apply("sample", data));
	}







	/*
	 * TODO ResourceLister was introduced when trying to load dir from bundle jar - it's no longer needed for 
	 * that so should be reverted to folder.listFiles(); approach below
	 */
	/* (non-Javadoc)
	 * @see it.danja.newsmonitor.standalone.templating.TemplateLoader#loadTemplatesFromFilesystem()
	 */
	@Override
	public synchronized void loadTemplates() {
		ResourceLister lister = new ResourceLister();
		String[] dir = null;
		try {
			dir = lister.getResourceListing(FsTemplateLoader.class,
					config.getProperty("TEMPLATES_DIR_LOCATION"));
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		for (int i = 0; i < dir.length; i++) {

			if (dir[i].toLowerCase().endsWith(".ftl")) {
				loadTemplate(dir[i]);
			}

		}
		/*
		 * // get template files by [path]/[name].ftl File folder = new
		 * File(Config.TEMPLATES_DIR); File[] listOfFiles = folder.listFiles();
		 * 
		 * for (int i = 0; i < listOfFiles.length; i++) {
		 * 
		 * if (listOfFiles[i].isFile()) { String filename =
		 * listOfFiles[i].getName(); if
		 * (filename.toLowerCase().endsWith(".ftl")) {
		 * loadTemplateFromFilesystem(filename); } } }
		 */
	}

	/* (non-Javadoc)
	 * @see it.danja.newsmonitor.standalone.templating.TemplateLoader#loadTemplateFromFilesystem(java.lang.String)
	 */
	public synchronized void loadTemplate(String filename) {
		try {
			Template template = getConfiguration().getTemplate(config.getProperty("TEMPLATES_LOCATION")
					+ "/" + filename);
			// log.info(filename);
			// String[] split = filename.split(".");

			String name = filename.substring(0, filename.indexOf(".")); // remove
			// extension
			// log.info(name);
			getTemplateMap().put(name, template);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public static String dataMapToString(Map<String, Object> data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("----------\nData Map :\n");
		Iterator<String> i = data.keySet().iterator();
		while (i.hasNext()) {
			buffer.append(i.next() + " = " + data.get(i) + "\n");
		}
		buffer.append("----------\n");
		return buffer.toString();
	}
}
