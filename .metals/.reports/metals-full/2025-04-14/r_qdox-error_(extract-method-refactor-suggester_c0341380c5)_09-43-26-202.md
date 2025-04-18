error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5496.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5496.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5496.java
text:
```scala
l@@istenerDef.getPropertyValues().add("friends", new RootBeanDefinition(BeanThatListens.class));

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

package org.springframework.context.event;

import java.util.HashSet;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import org.junit.Test;

import org.springframework.beans.TestBean;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.BeanThatBroadcasts;
import org.springframework.context.BeanThatListens;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.Ordered;

/**
 * Unit and integration tests for the ApplicationContext event support.
 *
 * @author Alef Arendsen
 * @author Rick Evans
 */
public class ApplicationContextEventTests {

	@Test
	public void simpleApplicationEventMulticaster() {
		ApplicationListener listener = EasyMock.createMock(ApplicationListener.class);
		ApplicationEvent evt = new ContextClosedEvent(new StaticApplicationContext());
		listener.onApplicationEvent(evt);

		SimpleApplicationEventMulticaster smc = new SimpleApplicationEventMulticaster();
		smc.addApplicationListener(listener);

		replay(listener);
		smc.multicastEvent(evt);
		verify(listener);
	}

	@Test
	public void orderedListeners() {
		MyOrderedListener1 listener1 = new MyOrderedListener1();
		MyOrderedListener2 listener2 = new MyOrderedListener2(listener1);

		SimpleApplicationEventMulticaster smc = new SimpleApplicationEventMulticaster();
		smc.addApplicationListener(listener2);
		smc.addApplicationListener(listener1);

		smc.multicastEvent(new MyEvent(this));
		smc.multicastEvent(new MyOtherEvent(this));
	}

	@Test
	public void testEventPublicationInterceptor() throws Throwable {
		MethodInvocation invocation = EasyMock.createMock(MethodInvocation.class);
		ApplicationContext ctx = EasyMock.createMock(ApplicationContext.class);

		EventPublicationInterceptor interceptor = new EventPublicationInterceptor();
		interceptor.setApplicationEventClass(MyEvent.class);
		interceptor.setApplicationEventPublisher(ctx);
		interceptor.afterPropertiesSet();

		expect(invocation.proceed()).andReturn(new Object());
		expect(invocation.getThis()).andReturn(new Object());
		ctx.publishEvent(isA(MyEvent.class));
		replay(invocation, ctx);
		interceptor.invoke(invocation);
		verify(invocation, ctx);
	}

	@Test
	public void listenersInApplicationContext() {
		StaticApplicationContext context = new StaticApplicationContext();
		context.registerBeanDefinition("listener1", new RootBeanDefinition(MyOrderedListener1.class));
		RootBeanDefinition listener2 = new RootBeanDefinition(MyOrderedListener2.class);
		listener2.getConstructorArgumentValues().addGenericArgumentValue(new RuntimeBeanReference("listener1"));
		context.registerBeanDefinition("listener2", listener2);
		context.refresh();

		MyOrderedListener1 listener1 = context.getBean("listener1", MyOrderedListener1.class);
		MyEvent event1 = new MyEvent(context);
		context.publishEvent(event1);
		MyOtherEvent event2 = new MyOtherEvent(context);
		context.publishEvent(event2);
		MyEvent event3 = new MyEvent(context);
		context.publishEvent(event3);
		MyOtherEvent event4 = new MyOtherEvent(context);
		context.publishEvent(event4);
		assertTrue(listener1.seenEvents.contains(event1));
		assertTrue(listener1.seenEvents.contains(event2));
		assertTrue(listener1.seenEvents.contains(event3));
		assertTrue(listener1.seenEvents.contains(event4));
	}

	@Test
	public void listenerAndBroadcasterWithCircularReference() {
		StaticApplicationContext context = new StaticApplicationContext();
		context.registerBeanDefinition("broadcaster", new RootBeanDefinition(BeanThatBroadcasts.class));
		RootBeanDefinition listenerDef = new RootBeanDefinition(BeanThatListens.class);
		listenerDef.getConstructorArgumentValues().addGenericArgumentValue(new RuntimeBeanReference("broadcaster"));
		context.registerBeanDefinition("listener", listenerDef);
		context.refresh();

		BeanThatBroadcasts broadcaster = context.getBean("broadcaster", BeanThatBroadcasts.class);
		context.publishEvent(new MyEvent(context));
		assertEquals("The event was not received by the listener", 2, broadcaster.receivedCount);
	}

	@Test
	public void innerBeanAsListener() {
		StaticApplicationContext context = new StaticApplicationContext();
		RootBeanDefinition listenerDef = new RootBeanDefinition(TestBean.class);
		listenerDef.getPropertyValues().addPropertyValue("friends", new RootBeanDefinition(BeanThatListens.class));
		context.registerBeanDefinition("listener", listenerDef);
		context.refresh();
		context.publishEvent(new MyEvent(this));
		context.publishEvent(new MyEvent(this));
		TestBean listener = context.getBean(TestBean.class);
		assertEquals(3, ((BeanThatListens) listener.getFriends().iterator().next()).getEventCount());
	}


	public static class MyEvent extends ApplicationEvent {

		public MyEvent(Object source) {
			super(source);
		}
	}


	public static class MyOtherEvent extends ApplicationEvent {

		public MyOtherEvent(Object source) {
			super(source);
		}
	}


	public static class MyOrderedListener1 implements ApplicationListener, Ordered {

		public final Set<ApplicationEvent> seenEvents = new HashSet<ApplicationEvent>();

		public void onApplicationEvent(ApplicationEvent event) {
			this.seenEvents.add(event);
		}

		public int getOrder() {
			return 0;
		}
	}


	public interface MyOrderedListenerIfc<E extends ApplicationEvent> extends ApplicationListener<E>, Ordered {
	}


	public static abstract class MyOrderedListenerBase implements MyOrderedListenerIfc<MyEvent> {

		public int getOrder() {
			return 1;
		}
	}


	public static class MyOrderedListener2 extends MyOrderedListenerBase {

		private final MyOrderedListener1 otherListener;

		public MyOrderedListener2(MyOrderedListener1 otherListener) {
			this.otherListener = otherListener;
		}

		public void onApplicationEvent(MyEvent event) {
			assertTrue(otherListener.seenEvents.contains(event));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5496.java