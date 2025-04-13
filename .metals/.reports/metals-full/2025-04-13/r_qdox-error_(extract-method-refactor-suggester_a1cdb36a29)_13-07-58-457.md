error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13631.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13631.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13631.java
text:
```scala
n@@ew Class<?>[] {Map.class},

/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springframework.ui;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.TestBean;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * @author Rick Evans
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public final class ModelMapTests {

	@Test
	public void testNoArgCtorYieldsEmptyModel() throws Exception {
		assertEquals(0, new ModelMap().size());
	}

	/*
	 * SPR-2185 - Null model assertion causes backwards compatibility issue
	 */
	@Test
	public void testAddNullObjectWithExplicitKey() throws Exception {
		ModelMap model = new ModelMap();
		model.addAttribute("foo", null);
		assertTrue(model.containsKey("foo"));
		assertNull(model.get("foo"));
	}

	/*
	 * SPR-2185 - Null model assertion causes backwards compatibility issue
	 */
	@Test
	public void testAddNullObjectViaCtorWithExplicitKey() throws Exception {
		ModelMap model = new ModelMap("foo", null);
		assertTrue(model.containsKey("foo"));
		assertNull(model.get("foo"));
	}

	@Test
	public void testNamedObjectCtor() throws Exception {
		ModelMap model = new ModelMap("foo", "bing");
		assertEquals(1, model.size());
		String bing = (String) model.get("foo");
		assertNotNull(bing);
		assertEquals("bing", bing);
	}

	@Test
	public void testUnnamedCtorScalar() throws Exception {
		ModelMap model = new ModelMap("foo", "bing");
		assertEquals(1, model.size());
		String bing = (String) model.get("foo");
		assertNotNull(bing);
		assertEquals("bing", bing);
	}

	@Test
	public void testOneArgCtorWithScalar() throws Exception {
		ModelMap model = new ModelMap("bing");
		assertEquals(1, model.size());
		String string = (String) model.get("string");
		assertNotNull(string);
		assertEquals("bing", string);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testOneArgCtorWithNull() {
		//Null model arguments added without a name being explicitly supplied are not allowed
		new ModelMap(null);
	}

	@Test
	public void testOneArgCtorWithCollection() throws Exception {
		ModelMap model = new ModelMap(new String[]{"foo", "boing"});
		assertEquals(1, model.size());
		String[] strings = (String[]) model.get("stringList");
		assertNotNull(strings);
		assertEquals(2, strings.length);
		assertEquals("foo", strings[0]);
		assertEquals("boing", strings[1]);
	}

	@Test
	public void testOneArgCtorWithEmptyCollection() throws Exception {
		ModelMap model = new ModelMap(new HashSet<Object>());
		// must not add if collection is empty...
		assertEquals(0, model.size());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddObjectWithNull() throws Exception {
		// Null model arguments added without a name being explicitly supplied are not allowed
		ModelMap model = new ModelMap();
		model.addAttribute(null);
	}

	@Test
	public void testAddObjectWithEmptyArray() throws Exception {
		ModelMap model = new ModelMap(new int[]{});
		assertEquals(1, model.size());
		int[] ints = (int[]) model.get("intList");
		assertNotNull(ints);
		assertEquals(0, ints.length);
	}

	@Test
	public void testAddAllObjectsWithNullMap() throws Exception {
		ModelMap model = new ModelMap();
		model.addAllAttributes((Map<String, ?>) null);
		assertEquals(0, model.size());
	}

	@Test
	public void testAddAllObjectsWithNullCollection() throws Exception {
		ModelMap model = new ModelMap();
		model.addAllAttributes((Collection<Object>) null);
		assertEquals(0, model.size());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddAllObjectsWithSparseArrayList() throws Exception {
		// Null model arguments added without a name being explicitly supplied are not allowed
		ModelMap model = new ModelMap();
		ArrayList<String> list = new ArrayList<String>();
		list.add("bing");
		list.add(null);
		model.addAllAttributes(list);
	}

	@Test
	public void testAddMap() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("one", "one-value");
		map.put("two", "two-value");
		ModelMap model = new ModelMap();
		model.addAttribute(map);
		assertEquals(1, model.size());
		String key = StringUtils.uncapitalize(ClassUtils.getShortName(map.getClass()));
		assertTrue(model.containsKey(key));
	}

	@Test
	public void testAddObjectNoKeyOfSameTypeOverrides() throws Exception {
		ModelMap model = new ModelMap();
		model.addAttribute("foo");
		model.addAttribute("bar");
		assertEquals(1, model.size());
		String bar = (String) model.get("string");
		assertEquals("bar", bar);
	}

	@Test
	public void testAddListOfTheSameObjects() throws Exception {
		List<TestBean> beans = new ArrayList<TestBean>();
		beans.add(new TestBean("one"));
		beans.add(new TestBean("two"));
		beans.add(new TestBean("three"));
		ModelMap model = new ModelMap();
		model.addAllAttributes(beans);
		assertEquals(1, model.size());
	}

	@Test
	public void testMergeMapWithOverriding() throws Exception {
		Map<String, TestBean> beans = new HashMap<String, TestBean>();
		beans.put("one", new TestBean("one"));
		beans.put("two", new TestBean("two"));
		beans.put("three", new TestBean("three"));
		ModelMap model = new ModelMap();
		model.put("one", new TestBean("oneOld"));
		model.mergeAttributes(beans);
		assertEquals(3, model.size());
		assertEquals("oneOld", ((TestBean) model.get("one")).getName());
	}

	@Test
	public void testInnerClass() throws Exception {
		ModelMap map = new ModelMap();
		SomeInnerClass inner = new SomeInnerClass();
		map.addAttribute(inner);
		assertSame(inner, map.get("someInnerClass"));
	}

	@Test
	public void testInnerClassWithTwoUpperCaseLetters() throws Exception {
		ModelMap map = new ModelMap();
		UKInnerClass inner = new UKInnerClass();
		map.addAttribute(inner);
		assertSame(inner, map.get("UKInnerClass"));
	}

	@Test
	public void testAopCglibProxy() throws Exception {
		ModelMap map = new ModelMap();
		ProxyFactory factory = new ProxyFactory();
		Date date = new Date();
		factory.setTarget(date);
		factory.setProxyTargetClass(true);
		map.addAttribute(factory.getProxy());
		assertTrue(map.containsKey("date"));
		assertEquals(date, map.get("date"));
	}

	@Test
	public void testAopJdkProxy() throws Exception {
		ModelMap map = new ModelMap();
		ProxyFactory factory = new ProxyFactory();
		Map<?, ?> target = new HashMap<Object, Object>();
		factory.setTarget(target);
		factory.addInterface(Map.class);
		Object proxy = factory.getProxy();
		map.addAttribute(proxy);
		assertSame(proxy, map.get("map"));
	}

	@Test
	public void testAopJdkProxyWithMultipleInterfaces() throws Exception {
		ModelMap map = new ModelMap();
		Map<?, ?> target = new HashMap<Object, Object>();
		ProxyFactory factory = new ProxyFactory();
		factory.setTarget(target);
		factory.addInterface(Serializable.class);
		factory.addInterface(Cloneable.class);
		factory.addInterface(Comparable.class);
		factory.addInterface(Map.class);
		Object proxy = factory.getProxy();
		map.addAttribute(proxy);
		assertSame(proxy, map.get("map"));
	}

	@Test
	public void testAopJdkProxyWithDetectedInterfaces() throws Exception {
		ModelMap map = new ModelMap();
		Map<?, ?> target = new HashMap<Object, Object>();
		ProxyFactory factory = new ProxyFactory(target);
		Object proxy = factory.getProxy();
		map.addAttribute(proxy);
		assertSame(proxy, map.get("map"));
	}

	@Test
	public void testRawJdkProxy() throws Exception {
		ModelMap map = new ModelMap();
		Object proxy = Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[] {Map.class},
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) {
						return "proxy";
					}
				});
		map.addAttribute(proxy);
		assertSame(proxy, map.get("map"));
	}


	private static class SomeInnerClass {
	}


	private static class UKInnerClass {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13631.java