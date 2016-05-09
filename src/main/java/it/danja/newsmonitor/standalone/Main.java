/**
 * NewsMonitor
 *
 * Main.java
 *
 * @author danja
 * @date Aug 18, 2014
 *
 */
package it.danja.newsmonitor.standalone;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import it.danja.newsmonitor.io.TextFileReader;
import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.main.ConfigReader;
import it.danja.newsmonitor.main.NewsMonitor;
import it.danja.newsmonitor.standalone.templating.FsTemplateLoader;
import it.danja.newsmonitor.templating.TemplateLoader;
import it.danja.newsmonitor.templating.Templater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        ConfigReader configReader = new ConfigReader();
        try {
            configReader.loadPropertiesFile(Config.CONFIG_PROPERTIES_STANDALONE_LOCATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Properties config = configReader.getProperties();

        TemplateLoader templateLoader = new FsTemplateLoader(config);
        templateLoader.loadTemplates();
        Templater templater = new Templater();
        templater.setTemplateMap(templateLoader.getTemplateMap());

        TextFileReader textFileReader = new FsTextFileReader();
        NewsMonitor nm = new NewsMonitor(config, textFileReader, templater);
        if (args.length > 1) {
            log.info("args[0] = " + args[0]);
//			if("-C".equals(args[0])) {
//				nm.start(Config.SEED_FEEDLIST_FILE);
//			} 
//			if("-f".equals(args[0])) {
//				nm.start(args[1]);
//			} 
        } else {
            nm.start();
        }
    }

    /**
     * for debugging
     *
     * @param properties
     */
    public static void listProperties(Properties properties) {
        System.out.println("=== Properties ===");
        Enumeration<Object> keys = properties.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            System.out.println(key + " = " + properties.getProperty(key));

        }
    }
}
