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

package org.apache.commons.feedparser.tools;


/**
 *
 * Given an XML document pull out the encoding or the default (UTF-8) if not
 * specified.
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 */
public class XMLEncodingParser {

    public static final String ENCODING = "encoding=\"";
    
    /**
     *
     * 
     */
    public static String parse( byte[] content ) throws Exception {

        //this isn't really pretty but it is fast.

        //just use the first 100 bytes

        String str;

        if ( content.length > 100 ) {
            str = new String( content, 0, 100 );
        } else {
            str = new String( content );
        }

        String result = getEncodingFromBOM( content );

        if ( result != null )
            return result;
        
        int end = str.indexOf( ">" );

        if ( end == -1 )
            return "UTF-8";

        String decl = str.substring( 0, end );

        int index = decl.indexOf( ENCODING );
        
        if ( index != -1 ) {

            String encoding = decl.substring( index + ENCODING.length(),
                                              decl.length() );

            end = encoding.indexOf( "\"" );
            
            if ( end == -1 )
                return "UTF-8";

            encoding = encoding.substring( 0, end);
            encoding = encoding.toUpperCase();

            if ( "UTF8".equals( encoding ) )
                encoding = "UTF-8";
            
            return encoding;
            
        }

        return "UTF-8";

    }

    private static String getEncodingFromBOM( byte[] content ) {

        // Technically speaking if we see a BOM is specified we're supposed to
        // return UTF-16 or UTF-32 but because we only care about anything UTF
        // returning UTF-8 is incorrect but acceptable.
        //
        // http://www.unicode.org/faq/utf_bom.html#BOM

        if ( content.length > 2 ) {

            //perform UTF-16 tests
            if ( content[0] == -1 &&
                 content[1] == -2 ) 
                return "UTF-16";

            if ( content[0] == -2 &&
                 content[1] == -1 ) 
                return "UTF-16";

        }

        if ( content.length > 4 ) {

            //perform UTF-16 tests
            if ( content[0] == 0 &&
                 content[1] == 0 &&
                 content[2] == -2 &&
                 content[3] == -1 ) 
                return "UTF-32";

            if ( content[0] == -1 &&
                 content[1] == -2 &&
                 content[2] == 0 &&
                 content[3] == 0 ) 
                return "UTF-32";

        }

        return null;
        
    }
    
    public static void main( String[] args ) throws Exception {

        System.out.println( parse( "<?xml encoding=\"utf-8\"?>".getBytes() ) );
        System.out.println( parse( "<?xml encoding=\"UTF-8\"?>".getBytes() ) );
        System.out.println( parse( "<?xml encoding=\"utf8\"?>".getBytes() ) );

    }

}
