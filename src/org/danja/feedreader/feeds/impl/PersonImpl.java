/**
 * feedreader-prototype
 *
 * PersonImpl.java
 * @author danja
 * @date Apr 27, 2014
 *
 */
package org.danja.feedreader.feeds.impl;

import org.danja.feedreader.feeds.Person;

/**
 *
 */
public class PersonImpl implements Person {

	private String name = "";
	private String email = "";

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
		return "Person : name="+getName()+", email="+getEmail();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
