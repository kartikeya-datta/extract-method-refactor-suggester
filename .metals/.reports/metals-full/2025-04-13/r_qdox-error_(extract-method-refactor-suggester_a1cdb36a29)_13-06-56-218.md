error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10370.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10370.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10370.java
text:
```scala
e@@xecuteBehavior(behavior, "TestDetachPageAjaxResult.html");

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

import org.apache.wicket.ajax.AjaxEventBehavior;

/**
 * Test for ajax handler.
 * 
 * @author Juergen Donnerstag
 */
public class ComponentTest extends WicketTestCase
{
	/**
	 * Construct.
	 * 
	 * @param name
	 */
	public ComponentTest(String name)
	{
		super(name);
	}

	/**
	 * Tests the number of detach calls on a Page, Component, Behavior and Model during a normal
	 * request.
	 * 
	 * @throws Exception
	 */
	public void testDetachPage() throws Exception
	{
		executeTest(TestDetachPage.class, "TestDetachPageExpectedResult.html");
		TestDetachPage page = (TestDetachPage)tester.getLastRenderedPage();
		assertTrue(page.getNrComponentDetachModelCalls() > 0);
		assertTrue(page.getNrComponentDetachModelsCalls() > 0);
		assertTrue(page.getNrComponentOnDetachCalls() > 0);
		assertTrue(page.getNrPageDetachModelCalls() > 0);
		assertTrue(page.getNrPageDetachModelsCalls() > 0);
		assertTrue(page.getNrPageOnDetachCalls() > 0);
		assertTrue(page.getNrModelDetachCalls() > 0);
		assertTrue(page.getNrAjaxBehaviorDetachModelCalls() > 0);
	}

	/**
	 * Tests the number of detach calls on a Page, Component, Behavior and Model during an Ajax
	 * request.
	 * 
	 * @throws Exception
	 */
	public void testDetachPageAjaxRequest() throws Exception
	{
		executeTest(TestDetachPage.class, "TestDetachPageExpectedResult.html");
		TestDetachPage page = (TestDetachPage)tester.getLastRenderedPage();

		assertTrue(page.getNrComponentDetachModelCalls() > 0);
		assertTrue(page.getNrComponentDetachModelsCalls() > 0);
		assertTrue(page.getNrComponentOnDetachCalls() > 0);
		assertTrue(page.getNrPageDetachModelCalls() > 0);
		assertTrue(page.getNrPageDetachModelsCalls() > 0);
		assertTrue(page.getNrPageOnDetachCalls() > 0);
		assertTrue(page.getNrModelDetachCalls() > 0);
		assertTrue(page.getNrAjaxBehaviorDetachModelCalls() > 0);

		AjaxEventBehavior behavior = page.getAjaxBehavior();
		executedBehavior(TestDetachPage.class, behavior, "TestDetachPageAjaxResult.html");
		assertTrue(1 <= page.getNrComponentDetachModelCalls());
		assertTrue(1 <= page.getNrComponentDetachModelsCalls());
		assertTrue(1 <= page.getNrComponentOnDetachCalls());
		assertTrue(1 <= page.getNrPageDetachModelCalls());
		assertTrue(1 <= page.getNrPageDetachModelsCalls());
		assertTrue(1 <= page.getNrPageOnDetachCalls());
		assertTrue(1 <= page.getNrModelDetachCalls());
		assertTrue(1 <= page.getNrAjaxBehaviorDetachModelCalls());
	}

	/**
	 * @throws Exception
	 */
	public void testRenderHomePage_1() throws Exception
	{
		executeTest(TestPage_1.class, "TestPageExpectedResult_1.html");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10370.java