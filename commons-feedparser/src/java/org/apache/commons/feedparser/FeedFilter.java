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

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: FeedFilter.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class FeedFilter {

    private static Logger log = Logger.getLogger( FeedFilter.class );

    public static boolean DO_REMOVE_LEADING_PROLOG = true;
    public static boolean DO_DECODE_ENTITIES = true;

    public static HashMap LATIN1_ENTITIES = new HashMap();

    private static Pattern entity_pattern = Pattern.compile( "&([a-zA-Z]+);" );

    /**
     * This is probably the wrong behavior.  I shouldn't call this method I
     * think because assuming a content type is bad form.
     *
     * @deprecated Specify an encoding with #parse( bytes[], encoding )
     * 
     */
    public static byte[] parse( byte[] bytes )
        throws Exception {

        return parse( bytes, "UTF-8" );

    }

    public static byte[] parse( byte[] bytes, String encoding )
        throws Exception {

        String content = new String( bytes, encoding );

        return parse( content, encoding );

    }

    /**
     * Parse out an input string of content.
     * 
     * http://wiki.apache.org/jakarta-commons/FeedParser_2fStringAllocationConsideredHelpful
     *
     * 
     */
    public static byte[] parse( String content, String encoding )
        throws Exception {

        //FIXME: return an object here so that I can flag a bozo bit.
        
        //remove leading prolog...
        if ( DO_REMOVE_LEADING_PROLOG )
            content = doRemoveLeadingProlog( content, encoding );

        //decode HTML entities that are referenced.
        if ( DO_DECODE_ENTITIES )
            content = doDecodeEntities( content );

        //TODO: undeclared namespace prefixes should be expanded to their common
        //form. 'rdf, 'atom', 'xhtml' etc. Considering that they're will only be
        //a handful H and then 4^36 different possibilities the probability will
        //only be H in 4^36 which is pretty good that we won't have a false
        //positive.
        
        return content.getBytes( encoding );

    }
        
    /**
     * Removing prolog whitespace, comments, and other garbage from the
     * beginning of a feed.
     *
     * 
     */
    private static String doRemoveLeadingProlog( String content, String encoding ) {

        // if we're a UTF-16 or UTF-32 feed we need to LEAVE the prolog because
        // it triggers a UTF-16 parse due to the BOM.
        //
        // FIXME: this isn't actually true.  We should leave the BOM and remove
        // the prolog anyway due to the fact that this will still break the
        // parser.  Come up with some tests for UTF-16 to see if I can get it to
        // break and then update this method.

        if ( "UTF-16".equals( encoding ) ||
             "UTF-32".equals( encoding ) )
            return content;
        
        //move to the beginning of the first element or comment.  When this is a
        //processing instruction we will move to that
        int begin = content.indexOf( "<" );

        if ( begin > 0 ) {
            content = content.substring( begin, content.length() );
            log.warn( "Skipped whitespace in prolog and moved towards first element." );
        }

        //now skip to the XML processing instruction when necessary.  This is
        //used to remove comments prior to <?xml which are not allowed.
        
        begin = content.indexOf( "<?xml" );

        if ( begin > 0 ) {
            content = content.substring( begin, content.length() );
            log.warn( "Removed prolog towards first processing instruction." );
        }

        content = doRemoveElementProlog( content );

        return content;
        
    }

    /**
     * Remove element content between:
     *
     * <?xml version="1.0"?>
     *
     * THIS IS BROKEN PROLOG
     *
     * <foo>
     *
     * 
     */
    private static String doRemoveElementProlog( String content ) {

        int end = content.lastIndexOf( "?>", 100 );

        if ( end == -1 )
            return content;

        StringBuffer buff = new StringBuffer( content.length() );
        end = end + 2;
        buff.append( content.substring( 0, end ) );

        int begin = content.indexOf( "<", end );

        if ( begin != -1 ) {

            buff.append( "\n" );
            buff.append( content.substring( begin, content.length() ) );
            
        }
        
        return buff.toString();
        
    }
    
    private static String doDecodeEntities( String content ) {

        StringBuffer buff = new StringBuffer( content.length() + 1000 );

        Matcher m = entity_pattern.matcher( content );

        int begin = 0;

        boolean hasFilterDecodedEntities = false;
        boolean hasFilterFoundUnknownEntity = false;

        //FIXME: note that when I was benchmarking this code that this showed up
        //as a MAJOR bottleneck so we might want to optimize it a little more.

        while ( m.find() ) {

            buff.append( content.substring( begin, m.start() ) );
            
            String entity = m.group( 1 );

            String value = (String)LATIN1_ENTITIES.get( entity );

            if ( value != null ) {
                buff.append( "&#" );
                buff.append( value );
                buff.append( ";" );

                hasFilterDecodedEntities = true;

            } else {

                //This is not a known entity so we have no way to correct it.
                //If this is done then we have a problem and the feed probably
                //still won't parse
                buff.append( "&" );
                buff.append( entity );
                buff.append( ";" );

                hasFilterFoundUnknownEntity = true;
            }

            begin = m.end( 0 );
            
        } 

        buff.append( content.substring( begin, content.length() ) );

        if ( hasFilterFoundUnknownEntity ) 
            log.warn( "Filter encountered unknown entities" );

        if ( hasFilterDecodedEntities ) 
            log.warn( "Filter has decoded latin1 entities." );

        return buff.toString();
        
    }
    
    public static void main( String[] args ) throws Exception {

        byte[] b = parse( "hello &eacute; world".getBytes() );

        String v = new String( b );

        System.out.println( "v: " + v );
        
    }
    
    static {

        // load the latin1 entity map.  We will replace latin1 entities with
        // their char references directly.  For example if someone incorrectly
        // references:
        //
        // &auml;
        //
        // we replace it with:
        //
        // &#228;
        //
        // Which is correct in Latin1

        // http://my.netscape.com/publish/formats/rss-0.91.dtd

        LATIN1_ENTITIES.put( "nbsp",      "160" );
        LATIN1_ENTITIES.put( "iexcl",     "161" );
        LATIN1_ENTITIES.put( "cent",      "162" );
        LATIN1_ENTITIES.put( "pound",     "163" );
        LATIN1_ENTITIES.put( "curren",    "164" );
        LATIN1_ENTITIES.put( "yen",       "165" );
        LATIN1_ENTITIES.put( "brvbar",    "166" );
        LATIN1_ENTITIES.put( "sect",      "167" );
        LATIN1_ENTITIES.put( "uml",       "168" );
        LATIN1_ENTITIES.put( "copy",      "169" );
        LATIN1_ENTITIES.put( "ordf",      "170" );
        LATIN1_ENTITIES.put( "laquo",     "171" );
        LATIN1_ENTITIES.put( "not",       "172" );
        LATIN1_ENTITIES.put( "shy",       "173" );
        LATIN1_ENTITIES.put( "reg",       "174" );
        LATIN1_ENTITIES.put( "macr",      "175" );
        LATIN1_ENTITIES.put( "deg",       "176" );
        LATIN1_ENTITIES.put( "plusmn",    "177" );
        LATIN1_ENTITIES.put( "sup2",      "178" );
        LATIN1_ENTITIES.put( "sup3",      "179" );
        LATIN1_ENTITIES.put( "acute",     "180" );
        LATIN1_ENTITIES.put( "micro",     "181" );
        LATIN1_ENTITIES.put( "para",      "182" );
        LATIN1_ENTITIES.put( "middot",    "183" );
        LATIN1_ENTITIES.put( "cedil",     "184" );
        LATIN1_ENTITIES.put( "sup1",      "185" );
        LATIN1_ENTITIES.put( "ordm",      "186" );
        LATIN1_ENTITIES.put( "raquo",     "187" );
        LATIN1_ENTITIES.put( "frac14",    "188" );
        LATIN1_ENTITIES.put( "frac12",    "189" );
        LATIN1_ENTITIES.put( "frac34",    "190" );
        LATIN1_ENTITIES.put( "iquest",    "191" );
        LATIN1_ENTITIES.put( "Agrave",    "192" );
        LATIN1_ENTITIES.put( "Aacute",    "193" );
        LATIN1_ENTITIES.put( "Acirc",     "194" );
        LATIN1_ENTITIES.put( "Atilde",    "195" );
        LATIN1_ENTITIES.put( "Auml",      "196" );
        LATIN1_ENTITIES.put( "Aring",     "197" );
        LATIN1_ENTITIES.put( "AElig",     "198" );
        LATIN1_ENTITIES.put( "Ccedil",    "199" );
        LATIN1_ENTITIES.put( "Egrave",    "200" );
        LATIN1_ENTITIES.put( "Eacute",    "201" );
        LATIN1_ENTITIES.put( "Ecirc",     "202" );
        LATIN1_ENTITIES.put( "Euml",      "203" );
        LATIN1_ENTITIES.put( "Igrave",    "204" );
        LATIN1_ENTITIES.put( "Iacute",    "205" );
        LATIN1_ENTITIES.put( "Icirc",     "206" );
        LATIN1_ENTITIES.put( "Iuml",      "207" );
        LATIN1_ENTITIES.put( "ETH",       "208" );
        LATIN1_ENTITIES.put( "Ntilde",    "209" );
        LATIN1_ENTITIES.put( "Ograve",    "210" );
        LATIN1_ENTITIES.put( "Oacute",    "211" );
        LATIN1_ENTITIES.put( "Ocirc",     "212" );
        LATIN1_ENTITIES.put( "Otilde",    "213" );
        LATIN1_ENTITIES.put( "Ouml",      "214" );
        LATIN1_ENTITIES.put( "times",     "215" );
        LATIN1_ENTITIES.put( "Oslash",    "216" );
        LATIN1_ENTITIES.put( "Ugrave",    "217" );
        LATIN1_ENTITIES.put( "Uacute",    "218" );
        LATIN1_ENTITIES.put( "Ucirc",     "219" );
        LATIN1_ENTITIES.put( "Uuml",      "220" );
        LATIN1_ENTITIES.put( "Yacute",    "221" );
        LATIN1_ENTITIES.put( "THORN",     "222" );
        LATIN1_ENTITIES.put( "szlig",     "223" );
        LATIN1_ENTITIES.put( "agrave",    "224" );
        LATIN1_ENTITIES.put( "aacute",    "225" );
        LATIN1_ENTITIES.put( "acirc",     "226" );
        LATIN1_ENTITIES.put( "atilde",    "227" );
        LATIN1_ENTITIES.put( "auml",      "228" );
        LATIN1_ENTITIES.put( "aring",     "229" );
        LATIN1_ENTITIES.put( "aelig",     "230" );
        LATIN1_ENTITIES.put( "ccedil",    "231" );
        LATIN1_ENTITIES.put( "egrave",    "232" );
        LATIN1_ENTITIES.put( "eacute",    "233" );
        LATIN1_ENTITIES.put( "ecirc",     "234" );
        LATIN1_ENTITIES.put( "euml",      "235" );
        LATIN1_ENTITIES.put( "igrave",    "236" );
        LATIN1_ENTITIES.put( "iacute",    "237" );
        LATIN1_ENTITIES.put( "icirc",     "238" );
        LATIN1_ENTITIES.put( "iuml",      "239" );
        LATIN1_ENTITIES.put( "eth",       "240" );
        LATIN1_ENTITIES.put( "ntilde",    "241" );
        LATIN1_ENTITIES.put( "ograve",    "242" );
        LATIN1_ENTITIES.put( "oacute",    "243" );
        LATIN1_ENTITIES.put( "ocirc",     "244" );
        LATIN1_ENTITIES.put( "otilde",    "245" );
        LATIN1_ENTITIES.put( "ouml",      "246" );
        LATIN1_ENTITIES.put( "divide",    "247" );
        LATIN1_ENTITIES.put( "oslash",    "248" );
        LATIN1_ENTITIES.put( "ugrave",    "249" );
        LATIN1_ENTITIES.put( "uacute",    "250" );
        LATIN1_ENTITIES.put( "ucirc",     "251" );
        LATIN1_ENTITIES.put( "uuml",      "252" );
        LATIN1_ENTITIES.put( "yacute",    "253" );
        LATIN1_ENTITIES.put( "thorn",     "254" );
        LATIN1_ENTITIES.put( "yuml",      "255" );

    }
    
}
