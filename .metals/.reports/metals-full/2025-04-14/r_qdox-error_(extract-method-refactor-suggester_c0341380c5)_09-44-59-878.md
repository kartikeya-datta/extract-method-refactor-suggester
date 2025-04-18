error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8276.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8276.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8276.java
text:
```scala
I@@FrameworkSettings settings = new FrameworkSettings(new MockApplication());

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

import static org.junit.Assert.assertEquals;

import java.util.List;

import junit.framework.Assert;

import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.apache.wicket.resource.loader.BundleStringResourceLoader;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.resource.loader.ComponentStringResourceLoader;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.resource.loader.PackageStringResourceLoader;
import org.apache.wicket.resource.loader.ValidatorStringResourceLoader;
import org.apache.wicket.settings.IFrameworkSettings;
import org.apache.wicket.settings.IResourceSettings;
import org.apache.wicket.settings.def.FrameworkSettings;
import org.apache.wicket.settings.def.ResourceSettings;
import org.junit.After;
import org.junit.Test;

/**
 * Test cases for the <code>ApplicationSettings</code> class.
 * 
 * @author Chris Turner
 */
public class ApplicationSettingsTest
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
	@Test
	public void testFrameworkVersion()
	{
		IFrameworkSettings settings = new FrameworkSettings();
		assertEquals("n/a", settings.getVersion());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testExceptionOnMissingResourceDefaultValue() throws Exception
	{
		IResourceSettings settings = new ResourceSettings(new MockApplication());
		Assert.assertTrue("exceptionOnMissingResource should default to true",
			settings.getThrowExceptionOnMissingResource());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testExceptionOnMissingResourceSetsCorrectly() throws Exception
	{
		IResourceSettings settings = new ResourceSettings(new MockApplication());
		settings.setThrowExceptionOnMissingResource(false);
		Assert.assertFalse("exceptionOnMissingResource should have been set to false",
			settings.getThrowExceptionOnMissingResource());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testUseDefaultOnMissingResourceDefaultValue() throws Exception
	{
		IResourceSettings settings = new ResourceSettings(new MockApplication());
		Assert.assertTrue("useDefaultOnMissingResource should default to true",
			settings.getUseDefaultOnMissingResource());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testUseDefaultOnMissingResourceSetsCorrectly() throws Exception
	{
		IResourceSettings settings = new ResourceSettings(new MockApplication());
		settings.setUseDefaultOnMissingResource(false);
		Assert.assertFalse("useDefaultOnMissingResource should have been set to false",
			settings.getUseDefaultOnMissingResource());
	}

	/**
	 * 
	 */
	@Test
	public void testDefaultStringResourceLoaderSetup()
	{
		IResourceSettings settings = new ResourceSettings(new MockApplication());
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
	@Test
	public void testOverrideStringResourceLoaderSetup()
	{
		IResourceSettings settings = new ResourceSettings(new MockApplication());
		settings.getStringResourceLoaders().clear();
		settings.getStringResourceLoaders().add(
			new BundleStringResourceLoader("org.apache.wicket.resource.DummyResources"));
		settings.getStringResourceLoaders().add(new ComponentStringResourceLoader());
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
	@Test
	public void testLocalizer()
	{
		MockApplication dummy = new MockApplication();
		dummy.setName("test-app");
		dummy.setServletContext(new MockServletContext(dummy, ""));
		ThreadContext.setApplication(dummy);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8276.java