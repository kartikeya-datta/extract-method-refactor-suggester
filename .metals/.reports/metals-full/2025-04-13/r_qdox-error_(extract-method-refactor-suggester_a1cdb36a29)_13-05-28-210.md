error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4997.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4997.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4997.java
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

import org.apache.wicket.Application;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.javascript.IJavascriptCompressor;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.util.io.Streams;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.string.JavascriptStripper;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Package resource for javascript files. It strips comments and whitespace from javascript and
 * gzips the content. The stripped and gzipped version is cached.
 * 
 * @author Matej Knopp
 */
public class JavascriptPackageResource extends CompressedPackageResource
{
	private static final long serialVersionUID = 1L;;

	private static final Logger log = LoggerFactory.getLogger(JavascriptPackageResource.class);

	/**
	 * Returns a new instance of {@link HeaderContributor} with a header contributor that references
	 * a java script file that lives in a package.
	 * 
	 * @param scope
	 *            The scope of the package resource (typically the class of the caller, or a class
	 *            that lives in the package where the resource lives).
	 * @param path
	 *            The path
	 * @return the new header contributor instance
	 */
	public static final HeaderContributor getHeaderContribution(final Class<?> scope,
		final String path)
	{
		return new HeaderContributor(new IHeaderContributor()
		{
			private static final long serialVersionUID = 1L;

			public void renderHead(IHeaderResponse response)
			{
				response.renderJavascriptReference(new JavascriptResourceReference(scope, path));
			}
		});
	}

	/**
	 * Returns a new instance of {@link HeaderContributor} with a header contributor that references
	 * a java script file that lives in a package.
	 * 
	 * @param reference
	 * 
	 * @return the new header contributor instance
	 */
	public static final HeaderContributor getHeaderContribution(final ResourceReference reference)
	{
		return new HeaderContributor(new IHeaderContributor()
		{
			private static final long serialVersionUID = 1L;

			public void renderHead(IHeaderResponse response)
			{
				response.renderJavascriptReference(reference);
			}
		});
	}

	/**
	 * Returns a new instance of {@link HeaderContributor} with a header contributor referencing a
	 * java script file using one of the following schemes:
	 * <ul>
	 * <li>Starts with http:// or https:// for an external reference.</li>
	 * <li>Starts with "/" for an absolute reference that Wicket will not rewrite.</li>
	 * <li>Starts with anything else, which Wicket will automatically prepend to make relative to
	 * the context root of your web-app.</li>
	 * </ul>
	 * 
	 * @param location
	 *            The location of the java script file.
	 * @return the new header contributor instance
	 */
	public static final HeaderContributor getHeaderContribution(final String location)
	{
		return new HeaderContributor(new IHeaderContributor()
		{
			private static final long serialVersionUID = 1L;

			public void renderHead(IHeaderResponse response)
			{
				response.renderJavascriptReference(returnRelativePath(location));
			}
		});
	}

	/**
	 * 
	 * @param location
	 * @return relative path
	 */
	private static final String returnRelativePath(String location)
	{
		// WICKET-59 allow external URLs, WICKET-612 allow absolute URLs.
		if (location.startsWith("http://") || location.startsWith("https://") ||
			location.startsWith("/"))
		{
			return location;
		}
		else
		{
			return RequestCycle.get()
				.getProcessor()
				.getRequestCodingStrategy()
				.rewriteStaticRelativeUrl(location);
		}
	}

	/**
	 * Resource Stream that caches the stripped content.
	 * 
	 * @author Matej Knopp
	 */
	protected abstract class FilteringResourceStream implements IResourceStream
	{
		private static final long serialVersionUID = 1L;

		/** Cache for compressed data */
		private SoftReference<byte[]> cache = new SoftReference<byte[]>(null);

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
			return new ByteArrayInputStream(getFilteredContent());
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
			return getFilteredContent().length;
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
		private byte[] getFilteredContent()
		{
			IResourceStream stream = getOriginalResourceStream();
			try
			{
				byte ret[] = cache.get();
				if (ret != null && timeStamp != null)
				{
					if (timeStamp.equals(stream.lastModifiedTime()))
					{
						return ret;
					}
				}

				int length = (int)stream.length();
				ByteArrayOutputStream out = new ByteArrayOutputStream(length > 0 ? length : 0);
				Streams.copy(stream.getInputStream(), out);
				stream.close();
				ret = filterContent(out.toByteArray());
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

		protected abstract byte[] filterContent(byte[] input);

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
		return new JavascriptPackageResource(scope, path, locale, style);
	}

	/**
	 * Creates a new javascript package resource.
	 * 
	 * @param scope
	 * @param path
	 * @param locale
	 * @param style
	 */
	protected JavascriptPackageResource(Class<?> scope, String path, Locale locale, String style)
	{
		super(scope, path, locale, style);
	}

	/**
	 * @see org.apache.wicket.markup.html.CompressedPackageResource#newResourceStream()
	 */
	@Override
	protected IResourceStream newResourceStream()
	{
		final FilteringResourceStream filteringStream = new FilteringResourceStream()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected byte[] filterContent(byte[] input)
			{
				try
				{
					// @TODO remove in 1.5
					if (Application.get()
						.getResourceSettings()
						.getStripJavascriptCommentsAndWhitespace())
					{
						String s = new String(input, "UTF-8");
						return JavascriptStripper.stripCommentsAndWhitespace(s).getBytes("UTF-8");
					}

					IJavascriptCompressor compressor = Application.get()
						.getResourceSettings()
						.getJavascriptCompressor();
					if (compressor != null)
					{
						String s = new String(input, "UTF-8");
						return compressor.compress(s).getBytes("UTF-8");
					}

					// don't strip the comments, just return original input
					return input;
				}
				catch (Exception e)
				{
					log.error("Error while filtering content", e);
					return input;
				}
			}

			@Override
			protected IResourceStream getOriginalResourceStream()
			{
				return getPackageResourceStream();
			}
		};

		return new CompressingResourceStream()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IResourceStream getOriginalResourceStream()
			{
				return filteringStream;
			}
		};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4997.java