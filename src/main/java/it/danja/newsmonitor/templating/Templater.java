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

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import it.danja.newsmonitor.main.Config;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for caching/applying Freemarker templates
 */
public class Templater {

    private static Logger log = LoggerFactory.getLogger(Templater.class);

    private static Configuration configuration = new Configuration();
    private static Map<String, Template> templates = new HashMap<String, Template>();

    private     Collection<Bundle> bundles = new HashSet<Bundle>();
    
    private BundleContext bundleContext;

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Templater templater = new Templater();
        templater.init();
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

    public void init() {
        // Templater templater = new Templater
        configuration.setDefaultEncoding("UTF-8");
        if (Config.BUILD_TYPE == Config.STANDALONE_BUILD) {
            loadTemplatesFromFilesystem();
        } else {
            loadTemplatesFromBundle();
        }
    }

    public synchronized void loadTemplatesFromFilesystem() {
        // get template files by [path]/[name].ftl
        File folder = new File(Config.TEMPLATES_DIR);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {
                String filename = listOfFiles[i].getName();
                if (filename.toLowerCase().endsWith(".ftl")) {
                    loadTemplateFromFilesystem(filename);
                }
            }
        }
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
            e.printStackTrace();
        }
    }

    public synchronized void loadTemplatesFromBundle() {
        Bundle bundle = bundleContext.getBundle();
       // URL test = bundle.getResource(Config.TEMPLATES_LOCATION+"sample.ftl");
        URL test = null;
        try {
            test = findTemplateSource("sample");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Templater.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.err.println("A URL : "+test.toString());
        log.error("B URL : "+test.toString());
        throw new RuntimeException("C URL : "+test.toString());
        /*
        Enumeration<URL> urls = null;
        try {
            urls = bundle.getResources(Config.TEMPLATES_LOCATION);

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                 log.error("loading URL : "+url);
                loadTemplateFromUrl(url);
            }
        } catch (IOException ex) {
            
            log.error(ex.getMessage());
        }
                */
    }
    
   private void loadBundles(BundleContext bundleContext) {
		final Bundle[] registeredBundles = bundleContext.getBundles();
		for (int i = 0; i < registeredBundles.length; i++) {
			if ((registeredBundles[i].getState() == Bundle.ACTIVE) 
					&& containsTemplates(registeredBundles[i])) {
				bundles.add(registeredBundles[i]);
			}
		}	
		
	}
   
   	private boolean containsTemplates(Bundle bundle) {
		return bundle.getResource(Config.TEMPLATES_LOCATION) != null;
	}
    
    public URL findTemplateSource(String name) throws IOException {
    
		if (!name.endsWith(".ftl")) {
			name = name +".ftl";
		}
		final String path = Config.TEMPLATES_LOCATION+name;
		for (Bundle bundle : bundles) {
			URL res = bundle.getResource(path);
			if (res != null) {
				return res;
			}
		}
		log.warn("Template "+name+" not known");
		return null;
	}

    public synchronized void loadTemplateFromUrl(URL url) {
        try {
            //  InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
            configuration.setTemplateLoader(new BundleURLTemplateLoader());
            String urlString = url.toString();
            Template template = configuration.getTemplate(urlString);
            // log.info(filename);
            // String[] split = filename.split(".");

            String name = urlString.substring(0, urlString.indexOf(".")); // remove
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
