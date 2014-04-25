/**
 * feedreader-prototype
 *
 * Configuration.java
 * 
 * @author danja
 * @date Apr 24, 2014
 *
 */
package org.danja.feedreader.main;

/**
 *
 */
public class Configuration {
	
    private static int REFRESH_PERIOD = 10000; // milliseconds
    
	public static int PER_FEED_SLEEP_PERIOD = 1000; /// ????

    private static int MAX_ITEMS = 5;
    
    public static void load() {
    	// TODO read from store
    }
    
    public static int getPollerPeriod() {
    	return REFRESH_PERIOD;
    }
    
    public static int getMaxItems() {
    	return MAX_ITEMS;
    }
}
