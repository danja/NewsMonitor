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

package org.apache.commons.feedparser.sax;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.feedparser.FeedParserException;
import org.apache.commons.feedparser.FeedParserListener;
import org.apache.commons.feedparser.FeedParserState;
import org.apache.commons.feedparser.FeedVersion;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public class RSSFeedParser extends BaseDefaultHandler {

    public FeedParserListener listener = null;

    boolean onItem = false;

    HashMap properties = new HashMap();

    FeedParserState state = new FeedParserState();

    static HashSet RSS_NAMESPACES = new HashSet();

    static HashSet RDF_NAMESPACES = new HashSet();

    static HashSet MOD_CONTENT_NAMESPACES = new HashSet();

    static {

        RSS_NAMESPACES.add( "http://purl.org/rss/1.0/" );

        RDF_NAMESPACES.add( "http://www.w3.org/1999/02/22-rdf-syntax-ns#" );

        MOD_CONTENT_NAMESPACES.add( "http://purl.org/rss/1.0/modules/content/" );
        
    }
    
    /**
     * 
     * Create a new <code>RSSFeedParser</code> instance.
     *
     * 
     */
    public RSSFeedParser() {

        super( "FIXME" );
        
        this.parser = this;

        setNext( new ChannelTemplate( this ) );

    }

    public void startDocument() throws SAXException {

        try { 
            
            FeedVersion v = new FeedVersion();
            v.isRSS = true;
            listener.onFeedVersion( v );
            
            listener.init();
            
        } catch ( FeedParserException f ) {
            throw new SAXException( f );
        }

    }

    public void endDocument() throws SAXException {

        try { 
            
            listener.finished();
            
        } catch ( FeedParserException f ) {
            throw new SAXException( f );
        }

    }

    /**
     * Match rss:channel
     */
    class ChannelTemplate extends BaseDefaultHandler {

        public ChannelTemplate( RSSFeedParser parser ) {

            super( "channel", parser.RSS_NAMESPACES, parser );

            setNext( new URLTemplate( parser ) );

        }

        public void beginFeedElement() throws FeedParserException {

            parser.listener.onChannel( parser.state,
                                       getProperty( "title" ),
                                       getProperty( "link" ),
                                       getProperty( "description" ) );

        }
    
        public void endFeedElement() throws FeedParserException {
            parser.listener.onChannelEnd();
        }

    }

    /**
     * Match rss:url for images/etc
     */
    class URLTemplate extends BaseDefaultHandler {

        public URLTemplate( RSSFeedParser parser ) {

            super( "url", parser.RSS_NAMESPACES, parser );

            setNext( new ModContentTemplate( parser ) );
            //this.setNext( new RDFValueTemplate( parser ) );

        }

    }

    /**
     * Match the rdf:value for mod_content
     *
     * 
     */
    class ModContentTemplate extends BaseDefaultHandler {

        public ModContentTemplate( RSSFeedParser parser ) {

            super( "items", parser.MOD_CONTENT_NAMESPACES, parser );

            this.setNext( new RDFValueTemplate( parser ) );

        }

    }
    
    /**
     * Match the rdf:value for mod_content
     *
     * 
     */
    class RDFValueTemplate extends BaseDefaultHandler {

        public RDFValueTemplate( RSSFeedParser parser ) {

            super( "value", parser.RDF_NAMESPACES, parser );

            this.setIncludeContent( true );
            this.setNext( new RSSImageFeedParser( parser ) );

        }

        public void endFeedElement() throws FeedParserException {
            //System.out.println( " FIXME: (debug): " + getProperty( "value" ) );
        }

    }
    
}

class RSSImageFeedParser extends BaseDefaultHandler {

    public RSSImageFeedParser( RSSFeedParser parser ) {

        super( "image", parser.RSS_NAMESPACES, parser );

        setNext( new RSSItemFeedParser( parser ) );

    }

    public void beginFeedElement() throws FeedParserException {

        parser.listener.onImage( parser.state,
                                 getProperty( "title" ),
                                 getProperty( "link" ),
                                 getProperty( "url" ) );

    }
    
    public void endFeedElement() throws FeedParserException {
        parser.listener.onImageEnd();
    }

}

class RSSItemFeedParser extends BaseDefaultHandler {

    public RSSItemFeedParser( RSSFeedParser parser ) {

        super( "item", parser );
        this.namespaces = parser.RSS_NAMESPACES;

        setNext( new RSSTitleFeedParser( parser ) );

    }

    public void beginFeedElement() throws FeedParserException {

        parser.listener.onItem( parser.state,
                                getProperty( "title" ),
                                getProperty( "link" ),
                                getProperty( "description" ),
                                null );

    }

    public void endFeedElement() throws FeedParserException {
        parser.listener.onItemEnd();
    }
    
}

class RSSTitleFeedParser extends BaseDefaultHandler {
    
    public RSSTitleFeedParser( RSSFeedParser parser ) {

        super( "title", parser );

        setNext( new RSSLinkFeedParser( parser ) );

    }

}

class RSSLinkFeedParser extends BaseDefaultHandler {

    public RSSLinkFeedParser( RSSFeedParser parser ) {
        super( "link", parser );

        setNext( new RSSDescriptionFeedParser( parser ) );
    }

}

class RSSDescriptionFeedParser extends BaseDefaultHandler {

    public RSSDescriptionFeedParser( RSSFeedParser parser ) {
        super( "description", parser );
    }

}

/**
 * dc:subject support
 */
class RSSDcSubjectFeedParser extends BaseDefaultHandler {

    //MetaFeedParserListener metadataListener= null;
    
    public RSSDcSubjectFeedParser( RSSFeedParser parser ) {
        super( "subject", parser );
    }

    public void beginFeedElement() {

        //only if it's dc:subject
        //listener.onSubject( parser.state, parser.getProperty( "subject" ) );

    }

    public void endFeedElement() {

    }

}

class BaseDefaultHandler extends DefaultHandler {

    public static int STRING_BUFFER_CAPACITY = 100000;
    
    //BUG: this will break on nested code:

    //     <foo>
    //         <foo>
    //
    //         </foo>
    //
    //     </foo>

    // won't be smart enough to realize it's nested
    
    /**
     * The local name of the element
     */
    private String local = null;

    //FIXME: move to a FastStringBuffer that's not synchronized.
    private StringBuffer buff = null;

    private boolean onElement = false;

    private boolean includeContent = false;
    
    BaseDefaultHandler next = null;

    FeedParserListener listener = null;

    RSSFeedParser parser = null;

    static HashMap nsPrefixMapping = new HashMap();
    
    /**
     * Store a hashset of namespaces that the given URL supports.
     *
     */
    HashSet namespaces = null;

    public BaseDefaultHandler( String local ) {
        this.local = local;
    }

    public BaseDefaultHandler( String local, RSSFeedParser parser ) {

        this.local = local;
        this.parser = parser;
        
    }

    public BaseDefaultHandler( String local,
                               HashSet namespaces,
                               RSSFeedParser parser ) {

        this.local = local;
        this.namespaces = namespaces;
        this.parser = parser;
        
    }

    /**
     * If true we include the RAW XML content from the parser.
     *
     * 
     */
    public void setIncludeContent( boolean includeContent ) {
        this.includeContent = includeContent;
    }
    
    /**
     * Set the next template to process in this chain.
     *
     * 
     */
    public void setNext( BaseDefaultHandler next ) {
        this.next = next;
    }
    
    /**
     * Return the value of character data forfor the element.
     *
     * 
     */
    public String toString() {

        if ( buff == null )
            return null;
        
        if ( buff.length() == 0 )
            return null;

        return buff.toString();
    }

    /**
     * Return true if the namespace is valid and this class is handling the
     * given element name
     *
     * 
     */
    boolean isLocal( String namespace, String local ) {

        //wee if we need to test forfor namespaces
        if ( namespace != null && namespaces != null && ! namespaces.contains( namespace ) )
            return false;

        return this.local.equals( local );
    }

    /**
     * Get the value of a string property we found whilewhile parsing
     *
     * 
     */
    public String getProperty( String name ) {
        return (String)parser.properties.get( name );
    }

    public boolean getBoolean( String name ) {

        return "true".equals( getProperty( name ) );
        
    }

    /**
     * Method to call when we're finished processing this element but BEFORE
     * processing of the next element in the chain.
     *
     * 
     */
    public void beginFeedElement() throws FeedParserException {}

    /**
     * Method to call when we're finished processing this element but AFTER
     * processing of the next element in the chain.
     *
     * 
     */
    public void endFeedElement() throws FeedParserException {}

    private boolean includeContentPrefix( String namespace ) {

        if ( namespace != null ) {

            String prefix = (String)nsPrefixMapping.get( namespace );

            if ( prefix != null ) {

                buff.append( prefix );
                buff.append( ":" );
                return true;
            }

        }

        return false;
        
    }
    
    // **** SAX DefaultHandler **************************************************

    /**
     * Keep track of namespaces.
     *
     * 
     */
    public void startPrefixMapping( String prefix,
                                    String namespace ) throws SAXException {

        if ( prefix != null && ! "".equals( prefix ) ) {
            //System.out.println( namespace + " -> " + prefix );
        
            nsPrefixMapping.put( namespace, prefix );

        } 

    }

    //FIXME: it might be possible to call an item again without a member and the
    //value from the LAST item is used... this needs to be a fatal error and we
    //need to clear ...

    public void startElement( String namespace,
                              String local,
                              String qName,
                              Attributes attributes ) throws SAXException {

        if ( isLocal( namespace, local ) ) {

            //FIXME: is there a more efficient way to clear a buffer than this?

            //FIXME: also only do this ifif it's necessary and content has
            //actually been added.  This will save some performance.

            //buff = new StringBuffer( STRING_BUFFER_CAPACITY );

            //buff = new StringBuffer( 1000 );

            if ( buff == null ) {
                buff = new StringBuffer( 1000 );
            } else {
                buff.setLength( 0 );
            }

            onElement = true;
        }

        if ( next != null )
            next.startElement( namespace, local, qName, attributes );

        if ( includeContent && onElement ) {
            buff.append( "<" );

            boolean hasPrefix = includeContentPrefix( namespace );
            
            buff.append( local );

            if ( ! hasPrefix && namespace != null ) {
                buff.append( " xmlns=\"" );
                buff.append( namespace );
                buff.append( "\"" );
            }

            //now include attributes

            int length = attributes.getLength();

            for ( int i = 0; i < length; ++i ) {

                buff.append( " " );
                buff.append( attributes.getQName( i ) );
                buff.append( "=" );
                buff.append( "\"" );
                buff.append( attributes.getValue( i ) );
                buff.append( "\"" );

            }
            
            buff.append( ">" );
        }
        
    }

    public void characters( char[] ch,
                            int start,
                            int length ) throws SAXException {
 
        if ( onElement ) {
            buff.append( ch, start, length );
        }

        if ( next != null )
            next.characters( ch, start, length );

    }
    
    public void endElement( String namespace,
                            String local,
                            String qName ) throws SAXException {

        try { 

            if ( isLocal( namespace, local ) ) {

                onElement = false;
                parser.properties.put( local, toString() );

                beginFeedElement();
            
            }

            if ( next != null )
                next.endElement( namespace, local, qName );

            if ( isLocal( namespace, local ) )
                endFeedElement();

            if ( includeContent && onElement ) {
                buff.append( "</" );

                includeContentPrefix( namespace );

                buff.append( local );

                buff.append( ">" );
            }

        } catch ( FeedParserException fpe ) {

            throw new SAXException( fpe );

        }

    }

}

