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

/**
 * Handles parsing RSS .
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public class RSSFeedParser extends BaseParser {

    /**
     * Parse the given document as an OPML document.
     *
     * 
     */
    public static void parse( FeedParserListener listener,
                              org.jdom.Document doc ) throws Exception {

        FeedParserState state = new FeedParserState( listener );

        FeedVersion v = new FeedVersion();

        v.isRSS = true;
        v.version = doc.getRootElement().getAttributeValue( "version" );
        
        listener.onFeedVersion( v );

        listener.init();

        //*** now process the channel. ***
        JDOMXPath xpath = new JDOMXPath( "/descendant::*[local-name() = 'channel']" );
        Element channel = (Element)xpath.selectSingleNode( doc );
        state.current = channel;

        doLocale( state, listener, channel );
        doChannel( listener, state );
        doLocaleEnd( state, listener, channel );

        //*** now process the image. ***
        xpath = new JDOMXPath( "/descendant::*[local-name() = 'image']" );
        List images = xpath.selectNodes( doc );
        Iterator i = images.iterator();
        //update items.
        while ( i.hasNext() ) {

            Element child = (Element)i.next();
            state.current = child;
            doParseImage( listener, state );

        }

        //*** now process all items. ***
        xpath = new JDOMXPath( "/descendant::*[local-name() = 'item']" );

        List items = xpath.selectNodes( doc );

        i = items.iterator();
            
        //update items.
        while ( i.hasNext() ) {

            Element item = (Element)i.next();

            state.current = item;
                
            doLocale( state, listener, item );
            doItem( listener, state );
            doLocaleEnd( state, listener, item );

        }

        listener.finished();

    }
    
    /**
     * Parse the given channel 
     *
     * 
     */
    private static void doChannel( FeedParserListener listener,
                                   FeedParserState state ) throws Exception {

        String link = getChildElementTextByName( state, "link" );

        if ( link != null )
            link = link.trim();
        
        listener.onChannel( state, 
                            getChildElementTextByName( state, "title" ),
                            link,
                            getChildElementTextByName( state, "description" ) );

        listener.onChannelEnd();

    }

    /**
     * Parse the given channel 
     *
     * 
     */
    private static void doParseImage( FeedParserListener listener,
                                      FeedParserState state ) throws Exception {

        String title = getChildElementTextByName( state, "title" );
        String link = getChildElementTextByName( state, "link" );
        String url = getChildElementTextByName( state, "url" );

        if ( url != null ) {
            listener.onImage( state, title, link, url );
            listener.onImageEnd();

        } 

    }

    /**
     * 
     *
     * 
     */
    private static void doItem( FeedParserListener listener,
                                FeedParserState state ) throws Exception {

        String resource = null;

        //FIXME: migrate this to XPath

        JDOMXPath xpath = new JDOMXPath( "@rdf:resource|guid|descendant::*[local-name() = 'link']" );
        xpath.addNamespace( NS.RDF.getPrefix(), NS.RDF.getURI() );
        Object node = xpath.selectSingleNode( state.current );

        //FIXME: if this is a GUID and isPermalink=false don't use it as the
        //permalink.

        if ( node instanceof Element ) {

            Element element = (Element)node;
            resource = element.getText();

            if ( "guid".equals( element.getName() ) ) {

                boolean isPermaLink =
                    "true".equals( element.getAttributeValue( "isPermaLink" ) );

                if ( isPermaLink == false ) {
                    //resort to the 'link'

                    Element link = state.current.getChild( "link" );

                    if ( link != null ) {
                        resource = link.getText();
                    }
                    
                }
                
            }

        } else if ( node instanceof Attribute ) {
            resource = ((Attribute)node).getValue();
        }

        if ( resource == null )
            return;

        //title, link, description

        listener.onItem( state,
                         getChildElementTextByName( state, "title" ),
                         getChildElementTextByName( state, "link" ),
                         getChildElementTextByName( state, "description" ),
                         resource );

        //see if we have content encoded and if we need to report these events.

        if ( listener instanceof ModContentFeedParserListener ) {

            ModContentFeedParserListener mcpl = (ModContentFeedParserListener)listener;

            Element encoded = state.current.getChild( "encoded", NS.CONTENT );

            if ( encoded != null ) {

                //FIXME: move to the onContent API defined within the
                //AtomFeedParser and deprecated this body handling.

                mcpl.onContentEncoded( new FeedParserState( encoded ),
                                       encoded.getText() );

                mcpl.onContentEncodedEnd();

            } else {

                Element items = state.current.getChild( "items", NS.CONTENT );

                if ( items != null ) {

                    //FIXME: with malformed XML this could throw an NPE. Luckly
                    //this format is rare now.
                    Element value =
                        items.getChild( "Bag", NS.RDF )
                            .getChild( "li", NS.RDF )
                                .getChild( "item", NS.CONTENT )
                                    .getChild( "value", NS.RDF );

                    //FIXME: move to the onContent API defined within the
                    //AtomFeedParser and deprecated this body handling.

                    mcpl.onContentItem( new FeedParserState( value ),
                                        null,
                                        null,
                                        value );

                    mcpl.onContentItemEnd();

                }
                    
            }

        } 

        //process xhtml:body

        if ( listener instanceof XHTMLFeedParserListener ) {

            XHTMLFeedParserListener xfp = (XHTMLFeedParserListener)listener;

            Element body = state.current.getChild( "body", NS.XHTML );

            //FIXME: move to the onContent API defined within the AtomFeedParser
            //and deprecated this body handling.
            
            if ( body != null ) {
                xfp.onXHTMLBody( new FeedParserState( body ),
                                 body );
                xfp.onXHTMLBodyEnd();
            } 

        }

        MetaFeedParser.parse( listener, state );
        TagFeedParser.parse( listener, state );

        doEnclosures( listener, state );
        
        listener.onItemEnd();
        
    }

    private static void doEnclosures( FeedParserListener listener,
                                      FeedParserState state ) throws Exception {

        if ( listener instanceof LinkFeedParserListener == false )
            return;

        Element element = state.current.getChild( "enclosure" );

        if ( element == null )
            return;

        LinkFeedParserListener linkFeedParserListener = (LinkFeedParserListener)listener;

        String rel = null;
        String type = element.getAttributeValue( "type" );
        String href = element.getAttributeValue( "url" );
        String title = null;
        long length = 0;
        if (element.getAttributeValue("length") != null)
        	length = Integer.parseInt( element.getAttributeValue( "length" ) );

        linkFeedParserListener.onLink( state,
                                       rel,
                                       type,
                                       href,
                                       title,
                                       length );
        
    }
    
}
