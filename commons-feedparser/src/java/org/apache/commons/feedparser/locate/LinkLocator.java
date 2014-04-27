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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.feedparser.FeedList;

/**
 * Find links by parsing the raw HTML.  We only return links that are on the
 * same site and link to /index.rdf LINKS and so forth.
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
 */
public class LinkLocator {

    /**
     * 
     *
     * 
     */
    public static final List locate( String resource,
                                     String content,
                                     final FeedList list )
        throws Exception {

        /**
         * When we have been given feeds at a higher level (via <link rel> tags
         * we should prefer these.
         */
        final boolean hasExplicitRSSFeed = list.getAdRSSFeed() != null;
        final boolean hasExplicitAtomFeed = list.getAdRSSFeed() != null;

        AnchorParserListener listener = new AnchorParserListener() {

                String resource = null;
                
                String site = null;

                HashSet seen = new HashSet();

                boolean hasFoundRSSFeed = false;
                boolean hasFoundAtomFeed = false;
                
                public void setContext( Object context ) {

                    resource = (String)context;
                    
                    //pass in the resource of the blog
                    site = getSite( resource );
                    
                }

                public Object getResult() {
                    return list;
                }

                public boolean onAnchor( String href, String rel, String title ) {
                    String current = ResourceExpander.expand( resource, href );
                    if ( current == null )
                        return true; //obviously not

                    //FIXME: if it's at the same directory level we should prioritize it.
                    //for example:
                    //
                    // http://peerfear.org/blog/
                    //
                    // http://peerfear.org/blog/index.rdf
                    //
                    // instead of 
                    //
                    // http://peerfear.org/index.rdf

                    //see if the link is on a different site
                    if ( ! site.equals( getSite( current ) ) ) {
                        return true;
                    }

                    //Radio style feed.  Screw that.
                    //FIXME: What happens if the Feed Parser is used to
                    //aggregate feeds on the localhost? This will break that.
                    //Brad Neuberg, bkn3@columbia.edu
                    if ( current.startsWith( "http://127" ) ) 
                        return true;

                    if ( seen.contains( current ) ) {
                        return true;
                    } 

                    seen.add( current );

                    //FIXME: we should assert tha that these feeds are from the SAME
                    //domain not a link to another feed.

                    boolean isRSSLink = current.endsWith( ".rss" );

                    //support ROLLER RSS links and explicit link discovery by
                    //non-extensions.
                    if ( isRSSLink == false ) {

                        isRSSLink =
                            title != null &&
                            title.equalsIgnoreCase( "rss" ) &&
                            href.indexOf( "rss" ) != -1;

                    } 

                    if ( isRSSLink ) {

                        //this is an RSS feed.
                        FeedReference ref = new FeedReference( current,
                                                               FeedReference.RSS_MEDIA_TYPE );

                        
                        //make sure we haven't already discovered this feed
                        //through a different process
                        if (list.contains(ref))
                            return true;

                        //Make sure to preserve existing AD feeds first.
                        if ( ! hasExplicitRSSFeed )
                            list.setAdRSSFeed( ref );

                        list.add( ref );

                        hasFoundRSSFeed = true;
                        
                    }

                    if ( current.endsWith( ".atom" ) ) {

                        FeedReference ref = new FeedReference( current,
                                                               FeedReference.RSS_MEDIA_TYPE );

                        //make sure we haven't already discovered this feed
                        //through a different process
                        if (list.contains(ref))
                            return true;
                        
                        //Make sure to preserve existing AD feeds first.
                        if ( ! hasExplicitAtomFeed )
                            list.setAdAtomFeed( ref );

                        list.add( ref );

                        hasFoundAtomFeed = true;

                    }

                    if ( current.endsWith( ".xml" ) ||
                         current.endsWith( ".rdf" ) ) {

                        //NOTE that we do allow autodiscovery forfor index.xml
                        //and index.rdf files but we don't prefer them since
                        //these extensions are generic.  We would prefer to use
                        //index.rss or even Atom (though people tend to use Atom
                        //autodiscovery now).  This is important because if we
                        //spit back an index.xml file thats NOT RSS or worse an
                        //index.rdf file thats FOAF then we might break callers.

                        FeedReference ref = new FeedReference( current,
                                                               FeedReference.RSS_MEDIA_TYPE );
                        
                        //make sure we haven't already discovered this feed
                        //through a different process
                        if (list.contains(ref))
                            return true;

                        //see if we should RESORT to using this.

                        if ( ! hasExplicitRSSFeed && ! hasFoundRSSFeed ) {

                            //NOTE: when we have found an existing RDF file use
                            //that instead..  This is probably RSS 1.0 which is
                            //much better than RSS 0.91

                            if ( list.getAdRSSFeed() == null ||
                                 list.getAdRSSFeed().resource.endsWith( ".rdf" ) == false ) {

                                list.setAdRSSFeed( ref );

                            }

                        }

                        //feed for this blog.
                        list.add( ref );
                        return true;
                        
                    } 

                    //for coderman's blog at http://www.peertech.org
                    //FIXME: This is a hack, Brad Neuberg, bkn3@columbia.edu
                    if ( current.endsWith( "/node/feed" ) )
                        list.add( current );

                    return true;
                    
                }

            };

        listener.setContext( resource );
        AnchorParser.parseAnchors( content, listener );
        
        return list;
        
    }

    public static String getSite( String resource ) {

        try {

            String site = new URL( resource ).getHost();
            return site.replaceAll( "http://www", "http://" );
            
        } catch ( MalformedURLException e ) {
            return null;
        }
        
    }

}
