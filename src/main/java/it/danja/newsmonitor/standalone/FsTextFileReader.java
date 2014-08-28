/**
 * NewsMonitor
 *
 * TextFileReader.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.standalone;

import it.danja.newsmonitor.io.TextFileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
public class FsTextFileReader implements TextFileReader {

	private static Logger log = LoggerFactory.getLogger(FsTextFileReader.class);
	
	public FsTextFileReader(){
		
	}
	/**
	 * @param filename text file to read
	 * @return content of text file
	 */
	public String read(String filename) {
	   String content = null;
	   File file = new File(filename); //for ex foo.txt
	   try {
	       FileReader reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       log.error(e.getMessage());
	   }
	   return content;
	}
	
	@Override
	public Reader getReader(String location) {
		File file = new File(location);
		Reader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
		return reader;
	}
}
