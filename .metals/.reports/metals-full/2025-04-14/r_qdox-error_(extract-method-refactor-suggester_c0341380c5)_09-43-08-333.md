error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15945.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15945.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15945.java
text:
```scala
private final i@@nt maxVersions;

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
package org.apache.wicket.session.pagemap;

import org.apache.wicket.AccessStackPageMap;
import org.apache.wicket.IPageMap;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.AccessStackPageMap.Access;

/**
 * A simple eviction strategy that evicts the least recently accessed page
 * version from the given page map.
 * 
 * @author Jonathan Locke
 */
public class LeastRecentlyAccessedEvictionStrategy implements IPageMapEvictionStrategy
{
	private static final long serialVersionUID = 1L;

	/** Maximum number of page versions in a page map before evictions start */
	private int maxVersions;

	/**
	 * Constructor.
	 * 
	 * @param maxVersions
	 *            Maximum number of page versions before eviction occurs
	 */
	public LeastRecentlyAccessedEvictionStrategy(int maxVersions)
	{
		if (maxVersions < 1)
		{
			throw new IllegalArgumentException("Value for maxVersions must be >= 1");
		}
		this.maxVersions = maxVersions;
	}

	/**
	 * @see org.apache.wicket.session.pagemap.IPageMapEvictionStrategy#evict(org.apache.wicket.PageMap)
	 */
	public void evict(final IPageMap pageMap)
	{
		if (pageMap instanceof AccessStackPageMap)
		{
			synchronized (Session.get())
			{
				AccessStackPageMap accessPM = (AccessStackPageMap)pageMap;
				// Do we need to evict under this strategy?
				if (accessPM.getVersions() > maxVersions)
				{
					// Remove oldest entry from access stack
					final AccessStackPageMap.Access oldestAccess = (Access)accessPM.getAccessStack()
							.remove(0);
					final IPageMapEntry oldestEntry = pageMap.getEntry(oldestAccess.getId());

					// If entry is a page (cannot be null if we're evicting)
					if (oldestEntry instanceof Page)
					{
						Page page = (Page)oldestEntry;

						// If there is more than one version of this page
						if (page.getVersions() > 1)
						{
							// expire the oldest version
							page.expireOldestVersion();
						}
						else
						{
							// expire whole page
							accessPM.removeEntry(page);
						}
					}
					else
					{
						// If oldestEntry is not an instance of Page, then it is
						// some
						// custom, user-defined IPageMapEntry class and cannot
						// contain
						// versioning information, so we just remove the entry.
						if (oldestEntry != null)
						{
							accessPM.removeEntry(oldestEntry);
						}
					}
				}
			}
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "[LeastRecentlyAccessedEvictionStrategy maxVersions = " + maxVersions + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15945.java