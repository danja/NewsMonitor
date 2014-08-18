/* tweaked version of ElementalHttpServer from
 * https://hc.apache.org/httpcomponents-core-ga/examples.html
 * 
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package it.danja.newsmonitor.tests.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Locale;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * Basic, yet fully functional and spec compliant, HTTP/1.1 file server.
 */
public class HttpServer {
	
	private static Logger log = LoggerFactory.getLogger(HttpServer.class);

	private String docRoot;
	private int port;
	private HttpService httpService;
	private SSLServerSocketFactory sf = null;
	private Thread serverThread = null;
	public static Socket socket;
	public static HttpServerConnection conn;
	public static WorkerThread workerThread;
	public static boolean running;
	
	
    public static void main(String[] args) throws Exception {
//        if (args.length < 1) {
//            System.err.println("Please specify document root directory");
//            System.exit(1);
//        }
//        // Document root directory
//        String docRoot = args[0];
    	
 //       int port = 8080;
//        if (args.length >= 2) {
//            port = Integer.parseInt(args[1]);
//        }
        HttpServer server = new HttpServer("src/main/resources/META-INF/resources/static/newsmonitor", 8088); // "www" "../NewsMonitor"
        server.init();
        server.start();
    }
    
    public HttpServer(String docRoot, int port) {
    	this.docRoot = docRoot;
    	this.port = port;
    }
    
    public void init() {

        // Set up the HTTP protocol processor
        HttpProcessor httpproc = HttpProcessorBuilder.create()
                .add(new ResponseDate())
                .add(new ResponseServer("Test/1.1"))
                .add(new ResponseContent())
                .add(new ResponseConnControl()).build();

        // Set up request handlers
        UriHttpRequestHandlerMapper reqistry = new UriHttpRequestHandlerMapper();
        reqistry.register("*", new HttpFileHandler(docRoot));

        // Set up the HTTP service
        httpService = new HttpService(httpproc, reqistry);
        
        if (port == 8443) {
            // Initialize SSL context
            ClassLoader cl = HttpServer.class.getClassLoader();
            URL url = cl.getResource("my.keystore");
            if (url == null) {
                log.info("HttpServer : Keystore not found");
                System.exit(1);
            }
            KeyStore keystore = null;
			try {
				keystore = KeyStore.getInstance("jks");
			} catch (KeyStoreException e) {

				e.printStackTrace();
			}
            try {
				keystore.load(url.openStream(), "secret".toCharArray());
			} catch (NoSuchAlgorithmException e) {

				e.printStackTrace();
			} catch (CertificateException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
            KeyManagerFactory kmfactory = null;
			try {
				kmfactory = KeyManagerFactory.getInstance(
				        KeyManagerFactory.getDefaultAlgorithm());
			} catch (NoSuchAlgorithmException e) {

				e.printStackTrace();
			}
            try {
				kmfactory.init(keystore, "secret".toCharArray());
			} catch (UnrecoverableKeyException e) {

				e.printStackTrace();
			} catch (KeyStoreException e) {

				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {

				e.printStackTrace();
			}
            KeyManager[] keymanagers = kmfactory.getKeyManagers();
            SSLContext sslcontext = null;
			try {
				sslcontext = SSLContext.getInstance("TLS");
			} catch (NoSuchAlgorithmException e) {

				e.printStackTrace();
			}
            try {
				sslcontext.init(keymanagers, null, null);
			} catch (KeyManagementException e) {

				e.printStackTrace();
			}
            this.sf = sslcontext.getServerSocketFactory();
        }
    }
    
    public void start(){
        
        if(this.serverThread == null) {
        	running = true;
		try {
			this.serverThread = new RequestListenerThread(this.port, this.httpService, this.sf);
		} catch (IOException e) {
		//	e.printStackTrace();
			return;
		}
        this.serverThread.setDaemon(false);
        this.serverThread.start();
        }
    }
    
    public void stop(){ // TODO can't get this to work... yucky workaround in start()
    	//log.info("STOP server");
    	running = false;
//    	try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//    	try {
//			socket.close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//    	try {
//			conn.close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}

    }

    static class HttpFileHandler implements HttpRequestHandler  {

        private final String docRoot;

        public HttpFileHandler(final String docRoot) {
            super();
            this.docRoot = docRoot;
        }

        public void handle(
                final HttpRequest request,
                final HttpResponse response,
                final HttpContext context) throws HttpException, IOException {

            String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
            if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
                throw new MethodNotSupportedException(method + " method not supported");
            }
            String target = request.getRequestLine().getUri();

            if (request instanceof HttpEntityEnclosingRequest) {
                HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                byte[] entityContent = EntityUtils.toByteArray(entity);
                log.info("HttpServer : Incoming entity content (bytes): " + entityContent.length);
            }

            final File file = new File(this.docRoot, URLDecoder.decode(target, "UTF-8"));
            
            if (!file.exists()) {

                response.setStatusCode(HttpStatus.SC_NOT_FOUND);
                StringEntity entity = new StringEntity(
                        "<html><body><h1>File" + file.getPath() +
                        " not found</h1></body></html>",
                        ContentType.create("text/html", "UTF-8"));
                response.setEntity(entity);
                log.info("HttpServer : File " + file.getPath() + " not found");

            } else if (!file.canRead() || file.isDirectory()) {

                response.setStatusCode(HttpStatus.SC_FORBIDDEN);
                StringEntity entity = new StringEntity(
                        "<html><body><h1>Access denied</h1></body></html>",
                        ContentType.create("text/html", "UTF-8"));
                response.setEntity(entity);
                log.info("HttpServer : Cannot read file " + file.getPath());

            } else {

                response.setStatusCode(HttpStatus.SC_OK);
                String mime = "text/html";
                if(target.endsWith(".css")){
                	mime = "text/css";
                }
                if(target.endsWith(".js")){
                	mime = "application/javascript";
                }
                if(target.endsWith(".png")){
                	mime = "image/png";
                }
                if(target.endsWith(".gif")){
                	mime = "image/gif";
                }
                // log.info("CONTENT_ENCODING");
                FileEntity body = new FileEntity(file, ContentType.create(mime, (Charset) null));
            
                // simulate latency
//                try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
                
                response.setEntity(body);
                if(target.endsWith(".html")){
                	response.addHeader("Content-Type", "text/html; charset=UTF-8");
                }
                log.info("HttpServer : Serving file " + file.getPath());
            }
        }

    }

    static class RequestListenerThread extends Thread {

        private final HttpConnectionFactory<DefaultBHttpServerConnection> connFactory;
        private final ServerSocket serversocket;
        private final HttpService httpService;

        public RequestListenerThread(
                final int port,
                final HttpService httpService,
                final SSLServerSocketFactory sf) throws IOException {
            this.connFactory = DefaultBHttpServerConnectionFactory.INSTANCE;
            this.serversocket = sf != null ? sf.createServerSocket(port) : new ServerSocket(port);
            this.httpService = httpService;
        }

        @Override
        public void run() {
            log.info("HttpServer : Listening on port " + this.serversocket.getLocalPort());
            while (running) {
                try {
                    // Set up HTTP connection
                    socket = this.serversocket.accept();
                    log.info("HttpServer : Incoming connection from " + socket.getInetAddress());
                    conn = this.connFactory.createConnection(socket);

                    // Start worker thread
                     workerThread = new WorkerThread(this.httpService, conn);
                    workerThread.setDaemon(true);
                    workerThread.start();
                } catch (InterruptedIOException ex) {
                    break;
                } catch (IOException e) {
                    System.err.println("I/O error initialising connection thread: "
                            + e.getMessage());
                    break;
                }
            }
        }
    }

    static class WorkerThread extends Thread {

        private final HttpService httpservice;
        private final HttpServerConnection conn;

        public WorkerThread(
                final HttpService httpservice,
                final HttpServerConnection conn) {
            super();
            this.httpservice = httpservice;
            this.conn = conn;
        }

        @Override
        public void run() {
            log.info("HttpServer : New connection thread");
            HttpContext context = new BasicHttpContext(null);
            try {
                while (running && !Thread.interrupted() && this.conn.isOpen()) {
                    this.httpservice.handleRequest(this.conn, context);
                }
            } catch (ConnectionClosedException ex) {
                System.err.println("HttpServer : Client closed connection");
            } catch (IOException ex) {
                System.err.println("HttpServer : I/O error: " + ex.getMessage());
            } catch (HttpException ex) {
                System.err.println("HttpServer : Unrecoverable HTTP protocol violation: " + ex.getMessage());
            } finally {
                try {
                    this.conn.shutdown();
                } catch (IOException ignore) {}
            }
        }

    }

}
