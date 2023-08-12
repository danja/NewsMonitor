import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SparqlUpdateClient {
    public static void main(String[] args) {
        System.out.println("SparqlUpdateClient");
        try {
            // Define your SPARQL Update query
            String sparqlUpdate = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> INSERT DATA { <http://example/book1> rdf:type <http://example/Y> . }";

            // Set the endpoint URL for Fuseki
            String endpointUrl = "https://fuseki.hyperdata.it/newsmonitor/";

            // Create HttpClient instance
            HttpClient httpClient = HttpClients.createDefault();

            // Prepare HTTP POST request with the SPARQL update query
            HttpPost httpPost = new HttpPost(endpointUrl);
            httpPost.setHeader("Content-Type", "application/sparql-update");
            httpPost.setEntity(new StringEntity(sparqlUpdate));

            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);

            // Print the HTTP status code and response content
            System.out.println("Response Status Code: " + response.getStatusLine().getStatusCode());
            System.out.println("Response Content: " + EntityUtils.toString(response.getEntity()));

            // Release resources
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
