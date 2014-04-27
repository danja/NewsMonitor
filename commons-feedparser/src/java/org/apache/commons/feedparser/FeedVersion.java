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
 * @version $Id: FeedVersion.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class FeedVersion {

    /**
     * True if this is an Atom feed.
     */
    public boolean isAtom;

    /**
     * True if this is an RSS feed
     */
    public boolean isRSS;
    
    /**
     * True when this is a Friend of a Friend FOAF file.
     */
    public boolean isFOAF;

    public boolean isOPML;

    public boolean isXFN;

    /**
     * True if this is a changes.xml file.
     */
    public boolean isChanges;

    /**
     * The version of this specification.  If this is RSS 1.0 the version will
     * be "1.0".  If this is Atom 0.5 the version will be "0.5" (and so
     * forth). If the version is unknown the value is simply null.  The format
     * of this is unstructured text and in many situations is mirrored right
     * form the 'version' attribute in some RSS/Atom specifications.  See
     * version_major, version_minor, and version_sub for more info.
     *
     * 
     */
    public String version = null;

    public int version_major = 0;
    public int version_minor = 0;
    public int version_sub = 0;

    public String toString() {

        String result = "";

        if ( isAtom ) 
            result = "atom"; 
        else if ( isRSS )
            result = "rss";
        else if ( isFOAF )
            result = "foaf";
        else if ( isOPML )
            result = "opml";
        else if ( isXFN )
            result = "xfn";
        else if ( isChanges )
            result = "changes";

        return result += ":" + version;

    }
    
}
