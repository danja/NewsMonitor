/**
 * feedreader-prototype
 *
 * Templater.java
 * 
 * @author danja
 * @date Apr 26, 2014
 *
 */
package org.danja.feedreader.templating;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.danja.feedreader.main.Config;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Utility for caching/applying Freemarker templates
 */
public class Templater {

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

		System.out.println(apply("sample", data));
	}

	public static String apply(String templateName, Object dataModel) {
		//System.out.println("templateName = " + templateName);

		Writer writer = new StringWriter();
		try {
			Template template = templates.get(templateName);
		//	System.out.println("template = " + template);
		//	System.out.println("dataModel = " + dataModel);
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
			// System.out.println(filename);
			// String[] split = filename.split(".");

			String name = filename.substring(0, filename.indexOf(".")); // remove
																		// extension
			// System.out.println(name);
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
