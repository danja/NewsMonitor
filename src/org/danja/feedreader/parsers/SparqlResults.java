/**
 * feedreader-prototype
 *
 * SparqlResults.java
 * @author danja
 * @date Apr 21, 2014
 *
 */
package org.danja.feedreader.parsers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 *
 */
public class SparqlResults {

	private Set variables = new HashSet<String>();
	private List results = new ArrayList<Result>();
	// private Set result = new HashSet<Binding>();

	static int URI_TYPE = 0;
	static int BNODE_TYPE = 1;
	static int LITERAL_TYPE = 2;

	static String[] types = { "uri", "bnode", "literal" };
	
	public String toString() {
//		StringBuffer content = new StringBuffer();
//		Iterator iterator = result.iterator();
//		content.append("<results>");
//		for(int i=0;i<result.size();i++) {
//			content.append("   <result>");
//			content.append("      "+result.get(i));
//			content.append("   </result>");
//		}
//		content.append("</results>");
//		return content.toString();
		return "SIZE = "+Integer.toString(results.size());
	}

	public Result createResult() {
		return new Result();
	}

	public Binding createBinding() {
		return new Binding();
	}

	public void addVariable(String text) {
		variables.add(text);
	}

	class Result extends HashSet {

		public String toString() {
			Iterator iterator = iterator();
			StringBuffer content = new StringBuffer();
			while (iterator.hasNext()) {
				content.append(iterator.next());
			}
			return content.toString();
		}
	}
	
	public class Binding {
		private String name = null;
		private String value = null;
		private int type = 0;

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value
		 *            the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * @return the type
		 */
		public int getType() {
			return type;
		}

		/**
		 * @param type
		 *            the type to set
		 */
		public void setType(int type) {
			this.type = type;
		}

		/*
		 * <binding name="hpage"> <uri>http://work.example.org/bob/</uri>
		 * </binding>
		 */

		public String toString() {
			return "      <binding name=\"" + getName() + "\">"
					+ "         <" + types[getType()] + ">" + getValue()
					+ "<" + types[getType()] + ">" + "      <binding>";
		}
	}
}
