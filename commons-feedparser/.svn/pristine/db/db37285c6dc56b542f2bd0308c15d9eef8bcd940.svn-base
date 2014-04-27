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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * Get a ResourceRequest for a given URL.  The request is handled based on the
 * URL.
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id$
 */
public class ResourceRequestFactory {

    private static Logger log = Logger.getLogger( ResourceRequestFactory.class.getName() );

    /**
     * Specified in java.security to indicate the caching policy for successful
     * name lookups from the name service.. The value is specified as as integer
     * to indicate the number of seconds to cache the successful lookup.
     * 
     *
     * sun.net.inetaddr.ttl:
     * 
     * This is a sun private system property which corresponds to
     * networkaddress.cache.ttl. It takes the same value and has the same meaning,
     * but can be set as a command-line option. However, the preferred way is to
     * use the security property mentioned above.
     * 
     * A value of -1 indicates "cache forever".
     */
    public static int NETWORKADDRESS_CACHE_TTL = 5 * 60;
    
    /**
     * These properties specify the default connect and read timeout (resp.) for
     * the protocol handler used by java.net.URLConnection.
     * 
     * sun.net.client.defaultConnectTimeout specifies the timeout (in
     * milliseconds) to establish the connection to the host. For example for
     * http connections it is the timeout when establishing the connection to
     * the http server. For ftp connection it is the timeout when establishing
     * the connection to ftp servers.
     * 
     * sun.net.client.defaultReadTimeout specifies the timeout (in milliseconds)
     * when reading from input stream when a connection is established to a
     * resource.
     */
    public static int DEFAULT_CONNECT_TIMEOUT = 1 * 60 * 1000;

    public static int DEFAULT_READ_TIMEOUT = DEFAULT_CONNECT_TIMEOUT;

    /**
     * Specify the maximum number of redirects to use.
     */
    public static int DEFAULT_MAX_REDIRECTS = 5;

    //FIXME: (should this be a linked list?)
    private static ArrayList listeners = new ArrayList( 30 );

    private static HashMap schemeMap = null;

    private static boolean transparentHTCacheEnabled = false;

    /**
     * When offline we either throw an exception or return content from the
     * cache directly.  This can be used to run code that does not depend on the
     * network.
     */
    private static boolean offline = false;

    public static ResourceRequest getResourceRequest( String resource,
                                                      long ifModifiedSince ) throws NetworkException {

        return getResourceRequest( resource, ifModifiedSince, null );
        
    }

    /**
     * Get a ResourceRequest for the protocol represented in the resource URL.
     * It is important that we use a ResourceRequest implementation that supports
     * fetching the URL.
     *
     * 
     */
    public static ResourceRequest getResourceRequest( String resource,
                                                      long ifModifiedSince,
                                                      String etag ) throws NetworkException {

        //log.debug( resource );
        
        //make sure we are initialized correctly.
        ResourceRequestFactory.init();

        //make sure we have an index..

        int schemeIndex = resource.indexOf( ":" );

        if ( schemeIndex == -1 )
            throw new NetworkException( "Unknown scheme: '" + resource + "'" );

        String scheme = resource.substring( 0, schemeIndex );

        if ( scheme == null || scheme.equals( "" ) )
            throw new MalformedResourceException( "Not supported: " + resource );

        Class clazz = (Class)schemeMap.get( scheme );

        if ( clazz == null ) {
            throw new MalformedResourceException( "Scheme not supported: " + scheme );
        } 

        try { 
            
            ResourceRequest request = (ResourceRequest)clazz.newInstance();

            request.setResource( resource );

            //setup resource request options.
            request.setIfModifiedSince( ifModifiedSince );

            //set the etag... when its null nothing will happen
            request.setEtag( etag );
            
            request.init();

            return request;

        } catch ( Throwable t ) {
            throw new NetworkException( t );
        }

    }

    /**
     * 
     * @see #getResourceRequest( String )
     * 
     */
    public static ResourceRequest getResourceRequest( String resource ) throws NetworkException {
        return getResourceRequest( resource, -1 );
    }
    
    /**
     * 
     * @see #getResourceRequest( String )
     * 
     */
    public static ResourceRequest getResourceRequest( URL resource ) throws NetworkException {
        return getResourceRequest( resource.toString() );
    }

    /**
     * Add an event listener to this instance of the factory.  This provides a
     * mechanism to give default listeners to each new ResourceRequest.
     *
     * 
     */
    public static void addEventListener( NetworkEventListener listener ) {

        listeners.add( listener );

    }

    /**
     * Get all event listeners.
     *
     * 
     */
    public static Iterator getNetworkEventListeners() {

        return listeners.iterator();
        
    }
    
    /**
     * Make sure the factory is initialized.  Called once per JVM instance.
     *
     * 
     */
    private synchronized static void init() {

        //set the authenticator to use

        //FIXME: remove this until we figure out how to do proxy authentication.
        //java.net.Authenticator.setDefault ( new Authenticator() );

        // A full list of properties is available here:

        // http://java.sun.com/j2se/1.4.2/docs/guide/net/properties.html

        System.setProperty( "sun.net.inetaddr.ttl",
                            Integer.toString( NETWORKADDRESS_CACHE_TTL ) );

        System.setProperty( "networkaddress.cache.ttl",
                            Integer.toString( NETWORKADDRESS_CACHE_TTL ) );

        System.setProperty( "sun.net.client.defaultReadTimeout",
                            Integer.toString( DEFAULT_READ_TIMEOUT ) );

        System.setProperty( "sun.net.client.defaultConnectTimeout",
                            Integer.toString( DEFAULT_CONNECT_TIMEOUT ) );

        System.setProperty( "http.maxRedirects",
                            Integer.toString( DEFAULT_MAX_REDIRECTS ) );

        if ( schemeMap == null ) {
            
            schemeMap = new HashMap();
            
            schemeMap.put( "file", URLResourceRequest.class );
            schemeMap.put( "http", URLResourceRequest.class );
            schemeMap.put( "https", URLResourceRequest.class );
            schemeMap.put( BlockingResourceRequest.SCHEME, BlockingResourceRequest.class );
            
        }

    }

    /**
     * Return true if we support fetching content with the given scheme.
     * Examples would be "http" and "file"
     *
     * 
     */
    public static boolean isSupportedScheme( String scheme ) {

        return schemeMap.get( scheme ) != null;
        
    }
    
    /**
     * When the transparent HTCache is enabled we will keep content local
     * similar to the Mozilla cache and return the cached copy and use
     * if-Modified-Since when necessary.
     *
     * 
     */
    public static void setTransparentHTCacheEnabled( boolean enabled ) {
        transparentHTCacheEnabled = enabled;
    }

    /**
     * Return true if we can enable the htcache.
     *
     * 
     */
    public static boolean isTransparentHTCacheEnabled() {
        return transparentHTCacheEnabled;
    }

    /**
     * Enable/disable offline operation.
     *
     * 
     */
    public static void setOffline( boolean offline ) {
        ResourceRequestFactory.offline = offline;
    }

    /**
     * 
     *
     * 
     */
    public static boolean isOffline() {
        return offline;
    }

}
