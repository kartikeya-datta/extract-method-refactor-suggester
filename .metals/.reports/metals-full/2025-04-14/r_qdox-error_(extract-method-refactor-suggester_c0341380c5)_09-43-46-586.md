error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9484.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9484.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9484.java
text:
```scala
m@@ountBookmarkablePageWithUrlCoding("/path/to/page2qpencoded", Page2QP.class);

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
package org.apache.wicket.examples.niceurl;

import org.apache.wicket.Page;
import org.apache.wicket.examples.WicketExampleApplication;
import org.apache.wicket.examples.niceurl.mounted.Page3;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;
import org.apache.wicket.protocol.http.request.WebRequestCodingStrategy;
import org.apache.wicket.request.IRequestCodingStrategy;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.request.target.coding.QueryStringUrlCodingStrategy;
import org.apache.wicket.util.lang.PackageName;


/**
 * Application class for this example.
 * 
 * @author Eelco Hillenius
 */
public class NiceUrlApplication extends WicketExampleApplication
{
	/**
	 * Construct.
	 */
	public NiceUrlApplication()
	{
		super();
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class< ? extends Page< ? >> getHomePage()
	{
		return Home.class;
	}

	private void mountBookmarkablePageWithUrlCoding(String path, Class pageClass)
	{
		mount(new QueryStringUrlCodingStrategy(path, pageClass));
	}

	/**
	 * @see org.apache.wicket.examples.WicketExampleApplication#init()
	 */
	@Override
	protected void init()
	{
		// Disable creation of javascript which jWebUnit (test only)
		// doesn't handle properly
		getPageSettings().setAutomaticMultiWindowSupport(false);

		// mount single bookmarkable pages
		mountBookmarkablePage("/the/homepage/path", Home.class);
		mountBookmarkablePage("/a/nice/path/to/the/first/page", Page1.class);
		mountBookmarkablePage("/path/to/page2", Page2.class);

		mountBookmarkablePageWithUrlCoding("/path/to/page2qpencoded/", Page2QP.class);

		// mount a whole package at once (all bookmarkable pages,
		// the relative class name will be part of the url

		// maybe not the neatest sight, but for package mounting it makes
		// sense to use one of the (important) classes in your package, so
		// that any refactoring (like a package rename) will automatically
		// be applied here.
		mount("/my/mounted/package", PackageName.forClass(Page3.class));
	}

	/**
	 * Sets up a request coding strategy that uses case-insensitive mounts
	 * 
	 * @see org.apache.wicket.protocol.http.WebApplication#newRequestCycleProcessor()
	 */
	@Override
	protected IRequestCycleProcessor newRequestCycleProcessor()
	{
		return new WebRequestCycleProcessor()
		{
			@Override
			protected IRequestCodingStrategy newRequestCodingStrategy()
			{
				WebRequestCodingStrategy.Settings stratSettings = new WebRequestCodingStrategy.Settings();
				stratSettings.setMountsCaseSensitive(false);
				return new WebRequestCodingStrategy(stratSettings);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9484.java