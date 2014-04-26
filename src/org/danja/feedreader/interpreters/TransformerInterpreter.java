/**
 * feedreader-prototype
 *
 * TransformerInterpreter.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.interpreters;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.danja.feedreader.feeds.FeedConstants;
import org.danja.feedreader.feeds.Feed;
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
    private static final String xslFilename = "xslt/feed-rss1.0.xsl";

    public TransformerInterpreter() {
        rdfInterpreter = new WriterInterpreter();

    }

    public void interpret(Feed feed) {
        InputStream inputStream = feed.getInputStream();
        String filename = "data/" + InterpreterFactory.getFilename(feed);
        
        System.out.println("\nFeed: "+feed.getUrl());
        System.out.println("type: "+ FeedConstants.formatName(feed.getFormatHint()));
        System.out.println("Writing from TransformerInterpreter...");
        
        writeXmlFile(inputStream, filename, xslFilename);
        try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
        try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }

}