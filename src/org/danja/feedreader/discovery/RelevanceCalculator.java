/**
 * feedreader-prototype
 *
 * RelevanceCalculator.java
 * @author danja
 * @date Jun 12, 2014
 *
 */
package org.danja.feedreader.discovery;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.danja.feedreader.io.TextFileReader;
import org.danja.feedreader.main.Config;
import org.danja.feedreader.utils.HtmlCleaner;

/**
 *
 */
public class RelevanceCalculator {

	public static Set<String> STOPWORDS = new HashSet<String>();
	/**
	 * 
	 */
	static {
		String wordString = TextFileReader.read(Config.STOPWORDS_FILENAME);
		String[] split = wordString.toLowerCase().split("\\s+");
		STOPWORDS.addAll(Arrays.asList(split));
		Iterator<String> iterator = STOPWORDS.iterator();
		while (iterator.hasNext()) {
			String stopWord = iterator.next();
			if(stopWord == null || stopWord.trim().equals("")) {
				STOPWORDS.remove(stopWord);
			}
		}
	}

	/**
	 * 
	 */
	public RelevanceCalculator() {
	}

	public float calculateRelevance(Topic topic, String content) {

		content = pruneContent(content);
		System.out.println("PRUNED = "+content);

		int words = getCount("(\\w+)", content);

		System.out.println("words = "+words);
		
		float relevance = 0;

		Map<String, Float> keywords = topic.getKeywords();
		Iterator<String> iterator = keywords.keySet().iterator();

	//	float keywordFactor = 0;

		while (iterator.hasNext()) {
			String keyword = iterator.next();
		//	System.out.println("K = "+keyword);
			int keywordCount = getCount("(" + keyword.toLowerCase() + ")", content);
			System.out.println(keyword + " : "+keywordCount+" occurences");
			float keywordRelevance = keywords.get(keyword);
			System.out.println("keyword relevance = : "+keywordCount);
		//	System.out.println("keywordCount = "+keywordCount);
			relevance += keywordCount * keywordRelevance;
		}
		
	System.out.println("RELEVANCE = "+relevance);
		
	 relevance = relevance/words;
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
		content = HtmlCleaner.unescape(content);
		content = HtmlCleaner.stripTags(content);
		content = content.toLowerCase();
//		content.replaceAll("\"", " ");
//		content.replaceAll(".", " ");
//		content.replaceAll(",", " ");
		
		content = content.replaceAll("[^a-zA-Z]", " "); // or \\p{P}

		return content;
	}
	
	public String removeStopwords(String content, Set<String> stopwords) {
		content = content.replaceAll("[^a-zA-Z]", " ");
		System.out.println("BEFORE = "+content);
		Iterator<String> iterator = STOPWORDS.iterator();
		while (iterator.hasNext()) {
			String stopWord = iterator.next();
			System.out.println("STOPWORD = \""+stopWord+"\"");
			content = content.replaceAll("\\w+"+stopWord+"\\w+", "");
			System.out.println("C = "+content);
		}
		content = content.replaceAll("\\s+", " ").trim();
		System.out.println("AFTER = "+content);
		return content;
	}

	public static void main(String[] args) {
		Iterator<String> iterator = STOPWORDS.iterator();
		while (iterator.hasNext()) {
			String stopWord = iterator.next();

		}
		RelevanceCalculator calculator = new RelevanceCalculator();
		String content = TextFileReader.read("input/html-sample.html");
		//String clean = calculator.cleanContent(content);
		Topic topic = new Topic();
		topic.addKeyword("RDF", 1);
		topic.addKeyword("Semantic", 0.5F);
		topic.addKeyword("Web", 0.1F);
		System.out.println("relevance = "+calculator.calculateRelevance(topic, content));
	}

}
