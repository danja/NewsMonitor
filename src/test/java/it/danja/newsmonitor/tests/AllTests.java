/**
 * NewsMonitor
 *
 * AllTests.java
 * @author danja
 * dc:date May 27, 2014
 *
 */
package it.danja.newsmonitor.tests;

import it.danja.newsmonitor.tests.parsers.TestAtomParser;
import it.danja.newsmonitor.tests.parsers.TestAtomSniffer;
import it.danja.newsmonitor.tests.utils.TestContentProcessor;
import it.danja.newsmonitor.tests.parsers.TestRss1Parser;
import it.danja.newsmonitor.tests.parsers.TestRss1Sniffer;
import it.danja.newsmonitor.tests.parsers.TestRss2Parser;
import it.danja.newsmonitor.tests.parsers.TestRss2Sniffer;

import java.util.List;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
