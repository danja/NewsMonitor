/**
 * feedreader-prototype
 *
 * HttpMessage.java
 * @author danja
 * @date May 29, 2014
 *
 */
package org.danja.feedreader.io;

/**
 * maybe tie to org.apache.http.HttpMessage
 */
public class HttpMessage {
	private int statusCode = -1;
	private String statusMessage;
	
	public HttpMessage(int statusCode, String statusMessage) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
	}
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}
	/**
	 * @param statusMessage the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
}
