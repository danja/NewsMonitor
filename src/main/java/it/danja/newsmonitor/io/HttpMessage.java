/**
 * NewsMonitor
 *
 * HttpMessage.java
 * @author danja
 * @date May 29, 2014
 *
 */
package it.danja.newsmonitor.io;

/**
 * maybe tie to/replace with org.apache.http.HttpMessage
 */
public class HttpMessage {
	private int statusCode = -1;
	private String statusMessage;
	private String requestBody = "";
	
	/**
	 * @return the requestBody
	 */
	public String getRequestBody() {
		return requestBody;
	}
	/**
	 * @param requestBody the requestBody to set
	 */
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
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
	
	public String toString() {
		return this.statusCode + " : "+this.statusMessage + "\n" +this.requestBody;
	}
}
