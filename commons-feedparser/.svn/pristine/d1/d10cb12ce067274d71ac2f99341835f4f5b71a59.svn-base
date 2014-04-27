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

package org.apache.commons.feedparser.locate.blogservice;

import org.apache.commons.feedparser.FeedParserException;
import org.apache.commons.feedparser.locate.*;

/**
 * Models an unknown blog service, encapsulating whether a given weblog
 * is this type of service and where it usually keeps its feeds.
 * 
 * @author Brad Neuberg, bkn3@columbia.edu
 */
public class Unknown extends BlogService {
    
    /** Returns whether we can trust the results of this blog service's 
     *  autodiscovery links.  For example, TextAmerica returns invalid 
     *  autodiscovery results.
     */
    public boolean hasValidAutoDiscovery() {
        return true;
    }
    
    /** Returns whether we should follow HTTP redirects for this blog service.
     *  Some services don't implement HTTP redirects correctly, while others,
     *  like Xanga, require it.
     */
    public boolean followRedirects() {
        return false;
    }
    
    /** Determines if the weblog at the given resource and with the given
     *  content is this blog service.
     * @param resource A full URI to this resource, such as 
     * "http://www.codinginparadise.org".
     * @param content The full HTML content at the resource's URL.
     * @throws FeedParserException Thrown if an error occurs while 
     * determining the type of this weblog.
     */
    public boolean isThisService(String resource, String content)
                                                throws FeedParserException {
        return false;
    }

    /**
     * Returns an array of FeedReferences that contains information on the
     * usual locations this blog service contains its feed.  The feeds should
     * be ordered by quality, so that higher quality feeds come before lower
     * quality ones (i.e. you would want to have an Atom FeedReference
     * object come before an RSS 0.91 FeedReference object in this list).
     * @param resource A URL to the given weblog that might be used to build
     * up where feeds are usually located.
     * @param content The full content of the resource URL, which might
     * be useful to determine where feeds are usually located.  This can be
     * null.
     * @throws FeedParserException Thrown if an error occurs while trying
     * to determine the usual locations of feeds for this service.
     */
    public FeedReference[] getFeedLocations(String resource,
                                            String content)
                                                throws FeedParserException {
        FeedReference unknownLocations[] =
            { new FeedReference("atom.xml",FeedReference.ATOM_MEDIA_TYPE),
              new FeedReference("index.rss", FeedReference.RSS_MEDIA_TYPE),
              new FeedReference("rss.xml", FeedReference.RSS_MEDIA_TYPE),
              new FeedReference("index.rdf", FeedReference.RSS_MEDIA_TYPE), 
              new FeedReference("index.xml", FeedReference.RSS_MEDIA_TYPE),
              new FeedReference("xml/rss.xml", FeedReference.RSS_MEDIA_TYPE) };
        
        return unknownLocations;
    }
}
