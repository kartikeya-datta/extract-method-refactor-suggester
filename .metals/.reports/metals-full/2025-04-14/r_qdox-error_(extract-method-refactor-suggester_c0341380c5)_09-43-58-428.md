error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14495.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14495.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14495.java
text:
```scala
private final S@@tringBuilder buffer = new StringBuilder();

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
package org.apache.wicket.util.template;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.javascript.IJavascriptCompressor;
import org.apache.wicket.util.io.Streams;
import org.apache.wicket.util.lang.Packages;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.resource.locator.ResourceStreamLocator;
import org.apache.wicket.util.string.interpolator.MapVariableInterpolator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A <code>String</code> resource that can be appended to.
 * 
 * @author Eelco Hillenius
 * @since 1.2.6
 */
// TODO cache templates application scoped with a watch
public class PackagedTextTemplate extends TextTemplate
{
	private static final class CachedTextTemplate implements Serializable
	{
		private static final long serialVersionUID = 1L;

		CachedTextTemplate(String text)
		{
		}
	}

	private static final class CachedTextTemplateKey implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private final String className;

		CachedTextTemplateKey(Class<?> clazz, String path)
		{
			className = clazz.getName();
		}

	}

	private static final class TextTemplateCache implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private final Map<CachedTextTemplateKey, CachedTextTemplate> cache = new ConcurrentHashMap<CachedTextTemplateKey, CachedTextTemplate>();

		CachedTextTemplate get(CachedTextTemplateKey key)
		{
			return cache.get(key);
		}

		void put(CachedTextTemplateKey key, CachedTextTemplate value)
		{
			cache.put(key, value);
		}
	}

	/** log. */
	private static final Logger log = LoggerFactory.getLogger(PackagedTextTemplate.class);

	private static final long serialVersionUID = 1L;

	private static final MetaDataKey<TextTemplateCache> TEXT_TEMPLATE_CACHE_KEY = new MetaDataKey<TextTemplateCache>()
	{
		private static final long serialVersionUID = 1L;
	};

	/** contents */
	private final StringBuffer buffer = new StringBuffer();

	/**
	 * Constructor.
	 * 
	 * @param clazz
	 *            the <code>Class</code> to be used for retrieving the classloader for loading the
	 *            <code>PackagedTextTemplate</code>
	 * @param fileName
	 *            the name of the file, relative to the <code>clazz</code> position
	 */
	public PackagedTextTemplate(final Class<?> clazz, final String fileName)
	{
		this(clazz, fileName, "text");
	}

	/**
	 * Constructor.
	 * 
	 * @param clazz
	 *            the <code>Class</code> to be used for retrieving the classloader for loading the
	 *            <code>PackagedTextTemplate</code>
	 * @param fileName
	 *            the name of the file, relative to the <code>clazz</code> position
	 * @param contentType
	 *            the mime type of this resource, such as "<code>image/jpeg</code>" or "
	 *            <code>text/html</code>"
	 */
	public PackagedTextTemplate(final Class<?> clazz, final String fileName,
		final String contentType)
	{
		this(clazz, fileName, contentType, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param clazz
	 *            the <code>Class</code> to be used for retrieving the classloader for loading the
	 *            <code>PackagedTextTemplate</code>
	 * @param fileName
	 *            the name of the file, relative to the <code>clazz</code> position
	 * @param contentType
	 *            the mime type of this resource, such as "<code>image/jpeg</code>" or "
	 *            <code>text/html</code>"
	 * @param encoding
	 *            the file's encoding, for example, "<code>UTF-8</code>"
	 */
	public PackagedTextTemplate(final Class<?> clazz, final String fileName,
		final String contentType, final String encoding)
	{
		super(contentType);

		String path = Packages.absolutePath(clazz, fileName);

		Application app = Application.get();
		app.getMetaData(TEXT_TEMPLATE_CACHE_KEY);

		// first try default class loading locator to find the resource
		IResourceStream stream = app.getResourceSettings().getResourceStreamLocator().locate(clazz,
			path);

		if (stream == null)
		{
			// if default locator couldnt find the resource, than some fallback
			stream = new ResourceStreamLocator().locate(clazz, path);
		}

		if (stream == null)
		{
			throw new IllegalArgumentException("resource " + fileName + " not found for scope " +
				clazz + " (path = " + path + ")");
		}

		try
		{
			if (encoding != null)
			{
				buffer.append(Streams.readString(stream.getInputStream(), encoding));
			}
			else
			{
				buffer.append(Streams.readString(stream.getInputStream()));
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		catch (ResourceStreamNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				stream.close();
			}
			catch (IOException e)
			{
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * @see org.apache.wicket.util.resource.AbstractStringResourceStream#getString()
	 */
	@Override
	public String getString()
	{
		IJavascriptCompressor compressor = Application.get()
			.getResourceSettings()
			.getJavascriptCompressor();

		if (compressor != null)
		{
			return compressor.compress(buffer.toString());
		}
		else
		{
			// don't strip the comments
			return buffer.toString();
		}
	}

	/**
	 * Interpolates a <code>Map</code> of variables with the content and replaces the content with
	 * the result. Variables are denoted in the <code>String</code> by the
	 * <code>syntax ${variableName}</code>. The contents will be altered by replacing each variable
	 * of the form <code>${variableName}</code> with the value returned by
	 * <code>variables.getValue("variableName")</code>.
	 * <p>
	 * WARNING: there is no going back to the original contents after the interpolation is done. If
	 * you need to do different interpolations on the same original contents, use the method
	 * {@link #asString(Map)} instead.
	 * </p>
	 * 
	 * @param variables
	 *            a <code>Map</code> of variables to interpolate
	 * @return this for chaining
	 */
	@Override
	public final TextTemplate interpolate(Map<String, ?> variables)
	{
		if (variables != null)
		{
			String result = new MapVariableInterpolator(buffer.toString(), variables).toString();
			buffer.delete(0, buffer.length());
			buffer.append(result);
		}
		return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14495.java