/**
 * NewsMonitor - FeedListLoader
 *
 * @author danja
 * @version 1.20.23
 * dc:date 2023-08-14
 * @since Apr 25, 2014
 */
package it.danja.newsmonitor.main;

import it.danja.newsmonitor.io.TextFileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for reading a text list from disk, wrapping it with a SPARQL template, and pushing it into a store.
 */
public class FeedListLoader {

  private static Logger log = LoggerFactory.getLogger(FeedListLoader.class);
  private static int urlCount = 0;

  public static String SPARQL_TEMPLATE =
    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
    "PREFIX rss: <http://purl.org/rss/1.0/> \n" +
    "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n" +
    "INSERT DATA {\n ${channels} \n}";

  public static String CHANNEL_TEMPLATE =
    "<${url}> rdf:type rss:channel ; \n" +
    "foaf:topic <https://en.wikipedia.org/wiki/Deep_learning> . \n\n";

  private Properties config = null;
  private TextFileReader textFileReader = null;

  /**
   * Constructs a FeedListLoader object with the given configuration and TextFileReader.
   *
   * @param config the properties configuration
   * @param textFileReader the reader to read text files
   */
  public FeedListLoader(Properties config, TextFileReader textFileReader) {
    this.config = config;
    this.textFileReader = textFileReader;
  }

  /**
   * Reads a text file from disk line by line.
   *
   * TODO switch ad hoc templating to freemarker
   *
   * @param location the location of the target file
   * @return String containing text content
   */
  public String readFile(String location) {
    BufferedReader reader = new BufferedReader(
      textFileReader.getReader(location)
    );
    LineHandler handler = new LineHandler();

    String line = null;
    try {
      while ((line = reader.readLine()) != null) {
        handler.handle(line);
      }
      reader.close();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    log.info(urlCount + " URLs loaded from file");
    return handler.getTurtleBody();
  }

  /**
   * Ultra-crude templating method.
   *
   * TODO ready to remove (using freemarker instead)
   *
   * @param template the template string
   * @param name the name of the placeholder
   * @param value the value to be inserted
   * @return the template with the placeholder replaced by the value
   */
  public static String insertValue(String template, String name, String value) {
    return template.replace("${" + name + "}", value);
  }

  /**
   * Inner class responsible for handling lines from the text file.
   */
  class LineHandler {

    StringBuffer targetBuffer = new StringBuffer();

    public void handle(String line) {
      if (line.trim().length() != 0 && line.charAt(0) != '#') {
        String sparql = FeedListLoader.insertValue(
          CHANNEL_TEMPLATE,
          "url",
          line.trim()
        );
        targetBuffer.append(sparql);
        urlCount++;
      }
    }

    public String getTurtleBody() {
      return targetBuffer.toString();
    }
  }
}
