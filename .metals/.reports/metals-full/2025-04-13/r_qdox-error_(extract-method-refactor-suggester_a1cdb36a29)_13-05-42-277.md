error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9648.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9648.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9648.java
text:
```scala
s@@ervice.evict(o1, null);

/*
 * Copyright 2010-2011 the original author or authors.
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

package org.springframework.cache.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Abstract annotation test (containing several reusable methods).
 * 
 * @author Costin Leau
 */
public abstract class AbstractAnnotationTests {

	protected ApplicationContext ctx;

	protected CacheableService cs;

	protected CacheableService ccs;

	protected CacheManager cm;

	protected abstract String getConfig();

	@Before
	public void setup() {
		ctx = new ClassPathXmlApplicationContext(getConfig());
		cs = ctx.getBean("service", CacheableService.class);
		ccs = ctx.getBean("classService", CacheableService.class);
		cm = ctx.getBean(CacheManager.class);
	}

	public void testCacheable(CacheableService service) throws Exception {
		Object o1 = new Object();
		Object o2 = new Object();

		Object r1 = service.cache(o1);
		Object r2 = service.cache(o1);
		Object r3 = service.cache(o1);

		assertSame(r1, r2);
		assertSame(r1, r3);
	}

	public void testInvalidate(CacheableService service) throws Exception {
		Object o1 = new Object();
		Object o2 = new Object();

		Object r1 = service.cache(o1);
		Object r2 = service.cache(o1);

		assertSame(r1, r2);
		service.invalidate(o1);
		Object r3 = service.cache(o1);
		Object r4 = service.cache(o1);
		assertNotSame(r1, r3);
		assertSame(r3, r4);
	}

	public void testInvalidateWKey(CacheableService service) throws Exception {
		Object o1 = new Object();
		Object o2 = new Object();

		Object r1 = service.cache(o1);
		Object r2 = service.cache(o1);

		assertSame(r1, r2);
		service.invalidate(o1, null);
		Object r3 = service.cache(o1);
		Object r4 = service.cache(o1);
		assertNotSame(r1, r3);
		assertSame(r3, r4);
	}

	public void testConditionalExpression(CacheableService service)
			throws Exception {
		Object r1 = service.conditional(4);
		Object r2 = service.conditional(4);

		assertNotSame(r1, r2);

		Object r3 = service.conditional(3);
		Object r4 = service.conditional(3);

		assertSame(r3, r4);
	}

	public void testKeyExpression(CacheableService service) throws Exception {
		Object r1 = service.key(5, 1);
		Object r2 = service.key(5, 2);

		assertSame(r1, r2);

		Object r3 = service.key(1, 5);
		Object r4 = service.key(2, 5);

		assertNotSame(r3, r4);
	}

	public void testNullValue(CacheableService service) throws Exception {
		Object key = new Object();
		assertNull(service.nullValue(key));
		int nr = service.nullInvocations().intValue();
		assertNull(service.nullValue(key));
		assertEquals(nr, service.nullInvocations().intValue());
		assertNull(service.nullValue(new Object()));
		assertEquals(nr + 1, service.nullInvocations().intValue());
	}

	public void testMethodName(CacheableService service, String keyName)
			throws Exception {
		Object key = new Object();
		Object r1 = service.name(key);
		assertSame(r1, service.name(key));
		Cache cache = cm.getCache("default");
		// assert the method name is used
		assertNotNull(cache.get(keyName));
	}

	public void testRootVars(CacheableService service) {
		Object key = new Object();
		Object r1 = service.rootVars(key);
		assertSame(r1, service.rootVars(key));
		Cache cache = cm.getCache("default");
		// assert the method name is used
		String expectedKey = "rootVarsrootVars" + AopProxyUtils.ultimateTargetClass(service) + service;
		assertNotNull(cache.get(expectedKey));
	}

	public void testNullArg(CacheableService service) {
		Object r1 = service.cache(null);
		assertSame(r1, service.cache(null));
	}

	@Test
	public void testCacheable() throws Exception {
		testCacheable(cs);
	}

	@Test
	public void testInvalidate() throws Exception {
		testInvalidate(cs);
	}

	@Test
	public void testInvalidateWithKey() throws Exception {
		testInvalidateWKey(cs);
	}

	@Test
	public void testConditionalExpression() throws Exception {
		testConditionalExpression(cs);
	}

	@Test
	public void testKeyExpression() throws Exception {
		testKeyExpression(cs);
	}

	@Test
	public void testClassCacheCacheable() throws Exception {
		testCacheable(ccs);
	}

	@Test
	public void testClassCacheInvalidate() throws Exception {
		testInvalidate(ccs);
	}

	@Test
	public void testClassCacheInvalidateWKey() throws Exception {
		testInvalidateWKey(ccs);
	}

	@Test
	public void testNullValue() throws Exception {
		testNullValue(cs);
	}

	@Test
	public void testClassNullValue() throws Exception {
		Object key = new Object();
		assertNull(ccs.nullValue(key));
		int nr = ccs.nullInvocations().intValue();
		assertNull(ccs.nullValue(key));
		assertEquals(nr, ccs.nullInvocations().intValue());
		assertNull(ccs.nullValue(new Object()));
		// the check method is also cached
		assertEquals(nr, ccs.nullInvocations().intValue());
		assertEquals(nr + 1, AnnotatedClassCacheableService.nullInvocations
				.intValue());
	}

	@Test
	public void testMethodName() throws Exception {
		testMethodName(cs, "name");
	}

	@Test
	public void testClassMethodName() throws Exception {
		testMethodName(ccs, "namedefault");
	}

	@Test
	public void testRootVars() throws Exception {
		testRootVars(cs);
	}

	@Test
	public void testClassRootVars() throws Exception {
		testRootVars(ccs);
	}

	@Test
	public void testNullArg() throws Exception {
		testNullArg(cs);
	}

	@Test
	public void testClassNullArg() throws Exception {
		testNullArg(ccs);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9648.java