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

import java.util.Iterator;
import java.util.List;

import org.jaxen.jdom.JDOMXPath;
import org.jdom.Element;

/**
 * Handles parsing RSS metadata including dates
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: TagFeedParser.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class TagFeedParser {

    /**
     * 
     */
    public static void parse( FeedParserListener listener,
                              FeedParserState state ) throws Exception {

        if ( listener instanceof TagFeedParserListener == false )
            return;

        TagFeedParserListener tagFeedParserListener
            = (TagFeedParserListener)listener;

        if ( doParseModTaxonomy( tagFeedParserListener, state ) )
            return;

        if ( doParseCategory( tagFeedParserListener, state ) )
            return;

        doDcSubject( tagFeedParserListener, state );

    }

    /**
     * 
     * Parse out dc:subject tags
     *
     * Its not very clear how this is supposed to work.
     * 
     * "Comment: Typically, a Subject will be expressed as keywords, key phrases
     * or classification codes that describe a topic of the resource.
     * Recommended best practice is to select a value from a controlled
     * vocabulary or formal classification scheme."
     * 
     * But this leaves it open to whether its a space separated list or that
     * this needs to be per element.  It seems that the real infringer is
     * Delicious but I can fix this by processing dc:subject last.
     *
     * 
     */
    public static void doDcSubject( TagFeedParserListener listener, 
                                    FeedParserState state ) throws Exception {

        JDOMXPath xpath = new JDOMXPath( "dc:subject" );
        xpath.addNamespace( NS.DC.getPrefix(), NS.DC.getURI() );
        
        List list = xpath.selectNodes( state.current );

        Iterator it = list.iterator();

        while ( it.hasNext() ) {

            Element element = (Element)it.next();

            String tag = element.getText();
            String tagspace = null;
            
            listener.onTag( state, tag, tagspace );
            listener.onTagEnd();

        }
        
    }

    /**
     * Parse out atom:category and RSS 2.0/0.91 category
     *
     * 
     */
    public static boolean doParseCategory( TagFeedParserListener listener, 
                                           FeedParserState state ) throws Exception {

        //XPath xpath = new XPath( "local-name() = 'category'" );

        JDOMXPath xpath = new JDOMXPath( "descendant::*[local-name() = 'category']" );

        //NOTE: this only works for elements without namespaces
        //XPath xpath = new XPath( "category" );

        List list = xpath.selectNodes( state.current );

        Iterator it = list.iterator();

        boolean found = false;
        
        while ( it.hasNext() ) {

            Element element = (Element)it.next();

            String tag = element.getText();
            String tagspace = null;
            
            listener.onTag( state, tag, tagspace );
            listener.onTagEnd();

            found = true;

        }

        return found;
        
    }

    public static boolean doParseModTaxonomy( TagFeedParserListener listener, 
                                              FeedParserState state ) throws Exception {

        // <taxo:topics>
        //      <rdf:Bag>
        //          <rdf:li resource="http://del.icio.us/tag/hacking" />
        //          <rdf:li resource="http://del.icio.us/tag/howto" />
        //          <rdf:li resource="http://del.icio.us/tag/programming" />
        //          <rdf:li resource="http://del.icio.us/tag/software" />
        //          <rdf:li resource="http://del.icio.us/tag/tech" />
        //          <rdf:li resource="http://del.icio.us/tag/technology" />
        //          <rdf:li resource="http://del.icio.us/tag/tools" />
        //          <rdf:li resource="http://del.icio.us/tag/tivo" />
        //      </rdf:Bag>
        //  </taxo:topics>
 
        JDOMXPath xpath = new JDOMXPath( "taxo:topics/rdf:Bag/rdf:li" );
        xpath.addNamespace( NS.RDF.getPrefix(), NS.RDF.getURI() );
        xpath.addNamespace( NS.TAXO.getPrefix(), NS.TAXO.getURI() );
        
        List list = xpath.selectNodes( state.current );

        Iterator it = list.iterator();

        boolean found = false;
        
        while ( it.hasNext() ) {

            Element element = (Element)it.next();

            String resource = element.getAttributeValue( "resource" );

            if ( resource != "" && resource != null ) {

                String tag = resource;
                String tagspace = resource;
                
                int begin = resource.lastIndexOf( "/" );                    

                if ( begin != -1 ) {
                    ++begin;
                    tag = resource.substring( begin, resource.length() );
                }

                listener.onTag( state, tag, tagspace );
                listener.onTagEnd();

                found = true;
            }
            
        } 

        return found;
        
    }

}
