/**
 * feedreader-prototype
 *
 * HttpConnector.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package org.danja.feedreader.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.danja.feedreader.main.Config;

/**
 * HTTP connection handler
 * 
 * supports Conditional GET and gzip encoding
 * 
 * Note: uses java.net libs, not Apache
 */
public class HttpConnector {

	private boolean conditional = true;

	private URL url = null;

	private int responseCode = -1;

	private String eTag = null;

	private String previousETag = null;

	private String lastModified = null;

	private String previousLastModified = null;

	private String contentType = null;

	private String encoding = null;

	private InputStream inputStream = null;

	private boolean dead = false;

	private Map<String, List<String>> headers = null;

	static {
		HttpURLConnection.setFollowRedirects(true);
	}

	public HttpConnector() {

	}

	public void setUrl(String urlString) {
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Carries out everything needed to obtain an input stream if one is needed
	 * by the feed. Returns true on success Any problems, or the feed is already
	 * up-to-date returns false.
	 */
	public boolean load() {
		HttpURLConnection connection = null;
		responseCode = -1;
		dead = true;
		connection = connect();
		try {
			responseCode = connection.getResponseCode();
			System.out.println("Response Code : " + responseCode); // TODO
																	// handle
																	// response
																	// code here
		} catch (IOException e1) {
			e1.printStackTrace(); // TODO log error
			dead = false;
			return false;
		}
		if (responseCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
			connection.disconnect();
			dead = false;
			return false;
		}

		InputStream inputStream = null;
		try {
			inputStream = getInputStream(connection);
		} catch (IOException e2) {
			dead = true;
			e2.printStackTrace(); // TODO log error
			return false;
		}

		dead = false;
		return true;
	}

	public HttpURLConnection connect() {

		HttpURLConnection connection = null;
		try {
			// System.out.println("URL in HttpConnector = "+url);
			connection = (HttpURLConnection) url.openConnection();

		} catch (IOException e) {
			e.printStackTrace();
		}

		connection.setReadTimeout(Config.READ_TIMEOUT);
		connection.setConnectTimeout(Config.CONNECT_TIMEOUT);

		connection.setRequestProperty("Accept-Encoding", "gzip, deflate");

		if (conditional) {
			if (previousETag != null) {
				connection.addRequestProperty("If-None-Match", previousETag);
			}

			if (previousLastModified != null) {
				connection.addRequestProperty("If-Modified-Since",
						previousLastModified);
			}
		}
		try {
			connection.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		headers = connection.getHeaderFields();
		return connection;
	}

	public InputStream getInputStream(HttpURLConnection connection)
			throws IOException {

		lastModified = connection.getHeaderField("Last-Modified");
		previousLastModified = lastModified;
		eTag = connection.getHeaderField("ETag");
		previousETag = eTag;

		encoding = connection.getContentEncoding();
		contentType = connection.getContentType();

		if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
			inputStream = new GZIPInputStream(connection.getInputStream());
		} else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
			inputStream = new InflaterInputStream(connection.getInputStream(),
					new Inflater(true));
		} else {
			inputStream = connection.getInputStream();
		}
		return inputStream;
	}

	public void downloadToFile(String filename) {
		InputStream in = getInputStream();
		FileWriter out;
		int character;
		try {
			out = new FileWriter(new File(filename));
			while ((character = in.read()) != -1) {
				out.write(character);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isConditional() {
		return conditional;
	}

	public void setConditional(boolean conditional) {
		this.conditional = conditional;
	}

	public String getStatus() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n\nFeed:" + url);
		buffer.append("\nresponse code:" + getResponseCode());
		buffer.append("\nencoding:" + getContentEncoding());
		buffer.append("\ncontent-type:" + getContentType());
		buffer.append("\nlast-modified:" + getLastModified());
		buffer.append("\nisDead:" + isDead());
		return buffer.toString();
	}

	public void setPreviousETag(String previousETag) {
		this.previousETag = previousETag;
	}

	public void setPreviousLastModified(String previousLastModified) {
		this.previousLastModified = previousLastModified;
	}

	public String getETag() {
		return eTag;
	}

	public String getLastModified() {
		return lastModified;
	}

	public String getContentEncoding() {
		return encoding;
	}

	public String getContentType() {
		return contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public boolean isDead() {
		return dead;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public String getHeadersString() {
		StringBuffer buffer = new StringBuffer();

		Iterator<String> keyIterator = headers.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = keyIterator.next();
			List<String> values = headers.get(key);
			for (int i = 0; i < values.size(); i++) {
				buffer.append(key + ":" + values.get(i) + "\n");
			}
		}
		return buffer.toString();
	}

	public static void main(String[] args) {
		HttpConnector connector = new HttpConnector();
		connector.setUrl(args[0]);
		boolean isOk = connector.load();
		if (isOk) {
			connector.downloadToFile("C:/test.xml");
		}
		System.out.println(connector.getStatus());
	}
}