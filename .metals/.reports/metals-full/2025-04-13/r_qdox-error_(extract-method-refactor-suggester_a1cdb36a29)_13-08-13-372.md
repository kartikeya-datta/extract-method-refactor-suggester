error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3310.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3310.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3310.java
text:
```scala
i@@f ((last != null) && (urlSegments.isEmpty() || (baseUrlSegments.size() == common)))

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
package org.apache.wicket.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.string.PrependingStringBuffer;

/**
 * Takes care of rendering relative (or in future possibly absolute - depending on configuration)
 * URLs.
 * <p>
 * All Urls are rendered relative to the base Url. Base Url is normally Url of the page being
 * rendered. However, during Ajax request and redirect to buffer rendering the BaseUrl needs to be
 * adjusted.
 * 
 * @author Matej Knopp
 */
public class UrlRenderer
{
	private Url baseUrl;

	/**
	 * Construct.
	 * 
	 * @param base
	 *            base Url. All generated Urls will be relative to this Url.
	 */
	public UrlRenderer(final Url base)
	{
		Args.notNull(base, "base");

		baseUrl = base;
	}

	/**
	 * Sets the base Url. All generated URLs will be relative to this Url.
	 * 
	 * @param base
	 * @return original base Url
	 */
	public Url setBaseUrl(final Url base)
	{
		Args.notNull(base, "base");

		Url original = baseUrl;
		baseUrl = base;
		return original;
	}

	/**
	 * Returns the base Url.
	 * 
	 * @return base Url
	 */
	public Url getBaseUrl()
	{
		return baseUrl;
	}

	/**
	 * Renders the Url relative to currently set Base Url.
	 * 
	 * This method is only intended for Wicket URLs, because the {@link Url} object represents part
	 * of URL after Wicket Filter.
	 * 
	 * For general URLs within context use {@link #renderContextPathRelativeUrl(String, Request)}
	 * 
	 * @param url
	 * @return Url rendered as string
	 */
	public String renderUrl(final Url url)
	{
		Args.notNull(url, "url");

		if (url.isAbsolute())
		{
			return url.toString();
		}
		else
		{
			List<String> baseUrlSegments = getBaseUrl().getSegments();
			List<String> urlSegments = new ArrayList<String>(url.getSegments());

			List<String> newSegments = new ArrayList<String>();

			int common = 0;

			String last = null;

			for (String s : baseUrlSegments)
			{
				if (!urlSegments.isEmpty() && s.equals(urlSegments.get(0)))
				{
					++common;
					last = urlSegments.remove(0);
				}
				else
				{
					break;
				}
			}

			// we want the new URL to have at least one segment (other than possible ../)
			if (last != null && (urlSegments.isEmpty() || baseUrlSegments.size() == common))
			{
				--common;
				urlSegments.add(0, last);
			}

			int baseUrlSize = baseUrlSegments.size();
			if (common + 1 == baseUrlSize)
			{
				newSegments.add(".");
			}
			else
			{

				for (int i = common + 1; i < baseUrlSize; ++i)
				{
					newSegments.add("..");
				}
			}
			newSegments.addAll(urlSegments);

			return new Url(newSegments, url.getQueryParameters()).toString();
		}
	}

	/**
	 * Renders the URL within context relative to current base URL.
	 * 
	 * @param url
	 * @param request
	 * @return relative URL
	 */
	public String renderContextPathRelativeUrl(String url, final Request request)
	{
		Args.notNull(url, "url");

		if (url.startsWith("/"))
		{
			url = url.substring(1);
		}

		PrependingStringBuffer buffer = new PrependingStringBuffer(url);
		for (int i = 0; i < getBaseUrl().getSegments().size() - 1; ++i)
		{
			buffer.prepend("../");
		}

		buffer.prepend(request.getPrefixToContextPath());

		return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3310.java