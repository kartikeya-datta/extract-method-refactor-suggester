error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1908.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1908.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1908.java
text:
```scala
public abstract M@@ap getParameterMap();

/*
 * $Id$ $Revision:
 * 1.14 $ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket;

import java.util.Locale;
import java.util.Map;

import wicket.request.IRequestCodingStrategy;
import wicket.request.IRequestCycleProcessor;
import wicket.request.RequestParameters;

/**
 * Base class for page request implementations allowing access to request
 * parameters. A Request has a URL and a parameter map. You can retrieve the URL
 * of the request with getURL(). The entire parameter map can be retrieved via
 * getParameterMap(). Individual parameters can be retrieved via
 * getParameter(String). If multiple values are available for a given parameter,
 * they can be retrieved via getParameters(String).
 * 
 * @author Jonathan Locke
 */
public abstract class Request
{
	/** Any Page decoded for this request */
	private Page page;

	/** the type safe request parameters object for this request. */
	private RequestParameters requestParameters;

	/**
	 * Construct.
	 */
	public Request()
	{
	}

	/**
	 * An implementation of this method is only required if a subclass wishes to
	 * support sessions via URL rewriting. This default implementation simply
	 * returns the URL String it is passed.
	 * 
	 * @param url
	 *            The URL to decode
	 * @return The decoded url
	 */
	public String decodeURL(final String url)
	{
		return url;
	}

	/**
	 * @return The locale for this request
	 */
	public abstract Locale getLocale();

	/**
	 * @return Any Page for this request
	 */
	public Page getPage()
	{
		return page;
	}

	/**
	 * Gets a given (query) parameter by name.
	 * 
	 * @param key
	 *            Parameter name
	 * @return Parameter value
	 */
	public abstract String getParameter(final String key);

	/**
	 * Gets a map of (query) parameters sent with the request.
	 * 
	 * @return Map of parameters
	 */
	public abstract Map<String, ? extends Object> getParameterMap();

	/**
	 * Gets an array of multiple parameters by name.
	 * 
	 * @param key
	 *            Parameter name
	 * @return Parameter values
	 */
	public abstract String[] getParameters(final String key);

	/**
	 * @return Path info for request
	 */
	public abstract String getPath();

	/**
	 * Gets the relative (to some root) url (e.g. in a servlet environment, the
	 * url without the context path and without a leading '/'). Use this method
	 * e.g. to load resources using the servlet context.
	 * 
	 * @return Request URL
	 */
	public abstract String getRelativeURL();

	/**
	 * Retrieves the absolute URL of this request for local use.
	 * 
	 * @return The absolute request URL for local use
	 */
	public abstract String getURL();

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * 
	 * @param page
	 *            The Page for this request
	 */
	public void setPage(final Page page)
	{
		this.page = page;
	}

	/**
	 * Gets the request parameters object using the instance of
	 * {@link IRequestCodingStrategy} of the provided request cycle processor.
	 * 
	 * @return the request parameters object
	 */
	public final RequestParameters getRequestParameters()
	{
		// reused cached parameters
		if (requestParameters != null)
		{
			return requestParameters;
		}

		// get the request encoder to decode the request parameters
		IRequestCycleProcessor processor = RequestCycle.get().getProcessor();
		final IRequestCodingStrategy encoder = processor.getRequestCodingStrategy();
		if (encoder == null)
		{
			throw new WicketRuntimeException("request encoder must be not-null (provided by "
					+ processor + ")");
		}

		// decode the request parameters into a strongly typed parameters
		// object that is to be used by the target resolving
		requestParameters = encoder.decode(this);
		if (requestParameters == null)
		{
			throw new WicketRuntimeException("request parameters must be not-null (provided by "
					+ encoder + ")");
		}
		return requestParameters;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1908.java