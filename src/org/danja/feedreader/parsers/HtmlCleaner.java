/**
 * feedreader-prototype
 *
 * HtmlCleaner.java
 * @author danja
 * @date May 26, 2014
 *
 */
package org.danja.feedreader.parsers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class HtmlCleaner {
	
	private static final String[] excludeAttrs = {"font", "class"};
	
	public static final Set<String> excludeAttributes = new HashSet<String>();
	static {
		Collections.addAll(excludeAttributes, excludeAttrs);
	}
	public static String normaliseElement(String localName) {
		if("b".equals(localName)) {
			return "strong";
		}
		if("i".equals(localName)) {
			return "em";
		}
		return localName;
	}

}
