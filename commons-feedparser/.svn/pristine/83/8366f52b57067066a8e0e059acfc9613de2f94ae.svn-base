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

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.commons.feedparser.FeedParser;
import org.apache.commons.feedparser.FeedParserFactory;
import org.apache.commons.feedparser.impl.DebugFeedParserListener;

/**
 *
 * @author <a href="mailto:burton@peerfear.org">Kevin A. Burton</a>
 * @version $Id$
 */
public class TestFeedFilter extends TestCase {
    protected String feedparserHome;
    
    public static int current = 0;

    public TestFeedFilter( String name ) throws Exception {
        super( name );
        
        feedparserHome = System.getProperty("feedparser.home", ".");
        // these come in as forward slashes, even on Windows, because of Ant,
        // so it is safe to check them as / rather than File.separator
        // Brad Neuberg
        if (feedparserHome.endsWith("/") == false)
            feedparserHome = feedparserHome + "/";
    }

    private void doTest( String resource ) throws Exception {

        System.out.println( "resource: (" + current + ") " + resource );

        URL url = new URL( resource );

        FileOutputStream fos = new FileOutputStream( "/tmp/test-feed-filter-" + current + ".html" );

        PrintStream out = new PrintStream( fos, true, "UTF-8" );

        out.println( "<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\"> " );
        out.println( "<pre>" );

        DebugFeedParserListener listener = new DebugFeedParserListener( out );

        FeedParser parser=FeedParserFactory.newFeedParser();
        parser.parse( listener, url.openStream(), resource );

        out.println( "</pre>" );

        ++current;

    }

    public void test1() throws Exception {
        String path = "file:" + feedparserHome;

        doTest( path + "tests/feeds/rss-1.0-EUC-JP.rdf" );

        doTest( path + "tests/filter/nbsp-1.xml" );

        doTest( path + "tests/filter/entity-atom-1.xml" );

        doTest( path + "tests/filter/prolog-atom-1.xml" );
        doTest( path + "tests/filter/prolog-atom-2.xml" );
        doTest( path + "tests/filter/prolog-opml-1.xml" );

        doTest( path + "tests/filter/lisa.opml" );

        doTest( path + "tests/feeds/utf16.rss1" );
        doTest( path + "tests/feeds/utf16.rss2" );
        doTest( path + "tests/feeds/i18n.atom" );
        doTest( path + "tests/feeds/utf16.atom" );

        doTest( path + "tests/feeds/atom-1.xml" );
        doTest( path + "tests/feeds/rss-1.0-EUC-JP.rdf" );
        doTest( path + "tests/feeds/rss-1.0-international-1.rdf" );

    }

    public static void main( String[] args ) throws Exception {

        TestFeedFilter test = new TestFeedFilter( null );

        //test.testGetWeblogLinkForResource();
        test.test1();

    }

}

