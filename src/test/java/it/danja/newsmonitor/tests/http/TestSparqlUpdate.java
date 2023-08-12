package it.danja.newsmonitor.tests.http;

import static org.junit.Assert.assertEquals;

import it.danja.newsmonitor.io.HttpMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestSparqlUpdate {

  // Define your SPARQL Update query
  String sparql =
    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> INSERT DATA { <http://example/book1> rdf:type <http://example/Z> . }";

  // Set the endpoint URL for Fuseki
  String updateEndpoint = "https://fuseki.hyperdata.it/newsmonitor/";

  String SPARQL_HOST = "fuseki.hyperdata.it";
  String SPARQL_PORT = "443";
  String SPARQL_SCHEME = "https";
  String USERNAME = "admin";
  String PASSWORD = "sasha";

  @Test
  public void testSparqlUpdateNoAuth() {
    System.out.println("SparqlUpdateClient");
    try {
      // Create HttpClient instance
      HttpClient httpClient = HttpClients.createDefault();

      // Prepare HTTP POST request with the SPARQL update query
      HttpPost httpPost = new HttpPost(updateEndpoint);
      httpPost.setHeader("Content-Type", "application/sparql-update");
      httpPost.setEntity(new StringEntity(sparql));

      // Execute the request
      HttpResponse response = httpClient.execute(httpPost);

      // Print the HTTP status code
      int statusCode = response.getStatusLine().getStatusCode();
      System.out.println("Response Status Code: " + statusCode);

      if (response.getEntity() != null) {
        System.out.println(
          "Response Content: " + EntityUtils.toString(response.getEntity())
        );
      }

      // Release resources
      httpClient.getConnectionManager().shutdown();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSparqlUpdateAuth() {
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
      SPARQL_HOST,
      Integer.parseInt(SPARQL_PORT),
      SPARQL_SCHEME
    );

    CredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(
      new AuthScope(targetHost.getHostName(), targetHost.getPort()),
      new UsernamePasswordCredentials(USERNAME, PASSWORD)
    );
    HttpClientContext context = HttpClientContext.create();
    context.setCredentialsProvider(credsProvider);

    List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    parameters.add(new BasicNameValuePair("update", sparql));
    try {
      request.setEntity(new UrlEncodedFormEntity(parameters));
    } catch (UnsupportedEncodingException e) {
      System.out.println(e.getMessage());
    }

    CloseableHttpResponse response = null;
    String statusMessage = "";

    try {
      // response = httpclient.execute(request);

      response = httpclient.execute(targetHost, request, context);

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
      System.out.println(e.getMessage());
    }
    try {
      httpclient.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    HttpMessage message = new HttpMessage(statusCode, statusMessage);
  }
}
