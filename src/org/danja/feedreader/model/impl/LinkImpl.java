/**
 * feedreader-prototype
 *
 * LinkImpl.java
 * @author danja
 * @date May 25, 2014
 *
 */
package org.danja.feedreader.model.impl;

import java.util.HashMap;
import java.util.Map;

import org.danja.feedreader.model.Link;
import org.danja.feedreader.model.Templatable;

/**
 *
 */
public class LinkImpl implements Link, Templatable {

	private String type = null;
	private String rel = null;
	private String label = null;
	
	private String href = null;
	private boolean explored = false;
	private boolean remote = false;
	
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#getHref()
	 */
	@Override
	public String getHref() {
		return href;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#setHref(java.lang.String)
	 */
	@Override
	public void setHref(String href) {
		this.href = href;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#getType()
	 */
	@Override
	public String getType() {
		return type;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#setType(java.lang.String)
	 */
	@Override
	public void setType(String type) {
		this.type = type;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#getRel()
	 */
	@Override
	public String getRel() {
		return rel;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#setRel(java.lang.String)
	 */
	@Override
	public void setRel(String rel) {
		this.rel = rel;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#getLabel()
	 */
	@Override
	public String getLabel() {
		return label;
	}
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.Link#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public boolean explored() {
		return explored ;
	}
	@Override
	public void setExplored(boolean explored) {
		this.explored = explored;
	}
	
    public String toString() {
//        return "<link rel=\"" + rel + "\" href=\"" + href + "\" type=\""
//                + type + "\"/>";
		return "[href = "+href+", type = "+type+", rel = "+rel+", label = "+label+", explored = "+explored+"]\n";
    }

    
	@Override
	public Map<String, Object> getTemplateDataMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", this.href);
		map.put("label", this.label);
		map.put("rel", this.rel);
		map.put("type", this.type);
		map.put("explored", this.explored);
		return map;
	}
	
	@Override
	public boolean setRemote(boolean remote) {
		return this.remote  = remote;
	}
	@Override
	public boolean isRemote() {
		return remote;
	}
}
