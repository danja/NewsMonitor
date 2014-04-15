package org.urss.parsers;


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