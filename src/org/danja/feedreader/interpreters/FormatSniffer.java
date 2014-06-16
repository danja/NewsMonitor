/**
 * feedreader-prototype
 *
 * FormatSniffer.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.interpreters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

import org.danja.feedreader.model.ContentType;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


public class FormatSniffer extends DefaultHandler {

    char format = ContentType.UNKNOWN;
    String[] strings = {"<rdf:RDF", "<rss", "<feed"};
    
    public char sniff(InputStream inputStream) {
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
			// return null;
		}
    	// System.out.println(data);
    	if(data.indexOf("<!DOCTYPE html") != -1) {
    		return ContentType.HTML;
    	}
    	boolean match = false;
    	String matched = "";
    	for(int i=0;i<strings.length;i++){
    		if(data.indexOf(strings[i]) != -1) {
    			match = true;
    			matched = strings[i];
    			break;
    		}
    	}
    	// System.out.println("MATCH = "+matched);
    	if(!match) {
    	//	System.out.println("NO MATCH");
    		return ContentType.UNKNOWN;
    	}
    	try {
			inputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        XMLReader reader = null;
        try {
            reader = parserFactory.newSAXParser().getXMLReader();
        } catch (Exception e) { // bad
            e.printStackTrace();
        }
        reader.setContentHandler(this);
        
        InputSource inputSource = new InputSource(new StringReader(data));

       // InputSource inputSource = new InputSource(inputStream);
        try {
            reader.parse(inputSource); // TODO TIMEOUT HERE!?
            
        } catch (Exception e) { // whatever the problem, there's no XML available
            return ContentType.RSS_SOUP;
        }
        try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return format; // 
    }
    
    public void startElement(String namespaceURI, String localName,
            String qName, Attributes attributes) {
    	// System.out.println("namespaceURI = "+namespaceURI);
    	// System.out.println("localName = "+localName);
        if(namespaceURI.equals(NamespaceConstants.RDF_NS) && localName.equals("RDF")){
            format = ContentType.RDF_OTHER;
        }
        if(namespaceURI.equals(NamespaceConstants.RSS1_NS) && localName.equals("items")){
            format = ContentType.RSS1;
        }
        if(localName.equals("rss")){
            format = ContentType.RSS2;
        }
        if(namespaceURI.startsWith(NamespaceConstants.ATOM_NS) && localName.equals("feed")){
            format = ContentType.ATOM;
        }
    }
}