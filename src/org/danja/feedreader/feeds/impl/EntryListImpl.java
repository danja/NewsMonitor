/**
 * feedreader-prototype
 *
 * EntryListImpl.java
 * 
 * @author danja
 * @date Apr 25, 2014
 * 
 * TODO there's cruft here from pre-triplestore version
 *
 */
package org.danja.feedreader.feeds.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.danja.feedreader.content.Templater;
import org.danja.feedreader.feeds.Entry;
import org.danja.feedreader.feeds.EntryList;

/**
 *  implements EntryList, an ordered series of Entry objects
 */
public class EntryListImpl implements EntryList {

    private List<Entry> entries;

    public EntryListImpl() {
        entries = new ArrayList<Entry>();
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public int size() {
        return entries.size();
    }

    public Entry getEntry(int i) {
        return (Entry) entries.get(i);
    }

    // added for Poller
    public void trimList(int trimSize) {
        removeDuplicates();
        if (trimSize > size()) {
            return;
        }
        entries.subList(trimSize - 1, size() - 1).clear();
    }

    public void removeDuplicates() {
        Set<String> entryIDs = new HashSet<String>();
        String id;
        for (int i = entries.size()-1; i == 0; i--) {
            id = ((Entry) entries.get(i)).getUrl();
            if (entryIDs.contains(id)) {
               entries.remove(i);
            }
            entryIDs.add(id);
        }
    }

	@Override
	public String toTurtle() {
		if(entries.size() == 0) {
			return "# No entries.";
		}
		StringBuffer bodyBuffer = new StringBuffer();
		for(int i=0;i<entries.size();i++){
			bodyBuffer.append(entries.get(i).toString()+"\n\n");
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("body", bodyBuffer.toString());
		System.out.println(Templater.dataMapToString(data));
		return Templater.apply("turtle-prefixes", data);
	}
	
    public String toString() {
		StringBuffer bodyBuffer = new StringBuffer();
		for(int i=0;i<entries.size();i++){
			bodyBuffer.append(entries.get(i).toString()+"\n\n");
		}
        return bodyBuffer.toString();
    }

	@Override
	public void addAll(EntryList incoming) {
		entries.addAll(incoming.getEntries());
	}

	@Override
	public List<Entry> getEntries() {
		return entries;
	}
}