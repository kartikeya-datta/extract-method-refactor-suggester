error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3979.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3979.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3979.java
text:
```scala
public <@@T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {

/*
 * Copyright 2002-2012 the original author or authors.
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
package org.springframework.web.context.request.async;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Holder for a {@link Callable}, a timeout value, and a task executor.
 *
 * @author Rossen Stoyanchev
 * @since 3.2
 */
public class MvcAsyncTask<V> {

	private final Callable<V> callable;

	private final Long timeout;

	private final String executorName;

	private final AsyncTaskExecutor executor;

	private Callable<V> timeoutCallback;

	private Runnable completionCallback;

	private BeanFactory beanFactory;


	/**
	 * Create an {@code MvcAsyncTask} wrapping the given {@link Callable}.
	 * @param callable the callable for concurrent handling
	 */
	public MvcAsyncTask(Callable<V> callable) {
		this(null, null, null, callable);
	}

	/**
	 * Create an {@code MvcAsyncTask} with a timeout value and a {@link Callable}.
	 * @param timeout timeout value in milliseconds
	 * @param callable the callable for concurrent handling
	 */
	public MvcAsyncTask(long timeout, Callable<V> callable) {
		this(timeout, null, null, callable);
	}

	/**
	 * Create an {@code MvcAsyncTask} with a timeout value, an executor name, and a {@link Callable}.
	 * @param timeout timeout value in milliseconds; ignored if {@code null}
	 * @param callable the callable for concurrent handling
	 */
	public MvcAsyncTask(Long timeout, String executorName, Callable<V> callable) {
		this(timeout, null, executorName, callable);
		Assert.notNull(executor, "Executor name must not be null");
	}

	/**
	 * Create an {@code MvcAsyncTask} with a timeout value, an executor instance, and a Callable.
	 * @param timeout timeout value in milliseconds; ignored if {@code null}
	 * @param callable the callable for concurrent handling
	 */
	public MvcAsyncTask(Long timeout, AsyncTaskExecutor executor, Callable<V> callable) {
		this(timeout, executor, null, callable);
		Assert.notNull(executor, "Executor must not be null");
	}

	private MvcAsyncTask(Long timeout, AsyncTaskExecutor executor, String executorName, Callable<V> callable) {
		Assert.notNull(callable, "Callable must not be null");
		this.callable = callable;
		this.timeout = timeout;
		this.executor = executor;
		this.executorName = executorName;
	}


	/**
	 * Return the {@link Callable} to use for concurrent handling, never {@code null}.
	 */
	public Callable<?> getCallable() {
		return this.callable;
	}

	/**
	 * Return the timeout value in milliseconds or {@code null} if not value is set.
	 */
	public Long getTimeout() {
		return this.timeout;
	}

	/**
	 * Return the AsyncTaskExecutor to use for concurrent handling, or {@code null}.
	 */
	public AsyncTaskExecutor getExecutor() {
		if (this.executor != null) {
			return this.executor;
		}
		else if (this.executorName != null) {
			Assert.state(this.beanFactory != null, "A BeanFactory is required to look up an task executor bean");
			return this.beanFactory.getBean(this.executorName, AsyncTaskExecutor.class);
		}
		else {
			return null;
		}
	}

	/**
	 * A {@link BeanFactory} to use to resolve an executor name. Applications are
	 * not expected to have to set this property when {@code MvcAsyncTask} is used in a
	 * Spring MVC controller.
	 */
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}


	/**
	 * Register code to invoke when the async request times out. This method is
	 * called from a container thread when an async request times out before the
	 * {@code Callable} has completed. The callback is executed in the same
	 * thread and therefore should return without blocking. It may return an
	 * alternative value to use, including an {@link Exception} or return
	 * {@link CallableProcessingInterceptor#RESULT_NONE RESULT_NONE}.
	 */
	public void onTimeout(Callable<V> callback) {
		this.timeoutCallback = callback;
	}

	/**
	 * Register code to invoke when the async request completes. This method is
	 * called from a container thread when an async request completed for any
	 * reason including timeout and network error.
	 */
	public void onCompletion(Runnable callback) {
		this.completionCallback = callback;
	}

	CallableProcessingInterceptor getInterceptor() {
		return new CallableProcessingInterceptorAdapter() {

			@Override
			public <T> Object afterTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
				return (timeoutCallback != null) ? timeoutCallback.call() : CallableProcessingInterceptor.RESULT_NONE;
			}

			@Override
			public <T> void afterCompletion(NativeWebRequest request, Callable<T> task) throws Exception {
				if (completionCallback != null) {
					completionCallback.run();
				}
			}
		};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3979.java