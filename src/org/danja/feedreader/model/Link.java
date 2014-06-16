package org.danja.feedreader.model;

import java.util.Map;

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
	public  String getContentType();

	/**
	 * @param type the type to set
	 */
	public  void setContentType(String contentType);

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

	public void setFormat(String format);
	
	public String getFormat();

	public Map<String, Object> getTemplateDataMap();

	// public void setType(char type); too confusing, do it longhand in the callers
}