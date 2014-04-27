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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.feedparser.FeedList;
import org.apache.commons.feedparser.locate.blogservice.BlogService;
import org.apache.commons.feedparser.locate.blogservice.Unknown;
import org.apache.commons.feedparser.network.ResourceRequest;
import org.apache.commons.feedparser.network.ResourceRequestFactory;
import org.apache.log4j.Logger;

/**
 * Locator which uses Link probing.  It also attempts to determine the type of
 * blog service provider it is dealing with, such as BlogSpot, Blogsxom, etc.,
 * in order to find feed URLs that are not specified through autodiscovery.
 * 
 * If ProbeLocator.AGGRESIVE_PROBING_ENABLED is true (by default it is false),
 * then we probe for links.
 * 
 * 
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
 */
public class ProbeLocator {

    private static Logger log = Logger.getLogger( ProbeLocator.class );

    /** If true, then we aggresively probe a site if it doesn't have
     *  autodiscovery.  This includes trying to determine what the blog provider
     *  is, trying individual locations based on a blog provider, and probing
     *  in several locations if the blog provider is unknown.
     * 
     *  The default value for this should be false.  This should only be 
     *  used on server-side aggregators that generate few requests, and 
     *  _never_ on client-side aggregators.  The level of traffic for 
     *  client-side aggregators would be too great.
     */
    public static boolean AGGRESIVE_PROBING_ENABLED = false;
    
    /** If true, then after discovering what a site's blog provider is we
     *  probe in select locations for feeds based on the provider.  This
     *  is useful if autodiscovery is not enabled on this blog and we don't
     *  want to do the full aggresive probing.
     * 
     *  The default value for this should be false.  This should only 
     *  be used on server-side aggregators that generate few requests, 
     *  and _never_ on client-side aggregators.  The level of traffic 
     *  for client-side aggregators would be too great.
     */
    public static boolean BLOG_SERVICE_PROBING_ENABLED = false;

    
    /**
     *
     * 
     */
    public static final List locate( String resource, String content, FeedList list )
        throws Exception {
        log.debug("ProbeLocator, resource="+resource+", list="+list);

        // determine what blog service we are dealing with
        BlogService blogService = BlogServiceDiscovery.discover( resource, content );  
        log.debug("blogService="+blogService);
        log.debug("blogService.hasValidAutoDiscovery="+blogService.hasValidAutoDiscovery());
        // fail-fast if we already have some results and if we determine that
        // we can trust the results (TextAmerica has invalid autodiscovery,
        // for example)
        if ( list.size() > 0 && blogService.hasValidAutoDiscovery() )
            return list;
        else if ( blogService.hasValidAutoDiscovery() == false ) {
            // clear out the list so far since we can't trust the results
            list.clear();
        }

        if ( BLOG_SERVICE_PROBING_ENABLED || AGGRESIVE_PROBING_ENABLED ) {
            log.debug("PROBING!!");
            List servicesToTry = new ArrayList();
            servicesToTry.add(blogService);
            // only try the Unknown service if we want aggresive probing
            if (AGGRESIVE_PROBING_ENABLED)
                servicesToTry.add(new Unknown());
            Iterator iter = servicesToTry.iterator();
            Set previousAttempts = new HashSet();
            
            while (iter.hasNext() && list.size() == 0) {
                BlogService currentService = (BlogService)iter.next();
                FeedReference[] mapping = currentService.getFeedLocations(resource, content);
                log.debug( "mapping = " + mapping );
            
                // try out each mapping
                for (int i = 0; i < mapping.length; i++) {
                    String baseFeedPath = currentService.getBaseFeedPath(resource);
                    String pathToTest ;
                    // build up our path to test differently if we are a
                    // relative or an exact path; needed because some
                    // blog services rewrite the domain name, such as
                    // Yahoo Groups
                    if (mapping[i].isRelative())
                        pathToTest = baseFeedPath + mapping[i].resource;
                    else
                        pathToTest = mapping[i].resource;
                    
                    log.debug( "pathToTest = " + pathToTest );

                    if ( !previousAttempts.contains( pathToTest ) 
                         && feedExists( pathToTest, currentService ) ) {
                        log.debug("Feed exists");
                        FeedReference feedReference = new FeedReference( pathToTest,
                                                                         mapping[i].type );
                        feedReference.method = FeedReference.METHOD_PROBE_DISCOVERY;       
                        previousAttempts.add( pathToTest );
                        onFeedReference( feedReference, list );
                    }
                
                    // record this attempt so we don't repeat it again if
                    // we are doing aggresive probing
                    previousAttempts.add( pathToTest );
                }
            }

            log.info( "Using aggresive probing, found the following:" );
            log.info( "Blog service: " + blogService );
        }

        log.info( "List: " + list );
        log.info( "RSS feed: " + list.getAdRSSFeed() );
        log.info( "Atom feed: " + list.getAdAtomFeed() );
        return list;

    }

    /**
     * Called each time we find a feed so that we can set the Ad method.
     * 
     * FIXME: This doesn't seem like the right place for this.  Can you
     * document this more? It's cryptic.  Brad Neuberg, bkn3@columbia.edu.
     * 
     */
    private static void onFeedReference( FeedReference ref, FeedList list ) {

        if ( list.getAdAtomFeed() == null &&
             FeedReference.ATOM_MEDIA_TYPE.equals( ref.type ) ) {

            list.setAdAtomFeed( ref );

        } else if ( list.getAdRSSFeed() == null &&
                    FeedReference.RSS_MEDIA_TYPE.equals( ref.type ) ) {

            list.setAdRSSFeed( ref );

        }

        list.add( ref );
        
    }

    /** Does an HTTP HEAD to see if the given resource exists.
     * 
     *  @param resource The full URI to the resource to check for.
     * 
     * 
     */
    protected static boolean feedExists( String resource,
                                         BlogService blogService) 
        throws Exception {
        
        log.debug("feedExists, resource="+resource);
        ResourceRequest request = ResourceRequestFactory.getResourceRequest( resource );

        request.setRequestMethod( "HEAD" );
        
        // Some services need to follow redirects; others block if you do.
        // Ask the blog service itself what to do.
        request.setFollowRedirects( blogService.followRedirects() );
        
        // the call below actually causes the connection to be made
        request.getContentLength();
        
        long response = request.getResponseCode();
        log.debug("response="+response);

        return response == 200;
    }
    
    

}
