error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6872.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6872.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6872.java
text:
```scala
r@@eturn formError("501 Method not implemented", "Service not implemented");

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

/**
 * Utility class to generate HTTP responses of various types.
 * 
 * @version $Revision$
 */
public final class HttpReplyHdr {
	/** String representing a carriage-return/line-feed pair. */
	private static final String CR = "\r\n";

	/** A HTTP protocol version string. */
	private static final String HTTP_PROTOCOL = "HTTP/1.0";

	/** The HTTP server name. */
	private static final String HTTP_SERVER = "Java Proxy Server";

	/**
	 * Don't allow instantiation of this utility class.
	 */
	private HttpReplyHdr() {
	}

	/**
	 * Forms a http ok reply header
	 * 
	 * @param contentType
	 *            the mime-type of the content
	 * @param contentLength
	 *            the length of the content
	 * @return a string with the header in it
	 */
	public static String formOk(String contentType, long contentLength) {
		StringBuffer out = new StringBuffer();

		out.append(HTTP_PROTOCOL).append(" 200 Ok").append(CR);
		out.append("Server: ").append(HTTP_SERVER).append(CR);
		out.append("MIME-version: 1.0").append(CR);

		if (0 < contentType.length()) {
			out.append("Content-Type: ").append(contentType).append(CR);
		} else {
			out.append("Content-Type: text/html").append(CR);
		}

		if (0 != contentLength) {
			out.append("Content-Length: ").append(contentLength).append(CR);
		}

		out.append(CR);

		return out.toString();
	}

	/**
	 * private! builds an http document describing a headers reason.
	 * 
	 * @param error
	 *            Error name.
	 * @param description
	 *            Errors description.
	 * @return A string with the HTML description body
	 */
	private static String formErrorBody(String error, String description) {
		StringBuffer out = new StringBuffer();
		// Generate Error Body
		out.append("<HTML><HEAD><TITLE>");
		out.append(error);
		out.append("</TITLE></HEAD>");
		out.append("<BODY><H2>").append(error).append("</H2>\n");
		out.append("</P></H3>");
		out.append(description);
		out.append("</BODY></HTML>");
		return out.toString();
	}

	/**
	 * builds an http document describing an error.
	 * 
	 * @param error
	 *            Error name.
	 * @param description
	 *            Errors description.
	 * @return A string with the HTML description body
	 */
	private static String formError(String error, String description) {
		/*
		 * A HTTP RESPONSE HEADER LOOKS ALOT LIKE:
		 * 
		 * HTTP/1.0 200 OK Date: Wednesday, 02-Feb-94 23:04:12 GMT Server:
		 * NCSA/1.1 MIME-version: 1.0 Last-modified: Monday, 15-Nov-93 23:33:16
		 * GMT Content-Type: text/html Content-Length: 2345 \r\n
		 */

		String body = formErrorBody(error, description);
		StringBuffer header = new StringBuffer();

		header.append(HTTP_PROTOCOL).append(" ").append(error).append(CR);
		header.append("Server: ").append(HTTP_SERVER).append(CR);
		header.append("MIME-version: 1.0").append(CR);
		header.append("Content-Type: text/html").append(CR);

		header.append("Content-Length: ").append(body.length()).append(CR);

		header.append(CR);
		header.append(body);

		return header.toString();
	}

	/**
	 * Indicates a new file was created.
	 * 
	 * @return The header in a string;
	 */
	public static String formCreated() {
		return formError("201 Created", "Object was created");
	}

	/**
	 * Indicates the document was accepted.
	 * 
	 * @return The header in a string;
	 */
	public static String formAccepted() {
		return formError("202 Accepted", "Object checked in");
	}

	/**
	 * Indicates only a partial responce was sent.
	 * 
	 * @return The header in a string;
	 */
	public static String formPartial() {
		return formError("203 Partial", "Only partail document available");
	}

	/**
	 * Indicates a requested URL has moved to a new address or name.
	 * 
	 * @return The header in a string;
	 */
	public static String formMoved() {
		// 300 codes tell client to do actions
		return formError("301 Moved", "File has moved");
	}

	/**
	 * Never seen this used.
	 * 
	 * @return The header in a string;
	 */
	public static String formFound() {
		return formError("302 Found", "Object was found");
	}

	/**
	 * The requested method is not implemented by the server.
	 * 
	 * @return The header in a string;
	 */
	public static String formMethod() {
		return formError("303 Method unseported", "Method unseported");
	}

	/**
	 * Indicates remote copy of the requested object is current.
	 * 
	 * @return The header in a string;
	 */
	public static String formNotModified() {
		return formError("304 Not modified", "Use local copy");
	}

	/**
	 * Client not otherized for the request.
	 * 
	 * @return The header in a string;
	 */
	public static String formUnautorized() {
		return formError("401 Unathorized", "Unathorized use of this service");
	}

	/**
	 * Payment is required for service.
	 * 
	 * @return The header in a string;
	 */
	public static String formPaymentNeeded() {
		return formError("402 Payment required", "Payment is required");
	}

	/**
	 * Client if forbidden to get the request service.
	 * 
	 * @return The header in a string;
	 */
	public static String formForbidden() {
		return formError("403 Forbidden", "You need permission for this service");
	}

	/**
	 * The requested object was not found.
	 * 
	 * @return The header in a string;
	 */
	public static String formNotFound() {
		return formError("404 Not_found", "Requested object was not found");
	}

	/**
	 * The server had a problem and could not fulfill the request.
	 * 
	 * @return The header in a string;
	 */
	public static String formInternalError() {
		return formError("500 Internal server error", "Server broke");
	}

	/**
	 * Server does not do the requested feature.
	 * 
	 * @return The header in a string;
	 */
	public static String formNotImplemented() {
		return formError("501 Method not implemented", "Service not implemented, programer was lazy");
	}

	/**
	 * Server is overloaded, client should try again latter.
	 * 
	 * @return The header in a string;
	 */
	public static String formOverloaded() {
		return formError("502 Server overloaded", "Try again latter");
	}

	/**
	 * Indicates the request took to long.
	 * 
	 * @return The header in a string;
	 */
	public static String formTimeout() {
		return formError("503 Gateway timeout", "The connection timed out");
	}

	/**
	 * Indicates the client's proxies could not locate a server.
	 * 
	 * @return The header in a string;
	 */
	public static String formServerNotFound() {
		return formError("503 Gateway timeout", "The requested server was not found");
	}

	/**
	 * Indicates the client is not allowed to access the object.
	 * 
	 * @return The header in a string;
	 */
	public static String formNotAllowed() {
		return formError("403 Access Denied", "Access is not allowed");
	}
}
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6872.java