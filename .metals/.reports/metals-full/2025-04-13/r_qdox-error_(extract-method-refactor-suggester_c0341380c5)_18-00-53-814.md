error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2445.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2445.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2445.java
text:
```scala
public static i@@nt copyAndClose(final InputStream in, final OutputStream out)

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

/**
 * Utilities methods for working with input and output streams.
 * 
 * @author Jonathan Locke
 * @author Igor Vaynberg
 */
public final class Streams
{
	/**
	 * Writes the input stream to the output stream. Input is done without a Reader object, meaning
	 * that the input is copied in its raw form. After it is copied it will close the streams.
	 * 
	 * @param in
	 *            The input stream
	 * @param out
	 *            The output stream
	 * @return Number of bytes copied from one stream to the other
	 * @throws IOException
	 */
	public static long copyAndClose(final InputStream in, final OutputStream out)
		throws IOException
	{
		try
		{
			return copy(in, out);
		}
		finally
		{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
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
	public static void loadFromXml(final Properties properties, final InputStream inputStream)
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

		properties.loadFromXML(inputStream);
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
		final StringBuilder buffer = new StringBuilder(2048);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2445.java