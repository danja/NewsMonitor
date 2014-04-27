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
 * Handles parsing Blogger.com changes.xml files.
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public class ChangesFeedParser {

    /**
     * Parse this feed.
     *
     * 
     */
    public static void parse( FeedParserListener listener,
                              org.jdom.Document doc ) throws FeedParserException {

        try {
                
            FeedParserState state = new FeedParserState();

            FeedVersion v = new FeedVersion();
            v.isChanges = true;
            listener.onFeedVersion( v );

            listener.init();

            FeedDirectoryParserListener fdpl = (FeedDirectoryParserListener)listener;

            //this should be the root directory.
            JDOMXPath xpath = new JDOMXPath( "/weblogUpdates/weblog" );
            List list = xpath.selectNodes( doc );

            Iterator i = list.iterator();
            while ( i.hasNext() ) {

                Element child = (Element)i.next();
                onWeblog( fdpl, state, child );
                
            }
            
            listener.finished();

        } catch ( Throwable t ) { throw new FeedParserException( t ); }

    }

    private static void onWeblog( FeedDirectoryParserListener listener,
                                  FeedParserState state,
                                  Element current ) throws Exception {
        
        String weblog = current.getAttributeValue( "url" );
        String description = current.getAttributeValue( "name" );
        String title = description;
        String feed = null;

        if ( weblog == null )
            weblog = feed;
        
        listener.onItem( state, title, weblog, description, feed );

    }

}

