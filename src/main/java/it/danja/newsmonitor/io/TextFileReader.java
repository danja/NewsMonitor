/**
 * 
 */
package it.danja.newsmonitor.io;

import java.io.Reader;

/**
 * @author danny
 *
 */
public interface TextFileReader {
	
	public String read(String location);

	public Reader getReader(String location);
	
}
