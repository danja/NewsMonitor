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
 * Atom link, RSS 2.0 enclosure, and RSS 1.0 mod_link support.
 *
 * Note that you are given callbacks for *all* atom:link and RSS mod_link
 * callbacks.  This includes the HTML permalink URL so its important that you
 * handle this situation as well.  
 * 
 * More info on the link mechanism:
 *
 * http://web.resource.org/rss/1.0/modules/link/
 * 
 * http://xml.com/pub/a/2004/06/16/dive.html
 * 
 * http://www.peerfear.org/rss/permalink/2004/06/17/AtomLinkModel
 *
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public interface LinkFeedParserListener {

    /**
     *
     * 
     * @param length The length of the content or -1 if not specified.
     */
    public void onLink( FeedParserState state,
                        String rel,
                        String type,
                        String href,
                        String title,
                        long length ) throws FeedParserException;

}

