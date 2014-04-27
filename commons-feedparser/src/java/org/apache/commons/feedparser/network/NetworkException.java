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
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

/**
 * 
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id: NetworkException.java 373622 2006-01-30 22:53:00Z mvdb $
 */
public class NetworkException extends IOException {

    private static Logger log = Logger.getLogger( NetworkException.class );

    private ResourceRequest request = null;

    public Exception e = null;

    private URL _url = null;

    private URLConnection _urlConnection = null;

    private int responseCode = -1;

    /**
     * 
     * Create a new <code>NetworkException</code> instance.
     *
     * 
     */
    public NetworkException( String message ) {
        super( message );
    }

    public NetworkException( Throwable t) {
        super( t.getMessage() );
    }

    /**
     * 
     * Create a new <code>NetworkException</code> instance.
     *
     * 
     */
    public NetworkException( String message,
                             Exception e,
                             ResourceRequest request,
                             URL _url,
                             URLConnection _urlConnection ) {

        super( message ); //why doesn't java.io.IOException support nesting?
        this.e = e;
        this.request = request;
        this._url = _url;
        this._urlConnection = _urlConnection;
        initCause( e );
        
    }

    /**
     * 
     * Create a new <code>NetworkException</code> instance.
     *
     * 
     */
    public NetworkException( Exception e,
                             ResourceRequest request,
                             URL _url,
                             URLConnection _urlConnection ) {

        super( e.getMessage() ); //why doesn't java.io.IOException support nesting?
        this.e = e;
        this.request = request;
        this._url = _url;
        this._urlConnection = _urlConnection;
        initCause( e );
        
    }

    public ResourceRequest getResourceRequest() {
        return request;
    }

    public URL getURL() {
        return _url;
    }

    public URLConnection getURLConnection() {
        return _urlConnection;
    }

    public Exception getException() {
        return e;
    }

    public int getResponseCode() {

        //FIXME: 
        //        java.lang.NumberFormatException: For input string: "fie"
        //         at java.lang.NumberFormatException.forInputString(NumberFormatException.java:48)
        //         at java.lang.Integer.parseInt(Integer.java:468)
        //         at java.lang.Integer.parseInt(Integer.java:518)
        //         at org.peerfear.newsmonster.network.NetworkException.getResponseCode(NetworkException.java:142)
        //         at ksa.robot.FeedTask._doTaskLogFailure(FeedTask.java:264)
        //         at ksa.robot.FeedTask.run(FeedTask.java:202)
        //         at ksa.robot.TaskThread.doProcessTask(TaskThread.java:298)
        //         at ksa.robot.TaskThread.run(TaskThread.java:111)
        
        if ( _urlConnection == null ) {
            return -1;
        } 

        if ( responseCode == -1 ) {

            //parse the exception
            String status = (String)_urlConnection.getHeaderField( null );

            if ( status == null ) {
                return -1;
            } 

            int begin = "HTTP/1.1 ".length();
            int offset = "200".length();
            int end = begin + offset;

            try {

                responseCode = Integer.parseInt( status.substring( begin, end ) );
                
            } catch ( NumberFormatException e ) {

                log.warn( "Unable to parse response code in header: " + status );
                return -1;
                
            }

        } 

        return responseCode;
        
    }
    
}
