/*
 * Copyright 2014 danny.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.danja.newsmonitor.osgi.templating;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.cache.URLTemplateLoader;
import it.danja.newsmonitor.main.Config;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 *
 * @author danny
 */
public class BundleURLTemplateLoader extends URLTemplateLoader {
    
     private static Logger log = LoggerFactory.getLogger(BundleURLTemplateLoader.class);
private BundleContext bundleContext;

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }
    
    @Override
    protected URL getURL(String string) {
            Bundle bundle = bundleContext.getBundle();
        return bundle.getResource(string);
//    } catch (IOException ex) {
//        log.error(ex.getMessage());
//    }
        
    }
    
}
