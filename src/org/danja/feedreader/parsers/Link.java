/**
 * feedreader-prototype
 *
 * LinkImpl.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.parsers;


public class Link {
    public String rel = null;

    public String href = null;

    public String type = null;

    public String toString() {
        return "<link rel=\"" + rel + "\" href=\"" + href + "\" type=\""
                + type + "\"/>";
    }

    public boolean isAlternate() {
        if ((rel != null) && rel.equals("alternate") && (href != null) && (type != null)) {
            return true;
        }
        return false;
    }
}