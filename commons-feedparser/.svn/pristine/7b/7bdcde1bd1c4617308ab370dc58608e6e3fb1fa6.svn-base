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
public interface FeedLifecycleListener {

    /**
     * Called prior to event parsing to signal the parsing of a new feed.
     *
     * @throws FeedParserListener Possibly wraping another exception
     * 
     */
    public void init() throws FeedParserException;

    /**
     * Set this context that this FeedParserListener is executing within.  This
     * can be used with anonymous listeners to pass contextual information about
     * threads they are working within, etc.
     *
     * 
     */
    public void setContext( Object context ) throws FeedParserException;
    public Object getContext() throws FeedParserException;

    /**
     * Called when the feed has finished parsing.
     *
     * 
     */
    public void finished() throws FeedParserException;

}
