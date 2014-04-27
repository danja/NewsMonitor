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

/**
 *
 * Given a string of HTML content, parse out anchors and fire events with all
 * the data when they are found.
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 */
public class AnchorParser {

    public static void parse( String content,
                              AnchorParserListener listener )
        throws AnchorParserException {

        //FIXME: we do NOT obey base right now and this is a BIG problem!
        
        parseAnchors( content, listener );
        
    }

    /**
     * Get links from the given html with included titles and other metainfo.
     *
     * @deprecated use HTParser
     * 
     */
    public static void parseAnchors( String content,
                                     AnchorParserListener listener )
        throws AnchorParserException {

        int index = 0;
        int begin = 0;
        int end = 0;

        //FIXME: what if there are HTML comments here?  We would parse links
        //within comments which isn't what we want.

        // FIXME: how do we pass back the content of the href?
        //
        // <a href=''> this is the content </a>
        //
        // which would pass a string "this is the content"

        //Matcher m = pattern.matcher( content );

        while ( (begin = content.indexOf( "<a", index )) != -1 ) {

            index = begin;

            end = content.indexOf( "</a>", index );
            if ( end == -1 )
                break;
            index = end + 1;
            
            String match =  content.substring( begin, end );
            
            HashMap map = DiscoveryLocator.getAttributes( match );
            //String resource = EntityDecoder.decode( m.group( 1 ) );

            //FIXME: we SHOULD be using this but its not working right now.
            String resource = (String)map.get( "href" );
            
            if ( resource == null || resource.equals( "" ) ) {
                continue;
            }

            String title = (String)map.get( "title" );

            if ( title != null )
                title = EntityDecoder.decode( title );
                
            String rel = (String)map.get( "rel" );
            
            if ( ! listener.onAnchor( resource, rel, title ) )
                return;

        } 

    }

    public static void main( String[] args ) throws Exception {

        AnchorParserListener listener = new AnchorParserListener() {

                public boolean onAnchor( String href, String rel, String title ) {

                    System.out.println( "href: " + href );
                    System.out.println( "rel: " + rel );
                    System.out.println( "title: " + title );
                    return true;
                }

                public Object getResult() {
                    return null;
                }
                public void setContext( Object context ) {}
                
            };

        //FIXME: won't work with single quotes
        //FIXME: won't work with <a />
        //parse( "<a href=\"http://peerfear.org\" rel=\"linux\" title=\"linux\" >adf</a>", listener );

        //parse( "<a rel=\"linux\" href=\"http://peerfear.org\" title=\"linux\" >adf</a>", listener );
        //parse( "<a title=\"linux\" rel=\"linux\" href=\"http://peerfear.org\" >adf</a>", listener );

        //parse( "<a href='http://peerfear.org' rel='linux' title='linux' >adf</a>", listener );

        parse( "<a href='mailto:burton@rojo.com' rel='linux' title='linux' ><img src='' /></a>", listener );

    }

}
