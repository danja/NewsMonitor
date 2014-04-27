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
 * <p>
 * Interface for generic feeds that support feed directory structures.  These
 * are feed such as OPML and OCS that support nested feeds.  This can be used
 * within two systems to exchange feed lists.
 *
 * <p>
 * This interface needs to be compatible with:
 * 
 * <p>
 *
 * <dl>
 *     <dt>FDML</dt>
 *     <dd>http://www.intertwingly.net/wiki/fdml/</dd>
 * 
 *     <dt>OPML (Outline Processor Markup Language)</dt>
 *     <dt>OCS (Open Content Syndication)</dt>
 * 
 *     <dt>XFN (XHTML Friends Network)</dt>
 * 
 * </dl>
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: FeedDirectoryParserListener.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public interface FeedDirectoryParserListener extends FeedParserListener {

    /**
     * Called when an directory item is found.  This is compatible with the
     * FeedParserListener so that existing implementations work.  This provides
     * a mechanism to index FDML, OPML, OCS, etc with existing feed parsers.
     *
     * @param weblog The HTML URL to the root of the weblog.  Example:
     * http://www.peerfear.org
     *
     * @param title The title of the feed or weblog.  Maybe be null when not
     * specified.
     * 
     * @param feed The XML URL to the RSS/Atom feed for this weblog.  This may
     * be null in some situations when we don't have a feed URL
     * 
     * @see FeedParserListener#onItem
     * 
     */
    public void onItem( FeedParserState state,
                        String title,
                        String weblog,
                        String description,
                        String feed ) throws FeedParserException;

    public void onItemEnd() throws FeedParserException;

    /**
     * Called when we've found a relation for a given item.  This way you can
     * specify the relationship you have with a given entry in your directory.
     * This is mostly for compatibility purposes with XFN so that the values can
     * be 'met', 'date', 'sweetheart', 'friend'.
     *
     * For XFN we would call onItem() methods and then onRelation() methods with
     * each of the relations passed.
     * 
     * 
     */
    public void onRelation( FeedParserState state,
                            String value );

    public void onRelationEnd();

    /**
     * Called when a new Folder is found.  If feeds are in the default root
     * folder this method is not called.  This is mostly for OPML support but
     * could be used within other feed formats.  When this method isn't called
     * one could assume that items are in the 'root' folder or no folder.
     *
     * 
     */
    public void onFolder( FeedParserState state,
                          String name ) throws FeedParserException;

    public void onFolderEnd() throws FeedParserException;

}

