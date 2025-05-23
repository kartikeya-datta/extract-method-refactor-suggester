error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/659.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/659.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/659.java
text:
```scala
public i@@nt read(final char[] buf, final int from, final int to) throws IOException

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
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This is a simple XmlReader. Its only purpose is to read the xml decl string from the input and
 * apply proper character encoding to all subsequent characters. The xml decl string itself is
 * removed from the output.
 * 
 * @author Juergen Donnerstag
 */
public final class XmlReader extends Reader
{
	/** Regex to find <?xml encoding ... ?> */
	private static final Pattern xmlDecl = Pattern.compile("[\\s\\n\\r]*<\\?xml(\\s+.*)?\\?>");

	/** Regex to find <?xml encoding ... ?> */
	private static final Pattern encodingPattern = Pattern.compile("\\s+encoding\\s*=\\s*([\"\'](.*?)[\"\']|(\\S*)).*\\?>");

	/** Null, if JVM default. Else from <?xml encoding=""> */
	private String encoding;

	/** Null or if found in the markup, the whole <?xml ...?> string */
	private CharSequence xmlDeclarationString;

	/** The input stream to read the data from */
	private final InputStream inputStream;

	/** The reader which does the character encoding */
	private Reader reader;

	/**
	 * Construct.
	 * 
	 * @param inputStream
	 *            The InputStream to read the xml data from
	 * @param defaultEncoding
	 *            Default character encoding to use when not specified in XML declaration, specify
	 *            null to use JVM default
	 * @throws IOException
	 *             In case something went wrong while reading the data
	 */
	public XmlReader(final InputStream inputStream, final String defaultEncoding)
		throws IOException
	{
		// The xml parser does not have a parent filter
		super();

		this.inputStream = inputStream;
		encoding = defaultEncoding;

		if (inputStream == null)
		{
			throw new IllegalArgumentException("Parameter 'inputStream' must not be null");
		}

		init();
	}

	/**
	 * Return the encoding used while reading the markup file.
	 * 
	 * @return if null, then JVM default
	 */
	public final String getEncoding()
	{
		return encoding;
	}

	/**
	 * Return the XML declaration string, in case if found in the markup.
	 * 
	 * @return Null, if not found.
	 */
	public final CharSequence getXmlDeclaration()
	{
		return xmlDeclarationString;
	}

	/**
	 * Reads and parses markup from a resource such as file.
	 * 
	 * @throws IOException
	 */
	public void init() throws IOException
	{
		if (!inputStream.markSupported())
		{
			throw new IOException("The InputStream must support mark/reset");
		}

		// read ahead buffer required for the first line of the markup
		// (encoding)
		final int readAheadSize = 80;
		inputStream.mark(readAheadSize);

		// read-ahead the input stream and check if it starts with <?xml..?>.
		if (getXmlDeclaration(inputStream, readAheadSize))
		{
			// If yes than determine the encoding from the xml decl
			encoding = determineEncoding(xmlDeclarationString);
		}
		else
		{
			// If not, reset the input stream to the beginning of the file
			inputStream.reset();
		}

		if (encoding == null)
		{
			// Use JVM default
			reader = new BufferedReader(new InputStreamReader(inputStream));
		}
		else
		{
			// Use the encoding provided
			reader = new BufferedReader(new InputStreamReader(inputStream, encoding));
		}
	}

	/**
	 * Determine the encoding from the xml decl.
	 * 
	 * @param string
	 *            The xmlDecl string
	 * @return The encoding. Null, if not found
	 */
	private final String determineEncoding(final CharSequence string)
	{
		// Does the string match the <?xml .. ?> pattern
		final Matcher matcher = encodingPattern.matcher(string);
		if (!matcher.find())
		{
			// No
			return null;
		}

		// Extract the encoding
		String encoding = matcher.group(2);
		if ((encoding == null) || (encoding.length() == 0))
		{
			encoding = matcher.group(3);
		}

		if (encoding != null)
		{
			encoding = encoding.trim();
		}

		return encoding;
	}

	/**
	 * Read-ahead the input stream (markup file). If the first line contains &lt;?xml...?&gt;, than
	 * remember the xml decl for later to determine the encoding.
	 * <p>
	 * The xml decl will not be forwarded to the user.
	 * 
	 * @param in
	 *            The markup file
	 * @param readAheadSize
	 *            The read ahead buffer available to read the xml encoding information
	 * @return true, if &lt;?xml ..?&gt; has been found
	 * @throws IOException
	 */
	private final boolean getXmlDeclaration(final InputStream in, final int readAheadSize)
		throws IOException
	{
		// Max one line
		final StringBuilder pushBack = new StringBuilder(readAheadSize);

		// The current char from the markup file
		int value;
		while ((value = in.read()) != -1)
		{
			pushBack.append((char)value);

			// Stop at the end of the first tag or end of line. If it is HTML
			// without newlines, stop after X bytes (= characters)
			if ((value == '>') || (value == '\n') || (value == '\r') ||
				(pushBack.length() >= (readAheadSize - 1)))
			{
				break;
			}
		}

		// Does the string match the <?xml .. ?> pattern
		final Matcher matcher = xmlDecl.matcher(pushBack);
		if (!matcher.matches())
		{
			// No
			return false;
		}

		// Save the whole <?xml ..> string for later
		xmlDeclarationString = pushBack.toString().trim();
		return true;
	}

	/**
	 * @see java.io.Reader#close()
	 */
	@Override
	public void close() throws IOException
	{
		try
		{
			reader.close();
		}
		finally
		{
			inputStream.close();
		}
	}

	/**
	 * @see java.io.Reader#read(char[], int, int)
	 */
	@Override
	public int read(char[] buf, int from, int to) throws IOException
	{
		return reader.read(buf, from, to);
	}

	/**
	 * @return The markup to be parsed
	 */
	@Override
	public String toString()
	{
		return inputStream.toString() + " (" + encoding + ")";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/659.java