/*
 * Copyright 1999,2004 The Apache Software Foundation.
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

package org.apache.commons.feedparser.locate;

import java.util.regex.Pattern;

/**
 * <p>
 * A FeedReference is used within the RSS/Atom location facility to pass back
 * metadata for feed discoveries.
 *
 * <p>
 * Right now we pass back the URL to the feeds as the `resource' parameter.  The
 * media type (application/rss+xml, application/atom+xml, etc) as the type param
 * (which is optional).  We also pass back the `title' back if its specified
 * within autodiscovery.  This will be null if another discovery method is used.
 *
 * <p> Its important to realize that the media type is only given if we're 100%
 * certain of the value.  If we have to use probe discovery or link discovery it
 * might not be possible to obtain the media type without antoher network
 * request (via HTTP headers).
 * 
 * <p> Note that internally (within the ProbeLocator) we don't use absolute
 * resource URLs but use relative ones and use the media type as the default
 * media type.
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
 */
public class FeedReference {

    public static final String ATOM_MEDIA_TYPE = "application/atom+xml";
    public static final String RSS_MEDIA_TYPE  = "application/rss+xml";
    public static final String XML_MEDIA_TYPE  = "text/xml";

    public static int METHOD_AUTO_DISCOVERY  = 1;
    public static int METHOD_PROBE_DISCOVERY = 2;
    public static int METHOD_LINK_DISCOVERY  = 4;
    
    /**
     * The network addressable resource forfor this feed.
     */
    public String resource = null;

    /**
     * The media type of this feed.
     */
    public String type = null;

    /**
     * The title of the reference.  Usually FOAF, RSS or Atom per auto-discovery
     * link.
     */
    public String title = null;

    /**
     * The method of discovery... 
     */
    public int method = 0;
    
    protected Pattern schemePattern = Pattern.compile("^[^:/]*:/.*$");
    
    public FeedReference( String resource, String type ) {
        this.resource = resource;
        this.type = type;
    }

    public String toString() {
        return resource;
    }
    
    public boolean equals(Object obj) {
        if (obj == null || (obj instanceof FeedReference) == false)
            return false;
        
        FeedReference compareMe = (FeedReference)obj;
        
        if (resource.equals(compareMe.resource)) {
            //ignore title and type when doing equality
            return true;
        }
        
        return false;
    }
    
    /** Determines if the resource given by this FeedReference is relative.
     *  For example, the resource could be '/atom.xml', which is relative.
     *  It could also be 
     *  "http://rss.groups.yahoo.com/group/talkinaboutarchitecture/rss".
     */
    public boolean isRelative() {
        if (resource == null)
            return true;
        
        // look for a scheme:/
        return !schemePattern.matcher(resource).matches();
        
    }
    
}
