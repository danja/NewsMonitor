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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id$
 */
public abstract class BaseResourceRequest implements ResourceRequest {

    public static boolean FOLLOW_REDIRECTS = true;
    
    private String resource = null;

    private DataEvent event = new DataEvent();

    private long _ifModifiedSince = -1;

    private long _responseCode = HttpURLConnection.HTTP_OK;

    private String _etag = null;
    
    private byte[] data = new byte[0];

    private boolean localCache = false;

    private boolean followRedirects = FOLLOW_REDIRECTS;

    /**
     * A single resource request can now have a given event listener.
     */
    private NetworkEventListener eventListener = null;

    private HashMap requestHeaders = new HashMap();
    
    /**
     * 
     * Get the value of <code>resource</code>.
     *
     * 
     */
    public String getResource() { 
        
        return this.resource;
        
    }

    /**
     * 
     * Set the value of <code>resource</code>.
     *
     * 
     */
    public void setResource( String resource ) { 
        
        this.resource = resource;
        
    }

    /**
     * Fire a new ArchiveEvent
     *
     * 
     */
    public void fireDataEvent( long count ) {

        event.count = count;
        event.resource = resource;
        
        fireDataEvent( event );
        
    }

    public void fireInit() {

        DataEvent event = new DataEvent();
        event.request = this;
        
        Iterator i = ResourceRequestFactory.getNetworkEventListeners();

        while ( i.hasNext() ) {
            ((NetworkEventListener)i.next()).init( event );
        } 

        if ( eventListener != null )
            eventListener.init( event );
    }

    /**
     * Fire a new ArchiveEvent
     *
     * 
     */
    public void fireDataEvent( DataEvent event ) {

        event.request = this;
        
        Iterator i = ResourceRequestFactory.getNetworkEventListeners();

        while ( i.hasNext() ) {
            ((NetworkEventListener)i.next()).dataEvent( event );
        } 

        if ( eventListener != null )
            eventListener.dataEvent( event );
    }

    public void fireOnClosed() {

        Iterator i = ResourceRequestFactory.getNetworkEventListeners();

        while ( i.hasNext() ) {
            ((NetworkEventListener)i.next()).onClosed();
        } 

        if ( eventListener != null )
            eventListener.onClosed();
    }

    /**
     * @see ResourceRequest
     * 
     */
    public String getInputStreamAsString() throws IOException {
        return new String( getInputStreamAsByteArray() );
    }

    /**
     * @see ResourceRequest
     * 
     */
    public byte[] getInputStreamAsByteArray() throws IOException {

        InputStream is = getInputStream();

        int contentLength = -1;

        try {

            contentLength = getContentLength() + 5000;

        } catch ( IOException e ) { e.printStackTrace(); }

        if ( contentLength == -1  ) {

            //use a larger default than what's provided with the
            //ByteArrayOutputStream

            contentLength = 100000;
        } 

        //include length of content from the original site with contentLength
        ByteArrayOutputStream bos = new ByteArrayOutputStream( contentLength );
      
        //now process the Reader...
        byte data[] = new byte[200];
    
        int readCount = 0;

        while( ( readCount = is.read( data )) > 0 ) {
            bos.write( data, 0, readCount );
        }

        is.close();
        bos.close();

        return bos.toByteArray();

    }

    /**
     * @see ResourceRequest
     * 
     */
    public InputStream getLocalInputStream() throws NetworkException {

        try { 
            
            byte[] data;
            
            if ( this.data.length > 0 ) {
                
                //we have cached this... return the cached value.
                data = this.data;
                
            } else {
                
                data = getInputStreamAsByteArray();
                
                if ( localCache )
                    this.data = data;
                
            }
            
            return new ByteArrayInputStream( data );
            
        } catch ( NetworkException n ) {
            throw n;
        } catch ( Throwable t ) {
            throw new NetworkException( t );
        }

    }

    public byte[] getLocalInputStreamAsByteArray() throws IOException {
        //FIXME: this needs to use the cache.
        return this.data;
    }

    public void setLocalCache( boolean v ) {
        this.localCache = v;
    }
    
    /**
     * Copy this resource request to the given OutputStream
     *
     * 
     */
    public void toOutputStream( OutputStream out ) throws IOException {

        InputStream is = getInputStream();
        
        //now process the Reader...
        byte data[] = new byte[200];
    
        int readCount = 0;

        while( ( readCount = is.read( data )) > 0 ) {
            
            out.write( data, 0, readCount );
        }

        is.close();

    }

    public long getIfModifiedSince() {
        return _ifModifiedSince;
    }

    public void setIfModifiedSince( long ifModifiedSince ) {
        this._ifModifiedSince = ifModifiedSince;
    }

    public String getEtag() {
        return _etag;
    }
    
    public void setEtag( String etag ) {
        this._etag = etag;
    }

    /**
     * Get and set an HTTP style response code.  Only used with HTTP URLs.
     *
     * 
     */
    public long getResponseCode() {
        return this._responseCode;
    }
    
    public void setResponseCode( int responseCode ) {
        this._responseCode = responseCode;
    }

    public int getContentLength() throws IOException {
        return -1;
    }

    public void setEventListener( NetworkEventListener eventListener ) {
        this.eventListener = eventListener;
    }
    
   public String getHeaderField( String name ) {
       //default impl always returns null
       return  null;
    }

    public void setRequestHeaderField( String name, String value ) {
        requestHeaders.put( name, value );
    }

    public Iterator getRequestHeaderFields() {
        return requestHeaders.keySet().iterator();
    }

    public String getRequestHeaderField( String name ) {
        return (String)requestHeaders.get( name );
    }

    public void setRequestMethod( String method ) throws NetworkException {
        throw new NetworkException( "not implemented" );
    }

    public boolean getFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects( boolean v ) {
        this.followRedirects = v;
    }

    public String getResourceFromRedirect() {
        return getResource();
    }
    
}
