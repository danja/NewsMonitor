package it.danja.newsmonitor.osgi;

import java.util.Properties;

import it.danja.newsmonitor.io.TextFileReader;
import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.main.ConfigReader;
import it.danja.newsmonitor.main.NewsMonitor;
import it.danja.newsmonitor.osgi.templating.BundleTemplateLoader;
import it.danja.newsmonitor.templating.TemplateLoader;
import it.danja.newsmonitor.templating.Templater;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * 
 * @author danny
 */
public class NewsMonitorActivator implements BundleActivator {
	private ServiceRegistration registration;
        private ServiceRegistration webUIRegistration;
	private NewsMonitor newsmonitor;
        private WebUI webUI;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		
		ConfigReader configReader = new ConfigReader();
		configReader.loadPropertiesFile(Config.CONFIG_PROPERTIES_BUNDLE_LOCATION);
		Properties config = configReader.getProperties();
		
		TextFileReader textFileReader = new BundleTextFileReader(bundle);
		
		TemplateLoader templateLoader = new BundleTemplateLoader(bundle, config);
		templateLoader.init();
		Templater templater = new Templater();
templater.setTemplateMap(templateLoader.getTemplateMap());
		newsmonitor = new NewsMonitor(config, textFileReader, templater);
		registration = bundleContext.registerService(
				NewsMonitor.class.getName(), newsmonitor, null);
                webUI = new WebUI();
                webUIRegistration = bundleContext.registerService(
				WebUI.class.getName(), webUI, null);
		// newsmonitor.setBundleContext(bundleContext);
		newsmonitor.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {

		registration.unregister();
                webUIRegistration.unregister();
		newsmonitor.stop(); 
	}
}
