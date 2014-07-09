/**
 * feedreader-prototype
 *
 * SoupParser.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.newsmonitor.interpreters;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Basic tag soup/ill-formed XML/XHTML parser
 * 
 * FIXME
 * 
 * produces SAX2 callbacks mostly namespace-aware
 * 
 * ported from
 * https://web.archive.org/web/20050210065742/http://dannyayers.com/archives
 * /2004/07/11/tag-soup-play-code/ fills in missing end tags and is pretty
 * forgiving of other errors. It’s namespace-aware up to a point, if there are
 * nested changes to the default namespace it’ll get confused.
 * 
 */
public class SoupParser extends FeedParserBase {

	private static char NOT_TAG = 0;
	private static char START_TAG = 1;
	private static char END_TAG = 2;
	private char state = NOT_TAG;
	boolean firstInTag = false;

	private String data;
	private String qname;
	private AttributesImpl attributes;
	private int charIndex;
	private String defaultNS = "";
	private Map<String, String> prefixMap = new HashMap<String, String>();
	private Stack<String> openTags;
	private String block;
	private String cdata;
	// private ContentHandler getHandler();

	private static Pattern whitespaceRe = Pattern.compile("\\s+");
	private static Pattern commentRe = Pattern.compile("^<!--.+?-->",
			Pattern.DOTALL); // dotall
	private static Pattern piRe = Pattern.compile("^<\\?.+?\\?>",
			Pattern.DOTALL);
	private static Pattern cdataRe = Pattern.compile(
			"^<!\\[CDATA\\[.+?\\]\\]>", Pattern.DOTALL);
	// Python version is "\S+?\s*=\s*[\"|\'].+?[\"|\']"
	private static Pattern attributeSplitRe = Pattern
			.compile("\\S+?\\s*=\\s*[\"|\'].+?[\"|\']");

	// @Override
	// public void parse(InputStream inputStream) {
	// // TODO Auto-generated method stub
	//
	// }

	public void end(String qname) {
		String[] namePair = qnameSplit(qname);
		try {
			getHandler().endElement(namePair[0], namePair[1], qname);
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public void start(String tag) {
		this.qname = "";
		Matcher m = whitespaceRe.matcher(tag);
		tag = m.replaceAll(" "); // # normalise whitespace

		this.qname = tag.split(" ")[0]; // first chunk before space

		Matcher ma = attributeSplitRe.matcher(tag);
		this.attributes = new AttributesImpl();
		while (ma.find()) {
			this.extractAttribute(ma.group());
		}
		this.doName(qname);

		if (tag.charAt(tag.length() - 1) == '/') {// <something />
			this.end(qname);
		}
	}

	public void doName(String qname) {
		String[] namePair = this.qnameSplit(qname);
		try {
			this.getHandler().startElement(namePair[0], namePair[1], qname,
					attributes);
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public boolean isProcessingInstruction() {

		Matcher match = piRe.matcher(this.data.substring(this.charIndex));

		if (match.find()) {
			this.charIndex = this.charIndex + match.end() - 1;
			String raw = match.group();
			raw = raw.trim();
			int space = raw.indexOf(' ');
			String target = raw.substring(2, space);
			String data = raw.substring(space + 1, raw.length() - 2).trim();
			try {
				this.getHandler().processingInstruction(target, data);
			} catch (SAXException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public void comment(String raw) {

		String comment = raw.substring(4, raw.length() - 4);
		// System.out.println(comment);
//		if (getHandler() instanceof CommentHandler) {
//			((CommentHandler) getHandler()).comment(comment); // added for
//																// trackback
//																// handler
//		}
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

	public String isCDATA() {
		Matcher match = cdataRe.matcher(this.data.substring(this.charIndex));

		if (match.find()) {
			// int start = this.charIndex - 1; // ?
			this.charIndex = this.charIndex + match.end();
			this.cdata(match.group());
		//	String cdata = "<" + match.group();
			String cdata = match.group();
			return cdata;
		}
		return null;
	}

	public void cdata(String raw) {
		this.cdata = raw.substring(8, raw.length() - 3);
	}

	public boolean isComment() {
		Matcher match = commentRe.matcher(this.data.substring(this.charIndex));

		if (match.find()) {
			// System.out.println("Comment");
			this.charIndex = this.charIndex + match.end();
			// System.out.println(match.group());
			comment(match.group()); // / @@@@ NEEDED FOR COMMENT RECOGNITION
			return true;
		}
		return false;
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

	public void parseData(String data) {
		this.data = data;
		char[] dataChars = new char[data.length()];
		data.getChars(0, data.length(), dataChars, 0);

		this.openTags = new Stack<String>();
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
					// System.out.println("CDATA"+cdata);
					block = block + cdata;
					continue;
				}
				state = START_TAG;
				firstInTag = true;
				if (block != "") {
					try {
						getHandler().characters(dataChars,
								charIndex - block.length(), block.length());
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
					String lastOpen = openTags.pop();
					while (!lastOpen.equals(block)) {
						this.end(lastOpen);
						if (openTags.size() > 0) {
							lastOpen =  openTags.pop();
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
}
