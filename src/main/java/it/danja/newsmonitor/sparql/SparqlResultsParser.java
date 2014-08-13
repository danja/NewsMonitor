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

	private static final String SPARQL_NS = "http://www.w3.org/2005/sparql-resultsObject#";

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
	//	System.out.println("Size in main = "+sparqlResults.getResults().size());
		System.out.println(sparqlResults.getResults());
	}

	// Custom type conversion methods - see
	// http://www.ibm.com/developerworks/library/x-tipsaxis/
	
	public SparqlResults parse(String string) {
		InputSource source = new InputSource( new StringReader(string));
		return parse(source);
	}

	public SparqlResults parse(File file) {
		FileReader reader = null;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		SparqlResults results = parse(reader);
	//	System.out.println("size in parse3 = "+results.getResults().size());
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	//	System.out.println("size in parse4 = "+results.getResults().size());
		return results;
	}

	public SparqlResults parse(Reader reader) {
		
		SparqlResults results = parse(new InputSource(reader));
	//	System.out.println("size in parse2 = "+results.getResults().size());
		return results;
	}

	public SparqlResults parse(InputSource source) {
		try {
			SparqlResultsParser handler = new SparqlResultsParser();
			XMLReader parser = XMLReaderFactory.createXMLReader();
			parser.setContentHandler(handler);
			parser.parse(source);
		//	System.out.println("size in parse = "+handler.getSparqlResults().getResults().size());
			return handler.getSparqlResults();
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

		state = State.valueOf(localName);

		switch (state) {
		case variable:
			break;
		case result:
			currentResult = resultsObject.createResult();
			break;
		case binding:
			currentBinding = resultsObject.createBinding();
			currentName = atts.getValue("name");
		//	System.out.println(currentName);
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
		// System.out.println("text = "+text +"\n-------------");

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
		//	System.out.println("adding result "+currentResult);
			break;
		case binding:
			currentResult.add(currentBinding);
			// System.out.println("adding binding "+currentBinding);
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