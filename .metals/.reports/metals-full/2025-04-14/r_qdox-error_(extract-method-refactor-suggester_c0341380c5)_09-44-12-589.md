error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4996.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4996.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4996.java
text:
```scala
public static P@@ackageResource newPackageResource(final Class<?> scope, final String path,

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
package org.apache.wicket.markup.html;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;

import org.apache.wicket.Application;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.util.io.Streams;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;


/**
 * Identical to PackageResource, but supports gzip compression of data
 * 
 * See {@link PackageResource} and {@link CompressedResourceReference}
 * 
 * @author Janne Hietam&auml;ki
 */
public class CompressedPackageResource extends PackageResource
{
	private static final long serialVersionUID = 1L;

	private final IResourceStream resourceStream;

	/**
	 * IResourceStream implementation which compresses the data with gzip if the requests header
	 * Accept-Encoding contains string gzip
	 */
	protected abstract class CompressingResourceStream implements IResourceStream
	{
		private static final long serialVersionUID = 1L;

		/** Cache for compressed data */
		private transient SoftReference<byte[]> cache = new SoftReference<byte[]>(null);

		/** Timestamp of the cache */
		private Time timeStamp = null;

		/**
		 * @see org.apache.wicket.util.resource.IResourceStream#close()
		 */
		public void close() throws IOException
		{
		}

		/**
		 * @see org.apache.wicket.util.resource.IResourceStream#getContentType()
		 */
		public String getContentType()
		{
			return getOriginalResourceStream().getContentType();
		}

		/**
		 * @see org.apache.wicket.util.resource.IResourceStream#getInputStream()
		 */
		public InputStream getInputStream() throws ResourceStreamNotFoundException
		{
			if (supportsCompression())
			{
				return new ByteArrayInputStream(getCompressedContent());
			}
			else
			{
				return getOriginalResourceStream().getInputStream();
			}
		}

		/**
		 * @see org.apache.wicket.util.resource.IResourceStream#getLocale()
		 */
		public Locale getLocale()
		{
			return getOriginalResourceStream().getLocale();
		}

		/**
		 * @see org.apache.wicket.util.watch.IModifiable#lastModifiedTime()
		 */
		public Time lastModifiedTime()
		{
			return getOriginalResourceStream().lastModifiedTime();
		}

		/**
		 * @see org.apache.wicket.util.resource.IResourceStream#length()
		 */
		public long length()
		{
			if (supportsCompression())
			{
				return getCompressedContent().length;
			}
			else
			{
				return getOriginalResourceStream().length();
			}
		}

		/**
		 * @see org.apache.wicket.util.resource.IResourceStream#setLocale(java.util.Locale)
		 */
		public void setLocale(Locale locale)
		{
			getOriginalResourceStream().setLocale(locale);
		}

		/**
		 * @return compressed content
		 */
		private byte[] getCompressedContent()
		{
			IResourceStream stream = getOriginalResourceStream();
			try
			{
				byte ret[];
				if (cache != null)
				{
					ret = cache.get();
					if (ret != null && timeStamp != null)
					{
						if (timeStamp.equals(stream.lastModifiedTime()))
						{
							return ret;
						}
					}
				}
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				GZIPOutputStream zout = new GZIPOutputStream(out);
				Streams.copy(stream.getInputStream(), zout);
				zout.close();
				stream.close();
				ret = out.toByteArray();
				timeStamp = stream.lastModifiedTime();
				cache = new SoftReference<byte[]>(ret);
				return ret;
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
			catch (ResourceStreamNotFoundException e)
			{
				throw new RuntimeException(e);
			}
		}

		protected abstract IResourceStream getOriginalResourceStream();
	}

	/**
	 * Create a new PackageResource
	 * 
	 * @param scope
	 *            This argument will be used to get the class loader for loading the package
	 *            resource, and to determine what package it is in. Typically this is the class in
	 *            which you call this method
	 * @param path
	 *            The path to the resource
	 * @param locale
	 *            The locale of the resource
	 * @param style
	 *            The style of the resource (see {@link org.apache.wicket.Session})
	 * @return The resource
	 */
	protected static PackageResource newPackageResource(final Class<?> scope, final String path,
		final Locale locale, final String style)
	{
		return new CompressedPackageResource(scope, path, locale, style);
	}

	/**
	 * Hidden constructor.
	 * 
	 * @param scope
	 *            This argument will be used to get the class loader for loading the package
	 *            resource, and to determine what package it is in
	 * @param path
	 *            The path to the resource
	 * @param locale
	 *            The locale of the resource
	 * @param style
	 *            The style of the resource
	 */
	protected CompressedPackageResource(Class<?> scope, String path, Locale locale, String style)
	{
		super(scope, path, locale, style);
		resourceStream = newResourceStream();
	}

	/**
	 * Factory method for creating resource stream instance
	 * 
	 * @return new stream instance
	 */
	protected IResourceStream newResourceStream()
	{
		return new CompressingResourceStream()
		{
			private static final long serialVersionUID = 1L;

			/**
			 * 
			 * @see org.apache.wicket.markup.html.CompressedPackageResource.CompressingResourceStream#getOriginalResourceStream()
			 */
			@Override
			protected IResourceStream getOriginalResourceStream()
			{
				return getPackageResourceStream();
			}
		};
	}

	/**
	 * Returns the resource stream of package resource
	 * 
	 * @return resource stream
	 */
	protected IResourceStream getPackageResourceStream()
	{
		return CompressedPackageResource.super.getResourceStream();
	}

	/**
	 * IResourceStream implementation which compresses the data with gzip if the requests header
	 * Accept-Encoding contains string gzip
	 * 
	 * @see org.apache.wicket.markup.html.PackageResource#getResourceStream()
	 */
	@Override
	public IResourceStream getResourceStream()
	{
		return resourceStream;
	}

	/**
	 * @return Whether the client supports compression
	 */
	private boolean supportsCompression()
	{
		if (Application.get().getResourceSettings().getDisableGZipCompression())
		{
			return false;
		}
		WebRequest request = (WebRequest)RequestCycle.get().getRequest();
		String s = request.getHttpServletRequest().getHeader("Accept-Encoding");
		if (s == null)
		{
			return false;
		}
		else
		{
			return s.indexOf("gzip") >= 0;
		}
	}

	/**
	 * @see org.apache.wicket.markup.html.WebResource#setHeaders(org.apache.wicket.protocol.http.WebResponse)
	 */
	@Override
	protected void setHeaders(WebResponse response)
	{
		super.setHeaders(response);
		if (supportsCompression())
		{
			response.setHeader("Content-Encoding", "gzip");
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4996.java