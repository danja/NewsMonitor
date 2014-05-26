/**
 * feedreader-prototype
 *
 * HtmlHandler.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.parsers;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HtmlHandler extends FeedHandler implements CommentHandler {
    
    private List links = new ArrayList();

    public void startElement(String namespaceURI, String localName,
            String qName, Attributes attributes) {
        if ("link".equalsIgnoreCase(localName)) {
            Link link = new Link();
            link.rel = attributes.getValue("rel");
            link.href = attributes.getValue("href");
            link.type = attributes.getValue("type");
            links.add(link);
        }
    }

    public List getLinks() {
        return links;
    }

    private List comments = new ArrayList();
    
    public List getComments() {
        return comments;
    }

    public void comment(String comment) {
        comments.add(comment);
    }

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}
}