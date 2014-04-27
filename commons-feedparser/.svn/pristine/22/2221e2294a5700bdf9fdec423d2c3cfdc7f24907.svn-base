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

package org.apache.commons.feedparser.post;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcClient;

/**
 * A PostAgent allows a developer to post to a given weblog.
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public class MetaWeblogPostAgent {

    public void newPost( String router,
                         String weblog,
                         String username,
                         String password, 
                         PostEntry entry ) throws Exception {

        XmlRpcClient xmlrpc = new XmlRpcClient( router );

        Vector params = new Vector();
        params.add( weblog); 
        params.add( username );
        params.add( password );

        Hashtable struct = new Hashtable();

        struct.put( "title", entry.title );
        struct.put( "description", entry.description );

        params.add( struct );
        params.add( new Boolean( true ) );
        
        Vector v = (Vector)xmlrpc.execute( "metaWeblog.newPost", params );

    }
}

