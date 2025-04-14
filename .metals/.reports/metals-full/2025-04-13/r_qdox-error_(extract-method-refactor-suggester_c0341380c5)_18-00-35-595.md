error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1168.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1168.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 664
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1168.java
text:
```scala
public class ExpressionEvalutatorTest {

/*
 * Copyright 2002-2013 the original author or authors.
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

p@@ackage org.springframework.cache.interceptor;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ReflectionUtils;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @author Costin Leau
 * @author Phillip Webb
 */
public class ExpressionEvaluatorTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private ExpressionEvaluator eval = new ExpressionEvaluator();

	private AnnotationCacheOperationSource source = new AnnotationCacheOperationSource();

	private Collection<CacheOperation> getOps(String name) {
		Method method = ReflectionUtils.findMethod(AnnotatedClass.class, name, Object.class, Object.class);
		return source.getCacheOperations(method, AnnotatedClass.class);
	}

	@Test
	public void testMultipleCachingSource() throws Exception {
		Collection<CacheOperation> ops = getOps("multipleCaching");
		assertEquals(2, ops.size());
		Iterator<CacheOperation> it = ops.iterator();
		CacheOperation next = it.next();
		assertTrue(next instanceof CacheableOperation);
		assertTrue(next.getCacheNames().contains("test"));
		assertEquals("#a", next.getKey());
		next = it.next();
		assertTrue(next instanceof CacheableOperation);
		assertTrue(next.getCacheNames().contains("test"));
		assertEquals("#b", next.getKey());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testMultipleCachingEval() throws Exception {
		AnnotatedClass target = new AnnotatedClass();
		Method method = ReflectionUtils.findMethod(AnnotatedClass.class, "multipleCaching", Object.class,
				Object.class);
		Object[] args = new Object[] { new Object(), new Object() };
		Collection<Cache> map = Collections.singleton(new ConcurrentMapCache("test"));

		EvaluationContext evalCtx = eval.createEvaluationContext(map, method, args, target, target.getClass());
		Collection<CacheOperation> ops = getOps("multipleCaching");

		Iterator<CacheOperation> it = ops.iterator();

		Object keyA = eval.key(it.next().getKey(), method, evalCtx);
		Object keyB = eval.key(it.next().getKey(), method, evalCtx);

		assertEquals(args[0], keyA);
		assertEquals(args[1], keyB);
	}

	@Test
	public void withReturnValue() throws Exception {
		EvaluationContext context = createEvaluationContext("theResult");
		Object value = new SpelExpressionParser().parseExpression("#result").getValue(context);
		assertThat(value, equalTo((Object) "theResult"));
	}

	@Test
	public void withNullReturn() throws Exception {
		EvaluationContext context = createEvaluationContext(null);
		Object value = new SpelExpressionParser().parseExpression("#result").getValue(context);
		assertThat(value, nullValue());
	}

	@Test
	public void withoutReturnValue() throws Exception {
		EvaluationContext context = createEvaluationContext(ExpressionEvaluator.NO_RESULT);
		Object value = new SpelExpressionParser().parseExpression("#result").getValue(context);
		assertThat(value, nullValue());
	}

	private EvaluationContext createEvaluationContext(Object result) {
		AnnotatedClass target = new AnnotatedClass();
		Method method = ReflectionUtils.findMethod(AnnotatedClass.class, "multipleCaching", Object.class,
				Object.class);
		Object[] args = new Object[] { new Object(), new Object() };
		@SuppressWarnings("unchecked")
		Collection<Cache> map = Collections.singleton(new ConcurrentMapCache("test"));
		EvaluationContext context = eval.createEvaluationContext(map, method, args, target, target.getClass(), result);
		return context;
	}

	private static class AnnotatedClass {
		@Caching(cacheable = { @Cacheable(value = "test", key = "#a"), @Cacheable(value = "test", key = "#b") })
		public void multipleCaching(Object a, Object b) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1168.java