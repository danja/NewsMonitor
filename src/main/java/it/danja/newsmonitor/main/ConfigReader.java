package it.danja.newsmonitor.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * ConfigReader class responsible for reading and managing properties from a configuration file.
 * The class provides methods to load properties from a file and retrieve individual values.
 *
 * @author danja
 * @version 1.20.23
 * dc:date 2023-08-14
 */
public class ConfigReader {

  private Properties properties = new Properties();

  /**
   * Retrieves the loaded properties.
   *
   * @return The Properties object containing all loaded properties.
   */
  public Properties getProperties() {
    return properties;
  }

  /**
   * Sets the properties object.
   *
   * @param properties The Properties object to set.
   */
  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public static void main(String[] args) {
    ConfigReader configReader = new ConfigReader();
    try {
      configReader.loadPropertiesFile("config.properties");
    } catch (IOException e) {
      e.printStackTrace();
    }
    Properties props = configReader.getProperties();
    Enumeration<Object> keys = props.keys();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      System.out.println(key + " = " + props.getProperty(key));
    }
  }

  /**
   * Loads properties from a specified file.
   *
   * @param propertiesFileName The name of the properties file to load.
   * @throws IOException if an error occurs while loading the file.
   */
  public void loadPropertiesFile(String propertiesFileName) throws IOException {
    InputStream inputStream = getClass()
      .getClassLoader()
      .getResourceAsStream(propertiesFileName);
    properties.load(inputStream);
    if (inputStream == null) {
      throw new FileNotFoundException(
        "property file '" + propertiesFileName + "' not found in the classpath"
      );
    }
  }

  /**
   * Retrieves a long value from the properties using the specified key.
   *
   * @param key The key to use to retrieve the value.
   * @return The long value associated with the specified key.
   */
  public long getLongValue(String key) {
    String valueString = properties.getProperty(key);
    return Long.parseLong(valueString);
  }
}
