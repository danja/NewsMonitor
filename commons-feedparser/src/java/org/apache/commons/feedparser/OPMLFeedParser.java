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

package org.apache.commons.feedparser;

import java.util.Iterator;
import java.util.List;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Element;

/**
 * Handles parsing OPML.
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: OPMLFeedParser.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class OPMLFeedParser {

    /**
     * Parse this feed.
     *
     * 
     */
    public static void parse( FeedParserListener listener,
                              org.jdom.Document doc ) throws FeedParserException {

        try {
                
            FeedParserState state = new FeedParserState();

            //will result in an incorrect interface if the caller isn't using the
            //system correctly.

            FeedVersion v = new FeedVersion();
            v.isOPML = true;
            listener.onFeedVersion( v );

            listener.init();

            FeedDirectoryParserListener fdpl = (FeedDirectoryParserListener)listener;

            //this should be the root directory.
            JDOMXPath xpath = new JDOMXPath( "/opml/body/outline" );
            List list = xpath.selectNodes( doc );

            Iterator i = list.iterator();
            while ( i.hasNext() ) {

                Element child = (Element)i.next();
                onOutline( fdpl, state, child );
                
            }
            
            listener.finished();

        } catch ( Throwable t ) { throw new FeedParserException( t ); }

    }

    private static void onOutlineFolder( FeedDirectoryParserListener listener,
                                         FeedParserState state,
                                         Element current ) throws Exception {

        JDOMXPath xpath = new JDOMXPath( "outline" );
        List list = xpath.selectNodes( current );

        Iterator i = list.iterator();
        while ( i.hasNext() ) {

            Element child = (Element)i.next();
            onOutline( listener, state, child );
                
        }

    }
        
    private static void onOutline( FeedDirectoryParserListener listener,
                                   FeedParserState state,
                                   Element current ) throws Exception {

        String title = current.getAttributeValue( "title" );

        if ( title == null )
            title = current.getAttributeValue( "text" );

        String weblog = current.getAttributeValue( "htmlUrl" );
        String description = current.getAttributeValue( "description" );
        String feed = current.getAttributeValue( "xmlUrl" );

        if ( weblog == null )
            weblog = feed;
        
        if ( feed != null ) {
            listener.onItem( state, title, weblog, description, feed );
        } else if ( title != null ) {
            //this is almost certainly a folder
            
            listener.onFolder( state, title );
            onOutlineFolder( listener, state, current );
            listener.onFolderEnd();
                    
        }

    }

}

