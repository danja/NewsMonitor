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

/**
 * 
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id: AdvancedInputStream.java 373622 2006-01-30 22:53:00Z mvdb $
 */
public class AdvancedInputStream extends InputStream {

    private InputStream is = null;

    private BaseResourceRequest request = null;

    /**
     * 
     * Create a new <code>AdvancedInputStream</code> instance.
     *
     * 
     */
    public AdvancedInputStream( InputStream is, ResourceRequest request ) {
        this.is = is;
        this.request = (BaseResourceRequest)request;
    }

    public int read() throws IOException {

        int v = is.read();

        request.fireDataEvent( 1 ); //one byte is read

        return v;
    }

    public int read( byte b[] ) throws IOException {

        int v = is.read( b );
        
        request.fireDataEvent( b.length );

        return v;

    }

    public int read( byte b[], int off, int len ) throws IOException {

        int v = is.read( b, off, len ); 

        request.fireDataEvent( len );

        return v;

    }

    public long skip( long n ) throws IOException {

        long v = is.skip( n );

        request.fireDataEvent( n );

        return v;
    }

    //non-read related
    
    public int available() throws IOException {
        return is.available();
    }

    public void close() throws IOException {
        is.close();

        request.fireOnClosed();
    }

    public void mark(int readlimit) {
        is.mark( readlimit );
    }

    public void reset() throws IOException {
        is.reset();
    }

    public boolean markSupported() {
        return is.markSupported();
    }

}
