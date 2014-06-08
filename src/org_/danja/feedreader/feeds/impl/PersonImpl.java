/**
 * feedreader-prototype
 *
 * PersonImpl.java
 * @author danja
 * @date Apr 27, 2014
 *
 */
package org.danja.feedreader.feeds.impl;

import java.util.HashMap;
import java.util.Map;

import org.danja.feedreader.feeds.Person;
import org.danja.feedreader.feeds.Templatable;

/**
 *
 */
public class PersonImpl implements Person, Templatable {

	private String name = null;
	private String email = null;
	private String homepage = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.danja.feedreader.feeds.Person#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.danja.feedreader.feeds.Person#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.danja.feedreader.feeds.Person#setEmail(java.lang.String)
	 */
	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.danja.feedreader.feeds.Person#getEmail()
	 */
	@Override
	public String getEmail() {
		return email;
	}
	
	public String toString(){
		return "[name="+getName()+", email="+getEmail()+", homepage="+homepage+"]";
	}

	@Override
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	@Override
	public String getHomepage() {
		return homepage;
	}
	
	@Override
	public Map<String, Object> getTemplateDataMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", this.name);
		map.put("email", this.email);
		map.put("homepage", this.homepage);
		return map;
	}
}
