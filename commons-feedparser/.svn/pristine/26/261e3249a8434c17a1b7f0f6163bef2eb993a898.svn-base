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
import java.util.Locale;

import org.jdom.Element;

/**
 * Default implemmentation of a FeedParserListener with noop methods.  This can
 * be used as a base class for new implementations which do not need most of the
 * functionality of a FeedParserListener.
 * 
 * Its recommended (but not required) that implementors extend this interface to
 * that when new methods are added to the FeedParserListener that upgrades to
 * this library do not break your application.
 * 
 * @see FeedParserListener 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public abstract class DefaultFeedParserListener implements FeedParserListener,
                                                           MetaFeedParserListener,
                                                           ModContentFeedParserListener,
                                                           XHTMLFeedParserListener,
                                                           ContentFeedParserListener {

    private Object context = null;

    public void onFeedVersion( FeedVersion version ) throws FeedParserException {}
    public void init() throws FeedParserException {}

    public void setContext( Object context ) throws FeedParserException {
        this.context = context;
    }

    public Object getContext() throws FeedParserException {
        return this.context;
    }

    public void onChannel( FeedParserState state,
                           String title,
                           String link,
                           String description ) throws FeedParserException {}

    public void onChannelEnd() throws FeedParserException { }

    public void onImage( FeedParserState state,
                         String title,
                         String link,
                         String url ) throws FeedParserException {}

    public void onImageEnd() throws FeedParserException {}
    
    public void onItem( FeedParserState state,
                        String title,
                        String link,
                        String description,
                        String permalink ) throws FeedParserException { }

    public void onItemEnd() throws FeedParserException {}
    public void finished() throws FeedParserException {}

    // **** MetaFeedParserListener **********************************************

    public void onCopyright( FeedParserState state, String content ) throws FeedParserException {}
    public void onCopyrightEnd() throws FeedParserException {}

    /**
     * http://www.mnot.net/drafts/draft-nottingham-atom-format-00.html#rfc.section.3.2.8
     *
     * 
     */
    public void onCreated( FeedParserState state, Date date ) throws FeedParserException{}
    public void onCreatedEnd() throws FeedParserException {}

    public void onSubject( FeedParserState state, String content ) throws FeedParserException {}
    public void onSubjectEnd() throws FeedParserException {}

    /**
     * http://www.mnot.net/drafts/draft-nottingham-atom-format-00.html#rfc.section.3.2.7
     *
     * 
     */
    public void onIssued( FeedParserState state, String content ) throws FeedParserException {} 
    public void onIssuedEnd() throws FeedParserException {}

    public void onLocale( FeedParserState state, Locale locale ) throws FeedParserException {}

    public void onLocaleEnd() throws FeedParserException {}

    public void onGUID( FeedParserState state,
                        String value,
                        boolean isPermalink ) throws FeedParserException {}

    public void onGUIDEnd() throws FeedParserException{}

    public void onGenerator( FeedParserState state, String content ) throws FeedParserException {}
    public void onGeneratorEnd() throws FeedParserException {}

    public void onAuthor( FeedParserState state,
                          String name,
                          String email,
                          String resource ) throws FeedParserException {}

    public void onAuthorEnd() throws FeedParserException {}

    public void onComments( FeedParserState state,
                            String resource ) throws FeedParserException {}

    public void onCommentsEnd() throws FeedParserException {}

    public void onCommentsFeed( FeedParserState state,
                                String resource ) throws FeedParserException {}

    public void onCommentsFeedEnd() throws FeedParserException {}

    // **** ModContentFeedParserListener ****************************************

    public void onContentEncoded( FeedParserState state,
                                  String value ) throws FeedParserException {}

    public void onContentEncodedEnd() throws FeedParserException {}

    public void onContentItem( FeedParserState state,
                               String format,
                               String encoding,
                               Element value ) throws FeedParserException {}

    public void onContentItemEnd() throws FeedParserException {}

    // **** XHTMLFeedParserListener *********************************************

    public void onXHTMLBody( FeedParserState state, Element value ) throws FeedParserException {}

    public void onXHTMLBodyEnd() throws FeedParserException {}

    // **** ContentFeedParserListener *******************************************

    public void onContent( FeedParserState state,
                           String type,
                           String format,
                           String encoding,
                           String mode,
                           String value,
                           boolean isSummary ) throws FeedParserException {}

    public void onContentEnd() throws FeedParserException {}

}
