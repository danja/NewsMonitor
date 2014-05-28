/**
 * feedreader-prototype
 *
 * SparqlTemplater.java
 * @author danja
 * @date May 28, 2014
 *
 */
package org.danja.feedreader.sparql;

import org.danja.feedreader.io.TextFileReader;
import org.danja.feedreader.main.Config;

/**
 *
 */
public class SparqlTemplater {

	private static final String PREFIXES;
	
	static {
		PREFIXES = TextFileReader.read(Config.SPARQL_PREFIXES_FILENAME);
	}
	
	public static void main(String[] args) {
		System.out.println(PREFIXES);
	}
}
