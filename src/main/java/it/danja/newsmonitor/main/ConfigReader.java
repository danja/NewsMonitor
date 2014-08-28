package it.danja.newsmonitor.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class ConfigReader {

	private Properties properties = new Properties();

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public static void main(String[] args) {
		// "src/main/resources/config.properties";

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

	public void loadPropertiesFile(String propertiesFileName) throws IOException {

		// FileInputStream in = new FileInputStream(propertiesFileName);
		// properties.load(in);
		// in.close();

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(propertiesFileName);
		properties.load(inputStream);
		if (inputStream == null) {
			throw new FileNotFoundException("property file '"
					+ propertiesFileName + "' not found in the classpath");
		}
	}

	public long getLongValue(String key) {
		String valueString = properties.getProperty(key);
		return Long.parseLong(valueString);
	}
}
