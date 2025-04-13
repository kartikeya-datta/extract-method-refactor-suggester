error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/663.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/663.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/663.java
text:
```scala
public v@@oid setLastModified(final Time lastModified)

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
package org.apache.wicket.util.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.io.Streams;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.time.Time;


/**
 * Base class for string resources.
 * 
 * @author Jonathan Locke
 */
public abstract class AbstractStringResourceStream extends AbstractResourceStream
	implements
		IStringResourceStream
{
	private static final long serialVersionUID = 1L;

	/** The content-type applied in case the resource stream's default constructor is used */
	public static final String DEFAULT_CONTENT_TYPE = "text";

	/** Charset name for resource */
	private String charsetName;

	/** MIME content type */
	private final String contentType;

	/** The last time this stylesheet was modified */
	private Time lastModified = null;

	/**
	 * Constructor.
	 */
	public AbstractStringResourceStream()
	{
		this(DEFAULT_CONTENT_TYPE);
	}

	/**
	 * Constructor.
	 * 
	 * @param contentType
	 *            The mime type of this resource, such as "image/jpeg" or "text/html"
	 */
	public AbstractStringResourceStream(final String contentType)
	{
		// TODO null for contentType is allowed? or should the default be applied instead?
		this.contentType = contentType;

		setLastModified(Time.now());
	}

	/**
	 * @return This resource as a String.
	 */
	public String asString()
	{
		Reader reader = null;
		try
		{
			if (charsetName == null)
			{
				reader = new InputStreamReader(getInputStream());
			}
			else
			{
				reader = new InputStreamReader(getInputStream(), getCharset());
			}
			return Streams.readString(reader);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Unable to read resource as String", e);
		}
		catch (ResourceStreamNotFoundException e)
		{
			throw new RuntimeException("Unable to read resource as String", e);
		}
		finally
		{
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(this);
		}
	}

	/**
	 * @return Charset for resource
	 */
	protected Charset getCharset()
	{
		// java.nio.Charset is not serializable so we can only store the name
		return (charsetName != null) ? Charset.forName(charsetName) : null;
	}

	/**
	 * Sets the character set used for reading this resource.
	 * 
	 * @param charset
	 *            Charset for component
	 */
	public void setCharset(final Charset charset)
	{
		// java.nio.Charset itself is not serializable so we can only store the name
		charsetName = (charset != null) ? charset.name() : null;
	}

	/**
	 * @see org.apache.wicket.util.resource.IResourceStream#close()
	 */
	public void close() throws IOException
	{
	}

	/**
	 * @see org.apache.wicket.util.resource.IResourceStream#getContentType()
	 */
	@Override
	public String getContentType()
	{
		return contentType;
	}

	/**
	 * @see org.apache.wicket.util.resource.IResourceStream#getInputStream()
	 */
	public InputStream getInputStream() throws ResourceStreamNotFoundException
	{
		final byte[] bytes;
		if (getCharset() != null)
		{
			try
			{
				bytes = getString().getBytes(getCharset().name());
			}
			catch (UnsupportedEncodingException e)
			{
				throw new ResourceStreamNotFoundException("Could not encode resource", e);
			}
		}
		else
		{
			bytes = getString().getBytes();
		}
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * @see org.apache.wicket.util.watch.IModifiable#lastModifiedTime()
	 */
	@Override
	public Time lastModifiedTime()
	{
		return lastModified;
	}

	/**
	 * @param lastModified
	 *            The lastModified to set.
	 */
	public void setLastModified(Time lastModified)
	{
		this.lastModified = lastModified;
	}

	/**
	 * @return The string resource
	 */
	protected abstract String getString();

	@Override
	public final Bytes length()
	{
		int lengthInBytes = Strings.lengthInBytes(getString(), getCharset());
		return Bytes.bytes(lengthInBytes);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/663.java