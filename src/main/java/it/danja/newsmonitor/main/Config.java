/**
 * Configuration class for the NewsMonitor application.
 * This class currently contains hard-coded constants for various settings, with plans for future
 * migration to an external file.
 *
 * TODO move this to an external declarative file (RDF, properties or similar)
 *
 * @author danja
 * @version 1.20.23
 * dc:date 2023-08-14
 */
package it.danja.newsmonitor.main;

import it.danja.newsmonitor.discovery.PresetTopics;
import it.danja.newsmonitor.discovery.Topic;

public class Config {

  // Topic for the monitoring; this may be moved to a map in future implementations.
  public static final Topic TOPIC = PresetTopics.ALL;

  /**
   * Location for the bundle configuration properties file.
   */
  public static final String CONFIG_PROPERTIES_BUNDLE_LOCATION =
    "bundle-config.properties";

  /**
   * Location for the standalone configuration properties file.
   */
  public static final String CONFIG_PROPERTIES_STANDALONE_LOCATION =
    "standalone-config.properties";
}
