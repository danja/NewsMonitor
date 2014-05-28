/**
 * feedreader-prototype
 *
 * LinkImpl.java
 * @author danja
 * @date May 25, 2014
 *
 */
package org.danja.feedreader.feeds.impl;

import java.util.HashMap;
import java.util.Map;

import org.danja.feedreader.feeds.Link;
import org.danja.feedreader.feeds.Templatable;

/**
 *
 */
public class LinkImpl implements Link, Templatable {

	private String type;
	private String rel;
	private String label;
	
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

	
    public String toString() {
//        return "<link rel=\"" + rel + "\" href=\"" + href + "\" type=\""
//                + type + "\"/>";
		return "[href = "+href+", type = "+type+", rel = "+rel+", label = "+label+"]\n";

    }

    public boolean isHtmlAlternate() {
        if ((rel != null) && rel.equals("alternate")) {
        	if(type == null || "text/html".equals(type)){
            return true;
        	}
        }
        return false;
    }
    
	@Override
	public Map<String, Object> getTemplateDataMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", this.href);
		map.put("label", this.label);
		map.put("rel", this.rel);
		map.put("type", this.type);
		return map;
	}
}
