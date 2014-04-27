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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * A ResourceRequest is a generic interface to a network resource such as an
 * HTTP URL.
 * 
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id: ResourceRequest.java 373622 2006-01-30 22:53:00Z mvdb $
 */
public interface ResourceRequest {

    /**
     * Perform all initialization and connection to the remote server.  This
     * should always be called BEFORE network getInputStream() if you want to
     * perform other operations first.  When using a HEAD request this must be
     * used and not getInputStream()
     *
     * 
     */
    public void init() throws IOException;
    
    /**
     * Get an input stream for this content.
     *
     * 
     */
    public InputStream getInputStream() throws IOException;

    /**
     * Set the resource for this request.
     *
     * 
     */
    public String getResource();
    public void setResource( String resource );

    /**
     * Get the resource but make sure all redirects are taken into
     * consideration.
     *
     * 
     */
    public String getResourceFromRedirect();
    
    /**
     * Get the given Input Stream as a String by calling read() until we have
     * all the data locally.
     *
     * 
     */
    public String getInputStreamAsString() throws IOException;
    public byte[] getInputStreamAsByteArray() throws IOException;
    public InputStream getLocalInputStream() throws NetworkException;
    public byte[] getLocalInputStreamAsByteArray() throws IOException;

    /**
     * When true we cache getLocalInputStream() so that multiple requests are
     * returned from local data.  Provides more flexibility but uses more
     * memory.
     */
    public void setLocalCache( boolean v );

    /**
     * Copy this input stream to an OutputStream
     *
     * 
     */
    public void toOutputStream( OutputStream out ) throws IOException;

    /**
     * Set the If-Modified-Since header for HTTP URL connections and protocols
     * that support similar operation.
     *
     * A value of -1 means do not use the If-Modified-Since header
     *
     * Fri Jun 06 2003 08:34 PM (burton@peerfear.org): Currently just URLResourceRequest     
     *
     * 
     */
    public long getIfModifiedSince();
    public void setIfModifiedSince( long ifModifiedSince );

    /**
     * The HTTP ETag to use with If-None-Match
     *
     * 
     */
    public String getEtag();
    public void setEtag( String etag );
    
    /**
     * Get and set an HTTP style response code.  Only used with HTTP URLs.
     *
     * 
     */
    public long getResponseCode();
    public void setResponseCode( int responseCode );

    /**
     * Return the conent length of this request or -1 if not known.
     *
     * 
     */
    public int getContentLength() throws IOException;

    public void setEventListener( NetworkEventListener eventListener );

    /**
     * Get a given response header.
     *
     * 
     */
    public String getHeaderField( String name );

    /**
     * Set a given request header such as UserAgent, ETag, etc.
     *
     * 
     */
    public void setRequestHeaderField( String name, String value );

    /**
     * Get the names of all set request headers.
     *
     * 
     */
    public Iterator getRequestHeaderFields();

    public String getRequestHeaderField( String name );

    public void setRequestMethod( String method ) throws NetworkException;

    public boolean getFollowRedirects();
    public void setFollowRedirects( boolean v );
    
}
