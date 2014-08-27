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

import it.danja.newsmonitor.main.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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
	public synchronized String query(String queryEndpoint, String sparql) {
		int statusCode = -1;
		String queryURL = null;
		String encoded = null;
		try {
			encoded = URLEncoder.encode(sparql, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
		encoded = encoded.replace("{", "%7B"); // {} are special to Jersey
		encoded = encoded.replace("}", "%7D");

		queryURL = queryEndpoint + "?query=" + encoded;
		// log.info("\n\n"+queryURL);

		// Eclipse warns about client not being closed, but it doesn't have a
		// close() method, hopefully release does the trick
	//	HttpClient client = new DefaultHttpClient();
//		HttpClient client = HttpClientBuilder.create().build();
//	    client.
	    // getCredentialsProvider().setCredentials(new AuthScope(host, AuthScope.ANY_PORT), new UsernamePasswordCredentials(USERNAME, PASSWORD));
	       
		CloseableHttpClient client = HttpClientBuilder.create().build();

		/*
		HttpHost targetHost = new HttpHost(Config.SPARQL_HOST, Config.SPARQL_PORT, Config.SPARQL_SCHEME);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
		        new AuthScope(targetHost.getHostName(), targetHost.getPort()),
		        new UsernamePasswordCredentials(Config.USERNAME, Config.PASSWORD));

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);

		// Add AuthCache to the execution context
		HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);
		context.setAuthCache(authCache);
		
		*/
		//
		HttpGet request = new HttpGet(queryURL);
		request.addHeader("Accept", "sparql-results+xml");
		HttpResponse response = null;
		try {
		//	response = client.execute(targetHost, request, context);
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		request.releaseConnection();

		 statusCode = response.getStatusLine().getStatusCode();
		 
	//	 throw new RuntimeException("\nCONTENT = "+statusCode+"\n");
		 
		 if(statusCode == 401) {
			 request.addHeader("Authorization", "Basic YWRtaW46YWRtaW4=");
			 request.addHeader("Cache-Control", "no-cache");
			 request.addHeader("Pragma", "no-cache");
			 request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			 request.addHeader("Accept-Encoding", "gzip,deflate,sdch");
			 request.addHeader("Accept-Language", "en-US,en;q=0.8");
			 request.addHeader("Connection", "keep-alive");
		
			 HttpHost targetHost = new HttpHost(Config.SPARQL_HOST, Config.SPARQL_PORT, Config.SPARQL_SCHEME);
			 
			 CredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(
				        new AuthScope(targetHost.getHostName(), targetHost.getPort()),
				        new UsernamePasswordCredentials(Config.USERNAME, Config.PASSWORD)
				        );
				HttpClientContext context = HttpClientContext.create();
				context.setCredentialsProvider(credsProvider);
				try {
					response = client.execute(targetHost, request, context);
					response = client.execute(request);
				} catch (ClientProtocolException e) {
					log.error(e.getMessage());
				} catch (IOException e) {
					log.error(e.getMessage());
				}
				request.releaseConnection();
		 }
		
		String headersString = new String();
		 Header[] headers = response.getAllHeaders();
		 for(int i =0;i<headers.length; i++){
		 headersString += "HEADER "+headers[i].getName()+" : "+headers[i].getValue()+"\n";
		 }
// throw new RuntimeException("\n"+Integer.toString(statusCode)+"\n"+headersString+"\n");

 // /*
//401
//HEADER Date : Tue, 26 Aug 2014 09:49:06 GMT
//HEADER WWW-Authenticate : Basic realm="Apache Stanbol authentication needed"
//HEADER Content-Length : 38
//HEADER Server : Jetty(8.1.14.v20131031)
		 
		 // Authorization:Basic YWRtaW46YWRtaW4=

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
	//	
		 
		 return content.toString();
	// */
	}

	/**
	 * @param updateEndpoint
	 * @param sparql
	 * @return
	 */
	public synchronized HttpMessage update(String updateEndpoint, String sparql) {

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
