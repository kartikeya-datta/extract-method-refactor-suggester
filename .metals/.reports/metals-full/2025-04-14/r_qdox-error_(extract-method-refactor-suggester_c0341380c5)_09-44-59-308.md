error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14631.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14631.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 706
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14631.java
text:
```scala
public class AnnotatedClassCacheableService implements CacheableService<Object> {

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

p@@ackage org.springframework.cache.config;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.cache.annotation.CacheDefinitions;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author Costin Leau
 */
@Cacheable("default")
public class AnnotatedClassCacheableService implements CacheableService {

	private final AtomicLong counter = new AtomicLong();
	public static final AtomicLong nullInvocations = new AtomicLong();

	public Object cache(Object arg1) {
		return counter.getAndIncrement();
	}

	public Object conditional(int field) {
		return null;
	}

	@CacheEvict("default")
	public void invalidate(Object arg1) {
	}

	@CacheEvict(value = "default", key = "#p0")
	public void evict(Object arg1, Object arg2) {
	}

	@Cacheable(value = "default", key = "#p0")
	public Object key(Object arg1, Object arg2) {
		return counter.getAndIncrement();
	}

	@Cacheable(value = "default", key = "#root.methodName + #root.caches[0].name")
	public Object name(Object arg1) {
		return counter.getAndIncrement();
	}

	@Cacheable(value = "default", key = "#root.methodName + #root.method.name + #root.targetClass + #root.target")
	public Object rootVars(Object arg1) {
		return counter.getAndIncrement();
	}

	@CachePut("default")
	public Object update(Object arg1) {
		return counter.getAndIncrement();
	}

	@CachePut(value = "default", condition = "#arg.equals(3)")
	public Object conditionalUpdate(Object arg) {
		return arg;
	}

	public Object nullValue(Object arg1) {
		nullInvocations.incrementAndGet();
		return null;
	}

	public Number nullInvocations() {
		return nullInvocations.get();
	}

	public Long throwChecked(Object arg1) throws Exception {
		throw new UnsupportedOperationException(arg1.toString());
	}

	public Long throwUnchecked(Object arg1) {
		throw new UnsupportedOperationException();
	}

	// multi annotations

	@CacheDefinitions(cacheable = { @Cacheable("primary"), @Cacheable("secondary") })
	public Object multiCache(Object arg1) {
		return counter.getAndIncrement();
	}

	@CacheDefinitions(evict = { @CacheEvict("primary"), @CacheEvict(value = "secondary", key = "#p0") })
	public Object multiEvict(Object arg1) {
		return counter.getAndIncrement();
	}

	@CacheDefinitions(cacheable = { @Cacheable(value = "primary", key = "#root.methodName") }, evict = { @CacheEvict("secondary") })
	public Object multiCacheAndEvict(Object arg1) {
		return counter.getAndIncrement();
	}

	@CacheDefinitions(cacheable = { @Cacheable(value = "primary", condition = "#p0 == 3") }, evict = { @CacheEvict("secondary") })
	public Object multiConditionalCacheAndEvict(Object arg1) {
		return counter.getAndIncrement();
	}

	@CacheDefinitions(put = { @CachePut("primary"), @CachePut("secondary") })
	public Object multiUpdate(Object arg1) {
		return arg1;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14631.java