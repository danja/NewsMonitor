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

import it.danja.newsmonitor.io.ResourceLister;
import it.danja.newsmonitor.main.Config;

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
public class Templater {

	private static Logger log = LoggerFactory.getLogger(Templater.class);

	private static Configuration configuration = new Configuration();
	private static Map<String, Template> templates = new HashMap<String, Template>();

	private Collection<Bundle> bundles = new HashSet<Bundle>();

	private BundleContext bundleContext;

//	public void setBundleContext(BundleContext bundleContext) {
//		this.bundleContext = bundleContext;
//	}
	
	public Templater() {
	}
	
	public Templater(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
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

	public static String apply(String templateName, Object dataModel) {
		// log.info("templateName = " + templateName);

		Writer writer = new StringWriter();
		try {
			Template template = templates.get(templateName);
			// log.info("template = " + template);
			// log.info("dataModel = " + dataModel);
			template.process(dataModel, writer);
			writer.close();
		} catch (TemplateException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return writer.toString();
	}

	public static Template getTemplate(String name) {
		return templates.get(name);
	}

	public void init() {
		// Templater templater = new Templater
		configuration.setDefaultEncoding("UTF-8");
		if (Config.BUILD_TYPE == Config.STANDALONE_BUILD) {
			loadTemplatesFromFilesystem();
		} else {
			loadTemplatesFromBundle();
			// loadTemplatesFromFilesystem();
		}
	}

	/*
	 * TODO ResourceLister was introduced when trying to load dir from bundle jar - it's no longer needed for 
	 * that so should be reverted to folder.listFiles(); approach below
	 */
	public synchronized void loadTemplatesFromFilesystem() {
		ResourceLister lister = new ResourceLister();
		String[] dir = null;
		try {
			dir = lister.getResourceListing(Templater.class,
					Config.TEMPLATES_DIR_IN_BUNDLE);
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		for (int i = 0; i < dir.length; i++) {

			if (dir[i].toLowerCase().endsWith(".ftl")) {
				loadTemplateFromFilesystem(dir[i]);
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

	public synchronized void loadTemplateFromFilesystem(String filename) {
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
			log.error(e.getMessage());
		}
	}

	public synchronized void loadTemplatesFromBundle() {
		Bundle bundle = bundleContext.getBundle();
		Enumeration<URL> entries = bundle.findEntries("/", "*.ftl", true);
		// String name = null;

		while (entries.hasMoreElements()) {
			URL url = entries.nextElement();
			String[] pathSplit = url.getPath().split("/");
			String filename = pathSplit[pathSplit.length - 1];
			String name = filename.substring(0, filename.length() - 4);
			// String templateString = null;
			Configuration configuration = new Configuration();
			StringBuffer buffer = null;
			// if (url != null) {
			InputStream inputStream = null;
			try {
				inputStream = url.openStream();

				//String string = "";
				//buffer = new StringBuffer();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));

				Template template = new Template(name, reader, configuration);
				reader.close();
				// while ((string = reader.readLine()) != null) {
				// buffer.append(string + "\n");
				// }
				templates.put(name, template);
			} catch (IOException ex) {
				log.error(ex.getMessage());
			}

			// templateString = buffer.toString();
			// }

			// File file = bundleContext.getDataFile(url.getPath().substring(1,
			// url.getPath().length() ));

			// throw new RuntimeException("TEXT : " + buf.toString());
			// StringTemplateLoader templateLoader = new StringTemplateLoader();

		}
	//	throw new RuntimeException("NAME : " + name);
		// File file=new File(url.toURI());

		/*
		 * try { FileReader reader = new FileReader(file); char[] chars = new
		 * char[(int) file.length()]; reader.read(chars); content = new
		 * String(chars); reader.close(); } catch (IOException e) {
		 * log.error(e.getMessage()); }
		 */
		// throw new RuntimeException(file.toString()+"\n - CONTENTS : "+s);
		// bundle://298.0:0/templates/html/it/danja/newsmonitor/resource/sample.ftl

		/*
		 * String name =
		 * "/templates/html/it/danja/newsmonitor/resource/sample.ftl";
		 * InputStream inputStream = getClass().getResourceAsStream(name);
		 * 
		 * String str = ""; StringBuffer buf = new StringBuffer();
		 * 
		 * BufferedReader reader = new BufferedReader(new
		 * InputStreamReader(inputStream)); if (inputStream != null) { try {
		 * while ((str = reader.readLine()) != null) { buf.append(str + "\n"); }
		 * } catch (IOException ex) { log.error(ex.getMessage()); } } throw new
		 * RuntimeException("TEXT : " + buf.toString());
		 */
		/*
		 * // URL test =
		 * bundle.getResource(Config.TEMPLATES_DIR_IN_BUNDLE+"sample.ftl"); URL test
		 * = null; try { test = findTemplateSource("sample"); } catch
		 * (IOException ex) {
		 * java.util.logging.Logger.getLogger(Templater.class.
		 * getName()).log(Level.SEVERE, null, ex); }
		 * System.err.println("A URL : "+test.toString());
		 * log.error("B URL : "+test.toString()); throw new
		 * RuntimeException("C URL : "+test.toString());
		 */
		/*
		 * Enumeration<URL> urls = null; try { urls =
		 * bundle.getResources(Config.TEMPLATES_DIR_IN_BUNDLE);
		 * 
		 * while (urls.hasMoreElements()) { URL url = urls.nextElement();
		 * log.error("loading URL : "+url); loadTemplateFromUrl(url); } } catch
		 * (IOException ex) {
		 * 
		 * log.error(ex.getMessage()); }
		 */
	}

	private boolean containsTemplates(Bundle bundle) {
		return bundle.getResource(Config.TEMPLATES_DIR_IN_BUNDLE) != null;
	}

	// public synchronized void loadTemplateFromUrl(URL url) {
	// try {
	// // InputStreamReader isr = new InputStreamReader(url.openStream(),
	// "UTF-8");
	// configuration.setTemplateLoader(new BundleURLTemplateLoader());
	// String urlString = url.toString();
	// Template template = configuration.getTemplate(urlString);
	// // log.info(filename);
	// // String[] split = filename.split(".");
	//
	// String name = urlString.substring(0, urlString.indexOf(".")); // remove
	// // extension
	// // log.info(name);
	// templates.put(name, template);
	// } catch (IOException e) {
	// log.error(e.getMessage());
	// }
	// }

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
