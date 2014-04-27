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

import java.util.Locale;

import org.apache.commons.feedparser.tools.RFC3066LocaleParser;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * Basic parser support common between RSS, Atom, and FOAF feed impls.
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public class BaseParser {

    protected static void doLocale( FeedParserState state,
                                    FeedParserListener listener,
                                    Element element ) throws Exception {

        if ( element == null )
            return;

        if ( state.metaFeedParserlistener == null )
            return;

        String l = getLocaleString( element );
        
        if ( l != null ) {

            Locale locale = RFC3066LocaleParser.parse( l );

            if ( locale != null )
                state.metaFeedParserlistener.onLocale( state, locale );

        }

    }

    protected static void doLocaleEnd( FeedParserState state,
                                       FeedParserListener listener,
                                       Element element )
        throws Exception {

        if ( element == null )
            return;

        if ( state.metaFeedParserlistener == null )
            return;

        String l = getLocaleString( element );

        if ( l != null ) 
            state.metaFeedParserlistener.onLocaleEnd();

    }

    protected static String getLocaleString( Element element ) {

        //hm.. crap. how do we get the 'xml' namespace here?
        Attribute attr = element.getAttribute( "lang" );

        if ( attr != null )
            return attr.getValue();
        
        //when stil null see that we have dc:language

        Element lang = element.getChild( "language", NS.DC );

        if ( lang != null )
            return lang.getText();

        //fall over to just using "language" and if it isn't a local string we
        //won't parse it.  This is for RSS 0.91 and RSS 2.0 content.
        lang = element.getChild( "language" );

        if ( lang != null )
            return lang.getText();

        return null;

    }

    // **** shared code for all parsers *****************************************

    //FIXME: unify this with RSSFeedParser.getChildElementTextByName
    protected static String selectText( String query, Element element ) throws Exception {

        JDOMXPath xpath = new JDOMXPath( query );
        xpath.setNamespaceContext( NS.context );
        
        //perform onChannel method...  (title, link, description)
        Element e = (Element)xpath.selectSingleNode( element );

        if ( e == null )
            return null;

        String result = e.getText();

        //The normalize method of XML SHOULD take care of this but for some
        //reason it doesnt.
        if ( result != null )
            result = result.trim();

        return result;

    }

    /**
     * Regardless of namespace, get the child node text by name or null if it is not found.
     *
     * 
     */
    protected static String getChildElementTextByName( FeedParserState state,
                                                    String name ) throws Exception {

        //FIXME: this can be rewritten to use getChild()
        
        JDOMXPath xpath = new JDOMXPath( "descendant::*[local-name() = '" + name + "']" );
        Object resultNode = xpath.selectSingleNode( state.current );

        String resultText = null;

        if ( resultNode != null )
            resultText = ((Element)resultNode).getText();

        //The normalize method of XML SHOULD take care of this but for some
        //reason it doesnt.
        if ( resultText != null )
            resultText = resultText.trim();

        return resultText;
        
    }

}
