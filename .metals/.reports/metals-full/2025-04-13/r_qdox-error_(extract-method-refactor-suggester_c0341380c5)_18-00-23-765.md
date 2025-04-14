error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4854.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4854.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4854.java
text:
```scala
v@@oid redirectToInterceptPage(final Class< ? extends Page> pageClazz);

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
package org.apache.wicket;

import org.apache.wicket.session.pagemap.IPageMapEntry;

/**
 * @author eelcohillenius
 * @author Johan Compagner
 */
public interface IPageMap extends IClusterable
{
	/**
	 * @param id
	 *            The page id to create an attribute for
	 * @return The session attribute for the given page (for replication of state)
	 */
	String attributeForId(final int id);

	/**
	 * Removes all pages from this map
	 */
	void clear();

	/**
	 * Redirects to any intercept page previously specified by a call to redirectToInterceptPage.
	 * 
	 * @return True if an original destination was redirected to
	 * @see Component#redirectToInterceptPage(Page)
	 */
	boolean continueToOriginalDestination();

	/**
	 * Retrieves page with given id.
	 * 
	 * @param id
	 *            The page identifier
	 * @param versionNumber
	 *            The version to get
	 * @return Any page having the given id
	 */
	Page get(final int id, int versionNumber);

	/**
	 * Retrieves entry with given id.
	 * 
	 * @param id
	 *            The page identifier
	 * @return Any entry having the given id
	 */
	IPageMapEntry getEntry(final int id);

	/**
	 * @return Returns the name.
	 */
	String getName();

	/**
	 * @return Size of this page map in bytes, including a sum of the sizes of all the pages it
	 *         contains.
	 */
	long getSizeInBytes();

	/**
	 * @return True if this is the default page map
	 */
	boolean isDefault();

	/**
	 * @return The next id for this pagemap
	 */
	int nextId();

	/**
	 * @param page
	 *            The page to put into this map
	 */
	void put(final Page page);

	/**
	 * Redirects browser to an intermediate page such as a sign-in page. The current request's URL
	 * is saved exactly as it was requested for future use by continueToOriginalDestination(); Only
	 * use this method when you plan to continue to the current URL at some later time; otherwise
	 * just use setResponsePage or, when you are in a constructor, redirectTo.
	 * 
	 * @param pageClazz
	 *            The page clazz to temporarily redirect to
	 */
	void redirectToInterceptPage(final Class pageClazz);

	/**
	 * Redirects browser to an intermediate page such as a sign-in page. The current request's url
	 * is saved for future use by method continueToOriginalDestination(); Only use this method when
	 * you plan to continue to the current url at some later time; otherwise just use
	 * setResponsePage or - when you are in a constructor or checkAccessMethod, call redirectTo.
	 * 
	 * @param page
	 *            The sign in page
	 * 
	 * @see Component#continueToOriginalDestination()
	 */
	void redirectToInterceptPage(final Page page);

	/**
	 * Removes this PageMap from the Session.
	 */
	void remove();

	/**
	 * Removes the page from the pagemap
	 * 
	 * @param page
	 *            page to be removed from the pagemap
	 */
	void remove(final Page page);

	/**
	 * @param entry
	 *            The entry to remove
	 */
	void removeEntry(final IPageMapEntry entry);

	/**
	 * Returns true if the PageMap contains page with given id and versonNumber
	 * 
	 * @param id
	 * @param versionNumber
	 * @return
	 */
	public boolean containsPage(int id, int versionNumber);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4854.java