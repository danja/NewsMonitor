/**
 * NewsMonitor
 *
 * FormatSniffer.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.interpreters;

import it.danja.newsmonitor.io.HttpConnector;
import it.danja.newsmonitor.utils.ContentType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class FormatSniffer extends DefaultHandler {
	
	private static Logger log = LoggerFactory.getLogger(FormatSniffer.class);

	char format = ContentType.UNKNOWN;

	// String[] strings = {"<rdf:RDF", "<rss", "<feed"};

	public char sniff(HttpConnector httpConnector) {
		InputStream inputStream = httpConnector.getInputStream();
		Reader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader in = new BufferedReader(inputStreamReader);
		StringBuffer buffer = new StringBuffer();
		String readLine;
		try {
			while ((readLine = in.readLine()) != null) {
				buffer.append(readLine);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String data = buffer.toString();
		try {
			in.close();
		} catch (IOException e2) {
			return ContentType.UNAVAILABLE;
		}
		// log.info(data);
		// if(data.indexOf("<!DOCTYPE html") != -1) {
		// return ContentType.HTML;
		// }
		// boolean match = false;
		// String matched = "";
		// for(int i=0;i<strings.length;i++){
		// if(data.indexOf(strings[i]) != -1) {
		// match = true;
		// matched = strings[i];
		// break;
		// }
		// }
		// log.info("MATCH = "+matched);
		// if(!match) {
		// // log.info("NO MATCH");
		// return ContentType.UNKNOWN;
		// }
		// try {
		// inputStream.close();
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }

		// First try parsing the data as XML
		boolean notXml = false;

		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		parserFactory.setNamespaceAware(true);
		XMLReader reader = null;
		try {
			reader = parserFactory.newSAXParser().getXMLReader();
		} catch (Exception e) { // bad
			log.error(e.getMessage());
		}
		reader.setContentHandler(this);

		InputSource inputSource = new InputSource(new StringReader(data));

		try {
			reader.parse(inputSource); // TODO TIMEOUT HERE!?
		} catch (Exception e) { // whatever the problem, it's not XML
			// format = ContentType.RSS_SOUP;
			notXml = true;
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		if (format != ContentType.UNKNOWN) {
			return format;
		}
		return ContentType.identifyFormat(httpConnector.getContentType(), data);
	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attributes) {
		// log.info("namespaceURI = "+namespaceURI);
		// log.info("localName = "+localName);
		if (namespaceURI.equals(NamespaceConstants.RDF_NS)
				&& localName.equals("RDF")) {
			format = ContentType.RDF_OTHER;
		}
		if (namespaceURI.equals(NamespaceConstants.RSS1_NS)
				&& localName.equals("items")) {
			format = ContentType.RSS1;
		}
		if (localName.equals("rss")) {
			format = ContentType.RSS2;
		}
		if (namespaceURI.startsWith(NamespaceConstants.ATOM_NS)
				&& localName.equals("feed")) {
			format = ContentType.ATOM;
		}
	}
}