error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3192.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3192.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3192.java
text:
```scala
i@@f (httpsSpoof && SampleResult.TEXT.equals(result.getDataType()))

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jmeter.protocol.http.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerFactory;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Thread to handle one client request. Gets the request from the client and
 * passes it on to the server, then sends the response back to the client.
 * Information about the request and response is stored so it can be used in a
 * JMeter test plan.
 * 
 * @author mike
 * @version $Revision$ Last updated: $Date$ Created
 *          June 8, 2001
 */
public class Proxy extends Thread {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String NEW_LINE = "\n"; // $NON-NLS-1$

	/** Socket to client. */
	private Socket clientSocket = null;

	/** Target to receive the generated sampler. */
	private ProxyControl target;

	/** Whether or not to capture the HTTP headers. */
	private boolean captureHttpHeaders;

	/** Whether to try to spoof as https **/
	private boolean httpsSpoof;

	/**
	 * Default constructor - used by newInstance call in Daemon
	 */
	public Proxy() {
	}

	/**
	 * Create and configure a new Proxy object.
	 * 
	 * @param clientSocket
	 *            the socket connection to the client
	 * @param target
	 *            the ProxyControl which will receive the generated sampler
	 */
	Proxy(Socket clientSocket, ProxyControl target) {
		configure(clientSocket, target);
	}

	/**
	 * Configure the Proxy.
	 * 
	 * @param clientSocket
	 *            the socket connection to the client
	 * @param target
	 *            the ProxyControl which will receive the generated sampler
	 */
	void configure(Socket _clientSocket, ProxyControl _target) {
		this.target = _target;
		this.clientSocket = _clientSocket;
		this.captureHttpHeaders = _target.getCaptureHttpHeaders();
		this.httpsSpoof = target.getHttpsSpoof();
	}

	/**
	 * Main processing method for the Proxy object
	 */
	public void run() {
		HttpRequestHdr request = new HttpRequestHdr();
		SampleResult result = null;
		HeaderManager headers = null;

		HTTPSamplerBase sampler = null;
		try {
			request.parse(new BufferedInputStream(clientSocket.getInputStream()));

			sampler = request.getSampler();

			/*
			 * Create a Header Manager to ensure that the browsers headers are
			 * captured and sent to the server
			 */
			headers = request.getHeaderManager();
			sampler.setHeaderManager(headers);
			
			/* 
			 * If we are trying to spoof https, change the protocol
			 */
			if (httpsSpoof) {
				sampler.setProtocol("https");
			}
			result = sampler.sample();
			
			/*
			 * If we're dealing with text data, and if we're spoofing https, 
			 * replace all occurences of "https" with "http" for the client. 
			 */
			if (httpsSpoof && result.getDataType() == result.TEXT)
			{
				String noHttpsResult = new String(result.getResponseData());
				result.setResponseData(noHttpsResult.replaceAll("https", "http").getBytes());
			}
				
			writeToClient(result, new BufferedOutputStream(clientSocket.getOutputStream()));
			/*
			 * We don't want to store any cookies in the generated test plan
			 */
			headers.removeHeaderNamed("cookie");// Always remove cookies // $NON-NLS-1$
		} catch (UnknownHostException uhe) {
			log.warn("Server Not Found.", uhe);
			writeErrorToClient(HttpReplyHdr.formServerNotFound());
		} catch (Exception e) {
			log.error("", e);
			writeErrorToClient(HttpReplyHdr.formTimeout());
		} finally {
            if (sampler == null){
                sampler = HTTPSamplerFactory.newInstance();
            }
			target.deliverSampler(sampler, new TestElement[] { captureHttpHeaders ? headers : null }, result);
			try {
				clientSocket.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	/**
	 * Write output to the output stream, then flush and close the stream.
	 * 
	 * @param inBytes
	 *            the bytes to write
	 * @param out
	 *            the output stream to write to
	 * @throws IOException
	 *             if an IOException occurs while writing
	 */
	private void writeToClient(SampleResult res, OutputStream out) throws IOException {
		try {
			String responseHeaders = massageResponseHeaders(res, res.getResponseHeaders());
            out.write(responseHeaders.getBytes());
			out.write('\n'); // $NON-NLS-1$
			out.write(res.getResponseData());
			out.flush();
			log.debug("Done writing to client");
		} catch (IOException e) {
			log.error("", e);
			throw e;
		} finally {
			try {
				out.close();
			} catch (Exception ex) {
				log.warn("Error while closing socket", ex);
			}
		}
	}

	/**
	 * In the event the content was gzipped and unpacked, the content-encoding
	 * header must be removed and the content-length header should be corrected.
	 * 
	 * @param res
	 * @param headers
	 * @return
	 */
	private String massageResponseHeaders(SampleResult res, String headers) {
		int encodingHeaderLoc = headers.indexOf(": gzip"); // $NON-NLS-1$
		String newHeaders = headers;
		if (encodingHeaderLoc > -1) {
			int end = headers.indexOf(NEW_LINE, encodingHeaderLoc);
			int begin = headers.lastIndexOf(NEW_LINE, encodingHeaderLoc);
			newHeaders = newHeaders.substring(0, begin) + newHeaders.substring(end);
			int lengthIndex = newHeaders.indexOf("ength: "); // $NON-NLS-1$
			end = newHeaders.indexOf(NEW_LINE, lengthIndex);
			newHeaders = newHeaders.substring(0, lengthIndex + 7) + res.getResponseData().length
					+ newHeaders.substring(end);
		}
		return newHeaders;
	}

	/**
	 * Write an error message to the client. The message should be the full HTTP
	 * response.
	 * 
	 * @param message
	 *            the message to write
	 */
	private void writeErrorToClient(String message) {
		try {
			OutputStream sockOut = clientSocket.getOutputStream();
			DataOutputStream out = new DataOutputStream(sockOut);
			out.writeBytes(message);
			out.flush();
		} catch (Exception e) {
			log.warn("Exception while writing error", e);
		}
	}
}
 No newline at end of file
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3192.java