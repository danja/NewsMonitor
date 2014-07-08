/**
 * feedreader-prototype
 *
 * LinkSetImpl.java
 * @author danja
 * @date Jul 8, 2014
 *
 */
package org.danja.feedreader.model.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.danja.feedreader.model.Link;
import org.danja.feedreader.model.LinkSet;

/**
 * A set of links, aware that those with matching hrefs may be treated as equivalent
 * TODO could just create an equals() method on Link ??
 */
public class LinkSetImpl implements LinkSet  {
	
	private Set<Link> links = Collections
			.newSetFromMap(new ConcurrentHashMap<Link, Boolean>());

	private Set<String> hrefs = Collections
			.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.impl.LinkSet#size()
	 */
	@Override
	public int size() {
		return links.size();
	}

	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.impl.LinkSet#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return links.isEmpty();
	}

	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.impl.LinkSet#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		return links.contains(o);
	}

	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.impl.LinkSet#iterator()
	 */
	@Override
	public Iterator<Link> iterator() {
		return links.iterator();
	}

	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.impl.LinkSet#toArray()
	 */
	@Override
	public Object[] toArray() {
		return links.toArray();
	}

	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.impl.LinkSet#toArray(Link[])
	 */
	@Override
	public <Link> Link[] toArray(Link[] a) {
		return links.toArray(a);
	}

	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.impl.LinkSet#addLink(org.danja.feedreader.model.Link)
	 */
	@Override
	public void addLink(Link link) {

	}
	
	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.impl.LinkSet#add(org.danja.feedreader.model.Link)
	 */
	@Override
	public boolean add(Link link) {
		boolean known = links.contains(link) || hrefs.contains(link.getHref());
		if (!known) {
			links.add(link);
			hrefs.add(link.getHref());
		}
		return !known;
	}

	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.impl.LinkSet#remove(org.danja.feedreader.model.Link)
	 */
	@Override
	public boolean remove(Link link) {
		boolean known = links.contains(link) || hrefs.contains(link.getHref());
		links.remove(link);
		hrefs.remove(link.getHref());
		return known;
	}



	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.impl.LinkSet#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<Link> links) {
		boolean changed = false;
		Iterator<Link> iterator = links.iterator();
		while (iterator.hasNext()) {
			if(add(iterator.next())) {
				changed = true;
			}
		}
		return changed;
	}

	/* (non-Javadoc)
	 * @see org.danja.feedreader.model.impl.LinkSet#clear()
	 */
	@Override
	public void clear() {
		links.clear();
		hrefs.clear();
		
	}

	@Override
	public Set<org.danja.feedreader.model.Link> getLinks() {
		return links;
	}

}
