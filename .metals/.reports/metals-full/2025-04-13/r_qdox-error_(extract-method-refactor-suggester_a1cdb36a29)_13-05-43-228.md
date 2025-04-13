error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15180.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15180.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15180.java
text:
```scala
I@@PageManager pageManager = wicketTester.getSession().getPageManager();

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
package org.apache.wicket.versioning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.wicket.IPageManagerProvider;
import org.apache.wicket.Page;
import org.apache.wicket.page.IPageManager;
import org.apache.wicket.page.IPageManagerContext;
import org.apache.wicket.page.PersistentPageManager;
import org.apache.wicket.pageStore.AsynchronousDataStore;
import org.apache.wicket.pageStore.DefaultPageStore;
import org.apache.wicket.pageStore.IDataStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 * A test for page versioning
 * 
 * @author martin-g
 */
public class PageVersioningTest
{
	WicketTester wicketTester;

	/**
	 * setup()
	 */
	@Before
	public void setup()
	{
		final PageVersioningApplication application = new PageVersioningApplication();

		wicketTester = new WicketTester(application)
		{

			/**
			 * @see org.apache.wicket.util.tester.BaseWicketTester#newTestPageManagerProvider()
			 */
			@Override
			protected IPageManagerProvider newTestPageManagerProvider()
			{
				return new IPageManagerProvider()
				{

					public IPageManager get(IPageManagerContext pageManagerContext)
					{

						final IDataStore dataStore = new InMemoryPageStore();
						final AsynchronousDataStore asyncDS = new AsynchronousDataStore(dataStore);
						final DefaultPageStore pageStore = new DefaultPageStore(
							application.getName(), asyncDS, 40);
						return new PersistentPageManager(application.getName(), pageStore,
							pageManagerContext);
					}
				};
			}

		};
	}

	/**
	 * versionPage()
	 */
	@Test
	public void versionPage()
	{
		Page versioningPage = wicketTester.startPage(VersioningTestPage.class);

		assertEquals(0, versioningPage.getPageId());

		wicketTester.clickLink("noopLink");
		assertEquals(0, versioningPage.getPageId());

		wicketTester.clickLink("ajaxUpdatingLink", true);
		assertEquals(0, versioningPage.getPageId());

		wicketTester.clickLink("ajaxUpdatingChangeModelLink", true);
		assertEquals(0, versioningPage.getPageId());

		wicketTester.clickLink("addTemporaryBehaviorLink");
		assertEquals(0, versioningPage.getPageId());

		wicketTester.clickLink("addBehaviorLink");
		assertEquals(1, versioningPage.getPageId());

		wicketTester.clickLink("changeEnabledStateLink");
		assertEquals(2, versioningPage.getPageId());

		wicketTester.clickLink("changeVisibilityStateLink");
		assertEquals(3, versioningPage.getPageId());

		try
		{
			// disable page versioning and execute something that otherwise would create a new
			// version
			versioningPage.setVersioned(false);
			wicketTester.clickLink("changeVisibilityStateLink");
			assertEquals(3, versioningPage.getPageId());
		}
		finally
		{
			versioningPage.setVersioned(true);
		}

		checkPageVersionsAreStored(versioningPage);
	}

	/**
	 * Asserts that there is a version of the page for each operation that modified the page
	 * 
	 * @param versioningPage
	 */
	private void checkPageVersionsAreStored(Page versioningPage)
	{
		IPageManager pageManager = wicketTester.getApplication().getPageManager();

		int lastPageId = versioningPage.getPageId();
		while (lastPageId >= 0)
		{
			assertNotNull(pageManager.getPage(lastPageId));
			lastPageId--;
		}
	}

	private static final class PageVersioningApplication extends WebApplication
	{

		@Override
		public Class<? extends Page> getHomePage()
		{
			return VersioningTestPage.class;
		}

		/**
		 * @see org.apache.wicket.Application#getPageManagerContext()
		 */
		@Override
		public IPageManagerContext getPageManagerContext()
		{
			return super.getPageManagerContext();
		}
	};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15180.java