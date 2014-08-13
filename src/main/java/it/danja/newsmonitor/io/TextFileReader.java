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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 */
public class TextFileReader {

	/**
	 * @param filename text file to read
	 * @return content of text file
	 */
	public static String read(String filename) {
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
}
