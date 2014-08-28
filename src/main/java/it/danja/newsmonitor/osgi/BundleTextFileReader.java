/**
 * NewsMonitor
 *
 * TextFileReader.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.osgi;

import it.danja.newsmonitor.io.TextFileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class BundleTextFileReader implements TextFileReader {

	private static Logger log = LoggerFactory.getLogger(BundleTextFileReader.class);
	
	private Bundle bundle = null;
	
	public BundleTextFileReader(Bundle bundle){
		this.bundle = bundle;
	}
	
	public String read(String path) {
		URL url = bundle.getEntry(path);
		StringBuffer buffer = null;
		// if (url != null) {
		//InputStream inputStream = null;
		if(url == null) {
			throw new RuntimeException("Null URL from path  "+path);
		}
		try {
			InputStream  inputStream = url.openStream();

			String string = "";
			buffer = new StringBuffer();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(inputStream));
			
			 while ((string = reader.readLine()) != null) {
			 buffer.append(string + "\n");
			 }
			reader.close();
		} catch (IOException ex) {
			log.error(ex.getMessage());
		}
		return buffer.toString();
	}

	@Override
	public Reader getReader(String location) {
		URL url = bundle.getEntry(location); // SEED_FEEDLIST_IN_BUNDLE
		StringBuffer buffer = null;
		Reader reader = null;

		if(url == null) {
			throw new RuntimeException("Null URL from path  "+location);
		}
		try {
			InputStream  inputStream = url.openStream();

			reader = new BufferedReader(
					new InputStreamReader(inputStream));
		} catch (IOException ex) {
			log.error(ex.getMessage());
		}
		return reader;
	}
}
