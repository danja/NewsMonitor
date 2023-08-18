/**
 * NewsMonitor application.
 *
 * Contains preset topics for discovery.
 *
 * @author danja
 * @version 1.0
 * @since 2014-06-16
 */

package it.danja.newsmonitor.discovery;

/**
 * Predefined topic models.
 */
public class PresetTopics {

  /**
   * Topic model for Semantic Web.
   */
  public static Topic SEMWEB = new Topic();

  static {
    // Populate SEMWEB topic

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

  /**
   * Topic model for Wood Carving.
   */
  public static Topic WOODCARVING = new Topic();

  static {
    // Populate WOODCARVING topic

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

  /**
   * Topic model for Deep Learning.
   */
  public static Topic DEEP_LEARNING = new Topic();

  static {
    // Populate DEEP_LEARNING topic

    DEEP_LEARNING.setLongName("Deep Learning");
    DEEP_LEARNING.setName("deeps");
    DEEP_LEARNING.addKeyword("gpt", 0.9F);
    DEEP_LEARNING.addKeyword("chatgpt", 0.7F);
    DEEP_LEARNING.addKeyword("llm", 0.8F);
    DEEP_LEARNING.addKeyword("pytorch", 0.9F);
    DEEP_LEARNING.addKeyword("reinforcement", 0.7F);
    DEEP_LEARNING.addKeyword("deep", 0.5F);
    DEEP_LEARNING.addKeyword("learning", 0.5F);
    DEEP_LEARNING.addKeyword("classifier", 0.8F);
    DEEP_LEARNING.addKeyword("torch", 0.8F);
    DEEP_LEARNING.addKeyword("theano", 0.8F);
    DEEP_LEARNING.addKeyword("tensorflow", 0.8F);
    DEEP_LEARNING.addKeyword("keras", 0.8F);
    DEEP_LEARNING.addKeyword("nolearn", 0.7F);
    DEEP_LEARNING.addKeyword("neural", 0.9F);
    DEEP_LEARNING.addKeyword("network", 0.7F);
    DEEP_LEARNING.addKeyword("ai", 0.8F);
    DEEP_LEARNING.addKeyword("vgg", 0.8F);
    DEEP_LEARNING.addKeyword("softmax", 0.8F);
    DEEP_LEARNING.addKeyword("node", 0.8F);
    DEEP_LEARNING.addKeyword("activation", 0.8F);
    DEEP_LEARNING.addKeyword("mnist", 0.8F);
    DEEP_LEARNING.addKeyword("lstm", 0.8F);
    DEEP_LEARNING.addKeyword("googlelenet", 0.7F);
    DEEP_LEARNING.addKeyword("droput", 0.8F);
    DEEP_LEARNING.addKeyword("caffe", 0.8F);
    DEEP_LEARNING.addKeyword("rnn", 0.9F);
    DEEP_LEARNING.addKeyword("backpropagation", 0.8F);
    DEEP_LEARNING.addKeyword("pooling", 0.8F);
    DEEP_LEARNING.addKeyword("autoencoder", 0.8F);
    DEEP_LEARNING.addKeyword("alexnet", 0.7F);
    DEEP_LEARNING.addKeyword("attention", 0.9F);
    DEEP_LEARNING.addKeyword("affine", 0.7F);
    DEEP_LEARNING.addKeyword("adam", 0.8F);
    DEEP_LEARNING.addKeyword("adadelta", 0.8F);
    DEEP_LEARNING.addKeyword("adagrad", 0.8F);
    DEEP_LEARNING.addKeyword("relu", 0.8F);
    DEEP_LEARNING.addKeyword("tanh", 0.8F);
    DEEP_LEARNING.addKeyword("sigmoid", 0.8F);
  }

  /**
   * Aggregate topic containing all presets.
   */
  public static Topic ALL = new Topic();

  static {
    // Populate with all preset topics

    ALL.setLongName("All Presets");
    ALL.setName("all");
    ALL.merge(SEMWEB).merge(WOODCARVING).merge(DEEP_LEARNING);
  }
}
