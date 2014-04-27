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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.feedparser.locate.FeedReference;

/**
 * Contains a list of all feeds found the the AutoDiscovery system.  Can also be
 * used when needing to refer to a list of feeds and provides util methods for
 * dealing with them
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: FeedList.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class FeedList extends LinkedList implements List {
    
    private FeedReference adAtomFeed = null;
    
    private FeedReference adRSSFeed = null;

    /**
     * 
     * Get the RSS feed discovered via autodiscovery.
     *
     * 
     */
    public FeedReference getAdRSSFeed() { 
        
        return this.adRSSFeed;
        
    }

    /**
     * 
     * Set the value of <code>adRSSFeed</code>.
     *
     * 
     */
    public void setAdRSSFeed( FeedReference adRSSFeed ) { 
        
        this.adRSSFeed = adRSSFeed;
        
    }

    /**
     * 
     * Get the Atom feed discovered via autodiscovery.
     *
     * 
     */
    public FeedReference getAdAtomFeed() { 
        
        return this.adAtomFeed;
        
    }

    /**
     * 
     * Set the value of <code>adAtomFeed</code>.
     *
     * 
     */
    public void setAdAtomFeed( FeedReference ref ) { 
        
        this.adAtomFeed = ref;
        
    }

    public void setFirstAdAtomFeed( FeedReference ref ) {

        // > The order of the autodiscovery elements is significant.
        // > The first element SHOULD point to the publisher's
        // > preferred feed for the document.

        if ( getAdAtomFeed() == null )
            setAdAtomFeed( ref );

    }

    public void setFirstAdRSSFeed( FeedReference ref ) {

        // > The order of the autodiscovery elements is significant.
        // > The first element SHOULD point to the publisher's
        // > preferred feed for the document.

        if ( getAdRSSFeed() == null )
            setAdRSSFeed( ref );

    }
    
    public void clear() {
        super.clear();
        this.adAtomFeed = null;
        this.adRSSFeed = null;
    }

}

