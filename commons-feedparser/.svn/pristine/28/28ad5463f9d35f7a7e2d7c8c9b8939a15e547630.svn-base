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

import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id$
 */
public class ResourceExpander {

    private static Logger log = Logger.getLogger( ResourceExpander.class );

    /** A regexp to determine if a URL has a scheme, such as "http://foo.com".
     */
    protected static Pattern schemePattern = Pattern.compile("^\\w*://.*");
    
    /**
     * Expand a link relavant to the current site.  This takes care of links
     * such as
     *
     * /foo.html -> http://site.com/base/foo.html
     *
     * foo.html -> http://site.com/base/foo.html
     *
     * Links should *always* be expanded before they are used.
     *
     * This is because if we use the URL http://site.com/base then we don't know
     * if it's a directory or a file.  http://site.com/base/ would be a directory.
     * 
     * Note that all resource URLs will have correct trailing slashes.  If the URL
     * does not end with / then it is a file URL and not a directory.
     * 
     * @param resource The absolute base URL that will be used to expand the
     * link, such as "http://www.codinginparadise.org".
     * @param link The link to possibly expand, such as "/index.rdf" or
     * "http://www.somehost.com/somepage.html".
     *
     * 
     */
    public static String expand( String resource, String link ) {

        if ( link == null )
            return null;

        //make sure we can use this.
        if ( !isValidScheme( link ) )
            return link;

        //nothing if ALREADY relativized
        if ( isExpanded( link ) )
            return link;

        //    From: http://www.w3.org/Addressing/rfc1808.txt
        //
        //    If the parse string begins with a double-slash "//", then the
        //    substring of characters after the double-slash and up to, but not
        //    including, the next slash "/" character is the network
        //    location/login (<net_loc>) of the URL.  If no trailing slash "/"
        //    is present, the entire remaining parse string is assigned to
        //    <net_loc>.  The double- slash and <net_loc> are removed from the
        //    parse string before
        //FIXME: What happens if resource is a "file://" scheme?
        if ( link.startsWith( "//" ) ) {

            return "http:" + link;

        }

        //keep going
        if ( link.startsWith( "/" ) ) {

            link = getSite( resource ) + link;

            return link;

        } else if ( link.startsWith( "#" ) ) {

            link = resource + link;

            return link;

        } else if ( link.startsWith( ".." ) ) {

            //ok.  We need to get rid of these .. directories.

            String base = getBase( resource ) + "/";

            while ( link.startsWith( ".." ) ) {

                //get rid of the first previous dir in the link
                int begin = 2;
                if ( link.length() > 2 && link.charAt( 2 ) == '/' )
                    begin = 3;

                link = link.substring( begin, link.length() );

                //get rid of the last directory in the resource

                int end = base.length();

                if ( base.endsWith( "/" ) )
                     --end;

                base = base.substring( 0, base.lastIndexOf( "/", end - 1 ) );

            }

            link = base + "/" + link;

            return link;

        }

        // If the resource ends with a common file ending, then chop
        // off the file ending before adding the link
        // Is this rfc1808 compliant? Brad Neuberg, bkn3@columbia.edu
        resource = getBase(resource);
        if ( link.startsWith( "http://" ) == false ) {

            link = resource + "/" + link;
            log.debug("link="+link);

        }

        return link;

    }

    /**
     * Return true if the given link is ALREADY relativized..
     *
     * 
     */
    public static boolean isExpanded( String resource ) {
        return (resource.startsWith( "http://" ) ||
                resource.startsWith( "file://" ));
    }
    
    /**
     * Return true if this is an valid scheme and should be expanded.
     *
     * 
     */
    public static boolean isValidScheme( String resource ) {
        if (hasScheme(resource) == false)
            return true;
        
        //only on file: and http:

        if ( resource.startsWith( "http:" ) )
            return true;

        if ( resource.startsWith( "file:" ) )
            return true;

        return false;
        
    }
    
    /**
     * Determines if the given resource has a scheme. (i.e. does it start with
     * "http://foo.com" or does it just have "foo.com").
     */
    public static boolean hasScheme( String resource ) {
        return schemePattern.matcher( resource ).matches();
        
    }

    /**
     * Get the site for this resource.  For example:
     *
     * http://www.foo.com/directory/index.html
     *
     * we will return
     *
     * http://www.foo.com
     *
     * for file: URLs we return file://
     *
     * 
     */
    public static String getSite( String resource ) {

        if ( resource.startsWith( "file:" ) ) {
            return "file://";
        } 

        //start at 8 which is the width of http://
        int end = resource.indexOf( "/", 8 );

        if ( end == -1 ) {

            end = resource.length();

        } 

        return resource.substring( 0, end );

    }

    /**
     * Given a URL get the domain name.  
     *
     * 
     */
    public static String getDomain( String resource ) {

        String site = getSite( resource );

        int firstIndex = -1;
        int indexCount = 0;

        int index = site.length();

        while ( (index = site.lastIndexOf( ".", index-1 )) != -1 ) {

            ++indexCount;

            if ( indexCount == 2 )
                break;

        }

        int begin = 7; // http:// length
        if ( indexCount >= 2 )
            begin = index + 1;

        return site.substring( begin, site.length() );
        
    }
    
    /**
     * Get the base of this URL.  For example if we are given:
     *
     * http://www.foo.com/directory/index.html
     *
     * we will return
     *
     * http://www.foo.com/directory
     *
     *
     * 
     */
    public static String getBase( String resource ) {

        //FIXME: Brad says this method is totally broken.
        if ( resource == null )
            return null;
        
        int begin = "http://".length() + 1;
        
        int end = resource.lastIndexOf( "/" );
        
        if ( end == -1 || end <= begin ) {
            //probaby a URL like http://www.cnn.com
            
            end = resource.length();
            
        } 
        return resource.substring( 0, end );
        
    } 

    public static void main( String[] args ) throws Exception {

        System.out.println( expand( "http://peerfear.org/foo/bar/", "../../blog" ) );

        System.out.println( expand( "http://peerfear.org/foo/bar/", "../../index.html" ) );

        System.out.println( expand( "http://peerfear.org/blog/", ".." ) );

        System.out.println( expand( "http://peerfear.org", "/blog" ) );
        System.out.println( expand( "http://peerfear.org", "http://peerfear.org" ) );

        System.out.println( expand( "http://peerfear.org", "blog" ) );
        System.out.println( expand( "http://peerfear.org/blog", "foo/bar" ) );

        System.out.println( expand( "file://projects/newsmonster/", "blog" ) );

        System.out.println( expand( "file:/projects/ksa/src/java/ksa/test/TestFeedTask_WithRelativePath.rss"
                                      , "/blog" ) );        
    }

}

