/**
 * NOT USED
 *
 * NewsMonitor
 *
 * HtmlHandler.java
 *
 * @author danja
 * dc:date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.interpreters;

import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.impl.LinkImpl;
import it.danja.newsmonitor.utils.ContentType;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HtmlHandler extends FeedHandlerBase {

    private static Logger log = LoggerFactory.getLogger(InterpreterFactory.class);

    private List links = new ArrayList();

    /*
    typical :
    <link rel="alternate" type="application/rss+xml" title="Aaron Courville &raquo; Feed" href="https://aaroncourville.wordpress.com/feed/" />
    */
    public void startElement(String namespaceURI, String localName,
            String qName, Attributes attributes) {
        
      //   log.info("HtmlHandler.startElement, localName = "+localName);
         
        if ("link".equalsIgnoreCase(localName)) {
            
            log.info("found <link> tag");
            Link link = new LinkImpl();
            
            log.info("rel = '"+attributes.getValue("rel")+"'");
            link.setRel(attributes.getValue("rel"));
            
            log.info("href = '"+attributes.getValue("href")+"'");
            link.setHref(attributes.getValue("href"));
            
            log.info("type = '"+attributes.getValue("type")+"'");
            link.setContentType(attributes.getValue("type"));
            
            link.setFormat(ContentType.getTypeName(attributes.getValue("type")));
            // type = ContentType.getTypeName(type);
            
            log.info("link = \n"+link);
            
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
