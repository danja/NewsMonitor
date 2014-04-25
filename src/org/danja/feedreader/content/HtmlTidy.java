/**
 * feedreader-prototype
 *
 * HtmlTidy.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.content;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;

import org.w3c.tidy.Tidy;

/**
 * wrapper for JTidy, HTML cleaner
 */
public class HtmlTidy {
	
	private Tidy jTidy;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InputStream is = null;
		try {
			is = new FileInputStream("docs/manual.html");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		HtmlTidy tidy = new HtmlTidy();
		tidy.init();

  	    tidy.clean(is, System.out); // run tidy, providing an input and output stream
	}
	
	public void clean(InputStream in, OutputStream out) {
		jTidy.parse(in, out);
	}
	
	public void clean(InputStream in, Writer out) {
		jTidy.parse(in, out);
	}

	public void init(){
		jTidy = new Tidy();
		// see http://jtidy.sourceforge.net/apidocs/index.html?org/w3c/tidy/Tidy.html
		jTidy.setDropEmptyParas(true); 
		//	setDropFontTags(true); 
		jTidy.setDropProprietaryAttributes(true); 
		jTidy.setEncloseText(true); 
		jTidy.setEscapeCdata(true); 
		jTidy.setFixComments(true); 
		jTidy.setFixUri(true); 
		jTidy.setForceOutput(true); 
		jTidy.setLogicalEmphasis(true); 
		jTidy.setMakeBare(true); 
		jTidy.setMakeClean(true); 
		jTidy.setOutputEncoding("UTF-8"); 
		jTidy.setPrintBodyOnly(true); 
		jTidy.setTidyMark(false); 
		jTidy.setTrimEmptyElements(true); 
		jTidy.setXHTML(true); 
		 	 	// setXmlOut(true); 

//		    tidy.setTidyMark(false);
//		    tidy.setXHTML(true);
//		    tidy.setXmlOut(false);
//		    tidy.setNumEntities(true);        
//		    tidy.setSpaces(2);
//		    tidy.setWraplen(2000);
//		    tidy.setUpperCaseTags(false);
//		    tidy.setUpperCaseAttrs(false);
//		    tidy.setQuiet(false);
//		    tidy.setMakeClean(true);
//		    tidy.setShowWarnings(true);
//		    tidy.setBreakBeforeBR(true);
//		    tidy.setHideComments(true);
	}
}
