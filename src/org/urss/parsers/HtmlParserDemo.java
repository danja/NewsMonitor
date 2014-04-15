/*
 * Danny Ayers Aug 18, 2004 http://dannyayers.com
 *  
 */
package org.urss.parsers;

import java.io.InputStream;
import java.util.List;

import org.urss.io.HttpConnector;

 
public class HtmlParserDemo {
    private List links;

    private List comments;

    public List getComments() {
        return comments;
    }

    public List getLinks() {
        return links;
    }

    public void parseHTML(String uriString) {

        try {
            SoupParser parser = new SoupParser();
            HtmlHandler handler = new HtmlHandler();

            parser.setContentHandler(handler);

            HttpConnector connector = new HttpConnector(uriString);
            boolean streamAvailable = connector.load();
            if (streamAvailable) {
                InputStream inputStream = connector.getInputStream();
                parser.parse(inputStream);
                links = handler.getLinks();
                comments = handler.getComments();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HtmlParserDemo reader = new HtmlParserDemo();
        reader.parseHTML(args[0]);
        List links = reader.getLinks();

        for (int i = 0; i < links.size(); i++) {
            Link link = (Link) links.get(i);
            if (link.isAlternate()) {
                System.out.println(link);
            }
        }
        List comments = reader.getComments();
        for (int i = 0; i < comments.size(); i++) {
            System.out.println(comments.get(i));
        }
    }
}