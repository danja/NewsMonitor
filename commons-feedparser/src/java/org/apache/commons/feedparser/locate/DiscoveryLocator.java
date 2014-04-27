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

package org.apache.commons.feedparser.locate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.feedparser.FeedList;
import org.apache.log4j.Logger;

/**
 *
 * http://www.ietf.org/internet-drafts/draft-ietf-atompub-autodiscovery-00.txt
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
 */
public class DiscoveryLocator {
    
    private static Logger log = Logger.getLogger( DiscoveryLocator.class );

    /**
     * Get a FULL link within the content. We then pull the attributes out of
     * this.
     */
    static Pattern element_pattern =
        Pattern.compile( "<link[^>]+",
                         Pattern.CASE_INSENSITIVE );

    /**
     * Regex to match on attributes.
     * 
     * Implementation: Mon Mar 14 2005 01:59 PM (burton@rojo.com): this is a
     * pretty difficult regexp to grok.
     * 
     * There's are two regexps here.  One for attributes with quotes and one
     * without. Each regexp has two groups - 1 is the name and 2 is the value.
     * You can split the regexp on | to better understand each individual
     * regexp.
     */

    // > Attribute values MUST be one of the following: enclosed in double
    // > quotes, enclosed in single quotes, or not enclosed in quotes at all.
    //
    // 
    static String ATTR_REGEXP = "([a-zA-Z]+)=[\"']([^\"']+)[\"']|([a-zA-Z]+)=([^\"'>\r\n\t ]+)";
    
    static Pattern ATTR_PATTERN = Pattern.compile( ATTR_REGEXP,
                                                   Pattern.CASE_INSENSITIVE );

    static HashSet mediatypes = new HashSet();

    static {

        mediatypes.add( FeedReference.ATOM_MEDIA_TYPE );
        mediatypes.add( FeedReference.RSS_MEDIA_TYPE );
        mediatypes.add( FeedReference.XML_MEDIA_TYPE );
        
    }

    /**
     * Locate a feed via RSS/Atom auto-discovery.  If both Atom and RSS are
     * listed we return both.  Actually we return all Atom/RSS or XML feeds
     * including FOAF.  It's up to the caller to use the correct feed.
     *
     * 
     */
    public static final List locate( String resource,
                                     String content,
                                     FeedList list )
        throws Exception {

        //this mechanism is easier but it isn't efficient.  I should just parse
        //elements forward until I discover </head>.  Also note that this isn't
        //doing all feed URLs just the first ones it finds.  

        Matcher m = element_pattern.matcher( content );

        while( m.find() ) {
            //the value of the link element XML... example:
            
            // <link rel="alternate" 
            //      href="http://www.codinginparadise.org/weblog/atom.xml"
            //      type="application/atom+xml" 
            //      title="ATOM" />
                 
            String element = m.group( 0 );

            HashMap attributes = getAttributes( element );
            
            String type = (String)attributes.get( "type" );
            if (type != null)
                type = type.toLowerCase();

            if ( mediatypes.contains( type )  ) {

                //expand the href
                String href = (String)attributes.get( "href" );
                log.debug("href="+href);

                // http://xml.coverpages.org/draft-ietf-atompub-autodiscovery-00.txt
                
                // > The href attribute MUST be present in an Atom autodiscovery element,
                // > and its value MUST be the URI [RFC2396] of an Atom feed.  The value
                // > MAY be a relative URI, and if so, clients MUST resolve it to a full
                // > URI (section 5 of [RFC2396]) using the document's base URI (section
                // > 12.4 of HTML 4 [W3C.REC-html401-19991224]).

                href = ResourceExpander.expand( resource, href );

                FeedReference feedReference = new FeedReference( href, type );
                
                feedReference.title = (String)attributes.get( "title" );
                
                list.add( feedReference );

                if ( type.equals( FeedReference.ATOM_MEDIA_TYPE ) )
                    list.setFirstAdAtomFeed( feedReference );
                    
                if ( type.equals( FeedReference.RSS_MEDIA_TYPE ) )
                    list.setFirstAdRSSFeed( feedReference );

            }
            
        }
        
        return list;
        
    }

    /**
     * Parse attributes within elements into a hashmap.
     *
     * 
     */
    public static HashMap getAttributes( String content ) {

        HashMap map = new HashMap();

        Matcher m = ATTR_PATTERN.matcher( content );

        int index = 0;

        while ( m.find( index ) ) {

            String name = m.group( 1 );
            String value = null;

            //Since we use an OR regexp the first match will be 1/2 and the
            //second will be 3/4
            if ( name != null ) {
                value = m.group( 2 );
            } else {
                name = m.group( 3 );
                value = m.group( 4 );
            }

            //String value = m.group( 2 ).toLowerCase().trim();
            name = name.toLowerCase().trim();
            // Some services, such as AOL LiveJournal, are case sensitive
            // on their resource names; can't do a toLowerCase.
            // Brad Neuberg, bkn3@columbia.edu
            // String value = m.group( 2 ).toLowerCase().trim();
            value = value.trim();

            if ( "".equals( value ) ) 
                value = null; 

            map.put( name, value );
            
            index =  m.end();
            
        } 

        return map;
        
    }

}
