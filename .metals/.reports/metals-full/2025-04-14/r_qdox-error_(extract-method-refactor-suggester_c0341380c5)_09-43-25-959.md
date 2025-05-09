error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7313.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7313.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7313.java
text:
```scala
r@@esourceResponse.setCacheDurationToMaximum();

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
package org.apache.wicket.request.resource;

import java.io.IOException;
import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.ThreadContext;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.lang.Packages;
import org.apache.wicket.util.lang.WicketObjects;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackageResource extends AbstractResource
{
	private static final Logger log = LoggerFactory.getLogger(PackageResource.class);

	private static final long serialVersionUID = 1L;

	/**
	 * Exception thrown when the creation of a package resource is not allowed.
	 */
	public static final class PackageResourceBlockedException extends WicketRuntimeException
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Construct.
		 *
		 * @param message error message
		 */
		public PackageResourceBlockedException(String message)
		{
			super(message);
		}
	}

	/**
	 * The path to the resource
	 */
	private final String absolutePath;

	/**
	 * The resource's locale
	 */
	private final Locale locale;

	/**
	 * The path this resource was created with.
	 */
	private final String path;

	/**
	 * The scoping class, used for class loading and to determine the package.
	 */
	private final String scopeName;

	/**
	 * The resource's style
	 */
	private final String style;

	/**
	 * The component's variation (of the style)
	 */
	private final String variation;


	/**
	 * Hidden constructor.
	 *
	 * @param scope     This argument will be used to get the class loader for loading the package
	 *                  resource, and to determine what package it is in
	 * @param name      The relative path to the resource
	 * @param locale    The locale of the resource
	 * @param style     The style of the resource
	 * @param variation The component's variation (of the style)
	 */
	protected PackageResource(final Class<?> scope, final String name, final Locale locale,
	                          final String style, final String variation)
	{
		// Convert resource path to absolute path relative to base package
		absolutePath = Packages.absolutePath(scope, name);

		if (!accept(scope, name))
		{
			throw new PackageResourceBlockedException(
					"Access denied to (static) package resource " + absolutePath +
							". See IPackageResourceGuard");
		}

		// TODO NG: Check path for ../

		scopeName = scope.getName();
		path = name;
		this.locale = locale;
		this.style = style;
		this.variation = variation;
	}

	/**
	 * Gets the scoping class, used for class loading and to determine the package.
	 *
	 * @return the scoping class
	 */
	public final Class<?> getScope()
	{
		return WicketObjects.resolveClass(scopeName);
	}

	/**
	 * Gets the style.
	 *
	 * @return the style
	 */
	public final String getStyle()
	{
		return style;
	}

	/**
	 * creates a new resource response based on the request attributes
	 *
	 * @param attributes current request attributes from client
	 * @return resource response for answering request
	 */
	@Override
	protected ResourceResponse newResourceResponse(Attributes attributes)
	{
		final ResourceResponse resourceResponse = new ResourceResponse();

		if (resourceResponse.dataNeedsToBeWritten(attributes))
		{
			// get resource stream
			final IResourceStream resourceStream = getResourceStream();

			// bail out if resource stream could not be found
			if (resourceStream == null)
				return sendResourceError(resourceResponse, 404, "Unable to find resource");

			// set Content-Type (may be null)
			resourceResponse.setContentType(resourceStream.getContentType());

			// add Last-Modified header (to support HEAD requests and If-Modified-Since)
			final Time lastModified = resourceStream.lastModifiedTime();

			if(lastModified != null)
				resourceResponse.setLastModified(lastModified.toDate());

			try
			{
				// read resource data
				final byte[] bytes;

				try
				{
					bytes = IOUtils.toByteArray(resourceStream.getInputStream());
				}
				finally
				{
					resourceStream.close();
				}

				// send Content-Length header
				resourceResponse.setContentLength(bytes.length);

				// send response body with resource data
				resourceResponse.setWriteCallback(new WriteCallback()
				{
					@Override
					public void writeData(Attributes attributes)
					{
						attributes.getResponse().write(bytes);
					}
				});
			}
			catch (IOException e)
			{
				log.debug(e.getMessage(), e);
				return sendResourceError(resourceResponse, 500, "Unable to read resource stream");
			}
			catch (ResourceStreamNotFoundException e)
			{
				log.debug(e.getMessage(), e);
				return sendResourceError(resourceResponse, 500, "Unable to open resource stream");
			}
		}

		// if timestamps are enabled on resource we can maximize caching with no pain
		if(Application.get().getResourceSettings().getUseTimestampOnResources())
		{
			resourceResponse.setCacheDuration(Integer.MAX_VALUE);
			resourceResponse.setCachePublic(true);
		}

		return resourceResponse;
	}

	/**
	 * send resource specific error message and write log entry
	 *
	 * @param resourceResponse resource response
	 * @param errorCode error code (=http status)
	 * @param errorMessage error message (=http error message)
	 * @return resource response for method chaining
	 */
	private ResourceResponse sendResourceError(ResourceResponse resourceResponse, int errorCode, String errorMessage)
	{
		String msg = String.format("resource [path = %s, style = %s, variation = %s, locale = %s]: %s (status=%d)",
		                           absolutePath, style, variation, locale, errorMessage, errorCode);

		log.warn(msg);

		resourceResponse.setError(errorCode, errorMessage);
		return resourceResponse;
	}

	/**
	 * locate resource stream for current resource
	 *
	 * @return resource stream or <code>null</code> if not found
	 */
	private IResourceStream getResourceStream()
	{
		// Locate resource
		return ThreadContext.getApplication()
				.getResourceSettings()
				.getResourceStreamLocator()
				.locate(getScope(), absolutePath, style, variation, locale, null);
	}

	/**
	 * @param scope resource scope
	 * @param path  resource path
	 * @return <code>true<code> if resource access is granted
	 */
	private boolean accept(Class<?> scope, String path)
	{
		IPackageResourceGuard guard = ThreadContext.getApplication()
				.getResourceSettings()
				.getPackageResourceGuard();

		return guard.accept(scope, path);
	}

	/**
	 * Gets whether a resource for a given set of criteria exists.
	 *
	 * @param scope     This argument will be used to get the class loader for loading the package
	 *                  resource, and to determine what package it is in. Typically this is the class in
	 *                  which you call this method
	 * @param path      The path to the resource
	 * @param locale    The locale of the resource
	 * @param style     The style of the resource (see {@link org.apache.wicket.Session})
	 * @param variation The component's variation (of the style)
	 * @return true if a resource could be loaded, false otherwise
	 */
	public static boolean exists(final Class<?> scope, final String path, final Locale locale,
	                             final String style, final String variation)
	{
		String absolutePath = Packages.absolutePath(scope, path);
		return ThreadContext.getApplication()
				.getResourceSettings()
				.getResourceStreamLocator()
				.locate(scope, absolutePath, style, variation, locale, null) != null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7313.java