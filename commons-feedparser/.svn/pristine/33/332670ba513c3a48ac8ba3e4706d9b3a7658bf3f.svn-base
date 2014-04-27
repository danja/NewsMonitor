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
 * Class that can cleanse a string so that nothing can be present to break an
 * XML parser.  This is a VERY non-portable class as it is meant to work just
 * with Xalan/Xerces and may remove more text and replace things that are
 * non-XML centric.
 *
 * @author <a href="mailto:burton@peerfear.org">Kevin A. Burton</a>
 * @version $Id$
 */
public class XMLCleanser {

    public static String cleanse( String content ) {

        StringBuffer buff = new StringBuffer( content.length() );

        for ( int i = 0; i < content.length(); ++i ) {

            char c = content.charAt( i );
            
            if ( isXMLCharacter( c ) ) {

                buff.append( c );
                
            } 

        }

        return buff.toString();

    }

    /**
     * Copy based on a byte array.  
     *
     * 
     */
    public static String cleanse( byte[] content, String encoding ) throws Exception {

        String s = new String( content, encoding );
        
        StringBuffer buff = new StringBuffer( content.length );

        for ( int i = 0; i < s.length(); ++i ) {

            char c = s.charAt( i );
            
            if ( isXMLCharacter( c ) ) {

                buff.append( c );
                
            } 

        }

        return buff.toString();

    }

    public static char[] cleanseToCharArray( byte[] content ) {

        char[] buff = new char[content.length];

        int index = 0;

        for ( int i = 0; i < content.length; ++i ) {

            char c = (char)content[ i ];
            
            if ( isXMLCharacter( c ) ) {

                buff[index] = c;
                
                ++index;
            } 

        }

        return buff;

    }
    
    /**
     * Copy based on a byte array.  
     *
     * 
     */
    public static byte[] cleanseToByteArray( byte[] content ) {

        byte[] buff = new byte[ content.length ];

        int index = 0;
        for ( int i = 0; i < content.length; ++i ) {

            char c = (char)content[ i ];
            
            if ( isXMLCharacter( c ) ) {

                //buff.append( c );
                buff[index] = content[ i ];
                ++index;
            } 

        }

        return buff;

    }

    /*
     * This is a utility function for determining whether a specified character
     * is a character according to production 2 of the XML 1.0 specification.
     *
     * @param c <code>char</code> to check for XML compliance.

     * @return <code>boolean</code> - true if it's a character, false otherwise.
     */
    public static boolean isXMLCharacter( char c ) {

        // A parsed entity contains text, a sequence of characters, which may
        // represent markup or character data. A character is an atomic unit of
        // text as specified by ISO/IEC 10646 [ISO/IEC 10646]. Legal characters
        // are tab, carriage return, line feed, and the legal graphic characters
        // of Unicode and ISO/IEC 10646. The use of "compatibility characters",
        // as defined in section 6.8 of [Unicode], is discouraged.

        // [2] Char ::= #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] |
        // [#x10000-#x10FFFF] /* any Unicode character, excluding the surrogate
        // blocks, FFFE, and FFFF. */
        
        if (c == '\n') return true;
        if (c == '\r') return true;
        if (c == '\t') return true;

        //NOTE: this was BROKEN!  The range between 0x80 and 0xFF is valid XML
        //and would end up dropping latin characters in UTF-8.  Why did I want
        //to return false here again?
        
        //if (c < 0x20) return false;  if (c < 0x80) return true;
        //if (c < 0xFF) return false; if (c <= 0xD7FF) return true;

        if (c < 0x20) return false;  if (c <= 0xD7FF) return true;
        if (c < 0xE000) return false;  if (c <= 0xFFFD) return true;
        if (c < 0x10000) return false;  if (c <= 0x10FFFF) return true;
        
        return false;

    }

}
