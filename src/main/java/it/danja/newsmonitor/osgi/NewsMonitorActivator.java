/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.danja.newsmonitor.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import it.danja.newsmonitor.main.*;
//import com.bw.osgi.provider.able.HelloWorldService;
//import com.bw.osgi.provider.impl.HelloWorldServiceImpl;

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
                NewsMonitor.class.getName(),
                newsmonitor,
                null);
        newsmonitor.start(null); // TODO do it properly
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
    	
        registration.unregister();
        newsmonitor = null; // TODO do it properly
    }
}  

