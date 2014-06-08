/**
 * feedreader-prototype
 *
 * HtmlHandler.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.interpreters;

import java.util.ArrayList;
import java.util.List;

import org.danja.feedreader.feeds.Link;
import org.danja.feedreader.feeds.impl.LinkImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HtmlHandler extends FeedHandlerBase {
    
    private List links = new ArrayList();

    public void startElement(String namespaceURI, String localName,
            String qName, Attributes attributes) {
        if ("link".equalsIgnoreCase(localName)) {
            Link link = new LinkImpl();
            link.setRel(attributes.getValue("rel"));
            link.setHref(attributes.getValue("href"));
            link.setType(attributes.getValue("type"));
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