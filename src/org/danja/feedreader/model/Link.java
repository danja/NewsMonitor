package org.danja.feedreader.model;

public interface Link {

	public boolean isExplored();
	
	public void setExplored(boolean explored);
	/**
	 * @return the href
	 */
	public  String getHref();

	/**
	 * @param href the href to set
	 */
	public  void setHref(String href);

	/**
	 * @return the type
	 */
	public  String getType();

	/**
	 * @param type the type to set
	 */
	public  void setType(String type);

	/**
	 * @return the rel
	 */
	public  String getRel();

	/**
	 * @param rel the rel to set
	 */
	public  void setRel(String rel);

	/**
	 * @return the label
	 */
	public  String getLabel();

	/**
	 * @param label the label to set
	 */
	public  void setLabel(String label);

	public boolean setRemote(boolean remote);
	
	public boolean isRemote();

	public void setRelevance(float f);
	
	public float getRelevance();
}