package it.danja.newsmonitor.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * a collection of Links
 */
public interface LinkSet {
	
	public Set<Link> getLinks();

	public int size();

	public boolean isEmpty();

	public boolean contains(Object o);

	public Iterator<Link> iterator();

	public Object[] toArray();

	public <Link> Link[] toArray(Link[] a);

	public void addLink(Link link);

	public boolean add(Link link);

	public boolean remove(Link link);

	public boolean addAll(Collection<Link> links);

	public void clear();

}