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

package org.apache.commons.feedparser.example;

import java.io.InputStream;
import java.util.Date;

import org.apache.commons.feedparser.DefaultFeedParserListener;
import org.apache.commons.feedparser.FeedParser;
import org.apache.commons.feedparser.FeedParserException;
import org.apache.commons.feedparser.FeedParserFactory;
import org.apache.commons.feedparser.FeedParserListener;
import org.apache.commons.feedparser.FeedParserState;
import org.apache.commons.feedparser.network.ResourceRequest;
import org.apache.commons.feedparser.network.ResourceRequestFactory;

/**
 * Example use of the FeedParser
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public class HelloFeedParser {

    public static void main( String[] args ) throws Exception {

        //create a new FeedParser...
        FeedParser parser = FeedParserFactory.newFeedParser();

        //create a listener for handling our callbacks
        FeedParserListener listener = new DefaultFeedParserListener() {

                public void onChannel( FeedParserState state,
                                       String title,
                                       String link,
                                       String description ) throws FeedParserException {

                    System.out.println( "Found a new channel: " + title );

                }

                public void onItem( FeedParserState state,
                                    String title,
                                    String link,
                                    String description,
                                    String permalink ) throws FeedParserException {

                    System.out.println( "Found a new published article: " + permalink );
                    
                }

                public void onCreated( FeedParserState state, Date date ) throws FeedParserException {
                    System.out.println( "Which was created on: " + date );
                }

            };

        //specify the feed we want to fetch

        String resource = "http://peerfear.org/rss/index.rss";

        if ( args.length == 1 )
            resource = args[0];

        System.out.println( "Fetching resource:" + resource );
        
        //use the FeedParser network IO package to fetch our resource URL
        ResourceRequest request = ResourceRequestFactory.getResourceRequest( resource );

        //grab our input stream
        InputStream is = request.getInputStream();

        //start parsing our feed and have the above onItem methods called
        parser.parse( listener, is, resource );

    }

}

