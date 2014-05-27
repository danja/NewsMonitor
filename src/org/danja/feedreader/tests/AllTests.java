/**
 * feedreader-prototype
 *
 * AllTests.java
 * @author danja
 * @date May 27, 2014
 *
 */
package org.danja.feedreader.tests;

import org.danja.feedreader.tests.parsers.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestAtomParser.class, TestRss1Parser.class, TestRss2Parser.class })
public class AllTests {

} 
