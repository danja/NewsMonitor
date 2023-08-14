/**
 * NewsMonitor
 *
 * LinkSetImpl.java
 * @author danja
 * dc:date Jul 8, 2014
 *
 */
package it.danja.newsmonitor.model.impl;

import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.LinkSet;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A set of links, aware that those with matching hrefs may be treated as equivalent
 * TODO the equals()/hashCode() in LinkImpl should be taking care of this, why isn't it?
 */
public class LinkSetImpl implements LinkSet  {
	
	private Set<Link> links = Collections
			.newSetFromMap(new ConcurrentHashMap<Link, Boolean>());

	private Set<String> hrefs = Collections
			.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

	/* (non-Javadoc)
	 * @see it.danja.newsmonitor.model.impl.LinkSet#size()
	 */
	@Override
	public int size() {
		return links.size();
	}

	/* (non-Javadoc)
	 * @see it.danja.newsmonitor.model.impl.LinkSet#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return links.isEmpty();
	}

	/* (non-Javadoc)
	 * @see it.danja.newsmonitor.model.impl.LinkSet#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		return links.contains(o);
	}

	/* (non-Javadoc)
	 * @see it.danja.newsmonitor.model.impl.LinkSet#iterator()
	 */
	@Override
	public Iterator<Link> iterator() {
		return links.iterator();
	}

	/* (non-Javadoc)
	 * @see it.danja.newsmonitor.model.impl.LinkSet#toArray()
	 */
	@Override
	public Object[] toArray() {
		return links.toArray();
	}

	/* (non-Javadoc)
	 * @see it.danja.newsmonitor.model.impl.LinkSet#toArray(Link[])
	 */
	@Override
	public <Link> Link[] toArray(Link[] a) {
		return links.toArray(a);
	}

	/* (non-Javadoc)
	 * @see it.danja.newsmonitor.model.impl.LinkSet#addLink(it.danja.newsmonitor.model.Link)
	 */
	@Override
	public void addLink(Link link) {

	}
	
	/* (non-Javadoc)
	 * @see it.danja.newsmonitor.model.impl.LinkSet#add(it.danja.newsmonitor.model.Link)
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
	 * @see it.danja.newsmonitor.model.impl.LinkSet#remove(it.danja.newsmonitor.model.Link)
	 */
	@Override
	public boolean remove(Link link) {
		boolean known = links.contains(link) || hrefs.contains(link.getHref());
		links.remove(link);
		hrefs.remove(link.getHref());
		return known;
	}



	/* (non-Javadoc)
	 * @see it.danja.newsmonitor.model.impl.LinkSet#addAll(java.util.Collection)
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
	 * @see it.danja.newsmonitor.model.impl.LinkSet#clear()
	 */
	@Override
	public void clear() {
		links.clear();
		hrefs.clear();
		
	}

	@Override
	public Set<it.danja.newsmonitor.model.Link> getLinks() {
		return links;
	}

}
