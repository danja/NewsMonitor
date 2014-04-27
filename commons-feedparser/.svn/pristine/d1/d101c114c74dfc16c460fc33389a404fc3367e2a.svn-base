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
 * Generic content module designed to be compatible with xhtml:body, Atom
 * content, as well as RSS 1.0 mod_content module.
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public interface ContentFeedParserListener {

    /**
     *
     * Called when new content is found.
     * 
     * @param type (Atom) Content constructs MAY have a "type" attribute, whose
     * value indicates the media type of the content. When present, this attribute's
     * value MUST be a registered media type [RFC2045]. If not present, its value
     * MUST be considered to be "text/plain".  3.1.2 "mode" Attribute
     * 
     * @param mode (Atom) Content constructs MAY have a "mode" attribute, whose
     * value indicates the method used to encode the content. When present, this
     * attribute's value MUST be listed below. If not present, its value MUST be
     * considered to be "xml".
     * 
     * "xml":
     * 
     * A mode attribute with the value "xml" indicates that the element's content is
     * inline xml (for example, namespace-qualified XHTML).
     * 
     * "escaped":
     * 
     * A mode attribute with the value "escaped" indicates that the element's
     * content is an escaped string. Processors MUST unescape the element's content
     * before considering it as content of the indicated media type.
     * 
     * "base64":
     * 
     * A mode attribute with the value "base64" indicates that the element's content is
     * base64-encoded [RFC2045]. Processors MUST decode the element's content before
     * considering it as content of the the indicated media type.
     * 
     * @param format (RSS 1.0 mod_content) Required. An empty element with an
     * rdf:resource attribute that points to a URI representing the format of the
     * content:item. Suggested best practice is to use the list of RDDL natures.
     *
     * @param encoding (RSS 1.0 mod_content) Optional. An empty element with an
     * rdf:resource attribute that points to a URI representing the encoding of the
     * content:item. An encoding is a reversable method of including content within
     * the RSS file.
     *
     * @param value String value of the found content.  if this is Base64
     * encoded content we do NOT decode the value but return it as a string.
     * This is done because the content might be binary and returning as a
     * string would be invalid.
     * 
     * @param isSummary True if this is just a summary of the content and not
     * the full content.  This is only known for Atom feeds.
     * 
     * 
     */
    public void onContent( FeedParserState state,
                           String type,
                           String format,
                           String encoding,
                           String mode,
                           String value,
                           boolean isSummary ) throws FeedParserException;

    public void onContentEnd() throws FeedParserException;

}

