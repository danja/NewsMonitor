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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

import sun.net.www.protocol.http.HttpURLConnection;

/**
 * ResourceRequest implementation that uses java.net.URL as the backend.
 *
 * Differences from other ResourceRequests.
 *
 * setRequestMethod() - Allows us to change the request type (HEAD, etc).
 * 
 * getContentLength() - Returns the length/size of the content represented by
 * this resource.  Can be used by clients with setRequestMethod( "HEAD" ) to
 * find the size of a remote resource without doing a full fetch.
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id$
 */
public class URLResourceRequest extends BaseResourceRequest implements ResourceRequest {

    private static Logger log = Logger.getLogger( URLResourceRequest.class.getName() );

    public static final String ACCEPT_ENCODING_HEADER = "Accept-Encoding";
    public static final String IF_NONE_MATCH_HEADER = "If-None-Match";
    public static final String GZIP_ENCODING = "gzip";
    public static final String USER_AGENT_HEADER = "User-Agent";

    /**
     *
     * Enable RFC 3228 HTTP Delta for feeds.
     * 
     * http://bobwyman.pubsub.com/main/2004/09/using_rfc3229_w.html
     * 
     *  http://bobwyman.pubsub.com/main/2004/09/implementations.html
     * 
     */
    public static boolean ENABLE_HTTP_DELTA_FEED_IM = false;
    
    public static String USER_AGENT
        = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.2.1; aggregator:FeedParser; http://commons.apache.org/feedparser/) Gecko/20021130";

    public static String USER_AGENT_MOZILLA
        = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.2.1) Gecko/20021130";

    /**
     * Not used anymore.  Provided for historical reasons.
     */
    public static final String REFERER
        = "http://commons.apache.org/feedparser/?isAggregator=true";
    
    public static final int MAX_CONTENT_LENGTH = 1000000;
    
    private URL _url = null;

    private URLConnection _urlConnection = null;

    private InputStream inputStream = null;

    private boolean initConnection = false;
    
    /**
     * 
     * 
     */
    public void init() throws IOException {

        String resource = this.getResource();

        //if we are offline... we don't need to init.
        if ( ResourceRequestFactory.isOffline() ) { return; } 

        //pull from the HTCache if it is enabled and then short-circuit so that
        //we don't fetch from the network.

        //NOTE: currently removed because the htcache wasn't portable. I can OSS
        //this in the future if necessary

        // if ( ResourceRequestFactory.isTransparentHTCacheEnabled() &&
        //     HTCache.hasContentInCache( this.getResource() ) ) {
        //
        //    //get the input stream we can use from the HTCache.
        //    this.inputStream = HTCache.getContentAsInputStream( resource );
        //  return;
        //    
        // }

        _url = new URL( this.getResource() );
        _urlConnection = _url.openConnection();

    }

    /**
     * Init the actual connection.  Should be called AFTER init() but before
     * getInputStream() so that we can set any runtime params requestMethod,
     * etc.  If getInputStream() is called without an initConnection() we do
     * this automatically.  initConnection() might not want to be called when
     * doing a HEAD request.
     * 
     * 
     */
    public void initConnection() throws NetworkException {

        long before = System.currentTimeMillis();

        initConnection = true;

        this.fireInit();

        //FIXME: do smart user agent detection.  if this is a .html file we can
        //set it to us Mozilla and if not we can use NewsMonster
        //_urlConnection.setRequestProperty( "Referer", REFERER );

        String resource = this.getResource();

        //set the user agent if it hasn't ALREADY been set by the caller.
        if ( getRequestHeaderField( USER_AGENT_HEADER ) == null ) {
            _urlConnection.setRequestProperty( USER_AGENT_HEADER, USER_AGENT );
        } 

        _urlConnection.setRequestProperty( ACCEPT_ENCODING_HEADER, GZIP_ENCODING );

        //copy over any headers set in the request..

        Iterator it = getRequestHeaderFields();

        while ( it.hasNext() ) {

            String key = (String)it.next();

            _urlConnection.setRequestProperty( key, getRequestHeaderField( key ) );
            
        } 

        if ( _urlConnection instanceof HttpURLConnection ) {

            HttpURLConnection httpURLConn = (HttpURLConnection)_urlConnection;

            httpURLConn.setFollowRedirects( getFollowRedirects() );
            httpURLConn.setInstanceFollowRedirects( getFollowRedirects() );

            if ( this.getIfModifiedSince() != -1 )
                httpURLConn.setIfModifiedSince( this.getIfModifiedSince() );

            if ( getEtag() != null ) {
                httpURLConn.setRequestProperty( IF_NONE_MATCH_HEADER, getEtag() );

                //now support RFC3229 HTTP Delta
                //A-IM: feed, gzip

                if ( ENABLE_HTTP_DELTA_FEED_IM ) {

                    //note that this will return HTTP 226 if used.
                    //
                    
                    httpURLConn.setRequestProperty( "A-IM", "feed, gzip" );

                }

            }
            
            try {

                httpURLConn.connect();

                //setResource( getRedirectedResource() );
                
                this.setResponseCode( httpURLConn.getResponseCode() ); 

            } catch ( IOException e ) {
                throw new NetworkException( e );
            }

        } 

        int contentLength = _urlConnection.getContentLength();

        //bigger than 1 meg and it is a remote document (it is safe to process
        //local documents)
        if ( contentLength > MAX_CONTENT_LENGTH &&
             this.getResource().startsWith( "file:" ) == false ) {

            //NOTE: make 100% sure this doens't just go ahead and download the
            //file FIRST before doing a HEAD.  I think that's what happens but I
            //might be wrong.
            
            throw new NetworkException( "Content is too large - " + contentLength + " - " + getResource() );
            
        } 

        long after = System.currentTimeMillis();
        
        log.debug( getResource() + " - init duration: " + (after-before) );
        
    }

    java.lang.reflect.Field FIELD_HTTP_URL_CONNECTION_HTTP = null;
    java.lang.reflect.Field FIELD_HTTP_CLIENT_URL = null;
    
    /**
     * This method used Reflection to pull out the redirected URL in
     * java.net.URL.  Internally sun.net.www.protocol.http.HttpURLConnection
     * stores a reference to sun.net.www.http.HttpClient which then in turn does
     * all the redirection and stores the redirect java.net.URL.  We just use
     * reflection to FETCH this URL and then call toString to get the correct
     * value.
     * 
     * Java needs the concept of readonly private variables.
     *
     * 
     */
    public String getResourceFromRedirect() {

        try {

            if ( FIELD_HTTP_URL_CONNECTION_HTTP == null ) {

                //Note: when using a FILE URL this won't work!                
                FIELD_HTTP_URL_CONNECTION_HTTP = _urlConnection.getClass().getDeclaredField( "http" );
                FIELD_HTTP_URL_CONNECTION_HTTP.setAccessible( true );
                
            }

            Object http = FIELD_HTTP_URL_CONNECTION_HTTP.get( _urlConnection );

            //when java.net.URL has already cleaned itself up 'http' will be
            //null here.
            if ( http == null )
                return getResource();

            if ( FIELD_HTTP_CLIENT_URL == null ) {

                FIELD_HTTP_CLIENT_URL = http.getClass().getDeclaredField( "url" );
                FIELD_HTTP_CLIENT_URL.setAccessible( true );
                
            }
            
            Object url = FIELD_HTTP_CLIENT_URL.get( http );

            //this will be a java.net.URL and now I can call the toString method
            //on it which will return our full URI.
            return url.toString();
            
        } catch ( Throwable t ) {
            //log.error( t );
            return getResource();
        }
        
    }

    public InputStream getInputStream() throws NetworkException {

        try {
            return _getInputStream();

        } catch ( IOException e ) {

            String message = null;
            
            //the modern VM buries the FileNotFoundException which prevents a
            //catch.  Very very ugly.
            if ( e.getCause() instanceof FileNotFoundException ) {
                message = "File not found: " + e.getCause().getMessage();
            } else {
                message = e.getMessage();
            }

            throw new NetworkException( message, e, this, _url, _urlConnection );
        }

    }
    
    /**
     * 
     *
     * 
     */
    public InputStream _getInputStream() throws IOException {

        if ( ! initConnection ) { initConnection(); } 

        String resource = this.getResource();

        //if we haven't pulled from the cache (as above) and we are offline we
        //need to throw an exception.
        if ( ResourceRequestFactory.isOffline() ) {

            //see if we can return from the HTCache.
            // if ( ResourceRequestFactory.isTransparentHTCacheEnabled() &&
            //     HTCache.hasContentInCache( resource ) )
            //    return HTCache.getContentAsInputStream( resource );

            //if not we should throw an exception
            throw new IOException( "ResourceRequestFactory is offline and content was not in cache - " +
                                   resource );

        }

        //if we are using an input stream NOT from init() 
        if ( this.inputStream == null ) {
            
            this.inputStream = _urlConnection.getInputStream();
            this.inputStream = new AdvancedInputStream( this.inputStream, this );

            //first decompress
            if ( GZIP_ENCODING.equals( _urlConnection.getContentEncoding() ) ) {

                //note.  the advanced input stream must be wrapped by a GZIP
                //input stream and not vice-versa or we will end up with
                //incorrect results.
                
                this.inputStream = new GZIPInputStream( this.inputStream );

            }
        
            // if ( ResourceRequestFactory.isTransparentHTCacheEnabled() ) {
                
            //     System.out.println( "cache store for: " +
            //                         resource + " as " +
            //                         HTCache.getContentAsPath( resource ) );

            //     //FIXME: performance improvement... don't write do disk and then
            //     //read from disk.?
                
            //     //store this content from the network and save it in the cache.  Then fetch it and return
            //     HTCache.store( resource, this.inputStream );
                
            //     return HTCache.getContentAsInputStream( resource );
                
            // }

        }

        setResource( getResourceFromRedirect() );

        //this is potentially teh cached input stream created if we have used
        //the HTCache.
        return inputStream;
        
    }

    /**
     * Set the RequestMethod of this URLConnection.
     *
     * 
     */
    public void setRequestMethod( String method ) throws NetworkException {

        try { 
            
            if ( _urlConnection instanceof HttpURLConnection ) {
                
                ((HttpURLConnection)_urlConnection).setRequestMethod( method );
                
            } 
            
        } catch ( ProtocolException pe ) {
            
            NetworkException ne = new NetworkException( pe.getMessage() );
            ne.initCause( pe );
            throw ne;
            
        }

    }

    /**
     * 
     *
     * 
     */
    public int getContentLength() throws IOException {

        if ( ! initConnection ) { initConnection(); } 

        //if ( _urlConnection instanceof HttpURLConnection ) {

        return  _urlConnection.getContentLength();
        
    }
    
    public String getHeaderField( String name ) {
        return  _urlConnection.getHeaderField( name );
    }

}
