/**
 * NewsMonitor
 *
 * PresetTopics.java
 * @author danja
 * @date Jun 16, 2014
 *
 */
package it.danja.newsmonitor.discovery;

/**
 * TODO load these in from Turtle
 */
public class PresetTopics {

	public static Topic SEMWEB = new Topic();
	static {
		SEMWEB.setLongName("Semantic Web");
		SEMWEB.setName("semweb");
		SEMWEB.addKeyword("rdf", 0.9F);
		SEMWEB.addKeyword("semweb", 0.9F);
		SEMWEB.addKeyword("Semantic Web", 1F);
		SEMWEB.addKeyword("linked data", 0.9F);
		SEMWEB.addKeyword("semantic", 0.5F);
		SEMWEB.addKeyword("data", 0.2F);
		SEMWEB.addKeyword("web", 0.2F);
		SEMWEB.addKeyword("schema.org", 0.8F);
		SEMWEB.addKeyword("JSON-LD", 0.9F);
		SEMWEB.addKeyword("W3C", 0.4F);
		SEMWEB.addKeyword("RDFa", 0.9F);
		SEMWEB.addKeyword("SPARQL", 0.9F);
	}

	public static Topic WOODCARVING = new Topic();
	static {
		WOODCARVING.setLongName("Wood Carving");
		WOODCARVING.setName("woodcarving");
		WOODCARVING.addKeyword("woodcarving", 1F);
		
		WOODCARVING.addKeyword("carving", 0.8F);
		WOODCARVING.addKeyword("carve", 0.8F);
		WOODCARVING.addKeyword("woodcarving", 0.8F);
		WOODCARVING.addKeyword("whittle", 0.8F);
		WOODCARVING.addKeyword("whittling", 0.8F);
		
		WOODCARVING.addKeyword("relief", 0.6F);
		WOODCARVING.addKeyword("traditional", 0.6F);
		WOODCARVING.addKeyword("gouge", 0.6F);
		WOODCARVING.addKeyword("carvings", 0.6F);
		
		WOODCARVING.addKeyword("wood", 0.4F);
		WOODCARVING.addKeyword("wooden", 0.4F);
		WOODCARVING.addKeyword("woodwork", 0.4F);
		WOODCARVING.addKeyword("woodworking", 0.4F);
		WOODCARVING.addKeyword("spoon", 0.4F);
		WOODCARVING.addKeyword("kuksa", 0.4F);
		WOODCARVING.addKeyword("knife", 0.4F);
		WOODCARVING.addKeyword("knives", 0.4F);
		WOODCARVING.addKeyword("green", 0.4F);
		WOODCARVING.addKeyword("hone", 0.4F);
		WOODCARVING.addKeyword("honing", 0.4F);
		WOODCARVING.addKeyword("green", 0.4F);
		WOODCARVING.addKeyword("medieval", 0.4F);
		WOODCARVING.addKeyword("designs", 0.4F);
		WOODCARVING.addKeyword("sculpture", 0.4F);
		WOODCARVING.addKeyword("netsuke", 0.4F);
		WOODCARVING.addKeyword("chess", 0.4F);
		
		WOODCARVING.addKeyword("lettering", 0.1F);
		WOODCARVING.addKeyword("hand", 0.1F);
		WOODCARVING.addKeyword("tools", 0.1F);
		WOODCARVING.addKeyword("craft", 0.1F);
		WOODCARVING.addKeyword("box", 0.1F);
		WOODCARVING.addKeyword("oak", 0.1F);
		WOODCARVING.addKeyword("chestnut", 0.1F);
		WOODCARVING.addKeyword("basswood", 0.1F);
		WOODCARVING.addKeyword("lime", 0.1F);
		WOODCARVING.addKeyword("lathe", 0.1F);
		WOODCARVING.addKeyword("plane", 0.1F);
		WOODCARVING.addKeyword("bandsaw", 0.1F);
		WOODCARVING.addKeyword("mallet", 0.1F);
		WOODCARVING.addKeyword("bench", 0.1F);
		WOODCARVING.addKeyword("workbench", 0.1F);
		WOODCARVING.addKeyword("workshop", 0.1F);
		WOODCARVING.addKeyword("art", 0.1F);
		WOODCARVING.addKeyword("design", 0.1F);
	}
}
