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

import java.io.FileInputStream;

import org.jdom.Element;

/**
 * This FeedParser implementation is based on JDOM and Jaxen and is based around
 * XPath and JDOM iteration.  While the implementation is straight forward it
 * has not been optimized for performance.  A SAX based parser would certainly
 * be less memory intensive but with the downside of being harder to develop.  
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: Test.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class Test extends DefaultFeedParserListener
    implements FeedParserListener,
               ModContentFeedParserListener,
               XHTMLFeedParserListener {

    // **** FeedParserListener interface ****************************************

    /**
     * Called prior to event parsing to signal the parsing of a new feed.
     *
     * 
     */
    public void init() {
        System.out.println( "init()" );
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

        System.out.println( "onChannel: " );
        System.out.println( "\t title: " + title );
        System.out.println( "\t link: " + link );
        System.out.println( "\t description: " + description );

    }
    public void onChannelEnd() {
        System.out.println( "onChannelEnd()" );
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

        System.out.println( "onImage: " );
        System.out.println( "\t title: " + title );
        System.out.println( "\t link: " + link );
        System.out.println( "\t url: " + url );

    }

    public void onImageEnd() {
        System.out.println( "onImageEnd()" );
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

        System.out.println( "onItem: " );
        System.out.println( "\t title: " + title );
        System.out.println( "\t link: " + link );
        System.out.println( "\t description: " + description );

    }

    public void onItemEnd() {

        System.out.println( "onItemEnd()" );

    }

    public void finished() {
        System.out.println( "finished()" );
    }

    // **** ModContentFeedParserListener interface ******************************

    public void onContentEncoded( FeedParserState state,
                                  String value ) {

        System.out.println( "onContentEncoded: " + value.length() + " bytes" );
        
    }

    public void onContentEncodedEnd() {
        System.out.println( "onContentEncodedEnd" );
    }

    public void onContentItem( FeedParserState state,
                               String format,
                               String encoding,
                               Element value ) {

        System.out.println( "onContentItem" );
        
    }

    public void onContentItemEnd() {
        System.out.println( "onContentItemEnd" );
    }

    // **** XHTMLBodyFeedParserListener interface *******************************

    public void onXHTMLBody( FeedParserState state, Element value ) {
        System.out.println( "onXHTMLBody" );
    }

    public void onXHTMLBodyEnd() {
        System.out.println( "onXHTMLBodyEnd" );
    }

    public static void main( String[] args ) {

        try { 
            
            FeedParser parser = FeedParserFactory.newFeedParser();

            //String path = "/home/burton/.records/rss/index.rss";
            //String path = "/projects/newsmonster/xml-tests/aaronsw.xml";
            //String path = "/projects/newsmonster/xml-tests/intertwingly.rss2";

            String path = "/projects/newsmonster/xml-tests/feedster.xml";
            
            parser.parse( new Test(), new FileInputStream( path ), path );
            
        } catch ( Throwable t ) {
            
            t.printStackTrace();
            
        }
        
    }

}

