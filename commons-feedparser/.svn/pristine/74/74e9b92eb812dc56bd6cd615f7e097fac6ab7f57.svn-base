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


/**
 *
 * @author <a href="mailto:burton@peerfear.org">Kevin A. Burton</a>
 * @version $Id: TestComments.java 155416 2005-02-26 13:00:10Z dirkv $
 */
public class TestBrokenFeeds extends BaseTestCase {

    public TestBrokenFeeds( String name ) throws Exception {
        super( name );
    }

    /**
     * Jeremy Zawodny's RSS feed isn't including content for some reason.  We
     * need to fix this.
     *
     * 
     */
    public void testNoContent1() throws Exception {

        String resource = "file:tests/feeds/zawodny-broken-content.rss2";

        String output = captureOutputFromTest( resource );

        System.out.println( "output: " + output );

        assertTrue( output.indexOf( "onContent" ) != -1 );
        
    }

    public static void main( String[] args ) throws Exception {

        TestBrokenFeeds test = new TestBrokenFeeds( null );
        
        //test.testGetWeblogLinkForResource();
        test.testNoContent1();

    }

}
