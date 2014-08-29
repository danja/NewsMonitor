/**
 * NewsMonitor
 *
 * SparqlResultsParser.java
 *
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.sparql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.sparql.SparqlResults.Binding;
import it.danja.newsmonitor.sparql.SparqlResults.Result;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * SAX-based SPARQL XML Result Set format parser
 *
 * see @link http://www.w3.org/TR/rdf-sparql-XMLres/
 */
public class SparqlResultsParser implements ContentHandler {

    private static Logger log = LoggerFactory
            .getLogger(SparqlResultsParser.class);

    private static final String SPARQL_NS = "http://www.w3.org/2005/sparql-resultsObject#";
    private String sparql = "not set"; // for debugging

    public void setSparql(String sparqlString) { // for debugging
        this.sparql = sparqlString;
    }

    public String getSparql() {  // for debugging
        return sparql;
    }

    private enum State {

        variable, sparql, head, results, result, bindings, binding, uri, bnode, literal;
    }

    private SparqlResults resultsObject = new SparqlResults();
    private State state;
    private StringBuffer textBuffer = new StringBuffer();
    // private boolean hasText = false;

    private String currentName = null;
    // private Set results = new HashSet<Result>();
    private Result currentResult = null;
    private Binding currentBinding = null;

    public SparqlResultsParser() {
    }

    public static void main(String[] args) {
        SparqlResultsParser parser = new SparqlResultsParser();
        File file = new File("input/sparql-xml-results-sample.xml");
        SparqlResults sparqlResults = parser.parse(file);
        // log.info("Size in main = "+sparqlResults.getResults().size());
        log.info(sparqlResults.getResults().toString());
    }

	// Custom type conversion methods - see
    // http://www.ibm.com/developerworks/library/x-tipsaxis/
    public synchronized SparqlResults parse(String string) {
        Reader reader = new StringReader(string);
        InputSource source = new InputSource(reader);
        SparqlResults results = parse(source);
        try {
            reader.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return results;
    }

    public SparqlResults parse(File file) {
        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        }
        SparqlResults results = parse(reader);
        // log.info("size in parse3 = "+results.getResults().size());
        try {
            reader.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        // log.info("size in parse4 = "+results.getResults().size());
        return results;
    }

    public SparqlResults parse(Reader reader) {

        SparqlResults results = parse(new InputSource(reader));
        // log.info("size in parse2 = "+results.getResults().size());
        return results;
    }

    public SparqlResults parse(InputSource source) {
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(this);
            parser.parse(source);
            return getSparqlResults();
            
//            SparqlResultsParser handler = new SparqlResultsParser();
//            XMLReader parser = XMLReaderFactory.createXMLReader();
//            parser.setContentHandler(handler);
//            parser.parse(source);
//            return handler.getSparqlResults();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public SparqlResults getSparqlResults() {
        return resultsObject;
    }

	// methods from ContentHandler
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {

        // throw new RuntimeException("Local Name = "+localName);
        log.debug("START ELEMENT = " + localName);
//System.out.println("START ELEMENT = " + localName);
        try {
            state = State.valueOf(localName);
        } catch (IllegalArgumentException e) {
            log.debug("START ELEMENT : " + localName +"\n"+e.getMessage());
        //    throw new IllegalArgumentException(e.getMessage()
        //            + "\nSPARQL = "+getSparql()+"\nXML local name was " + localName);
        }

        switch (state) {
            case variable:
                break;
            case result:
                currentResult = resultsObject.createResult();
                break;
            case binding:
                currentBinding = resultsObject.createBinding();
                currentName = atts.getValue("name");
                // log.info(currentName);
                break;
            case uri:
                // hasText = true;
                break;
            case bnode:
                // hasText = true;
                break;
            case literal:
                // hasText = true;
                break;
            default:
                break;
        }

    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        textBuffer.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        String text = textBuffer.toString();
        // log.info("text = "+text +"\n-------------");
      //  System.out.println("END ELEMENT = " + localName);

        state = State.valueOf(localName);

        switch (state) {
            case variable:
                resultsObject.addVariable(text);
                break;
            case sparql:
                break;
            case head:
                break;
            case results:
                break;
            case result:
                resultsObject.add(currentResult);
                // log.info("adding result "+currentResult);
                break;
            case binding:
                currentResult.add(currentBinding);
                // log.info("adding binding "+currentBinding);
                break;
            case uri:
                currentBinding.setType(SparqlResults.URI_TYPE);
                currentBinding.setName(currentName.trim());
                currentBinding.setValue(text.trim());
                break;
            case bnode:
                currentBinding.setType(SparqlResults.BNODE_TYPE);
                currentBinding.setName(currentName.trim());
                currentBinding.setValue(text.trim());
                break;
            case literal:
                currentBinding.setType(SparqlResults.LITERAL_TYPE);
                currentBinding.setName(currentName.trim());
                currentBinding.setValue(text.trim());
                break;
            default:
                break;

        }
        textBuffer = new StringBuffer();
    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {

    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {

    }

    @Override
    public void processingInstruction(String target, String data)
            throws SAXException {

    }

    @Override
    public void setDocumentLocator(Locator locator) {

    }

    @Override
    public void skippedEntity(String name) throws SAXException {

    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {

    }

}
