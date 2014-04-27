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

package org.apache.commons.feedparser.locate;

import org.apache.commons.feedparser.FeedParserException;
import org.apache.commons.feedparser.locate.blogservice.BlogService;
import org.apache.commons.feedparser.locate.blogservice.Unknown;

/**
 * Determines what blog provider a given URI is using,
 * such as whether it is hosted on Blogspot, Radio Userland, etc.
 *
 * @author <a href="mailto:bkn3@columbia.edu">Brad Neuberg/a>
 */
public class BlogServiceDiscovery {
    
    public static BlogService discover( String resource ) 
                                            throws FeedParserException {
        return discover(resource, null);
    }

    public static BlogService discover( String resource, 
                                        String content ) 
                                            throws FeedParserException {
        // Some services, such as AOL LiveJournal, are case sensitive
        // on their resource names; can't do a toLowerCase.
        // Brad Neuberg, bkn3@columbia.edu
        //resource = resource.toLowerCase();
        BlogService[] blogServices = BlogService.getBlogServices();

        for (int i = 0; i < blogServices.length; i++) {
            if (blogServices[i].isThisService(resource, content)) {
                return blogServices[i];
            }
        }
        
        return new Unknown();
    }
}
