error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7193.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7193.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7193.java
text:
```scala
A@@pplication.get().getSharedResources();

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

import java.util.Locale;

import junit.framework.TestCase;

import org.apache.wicket.AbortException;
import org.apache.wicket.Application;
import org.apache.wicket.Resource;
import org.apache.wicket.SharedResources;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Tests for package resources.
 * 
 * @author Eelco Hillenius
 */
public class PackageResourceTest extends TestCase
{
	/** mock application object */
	public WebApplication application;

	/**
	 * Construct.
	 */
	public PackageResourceTest()
	{
		super();
	}

	/**
	 * Construct.
	 * 
	 * @param name
	 */
	public PackageResourceTest(String name)
	{
		super(name);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		application = new WicketTester().getApplication();
	}

	/**
	 * Tests binding a single absolute package resource.
	 * 
	 * @throws Exception
	 */
	public void testBindAbsolutePackageResource() throws Exception
	{
		final SharedResources sharedResources = Application.get().getSharedResources();
		PackageResource.bind(application, PackageResourceTest.class, "packaged1.txt");
		assertNotNull("resource packaged1.txt should be available as a packaged resource",
			sharedResources.get(PackageResourceTest.class, "packaged1.txt", null, null, true));
		assertNull("resource packaged2.txt should NOT be available as a packaged resource",
			sharedResources.get(PackageResourceTest.class, "packaged2.txt", null, null, true));
	}

	/**
	 * Tests {@link PackageResourceGuard}.
	 * 
	 * @throws Exception
	 */
	public void testPackageResourceGuard() throws Exception
	{
		PackageResourceGuard guard = new PackageResourceGuard();
		assertTrue(guard.acceptExtension("txt"));
		assertFalse(guard.acceptExtension("java"));
		assertTrue(guard.acceptAbsolutePath("foo/Bar.txt"));
		assertFalse(guard.acceptAbsolutePath("foo/Bar.java"));
		assertTrue(guard.accept(PackageResourceTest.class, "Bar.txt"));
		assertTrue(guard.accept(PackageResourceTest.class, "Bar.txt."));
		assertTrue(guard.accept(PackageResourceTest.class, ".Bar.txt"));
		assertTrue(guard.accept(PackageResourceTest.class, ".Bar.txt."));
		assertTrue(guard.accept(PackageResourceTest.class, ".Bar"));
		assertTrue(guard.accept(PackageResourceTest.class, ".java"));
		assertFalse(guard.accept(PackageResourceTest.class, "Bar.java"));
	}

	/**
	 * Test lenient matching
	 * 
	 * @throws Exception
	 */
	public void testLenientPackageResourceMatching() throws Exception
	{
		final SharedResources sharedResources = Application.get().getSharedResources();
		Resource invalidResource = new PackageResource(PackageResourceTest.class, "packaged3.txt",
			Locale.ENGLISH, null);
		assertNotNull(
			"resource packaged3.txt SHOULD be available as a packaged resource even if it doesn't exist",
			invalidResource);

		assertTrue(PackageResource.exists(PackageResourceTest.class, "packaged1.txt", null, null));
		assertTrue(PackageResource.exists(PackageResourceTest.class, "packaged1.txt", Locale.CHINA,
			null));
		assertTrue(PackageResource.exists(PackageResourceTest.class, "packaged1.txt", Locale.CHINA,
			"foo"));
		assertTrue(PackageResource.exists(PackageResourceTest.class, "packaged1.txt", null, "foo"));
		assertTrue(PackageResource.exists(PackageResourceTest.class, "packaged1_en.txt", null, null));
		assertTrue(PackageResource.exists(PackageResourceTest.class, "packaged1_en_US.txt", null,
			null));
		assertTrue(PackageResource.exists(PackageResourceTest.class, "packaged1_en_US.txt", null,
			"foo"));
		assertTrue(PackageResource.exists(PackageResourceTest.class, "packaged1_en_US.txt",
			Locale.US, null));
		assertTrue(PackageResource.exists(PackageResourceTest.class, "packaged1_en_US.txt",
			Locale.CANADA, null));
		assertTrue(PackageResource.exists(PackageResourceTest.class, "packaged1_en_US.txt",
			Locale.CHINA, null));
		assertTrue(PackageResource.exists(PackageResourceTest.class, "packaged1_foo_bar_en.txt",
			null, null));
		assertTrue(PackageResource.exists(PackageResourceTest.class, "packaged1_foo_bar_en_US.txt",
			null, null));
		assertTrue(PackageResource.exists(PackageResourceTest.class,
			"packaged1_foo_bar_en_US_MAC.txt", null, null));

		try
		{
			invalidResource.getResourceStream();
			fail("Should have raised an AbortException");
		}
		catch (AbortException e)
		{
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7193.java