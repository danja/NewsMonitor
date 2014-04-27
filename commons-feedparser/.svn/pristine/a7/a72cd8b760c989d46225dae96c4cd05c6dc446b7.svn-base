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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Given a string of HTML content we decode the entities it contains.
 *
 * NOTE: Currently this is a trivial implementation and we need to go through
 * and make sure all HTML entities are correctly supported.
 * 
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id$
 */
public class EntityDecoder {

    //FIXME: see FeedFilter.java for a list of all valid HTML entities.  I
    //should replace them with character literals in this situation.
    
    private static HashMap entities = new HashMap();

    static Pattern pattern = Pattern.compile( "&([a-z]+);" );
    
    static {

        //FIXME: there are a LOT more of these and we need an exhaustive colleciton.
        
        entities.put( "gt", ">" );
        entities.put( "apos", ">" );
        entities.put( "lt", "<" );
        entities.put( "amp", "&" );

        //FIXME: 
        entities.put( "raquo", "" );
        entities.put( "laquo", "" );
        
    }

    /**
     * Decode content.  If a null is passed in we return null. 
     *
     * 
     */
    public static String decode( String content ) {

        if ( content == null )
            return null;
        
        //FIXME(performance): do I have existing code that does this more efficiently?
        if (content == null)
            return null;

        StringBuffer buff = new StringBuffer( content.length() );

        Matcher m = pattern.matcher( content );
        
        int index = 0;
        while ( m.find() ) {

            //figure out which entity to escape or just include it.

            buff.append( content.substring( index, m.start( 0 ) ) );

            String entity = m.group( 1 );

            if ( entities.containsKey( entity ) ) {
                buff.append( entities.get( entity ) );
            } else {
                //found an entity we no NOTHING about.  Should we warn?
                
                buff.append( m.group( 0 ) );
            }

            index = m.end( 0 );

        }

        buff.append( content.substring( index, content.length() ) );

        return buff.toString();
        
    }

    public static void main( String[] args ) throws Exception {

        System.out.println( decode( "&amp;" ) );
        System.out.println( decode( "asdf&amp;asdf" ) );

        System.out.println( decode( "asdf&amp;" ) );

        System.out.println( decode( "&amp;asdf" ) );

    }

}
