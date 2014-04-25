/**
 * feedreader-prototype
 *
 * SoupParser.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.danja.feedreader.feeds.FeedFetcher;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * 
 * Tag soup/ill-formed HTML/XML parser
 * 
 * produces SAX2 callbacks mostly namespace-aware (can't handle nested default
 * namespaces)
 * 
 * fairly direct port from Python version:
 * http://dannyayers.com/archives/2004/07/11/tag-soup-play-code/
 *  
 */
public class SoupParser implements FeedParser, ContentHandler {

    // Python version is "\S+?\s*=\s*[\"|\'].+?[\"|\']"
    private static Pattern attributeSplitRe = Pattern
            .compile("\\S+?\\s*=\\s*[\"|\'].+?[\"|\']");

    // Python version is "\s+"
    private static Pattern whitespaceRe = Pattern.compile("\\s+");

    // Python version is "^!--.+?-->", re.DOTALL
    private static Pattern commentRe = Pattern.compile("^<!--.+?-->",
            Pattern.DOTALL); // dotall

    // Python version is "^\?.+?\?>", re.DOTALL
    private static Pattern piRe = Pattern.compile("^<\\?.+?\\?>",
            Pattern.DOTALL);

    // Python version is "^!\[CDATA\[.+?\]\]>", re.DOTALL
    private static Pattern cdataRe = Pattern.compile("^!\\[CDATA\\[.+?\\]\\]>",
            Pattern.DOTALL);

    private static char NOT_TAG = 0;

    private static char START_TAG = 1;

    private static char END_TAG = 2;

    //  private static char CLOSING_TAG = 4;

    private char state = NOT_TAG;

    private ContentHandler contentHandler;

    //private Map attributes;
    private Map prefixMap;

    private String defaultNS = "";

    private String qname;

    private String name;

    private AttributesImpl attributes;

    private Stack openTags;

    private int charIndex;

    private String data;

    private String cdata;

    private String block;

    boolean firstInTag = false;

    private FeedFetcher feed;

    private boolean unescape;

    public SoupParser() {
        this.prefixMap = new HashMap();
        this.defaultNS = "";
        setContentHandler(this);
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    public String[] qnameSplit(String qname) {
        String[] namePair = new String[2];
        String[] prefixSplit = qname.split(":");
        if (prefixSplit.length == 1) {
            namePair[0] = this.defaultNS;
            namePair[1] = qname;
            return namePair;
        }

        String prefix = prefixSplit[0];
        String localname = prefixSplit[1];

        String namespace = (String) this.prefixMap.get(prefix);
        namePair[0] = namespace;
        namePair[1] = localname;
        return namePair;
    }

    public void end(String qname) {
        String[] namePair = qnameSplit(qname);
        try {
            contentHandler.endElement(namePair[0], namePair[1], qname);
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void start(String tag) {
        this.qname = "";
        Matcher m = whitespaceRe.matcher(tag);
        tag = m.replaceAll(" "); //# normalise whitespace

        this.qname = tag.split(" ")[0]; // first chunk before space

        Matcher ma = attributeSplitRe.matcher(tag);
        this.attributes = new AttributesImpl();
        while (ma.find()) {
            this.extractAttribute(ma.group());
        }
        this.doName(qname);

        if (tag.charAt(tag.length() - 1) == '/') {// ugly
            this.end(qname);
        }
    }

    public void doName(String qname) {
        String[] namePair = this.qnameSplit(qname);
        try {
            this.contentHandler.startElement(namePair[0], namePair[1], qname,
                    attributes);
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void extractAttribute(String rawAttribute) {

        int index = rawAttribute.indexOf("=");
        String qname = rawAttribute.substring(0, index);
        String value = rawAttribute.substring(index + 1);
        qname = qname.trim(); // remove spaces
        value = value.trim();
        value = value.substring(1, value.length() - 1); // remove quotes

        if (qname.split(":")[0] == "xmlns") {
            this.doNamespace(qname, value);
            return;
        }
        String[] namePair = this.qnameSplit(qname);
        attributes
                .addAttribute(namePair[0], namePair[1], qname, "CDATA", value);
    }

    public void doNamespace(String qname, String value) {
        String[] qnameParts = qname.split(":");
        if (qnameParts.length == 1) { // "xmlns"
            this.defaultNS = value;
            return;
        }
        // xmlns:spam="http://eggs.com"
        String prefix = qnameParts[1];
        this.prefixMap.put(prefix, value);
    }

    public boolean isProcessingInstruction() {
        /* added in exercises */
        Matcher match = piRe.matcher(this.data.substring(this.charIndex));

        if (match.find()) {
            this.charIndex = this.charIndex + match.end() - 1;
            String raw = match.group();
            raw = raw.trim();
            int space = raw.indexOf(' ');
            String target = raw.substring(2, space);
            String data = raw.substring(space + 1, raw.length() - 2).trim();
            try {
                this.contentHandler.processingInstruction(target, data);
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public boolean isComment() {
        Matcher match = commentRe.matcher(this.data.substring(this.charIndex));

        if (match.find()) {
            // System.out.println("Comment");
            this.charIndex = this.charIndex + match.end();
            //     System.out.println(match.group());
            comment(match.group()); /// @@@@ NEEDED FOR COMMENT RECOGNITION
            return true;
        }
        return false;
    }

    /*
     * 
     *  
     */

    public String isCDATA() {
        Matcher match = cdataRe.matcher(this.data.substring(this.charIndex));

        if (match.find()) {
            int start = this.charIndex - 1; //?
            this.charIndex = this.charIndex + match.end();
            this.cdata(match.group());
            String cdata = "<" + match.group();
            return cdata;
        }
        return null;
    }

    /* added in exercises - is non-standard */
    public void comment(String raw) {

        String comment = raw.substring(4, raw.length() - 4);
     //   System.out.println(comment);
        if (contentHandler instanceof CommentHandler) {
            ((CommentHandler) contentHandler).comment(comment); // added for
                                                                // trackback
                                                                // handler
        }
    }

    public void cdata(String raw) {
        this.cdata = raw.substring(8, raw.length() - 3);
    }

    public void parseData(String data) {

        if (unescape) { // must be a utility somewhere for this
            if ((data.length() > 12) && data.startsWith("<![CDATA[")) { // ugh!!
                data = data.substring(8, data.length() - 3);
            } else {
                data.replaceAll("&amp;", "&");
                data.replaceAll("&lt;", "<");
                data.replaceAll("&gt;", ">");
            }
            //System.out.println(data);
        }

        this.data = data;
        char[] dataChars = new char[data.length()];
        data.getChars(0, data.length(), dataChars, 0);

        this.openTags = new Stack();
        block = "";
        char character;
        for (charIndex = 0; charIndex < dataChars.length; charIndex++) {
            character = dataChars[charIndex];
            if (character == '<') {
                if (this.isProcessingInstruction()) {
                    continue;
                }
                if (this.isComment()) { 
                    continue;
                }
                cdata = this.isCDATA();
                if (cdata != null) {
                    //	System.out.println("CDATA"+cdata);
                    block = block + cdata;
                    continue;
                }
                state = START_TAG;
                firstInTag = true;
                if (block != "") {
                    try {
                        contentHandler.characters(dataChars, charIndex
                                - block.length(), block.length());
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }
                }
                block = "";
                continue;
            }
            if (character == '/') {
                if (firstInTag) {
                    state = END_TAG;
                    firstInTag = false;
                    continue;
                }
            }
            if (character == '>') {
                if (state == START_TAG) {
                    this.start(block);
                    openTags.push(this.qname);
                }
                // System.out.println("*******");
                if (state == END_TAG) {
                    if (openTags.size() == 0) {
                        continue;
                    }
                    String lastOpen = (String) openTags.pop();
                    while (!lastOpen.equals(block)) {
                        this.end(lastOpen);
                        if (openTags.size() > 0) {
                            lastOpen = (String) openTags.pop();
                        } else {
                            break;
                        }

                    }
                    this.end(block);
                }
                state = NOT_TAG;
                block = "";
                continue;
            }
            block = block + character;
            firstInTag = false;
        }
        while (openTags.size() > 0) {
            this.end((String) openTags.pop());
        }
    }

    /***************************************************************************
     * ContentHandler Methods
     */

    public void startDocument() throws SAXException {
        System.out.println("Start Document");
    }

    public void endDocument() throws SAXException {
        System.out.println("End Document");
    }

    public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {
        System.out.println("\nstartElement- \nns:" + namespaceURI + "\nname:"
                + localName + "\nqname:" + qName + "\natts:");
        for (int i = 0; i < atts.getLength(); i++) {
            System.out.println("   " + atts.getLocalName(i) + " "
                    + atts.getValue(i));
        }

    }

    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        System.out.println("\nendElement- \nns:" + namespaceURI + "\nname:"
                + localName + "\nqname:" + qName);

    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        StringBuffer buffer = new StringBuffer();

        buffer.append(ch, start, length);
        System.out.println("characters: " + buffer);
    }

    public void processingInstruction(String target, String data)
            throws SAXException {
        System.out.println("PI target = '" + target + "'");
        System.out.println("data = '" + data + "'");
    }

    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        // not implemented
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        // not implemented
    }

    public void skippedEntity(String name) throws SAXException {
        // not implemented
    }

    public void setDocumentLocator(Locator locator) {
        // not implemented
    }

    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        // not implemented
    }

    public void parse(InputStream inputStream) {
        StringBuffer buffer = new StringBuffer();
        int character;
        try {
            while ((character = inputStream.read()) != -1) {
                buffer.append((char) character);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseData(buffer.toString());
    }

    public void setFeed(FeedFetcher feed) {
        this.feed = feed;

    }

    public void setUnescape(boolean unescape) {
        this.unescape = unescape;
    }

    public static void main(String[] args) {
        File inputFile = new File(args[0]);
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream in = new FileInputStream(inputFile);
            SoupParser parser = new SoupParser();
            parser.parse(in);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}