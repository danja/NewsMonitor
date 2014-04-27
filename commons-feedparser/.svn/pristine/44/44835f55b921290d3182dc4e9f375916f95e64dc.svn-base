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

import java.net.MalformedURLException;
import java.util.regex.*;

import org.apache.commons.feedparser.FeedParserException;
import org.apache.commons.feedparser.locate.*;

/**
 * Models the Blosxom blog service, encapsulating whether a given weblog
 * is this type of service and where it usually keeps its feeds.
 * 
 * @author Brad Neuberg, bkn3@columbia.edu
 */
public class Blosxom extends BlogService {
    
    /** A pattern used to discover Blosxom blogs. */
    private static Pattern blosxomPattern =
                Pattern.compile("alt=[\"' ]powered by blosxom[\"' ]",
                                Pattern.CASE_INSENSITIVE);
        
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
        boolean results = false;
        
        // This is the only kind of blog that we need to check for a 
        // 'Powered by Blosxom'.  We do this with the alt= value on the
        // Powered By image.
        // FIXME: This might be fragile, but it is used across all of the
        // Blosxom blogs I have looked at so far. Brad Neuberg, bkn3@columbia.edu
        
        Matcher blosxomMatcher = blosxomPattern.matcher(content);
        results = blosxomMatcher.find();
        
        return results;
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
        // there is sometimes an index.rss20 file, but Blosxom has a bug where
        // it incorrectly responds to HTTP HEAD requests for that file,
        // saying that it exists when it doesn't.  Most sites don't seem
        // to have this file so we don't include it here. 
        // Brad Neuberg, bkn3@columbia.edu
        FeedReference[] blosxomLocations = 
            { new FeedReference("index.rss", FeedReference.RSS_MEDIA_TYPE) };
        
        return blosxomLocations;
    }
    
    /** This method takes a resource, such as "http://www.codinginparadise.org/myweblog.php",
     *  and gets the path necessary to build up a feed, such as 
     *  "http://www.codinginparadise.org/".  Basicly it appends a slash 
     *  to the end if there is not one, and removes any file names that 
     *  might be at the end, such as "myweblog.php".
     *
     *  There is a special exception for some Blosxom blogs,
     *  which have things inside of a cgi-script and 'hang' their RSS files
     *  off of this cgi-bin.  For example, 
     *  http://www.bitbucketheaven.com/cgi-bin/blosxom.cgi has its RSS file
     *  at http://www.bitbucketheaven.com/cgi-bin/blosxom.cgi/index.rss, so
     *  we must return the blosxom.cgi at the end as well for this method.
     * 
     *  @throws MalformedURLException Thrown if the given resource's URL is 
     *  incorrectly formatted.
     */
    public String getBaseFeedPath( String resource ) {
        
        // strip off any query string or anchors
        int end = resource.lastIndexOf( "#" );
        
        if ( end != -1 )
            resource = resource.substring( 0, end );

        end = resource.lastIndexOf( "?" );

        if ( end != -1 )
            resource = resource.substring( 0, end );
        
        if ( ! resource.endsWith( "/" ) ) {
            resource = resource + "/";
        }
        
        return resource;
    }
}
