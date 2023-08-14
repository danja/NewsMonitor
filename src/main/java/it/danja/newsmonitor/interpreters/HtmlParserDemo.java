/**
 * NewsMonitor
 *
 * HtmlParserDemo.java
 * 
 * @author danja
 * dc:date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.interpreters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.io.HttpConnector;
// import it.danja.newsmonitor.main.DemoConstants;
import it.danja.newsmonitor.model.Link;

import java.io.InputStream;
import java.util.List;

 
public class HtmlParserDemo {
	private static Logger log = LoggerFactory.getLogger(HtmlParserDemo.class);
	
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

            HttpConnector connector = new HttpConnector(null);
            connector.setUrl(uriString);
            boolean streamAvailable = connector.load();
            if (streamAvailable) {
                InputStream inputStream = connector.getInputStream();
                parser.parse(inputStream);
                links = handler.getLinks();
                comments = handler.getComments();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
        HtmlParserDemo reader = new HtmlParserDemo();
        String url = "http://fgiasson.com/blog/index.php/2014/04/14/the-open-semantic-framework-academy/";

        if(args.length > 0) {
        	url = args[0];
        }
        log.info("Reading "+url);
        reader.parseHTML(url);
        List links = reader.getLinks();

        for (int i = 0; i < links.size(); i++) {
            Link link = (Link) links.get(i);
//            if (link.isHtmlAlternate()) {
//                log.info(link);
//            }
        }
        List comments = reader.getComments();
        for (int i = 0; i < comments.size(); i++) {
            log.info(comments.get(i).toString());
        }
    }
}