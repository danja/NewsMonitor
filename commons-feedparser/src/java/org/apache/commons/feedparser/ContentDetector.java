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
 * Given the RAW content of a URL, determine if we're looking at an RSS file or
 * an HTML file.  We also return the given RSS version or Atom version.
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: ContentDetector.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class ContentDetector {

    /**
     * Return true if the given content seems to be RSS.  This is going to be a
     * cheat because really we have no way of telling if this is RSS other than if
     * it is XML and it starts with an RSS 1.0, 2.0, 0.91 or 0.9 decl
     *
     * 
     */
    public static ContentDetectorResult detect( String content ) throws Exception {

        ContentDetectorResult result = new ContentDetectorResult();
        
        result.isHTML = isHTMLContent( content );
        result.isRSS = ( isRSS_1_0_Content( content ) ||
                         isRSS_2_0_Content( content ) ||
                         isRSS_0_9_0_Content( content ) ||
                         isRSS_0_9_1_Content( content ) ||
                         isRSS_0_9_2_Content( content ) );

        result.isAtom = isAtomContent( content );

        result.isFeed = result.isRSS || result.isAtom;

        return result;
                         
    }

    /**
     * Return true if this is RSS 1.0 content
     *
     * 
     */
    public static boolean isRSS_1_0_Content( String content ) throws Exception {

        //do a search for the RSS 1.0 namespace.  This is a bit of a trick right
        //now.

        return content.indexOf( "http://purl.org/rss/1.0/" ) != -1;
        
    }

    /**
     * Return true if this is RSS 2.0 content
     *
     * 
     */
    public static boolean isRSS_0_9_1_Content( String content ) throws Exception {

        //look for the beginning of the RSS element
        return content.indexOf( "<rss" ) != -1;

    }

    /**
     * Return true if this is RSS 0.9.2 content
     *
     * 
     */
    public static boolean isRSS_0_9_2_Content( String content ) throws Exception {

        //same check for RSS 0.9.1
        return isRSS_0_9_1_Content( content );
        
    }

    /**
     * Return true if this is RSS 2.0 content
     *
     * 
     */
    public static boolean isRSS_2_0_Content( String content ) throws Exception {

        return isRSS_0_9_1_Content( content );

    }

    /**
     * Return true if this is RSS 2.0 content
     *
     * 
     */
    public static boolean isRSS_0_9_0_Content( String content ) throws Exception {

        //FIXME: look for the RDF namespace and the RSS DTD namespace
        return content.indexOf( "http://my.netscape.com/rdf/simple/0.9/" ) != -1;

    }

    public static boolean isAtomContent( String content ) throws Exception {

        return content.indexOf( "http://purl.org/atom/ns#" ) != -1;

    }

    /**
     * Return true if this is RSS 2.0 content
     *
     * 
     */
    public static boolean isHTMLContent( String content ) throws Exception {

        //look for the beginning of the RSS element
        return content.indexOf( "<html" ) != -1;

    }

    public static void main( String[] args ) {

        try { 
            
            //System.out.println( RSSContentVerifier.isRSSContent( new URL( args[0] ) ) );
            
        } catch ( Throwable t ) {
            
            t.printStackTrace();
            
        }

    }
    
}
