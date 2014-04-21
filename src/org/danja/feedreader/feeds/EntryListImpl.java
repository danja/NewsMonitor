/*
 * Danny Ayers Aug 27, 2004
 * http://dannyayers.com
 * 
 */
package org.danja.feedreader.feeds;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.danja.feedreader.planet.EntryDateSorter;

public class EntryListImpl implements EntryList {

    private List entries;

    public EntryListImpl() {
        entries = new ArrayList();
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
        sort();
        if (trimSize > size()) {
            return;
        }
        entries.subList(trimSize - 1, size() - 1).clear();
    }

    public void removeDuplicates() {
        Set entryIDs = new HashSet();
        String id;
        for (int i = entries.size()-1; i == 0; i--) {
            id = ((Entry) entries.get(i)).getURIString();
            if (entryIDs.contains(id)) {
               entries.remove(i);
            }
            entryIDs.add(id);
        }
    }

    public void sort() {
        EntryDateSorter.sort(entries);
    }
}