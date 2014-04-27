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
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Handles parsing FOAF.
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: FOAFFeedParser.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class FOAFFeedParser {

    /**
     * Parse this feed.
     *
     * 
     */
    public static void parse( FeedParserListener listener,
                              org.jdom.Document doc ) throws Exception {

        try {
                
            FeedParserState state = new FeedParserState();

            FeedVersion v = new FeedVersion();
            v.isFOAF = true;
            listener.onFeedVersion( v );

            listener.init();

            FOAFFeedParserListener flistener = null;

            if ( listener instanceof FOAFFeedParserListener )
                flistener = (FOAFFeedParserListener)listener;

            //this should be the root directory.
            JDOMXPath xpath = new JDOMXPath( "/rdf:RDF/foaf:Person" );
            xpath.setNamespaceContext( NS.context );
            
            Element person = (Element)xpath.selectSingleNode( doc );

            String name = RSSFeedParser.getChildElementTextByName( state, "name" );

            String homepage = getAttributeValue( state,
                                                 "homepage", NS.FOAF,
                                                 "resource", NS.RDF );

            if ( flistener != null )
                flistener.onPerson( state, name );
            
            xpath = new JDOMXPath( "foaf:knows" );

            xpath.setNamespaceContext( NS.context );

            List list = xpath.selectNodes( person );
            Iterator i = list.iterator();

            while ( i.hasNext() ) {

                Element knows = (Element)i.next();

                Element currentPerson = knows.getChild( "Person", NS.FOAF );
                
                state.current = currentPerson;
                
                String title = RSSFeedParser.getChildElementTextByName( state, "name" );

                String seeAlso = getAttributeValue( state,
                                                    "seeAlso", NS.RDFS,
                                                    "resource", NS.RDF );

                homepage = getAttributeValue( state,
                                              "homepage", NS.FOAF,
                                              "resource", NS.RDF );

                System.out.println( "seeAlso: " + seeAlso );
                System.out.println( "homepage: " + homepage );

                if ( flistener != null )
                    flistener.onKnows( state );
                    
                listener.onItem( state,
                                 title,
                                 homepage,
                                 null,
                                 seeAlso );

                if ( flistener != null ) {

                    flistener.onHomepage( state,
                                          homepage );

                    flistener.onHomepageEnd();
                    
                    flistener.onSeeAlso( state,
                                         seeAlso );

                    flistener.onSeeAlsoEnd();
                    
                } 

                listener.onItemEnd();

                if ( flistener != null )
                    flistener.onKnowsEnd();

            }

            if ( flistener != null )
                flistener.onPersonEnd();

            listener.finished();

        } catch ( Throwable t ) { throw new FeedParserException( t ); }

    }

    public static String getAttributeValue( FeedParserState state,
                                            String name,
                                            Namespace name_ns,
                                            String attr,
                                            Namespace attr_ns ) {

        Element e = state.current.getChild( name, name_ns );

        if ( e == null )
            return null;

        Attribute a = e.getAttribute( attr, attr_ns );

        if ( a == null )
            return null;

        return a.getValue();

    }
    
    private static void doParseKnows( FeedParserListener listener,
                                      FeedParserState state ) throws Exception {

    }
    
}

