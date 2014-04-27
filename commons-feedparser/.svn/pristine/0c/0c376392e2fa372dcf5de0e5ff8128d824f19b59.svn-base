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

import org.jdom.Element;

/**
 *
 * @deprecated Migration to ContentFeedParserListener
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public interface ModContentFeedParserListener {

    public void onContentEncoded( FeedParserState state,
                                  String value ) throws FeedParserException;

    public void onContentEncodedEnd() throws FeedParserException;

    public void onContentItem( FeedParserState state,
                               String format,
                               String encoding,
                               Element value ) throws FeedParserException;

    public void onContentItemEnd() throws FeedParserException;

}

