error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8014.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8014.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8014.java
text:
```scala
a@@ddListener(listener);

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

package org.springframework.context.event;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.ITestBean;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.TestBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.TestListener;
import org.springframework.context.support.StaticApplicationContext;

/**
 * @author Dmitriy Kopylenko
 * @author Juergen Hoeller
 * @author Rick Evans
 */
public class EventPublicationInterceptorTests {

	private ApplicationEventPublisher publisher;


	@Before
	public void setUp() {
		publisher = mock(ApplicationEventPublisher.class);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithNoApplicationEventClassSupplied() throws Exception {
		EventPublicationInterceptor interceptor = new EventPublicationInterceptor();
		interceptor.setApplicationEventPublisher(publisher);
		interceptor.afterPropertiesSet();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithNonApplicationEventClassSupplied() throws Exception {
		EventPublicationInterceptor interceptor = new EventPublicationInterceptor();
		interceptor.setApplicationEventPublisher(publisher);
		interceptor.setApplicationEventClass(getClass());
		interceptor.afterPropertiesSet();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithAbstractStraightApplicationEventClassSupplied() throws Exception {
		EventPublicationInterceptor interceptor = new EventPublicationInterceptor();
		interceptor.setApplicationEventPublisher(publisher);
		interceptor.setApplicationEventClass(ApplicationEvent.class);
		interceptor.afterPropertiesSet();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWithApplicationEventClassThatDoesntExposeAValidCtor() throws Exception {
		EventPublicationInterceptor interceptor = new EventPublicationInterceptor();
		interceptor.setApplicationEventPublisher(publisher);
		interceptor.setApplicationEventClass(TestEventWithNoValidOneArgObjectCtor.class);
		interceptor.afterPropertiesSet();
	}

	@Test
	public void testExpectedBehavior() throws Exception {
		TestBean target = new TestBean();
		final TestListener listener = new TestListener();

		class TestContext extends StaticApplicationContext {
			@Override
			protected void onRefresh() throws BeansException {
				addApplicationListener(listener);
			}
		}

		StaticApplicationContext ctx = new TestContext();
		MutablePropertyValues pvs = new MutablePropertyValues();
		pvs.add("applicationEventClass", TestEvent.class.getName());
		// should automatically receive applicationEventPublisher reference
		ctx.registerSingleton("publisher", EventPublicationInterceptor.class, pvs);
		ctx.registerSingleton("otherListener", FactoryBeanTestListener.class);
		ctx.refresh();

		EventPublicationInterceptor interceptor =
				(EventPublicationInterceptor) ctx.getBean("publisher");
		ProxyFactory factory = new ProxyFactory(target);
		factory.addAdvice(0, interceptor);

		ITestBean testBean = (ITestBean) factory.getProxy();

		// invoke any method on the advised proxy to see if the interceptor has been invoked
		testBean.getAge();

		// two events: ContextRefreshedEvent and TestEvent
		assertTrue("Interceptor must have published 2 events", listener.getEventCount() == 2);
		TestListener otherListener = (TestListener) ctx.getBean("&otherListener");
		assertTrue("Interceptor must have published 2 events", otherListener.getEventCount() == 2);
	}


	@SuppressWarnings("serial")
	public static class TestEvent extends ApplicationEvent {

		public TestEvent(Object source) {
			super(source);
		}
	}


	@SuppressWarnings("serial")
	public static final class TestEventWithNoValidOneArgObjectCtor extends ApplicationEvent {

		public TestEventWithNoValidOneArgObjectCtor() {
			super("");
		}
	}


	public static class FactoryBeanTestListener extends TestListener implements FactoryBean<Object> {

		@Override
		public Object getObject() throws Exception {
			return "test";
		}

		@Override
		public Class<String> getObjectType() {
			return String.class;
		}

		@Override
		public boolean isSingleton() {
			return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8014.java