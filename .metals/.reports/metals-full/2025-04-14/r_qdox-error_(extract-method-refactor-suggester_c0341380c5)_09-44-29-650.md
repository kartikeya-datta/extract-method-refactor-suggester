error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4488.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4488.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4488.java
text:
```scala
private static final S@@tring serviceUrl = "http://www.webservicex.net/stockquote.asmx";

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.examples.stockquote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Provides access to a SOAP service for getting stock quotes based on a symbol. Found on
 * http://www.devx.com/Java/Article/27559/0/page/2
 */
public class StockQuote
{
	/**
	 * We used to use the www.xmethods.com demo webservice for stockquotes. We now use webservicex,
	 * as xmethods was really overloaded and unreliable.
	 */
	private static final String serviceUrl = "http://www.webservicex.net/stockquote.asmx?op=GetQuote";

	/** the symbol to get the quote for. */
	private String symbol;

	/**
	 * Default constructor.
	 */
	public StockQuote()
	{
	}

	/**
	 * Constructor setting the symbol to get the quote for.
	 * 
	 * @param symbol
	 *            the symbol to look up
	 */
	public StockQuote(String symbol)
	{
		this.symbol = symbol;
	}

	/**
	 * Gets the symbol.
	 * 
	 * @return the symbol
	 */
	public String getSymbol()
	{
		return symbol;
	}

	/**
	 * Sets the symbol for getting the quote.
	 * 
	 * @param symbol
	 */
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	/**
	 * Gets a stock quote for the given symbol
	 * 
	 * @return the stock quote
	 */
	public String getQuote()
	{
		final String response = getSOAPQuote(symbol);

		// make sure we get
		int start = response.indexOf("&lt;Last&gt;") + "&lt;Last&gt;".length();
		int end = response.indexOf("&lt;/Last&gt;");

		// if the string returned isn't valid, just return empty.
		if (start < "&lt;Last&gt;".length())
		{
			return "(unknown)";
		}
		String result = response.substring(start, end);
		return result.equals("0.00") ? "(unknown)" : result;
	}

	/**
	 * Calls the SOAP service to get the stock quote for the symbol.
	 * 
	 * @param symbol
	 *            the name to search for
	 * @return the SOAP response containing the stockquote
	 */
	private String getSOAPQuote(String symbol)
	{
		String response = "";

		try
		{
			final URL url = new URL(serviceUrl);
			final String message = createMessage(symbol);

			// Create the connection where we're going to send the file.
			HttpURLConnection httpConn = setUpHttpConnection(url, message.length());

			// Everything's set up; send the XML that was read in to
			// the service.
			writeRequest(message, httpConn);

			// Read the response and write it to standard out.
			response = readResult(httpConn);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * Writes the message to the connection.
	 * 
	 * @param message
	 *            the message to write
	 * @param httpConn
	 *            the connection
	 * @throws IOException
	 */
	private void writeRequest(String message, HttpURLConnection httpConn) throws IOException
	{
		OutputStream out = httpConn.getOutputStream();
		out.write(message.getBytes());
		out.close();
	}

	/**
	 * Sets up the HTTP connection.
	 * 
	 * @param url
	 *            the url to connect to
	 * @param length
	 *            the length to the input message
	 * @return the HttpurLConnection
	 * @throws IOException
	 * @throws ProtocolException
	 */
	private HttpURLConnection setUpHttpConnection(URL url, int length) throws IOException,
		ProtocolException
	{
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection)connection;
		// Set the appropriate HTTP parameters.
		httpConn.setRequestProperty("Content-Length", String.valueOf(length));
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpConn.setRequestProperty("SOAPAction", "\"http://www.webserviceX.NET/GetQuote\"");
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		return httpConn;
	}

	/**
	 * Reads the response from the http connection.
	 * 
	 * @param connection
	 *            the connection to read the response from
	 * @return the response
	 * @throws IOException
	 */
	private String readResult(HttpURLConnection connection) throws IOException
	{
		InputStream inputStream = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader in = new BufferedReader(isr);

		StringBuffer sb = new StringBuffer();
		String inputLine;
		while ((inputLine = in.readLine()) != null)
		{
			sb.append(inputLine);
		}

		in.close();
		return sb.toString();
	}

	/**
	 * Creates the request message for retrieving a stock quote.
	 * 
	 * @param symbol
	 *            the symbol to query for
	 * @return the request message
	 */
	private String createMessage(String symbol)
	{
		StringBuffer message = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		message.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		message.append("  <soap:Body>");
		message.append("    <GetQuote xmlns=\"http://www.webserviceX.NET/\">");
		message.append("      <symbol>").append(symbol).append("</symbol>");
		message.append("    </GetQuote>");
		message.append("  </soap:Body>");
		message.append("</soap:Envelope>");
		return message.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4488.java