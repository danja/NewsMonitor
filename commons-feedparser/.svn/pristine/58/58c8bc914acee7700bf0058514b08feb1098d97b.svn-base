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

import java.util.HashSet;

import org.apache.commons.feedparser.impl.DebugFeedParserListener;
import org.apache.commons.feedparser.locate.AnchorParser;
import org.apache.commons.feedparser.locate.AnchorParserException;
import org.apache.commons.feedparser.locate.AnchorParserListener;

/**
 *
 * Experimental class to play with supporting XFN.  HTML parsing in general is
 * interesting because I could start with the AnchorParser and move to an HTML
 * parser but that might be too generic.
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public class HTMLFeedParser extends BaseParser {

    public static final HashSet XFN_RELATIONS = new HashSet();
    
    public static void parse( String content, final FeedParserListener listener ) throws Exception {

        if ( listener instanceof FeedDirectoryParserListener == false )
            return;

        //FIXME: only convert to using XFN if these types of links are detected.
        //If its just a plain XHTML file then we shouldn't use this interface.
        //Also FeedVersion needs to be called.
        
        final FeedDirectoryParserListener directoryParserLisener =
            (FeedDirectoryParserListener)listener;

        directoryParserLisener.init();
        
        final FeedParserState state = new FeedParserState();
        
        AnchorParserListener alistener = new AnchorParserListener() {

                public void setContext( Object context ) {}

                public Object getResult() { return null; }

                public boolean onAnchor( String href, String rel, String title )
                    throws AnchorParserException {

                    try {
                        
                        if ( rel == null || "".equals( rel ) )
                            return true;

                        //right now these aren't valid here
                        String description = null;
                        String feed = null;
                    
                        //FIXME: only include onItem when we have at least ONE XFN
                        //relations that valid.

                        directoryParserLisener.onItem( state, title, href, description, feed );
                    
                        String[] rels = rel.split( " " );

                        for ( int i = 0; i < rels.length; ++i ) {

                            String current = rels[i];

                            //FIXME: when this current rel is NOT part of any XFN
                            //spec we should not be using the feed parser listener
                            //because it might just be a nofollow link or such.

                            boolean isXFriendRel = XFN_RELATIONS.contains( current );

                            if ( isXFriendRel ) {
                        
                                directoryParserLisener.onRelation( state,
                                                                   current );
                            
                                directoryParserLisener.onRelationEnd();

                            }
                        
                        }

                        directoryParserLisener.onItemEnd();
                    
                        //split this into individual rels... then call them.
                    
                        return true;

                    } catch ( Exception e ) {
                        throw new AnchorParserException( e );
                    }

                }

            };

        AnchorParser.parse( content, alistener );

        directoryParserLisener.finished();

    }

    public static void main( String[] args ) throws Exception {

        FeedParserListener listener = new DebugFeedParserListener();
        
        parse( "<a href='http://jane-blog.example.org/' rel='sweetheart date met'>Jane</a> ",
               listener );
        
    }

    static {

        XFN_RELATIONS.add( "contact" );
        XFN_RELATIONS.add( "acquaintance" );
        XFN_RELATIONS.add( "friend" );
        XFN_RELATIONS.add( "met" );
        XFN_RELATIONS.add( "co-worker" );
        XFN_RELATIONS.add( "colleague" );
        XFN_RELATIONS.add( "co-resident" );
        XFN_RELATIONS.add( "neighbor" );
        XFN_RELATIONS.add( "child" );
        XFN_RELATIONS.add( "parent" );
        XFN_RELATIONS.add( "sibling" );
        XFN_RELATIONS.add( "spouse" );
        XFN_RELATIONS.add( "kin" );
        XFN_RELATIONS.add( "muse" );
        XFN_RELATIONS.add( "crush" );
        XFN_RELATIONS.add( "date" );
        XFN_RELATIONS.add( "sweetheart" );
        XFN_RELATIONS.add( "me" );
        
    }
    
}

