/**
 * NewsMonitor
 *
 * Main.java
 * @author danja
 * @date Aug 18, 2014
 *
 */
package it.danja.newsmonitor.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class Main {

	private static Logger log = LoggerFactory.getLogger(Main.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.BUILD_TYPE = Config.STANDALONE_BUILD;
		NewsMonitor nm = new NewsMonitor();
		if(args.length > 1) {
			log.info("args[0] = "+args[0]);
			if("-C".equals(args[0])) {
				nm.start(Config.SEED_FEEDLIST);
			} 
			if("-f".equals(args[0])) {
				nm.start(args[1]);
			} 
		} else {
			nm.start();
		}

	}

}
