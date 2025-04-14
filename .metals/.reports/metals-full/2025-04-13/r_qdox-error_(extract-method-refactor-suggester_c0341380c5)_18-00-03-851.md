error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13745.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13745.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13745.java
text:
```scala
s@@elector = "mySelector", subscription = "mySubscription")

/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.jms.annotation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.jms.JMSException;
import javax.jms.Session;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.context.ApplicationContext;
import org.springframework.jms.StubTextMessage;
import org.springframework.jms.config.JmsListenerContainerTestFactory;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.config.MethodJmsListenerEndpoint;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessagingMessageListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author Stephane Nicoll
 */
public abstract class AbstractJmsAnnotationDrivenTests {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Test
	public abstract void sampleConfiguration();

	@Test
	public abstract void fullConfiguration();

	@Test
	public abstract void customConfiguration();

	@Test
	public abstract void explicitContainerFactory();

	@Test
	public abstract void defaultContainerFactory();

	@Test
	public abstract void jmsHandlerMethodFactoryConfiguration() throws JMSException;

	/**
	 * Test for {@link SampleBean} discovery. If a factory with the default name
	 * is set, an endpoint will use it automatically
	 */
	public void testSampleConfiguration(ApplicationContext context) {
		JmsListenerContainerTestFactory defaultFactory =
				context.getBean("jmsListenerContainerFactory", JmsListenerContainerTestFactory.class);
		JmsListenerContainerTestFactory simpleFactory =
				context.getBean("simpleFactory", JmsListenerContainerTestFactory.class);
		assertEquals(1, defaultFactory.getContainers().size());
		assertEquals(1, simpleFactory.getContainers().size());
	}

	@Component
	static class SampleBean {

		@JmsListener(destination = "myQueue")
		public void defaultHandle(String msg) {
		}

		@JmsListener(containerFactory = "simpleFactory", destination = "myQueue")
		public void simpleHandle(String msg) {
		}
	}

	/**
	 * Test for {@link FullBean} discovery. In this case, no default is set because
	 * all endpoints provide a default registry. This shows that the default factory
	 * is only retrieved if it needs to be.
	 */
	public void testFullConfiguration(ApplicationContext context) {
		JmsListenerContainerTestFactory simpleFactory =
				context.getBean("simpleFactory", JmsListenerContainerTestFactory.class);
		assertEquals(1, simpleFactory.getContainers().size());
		MethodJmsListenerEndpoint endpoint = (MethodJmsListenerEndpoint)
				simpleFactory.getContainers().get(0).getEndpoint();
		assertEquals("listener1", endpoint.getId());
		assertEquals("queueIn", endpoint.getDestination());
		assertTrue(endpoint.isQueue());
		assertEquals("mySelector", endpoint.getSelector());
		assertEquals("mySubscription", endpoint.getSubscription());
	}

	@Component
	static class FullBean {

		@JmsListener(id = "listener1", containerFactory = "simpleFactory", destination = "queueIn",
				responseDestination = "queueOut", selector = "mySelector", subscription = "mySubscription")
		public String fullHandle(String msg) {
			return "reply";
		}
	}

	/**
	 * Test for {@link CustomBean} and an manually endpoint registered
	 * with "myCustomEndpointId". The custom endpoint does not provide
	 * any factory so it's registered with the default one
	 */
	public void testCustomConfiguration(ApplicationContext context) {
		JmsListenerContainerTestFactory defaultFactory =
				context.getBean("jmsListenerContainerFactory", JmsListenerContainerTestFactory.class);
		JmsListenerContainerTestFactory customFactory =
				context.getBean("customFactory", JmsListenerContainerTestFactory.class);
		assertEquals(1, defaultFactory.getContainers().size());
		assertEquals(1, customFactory.getContainers().size());
		JmsListenerEndpoint endpoint = defaultFactory.getContainers().get(0).getEndpoint();
		assertEquals("Wrong endpoint type", SimpleJmsListenerEndpoint.class, endpoint.getClass());
		assertEquals("Wrong listener set in custom endpoint", context.getBean("simpleMessageListener"),
				((SimpleJmsListenerEndpoint) endpoint).getMessageListener());

		JmsListenerEndpointRegistry customRegistry =
				context.getBean("customRegistry", JmsListenerEndpointRegistry.class);
		assertEquals("Wrong number of containers in the registry", 2,
				customRegistry.getContainers().size());
		assertNotNull("Container with custom id on the annotation should be found",
				customRegistry.getContainer("listenerId"));
		assertNotNull("Container created with custom id should be found",
				customRegistry.getContainer("myCustomEndpointId"));
	}

	@Component
	static class CustomBean {

		@JmsListener(id = "listenerId", containerFactory = "customFactory", destination = "myQueue")
		public void customHandle(String msg) {
		}
	}

	/**
	 * Test for {@link DefaultBean} that does not define the container
	 * factory to use as a default is registered with an explicit
	 * default.
	 */
	public void testExplicitContainerFactoryConfiguration(ApplicationContext context) {
		JmsListenerContainerTestFactory defaultFactory =
				context.getBean("simpleFactory", JmsListenerContainerTestFactory.class);
		assertEquals(1, defaultFactory.getContainers().size());
	}

	/**
	 * Test for {@link DefaultBean} that does not define the container
	 * factory to use as a default is registered with the default name.
	 */
	public void testDefaultContainerFactoryConfiguration(ApplicationContext context) {
		JmsListenerContainerTestFactory defaultFactory =
				context.getBean("jmsListenerContainerFactory", JmsListenerContainerTestFactory.class);
		assertEquals(1, defaultFactory.getContainers().size());
	}

	static class DefaultBean {

		@JmsListener(destination = "myQueue")
		public void handleIt(String msg) {
		}
	}

	/**
	 * Test for {@link ValidationBean} with a validator ({@link TestValidator}) specified
	 * in a custom {@link org.springframework.jms.config.DefaultJmsHandlerMethodFactory}.
	 *
	 * The test should throw a {@link org.springframework.jms.listener.adapter.ListenerExecutionFailedException}
	 */
	public void testJmsHandlerMethodFactoryConfiguration(ApplicationContext context) throws JMSException {
		JmsListenerContainerTestFactory simpleFactory =
				context.getBean("defaultFactory", JmsListenerContainerTestFactory.class);
		assertEquals(1, simpleFactory.getContainers().size());
		MethodJmsListenerEndpoint endpoint = (MethodJmsListenerEndpoint)
				simpleFactory.getContainers().get(0).getEndpoint();

		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		endpoint.setupMessageContainer(container);
		MessagingMessageListenerAdapter listener = (MessagingMessageListenerAdapter) container.getMessageListener();
		listener.onMessage(new StubTextMessage("failValidation"), mock(Session.class));
	}

	@Component
	static class ValidationBean {

		@JmsListener(containerFactory = "defaultFactory", destination = "myQueue")
		public void defaultHandle(@Validated String msg) {
		}
	}

	static class TestValidator implements Validator {

		@Override
		public boolean supports(Class<?> clazz) {
			return String.class.isAssignableFrom(clazz);
		}

		@Override
		public void validate(Object target, Errors errors) {
			String value = (String) target;
			if ("failValidation".equals(value)) {
				errors.reject("TEST: expected invalid value");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13745.java