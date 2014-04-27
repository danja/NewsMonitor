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
 * A blocking request that waits for a specified or random amount of time.
 * 
 * SCHEME - block://localhost/?duration=100
 * 
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id: BlockingResourceRequest.java 373622 2006-01-30 22:53:00Z mvdb $
 */
public class BlockingResourceRequest extends BaseResourceRequest implements ResourceRequest {
    
    public static final String SCHEME = "block";
    
    /**
     * 
     * 
     */
    public void init() throws IOException {

        String resource = getResource();

    }

    /**
     * 
     *
     * 
     */
    public InputStream getInputStream() throws IOException {

        try { 
            
            Thread.sleep( 100 );
            
            return null;
            
        } catch ( Throwable t ) {

            IOException e = new IOException( t.getMessage() );
            e.initCause( t );

            throw e;
            
        }

    }

    public static void main( String[] args ) {

        try { 
            
        } catch ( Throwable t ) {
            
            t.printStackTrace();
            
        }
        
    }

}

