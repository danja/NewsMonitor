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

package org.apache.commons.feedparser.output;

import java.io.OutputStream;

import org.apache.commons.feedparser.DefaultFeedParserListener;
import org.apache.commons.feedparser.FeedParserListener;
import org.apache.commons.feedparser.FeedParserState;

/**
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: RSS10_OutputFeedParserListener.java 373622 2006-01-30 22:53:00Z mvdb $
 */
public class RSS10_OutputFeedParserListener extends DefaultFeedParserListener
    implements FeedParserListener {

    private OutputStream out = null;
    
    /**
     * 
     * Create a new <code>RSS10_OutputFeedParserListener</code> instance.
     *
     * 
     */
    public RSS10_OutputFeedParserListener( OutputStream out ) {
        this.out = out;
    }

    /**
     * Called prior to event parsing to signal the parsing of a new feed.
     *
     * 
     */
    public void init() {}
    public void setContext( Object context ) {}

    /**
     * Called when a channel item is found.
     *
     * 
     */
    public void onChannel( FeedParserState state,
                           String title,
                           String link,
                           String description ) {

    }

    public void onChannelEnd() {

    }
        
    /**
     * Called when an RSS image is found.
     *
     * 
     */
    public void onImage( FeedParserState state,
                         String title,
                         String link,
                         String url ) {

    }

    public void onImageEnd() {

    }
    
    /**
     * Called when an RSS item or Atom entry is found. 
     *
     * 
     */
    public void onItem( FeedParserState state,
                        String title,
                        String link,
                        String description,
                        String permalink ) {

    }

    public void onItemEnd() {

    }

    /**
     * Called when the feed has finished parsing.
     *
     * 
     */
    public void finished() {

    }

}

