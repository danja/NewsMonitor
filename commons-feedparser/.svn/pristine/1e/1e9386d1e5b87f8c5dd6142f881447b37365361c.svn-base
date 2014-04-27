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
 * <p>A generic interface 
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public interface FOAFFeedParserListener extends FeedLifecycleListener {

    /**
     * @see FeedParserListener#onItem
     * 
     */
    public void onPerson( FeedParserState state,
                          String name ) throws FeedParserException;

    public void onPersonEnd() throws FeedParserException;

    public void onKnows( FeedParserState state ) throws FeedParserException;

    public void onKnowsEnd() throws FeedParserException;

    /**
     * Called when an image is found.
     *
     * 
     */
    public void onImage( FeedParserState state,
                         String resource ) throws FeedParserException;

    public void onImageEnd() throws FeedParserException;

    public void onWeblog( FeedParserState state,
                          String resource ) throws FeedParserException;

    public void onWeblogEnd() throws FeedParserException;

    public void onHomepage( FeedParserState state,
                            String resource ) throws FeedParserException;

    public void onHomepageEnd() throws FeedParserException;

    /**
     * When rdfs:seeAlso is called for an additional FOAF file.
     *
     * 
     */
    public void onSeeAlso( FeedParserState state,
                           String resource ) throws FeedParserException;

    public void onSeeAlsoEnd() throws FeedParserException;

}

