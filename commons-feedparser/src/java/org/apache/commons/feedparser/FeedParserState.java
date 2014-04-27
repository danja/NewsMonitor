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

import java.util.Locale;

import org.jdom.Element;

/**
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: FeedParserState.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class FeedParserState {

    public Element current = null;

    /**
     * xml:lang
     * @deprecated
     */
    public String lang;

    /**
     * xml:base
     * @deprecated
     */
    public String base;

    public FeedParserState() { }

    MetaFeedParserListener metaFeedParserlistener = null;

    public FeedParserState( Element current ) {
        this.current = current;
    }

    public FeedParserState( FeedParserListener listener ) {

        if ( listener instanceof MetaFeedParserListener )
            metaFeedParserlistener = (MetaFeedParserListener)listener;

    }

    /**
     * Based on the current locale event model we return the current language.
     * By default EN is returned.
     *
     * 
     */
    public Locale getCurrentLocale() {
        return null;
    }
    
}

