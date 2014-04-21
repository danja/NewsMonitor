package org.danja.feedreader.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import org.danja.feedreader.parsers.SparqlResults.Binding;
import org.danja.feedreader.parsers.SparqlResults.Result;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * SAX-based SPARQL XML Result Set format parser
 */
public class SparqlResultsParser implements ContentHandler {

	private static final String SPARQL_NS = "http://www.w3.org/2005/sparql-results#";

	private enum State {
		variable, sparql, head, results, result, bindings, binding, uri, bnode, literal;
	}

	private SparqlResults results = new SparqlResults();
	private State state;
	private StringBuffer textBuffer = new StringBuffer();
	private boolean hasText = false;
	
	
	private String currentName = null;
	private Set currentResults = new HashSet<Result>();
	private Result currentResult = null;
	private Binding currentBinding = null;
	

	public SparqlResultsParser() {
	}

	public static void main(String[] args) {
		SparqlResultsParser parser = new SparqlResultsParser();
		File file = new File("input/sparql-xml-results-sample.xml");
		parser.parse(file);
		System.out.println(parser.getResults());
	}

	// Custom type conversion methods - see
	// http://www.ibm.com/developerworks/library/x-tipsaxis/

	public SparqlResults parse(File file) {
		FileReader reader = null;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		SparqlResults results = parse(reader);
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

	public SparqlResults parse(Reader reader) {
		return parse(new InputSource(reader));
	}

	public SparqlResults parse(InputSource source) {
		try {
			SparqlResultsParser handler = new SparqlResultsParser();
			XMLReader parser = XMLReaderFactory.createXMLReader();
			parser.setContentHandler(handler);
			parser.parse(source);
			return handler.getResults();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
	}

	public SparqlResults getResults() {
		return results;
	}

	// methods from ContentHandler

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		System.out.println("\nuri = " + uri);
		System.out.println("localName = " + localName);
		System.out.println("qName = " + uri);
		System.out.println("atts = " + uri);
		
		
		//textBuffer.delete(0, textBuffer.length() - 1);
		textBuffer = new StringBuffer();
		
		state = State.valueOf(localName);

		switch (state) {
		case variable:
			hasText = true;
			break;
			case result:
				currentResult  = results.createResult();
				break;
			case binding:
				currentBinding = results.createBinding();
				currentName = atts.getValue(SPARQL_NS, "name");
				break;
			case uri:
				hasText = true;
				break;
			case bnode:
				hasText = true;
				break;
			case literal:
				hasText = true;
				break;
			default:
				break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		textBuffer.append(ch);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		String text = textBuffer.toString();

		state = State.valueOf(localName);

		switch (state) {
		case variable:
			results.addVariable(text);
			break;
		case sparql:
			break;
		case head:
			break;
		case results:
			break;
		case result:
			currentResults.add(currentResult);
			System.out.println("adding result "+currentResult);
			break;
		case binding:
			currentResult.add(currentBinding);
			System.out.println("adding binding "+currentBinding);
			break;
		case uri:
			currentBinding.setType(SparqlResults.URI_TYPE);
			currentBinding.setName(currentName);
			currentBinding.setValue(text);
			break;
		case bnode:
			currentBinding.setType(SparqlResults.BNODE_TYPE);
			currentBinding.setName(currentName);
			currentBinding.setValue(text);
			break;
		case literal:
			currentBinding.setType(SparqlResults.LITERAL_TYPE);
			currentBinding.setName(currentName);
			currentBinding.setValue(text);
			break;
		default:
			break;

		}
	
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