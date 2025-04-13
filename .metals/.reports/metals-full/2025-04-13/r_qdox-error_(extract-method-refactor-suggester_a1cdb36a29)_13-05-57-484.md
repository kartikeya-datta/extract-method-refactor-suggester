error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5488.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5488.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5488.java
text:
```scala
M@@etaDataEntry<?>[] md = KEY1.set(null, "1");

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

import junit.framework.TestCase;

/**
 * Some tests for meta data.
 */
public class MetaDataTest extends TestCase
{
	private static final MetaDataKey<String> KEY1 = new MetaDataKey<String>()
	{
		private static final long serialVersionUID = 1L;
	};

	private static final MetaDataKey<String> KEY2 = new MetaDataKey<String>()
	{
		private static final long serialVersionUID = 1L;
	};

	private static final MetaDataKey<String> KEY3 = new MetaDataKey<String>()
	{
		private static final long serialVersionUID = 1L;
	};

	private static final MetaDataKey<String> KEY4 = new MetaDataKey<String>()
	{
		private static final long serialVersionUID = 1L;
	};

	/**
	 * Construct.
	 */
	public MetaDataTest()
	{
	}

	/**
	 * Construct.
	 * 
	 * @param name
	 */
	public MetaDataTest(String name)
	{
		super(name);
	}

	/**
	 * Test bounds and basic operations.
	 */
	public void testMetaDataKey()
	{
		MetaDataEntry[] md = KEY1.set(null, "1");
		assertNotNull(md);
		assertEquals(1, md.length);
		md = KEY1.set(md, null);
		assertNull(md);
		md = KEY1.set(md, "1");
		md = KEY2.set(md, "2");
		md = KEY3.set(md, "3");
		md = KEY4.set(md, "4");
		assertEquals(4, md.length);
		md = KEY3.set(md, null);
		assertEquals(3, md.length);
		assertEquals("1", KEY1.get(md));
		assertEquals("2", KEY2.get(md));
		assertEquals(null, KEY3.get(md));
		assertEquals("4", KEY4.get(md));
		md = KEY4.set(md, null);
		assertEquals(2, md.length);
		assertEquals("1", KEY1.get(md));
		assertEquals("2", KEY2.get(md));
		assertEquals(null, KEY3.get(md));
		assertEquals(null, KEY4.get(md));
		md = KEY1.set(md, null);
		assertEquals(1, md.length);
		assertEquals(null, KEY1.get(md));
		assertEquals("2", KEY2.get(md));
		assertEquals(null, KEY3.get(md));
		assertEquals(null, KEY4.get(md));
		md = KEY2.set(md, null);
		assertNull(md);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5488.java