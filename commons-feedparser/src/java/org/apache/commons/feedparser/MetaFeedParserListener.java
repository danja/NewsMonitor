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

/**
 *
 * Provides a MetaData event listener for RSS 0.9x, RSS 1.0, 2.0 and Atom
 * metadata values.
 * 
 * Each format provides mechanisms to represent copyright strings, dates,
 * modified times, etc.  This interface provides a generic implementation for
 * each.
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: MetaFeedParserListener.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public interface MetaFeedParserListener {

    public void onCopyright( FeedParserState state, String content ) throws FeedParserException;
    public void onCopyrightEnd() throws FeedParserException;

    /**
     * 
     * --- ATOM SUPPORT ---
     * 
     * The "atom:created" element's content indicates the time that the entry
     * was created. Entries MAY contain an atom:created element, but MUST NOT
     * contain more than one. When this element is present, its content MUST be
     * a W3C Date-Time string [[ref]]. The date SHOULD be expressed in the "UTC"
     * time zone [[reword?]].
     * 
     * If atom:created is not present, CONSUMERS MUST consider its value to be
     * the same as that of atom:modified.
     * 
     * http://www.mnot.net/drafts/draft-nottingham-atom-format-00.html#rfc.section.3.2.8
     *
     * --- RSS 2.0 SUPPORT ---
     *
     * <pubDate> is an optional sub-element of <item>.
     * 
     * Its value is a date, indicating when the item was published. If it's a
     * date in the future, aggregators may choose to not display the item until
     * that date.
     * 
     * <pubDate>Sun, 19 May 2002 15:21:36 GMT</pubDate>
     * 
     * http://feedvalidator.org/docs/rss2.html#ltpubdategtSubelementOfLtitemgt
     * 
     * --- RSS 1.0 SUPPORT ---
     * 
     * We use dc:date which is ISO 8601 compliant.
     * 
     * http://www.w3.org/TR/NOTE-datetime
     * http://web.resource.org/rss/1.0/modules/dc/
     *      
     * 
     */
    public void onCreated( FeedParserState state, Date date ) throws FeedParserException;
    public void onCreatedEnd() throws FeedParserException;

    /**
     * http://www.mnot.net/drafts/draft-nottingham-atom-format-00.html#rfc.section.3.2.7
     *
     * 
     */
    public void onIssued( FeedParserState state, String content ) throws FeedParserException; 
    public void onIssuedEnd() throws FeedParserException;

    /**
     * RSS 2.0 category.  Dublin Core.
     */
    public void onSubject( FeedParserState state, String content ) throws FeedParserException;
    public void onSubjectEnd() throws FeedParserException;

    /**
     * Called when we've found an xml:lang or a dc:lang on Atom and RSS feeds.
     *
     * 
     */
    public void onLocale( FeedParserState state, Locale locale ) throws FeedParserException;
    public void onLocaleEnd() throws FeedParserException;

    /**
     * Used to represent RSS 2.0 GUIDs and atom:id constructs.  For Atom
     * isPermalink should be ignored.
     *
     * 
     */
    public void onGUID( FeedParserState state,
                        String value,
                        boolean isPermalink ) throws FeedParserException;

    public void onGUIDEnd() throws FeedParserException;

    /**
     * Called when a generator contruct is found within Atom or RSS 2.0
     *
     * 
     */
    public void onGenerator( FeedParserState state, String content ) throws FeedParserException;
    public void onGeneratorEnd() throws FeedParserException;

    /**
     * Provided for author information across RSS 2.0, atom, dc:creator in RSS
     * 1.0.  Both email, and resource may be null if not specified.
     *
     * TODO: what does RSS 0.91, 0.9, etc provide?
     *
     * NOTE that this is not yet 100% compatible with FOAF person constructs.
     * FOAF provides additional metadata including title, firstName, surname,
     * nick, etc which we don't provide with this method.  We'll probably add
     * additional events for this in the future.
     *
     * 
     */
    public void onAuthor( FeedParserState state,
                          String name,
                          String email,
                          String resource ) throws FeedParserException;

    public void onAuthorEnd() throws FeedParserException;

    public void onComments( FeedParserState state,
                            String resource ) throws FeedParserException;

    public void onCommentsEnd() throws FeedParserException;

    public void onCommentsFeed( FeedParserState state,
                                String resource ) throws FeedParserException;

    public void onCommentsFeedEnd() throws FeedParserException;

}
