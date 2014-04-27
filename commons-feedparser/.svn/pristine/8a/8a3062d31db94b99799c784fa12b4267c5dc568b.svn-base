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
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public interface FeedParserListener extends FeedLifecycleListener {
    
    /**
     * Called when a channel item is found.
     *
     * 
     */
    public void onChannel( FeedParserState state,
                           String title,
                           String link,
                           String description ) throws FeedParserException;

    public void onChannelEnd() throws FeedParserException;

    /**
     * Called when an RSS image is found.
     *
     * 
     */
    public void onImage( FeedParserState state,
                         String title,
                         String link,
                         String url ) throws FeedParserException;

    public void onImageEnd() throws FeedParserException;
    
    /**
     * Called when an RSS item or Atom entry is found. 
     *
     * 
     */
    public void onItem( FeedParserState state,
                        String title,
                        String link,
                        String description,
                        String permalink ) throws FeedParserException;

    public void onItemEnd() throws FeedParserException;

    /**
     * Called when we are first able to determine the feed version for this
     * feed.  Ideally implementations should call this BEFORE onChannel but
     * depending on the parser infrastructure this might not be possible.
     *
     * Should be called before init()
     *
     * 
     */
    public void onFeedVersion( FeedVersion version ) throws FeedParserException;
    
}

