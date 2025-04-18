error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11135.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11135.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11135.java
text:
```scala
private v@@oid setDefaultJmsConfig(AbstractJmsListenerContainerFactory<?> factory) {

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

package org.springframework.jms.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.jms.ConnectionFactory;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.transaction.TransactionManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.jms.StubConnectionFactory;
import org.springframework.jms.listener.AbstractMessageListenerContainer;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.listener.endpoint.JmsActivationSpecConfig;
import org.springframework.jms.listener.endpoint.JmsMessageEndpointManager;
import org.springframework.jms.listener.endpoint.StubJmsActivationSpecFactory;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

/**
 *
 * @author Stephane Nicoll
 */
public class JmsListenerContainerFactoryTests {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	private final ConnectionFactory connectionFactory = new StubConnectionFactory();

	private final DestinationResolver destinationResolver = new DynamicDestinationResolver();

	private final MessageConverter messageConverter = new SimpleMessageConverter();

	private final TransactionManager transactionManager = mock(TransactionManager.class);

	@Test
	public void createSimpleContainer() {
		SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
		setDefaultJmsConfig(factory);
		SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();

		MessageListener messageListener = new MessageListenerAdapter();
		endpoint.setMessageListener(messageListener);
		endpoint.setDestination("myQueue");
		endpoint.setQueue(false); // See #setDefaultJmsConfig

		SimpleMessageListenerContainer container = factory.createMessageListenerContainer(endpoint);

		assertDefaultJmsConfig(container);
		assertEquals(messageListener, container.getMessageListener());
		assertEquals("myQueue", container.getDestinationName());
	}


	@Test
	public void createJmsContainerFullConfig() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		setDefaultJmsConfig(factory);
		factory.setCacheLevel(DefaultMessageListenerContainer.CACHE_CONSUMER);
		factory.setConcurrency("3-10");
		factory.setMaxMessagesPerTask(5);

		SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
		MessageListener messageListener = new MessageListenerAdapter();
		endpoint.setMessageListener(messageListener);
		endpoint.setDestination("myQueue");
		endpoint.setQueue(false); // See #setDefaultJmsConfig
		DefaultMessageListenerContainer container = factory.createMessageListenerContainer(endpoint);

		assertDefaultJmsConfig(container);
		assertEquals(DefaultMessageListenerContainer.CACHE_CONSUMER, container.getCacheLevel());
		assertEquals(3, container.getConcurrentConsumers());
		assertEquals(10, container.getMaxConcurrentConsumers());
		assertEquals(5, container.getMaxMessagesPerTask());

		assertEquals(messageListener, container.getMessageListener());
		assertEquals("myQueue", container.getDestinationName());
	}

	@Test
	public void createJcaContainerFullConfig() {
		DefaultJcaListenerContainerFactory factory = new DefaultJcaListenerContainerFactory();
		setDefaultJcaConfig(factory);
		factory.getActivationSpecConfig().setConcurrency("10");

		SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
		MessageListener messageListener = new MessageListenerAdapter();
		endpoint.setMessageListener(messageListener);
		endpoint.setDestination("myQueue");
		endpoint.setQueue(false); // See #setDefaultJmsConfig
		JmsMessageEndpointManager container = factory.createMessageListenerContainer(endpoint);

		assertDefaultJcaConfig(container);
		assertEquals(10, container.getActivationSpecConfig().getMaxConcurrency());
		assertEquals(messageListener, container.getMessageListener());
		assertEquals("myQueue", container.getActivationSpecConfig().getDestinationName());
	}

	@Test
	public void endpointCanOverrideConfig() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setPubSubDomain(true); // topic

		SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
		endpoint.setMessageListener(new MessageListenerAdapter());
		endpoint.setQueue(true); // queue

		DefaultMessageListenerContainer container = factory.createMessageListenerContainer(endpoint);
		assertEquals(false, container.isPubSubDomain()); // overridden by the endpoint config
	}

	@Test
	public void jcaExclusiveProperties() {
		DefaultJcaListenerContainerFactory factory = new DefaultJcaListenerContainerFactory();
		factory.setDestinationResolver(destinationResolver);
		factory.setActivationSpecFactory(new StubJmsActivationSpecFactory());

		SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
		endpoint.setMessageListener(new MessageListenerAdapter());
		thrown.expect(IllegalStateException.class);
		factory.createMessageListenerContainer(endpoint);
	}

	private void setDefaultJmsConfig(AbstractJmsListenerContainerFactory factory) {
		factory.setConnectionFactory(connectionFactory);
		factory.setDestinationResolver(destinationResolver);
		factory.setMessageConverter(messageConverter);
		factory.setSessionTransacted(true);
		factory.setSessionAcknowledgeMode(Session.DUPS_OK_ACKNOWLEDGE);
		factory.setPubSubDomain(true);
		factory.setSubscriptionDurable(true);
		factory.setClientId("client-1234");
	}

	private void assertDefaultJmsConfig(AbstractMessageListenerContainer container) {
		assertEquals(connectionFactory, container.getConnectionFactory());
		assertEquals(destinationResolver, container.getDestinationResolver());
		assertEquals(messageConverter, container.getMessageConverter());
		assertEquals(true, container.isSessionTransacted());
		assertEquals(Session.DUPS_OK_ACKNOWLEDGE, container.getSessionAcknowledgeMode());
		assertEquals(true, container.isPubSubDomain());
		assertEquals(true, container.isSubscriptionDurable());
		assertEquals("client-1234", container.getClientId());
	}

	private void setDefaultJcaConfig(DefaultJcaListenerContainerFactory factory) {
		factory.setDestinationResolver(destinationResolver);
		factory.setTransactionManager(transactionManager);
		JmsActivationSpecConfig config = new JmsActivationSpecConfig();
		config.setMessageConverter(messageConverter);
		config.setAcknowledgeMode(Session.DUPS_OK_ACKNOWLEDGE);
		config.setPubSubDomain(true);
		config.setSubscriptionDurable(true);
		config.setClientId("client-1234");
		factory.setActivationSpecConfig(config);
	}

	private void assertDefaultJcaConfig(JmsMessageEndpointManager container) {
		assertEquals(messageConverter, container.getMessageConverter());
		JmsActivationSpecConfig config = container.getActivationSpecConfig();
		assertNotNull(config);
		assertEquals(Session.DUPS_OK_ACKNOWLEDGE, config.getAcknowledgeMode());
		assertEquals(true, config.isPubSubDomain());
		assertEquals(true, config.isSubscriptionDurable());
		assertEquals("client-1234", config.getClientId());
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11135.java