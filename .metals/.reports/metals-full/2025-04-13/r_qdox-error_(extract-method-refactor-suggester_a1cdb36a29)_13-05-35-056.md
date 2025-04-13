error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6744.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6744.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6744.java
text:
```scala
a@@ssertEquals(2, channel.getInterceptors().size());

/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.messaging.simp.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.converter.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;
import org.springframework.messaging.simp.broker.DefaultSubscriptionRegistry;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.messaging.simp.user.UserDestinationMessageHandler;
import org.springframework.messaging.simp.user.UserSessionRegistry;
import org.springframework.messaging.simp.stomp.StompBrokerRelayMessageHandler;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Test fixture for {@link AbstractMessageBrokerConfiguration}.
 *
 * @author Rossen Stoyanchev
 * @author Brian Clozel
 * @author Sebastien Deleuze
 */
public class MessageBrokerConfigurationTests {

	private AnnotationConfigApplicationContext simpleBrokerContext;

	private AnnotationConfigApplicationContext brokerRelayContext;

	private AnnotationConfigApplicationContext defaultContext;

	private AnnotationConfigApplicationContext customContext;


	@Before
	public void setupOnce() {

		this.simpleBrokerContext = new AnnotationConfigApplicationContext();
		this.simpleBrokerContext.register(SimpleBrokerConfig.class);
		this.simpleBrokerContext.refresh();

		this.brokerRelayContext = new AnnotationConfigApplicationContext();
		this.brokerRelayContext.register(BrokerRelayConfig.class);
		this.brokerRelayContext.refresh();

		this.defaultContext = new AnnotationConfigApplicationContext();
		this.defaultContext.register(DefaultConfig.class);
		this.defaultContext.refresh();

		this.customContext = new AnnotationConfigApplicationContext();
		this.customContext.register(CustomConfig.class);
		this.customContext.refresh();
	}


	@Test
	public void clientInboundChannel() {

		TestChannel channel = this.simpleBrokerContext.getBean("clientInboundChannel", TestChannel.class);
		Set<MessageHandler> handlers = channel.getSubscribers();

		assertEquals(3, handlers.size());
		assertTrue(handlers.contains(simpleBrokerContext.getBean(SimpAnnotationMethodMessageHandler.class)));
		assertTrue(handlers.contains(simpleBrokerContext.getBean(UserDestinationMessageHandler.class)));
		assertTrue(handlers.contains(simpleBrokerContext.getBean(SimpleBrokerMessageHandler.class)));
	}

	@Test
	public void clientInboundChannelWithBrokerRelay() {
		TestChannel channel = this.brokerRelayContext.getBean("clientInboundChannel", TestChannel.class);
		Set<MessageHandler> handlers = channel.getSubscribers();

		assertEquals(3, handlers.size());
		assertTrue(handlers.contains(brokerRelayContext.getBean(SimpAnnotationMethodMessageHandler.class)));
		assertTrue(handlers.contains(brokerRelayContext.getBean(UserDestinationMessageHandler.class)));
		assertTrue(handlers.contains(brokerRelayContext.getBean(StompBrokerRelayMessageHandler.class)));
	}

	@Test
	public void clientInboundChannelCustomized() {
		AbstractSubscribableChannel channel = this.customContext.getBean(
				"clientInboundChannel", AbstractSubscribableChannel.class);

		assertEquals(1, channel.getInterceptors().size());

		ThreadPoolTaskExecutor taskExecutor = this.customContext.getBean(
				"clientInboundChannelExecutor", ThreadPoolTaskExecutor.class);

		assertEquals(11, taskExecutor.getCorePoolSize());
		assertEquals(12, taskExecutor.getMaxPoolSize());
		assertEquals(13, taskExecutor.getKeepAliveSeconds());
	}

	@Test
	public void clientOutboundChannelUsedByAnnotatedMethod() {
		TestChannel channel = this.simpleBrokerContext.getBean("clientOutboundChannel", TestChannel.class);
		SimpAnnotationMethodMessageHandler messageHandler = this.simpleBrokerContext.getBean(SimpAnnotationMethodMessageHandler.class);

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
		headers.setSessionId("sess1");
		headers.setSessionAttributes(new ConcurrentHashMap<>());
		headers.setSubscriptionId("subs1");
		headers.setDestination("/foo");
		Message<?> message = MessageBuilder.withPayload(new byte[0]).setHeaders(headers).build();

		messageHandler.handleMessage(message);

		message = channel.messages.get(0);
		headers = StompHeaderAccessor.wrap(message);

		assertEquals(SimpMessageType.MESSAGE, headers.getMessageType());
		assertEquals("/foo", headers.getDestination());
		assertEquals("bar", new String((byte[]) message.getPayload()));
	}

	@Test
	public void clientOutboundChannelUsedBySimpleBroker() {
		TestChannel channel = this.simpleBrokerContext.getBean("clientOutboundChannel", TestChannel.class);
		SimpleBrokerMessageHandler broker = this.simpleBrokerContext.getBean(SimpleBrokerMessageHandler.class);

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
		headers.setSessionId("sess1");
		headers.setSubscriptionId("subs1");
		headers.setDestination("/foo");
		Message<?> message = MessageBuilder.createMessage(new byte[0], headers.getMessageHeaders());

		// subscribe
		broker.handleMessage(message);

		headers = StompHeaderAccessor.create(StompCommand.SEND);
		headers.setSessionId("sess1");
		headers.setDestination("/foo");
		message = MessageBuilder.createMessage("bar".getBytes(), headers.getMessageHeaders());

		// message
		broker.handleMessage(message);

		message = channel.messages.get(0);
		headers = StompHeaderAccessor.wrap(message);

		assertEquals(SimpMessageType.MESSAGE, headers.getMessageType());
		assertEquals("/foo", headers.getDestination());
		assertEquals("bar", new String((byte[]) message.getPayload()));
	}

	@Test
	public void clientOutboundChannelCustomized() {

		AbstractSubscribableChannel channel = this.customContext.getBean(
				"clientOutboundChannel", AbstractSubscribableChannel.class);

		assertEquals(2, channel.getInterceptors().size());

		ThreadPoolTaskExecutor taskExecutor = this.customContext.getBean(
				"clientOutboundChannelExecutor", ThreadPoolTaskExecutor.class);

		assertEquals(21, taskExecutor.getCorePoolSize());
		assertEquals(22, taskExecutor.getMaxPoolSize());
		assertEquals(23, taskExecutor.getKeepAliveSeconds());
	}

	@Test
	public void brokerChannel() {
		TestChannel channel = this.simpleBrokerContext.getBean("brokerChannel", TestChannel.class);
		Set<MessageHandler> handlers = channel.getSubscribers();

		assertEquals(2, handlers.size());
		assertTrue(handlers.contains(simpleBrokerContext.getBean(UserDestinationMessageHandler.class)));
		assertTrue(handlers.contains(simpleBrokerContext.getBean(SimpleBrokerMessageHandler.class)));

		assertNull(channel.getExecutor());
	}

	@Test
	public void brokerChannelWithBrokerRelay() {
		TestChannel channel = this.brokerRelayContext.getBean("brokerChannel", TestChannel.class);
		Set<MessageHandler> handlers = channel.getSubscribers();

		assertEquals(2, handlers.size());
		assertTrue(handlers.contains(brokerRelayContext.getBean(UserDestinationMessageHandler.class)));
		assertTrue(handlers.contains(brokerRelayContext.getBean(StompBrokerRelayMessageHandler.class)));
	}

	@Test
	public void brokerChannelUsedByAnnotatedMethod() {
		TestChannel channel = this.simpleBrokerContext.getBean("brokerChannel", TestChannel.class);
		SimpAnnotationMethodMessageHandler messageHandler =
				this.simpleBrokerContext.getBean(SimpAnnotationMethodMessageHandler.class);

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
		headers.setSessionId("sess1");
		headers.setSessionAttributes(new ConcurrentHashMap<>());
		headers.setDestination("/foo");
		Message<?> message = MessageBuilder.createMessage(new byte[0], headers.getMessageHeaders());

		messageHandler.handleMessage(message);

		message = channel.messages.get(0);
		headers = StompHeaderAccessor.wrap(message);

		assertEquals(SimpMessageType.MESSAGE, headers.getMessageType());
		assertEquals("/bar", headers.getDestination());
		assertEquals("bar", new String((byte[]) message.getPayload()));
	}

	@Test
	public void brokerChannelUsedByUserDestinationMessageHandler() {
		TestChannel channel = this.simpleBrokerContext.getBean("brokerChannel", TestChannel.class);
		UserDestinationMessageHandler messageHandler = this.simpleBrokerContext.getBean(UserDestinationMessageHandler.class);

		this.simpleBrokerContext.getBean(UserSessionRegistry.class).registerSessionId("joe", "s1");

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
		headers.setDestination("/user/joe/foo");
		Message<?> message = MessageBuilder.createMessage(new byte[0], headers.getMessageHeaders());

		messageHandler.handleMessage(message);

		message = channel.messages.get(0);
		headers = StompHeaderAccessor.wrap(message);

		assertEquals(SimpMessageType.MESSAGE, headers.getMessageType());
		assertEquals("/foo-users1", headers.getDestination());
	}

	@Test
	public void brokerChannelCustomized() {

		AbstractSubscribableChannel channel = this.customContext.getBean(
				"brokerChannel", AbstractSubscribableChannel.class);

		assertEquals(3, channel.getInterceptors().size());

		ThreadPoolTaskExecutor taskExecutor = this.customContext.getBean(
				"brokerChannelExecutor", ThreadPoolTaskExecutor.class);

		assertEquals(31, taskExecutor.getCorePoolSize());
		assertEquals(32, taskExecutor.getMaxPoolSize());
		assertEquals(33, taskExecutor.getKeepAliveSeconds());
	}

	@Test
	public void configureMessageConvertersDefault() {
		AbstractMessageBrokerConfiguration config = new AbstractMessageBrokerConfiguration() {};
		CompositeMessageConverter compositeConverter = config.brokerMessageConverter();

		List<MessageConverter> converters = compositeConverter.getConverters();
		assertThat(converters.size(), Matchers.is(3));
		assertThat(converters.get(0), Matchers.instanceOf(StringMessageConverter.class));
		assertThat(converters.get(1), Matchers.instanceOf(ByteArrayMessageConverter.class));
		assertThat(converters.get(2), Matchers.instanceOf(MappingJackson2MessageConverter.class));

		ContentTypeResolver resolver = ((MappingJackson2MessageConverter) converters.get(2)).getContentTypeResolver();
		assertEquals(MimeTypeUtils.APPLICATION_JSON, ((DefaultContentTypeResolver) resolver).getDefaultMimeType());
	}

	@Test
	public void threadPoolSizeDefault() {

		String name = "clientInboundChannelExecutor";
		ThreadPoolTaskExecutor executor = this.defaultContext.getBean(name, ThreadPoolTaskExecutor.class);
		assertEquals(Runtime.getRuntime().availableProcessors() * 2, executor.getCorePoolSize());
		// No way to verify queue capacity

		name = "clientOutboundChannelExecutor";
		executor = this.defaultContext.getBean(name, ThreadPoolTaskExecutor.class);
		assertEquals(Runtime.getRuntime().availableProcessors() * 2, executor.getCorePoolSize());

		name = "brokerChannelExecutor";
		executor = this.defaultContext.getBean(name, ThreadPoolTaskExecutor.class);
		assertEquals(0, executor.getCorePoolSize());
		assertEquals(1, executor.getMaxPoolSize());
	}

	@Test
	public void configureMessageConvertersCustom() {
		final MessageConverter testConverter = mock(MessageConverter.class);
		AbstractMessageBrokerConfiguration config = new AbstractMessageBrokerConfiguration() {
			@Override
			protected boolean configureMessageConverters(List<MessageConverter> messageConverters) {
				messageConverters.add(testConverter);
				return false;
			}
		};

		CompositeMessageConverter compositeConverter = config.brokerMessageConverter();
		assertThat(compositeConverter.getConverters().size(), Matchers.is(1));
		Iterator<MessageConverter> iterator = compositeConverter.getConverters().iterator();
		assertThat(iterator.next(), Matchers.is(testConverter));
	}

	@Test
	public void configureMessageConvertersCustomAndDefault() {

		final MessageConverter testConverter = mock(MessageConverter.class);

		AbstractMessageBrokerConfiguration config = new AbstractMessageBrokerConfiguration() {
			@Override
			protected boolean configureMessageConverters(List<MessageConverter> messageConverters) {
				messageConverters.add(testConverter);
				return true;
			}
		};
		CompositeMessageConverter compositeConverter = config.brokerMessageConverter();

		assertThat(compositeConverter.getConverters().size(), Matchers.is(4));
		Iterator<MessageConverter> iterator = compositeConverter.getConverters().iterator();
		assertThat(iterator.next(), Matchers.is(testConverter));
		assertThat(iterator.next(), Matchers.instanceOf(StringMessageConverter.class));
		assertThat(iterator.next(), Matchers.instanceOf(ByteArrayMessageConverter.class));
		assertThat(iterator.next(), Matchers.instanceOf(MappingJackson2MessageConverter.class));
	}

	@Test
	public void customArgumentAndReturnValueTypes() throws Exception {
		SimpAnnotationMethodMessageHandler handler = this.customContext.getBean(SimpAnnotationMethodMessageHandler.class);

		List<HandlerMethodArgumentResolver> customResolvers = handler.getCustomArgumentResolvers();
		assertEquals(1, customResolvers.size());
		assertTrue(handler.getArgumentResolvers().contains(customResolvers.get(0)));

		List<HandlerMethodReturnValueHandler> customHandlers = handler.getCustomReturnValueHandlers();
		assertEquals(1, customHandlers.size());
		assertTrue(handler.getReturnValueHandlers().contains(customHandlers.get(0)));
	}

	@Test
	public void simpValidatorDefault() {
		AbstractMessageBrokerConfiguration config = new AbstractMessageBrokerConfiguration() {};
		config.setApplicationContext(new StaticApplicationContext());

		assertThat(config.simpValidator(), Matchers.notNullValue());
		assertThat(config.simpValidator(), Matchers.instanceOf(OptionalValidatorFactoryBean.class));
	}

	@Test
	public void simpValidatorCustom() {
		final Validator validator = mock(Validator.class);
		AbstractMessageBrokerConfiguration config = new AbstractMessageBrokerConfiguration() {
			@Override
			public Validator getValidator() {
				return validator;
			}
		};

		assertSame(validator, config.simpValidator());
	}

	@Test
	public void simpValidatorMvc() {
		StaticApplicationContext appCxt = new StaticApplicationContext();
		appCxt.registerSingleton("mvcValidator", TestValidator.class);
		AbstractMessageBrokerConfiguration config = new AbstractMessageBrokerConfiguration() {};
		config.setApplicationContext(appCxt);

		assertThat(config.simpValidator(), Matchers.notNullValue());
		assertThat(config.simpValidator(), Matchers.instanceOf(TestValidator.class));
	}

	@Test
	public void simpValidatorInjected() {
		SimpAnnotationMethodMessageHandler messageHandler =
				this.simpleBrokerContext.getBean(SimpAnnotationMethodMessageHandler.class);

		assertThat(messageHandler.getValidator(), Matchers.notNullValue(Validator.class));
	}

	@Test
	public void customPathMatcher() {
		SimpleBrokerMessageHandler broker = this.customContext.getBean(SimpleBrokerMessageHandler.class);
		DefaultSubscriptionRegistry registry = (DefaultSubscriptionRegistry) broker.getSubscriptionRegistry();
		assertEquals("a.a", registry.getPathMatcher().combine("a", "a"));

		SimpAnnotationMethodMessageHandler handler = this.customContext.getBean(SimpAnnotationMethodMessageHandler.class);
		assertEquals("a.a", handler.getPathMatcher().combine("a", "a"));
	}


	@Controller
	static class TestController {

		@SubscribeMapping("/foo")
		public String handleSubscribe() {
			return "bar";
		}

		@MessageMapping("/foo")
		@SendTo("/bar")
		public String handleMessage() {
			return "bar";
		}
	}


	@Configuration
	static class SimpleBrokerConfig extends AbstractMessageBrokerConfiguration {

		@Bean
		public TestController subscriptionController() {
			return new TestController();
		}

		@Override
		@Bean
		public AbstractSubscribableChannel clientInboundChannel() {
			return new TestChannel();
		}

		@Override
		@Bean
		public AbstractSubscribableChannel clientOutboundChannel() {
			return new TestChannel();
		}

		@Override
		@Bean
		public AbstractSubscribableChannel brokerChannel() {
			return new TestChannel();
		}
	}

	@Configuration
	static class BrokerRelayConfig extends SimpleBrokerConfig {

		@Override
		public void configureMessageBroker(MessageBrokerRegistry registry) {
			registry.enableStompBrokerRelay("/topic", "/queue").setAutoStartup(true);
		}
	}

	@Configuration
	static class DefaultConfig extends AbstractMessageBrokerConfiguration {
	}

	@Configuration
	static class CustomConfig extends AbstractMessageBrokerConfiguration {

		private ChannelInterceptor interceptor = new ChannelInterceptorAdapter() {};

		@Override
		protected void configureClientInboundChannel(ChannelRegistration registration) {
			registration.setInterceptors(this.interceptor);
			registration.taskExecutor().corePoolSize(11).maxPoolSize(12).keepAliveSeconds(13).queueCapacity(14);
		}

		@Override
		protected void configureClientOutboundChannel(ChannelRegistration registration) {
			registration.setInterceptors(this.interceptor, this.interceptor);
			registration.taskExecutor().corePoolSize(21).maxPoolSize(22).keepAliveSeconds(23).queueCapacity(24);
		}

		@Override
		protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.add(mock(HandlerMethodArgumentResolver.class));
		}

		@Override
		protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
			returnValueHandlers.add(mock(HandlerMethodReturnValueHandler.class));
		}

		@Override
		protected void configureMessageBroker(MessageBrokerRegistry registry) {
			registry.configureBrokerChannel().setInterceptors(this.interceptor, this.interceptor, this.interceptor);
			registry.configureBrokerChannel().taskExecutor().corePoolSize(31).maxPoolSize(32).keepAliveSeconds(33).queueCapacity(34);
			registry.setPathMatcher(new AntPathMatcher(".")).enableSimpleBroker("/topic", "/queue");
		}
	}


	private static class TestChannel extends ExecutorSubscribableChannel {

		private final List<Message<?>> messages = new ArrayList<>();

		@Override
		public boolean sendInternal(Message<?> message, long timeout) {
			this.messages.add(message);
			return true;
		}
	}

	private static class TestValidator implements Validator {

		@Override
		public boolean supports(Class<?> clazz) {
			return false;
		}

		@Override
		public void validate(Object target, Errors errors) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6744.java