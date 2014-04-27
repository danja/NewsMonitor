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


/**
 * This is a portable and thin URL resolver.  The goal is to quickly resolve and
 * normalize URLs.  This includes potentially saving redirects and having them
 * fully qualified.
 * 
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id: URLResolver.java 373622 2006-01-30 22:53:00Z mvdb $
 */
public class URLResolver {

    public static String resolve( String resource ) {

        //include "www" in hostnames like xmlhack.com
        resource = resolveTrailingSlash( resource );
        resource = resolveNoHostname( resource );

        return resource;
        
    }

    private static String resolveNoHostname( String resource ) {

        if ( resource.startsWith( "http://" ) ) {

            int first = resource.indexOf( "." );
            int second = resource.indexOf( ".", first + 1 );

            if ( second == -1 ) {
                //then we don't have a hostname on this domain.
                return resource.substring( 0, "http://".length() ) +
                       "www." + 
                       resource.substring( "http://".length(), resource.length() );
            } 

        }

        return resource;
        
    }

    private static String resolveTrailingSlash( String resource ) {

        if ( resource.startsWith( "http://" ) && ( resource.endsWith( "org" ) ||
                                                   resource.endsWith( "com" ) ||
                                                   resource.endsWith( "net" ) ) ) {

            return resource + "/";
            
        } 

        return resource;
        
    }
    
}
