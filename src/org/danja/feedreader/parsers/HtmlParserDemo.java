/**
 * feedreader-prototype
 *
 * HtmlParserDemo.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.parsers;

import java.io.InputStream;
import java.util.List;

import org.danja.feedreader.feeds.Link;
import org.danja.feedreader.io.HttpConnector;
import org.danja.feedreader.main.DemoConstants;

 
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

            parser.setHandler(handler);

            HttpConnector connector = new HttpConnector();
            connector.setUrl(uriString);
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
        String url = "http://fgiasson.com/blog/index.php/2014/04/14/the-open-semantic-framework-academy/";

        if(args.length > 0) {
        	url = args[0];
        }
        System.out.println("Reading "+url);
        reader.parseHTML(url);
        List links = reader.getLinks();

        for (int i = 0; i < links.size(); i++) {
            Link link = (Link) links.get(i);
//            if (link.isHtmlAlternate()) {
//                System.out.println(link);
//            }
        }
        List comments = reader.getComments();
        for (int i = 0; i < comments.size(); i++) {
            System.out.println(comments.get(i));
        }
    }
}