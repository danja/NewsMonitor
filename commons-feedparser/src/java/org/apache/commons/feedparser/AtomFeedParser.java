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

import org.apache.commons.feedparser.locate.EntityDecoder;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.output.XMLOutputter;

/**
 * http://www.intertwingly.net/wiki/pie/FrontPage
 *  
 * http://www.ietf.org/internet-drafts/draft-ietf-atompub-format-05.txt
 * 
 * http://www.ietf.org/internet-drafts/draft-ietf-atompub-format-04.txt
 * 
 * http://www.mnot.net/drafts/draft-nottingham-atom-format-02.html
 * 
 * http://www.ietf.org/html.charters/atompub-charter.html
 * 
 * http://www.ietf.org/internet-drafts/draft-ietf-atompub-format-01.txt
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: AtomFeedParser.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class AtomFeedParser extends BaseParser {

    /**
     * Parse this feed.
     *
     * 
     */
    public static void parse( FeedParserListener listener,
                              org.jdom.Document doc ) throws Exception {

        FeedParserState state = new FeedParserState( listener );

        FeedVersion v = new FeedVersion();
        v.isAtom = true;
        listener.onFeedVersion( v );

        listener.init();

        Element root = doc.getRootElement();

        doLocale( state, listener, root );
        
        doChannel( state, listener, doc );
        doEntry( state, listener, doc );

        doLocaleEnd( state, listener, root );

        listener.finished();

    }

    private static void doChannel( FeedParserState state,
                                   FeedParserListener listener,
                                   org.jdom.Document doc ) throws Exception {

        Element root = doc.getRootElement();

        //perform onChannel method...  (title, link, description)
        String title = selectText( "/atom:feed/atom:title", root );

        //xpath = new XPath( "/atom:feed/atom:link[atom:rel='alternate']" );
        
        //perform onChannel method...  (title, link, description)
        String link = selectSingleAttribute( "/atom:feed/atom:link[@rel='alternate'][@type='text/html']/@href", root );

        //String description = selectText( "/atom:feed/atom:summary[@rel='text/plain']", doc );

        String tagline = selectText( "/atom:feed/atom:tagline", root );
        
        //state.current = title;
        listener.onChannel( state, title, link, tagline );

        listener.onChannelEnd();

    }

    private static void doEntry( FeedParserState state,
                                 FeedParserListener listener,
                                 org.jdom.Document doc ) throws Exception {

        JDOMXPath xpath = new JDOMXPath( "/atom:feed/atom:entry" );
        xpath.setNamespaceContext( NS.context );

        List items = xpath.selectNodes( doc );

        Iterator i = items.iterator();
            
        //update items.
        while ( i.hasNext() ) {

            Element child = (Element)i.next();

            doLocale( state, listener, child );
            
            String title = selectText( "atom:title", child );

            // The "atom:link" element is a Link construct that conveys a URI
            // associated with the entry. The nature of the relationship as well
            // as the link itself is determined by the element's content.

            // atom:entry elements MUST contain at least one atom:link element
            // with a rel attribute value of "alternate".

            // atom:entry elements MUST NOT contain more than one atom:link
            // element with a rel attribute value of "alternate" that has the
            // same type attribute value.

            // atom:entry elements MAY contain additional atom:link elements
            // beyond those described above.
            
            String link = selectSingleAttribute( "atom:link[@rel='alternate'][@type='text/html']/@href",
                                                 child );

            // The "atom:summary" element is a Content construct that conveys a
            // short summary, abstract or excerpt of the entry. atom:entry
            // elements MAY contain an atom:created element, but MUST NOT
            // contain more than one.

            //FIXME: what if there is no type attribute specified?  Whats the default?

            // Content constructs MAY have a "type" attribute, whose value
            // indicates the media type of the content.  When present, this
            // attribute's value MUST be a media type [RFC2045].  If this
            // attribute is not present, processors MUST behave as if it were
            // present with a value of "text/ plain".

            String description = null;

            Element summary = child.getChild( "summary", NS.ATOM );

            if ( summary != null ) {

                String type = summary.getAttributeValue( "type", NS.ATOM );
                
                if ( type == null || "text/plain".equals( type ) )
                    description = summary.getText();
                
            }

            state.current = child;
            
            listener.onItem( state, title, link, description, link );
            
            doLink( state, listener, child );
            
            doMeta( state, listener, child );

            doContent( state, listener, child );

            MetaFeedParser.parse( listener, state );
            TagFeedParser.parse( listener, state );
            
            listener.onItemEnd();
            doLocale( state, listener, child );

        }

    }

    private static void doLink( FeedParserState state,
                                FeedParserListener listener,
                                Element current ) throws Exception {

        if ( listener instanceof LinkFeedParserListener == false )
            return;

        LinkFeedParserListener lfpl = (LinkFeedParserListener)listener;
        
        JDOMXPath xpath = new JDOMXPath( "atom:link" );
        xpath.setNamespaceContext( NS.context );

        List items = xpath.selectNodes( current );

        Iterator it = items.iterator();
            
        //update items.
        while ( it.hasNext() ) {

            Element link = (Element)it.next();
            
            String href = link.getAttributeValue( "href" );
            String rel = link.getAttributeValue( "rel" );
            String type = link.getAttributeValue( "type" );

            String title = null;
            long length = -1;

            lfpl.onLink( state, rel, type, href, title, length );
            
        }
        
    }
    
    private static void doContent( FeedParserState state,
                                   FeedParserListener listener,
                                   Element current ) throws Exception {

        if ( ! (listener instanceof ContentFeedParserListener) )
            return;
        
        ContentFeedParserListener clistener = (ContentFeedParserListener)listener;

        JDOMXPath xpath = new JDOMXPath( "atom:content" );
        xpath.setNamespaceContext( NS.context );

        List items = xpath.selectNodes( current );

        Iterator i = items.iterator();
            
        //update items.
        while ( i.hasNext() ) {

            Element content = (Element)i.next();

            doLocale( state, listener, content );

            String type = content.getAttributeValue( "type", "text/plain" );
            String mode = content.getAttributeValue( "mode" );

            String format = null;
            String encoding = null;

            String value = null;

            //
            if ( "xml".equals( mode ) ) {
                value = content.getText();
            } else if ( "escaped".equals( mode ) ) {

                //need to decode the content here &lt; -> < etc.
                value = getXMLOfContent( content.getContent() );
                value = EntityDecoder.decode( value );
            } else {
                mode = "xml";
                value = getXMLOfContent( content.getContent() );
            }

            boolean isSummary = false;
            
            clistener.onContent( state, type, format, encoding, mode, value, isSummary );

            doLocaleEnd( state, listener, content );
            
        }

        xpath = new JDOMXPath( "atom:summary[@type='application/xhtml+xml']" );
        xpath.setNamespaceContext( NS.context );
        Element e = (Element)xpath.selectSingleNode( current );

        if ( e != null ) {

            String type = "text/html";
            String format = "application/xhtml+xml";
            String encoding = null;
            String mode = "xml";

            //FIXME: get xml:base to expand the URIs.
            
            String value = getXMLOfContent( e );
            boolean isSummary = true;
            
            clistener.onContent( state, type, format, encoding, mode, value, isSummary );

        }
        
    }

    private static String getXMLOfContent( Element element ) {
        return getXMLOfContent( element.getContent() );
    }
    
    /**
     * Get the content of the given element.
     *
     * 
     */
    private static String getXMLOfContent( List content ) {

        //NOTE: Fri Mar 04 2005 03:59 PM (burton1@rojo.com): in my profiling I
        //found that this is a BIG memory allocater.  FIXME: We SHOULD be able
        //to do the same thing we do for xhtml:body RIGHT?
        
        StringBuffer buff = new StringBuffer( 10000 ); 

        // NOTE: Changed this constructor to use the default Format. Since the
        // constructor used no longer exists in jdom 1.0.
        XMLOutputter outputter = new XMLOutputter();

        Iterator it = content.iterator();
        
        while ( it.hasNext() ) {

            Object next = it.next();
            
            if ( next instanceof String ) {
                buff.append( (String)next );
            } else if ( next instanceof Element ) {
                buff.append( outputter.outputString( (Element)next ) );
            } else if ( next instanceof CDATA ) {
                buff.append( ((CDATA)next).getText() );
            } else if ( next instanceof Comment ) {
                buff.append( outputter.outputString( (Comment)next ) );
            } else if ( next instanceof Text ) {
                buff.append( outputter.outputString( (Text)next ) );
            } 

        } 

        return buff.toString();
        
    }

    private static void doMeta( FeedParserState state,
                                FeedParserListener listener,
                                Element element ) throws Exception {

        //FIXME: move this code to MetaFeedParser...
        
        if ( ! (listener instanceof MetaFeedParserListener) ) 
            return;

        MetaFeedParserListener mlistener = (MetaFeedParserListener)listener;

        //handle issued, created, and then dublin core..
        String subject = selectText( "dc:subject", element);

        if ( subject != null ) {
            mlistener.onSubject( state, subject );
            mlistener.onSubjectEnd();
        } 

    }

    private static Element selectSingleElement( String query, org.jdom.Document doc ) throws Exception {

        JDOMXPath xpath = new JDOMXPath( query );
        xpath.setNamespaceContext( NS.context );
        
        //perform onChannel method...  (title, link, description)
        return (Element)xpath.selectSingleNode( doc );

    }

    private static String selectSingleAttribute( String query, Element element ) throws Exception {

        JDOMXPath xpath = new JDOMXPath( query );
        xpath.setNamespaceContext( NS.context );
        
        //perform onChannel method...  (title, link, description)
        Attribute a = (Attribute)xpath.selectSingleNode( element );
        if ( a == null )
            return null;
        
        return a.getValue();

    }

}

