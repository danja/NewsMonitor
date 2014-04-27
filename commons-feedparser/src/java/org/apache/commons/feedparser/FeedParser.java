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

package org.apache.commons.feedparser;

import java.io.InputStream;

/**
 * This FeedParser implementation is based on JDOM and Jaxen and is based around
 * XPath and JDOM iteration.  While the implementation is straight forward it
 * has not been optimized for performance.  A SAX based parser would certainly
 * be less memory intensive but with the downside of being harder to develop.  
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: FeedParser.java 155416 2005-02-26 13:00:10Z dirkv $
 */
public interface FeedParser {

    /**
     * Parse this feed.
     * 
     * @param resource The URL of the feed being parsed.  This is optional and
     * may be null but is used when an exception is thrown to aid debugging.
     *
     */
    public void parse( FeedParserListener listener,
                       InputStream is ,
                       String resource ) throws FeedParserException;

    /**
     * @deprecated Use #parse( FeedParserException, InputStream, String )
     */
    public void parse( FeedParserListener listener,
                       InputStream is ) throws FeedParserException;

    /**
     * Parse this feed.
     *
     */
    public void parse( FeedParserListener listener,
                       org.jdom.Document doc ) throws FeedParserException;

}

