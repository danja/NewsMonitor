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

import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.commons.feedparser.FeedList;
import org.apache.commons.feedparser.locate.FeedLocator;

/**
 *
 * @author <a href="mailto:burton@peerfear.org">Kevin A. Burton</a>
 * @version $Id$
 */
public class TestFeedLocator extends TestCase {
    protected String feedparserHome;
    
    public TestFeedLocator( String name ) throws Exception {
        super( name );
        
        feedparserHome = System.getProperty("feedparser.home", ".");
        // these come in as forward slashes, even on Windows, because of Ant,
        // so it is safe to check them as / rather than File.separator
        // Brad Neuberg
        if (feedparserHome.endsWith("/") == false)
            feedparserHome = feedparserHome + "/";
    }

    private void doTest( String resource ) throws Exception {

        System.out.println( "resource: " + resource );
        
        FeedList l = FeedLocator.locate( resource );

        Iterator it = l.iterator();

        // See if no links were found
        assertTrue("No links were found for " + resource, it.hasNext());
        System.out.println( "Atom: " + l.getAdAtomFeed() );
        System.out.println( "RSS: " + l.getAdRSSFeed() );
        
    }
    
    public void test1() throws Exception {
        String path = "file:" + feedparserHome;

        doTest( path + "tests/locate/locate1.html" );
        doTest( path + "tests/locate/locate2.html" );
        doTest( path + "tests/locate/locate3.html" );
        doTest( path + "tests/locate/locate4.html" );
        //doTest( path + "tests/locate/locate5.html" );
        doTest( path + "tests/locate/locate6.html" );
        doTest( path + "tests/locate/locate7.html" );
        doTest( path + "tests/locate/locate10.html" );
    }

    public static void main( String[] args ) throws Exception {

        TestFeedLocator test = new TestFeedLocator( null );
        
        //test.testGetWeblogLinkForResource();
        test.test1();

    }

}

