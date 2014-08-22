/**
 * NewsMonitor
 *
 * RelevanceCalculator.java
 * @author danja
 * @date Jun 12, 2014
 *
 */
package it.danja.newsmonitor.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.io.TextFileReader;
import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.utils.ContentProcessor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class RelevanceCalculator {
	
	private static Logger log = LoggerFactory.getLogger(RelevanceCalculator.class);

	public static Set<String> STOPWORDS = new HashSet<String>();
	/**
	 * 
	 */
//	static {
//		String wordString = TextFileReader.read(Config.STOPWORDS_FILENAME);
//		String[] split = wordString.toLowerCase().split("\\s+");
//		STOPWORDS.addAll(Arrays.asList(split));
//		Iterator<String> iterator = STOPWORDS.iterator();
//		while (iterator.hasNext()) {
//			String stopWord = iterator.next();
//			if(stopWord == null || stopWord.trim().equals("")) {
//				STOPWORDS.remove(stopWord);
//			}
//		}
//	}

	/**
	 * 
	 */
	public RelevanceCalculator() {
	}

	public float calculateRelevance(Topic topic, String content) {

		content = pruneContent(content);
		// log.info("PRUNED = "+content);

		int words = getCount("(\\w+)", content);

		// log.info("words = "+words);
		
		float relevance = 0;

		Map<String, Float> keywords = topic.getKeywords();
		Iterator<String> iterator = keywords.keySet().iterator();

	//	float keywordFactor = 0;

		while (iterator.hasNext()) {
			String keyword = iterator.next();
		//	log.info("K = "+keyword);
			int keywordCount = getCount("(" + keyword.toLowerCase() + ")", content);
		//	log.info(keyword + " : "+keywordCount+" occurences");
			float keywordRelevance = keywords.get(keyword);
		//	log.info("keyword relevance = : "+keywordCount);
		//	log.info("keywordCount = "+keywordCount);
			relevance += keywordCount * keywordRelevance;
		}
		relevance = relevance/(words+1);
		
		relevance = relevance*100; // scale - is easier to play with
		// log.info("* Relevance = "+relevance);
		return relevance;
	}

	private int getCount(String regex, String content) {
		Pattern wordPattern = Pattern.compile(regex);
		Matcher wordMatcher = wordPattern.matcher(content);

		int count = 0;
		while (wordMatcher.find())
			count++;
		return count;
	}

	private String pruneContent(String content) {
		content = ContentProcessor.unescape(content);
		content = ContentProcessor.stripTags(content);
		content = content.toLowerCase();
		content = content.replaceAll("\\p{P}", " "); // [^a-zA-Z]or \\p{P}
		return content;
	}
	
	public String removeStopwords(String content, Set<String> stopwords) {
		content = content.replaceAll("[^a-zA-Z]", " ");
		log.info("BEFORE = "+content);
		Iterator<String> iterator = STOPWORDS.iterator();
		while (iterator.hasNext()) {
			String stopWord = iterator.next();
			log.info("STOPWORD = \""+stopWord+"\"");
			content = content.replaceAll("\\w+"+stopWord+"\\w+", "");
			log.info("C = "+content);
		}
		content = content.replaceAll("\\s+", " ").trim();
		log.info("AFTER = "+content);
		return content;
	}

	public static void main(String[] args) {
//		Iterator<String> iterator = STOPWORDS.iterator();
//		while (iterator.hasNext()) {
//			String stopWord = iterator.next();
//
//		}
//		RelevanceCalculator calculator = new RelevanceCalculator();
//		String content = TextFileReader.read("input/html-sample.html");
//		//String clean = calculator.cleanContent(content);
//		Topic topic = new Topic();
//		topic.addKeyword("RDF", 1);
//		topic.addKeyword("Semantic", 0.5F);
//		topic.addKeyword("Web", 0.1F);
//		log.info("relevance = "+calculator.calculateRelevance(topic, content));
	}

}
