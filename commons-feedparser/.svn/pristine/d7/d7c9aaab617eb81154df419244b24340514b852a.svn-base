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

package org.apache.commons.feedparser.impl;

import java.io.PrintStream;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.feedparser.DefaultFeedParserListener;
import org.apache.commons.feedparser.FeedDirectoryParserListener;
import org.apache.commons.feedparser.FeedParserException;
import org.apache.commons.feedparser.FeedParserListener;
import org.apache.commons.feedparser.FeedParserState;
import org.apache.commons.feedparser.FeedVersion;
import org.apache.commons.feedparser.LinkFeedParserListener;
import org.apache.commons.feedparser.MetaFeedParserListener;
import org.apache.commons.feedparser.ModContentFeedParserListener;
import org.apache.commons.feedparser.TagFeedParserListener;
import org.jdom.Element;

/**
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public class DebugFeedParserListener
    extends DefaultFeedParserListener
    implements FeedParserListener,
               FeedDirectoryParserListener,
               MetaFeedParserListener,
               LinkFeedParserListener,
               TagFeedParserListener,
               ModContentFeedParserListener {

    PrintStream out = System.out;

    public DebugFeedParserListener() { }

    public DebugFeedParserListener( PrintStream out ) {
        this.out = out;
    }

    /**
     * Called prior to event parsing to signal the parsing of a new feed.
     *
     * 
     */
    public void init() {
        out.println( "init()" );
    }

    public void onFeedVersion( FeedVersion version ) throws FeedParserException {
        out.println( "onFeedVersion: " + version );
    }

    public void setContext( Object context ) {}

    /**
     * Called when a channel item is found.
     *
     * 
     */
    public void onChannel( FeedParserState state,
                           String title,
                           String link,
                           String description ) {

        out.println( "onChannel" );
        out.println( "\ttitle : " + title );
        out.println( "\tlink : '" + link + "'" );
        out.println( "\tdescription : " + description );

    }

    public void onChannelEnd() {

        out.println( "onChannelEnd" );
        
    }

    /**
     * Called when an RSS image is found.
     *
     * 
     */
    public void onImage( FeedParserState state,
                         String title,
                         String link,
                         String url ) {

        out.println( "onImage" );

    }

    public void onImageEnd() {
        out.println( "onImageEnd" );
    }
    
    /**
     * Called when an RSS item or Atom entry is found. 
     *
     * 
     */
    public void onItem( FeedParserState state,
                        String title,
                        String link,
                        String description,
                        String permalink ) {

        out.println( "onItem" );

        out.println( "\ttitle : " + title );
        out.println( "\tlink : '" + link + "'" );
        out.println( "\tdescription : " + description );

    }

    public void onItemEnd() {
        out.println( "onItemEnd" );
    }

    /**
     * Called when the feed has finished parsing.
     *
     * 
     */
    public void finished() {
        out.println( "finished" );
    }

    /**
     * Called when a new Folder is found.  If feeds are in the default root
     * folder this method is not called.
     *
     * 
     */
    public void onFolder( FeedParserState state,
                          String name ) throws FeedParserException {

        out.println( "onFolder: " + name );

    }

    public void onFolderEnd() throws FeedParserException {

    }

    public void onContent( FeedParserState state,
                           String type,
                           String format,
                           String encoding,
                           String mode,
                           String value,
                           boolean isSummary ) throws FeedParserException {

        out.println( "onContent: " );
        out.println( value );
        
    }

    public void onCreated( FeedParserState state, Date date ) throws FeedParserException {

        out.println( "onCreated: " + date.toString() );

    }

    public void onGUID( FeedParserState state,
                        String value,
                        boolean isPermalink ) throws FeedParserException {

        out.println( "onGUID: " + value );
        out.println( "        isPermalink: " + isPermalink );

    }

    public void onAuthor( FeedParserState state,
                          String name,
                          String email,
                          String resource ) throws FeedParserException {

        out.println( "onAuthor: name:" + name );
        out.println( "          email: " + email );
        out.println( "          resource: " + resource );

    }

    public void onComments( FeedParserState state,
                            String resource ) throws FeedParserException {

        out.println( "onComments: " + resource );

    }

    public void onCommentsFeed( FeedParserState state,
                                String resource ) throws FeedParserException {

        out.println( "onCommentsFeed: " + resource );

    }

    // **** LinkFeedParserListener **********************************************

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
                        long length ) throws FeedParserException {

        out.println( "onLink: " );
        out.println( "\trel: " + rel );
        out.println( "\thref: " + href );
        out.println( "\ttype: " + type );
        
    }

    public void onLocale( FeedParserState state, Locale locale ) throws FeedParserException {

        out.println( "onLocale: " + locale );

    }

    public void onLocaleEnd() throws FeedParserException {

        out.println( "onLocaleEnd" );

    }

    public void onRelation( FeedParserState state,
                            String value ) {

        out.println( "onRelation: " + value );
        
    }

    public void onRelationEnd() {
        out.println( "onRelationEnd" );
    }

    // **** TagFeedParserListener ***********************************************

    public void onTag( FeedParserState state,
                       String tag,
                       String tagspace ) throws FeedParserException {

        out.println( "onTag: tag: " + tag + " tagspace: " + tagspace );

    }

    public void onTagEnd() throws FeedParserException {
        out.println( "onTagEnd" );
    }

    // **** ModContentFeedParserListener ****************************************

    public void onContentEncoded( FeedParserState state,
                                  String value ) throws FeedParserException {

        out.println( "onContentEncoded: " );
        out.println( value );

    }

    public void onContentItem( FeedParserState state,
                               String format,
                               String encoding,
                               Element value ) throws FeedParserException {

        out.println( "onContentItem: " );
        out.println( value );

    }

} 