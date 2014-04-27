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

import org.jaxen.SimpleNamespaceContext;
import org.jdom.Namespace;

/**
 * A class to manage XML namespaces
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: NS.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class NS {

    public static final Namespace RDFS =
        Namespace.getNamespace( "rdfs", "http://www.w3.org/2000/01/rdf-schema#" );

    public static final Namespace FOAF =
        Namespace.getNamespace( "foaf", "http://xmlns.com/foaf/0.1/" );

    public static final Namespace KR =
        Namespace.getNamespace( "kr", "http://ksa.peerfear.org/ksa-rdf#" );

    public static final Namespace CONTENT =
        Namespace.getNamespace( "content", "http://purl.org/rss/1.0/modules/content/" );

    public static final Namespace QUOTE =
        Namespace.getNamespace( "quote", "http://purl.org/rss/1.0/modules/quote/" );

    public static final Namespace RDF =
        Namespace.getNamespace( "rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#" );

    public static final Namespace RSS =
        Namespace.getNamespace( "rss", "http://purl.org/rss/1.0/" );

    public static final Namespace AGG =
        Namespace.getNamespace( "agg", "http://purl.org/rss/modules/aggregation/" );

    public static final Namespace IM =
        Namespace.getNamespace( "im", "http://purl.org/rss/1.0/item-images/" );

    public static final Namespace DC =
        Namespace.getNamespace( "dc", "http://purl.org/dc/elements/1.1/" );

    public static final Namespace DCTERMS =
        Namespace.getNamespace( "dcterms", "http://purl.org/dc/terms/" );

    public static final Namespace SUBSCRIPTION =
        Namespace.getNamespace( "sub", "http://purl.org/rss/1.0/modules/subscription/" );

    public static final Namespace NC =
        Namespace.getNamespace( "NC", "http://home.netscape.com/NC-rdf#" );

    public static final Namespace XHTML =
        Namespace.getNamespace( "xhtml", "http://www.w3.org/1999/xhtml" );

    public static final Namespace ATOM =
        Namespace.getNamespace( "atom", "http://purl.org/atom/ns#" );

    public static final Namespace WFW =
        Namespace.getNamespace( "wfw", "http://wellformedweb.org/CommentAPI/" );
    
    /**
     * mod_taxonomy
     */
    public static final Namespace TAXO =
        Namespace.getNamespace( "taxo", "http://purl.org/rss/1.0/modules/taxonomy/" );

    public static SimpleNamespaceContext context = null;

    static {

        //create all namespace contexts and do it ONCE
        context = new SimpleNamespaceContext();
        context.addNamespace( ATOM.getPrefix(), ATOM.getURI() );
        context.addNamespace( DC.getPrefix(), DC.getURI() );
        context.addNamespace( FOAF.getPrefix(), FOAF.getURI() );
        context.addNamespace( RDF.getPrefix(), RDF.getURI() );
        context.addNamespace( RDFS.getPrefix(), RDFS.getURI() );
        context.addNamespace( TAXO.getPrefix(), TAXO.getURI() );

    }
    
}
