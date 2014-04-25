/**
 * feedreader-prototype
 *
 * TransformerInterpreter.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.social;

import java.io.File;
import java.io.InputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.FeedFetcher;
import org.danja.feedreader.io.FileEntrySerializer;
import org.danja.feedreader.io.Interpreter;

/**
 * Transforms feed XML (RSS 2.0/Atom) into RDF/XML using XSLT
 *  
 */
public class TransformerInterpreter implements Interpreter {

    private WriterInterpreter rdfInterpreter;

    private Transformer transformer;

    // TODO move to config
    private static final String xslFilename = "templates/feed-rss1.0.xsl";

    public TransformerInterpreter() {
        rdfInterpreter = new WriterInterpreter();

    }

    public void interpret(FeedFetcher feed) {
        InputStream inputStream = feed.getInputStream();
        String filename = "data/" + RDFInterpreterFactory.getFilename(feed);
        
        System.out.println("\nFeed: "+feed.getURIString());
        System.out.println("type: "+ FeedConstants.formatName(feed.getFormatHint()));
        System.out.println("Writing from TransformerInterpreter...");
        
        writeXmlFile(inputStream, filename, xslFilename);
    }

    public static void writeXmlFile(InputStream inputStream, String outputFilename,
            String xslFilename) {
        try {
            Source source = new StreamSource(inputStream);

            Result result = new StreamResult(new File(outputFilename));

            Transformer transformer = FileEntrySerializer.getTransformer(xslFilename);

            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}