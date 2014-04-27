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

/**
 * Should be called prior to all use of a FeedParser.  We reserve the right to
 * change the underlying implementation of the FeedParser in the future for
 * performance reasons.
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: FeedParserFactory.java 155416 2005-02-26 13:00:10Z dirkv $
 */
public class FeedParserFactory {

    public static FeedParser newFeedParser() throws FeedParserException {

        //NOTE: design.  This should be a factory method which maps interface
        //name to classname implementation. 
        
        try { 
            
            return (FeedParser)new FeedParserImpl();
            
        } catch ( Throwable t ) { throw new FeedParserException( t ); }
    }
    
}

