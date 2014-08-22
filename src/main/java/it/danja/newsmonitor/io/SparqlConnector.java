/**
 * NewsMonitor
 *
 * SparqlConnector.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for interacting with SPARQL store over HTTP
 * 
 * Note : uses org.apache.http libs
 * 
 * @author danja
 * 
 */
public class SparqlConnector {

	private static Logger log = LoggerFactory.getLogger(SparqlConnector.class);

	/**
	 * @param queryEndpoint
	 * @param sparql
	 * @return
	 */
	public static String query(String queryEndpoint, String sparql) {
		int statusCode = -1;
		String queryURL = null;
		try {
			queryURL = queryEndpoint + "?query="
					+ URLEncoder.encode(sparql, "UTF-8");
			// log.info("\n\n"+queryURL);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(queryURL);
		request.addHeader("Accept", "sparql-results+xml");
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		// Header[] headers = response.getAllHeaders();
		// for(int i =0;i<headers.length; i++){
		// log.info("HEADER "+headers[i].getName()+" : "+headers[i].getValue());
		// }

		// Get the response
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {
			inputStream = response.getEntity().getContent();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// XmlEncodingSniffer sniffer;
		// try {
		// sniffer = new XmlEncodingSniffer(inputStream, "UTF-8");
		// inputStream = sniffer.getStream();
		// } catch (UnsupportedEncodingException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		try {
			reader = new BufferedReader(new InputStreamReader(inputStream)); // response.getEntity().getContent()
		} catch (IllegalStateException e) {
			log.error(e.getMessage());
		}

		StringBuffer content = new StringBuffer();
		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return content.toString();
	}

	/**
	 * @param updateEndpoint
	 * @param sparql
	 * @return
	 */
	public static HttpMessage update(String updateEndpoint, String sparql) {

		int statusCode = -1;

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(updateEndpoint);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("update", sparql));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(parameters));
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
		CloseableHttpResponse response = null;
		String statusMessage = "";
		try {
			response = httpclient.execute(httpPost);
			statusCode = response.getStatusLine().getStatusCode();
			statusMessage = response.getStatusLine().getReasonPhrase();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		try {
			httpclient.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		HttpMessage message = new HttpMessage(statusCode, statusMessage);
		return message;
	}
}
