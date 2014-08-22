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
	private NewsMonitor newsmonitor;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		newsmonitor = new NewsMonitor(bundleContext);
		registration = bundleContext.registerService(
				NewsMonitor.class.getName(), newsmonitor, null);
		// newsmonitor.setBundleContext(bundleContext);
		newsmonitor.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {

		registration.unregister();
		newsmonitor.stop(); // TODO do it properly
	}
}
