/**
 * NewsMonitor
 *
 * PersonImpl.java
 * @author danja
 * dc:date Apr 27, 2014
 *
 */
package it.danja.newsmonitor.model.impl;

import it.danja.newsmonitor.model.Person;
import it.danja.newsmonitor.model.Templatable;

import java.util.HashMap;
import java.util.Map;

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
	 * @see it.danja.newsmonitor.model.Person#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.Person#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.Person#setEmail(java.lang.String)
	 */
	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.danja.newsmonitor.model.Person#getEmail()
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
