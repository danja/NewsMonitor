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

import java.util.Iterator;

import org.apache.commons.feedparser.FeedList;
import org.apache.commons.feedparser.network.ResourceRequest;
import org.apache.commons.feedparser.network.ResourceRequestFactory;
import org.apache.log4j.Logger;

/**
 * Method to determine feed URLs from a given resource URI.  For example,
 * you would pass in the URI:
 * 
 * http://www.codinginparadise.org
 * 
 * and this class would pass back a List with one address of the feed URL,
 * which is
 * 
 * http://www.codinginparadise.org/weblog/atom.xml"
 *
 * <code>
 * String resource = "http://www.codinginparadise.org";
 * FeedList l = FeedLocator.locate( resource );
 * </code>
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
 */
public class FeedLocator {

    private static Logger log = Logger.getLogger( FeedLocator.class );
    
    /**
     * Locate all feeds within the given resource.  The resource should be a link
     * to an (X)HTML document, usually a weblog or a website.
     * 
     * Example: http://peerfear.org
     *
     * @param resource The weblog we need to discover
     * 
     */
    public static final FeedList locate( String resource ) throws Exception {
        // \: Use my network library when it's migrated into Apache.
        
        //fetch content
        ResourceRequest request = ResourceRequestFactory.getResourceRequest( resource );

        String content = request.getInputStreamAsString();

        //return resources
        return locate( resource, content );
        
    }

    /**
     * Locate the feed with the given content.
     *
     * 
     */
    public static final FeedList locate( String resource, String content ) throws Exception {

        log.info( "Locating " + resource + "..." );
        
        FeedList list = new FeedList();

        //FIXME: if we were GIVEN an RSS/Atom/OPML/etc file then we should just
        //attempt to use this and return a FeedList with just one entry.  Parse
        //it first I think to make sure its valid XML and then move forward.
        //The downside here is that it would be wasted CPU if its HTML content.
        
        log.debug( "Using DiscoveryLocator..." );
        DiscoveryLocator.locate( resource, content, list );
        log.debug("after discoverylocator, list="+list);

        log.debug( "Using LinkLocator..." );
        //this failed... try looking for links
        LinkLocator.locate( resource, content, list );
        log.debug("after linklocator, list="+list);

        //this failed... try probe location.  This is more reliable than
        //LinkLocation but requires a few more HTTP gets.
        log.debug( "Using ProbeLocator..." );
        ProbeLocator.locate( resource, content, list );
        log.debug("after probelocator, list="+list);
        
        log.info( "After locating, list="+list );
            
        return list;
        
    }

    public static void main( String[] args ) throws Exception {

        //This should find http://www.electoral-vote.com/index.rss
        //String resource = "http://brendonwilson.com/";

        //String resource = "file:///projects/feedparser/tests/locate4.html";
        //String resource = "file:///projects/feedparser/tests/locate5.html";
        //String resource = "file:///projects/feedparser/tests/locate6.html";

        //FIXME: add UNIT TESTS for Yahoo Groups and Flickr

        String resource = "http://craigslist.org/w4m/";
        
        //String resource = "http://groups.yahoo.com/group/aggregators/";

        //String resource = "http://flickr.com/photos/tags/cats";

        //String resource = "file:///projects/feedparser/tests/locate8.html";

        //String resource = "http://blogs.sun.com/roller/page/gonzo";

        //String resource = "http://gonze.com/weblog/";

        //String resource = "http://codinginparadise.org/";

        //        String resource = "http://bucsfishingreport.com/pMachine/weblog.php";
        
        //String resource = "http://www.livejournal.com/community/indiexiankids/";
//String resource= "http://www.thealarmclock.com/mt/";
        
        //String resource = "http://guinness.joeuser.com";
        
        //String resource = "http://georgewbush.com/blog";

        //String resource = "http://carolinascl.blogspot.com/";
        
        //String resource = "http://www.corante.com/strange/";
        //String resource = "http://peerfear.org";

        ProbeLocator.BLOG_SERVICE_PROBING_ENABLED = true;
        ProbeLocator.AGGRESIVE_PROBING_ENABLED = true;

        FeedList l = locate( resource );

        Iterator it = l.iterator();

        if ( it.hasNext() == false ) {
            System.out.println( "NO LINKS FOUND" );
        } 

        System.out.println( "AD RSS: " + l.getAdRSSFeed() );
        System.out.println( "AD Atom: " + l.getAdAtomFeed() );
        
        while ( it.hasNext() ) {

            FeedReference ref = (FeedReference)it.next();

            System.out.println( ref.resource );
            
        }

    }

}
