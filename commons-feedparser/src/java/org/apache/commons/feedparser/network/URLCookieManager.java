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

package org.apache.commons.feedparser.network;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 
 * By default java.net.URL does NOT handle cookies.  This is a simple extension
 * that allows us to persist cookies in the VM during runtime.
 * 
 * FIXME: How can we make sure to delete older sites...?!  no need for this to
 * grow to infinite size.
 * 
 * @author <a href="mailto:burton@peerfear.org">Kevin A. Burton</a>
 */
public class URLCookieManager {

    static Hashtable cookies = new Hashtable();
    
    /**
     * Get the cookies for a site.  When none are available return null.
     *
     * 
     */
    public static Hashtable getCookies( String site ) {

        return (Hashtable)cookies.get( site );
        
    }

    /**
     * Add cookies to this request and perform any other init.
     *
     * 
     */
    public static void init( ResourceRequest request ) {

        String site = getSite( request );
        
        Hashtable cookies = getCookies( site );

        if ( cookies == null )
            return;

        String header = getCookiesHeader( cookies );

        request.setRequestHeaderField( "Cookies", header );
        
    }

    public static String getSite( ResourceRequest request ) {

        String resource = request.getResource();

        int end = resource.indexOf( "://" );
        end = resource.indexOf( "/", end );

        if ( end == -1 )
            end = resource.length();
        
        return resource.substring( 0, end );

    }
    
    /**
     * Save the cookies FROM this request.
     *
     * 
     */
    public static void save( ResourceRequest request ) {

        String header = request.getHeaderField( "Set-Cookie" );

        Hashtable cookies = parseCookieHeader( header );

        String site = getSite( request );

        //FIXME: merge these... new cookies into the site cookies 

    }

    /**
     * Parse a given Cookie header into a hashtable.
     *
     * 
     */
    public static String getCookiesHeader( Hashtable cookies ) {

        Enumeration keys = cookies.keys();

        StringBuffer buff = new StringBuffer( 1024 );

        while ( keys.hasMoreElements() ) {

            String name = (String)keys.nextElement();
            String value = (String)cookies.get( name );

            if ( buff.length() > 0 )
                buff.append( "; " );

            buff.append( name );
            buff.append( "=" );
            buff.append( value );
            
        }
        
        return buff.toString();

    }

    /**
     * Parse a given Cookie header into a hashtable.
     *
     * 
     */
    public static Hashtable parseCookieHeader( String header ) {

        //this is a simple format and easy to parse

        //Cookie: password=HvS11dffnlD50bOLZYgG4oZFA-U

        /**
         * Where should we read the cookie name from
         */
        int begin = 0;

        /**
         * Where do we spit into the variable
         */
        int split = 0;

        /**
         * Where is the end of the cookie.
         */
        int end = 0;

        Hashtable result = new Hashtable();
        
        while ( (split = header.indexOf( "=", begin )) != -1 ) {

            end = header.indexOf( ";", split );

            if ( end == -1 )
                end = header.length();
            
            String name = header.substring( begin, split );
            String value = header.substring( split+1, end );
            
            //move to the next one.
            begin = end + 2;

            result.put( name, value );
            
        }

        return result;
        
    }
    
    public static void main( String[] args ) {

        parseCookieHeader( "password=HvS11dffnlD50bOLZYgG4oZFA-U; username=burtonator; rojoWeb=12.43.53.196.1091730560640949; JSESSIONID=B1245A7FEB43537E994324A157330F3A" );
        
    }

}
