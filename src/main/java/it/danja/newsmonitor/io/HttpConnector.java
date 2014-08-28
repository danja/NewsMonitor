/**
 * NewsMonitor
 *
 * HttpConnector.java
 * 
 * @author danja
 * @date Apr 25, 2014
 *
 */
package it.danja.newsmonitor.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.utils.XmlEncodingSniffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * HTTP connection handler
 * 
 * supports Conditional GET and gzip encoding
 * 
 * Note: uses java.net libs, not Apache
 */
public class HttpConnector {

	private static Logger log = LoggerFactory.getLogger(HttpConnector.class);
	
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

	private String userAgentHeader = null;
	private String acceptHeader = null;

	private Properties config = null;

	static {
		HttpURLConnection.setFollowRedirects(true);
	}

	public HttpConnector(Properties config) {
this.config  = config;
	}

	public void setUrl(String urlString) {
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Carries out everything needed to obtain an input stream for the feed. Returns true on success Any problems, or the feed is already
	 * up-to-date returns false.
	 */
	public boolean load() {
		HttpURLConnection connection = null;
		responseCode = -1;
		dead = true;
		connection = connect();
		try {
			responseCode = connection.getResponseCode();
			log.info(url+" response Code : " + responseCode); // TODO
																	// handle
																	// response
																	// code here
		char codeBlock = Integer.toString(responseCode).charAt(0);
		if(codeBlock == 4 || codeBlock == 5) {
			log.info("four or FIVE");
			return false;
		}
			
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
			// e2.printStackTrace(); // TODO log error
			return false;
		}

		dead = false;
		return true;
	}

	public HttpURLConnection connect() {
		return connect("GET");
	}
	
	public HttpURLConnection connect(String method) {

		HttpURLConnection connection = null;
		try {
			// log.info("URL in HttpConnector = "+url);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		connection.setReadTimeout(Integer.parseInt(config.getProperty("READ_TIMEOUT")));
		connection.setConnectTimeout(Integer.parseInt(config.getProperty("CONNECT_TIMEOUT")));

		connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
		if(userAgentHeader  != null) {
		connection.setRequestProperty("User-Agent", userAgentHeader);
		}
		if(acceptHeader  != null) {
		connection.setRequestProperty("Accept", acceptHeader);
		}

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
			// log.error(e.getMessage());
			log.error(e.getMessage());
		}
		headers = connection.getHeaderFields();
		return connection;
	}
	
	public String downloadAsString(String urlString) {
		setUrl(urlString);
		setConditional(false);
		if(!load()) {
			return null;
		}
		Reader inputStreamReader = new InputStreamReader(inputStream);
    	BufferedReader in = new BufferedReader(inputStreamReader);
    	StringBuffer buffer = new StringBuffer();
    	String readLine;
    	try {
			while ((readLine = in.readLine()) != null) {
			    buffer.append(readLine);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	String data = buffer.toString();
    	try {
			in.close();
		} catch (IOException e2) {
			return null;
		}
    	return data;
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
			log.error(e.getMessage());
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
		if(contentType == null) {
			doHeadRequest();
		}
		List<String> list = getHeaders().get("Content-Type");
		if(list == null || list.size() == 0) {
			return null;
		}
		contentType = getHeaders().get("Content-Type").get(0);
		return contentType;
	}

	private void doHeadRequest() {
		connect("HEAD");
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

//	public static void main(String[] args) {
//		HttpConnector connector = new HttpConnector(null);
//		connector.setUrl(args[0]);
//		boolean isOk = connector.load();
//		if (isOk) {
//			connector.downloadToFile("C:/test.xml");
//		}
//		log.info(connector.getStatus());
//	}

	/**
	 * @param userAgentHeader the userAgentHeader to set
	 */
	public void setUserAgentHeader(String userAgentHeader) {
		this.userAgentHeader = userAgentHeader;
	}

	/**
	 * @param acceptHeader the acceptHeader to set
	 */
	public void setAcceptHeader(String acceptHeader) {
		this.acceptHeader = acceptHeader;
	}
}