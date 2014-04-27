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


/**
 * 
 * Adapted from Public Domain SAXException sources.
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @author David Megginson, 
 *         <a href="mailto:sax@megginson.com">sax@megginson.com</a>
 * @version $Id: FeedParserException.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class FeedParserException extends Exception {

    public FeedParserException ( String message ) {
        super( message );
    }

    public FeedParserException ( Throwable e ) {
        super( e );
    }

    /**
     * Create an exception tied to a specific resource or feed.
     */
    public FeedParserException ( Throwable e, String resource ) {
        super( e.getMessage() + " - From feed URL: " + resource, e );
    }

    public FeedParserException ( String message, Throwable e ) {
        super( message, e );
    }

}

