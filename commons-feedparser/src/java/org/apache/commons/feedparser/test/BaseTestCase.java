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

import org.apache.commons.feedparser.FeedParser;
import org.apache.commons.feedparser.FeedParserFactory;
import org.apache.commons.feedparser.FeedParserListener;
import org.apache.commons.feedparser.impl.CaptureOutputFeedParserListener;
import org.apache.commons.feedparser.network.ResourceRequest;
import org.apache.commons.feedparser.network.ResourceRequestFactory;

/**
 *
 * @author <a href="mailto:burton@peerfear.org">Kevin A. Burton</a>
 * @version $Id: BaseTestCase.java 373622 2006-01-30 22:53:00Z mvdb $
 */
public class BaseTestCase extends TestCase {

    public BaseTestCase( String name ) throws Exception {
        super( name );
    }

    /**
     * Run a parse on the given feed and then capture the event output as a
     * string for unit testing.  We can then grep across the string assering
     * that the correct events are called.
     *
     * 
     */
    public String captureOutputFromTest( String resource ) throws Exception {

        FeedParser parser = FeedParserFactory.newFeedParser();

        FeedParserListener listener = new CaptureOutputFeedParserListener();
        
        ResourceRequest request = ResourceRequestFactory.getResourceRequest( resource );
        
        parser.parse( listener, request.getInputStream(), resource );

        String output = listener.toString();

        return output;
        
    }

}
