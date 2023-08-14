/**
 * NewsMonitor
 *
 * SparqlConnector.java
 *
 * @author danja
 * dc:date Apr 25, 2014
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
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
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

  private Properties config = null;

  public SparqlConnector(Properties config) {
    this.config = config;
  }

  /**
   * @param queryEndpoint
   * @param sparql
   * @return
   * // throws InterruptedException
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

    // getCredentialsProvider().setCredentials(new AuthScope(host,
    // AuthScope.ANY_PORT), new UsernamePasswordCredentials(USERNAME, PASSWORD));

    CloseableHttpClient client = HttpClientBuilder.create().build();

    // AuthCache authCache = new BasicAuthCache();

    HttpGet request = new HttpGet(queryURL);

    request.addHeader("Accept", "sparql-results+xml");
    HttpResponse response = null;
    try {
      // response = client.execute(targetHost, request, context);
      response = client.execute(request);
    } catch (ClientProtocolException e) {
      log.error(e.getMessage());
    } catch (IOException e) {
      log.error(e.getMessage());
    }

    try {
      TimeUnit.SECONDS.sleep(1); // 2023 ????
    } catch (InterruptedException e2) {
      // TODO Auto-generated catch block
      e2.printStackTrace();
    }
    // System.out.println("RESPONSE = " + response); // 2023

    statusCode = response.getStatusLine().getStatusCode();

    if (statusCode == 401) {
      request.addHeader("Authorization", "Basic YWRtaW46c2FzaGE="); // YWRtaW46c2FzaGE=  was  YWRtaW46YWRtaW4=
      request.addHeader("Cache-Control", "no-cache");
      request.addHeader("Pragma", "no-cache");
      request.addHeader(
        "Accept",
        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
      );
      request.addHeader("Accept-Encoding", "gzip,deflate,sdch");
      request.addHeader("Accept-Language", "en-US,en;q=0.8");
      request.addHeader("Connection", "keep-alive");

      HttpHost targetHost = new HttpHost(
        config.getProperty("SPARQL_HOST"),
        Integer.parseInt(config.getProperty("SPARQL_PORT")),
        config.getProperty("SPARQL_SCHEME")
      );

      CredentialsProvider credsProvider = new BasicCredentialsProvider();
      credsProvider.setCredentials(
        new AuthScope(targetHost.getHostName(), targetHost.getPort()),
        new UsernamePasswordCredentials(
          config.getProperty("USERNAME"),
          config.getProperty("PASSWORD")
        )
      );
      HttpClientContext context = HttpClientContext.create();
      context.setCredentialsProvider(credsProvider);

      try {
        response = client.execute(targetHost, request, context);
        // response = client.execute(request);
      } catch (ClientProtocolException e) {
        log.error(e.getMessage());
      } catch (IOException e) {
        log.error(e.getMessage());
      }
    }

    String headersString = new String();
    Header[] headers = response.getAllHeaders();
    for (int i = 0; i < headers.length; i++) {
      headersString +=
        "HEADER " + headers[i].getName() + " : " + headers[i].getValue() + "\n";
    }

    // Get the response
    InputStream inputStream = null;
    BufferedReader reader = null;
    try {
      inputStream = response.getEntity().getContent();
    } catch (IllegalStateException e1) {
      e1.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }

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
      // reader.close();
      // inputStream.close();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    //
    // System.out.println("CONTENT = "+content.toString());
    request.releaseConnection();
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

    HttpPost request = new HttpPost(updateEndpoint);

    request.addHeader("Authorization", "Basic YWRtaW46c2FzaGE="); // YWRtaW46c2FzaGE=  was  YWRtaW46YWRtaW4=
    request.addHeader("Cache-Control", "no-cache");
    request.addHeader("Pragma", "no-cache");
    request.addHeader(
      "Accept",
      "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
    );
    request.addHeader("Accept-Encoding", "gzip,deflate,sdch");
    request.addHeader("Accept-Language", "en-US,en;q=0.8");
    request.addHeader("Connection", "keep-alive");

    HttpHost targetHost = new HttpHost(
      config.getProperty("SPARQL_HOST"),
      Integer.parseInt(config.getProperty("SPARQL_PORT")),
      config.getProperty("SPARQL_SCHEME")
    );

    CredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(
      new AuthScope(targetHost.getHostName(), targetHost.getPort()),
      new UsernamePasswordCredentials(
        config.getProperty("USERNAME"),
        config.getProperty("PASSWORD")
      )
    );
    HttpClientContext context = HttpClientContext.create();
    context.setCredentialsProvider(credsProvider);

    List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    parameters.add(new BasicNameValuePair("update", sparql));
    try {
      request.setEntity(new UrlEncodedFormEntity(parameters));
    } catch (UnsupportedEncodingException e) {
      log.error(e.getMessage());
    }

    CloseableHttpResponse response = null;
    String statusMessage = "";

    try {
      response = httpclient.execute(request);

      //	response = httpclient.execute(targetHost, request, context);

      statusCode = response.getStatusLine().getStatusCode();
      statusMessage = response.getStatusLine().getReasonPhrase();

      // 502 Bad Gateway
      if ((statusCode < 200) || (statusCode > 299)) {
        System.out.println(
          "\nPOST response : " + statusCode + " " + statusMessage
        ); // 2023
        System.out.println("\n\nUpdateEndpoint = " + updateEndpoint);
        //		System.out.println("\nSPARQL = " + sparql);
        System.out.println("\nhttpPost = " + request.toString());
        // 2023
        // System.out.println("request : "+request);
        // System.out.println("User : "+config.getProperty("USERNAME"));
        // System.out.println("Pass : "+config.getProperty("PASSWORD"));
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    try {
      httpclient.close();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    HttpMessage message = new HttpMessage(statusCode, statusMessage);

    try { // 2023
      //		TimeUnit.SECONDS.sleep(5);
    } catch (Exception e) {
      log.error(e.getMessage());
    }

    return message;
  }
}
