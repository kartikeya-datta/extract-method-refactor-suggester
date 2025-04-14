error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3235.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3235.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3235.java
text:
```scala
t@@his.application = new WicketTester(new DummyApplication()).getApplication();

/*
 * $Id: StringResourceLoaderTestBase.java 5844 2006-05-24 20:53:56 +0000 (Wed,
 * 24 May 2006) joco01 $ $Revision$ $Date: 2006-05-24 20:53:56 +0000
 * (Wed, 24 May 2006) $
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.resource;

import java.util.Locale;

import junit.framework.Assert;
import junit.framework.TestCase;
import wicket.Application;
import wicket.Component;
import wicket.MockPageWithOneComponent;
import wicket.Page;
import wicket.resource.loader.IStringResourceLoader;
import wicket.util.tester.WicketTester;

/**
 * Abstract base class providing common test functionality to ensure that all
 * loader implementations comply with the contract of the loader interface.
 * 
 * @author Chris Turner
 */
public abstract class StringResourceLoaderTestBase extends TestCase
{
	// The loader to test
	protected IStringResourceLoader loader;

	// The dummy tester
	protected Application application;

	// The dummy component
	protected Component component;

	/**
	 * Create the test case.
	 * 
	 * @param message
	 *            The name of the test
	 */
	protected StringResourceLoaderTestBase(String message)
	{
		super(message);
	}

	/**
	 * Abstract method to create the loader instance to be tested.
	 * 
	 * @return The loader instance to test
	 */
	protected abstract IStringResourceLoader createLoader();

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		
		this.application = new WicketTester().getApplication();
		Page page = new MockPageWithOneComponent();
		this.component = new DummyComponent(page, "component", this.application);
		this.loader = createLoader();
	}

	/**
	 * 
	 */
	public void testLoaderValidKeyNoStyleDefaultLocale()
	{
		String s = loader.loadStringResource(component.getClass(), "test.string", Locale
				.getDefault(), null);
		Assert.assertEquals("Resource should be loaded", "This is a test", s);

		// And do it again to ensure caching path is exercised
		s = loader.loadStringResource(component.getClass(), "test.string", Locale.getDefault(),
				null);
		Assert.assertEquals("Resource should be loaded", "This is a test", s);
	}

	/**
	 * 
	 */
	public void testLoaderInvalidKeyNoStyleDefaultLocale()
	{
		Assert.assertNull("Missing key should return null", loader.loadStringResource(component
				.getClass(), "unknown.string", Locale.getDefault(), null));
	}

	/**
	 * 
	 */
	public void testLoaderValidKeyNoStyleAlternativeLocale()
	{
		String s = loader.loadStringResource(component.getClass(), "test.string", new Locale("zz"),
				null);
		Assert.assertEquals("Resource should be loaded", "Flib flob", s);
	}

	/**
	 * 
	 */
	public void testLoaderInvalidKeyNoStyleAlternativeLocale()
	{
		Assert.assertNull("Missing key should return null", loader.loadStringResource(component
				.getClass(), "unknown.string", new Locale("zz"), null));
	}

	/**
	 * 
	 */
	public void testLoaderValidKeyStyleNoLocale()
	{
		String s = loader.loadStringResource(component.getClass(), "test.string", null, "alt");
		Assert.assertEquals("Resource should be loaded", "Alt test string", s);
	}

	/**
	 * 
	 */
	public abstract void testLoaderUnknownResources();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3235.java