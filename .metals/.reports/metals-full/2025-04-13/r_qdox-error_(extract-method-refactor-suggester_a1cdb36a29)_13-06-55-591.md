error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14472.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14472.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14472.java
text:
```scala
final S@@tringBuilder buffer = new StringBuilder(2048);

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
package org.apache.wicket.util.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Utilities methods for working with input and output streams.
 * 
 * @author Jonathan Locke
 * @author Igor Vaynberg
 */
public final class Streams
{
	private static final String XML_PROPERTIES_DTD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		+ "<!-- DTD for properties -->" + "<!ELEMENT properties ( comment?, entry* ) >"
		+ "<!ATTLIST properties" + " version CDATA #FIXED \"1.0\">"
		+ "<!ELEMENT comment (#PCDATA) >" + "<!ELEMENT entry (#PCDATA) >" + "<!ATTLIST entry "
		+ " key CDATA #REQUIRED>";

	/**
	 * Closes a closeable. Guards against null closables.
	 * 
	 * @param closeable
	 *            closeable to close
	 * @throws IOException
	 *             when close fails
	 */
	public static void close(Closeable closeable) throws IOException
	{
		if (closeable != null)
		{
			closeable.close();
		}
	}


	/**
	 * Writes the input stream to the output stream. Input is done without a Reader object, meaning
	 * that the input is copied in its raw form.
	 * 
	 * @param in
	 *            The input stream
	 * @param out
	 *            The output stream
	 * @return Number of bytes copied from one stream to the other
	 * @throws IOException
	 */
	public static int copy(final InputStream in, final OutputStream out) throws IOException
	{
		return copy(in, out, 4096);
	}

	/**
	 * Writes the input stream to the output stream. Input is done without a Reader object, meaning
	 * that the input is copied in its raw form.
	 * 
	 * @param in
	 *            The input stream
	 * @param out
	 *            The output stream
	 * @param bufSize
	 *            The buffer size. A good value is 4096.
	 * @return Number of bytes copied from one stream to the other
	 * @throws IOException
	 */
	public static int copy(final InputStream in, final OutputStream out, final int bufSize)
		throws IOException
	{
		if (bufSize <= 0)
		{
			throw new IllegalArgumentException("The parameter 'bufSize' must not be <= 0");
		}

		final byte[] buffer = new byte[bufSize];
		int bytesCopied = 0;
		while (true)
		{
			int byteCount = in.read(buffer, 0, buffer.length);
			if (byteCount <= 0)
			{
				break;
			}
			out.write(buffer, 0, byteCount);
			bytesCopied += byteCount;
		}
		return bytesCopied;
	}

	/**
	 * Loads properties from an XML input stream into the provided properties object.
	 * 
	 * @param properties
	 *            The object to load the properties into
	 * @param inputStream
	 * @throws IOException
	 *             When the input stream could not be read from
	 */
	public static void loadFromXml(Properties properties, InputStream inputStream)
		throws IOException
	{
		if (properties == null)
		{
			throw new IllegalArgumentException("properties must not be null");
		}
		if (inputStream == null)
		{
			throw new IllegalArgumentException("inputStream must not be null");
		}

		// TODO in a Wicket version that supports Java 5 (Wicket 2.0?), we can
		// just use the loadFromXml method on java.util.Properties directly
		// rather than manual as we do here

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		documentBuilderFactory.setValidating(true);
		documentBuilderFactory.setCoalescing(true);
		documentBuilderFactory.setIgnoringComments(true);
		try
		{
			DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
			db.setEntityResolver(new EntityResolver()
			{
				public InputSource resolveEntity(String publicId, String systemId)
					throws SAXException
				{
					if (systemId.equals("http://java.sun.com/dtd/properties.dtd"))
					{
						InputSource inputSource;
						inputSource = new InputSource(new StringReader(XML_PROPERTIES_DTD));
						inputSource.setSystemId("http://java.sun.com/dtd/properties.dtd");
						return inputSource;
					}
					else
					{
						throw new SAXException("Invalid system identifier: " + systemId);
					}
				}
			});
			db.setErrorHandler(new ErrorHandler()
			{
				public void error(SAXParseException e) throws SAXException
				{
					throw e;
				}

				public void fatalError(SAXParseException e) throws SAXException
				{
					throw e;
				}

				public void warning(SAXParseException e) throws SAXException
				{
					throw e;
				}
			});
			InputSource is = new InputSource(inputStream);
			Document doc = db.parse(is);
			NodeList entries = ((Element)doc.getChildNodes().item(1)).getChildNodes();
			int len = entries.getLength();
			for (int i = (len > 0 && entries.item(0).getNodeName().equals("comment")) ? 1 : 0; i < len; i++)
			{
				Element entry = (Element)entries.item(i);
				if (entry.hasAttribute("key"))
				{
					Node node = entry.getFirstChild();
					String val = (node == null) ? "" : node.getNodeValue();
					properties.setProperty(entry.getAttribute("key"), val);
				}
			}
		}
		catch (ParserConfigurationException e)
		{
			throw new RuntimeException(e);
		}
		catch (SAXException e)
		{
			throw new RuntimeException("invalid XML properties format", e);
		}
	}

	/**
	 * Reads a stream as a string.
	 * 
	 * @param in
	 *            The input stream
	 * @return The string
	 * @throws IOException
	 */
	public static String readString(final InputStream in) throws IOException
	{
		return readString(new BufferedReader(new InputStreamReader(in)));
	}

	/**
	 * Reads a string using a character encoding.
	 * 
	 * @param in
	 *            The input
	 * @param encoding
	 *            The character encoding of the input data
	 * @return The string
	 * @throws IOException
	 */
	public static String readString(final InputStream in, final CharSequence encoding)
		throws IOException
	{
		return readString(new BufferedReader(new InputStreamReader(in, encoding.toString())));
	}

	/**
	 * Reads all input from a reader into a string.
	 * 
	 * @param in
	 *            The input
	 * @return The string
	 * @throws IOException
	 */
	public static String readString(final Reader in) throws IOException
	{
		final StringBuffer buffer = new StringBuffer(2048);
		int value;

		while ((value = in.read()) != -1)
		{
			buffer.append((char)value);
		}

		return buffer.toString();
	}

	/**
	 * Private to prevent instantiation.
	 */
	private Streams()
	{
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14472.java