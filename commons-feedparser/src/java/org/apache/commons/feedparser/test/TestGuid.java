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
 * @version $Id: TestGuid.java 373622 2006-01-30 22:53:00Z mvdb $
 */
public class TestGuid extends BaseTestCase {

    public TestGuid( String name ) throws Exception {
        super( name );
    }

    public void test1() throws Exception {

        String resource = "file:tests/feeds/rss-2.0-guid.rss";

        String output = captureOutputFromTest( resource );

        if ( output.indexOf( "onGUID: " ) == -1 )
            throw new Exception();

    }

    public static void main( String[] args ) throws Exception {

        TestGuid test = new TestGuid( null );
        
        //test.testGetWeblogLinkForResource();
        test.test1();

    }

}
