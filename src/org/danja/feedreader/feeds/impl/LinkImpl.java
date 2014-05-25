/**
 * feedreader-prototype
 *
 * LinkImpl.java
 * @author danja
 * @date May 25, 2014
 *
 */
package org.danja.feedreader.feeds.impl;

import org.danja.feedreader.feeds.Link;

/**
 *
 */
public class LinkImpl implements Link {

	private String href;
	/* (non-Javadoc)
	 * @see org.danja.feedreader.feeds.Link#getHref()
	 */
	@Override
	public String getHref() {
		return href;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.feeds.Link#setHref(java.lang.String)
	 */
	@Override
	public void setHref(String href) {
		this.href = href;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.feeds.Link#getType()
	 */
	@Override
	public String getType() {
		return type;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.feeds.Link#setType(java.lang.String)
	 */
	@Override
	public void setType(String type) {
		this.type = type;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.feeds.Link#getRel()
	 */
	@Override
	public String getRel() {
		return rel;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.feeds.Link#setRel(java.lang.String)
	 */
	@Override
	public void setRel(String rel) {
		this.rel = rel;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.feeds.Link#getLabel()
	 */
	@Override
	public String getLabel() {
		return label;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.feeds.Link#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {
		this.label = label;
	}
	private String type;
	private String rel;
	private String label;
}
