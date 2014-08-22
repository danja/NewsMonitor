/**
 * NewsMonitor
 *
 * TextFileReader.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.io;

import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.templating.Templater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.framework.Bundle;

import freemarker.template.Template;

/**
 * 
 */
public class TextFileReader {

	private static Logger log = LoggerFactory.getLogger(TextFileReader.class);
	
	
	/**
	 * @param filename text file to read
	 * @return content of text file
	 */
	public static String readFromFilesystem(String filename) {
	   String content = null;
	   File file = new File(filename); //for ex foo.txt
	   try {
	       FileReader reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   return content;
	}
	
	public static String readFromBundle(Bundle bundle, String path) {
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
}
