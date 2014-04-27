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

import java.net.*;
import java.util.*;
import java.util.regex.*;

import org.apache.commons.feedparser.*;
import org.apache.commons.feedparser.locate.*;

/**
 * Models the different kinds of blog services that are available.  This
 * is needed for two reasons.  First, sometimes it is useful to simply
 * know what provider a given weblog is being hosted by, such as Blogger
 * or PMachine, in order to use special, non-standard capabilities.  Second,
 * many services have "quirks" that don't follow the standards, such as
 * supporting autodiscovery or supporting it in an incorrect way, and we
 * therefore need to know what service we are dealing with so that we
 * can find its feed.
 * 
 * The BlogService object encapsulates how to determine if a given
 * weblog is of that type and how to find its feeds.  Concrete subclasses,
 * such as org.apache.commons.feedparser.locate.blogservice.Blogger,
 * fill in this class and provide the actual way to determine these
 * things for each blog service type.
 * 
 * @author Brad Neuberg, bkn3@columbia.edu
 */
public abstract class BlogService {
    protected static List blogServices = new ArrayList();
    
    /** Subclasses should have a static block similar to the following:
     *  <code>
     *      {
     *          BlogService.addBlogService(new MyBlogService());
     *      }
     *  </code>
     */
    
    /** Locates all the generator meta tags
     *  (i.e. <meta content="generator" content="someGenerator"/>)
     */
    protected static Pattern metaTagsPattern = 
                Pattern.compile("<[\\s]*meta[\\w\\s=\"']*name=['\" ]generator[\"' ][\\w\\s=\"']*[^>]*",
                                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    
    /**
     * A regex to find any trailing filename and strip it
     */
    protected static Pattern patternToStrip = Pattern.compile("[^/](/\\w*\\.\\w*$)"); 
        
    /** Returns whether we can trust the results of this blog service's 
     *  autodiscovery links.  For example, TextAmerica returns invalid 
     *  autodiscovery results.
     */
    public abstract boolean hasValidAutoDiscovery();
    
    /** Returns whether we should follow HTTP redirects for this blog service.
     *  Some services don't implement HTTP redirects correctly, while others,
     *  like Xanga, require it.
     */
    public abstract boolean followRedirects();
    
    /** Determines if the weblog at the given resource and with the given
     *  content is this blog service.
     * @param resource A full URI to this resource, such as 
     * "http://www.codinginparadise.org".
     * @param content The full HTML content at the resource's URL.
     * @throws FeedParserException Thrown if an error occurs while 
     * determining the type of this weblog.
     */
    public abstract boolean isThisService(String resource, String content)
                                                throws FeedParserException;

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
    public abstract FeedReference[] getFeedLocations(String resource,
                                                     String content)
                                                throws FeedParserException;
    
    /** Determines if the weblog at the given resource is this blog service.
     *  @param resource A full URI to this resource, such as 
     *  "http://www.codinginparadise.org".
     *  @throws FeedParserException Thrown if an error occurs while 
     *  determining the type of this weblog.
     */
    public boolean isThisService(String resource) throws FeedParserException {
        return isThisService(resource, null);
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

        Matcher fileMatcher = patternToStrip.matcher(resource);
        if (fileMatcher.find()) {
            String stringToStrip = fileMatcher.group(1);
            int startStrip = resource.indexOf(stringToStrip);
            resource = resource.substring(0, startStrip);
        }
        
        if ( ! resource.endsWith( "/" ) ) {
            resource = resource + "/";
        }
        
        return resource;
    }

    public String toString() {
        return this.getClass().getName();
    }
    
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        
        if (obj instanceof BlogService == false)
            return false;
        
        return (obj.getClass().equals(this.getClass()));
    }
    
    public int hashCode() {
        return this.getClass().hashCode();
    }
    
    /** Gets an array of all of the available BlogService implementations. */
    public static BlogService[] getBlogServices() {
        if (blogServices.size() == 0)
            initializeBlogServices();
        
        BlogService[] results = new BlogService[blogServices.size()];
        
        return (BlogService[])blogServices.toArray(results);
    }

    // **** util code ***********************************************************
    // These methods are useful for non-abstract subclasses of this object
    // to actually implement their functionality.
    
    /** Determines if the given resource contains the given domain name
     *  fragment.
     */
    protected boolean containsDomain(String resource, String domain) {
        return (resource.indexOf(domain) != -1);
    }
    
    /**
     * Determines if the given content was generated by the given generator.

     * Example. This document contains a meta tag with name="generator" and
     * content equal to the generatorType).
     */
    protected boolean hasGenerator(String content, String generatorType) {
        if (content == null) {
            return false;
        }
        
        Matcher metaTagsMatcher = metaTagsPattern.matcher(content);
        if (metaTagsMatcher.find()) {
            String metaTag = metaTagsMatcher.group(0).toLowerCase();
            generatorType = generatorType.toLowerCase();
            return (metaTag.indexOf(generatorType) != -1);
        }
        else {
            return false;
        }
    }
    
    protected static void initializeBlogServices() {
        blogServices.add(new AOLJournal());
        blogServices.add(new Blogger());
        blogServices.add(new Blosxom());
        blogServices.add(new DiaryLand());
        blogServices.add(new ExpressionEngine());
        blogServices.add(new Flickr());
        blogServices.add(new GreyMatter());
        blogServices.add(new iBlog());
        blogServices.add(new LiveJournal());
        blogServices.add(new Manila());
        blogServices.add(new MovableType());
        blogServices.add(new PMachine());
        blogServices.add(new RadioUserland());
        blogServices.add(new TextAmerica());
        blogServices.add(new TextPattern());
        blogServices.add(new Typepad());
        blogServices.add(new WordPress());
        blogServices.add(new Xanga());
        blogServices.add(new YahooGroups());
    }
}
