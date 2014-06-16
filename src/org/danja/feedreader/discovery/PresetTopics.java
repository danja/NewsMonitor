/**
 * feedreader-prototype
 *
 * PresetTopics.java
 * @author danja
 * @date Jun 16, 2014
 *
 */
package org.danja.feedreader.discovery;

/**
 *
 */
public class PresetTopics {

	public static Topic SEMWEB_TOPIC = new Topic();
	static {
		SEMWEB_TOPIC.setLongName("Semantic Web");
		SEMWEB_TOPIC.setName("semweb");
		SEMWEB_TOPIC.addKeyword("rdf", 0.9F);
		SEMWEB_TOPIC.addKeyword("semweb", 0.9F);
		SEMWEB_TOPIC.addKeyword("Semantic Web", 1F);
		SEMWEB_TOPIC.addKeyword("linked data", 0.9F);
		SEMWEB_TOPIC.addKeyword("semantic", 0.5F);
		SEMWEB_TOPIC.addKeyword("data", 0.2F);
		SEMWEB_TOPIC.addKeyword("web", 0.2F);
	}

}
