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

package org.apache.commons.feedparser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.feedparser.impl.DebugFeedParserListener;

/**
 * Provides a mechanism to quickly test feed parsing from the command line.
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: Main.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class Main {

    public static void main( String[] args ) throws Exception {

        FeedParser parser = FeedParserFactory.newFeedParser();

        FeedParserListener listener = new DebugFeedParserListener();

        if ( args.length != 1 ) {

            System.err.println( "SYNTAX: " + Main.class.getName() + " file|url" );
            
            System.exit( 1 );
            
        }
        
        String resource = args[0];

        InputStream is = null;

        if ( resource.startsWith( "http://" ) ) {
            is = new URL( resource ).openStream();
        } else {

            System.out.println( "Opening from file: " + resource );
            is = new FileInputStream( resource );
        }

        parser.parse( listener, is, resource );

    }

}
