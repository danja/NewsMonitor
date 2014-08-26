package it.danja.newsmonitor.osgi;

import it.danja.newsmonitor.main.NewsMonitor;

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
		newsmonitor = new NewsMonitor(bundleContext);
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
