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

import java.lang.reflect.Method;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
 * @version $Id: TestPerformance.java 373622 2006-01-30 22:53:00Z mvdb $
 */
public class TestPerformance extends TestCase {

    public TestPerformance( String name ) {
        super( name );
    }

    static SAXParser parser = null;

    public static void testSAX() throws Exception {

        if ( parser == null ) {
            parser = SAXParserFactory.newInstance().newSAXParser();

            //need to enable SAX2 locals and namespace
            parser.getXMLReader().setFeature( "http://xml.org/sax/features/namespaces", true );
        }

         org.apache.commons.feedparser.sax.RSSFeedParser handler =
              new org.apache.commons.feedparser.sax.RSSFeedParser();

         handler.listener = new DefaultFeedParserListener() {

                 public void onChannel( FeedParserState state,
                                        String title,
                                        String link,
                                        String description ) throws FeedParserException {

//                      System.out.println( "onChannel: title: " + title );
                    
                 }

                 public void onItem( FeedParserState state,
                                     String title,
                                     String link,
                                     String description,
                                     String permalink ) throws FeedParserException {

//                     System.out.println( "onItem: title: " + title );
                    
                 }

                 public void onItemEnd() throws FeedParserException {

//                     System.out.println( "onItemEnd");

                 }

             };

        String resource = "file:/home/burton/index.rss";
        
        ResourceRequest request = ResourceRequestFactory
            .getResourceRequest( resource );

        parser.parse( request.getInputStream(), handler );

    }

    public static void testDefault() throws Exception {

        FeedParser parser = FeedParserFactory.newFeedParser();
        FeedParserListener listener = new DefaultFeedParserListener() {};

        String resource = "file:/home/burton/index.rss";
        
        ResourceRequest request = ResourceRequestFactory
            .getResourceRequest( resource );
        
        parser.parse( listener,
                      request.getInputStream(),
                      resource );

    }

    public static void main( String[] args ) throws Exception {

        TestPerformance test = new TestPerformance( null );
        
        //test.testGetWeblogLinkForResource();
        //test.test1();

        doTestMethod( "testSAX", TestPerformance.class, 100 );
        doTestMethod( "testDefault", TestPerformance.class, 100 );
        
    }

    public static void  doTestMethod( String name, Class clazz, int max ) throws Exception {

        Method method = clazz.getMethod( name, null );

        System.out.println( "Testing method: " + name );

        long duration = 0;
        
        for ( int i = 0; i <= max; ++i ) {

            long before = System.currentTimeMillis();
            
            method.invoke( null, null );

            if ( i == 0 )
                continue; //don't measure the first call

            long after = System.currentTimeMillis();
            duration += after-before;
            
        }

        System.out.println( "----------------" );
        System.out.println( "Total parse count: " + max );

        System.out.println( "Total duration: " + duration + "  milliseconds" );

        float totalAvgDuration = (float)duration / (float)max;

        System.out.println( "Total avg duration: " + totalAvgDuration + "  milliseconds" );

        float totalPerSecond = 1000 / totalAvgDuration;

        System.out.println( "Total per second: " + totalPerSecond );

    }

}

