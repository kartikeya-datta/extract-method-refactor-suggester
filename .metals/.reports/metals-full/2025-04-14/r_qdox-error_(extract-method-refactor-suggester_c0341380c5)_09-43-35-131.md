error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6008.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6008.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6008.java
text:
```scala
i@@f (PackageResource.exists(scope, path, null, null, null))

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
package org.apache.wicket.request.target.resource;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.Application;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Resource;
import org.apache.wicket.Response;
import org.apache.wicket.SharedResources;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.application.IClassResolver;
import org.apache.wicket.markup.html.PackageResource;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Default implementation of {@link ISharedResourceRequestTarget}. Target that denotes a shared
 * {@link org.apache.wicket.Resource}.
 * 
 * @author Eelco Hillenius
 */
public class SharedResourceRequestTarget implements ISharedResourceRequestTarget
{
	/** Logging object */
	private static final Logger log = LoggerFactory.getLogger(SharedResourceRequestTarget.class);

	private final RequestParameters requestParameters;

	/**
	 * Construct.
	 * 
	 * @param requestParameters
	 *            the request parameters
	 */
	public SharedResourceRequestTarget(RequestParameters requestParameters)
	{
		this.requestParameters = requestParameters;
		if (requestParameters == null)
		{
			throw new IllegalArgumentException("requestParameters may not be null");
		}
		else if (requestParameters.getResourceKey() == null)
		{
			throw new IllegalArgumentException("requestParameters.getResourceKey() "
				+ "may not be null");
		}
	}

	/**
	 * @see org.apache.wicket.IRequestTarget#detach(org.apache.wicket.RequestCycle)
	 */
	public void detach(RequestCycle requestCycle)
	{
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof SharedResourceRequestTarget)
		{
			SharedResourceRequestTarget that = (SharedResourceRequestTarget)obj;
			return getRequestParameters().getResourceKey().equals(
				that.getRequestParameters().getResourceKey());
		}
		return false;
	}

	/**
	 * @see org.apache.wicket.request.target.resource.ISharedResourceRequestTarget#getRequestParameters()
	 */
	public final RequestParameters getRequestParameters()
	{
		return requestParameters;
	}

	/**
	 * @see org.apache.wicket.request.target.resource.ISharedResourceRequestTarget#getResourceKey()
	 */
	public final String getResourceKey()
	{
		return requestParameters.getResourceKey();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int result = "SharedResourceRequestTarget".hashCode();
		result += getRequestParameters().getResourceKey().hashCode();
		return 17 * result;
	}

	/**
	 * Respond by looking up the shared resource and delegating the actual response to that
	 * resource.
	 * 
	 * @see org.apache.wicket.IRequestTarget#respond(org.apache.wicket.RequestCycle)
	 */
	public void respond(RequestCycle requestCycle)
	{
		Application application = requestCycle.getApplication();
		SharedResources sharedResources = application.getSharedResources();
		final String resourceKey = getRequestParameters().getResourceKey();
		Resource resource = sharedResources.get(resourceKey);

		// try to lazily register
		if (resource == null)
		{
			int ix = resourceKey.indexOf('/');
			if (ix != -1)
			{
				String className = resourceKey.substring(0, ix);
				IClassResolver resolver = application.getApplicationSettings().getClassResolver();
				Class<?> scope = null;
				try
				{
					// First try to match mounted resources.
					scope = Application.get().getSharedResources().getAliasClass(className);

					// If that fails, resolve it as a fully qualified class
					// name.
					if (scope == null)
					{
						scope = resolver.resolveClass(className);
					}

					// get path component of resource key, replace '..' with escape sequence to
					// prevent crippled urls in browser
					final CharSequence escapeString = application.getResourceSettings()
						.getParentFolderPlaceholder();

					String path = resourceKey.substring(ix + 1);
					if (Strings.isEmpty(escapeString) == false)
					{
						path = path.replace(escapeString, "..");
					}

					if (PackageResource.exists(scope, path, null, null))
					{
						resource = PackageResource.get(scope, path);
					}
				}
				catch (Exception e)
				{
					// besides logging, ignore exception; after this an error
					// will be returned that the resource could not be retrieved
					log.error("unable to lazily register shared resource " + resourceKey, e);
				}
			}
		}

		// if resource is still null, it doesn't exist
		if (resource == null)
		{
			String msg = "shared resource " + resourceKey + " not found or not allowed access";
			log.info(msg);

			Response response = requestCycle.getResponse();
			if (response instanceof WebResponse)
			{
				((WebResponse)response).getHttpServletResponse().setStatus(
					HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			else
			{
				throw new WicketRuntimeException(msg);
			}
		}

		// set request parameters if there are any
		if (requestParameters != null)
		{
			resource.setParameters(requestParameters.getParameters());
		}

		// let the resource handle the request
		resource.onResourceRequested();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "[SharedResourceRequestTarget@" + hashCode() + ", resourceKey=" +
			getRequestParameters().getResourceKey() + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6008.java