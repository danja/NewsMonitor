/**
 * NewsMonitor
 *
 * Templater.java
 * 
 * @author danja
 * @date Apr 26, 2014
 *
 */
package it.danja.newsmonitor.templating;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.main.Config;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Utility for caching/applying Freemarker templates
 */
public class Templater {
	
	private static Logger log = LoggerFactory.getLogger(Templater.class);

	private static Configuration configuration = new Configuration();
	private static Map<String, Template> templates = new HashMap<String, Template>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Templater.init();
		// Template template = Templater.getTemplate("sample");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("place", "Mozzanella");

		log.info(apply("sample", data));
	}

	public static String apply(String templateName, Object dataModel) {
		//log.info("templateName = " + templateName);

		Writer writer = new StringWriter();
		try {
			Template template = templates.get(templateName);
		//	log.info("template = " + template);
		//	log.info("dataModel = " + dataModel);
			template.process(dataModel, writer);
			writer.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return writer.toString();
	}

	public static Template getTemplate(String name) {
		return templates.get(name);
	}

	public static void init() {
		configuration.setDefaultEncoding("UTF-8");
		loadTemplates();
	}

	public static void loadTemplates() {
		// get template files by [path]/[name].ftl
		File folder = new File(Config.TEMPLATES_DIR);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				String filename = listOfFiles[i].getName();
				if (filename.toLowerCase().endsWith(".ftl")) {
					loadTemplate(filename);
				}
			}
		}
	}

	public static void loadTemplate(String filename) {
		try {
			Template template = configuration.getTemplate(Config.TEMPLATES_DIR
					+ "/" + filename);
			// log.info(filename);
			// String[] split = filename.split(".");

			String name = filename.substring(0, filename.indexOf(".")); // remove
																		// extension
			// log.info(name);
			templates.put(name, template);
		} catch (IOException e) {
			e.printStackTrace();
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
