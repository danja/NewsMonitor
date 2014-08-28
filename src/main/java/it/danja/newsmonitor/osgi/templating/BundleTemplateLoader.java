/**
 * NewsMonitor
 *
 * Templater.java
 *
 * @author danja
 * @date Apr 26, 2014
 *
 */
package it.danja.newsmonitor.osgi.templating;

import it.danja.newsmonitor.io.ResourceLister;
import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.templating.TemplateLoader;
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
public class BundleTemplateLoader extends TemplateLoaderBase {

	private static Logger log = LoggerFactory
			.getLogger(BundleTemplateLoader.class);

	private static Configuration configuration = new Configuration();
	private static Map<String, Template> templateMap = new HashMap<String, Template>();

	// private Collection<Bundle> bundles = new HashSet<Bundle>();
	//
	// private BundleContext bundleContext;

	private Properties config = null;

	private Bundle bundle = null;

	// public void setBundleContext(BundleContext bundleContext) {
	// this.bundleContext = bundleContext;
	// }

	public BundleTemplateLoader(Bundle bundle, Properties config) {
		this.config = config;
		this.bundle = bundle;
	}

	public synchronized void loadTemplates() {
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

				// String string = "";
				// buffer = new StringBuffer();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));

				Template template = new Template(name, reader, configuration);
				reader.close();
				// while ((string = reader.readLine()) != null) {
				// buffer.append(string + "\n");
				// }
				templateMap.put(name, template);
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
		// throw new RuntimeException("NAME : " + name);
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
		 * bundle.getResource(Config.TEMPLATES_DIR_IN_BUNDLE+"sample.ftl"); URL
		 * test = null; try { test = findTemplateSource("sample"); } catch
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
		return bundle.getResource(config.getProperty("TEMPLATES_DIR_LOCATION")) != null;
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
