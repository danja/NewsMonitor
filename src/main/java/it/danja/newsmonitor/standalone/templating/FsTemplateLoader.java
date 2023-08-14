/**
 * NewsMonitor
 *
 * Templater.java
 *
 * @author danja
 * dc:date Apr 26, 2014
 *
 */
package it.danja.newsmonitor.standalone.templating;

import freemarker.template.Template;
import it.danja.newsmonitor.templating.TemplateLoaderBase;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for caching/applying Freemarker templates
 */
public class FsTemplateLoader extends TemplateLoaderBase {

    private static Logger log = LoggerFactory.getLogger(FsTemplateLoader.class);

    private Properties config = null;


    public FsTemplateLoader(Properties config) {
        this.config = config;
    }

    /*
	 * TODO ResourceLister was introduced when trying to load dir from bundle
	 * jar - it's no longer needed for that so should be reverted to
	 * folder.listFiles(); approach below
     */
 /*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.standalone.templating.TemplateLoader#
	 * loadTemplatesFromFilesystem()
     */
    @Override
    public synchronized void loadTemplates() {

        String dir = config.getProperty("TEMPLATES_LOCATION");
        System.out.println("TEMPLATES_LOCATION = "+dir);
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {
                String filename = listOfFiles[i].getName();
                System.out.println("filename =" + filename);
                if (filename.toLowerCase().endsWith(".ftl")) {
                    loadTemplate(filename);
                }
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

    /*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.standalone.templating.TemplateLoader#
	 * loadTemplateFromFilesystem(java.lang.String)
     */
    public synchronized void loadTemplate(String filename) {
        try {
            Template template = getConfiguration().getTemplate(
                    config.getProperty("TEMPLATES_LOCATION") + "/" + filename);
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
