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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 *
 * This is an exprimental ResourceRequest which used Jakarta HttpClient as the
 * backend.  Current its only meant for use in development as its not as
 * reliable and stable as the URLResourceRequest.
 * 
 * Most of this code isn't as functional as the URLResourceRequest including
 * correct timeout behavior, redirect limits, header and etag support, etc.
 * 
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id: HTTPClientNetworkResource.java 373622 2006-01-30 22:53:00Z mvdb $
 */
public class HTTPClientNetworkResource extends BaseResourceRequest implements ResourceRequest {

    public static final int TIMEOUT = 3 * 1000 * 60;
    
    private HttpClient client = new HttpClient();
    
    /**
     * 
     * Create a new <code>URLNetworkResource</code> instance.
     *
     * 
     */
    public void init() throws NetworkException {

        try { 

            client.setConnectionTimeout( TIMEOUT );
            client.setStrictMode( false );
            //client.setFollowRedirects( true );

        } catch ( Exception e ) {

            throw new NetworkException( e );
            
        }

    }

    /**
     * 
     *
     * 
     */
    public InputStream getInputStream() throws IOException {

        try {

            //now get the method so that we can execute it.
            HttpMethod method = new GetMethod( getResource() );
            method.setFollowRedirects( true );

            int result = client.executeMethod( method);

            if ( result >= 400 && result < 500 ) {
                throw new NetworkException( "HTTP " + result + " - " + method.getStatusText() );
            } 

            InputStream is = new AdvancedInputStream( method.getResponseBodyAsStream(), this );

            return is;

        } catch ( Exception e ) {
            
            throw new NetworkException( e );

        }
        
    }

}
