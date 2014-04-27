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

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.apache.commons.feedparser.FeedParser;
import org.apache.commons.feedparser.FeedParserFactory;
import org.apache.commons.feedparser.FeedParserListener;
import org.apache.commons.feedparser.impl.DebugFeedParserListener;

/**
 *
 * @author <a href="mailto:burton@peerfear.org">Kevin A. Burton</a>
 * @version $Id: TestFeedParserUTF8.java 373622 2006-01-30 22:53:00Z mvdb $
 */
public class TestFeedParserUTF8 extends TestCase {

    public TestFeedParserUTF8( String name ) throws Exception {
        super( name );
    }

    public void testAssertXerces() throws Exception {

        String v;
        
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        v = db.getClass().getName();
        
        if ( ! v.equals( "org.apache.xerces.jaxp.DocumentBuilderImpl" ) )
            throw new Exception( "Incorrect version of Xerces: " + v );

        v = org.apache.xerces.impl.Version.fVersion;
        
        if ( v.indexOf( "2.6" ) == -1 )
            throw new Exception( "Incorrect version of Xerces: " + v);

    }

    public void test( String path ) throws Exception {

        FeedParser parser = FeedParserFactory.newFeedParser();

        FeedParserListener listener = new DebugFeedParserListener();

        parser.parse( listener,
                      new FileInputStream( "src/java/org/apache/commons/feedparser/test/" + path),
                      path );

    }

    public void test1() throws Exception {

        test( "TestFeedParserUTF8.rss" );

    }
        
    public void test2() throws Exception {

        test( "broken-UTF8-feed.rss" );
    }

    public void test3() throws Exception {

        test( "broken-Invalid-byte-2-of-3-byte-UTF-8-sequence.xml" );
    }

    public void test4() throws Exception {

        test( "broken-salon.rdf" );
    }

    public static void main( String[] args ) throws Exception {

        TestFeedParserUTF8 test = new TestFeedParserUTF8( null );

        //test.testGetWeblogLinkForResource();
        //test.testParse();
        test.test2();
        
    }

}

