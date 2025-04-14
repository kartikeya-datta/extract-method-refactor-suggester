error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4753.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4753.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4753.java
text:
```scala
private final C@@ountDownLatch latch = new CountDownLatch(1);

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

package org.springframework.scheduling.annotation;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author Mark Fisher
 */
public class AsyncAnnotationBeanPostProcessorTests {

	@Test
	public void proxyCreated() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinition processorDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessor.class);
		BeanDefinition targetDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessorTests.TestBean.class);
		context.registerBeanDefinition("postProcessor", processorDefinition);
		context.registerBeanDefinition("target", targetDefinition);
		context.refresh();
		Object target = context.getBean("target");
		assertTrue(AopUtils.isAopProxy(target));
		context.close();
	}

	@Test
	public void invokedAsynchronously() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinition processorDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessor.class);
		BeanDefinition targetDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessorTests.TestBean.class);
		context.registerBeanDefinition("postProcessor", processorDefinition);
		context.registerBeanDefinition("target", targetDefinition);
		context.refresh();
		ITestBean testBean = (ITestBean) context.getBean("target");
		testBean.test();
		Thread mainThread = Thread.currentThread();
		testBean.await(3000);
		Thread asyncThread = testBean.getThread();
		assertNotSame(mainThread, asyncThread);
		context.close();
	}

	@Test
	public void threadNamePrefix() {
		StaticApplicationContext context = new StaticApplicationContext();
		BeanDefinition processorDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessor.class);
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("testExecutor");
		executor.afterPropertiesSet();
		processorDefinition.getPropertyValues().addPropertyValue("executor", executor);
		BeanDefinition targetDefinition = new RootBeanDefinition(AsyncAnnotationBeanPostProcessorTests.TestBean.class);
		context.registerBeanDefinition("postProcessor", processorDefinition);
		context.registerBeanDefinition("target", targetDefinition);
		context.refresh();
		ITestBean testBean = (ITestBean) context.getBean("target");
		testBean.test();
		testBean.await(3000);
		Thread asyncThread = testBean.getThread();
		assertTrue(asyncThread.getName().startsWith("testExecutor"));
		context.close();
	}


	private static interface ITestBean {

		Thread getThread();

		void test();

		void await(long timeout);
	}


	private static class TestBean implements ITestBean {

		private Thread thread;

		private CountDownLatch latch;

		public Thread getThread() {
			return this.thread;
		}

		@Async
		public void test() {
			this.thread = Thread.currentThread();
			this.latch.countDown();
		}

		public void await(long timeout) {
			try {
				this.latch.await(timeout, TimeUnit.MILLISECONDS);
			}
			catch (Exception e) {
				Thread.currentThread().interrupt();
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4753.java