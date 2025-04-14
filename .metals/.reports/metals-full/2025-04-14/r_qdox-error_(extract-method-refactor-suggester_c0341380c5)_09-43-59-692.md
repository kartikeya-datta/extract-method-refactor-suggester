error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1039.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1039.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1039.java
text:
```scala
A@@ssert.fail("IllegalStateException should be thrown");

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.resource;

import java.util.Locale;

import wicket.Component;
import wicket.markup.html.panel.Panel;
import wicket.resource.ComponentStringResourceLoader;
import wicket.resource.IStringResourceLoader;

import junit.framework.Assert;

/**
 * Test case for the <code>ComponentStringResourceLoader</code> class.
 * @author Chris Turner
 */
public class ComponentStringResourceLoaderTest extends StringResourceLoaderTestBase
{
	/**
	 * Create the test case.
	 * @param message The test name
	 */
	public ComponentStringResourceLoaderTest(String message)
	{
		super(message);
	}

	/**
	 * Create and return the loader instance
	 * @return The loader instance to test
	 */
	protected IStringResourceLoader createLoader()
	{
		return new ComponentStringResourceLoader();
	}

	/**
	 * @see wicket.resource.StringResourceLoaderTestBase#testLoaderUnknownResources()
	 */
	public void testLoaderUnknownResources()
	{
		Component c = new DummyComponent("hello", application)
		{
		};
		DummyPage page = new DummyPage();
		page.add(c);
		IStringResourceLoader loader = new ComponentStringResourceLoader();
		Assert.assertNull("Missing resource should return null", loader.loadStringResource(c, "test.string",
				Locale.getDefault(), null));
	}

	/**
	 * 
	 */
	public void testNullComponent()
	{
		Assert.assertNull("Null component should skip resource load", loader.loadStringResource(null,
				"test.string", Locale.getDefault(), null));
	}

	/**
	 * 
	 */
	public void testNonPageComponent()
	{
		Component c = new DummyComponent("hello", application)
		{
		};
		IStringResourceLoader loader = new ComponentStringResourceLoader();
		try
		{
			loader.loadStringResource(c, "test.string", Locale.getDefault(), null);
			Assert.fail("InvalidResourceSpecificationException should be thrown");
		}
		catch (IllegalStateException e)
		{
			// Expected result since component is not attached to a Page
		}
	}

	/**
	 * 
	 */
	public void testPageEmbeddedComponentLoadFromPage()
	{
		DummyPage p = new DummyPage();
		DummyComponent c = new DummyComponent("hello", application);
		p.add(c);
		IStringResourceLoader loader = new ComponentStringResourceLoader();
		Assert.assertEquals("Valid resourse string should be found", "Another string", loader.loadStringResource(
				c, "another.test.string", Locale.getDefault(), null));
	}

	/**
	 * 
	 */
	public void testMultiLevelEmbeddedComponentLoadFromComponent()
	{
		DummyPage p = new DummyPage();
		Panel panel = new Panel("panel");
		p.add(panel);
		DummyComponent c = new DummyComponent("hello", application);
		panel.add(c);
		IStringResourceLoader loader = new ComponentStringResourceLoader();
		Assert.assertEquals("Valid resourse string should be found", "Component string", loader
				.loadStringResource(c, "component.string", Locale.getDefault(), null));
	}

	/**
	 * 
	 */
	public void testLoadDirectFromPage()
	{
		DummyPage p = new DummyPage();
		IStringResourceLoader loader = new ComponentStringResourceLoader();
		Assert.assertEquals("Valid resourse string should be found", "Another string", loader.loadStringResource(
				p, "another.test.string", Locale.getDefault(), null));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1039.java