error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13831.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13831.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13831.java
text:
```scala
I@@nteger.class, null, Set.class, Set.class, null, Integer.class,

/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.core;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.springframework.beans.GenericBean;
import org.springframework.core.io.Resource;

/**
 * @author Serge Bogatyrjov
 * @author Juergen Hoeller
 */
public class GenericCollectionTypeResolverTests extends AbstractGenericsTests {

	protected void setUp() throws Exception {
		this.targetClass = Foo.class;
		this.methods = new String[] {"a", "b", "b2", "b3", "c", "d", "d2", "d3", "e", "e2", "e3"};
		this.expectedResults = new Class[] {
			Integer.class, null, null, Set.class, null, Integer.class,
			Integer.class, Integer.class, Integer.class, Integer.class, Integer.class};
	}

	protected Type getType(Method method) {
		return GenericCollectionTypeResolver.getMapValueReturnType(method);
	}

	public void testA() throws Exception {
		executeTest();
	}

	public void testB() throws Exception {
		executeTest();
	}

	public void testB2() throws Exception {
		executeTest();
	}

	public void testB3() throws Exception {
		executeTest();
	}

	public void testC() throws Exception {
		executeTest();
	}

	public void testD() throws Exception {
		executeTest();
	}

	public void testD2() throws Exception {
		executeTest();
	}

	public void testD3() throws Exception {
		executeTest();
	}

	public void testE() throws Exception {
		executeTest();
	}

	public void testE2() throws Exception {
		executeTest();
	}

	public void testE3() throws Exception {
		executeTest();
	}

	public void testProgrammaticListIntrospection() throws Exception {
		Method setter = GenericBean.class.getMethod("setResourceList", List.class);
		Assert.assertEquals(Resource.class,
				GenericCollectionTypeResolver.getCollectionParameterType(new MethodParameter(setter, 0)));

		Method getter = GenericBean.class.getMethod("getResourceList");
		Assert.assertEquals(Resource.class,
				GenericCollectionTypeResolver.getCollectionReturnType(getter));
	}

	public void testClassResolution() {
		assertEquals(String.class, GenericCollectionTypeResolver.getCollectionType(CustomSet.class));
		assertEquals(String.class, GenericCollectionTypeResolver.getMapKeyType(CustomMap.class));
		assertEquals(Integer.class, GenericCollectionTypeResolver.getMapValueType(CustomMap.class));
	}


	private abstract class CustomSet<T> extends AbstractSet<String> {
	}


	private abstract class CustomMap<T> extends AbstractMap<String, Integer> {
	}


	private abstract class OtherCustomMap<T> implements Map<String, Integer> {
	}


	private interface Foo {

		Map<String, Integer> a();

		Map<?, ?> b();

		Map<?, ? extends Set> b2();

		Map<?, ? super Set> b3();

		Map c();

		CustomMap<Date> d();

		CustomMap<?> d2();

		CustomMap d3();

		OtherCustomMap<Date> e();

		OtherCustomMap<?> e2();

		OtherCustomMap e3();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13831.java