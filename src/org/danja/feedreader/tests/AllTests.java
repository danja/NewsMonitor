/**
 * feedreader-prototype
 *
 * AllTests.java
 * @author danja
 * @date May 27, 2014
 *
 */
package org.danja.feedreader.tests;

import java.util.List;

import org.danja.feedreader.tests.parsers.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestAtomParser.class, 
	TestAtomSniffer.class,
	TestRss1Parser.class, 
	TestRss1Sniffer.class,
	TestRss2Parser.class, 
	TestRss2Sniffer.class,
	TestContentProcessor.class
	})
public class AllTests {
	
	// TODO HTTP server causes problems
	  public static void main(String[] args) {
		    Result result = JUnitCore.runClasses(AllTests.class);
		    List<Failure> failures = result.getFailures();
		    if(failures.size() == 0) {
		    	System.out.println("All tests pass.");
		    }
		    for (Failure failure : failures) {
		      System.out.println("FAIL : "+failure.toString());
		    }
		  }
} 
