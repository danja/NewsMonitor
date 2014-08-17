/**
 * NewsMonitor
 *
 * AllTests.java
 * @author danja
 * @date May 27, 2014
 *
 */
package it.danja.newsmonitor.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.danja.newsmonitor.tests.parsers.*;

import java.util.List;

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
	
	private static Logger log = LoggerFactory.getLogger(AllTests.class);
	
	// TODO HTTP server causes problems
	  public static void main(String[] args) {
		    Result result = JUnitCore.runClasses(AllTests.class);
		    List<Failure> failures = result.getFailures();
		    if(failures.size() == 0) {
		    	log.info("All tests pass.");
		    }
		    for (Failure failure : failures) {
		      log.info("FAIL : "+failure.toString());
		    }
		  }
} 
