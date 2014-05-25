package org.danja.feedreader.feeds;

public interface Link {

	/**
	 * @return the href
	 */
	public abstract String getHref();

	/**
	 * @param href the href to set
	 */
	public abstract void setHref(String href);

	/**
	 * @return the type
	 */
	public abstract String getType();

	/**
	 * @param type the type to set
	 */
	public abstract void setType(String type);

	/**
	 * @return the rel
	 */
	public abstract String getRel();

	/**
	 * @param rel the rel to set
	 */
	public abstract void setRel(String rel);

	/**
	 * @return the label
	 */
	public abstract String getLabel();

	/**
	 * @param label the label to set
	 */
	public abstract void setLabel(String label);

}