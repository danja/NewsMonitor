/**
 * NewsMonitor
 *
 * SparqlResults.java
 * 
 * @author danja
 * @date Apr 21, 2014
 *
 */
package it.danja.newsmonitor.sparql;

import it.danja.newsmonitor.sparql.SparqlResults.Result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Model of SPARQL XML Results format
 * 
 * @link http://www.w3.org/TR/rdf-sparql-XMLres/
 */
public class SparqlResults {

	private Set variables = new HashSet<String>();
	// private List results = new ArrayList<Result>();
	private Results results = new Results();
	// private Set result = new HashSet<Binding>();

	static int URI_TYPE = 0;
	static int BNODE_TYPE = 1;
	static int LITERAL_TYPE = 2;

	static String[] types = { "uri", "bnode", "literal" };

	public List<Result> getResults() {
		return results;
	}

	public Set<String> getVariables() {
		return variables;
	}

	public String toString() {
	//	System.out.println("SparqlResults to string results.size() = "+results.size());
		return results.toString();
		// System.out.println("SIZE = "+Integer.toString(results.size()));
		// StringBuffer content = new StringBuffer();
		// Iterator iterator = results.iterator();
		// content.append("<results>");
		// for(int i=0;i<results.size();i++) {
		// content.append("\n   <result>");
		// content.append("\n      "+results.get(i));
		// content.append("\n   </result>");
		// }
		// content.append("</results>");
		// return content.toString();
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

	public void add(Result result) {
		results.add(result);
		// System.out.println("size = "+results.size());
	}

	public class Results extends ArrayList {
		public String toString() {
		//	System.out.println("Results toString size = "+size());
			StringBuffer content = new StringBuffer();
			for (int i = 0; i < size(); i++) {
		//		System.out.println("a" + get(i));
				content.append(get(i));
			}
			return content.toString();
		}
	}

	public class Result extends HashSet<Binding> {

		public String toString() {
		//	System.out.println("Result toString size = "+size());
			Iterator iterator = iterator();
			StringBuffer content = new StringBuffer();
			while (iterator.hasNext()) {
				content.append("\n<result>");
				content.append(iterator.next());
				content.append("\n</result>");
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
		//	System.out.println("Binding toString");
			return "\n      <binding name=\"" + getName() + "\">"
					+ "\n         <" + types[getType()] + ">" + getValue()
					+ "<" + types[getType()] + ">\n      </binding>";
		}
	}
}
