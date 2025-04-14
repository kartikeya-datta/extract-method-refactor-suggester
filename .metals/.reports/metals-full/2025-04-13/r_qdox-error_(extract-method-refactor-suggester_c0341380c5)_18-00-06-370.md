error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9066.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9066.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9066.java
text:
```scala
h@@andler.setDestinationPrefixes(getMessageBrokerConfigurer().getApplicationDestinationPrefixes());

/*
 * Copyright 2002-2013 the original author or authors.
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

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.websocket.SubProtocolWebSocketHandler;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.handler.*;
import org.springframework.messaging.support.channel.AbstractSubscribableChannel;
import org.springframework.messaging.support.channel.ExecutorSubscribableChannel;
import org.springframework.messaging.support.converter.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.ClassUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.config.SockJsServiceRegistration;

import java.util.ArrayList;
import java.util.List;


/**
 * Configuration support for broker-backed messaging over WebSocket using a higher-level
 * messaging sub-protocol such as STOMP. This class can either be extended directly
 * or its configuration can also be customized in a callback style via
 * {@link EnableWebSocketMessageBroker @EnableWebSocketMessageBroker} and
 * {@link WebSocketMessageBrokerConfigurer}.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public abstract class WebSocketMessageBrokerConfigurationSupport {

	private static final boolean jackson2Present =
			ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", WebMvcConfigurationSupport.class.getClassLoader()) &&
					ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", WebMvcConfigurationSupport.class.getClassLoader());

	private MessageBrokerConfigurer messageBrokerConfigurer;


	// WebSocket configuration including message channels to/from the application

	@Bean
	public HandlerMapping brokerWebSocketHandlerMapping() {

		ServletStompEndpointRegistry registry = new ServletStompEndpointRegistry(
				subProtocolWebSocketHandler(), userSessionRegistry(), brokerDefaultSockJsTaskScheduler());

		registerStompEndpoints(registry);
		AbstractHandlerMapping hm = registry.getHandlerMapping();
		hm.setOrder(1);
		return hm;
	}

	@Bean
	public WebSocketHandler subProtocolWebSocketHandler() {
		SubProtocolWebSocketHandler wsHandler = new SubProtocolWebSocketHandler(webSocketRequestChannel());
		webSocketResponseChannel().subscribe(wsHandler);
		return wsHandler;
	}

	@Bean
	public UserSessionRegistry userSessionRegistry() {
		return new DefaultUserSessionRegistry();
	}

	/**
	 * The default TaskScheduler to use if none is configured via
	 * {@link SockJsServiceRegistration#setTaskScheduler(org.springframework.scheduling.TaskScheduler)}, i.e.
	 * <pre class="code">
	 * &#064;Configuration
	 * &#064;EnableWebSocketMessageBroker
	 * public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	 *
	 *   public void registerStompEndpoints(StompEndpointRegistry registry) {
	 *     registry.addEndpoint("/stomp").withSockJS().setTaskScheduler(myScheduler());
	 *   }
	 *
	 *   // ...
	 *
	 * }
	 * </pre>
	 */
	@Bean
	public ThreadPoolTaskScheduler brokerDefaultSockJsTaskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("BrokerSockJS-");
		return scheduler;
	}

	protected void registerStompEndpoints(StompEndpointRegistry registry) {
	}

	@Bean
	public AbstractSubscribableChannel webSocketRequestChannel() {
		return new ExecutorSubscribableChannel(webSocketChannelExecutor());
	}

	@Bean
	public AbstractSubscribableChannel webSocketResponseChannel() {
		return new ExecutorSubscribableChannel(webSocketChannelExecutor());
	}

	@Bean
	public ThreadPoolTaskExecutor webSocketChannelExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("BrokerWebSocketChannel-");
		return executor;
	}

	// Handling of messages by the application

	@Bean
	public SimpAnnotationMethodMessageHandler annotationMethodMessageHandler() {

		SimpAnnotationMethodMessageHandler handler =
				new SimpAnnotationMethodMessageHandler(brokerMessagingTemplate(), webSocketResponseChannel());

		handler.setDestinationPrefixes(getMessageBrokerConfigurer().getAnnotationMethodDestinationPrefixes());
		handler.setMessageConverter(simpMessageConverter());
		webSocketRequestChannel().subscribe(handler);
		return handler;
	}

	@Bean
	public AbstractBrokerMessageHandler simpleBrokerMessageHandler() {
		AbstractBrokerMessageHandler handler = getMessageBrokerConfigurer().getSimpleBroker();
		if (handler == null) {
			return noopBroker;
		}
		else {
			webSocketRequestChannel().subscribe(handler);
			brokerChannel().subscribe(handler);
			return handler;
		}
	}

	@Bean
	public AbstractBrokerMessageHandler stompBrokerRelayMessageHandler() {
		AbstractBrokerMessageHandler handler = getMessageBrokerConfigurer().getStompBrokerRelay();
		if (handler == null) {
			return noopBroker;
		}
		else {
			webSocketRequestChannel().subscribe(handler);
			brokerChannel().subscribe(handler);
			return handler;
		}
	}

	protected final MessageBrokerConfigurer getMessageBrokerConfigurer() {
		if (this.messageBrokerConfigurer == null) {
			MessageBrokerConfigurer configurer = new MessageBrokerConfigurer(webSocketResponseChannel());
			configureMessageBroker(configurer);
			this.messageBrokerConfigurer = configurer;
		}
		return this.messageBrokerConfigurer;
	}

	protected void configureMessageBroker(MessageBrokerConfigurer configurer) {
	}

	@Bean
	public UserDestinationMessageHandler userDestinationMessageHandler() {

		UserDestinationMessageHandler handler = new UserDestinationMessageHandler(
				brokerMessagingTemplate(), userDestinationResolver());

		webSocketRequestChannel().subscribe(handler);
		brokerChannel().subscribe(handler);
		return handler;
	}

	@Bean
	public SimpMessageSendingOperations brokerMessagingTemplate() {
		SimpMessagingTemplate template = new SimpMessagingTemplate(brokerChannel());
		String userDestinationPrefix = getMessageBrokerConfigurer().getUserDestinationPrefix();
		if (userDestinationPrefix != null) {
			template.setUserDestinationPrefix(userDestinationPrefix);
		}
		template.setMessageConverter(simpMessageConverter());
		return template;
	}

	@Bean
	public AbstractSubscribableChannel brokerChannel() {
		return new ExecutorSubscribableChannel(); // synchronous
	}

	@Bean
	public CompositeMessageConverter simpMessageConverter() {

		DefaultContentTypeResolver contentTypeResolver = new DefaultContentTypeResolver();

		List<MessageConverter> converters = new ArrayList<MessageConverter>();
		if (jackson2Present) {
			converters.add(new MappingJackson2MessageConverter());
			contentTypeResolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
		}
		converters.add(new StringMessageConverter());
		converters.add(new ByteArrayMessageConverter());

		return new CompositeMessageConverter(converters, contentTypeResolver);
	}

	@Bean
	public UserDestinationResolver userDestinationResolver() {
		DefaultUserDestinationResolver resolver = new DefaultUserDestinationResolver(userSessionRegistry());
		String prefix = getMessageBrokerConfigurer().getUserDestinationPrefix();
		if (prefix != null) {
			resolver.setUserDestinationPrefix(prefix);
		}
		return resolver;
	}


	private static final AbstractBrokerMessageHandler noopBroker = new AbstractBrokerMessageHandler(null) {

		@Override
		protected void startInternal() {
		}
		@Override
		protected void stopInternal() {
		}
		@Override
		protected void handleMessageInternal(Message<?> message) {
		}
	};

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9066.java