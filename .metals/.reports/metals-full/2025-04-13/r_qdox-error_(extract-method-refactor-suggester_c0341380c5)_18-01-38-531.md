error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7221.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7221.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7221.java
text:
```scala
a@@ssertNotNull("String does not extend Page. Should have thrown an exception", e);

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
package wicket;

import wicket.session.DefaultPageFactory;


/**
 * Test the Pagefactory
 */
public class PageFactoryTest extends WicketTestCase
{
	private DefaultPageFactory factory;

	/**
	 * Create the test case.
	 * 
	 * @param message
	 *            The test name
	 */
	public PageFactoryTest(String message)
	{
		super(message);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		factory = new DefaultPageFactory();
	}

	/**
	 * Test creating a new page using a class.
	 */
	public void testNewPageClass()
	{
		// MyPage0: no constructor at all
		assertEquals(MyPage0.class, factory.newPage(MyPage0.class).getClass());

		// MyPage1 has only a default constructor
		assertEquals(MyPage1.class, factory.newPage(MyPage1.class).getClass());

		// MyPage2: PageParameter parameter constructor only
		// will call PageParameter constructor with parameter = null
		assertEquals(MyPage2.class, factory.newPage(MyPage2.class, (PageParameters)null).getClass());

		// MyPage3: Page parameter constructor only
		Exception e = null;
		try
		{
			factory.newPage(MyPage3.class).getClass();
		}
		catch (WicketRuntimeException ex)
		{
			e = ex;
		}
		assertNotNull(
				"MyPage3 should have thrown an exception as it does not have a default or no constructor",
				e);

		// MyPage4: Illegal String parameter constructor only
		e = null;
		try
		{
			factory.newPage(MyPage4.class).getClass();
		}
		catch (WicketRuntimeException ex)
		{
			e = ex;
		}
		assertNotNull(
				"MyPage4 should have thrown an exception as it does not have a default or no constructor",
				e);

		// MyPage5: PageParameter and default constructor
		assertEquals(MyPage5.class, factory.newPage(MyPage5.class).getClass());

		// String: Illegal String parameter constructor only
		e = null; 
		try 
		{ 
		    factory.newPage(String.class).getClass(); 
		} 
		catch (ClassCastException ex) 
		{ 
		    e = ex; 
		}
		
		assertNotNull("String does not extend Page. Should habe thrown an exception", e);
	}

	/**
	 * Test a new page using a class and page parameters.
	 */
	public void testNewPageClassPageParameters()
	{
		assertEquals(MyPage0.class, factory.newPage(MyPage0.class, (PageParameters)null).getClass());

		// MyPage0: no constructor at all
		assertEquals(MyPage0.class, factory.newPage(MyPage0.class, new PageParameters()).getClass());

		// MyPage1 has only a default constructor
		assertEquals(MyPage1.class, factory.newPage(MyPage1.class, new PageParameters()).getClass());

		// MyPage2: PageParameter parameter constructor only
		assertEquals(MyPage2.class, factory.newPage(MyPage2.class, new PageParameters()).getClass());

		// MyPage3: Page parameter constructor only
		Exception e = null;
		try
		{
			factory.newPage(MyPage3.class, new PageParameters()).getClass();
		}
		catch (WicketRuntimeException ex)
		{
			e = ex;
		}
		assertNotNull(
				"MyPage4 should have thrown an exception as it does not have a default or no constructor",
				e);

		// MyPage4: Illegal String parameter constructor only
		e = null;
		try
		{
			factory.newPage(MyPage4.class, new PageParameters()).getClass();
		}
		catch (WicketRuntimeException ex)
		{
			e = ex;
		}
		assertNotNull(
				"MyPage4 should have thrown an exception as it does not have a default or no constructor",
				e);

		// MyPage5: PageParameter and default constructor
		assertEquals(MyPage5.class, factory.newPage(MyPage5.class, new PageParameters()).getClass());

		// String: Illegal String parameter constructor only
		e = null;
		try 
		{ 
		    factory.newPage(String.class, new PageParameters()).getClass(); 
		} 
		catch (ClassCastException ex) 
		{ 
		    e = ex; 
		} 
		
		assertNotNull("String does not extend Page. Should habe thrown an exception", e);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7221.java