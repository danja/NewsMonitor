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

package org.apache.commons.feedparser.test;

import junit.framework.TestCase;

import org.apache.commons.feedparser.DefaultFeedDirectoryParserListener;
import org.apache.commons.feedparser.FeedParser;
import org.apache.commons.feedparser.FeedParserException;
import org.apache.commons.feedparser.FeedParserFactory;
import org.apache.commons.feedparser.FeedParserListener;
import org.apache.commons.feedparser.FeedParserState;
import org.apache.commons.feedparser.network.ResourceRequest;
import org.apache.commons.feedparser.network.ResourceRequestFactory;

/**
 *
 * @author <a href="mailto:burton@peerfear.org">Kevin A. Burton</a>
 * @version $Id$
 */
public class TestFeedParser extends TestCase {

    public TestFeedParser( String name ) throws Exception {
        super( name );
    }

    public void testParseOPML() throws Exception {

        String resource = "http://weblog.infoworld.com/udell/gems/mySubscriptions.opml";
        
        FeedParser parser = FeedParserFactory.newFeedParser();

        FeedParserListener listener = new DefaultFeedDirectoryParserListener() {

                public void onItem( FeedParserState state,
                                    String title,
                                    String weblog,
                                    String description,
                                    String feed ) throws FeedParserException {

                    System.out.println( "title" + title );
                    System.out.println( "feed: " + feed );
                    
                }

                public void onItemEnd() {}

                public void finished() {}
                
            };
        
        listener.setContext( this );
        
        ResourceRequest request = ResourceRequestFactory.getResourceRequest( resource );
        
        parser.parse( listener, request.getInputStream(), resource );
            
    }

    public static void main( String[] args ) {

        try { 

            TestFeedParser test = new TestFeedParser( null );

            //test.testGetWeblogLinkForResource();
            test.testParseOPML();
            
        } catch ( Throwable t ) {
            
            t.printStackTrace();
            
        }
        
    }

}

