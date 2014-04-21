package org.danja.feedreader.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
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

/**
 * Utility for interacting with SPARQL store over HTTP
 * 
 * Note : uses org.apache.http libs
 * 
 * @author danja
 * 
 */
public class SparqlConnector {

	/**
	 * @param queryEndpoint
	 * @param sparql
	 * @return
	 */
	public static String query(String queryEndpoint, String sparql) {
		int statusCode = -1;
		String queryURL = null;
		try {
			queryURL = queryEndpoint+"?query="+ URLEncoder.encode(sparql, "UTF-8");
			System.out.println("\n\n"+queryURL);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(queryURL);
		request.addHeader("Accept", "sparql-results+xml");
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Get the response
		BufferedReader reader = null;
		try {
			reader = new BufferedReader
			  (new InputStreamReader(response.getEntity().getContent()));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		    
		StringBuffer content = new StringBuffer(); 
		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return content.toString();
	}
	
	/**
	 * @param updateEndpoint
	 * @param sparql
	 * @return
	 */
	public static int update(String updateEndpoint, String sparql) {

		int statusCode = -1;

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(updateEndpoint);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("update", sparql));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(parameters));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			statusCode = response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return statusCode;
	}
}
