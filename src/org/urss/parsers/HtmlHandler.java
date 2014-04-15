/*
 * Danny Ayers Dec 15, 2004 http://dannyayers.com
 *  
 */
package org.urss.parsers;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class HtmlHandler extends DefaultHandler implements CommentHandler {
    
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
}