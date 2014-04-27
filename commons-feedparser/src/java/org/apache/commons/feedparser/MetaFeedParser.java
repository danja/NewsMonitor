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

import java.util.Date;

import org.apache.commons.feedparser.tools.ISO8601DateParser;
import org.apache.commons.feedparser.tools.RFC822DateParser;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Handles parsing RSS metadata including dates
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: MetaFeedParser.java 373879 2006-01-31 17:23:15Z mvdb $
 */
public class MetaFeedParser extends BaseParser {

    /**
     * 
     */
    public static void parse( FeedParserListener listener,
                              FeedParserState state ) throws FeedParserException {

        //FIXME: this should be refactored into a new class called
        //MetaFeedParser to be used by both Atom and RSS.  Also the date
        //handling below needs to be generic.
        
        if ( listener instanceof MetaFeedParserListener == false )
            return;

        MetaFeedParserListener mfp = (MetaFeedParserListener)listener;

        parseDate( state, mfp );
        
        //FIXME: make sure RSS .9 is working and 0.91.  I just need to confirm
        //but I think they are working correctly
        parseGUID( state, mfp );

        parseAuthor( state, mfp );

        parseComments( state, mfp );

        parseCommentsFeed( state, mfp );
        
    }

    private static void parseComments( FeedParserState state,
                                       MetaFeedParserListener listener )
        throws FeedParserException {

        Element element = state.current.getChild( "comments" );

        if ( element != null ) {

            String resource = element.getText();
            listener.onComments( state, resource );
            listener.onCommentsEnd();
            
        }
        
    }

    private static void parseCommentsFeed( FeedParserState state,
                                           MetaFeedParserListener listener )
        throws FeedParserException {

        Element element = state.current.getChild( "commentRSS", NS.WFW );

        if ( element != null ) {

            String resource = element.getText();
            listener.onCommentsFeed( state, resource );
            listener.onCommentsFeedEnd();
            
        }

    }

    private static void parseAuthor( FeedParserState state,
                                     MetaFeedParserListener listener )
        throws FeedParserException {

        Element element = null;

        String name = null;
        String email = null;
        String resource = null;

        try {
            
            //atom:author

            element = state.current.getChild( "author", NS.ATOM );

            if ( element != null ) {
                name = selectText( "atom:name", element );
                email = selectText( "atom:email", element );
                resource = selectText( "atom:uri", element );
            }

            //dc:creator (RSS 1.0)
            element = state.current.getChild( "creator", NS.DC );

            if ( element != null )
                name = element.getText();
                
            //author (RSS 2.0)
            element = state.current.getChild( "author" );

            if ( element != null )
                name = element.getText();

            if ( name != null && ! "".equals( name ) ) {

                listener.onAuthor( state,
                                   name,
                                   email,
                                   resource );
                
                listener.onAuthorEnd();

            }

        } catch ( Exception e ) {
            throw new FeedParserException( e );
        }

    }

    private static void parseGUID( FeedParserState state,
                                   MetaFeedParserListener listener )
        throws FeedParserException {

        Element id = null;

        String guid = null;
        boolean isPermalink = false;
        
        id = state.current.getChild( "id", NS.ATOM );

        if ( id != null ) {
            guid = id.getText();
        }

        id = state.current.getChild( "guid" );

        if ( id != null ) {

            guid = id.getText();
            isPermalink = "true".equals( id.getAttributeValue( "isPermalink" ) );
            
        }

        if ( guid != null ) {

            listener.onGUID( state,
                             guid,
                             isPermalink );
            
            listener.onGUIDEnd();

        }
        
    }

    private static void parseDate( FeedParserState state,
                                   MetaFeedParserListener listener )
        throws FeedParserException {

        if ( parseDate( "date", NS.DC, listener, state ) )
            return;

        // http://www.mnot.net/drafts/draft-nottingham-atom-format-02.html#rfc.section.4.13.8
        // 
        // The "atom:created" element is a Date construct that indicates the
        // time that the entry was created. atom:entry elements MAY contain
        // an atom:created element, but MUST NOT contain more than one.

        // The content of an atom:created element MUST have a time zone
        // whose value SHOULD be "UTC".
        
        if ( parseDate( "created", NS.ATOM, listener, state ) )
            return;

        // http://www.mnot.net/drafts/draft-nottingham-atom-format-02.html#rfc.section.4.13.8
        //            
        // If atom:created is not present, its content MUST considered to be
        // the same as that of atom:modified.

        if ( parseDate( "modified", NS.ATOM, listener, state ) )
            return;

        //support RSS 2.0 and RSS 0.9x dates.

        if ( parseDate( "pubDate", null, listener, state, false ) )
            return;

    }
    
    private static boolean parseDate( String name,
                                      Namespace ns,
                                      MetaFeedParserListener listener,
                                      FeedParserState state ) {

        return parseDate( name, ns, listener, state, true );

    }
    
    private static boolean parseDate( String name,
                                      Namespace ns,
                                      MetaFeedParserListener listener,
                                      FeedParserState state,
                                      boolean ISO8601 ) {
        try { 
            //ok.  Support dc:date
            String v = state.current.getChildText( name, ns );
    
            if ( v != null ) {
                    
                Date d = null;
                if ( ISO8601 ) {
                    d = ISO8601DateParser.parse( v );
                } else {
                    d = RFC822DateParser.parse( v );
                }

                listener.onCreated( state, d );
                listener.onCreatedEnd();

                return true;
            }
        } catch ( Throwable t ) {
            // ignore the exception, so we can just move on
        }

        return false;

    }

}
