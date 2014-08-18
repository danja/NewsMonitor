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
	private NewsMonitor newsmonitor = new NewsMonitor();

	@Override
	public void start(BundleContext bundleContext) throws Exception {

		registration = bundleContext.registerService(
				NewsMonitor.class.getName(), newsmonitor, null);
		newsmonitor.start(); // TODO do it properly
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {

		registration.unregister();
		newsmonitor.stop(); // TODO do it properly
	}
}
