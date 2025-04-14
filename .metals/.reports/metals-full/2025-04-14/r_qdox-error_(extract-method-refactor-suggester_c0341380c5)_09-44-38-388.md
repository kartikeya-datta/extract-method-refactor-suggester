error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15105.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15105.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15105.java
text:
```scala
T@@hreadContext.setApplication(dummy);

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

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.apache.wicket.resource.loader.BundleStringResourceLoader;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.resource.loader.ComponentStringResourceLoader;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.resource.loader.PackageStringResourceLoader;
import org.apache.wicket.resource.loader.ValidatorStringResourceLoader;
import org.apache.wicket.settings.Settings;
import org.junit.After;

/**
 * Test cases for the <code>ApplicationSettings</code> class.
 * 
 * @author Chris Turner
 */
public class ApplicationSettingsTest extends TestCase
{

	/**
	 * detaches thread context
	 */
	@After
	public void detachThreadContext()
	{
		ThreadContext.detach();
	}

	/**
	 * 
	 */
	public void testFrameworkVersion()
	{
		Settings settings = new Settings(new MockApplication());
		assertEquals("n/a", settings.getVersion());
	}

	/**
	 * @throws Exception
	 */
	public void testExceptionOnMissingResourceDefaultValue() throws Exception
	{
		Settings settings = new Settings(new MockApplication());
		Assert.assertTrue("exceptionOnMissingResource should default to true",
			settings.getThrowExceptionOnMissingResource());
	}

	/**
	 * @throws Exception
	 */
	public void testExceptionOnMissingResourceSetsCorrectly() throws Exception
	{
		Settings settings = new Settings(new MockApplication());
		settings.setThrowExceptionOnMissingResource(false);
		Assert.assertFalse("exceptionOnMissingResource should have been set to false",
			settings.getThrowExceptionOnMissingResource());
	}

	/**
	 * @throws Exception
	 */
	public void testUseDefaultOnMissingResourceDefaultValue() throws Exception
	{
		Settings settings = new Settings(new MockApplication());
		Assert.assertTrue("useDefaultOnMissingResource should default to true",
			settings.getUseDefaultOnMissingResource());
	}

	/**
	 * @throws Exception
	 */
	public void testUseDefaultOnMissingResourceSetsCorrectly() throws Exception
	{
		Settings settings = new Settings(new MockApplication());
		settings.setUseDefaultOnMissingResource(false);
		Assert.assertFalse("useDefaultOnMissingResource should have been set to false",
			settings.getUseDefaultOnMissingResource());
	}

	/**
	 * 
	 */
	public void testDefaultStringResourceLoaderSetup()
	{
		Settings settings = new Settings(new MockApplication());
		List<IStringResourceLoader> loaders = settings.getStringResourceLoaders();
		Assert.assertEquals("There should be 4 default loaders", 4, loaders.size());
		Assert.assertTrue("First loader one should be the component one",
			loaders.get(0) instanceof ComponentStringResourceLoader);
		Assert.assertTrue("Second loader should be the application one",
			loaders.get(1) instanceof PackageStringResourceLoader);
		Assert.assertTrue("Third loader should be the application one",
			loaders.get(2) instanceof ClassStringResourceLoader);
		Assert.assertTrue("Fourth loader should be the validator one",
			loaders.get(3) instanceof ValidatorStringResourceLoader);
	}

	/**
	 * 
	 */
	public void testOverrideStringResourceLoaderSetup()
	{
		Settings settings = new Settings(new MockApplication());
		settings.getStringResourceLoaders().clear();
		settings.addStringResourceLoader(new BundleStringResourceLoader(
			"org.apache.wicket.resource.DummyResources"));
		settings.addStringResourceLoader(new ComponentStringResourceLoader());
		List<IStringResourceLoader> loaders = settings.getStringResourceLoaders();
		Assert.assertEquals("There should be 2 overridden loaders", 2, loaders.size());
		Assert.assertTrue("First loader one should be the bundle one",
			loaders.get(0) instanceof BundleStringResourceLoader);
		Assert.assertTrue("Second loader should be the component one",
			loaders.get(1) instanceof ComponentStringResourceLoader);
	}

	/**
	 * 
	 */
	public void testLocalizer()
	{
		MockApplication dummy = new MockApplication();
		dummy.setName("test-app");
		dummy.setServletContext(new MockServletContext(dummy, ""));
		dummy.set();
		dummy.initApplication();
		Localizer localizer = dummy.getResourceSettings().getLocalizer();
		Assert.assertNotNull("Localizer should be available", localizer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15105.java