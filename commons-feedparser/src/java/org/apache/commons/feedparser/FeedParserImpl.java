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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.feedparser.tools.XMLCleanser;
import org.apache.commons.feedparser.tools.XMLEncodingParser;
import org.apache.log4j.Logger;
import org.jdom.input.SAXBuilder;

/**
 * This FeedParser implementation is based on JDOM and Jaxen and is based around
 * XPath and JDOM iteration.  While the implementation is straight forward it
 * has not been optimized for performance.  A SAX based parser would certainly
 * be less memory intensive but with the downside of being harder to develop.
 *
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: FeedParserImpl.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class FeedParserImpl implements FeedParser {

    private static Logger log = Logger.getLogger(FeedParserImpl.class);

    /**
     * Parse this feed.
     *
     * @param resource The URL of the feed being parsed.  This is optional and
     *                 may be null but is used when an exception is thrown to aid debugging.
     */
    public void parse(FeedParserListener listener,
                      InputStream is,
                      String resource) throws FeedParserException {

        try {

            // Need to massage our XML support for UTF-8 to prevent the dreaded
            // "Invalid byte 1 of 1-byte UTF-8 sequence" content bug in some
            // default feeds.  This was tested a great deal under NewsMonster
            // and I'm happy with the results.  Within FeedParser 2.0 we will be
            // using SAX2 so this won't be as big of a problem.  In FeedParser
            // 2.0 (or as soon as we use SAX) this code should be totally
            // removed to use the original stream.

            is = getCorrectInputStream( is );

            //OK.  Now we have the right InputStream so we should build our DOM
            //and exec.
            SAXBuilder builder = new SAXBuilder();

            //NOTE: in b10 of JDOM this won't accept an InputStream and requires
            //a org.w3c.dom.Document so we'll have to build one here.  Will this
            //slow things down any?

            org.jdom.Document doc = builder.build( is );

            parse(listener, doc);

        } catch (FeedParserException fpe) {
            //if an explicit FeedParserException is thrown just rethrow it..
            throw fpe;
        } catch (Throwable t) {

            //FIXME: when this is a JDOM or XML parser Exception we should
            //detect when we're working with an XHTML or HTML file and then
            //parse it with an XFN/XOXO event listener.

            throw new FeedParserException(t);
        }

    }

    /**
     * Perform the Xerces UTF8 correction and FeedFilter.
     */
    private InputStream getCorrectInputStream(InputStream is)
            throws Exception {

        byte[] bytes = toByteArray(is);

        //FIXME: if we return the WRONG content type here we will break.
        //getBytes()... UTF-16 and UTF-32 especially.  We should also perform
        //HTTP Content-Type parsing here to preserve the content type.  This can
        //be fixed by integrating our networking API from NewsMonster.

        String encoding = XMLEncodingParser.parse(bytes);

        if (encoding == null)
            encoding = "UTF-8";

        if ( encoding.startsWith( "UTF" ) ) {

            String result = XMLCleanser.cleanse( bytes, encoding );
            bytes = FeedFilter.parse( result, encoding );

        } else {

            bytes = FeedFilter.parse(bytes, encoding);

        }

        //remove prefix whitespace, intern HTML entities, etc.

        //build an input stream from the our bytes for parsing...
        is = new ByteArrayInputStream( bytes );

        return is;

    }

    /**
     * @deprecated Use #parse( FeedParserException, InputStream, String )
     */
    public void parse(FeedParserListener listener,
                      InputStream is) throws FeedParserException {

        parse(listener, is, null);

    }

    /**
     * Parse this feed.
     */
    public void parse(FeedParserListener listener,
                      org.jdom.Document doc) throws FeedParserException {

        try {

            String root = doc.getRootElement().getName();

            //Handle OPML
            if ("opml".equals(root)) {
                OPMLFeedParser.parse(listener, doc);
                return;
            }

            //Handle changes.xml
            if ("weblogUpdates".equals(root)) {
                ChangesFeedParser.parse(listener, doc);
                return;
            }

            //Handle ATOM
            if ( "feed".equals( root ) ) {
                AtomFeedParser.parse(listener, doc);
                return;
            }

            //Handle FOAF
            if (doc.getRootElement().getChildren("Person", NS.FOAF).size() > 0) {
                FOAFFeedParser.parse(listener, doc);
                return;
            }

            //FIXME: if this is XHTML we need to handle this with either an XFN
            //or an XOXO directory parser.  There might be more metadata we need
            //to parse here.  (also I wonder if this could be a chance to do
            //autodiscovery).

            //fall back on RDF and RSS parsing.

            //FIXME: if this is an UNKNOWN format We need to throw an
            //UnsupportedFeedxception (which extends FeedParserException)
            //
            // In this situation the ROOT elements should be: rss or RDF

            RSSFeedParser.parse(listener, doc);

        } catch (FeedParserException fpe) {
            //if an explicit FeedParserException is thrown just rethrow it..
            throw fpe;
        } catch (Throwable t) {
            throw new FeedParserException(t);
        }

    }

    /**
     * Convert an InputStream to a byte array.
     */
    public byte[] toByteArray(InputStream is) throws IOException {

        //WARNING:
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        //now process the Reader...
        byte data[] = new byte[200];

        int readCount = 0;

        while ((readCount = is.read(data)) > 0) {

            bos.write(data, 0, readCount);
        }

        is.close();
        bos.close();

        return bos.toByteArray();

    }

}

