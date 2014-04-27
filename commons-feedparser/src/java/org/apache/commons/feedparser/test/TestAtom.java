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

import org.apache.commons.feedparser.DefaultFeedParserListener;
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
 * @version $Id: TestAtom.java 373622 2006-01-30 22:53:00Z mvdb $
 */
public class TestAtom extends TestCase {

    public TestAtom( String name ) throws Exception {
        super( name );
    }

    public void doTest( String resource ) throws Exception {

        FeedParser parser = FeedParserFactory.newFeedParser();

        FeedParserListener listener = new DefaultFeedParserListener() {

                public void onItem( FeedParserState state,
                                    String title,
                                    String weblog,
                                    String description,
                                    String permalink ) throws FeedParserException {

                    System.out.println( "title: " + title );
                    System.out.println( "description: " + description );
                    System.out.println( "permalink: " + permalink );
                    
                }

                public void onItemEnd() {}

                public void finished() {}

                // **** MetaFeedParserListener **********************************************
                
                public void onSubject( FeedParserState state, String content )
                    throws FeedParserException {

                    System.out.println( "subject: " + content );
                    
                }

                public void onContent( FeedParserState state,
                                       String type,
                                       String format,
                                       String encoding,
                                       String mode,
                                       String value ) throws FeedParserException {

                    System.out.println( "content (type): " + type );
                    System.out.println( "content (mode): " + mode );
                    System.out.println( "content (value): " + value );
                    
                }

            };
        
        listener.setContext( this );
        
        ResourceRequest request = ResourceRequestFactory.getResourceRequest( resource );
        
        parser.parse( listener, request.getInputStream(), resource );

    }

   public void test1() throws Exception {

       //String resource = "file:///projects/feedparser/src/java/org/apache/commons/feedparser/test/TestAtom.xml";
       
       //doTest( resource );
       doTest( "file:tests/feeds/atom-1.xml" );
    
   }

    public static void main( String[] args ) throws Exception {

        TestAtom test = new TestAtom( null );
        
        //test.testGetWeblogLinkForResource();
        test.test1();

    }

}

