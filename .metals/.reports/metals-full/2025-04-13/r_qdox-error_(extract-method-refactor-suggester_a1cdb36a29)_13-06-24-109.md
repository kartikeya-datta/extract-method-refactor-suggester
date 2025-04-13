error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9346.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9346.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9346.java
text:
```scala
private S@@cheduledFuture<?> sessionCleanupTask;

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

package org.springframework.web.socket.sockjs.transport.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.DefaultHandshakeHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HandshakeInterceptorChain;
import org.springframework.web.socket.sockjs.SockJsException;
import org.springframework.web.socket.sockjs.SockJsService;
import org.springframework.web.socket.sockjs.support.AbstractSockJsService;
import org.springframework.web.socket.sockjs.support.frame.Jackson2SockJsMessageCodec;
import org.springframework.web.socket.sockjs.support.frame.JacksonSockJsMessageCodec;
import org.springframework.web.socket.sockjs.support.frame.SockJsMessageCodec;
import org.springframework.web.socket.sockjs.transport.TransportHandler;
import org.springframework.web.socket.sockjs.transport.TransportType;
import org.springframework.web.socket.sockjs.transport.session.AbstractSockJsSession;
import org.springframework.web.socket.sockjs.transport.session.SockJsServiceConfig;


/**
 * A default implementation of {@link SockJsService} adding support for transport handling
 * and session management. See {@link AbstractSockJsService} base class for important
 * details on request mapping.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class DefaultSockJsService extends AbstractSockJsService {

	private static final boolean jackson2Present = ClassUtils.isPresent(
			"com.fasterxml.jackson.databind.ObjectMapper", DefaultSockJsService.class.getClassLoader());

	private static final boolean jacksonPresent = ClassUtils.isPresent(
			"org.codehaus.jackson.map.ObjectMapper", DefaultSockJsService.class.getClassLoader());


	private final Map<TransportType, TransportHandler> transportHandlers = new HashMap<TransportType, TransportHandler>();

	private SockJsMessageCodec messageCodec;

	private final List<HandshakeInterceptor> interceptors = new ArrayList<HandshakeInterceptor>();

	private final Map<String, AbstractSockJsSession> sessions = new ConcurrentHashMap<String, AbstractSockJsSession>();

	private ScheduledFuture sessionCleanupTask;


	/**
	 * Create an instance with default {@link TransportHandler transport handler} types.
	 *
	 * @param taskScheduler a task scheduler for heart-beat messages and removing
	 *        timed-out sessions; the provided TaskScheduler should be declared as a
	 *        Spring bean to ensure it is initialized at start up and shut down when the
	 *        application stops.
	 */
	public DefaultSockJsService(TaskScheduler taskScheduler) {
		this(taskScheduler, null);
	}

	/**
	 * Create an instance by overriding or replacing completely the default
	 * {@link TransportHandler transport handler} types.
	 *
	 * @param taskScheduler a task scheduler for heart-beat messages and removing
	 *        timed-out sessions; the provided TaskScheduler should be declared as a
	 *        Spring bean to ensure it is initialized at start up and shut down when the
	 *        application stops.
	 * @param transportHandlers the transport handlers to use (replaces the default ones);
	 *        can be {@code null} if you don't want to install the default ones.
	 * @param transportHandlerOverrides zero or more overrides to the default transport
	 *        handler types.
	 */
	public DefaultSockJsService(TaskScheduler taskScheduler, Collection<TransportHandler> transportHandlers,
			TransportHandler... transportHandlerOverrides) {

		super(taskScheduler);

		initMessageCodec();

		if (CollectionUtils.isEmpty(transportHandlers)) {
			addTransportHandlers(getDefaultTransportHandlers());
		}
		else {
			addTransportHandlers(transportHandlers);
		}

		if (!ObjectUtils.isEmpty(transportHandlerOverrides)) {
			addTransportHandlers(Arrays.asList(transportHandlerOverrides));
		}

		if (this.transportHandlers.isEmpty()) {
			logger.warn("No transport handlers");
		}
	}

	private void initMessageCodec() {
		if (jackson2Present) {
			this.messageCodec = new Jackson2SockJsMessageCodec();
		}
		else if (jacksonPresent) {
			this.messageCodec = new JacksonSockJsMessageCodec();
		}
	}

	protected final Set<TransportHandler> getDefaultTransportHandlers() {
		Set<TransportHandler> result = new HashSet<TransportHandler>();
		result.add(new XhrPollingTransportHandler());
		result.add(new XhrReceivingTransportHandler());
		result.add(new JsonpPollingTransportHandler());
		result.add(new JsonpReceivingTransportHandler());
		result.add(new XhrStreamingTransportHandler());
		result.add(new EventSourceTransportHandler());
		result.add(new HtmlFileTransportHandler());
		try {
			result.add(new WebSocketTransportHandler(new DefaultHandshakeHandler()));
		}
		catch (Exception ex) {
			if (logger.isWarnEnabled()) {
				logger.warn("Failed to create default WebSocketTransportHandler", ex);
			}
		}
		return result;
	}

	protected void addTransportHandlers(Collection<TransportHandler> handlers) {
		for (TransportHandler handler : handlers) {
			if (handler instanceof TransportHandlerSupport) {
				((TransportHandlerSupport) handler).setSockJsServiceConfiguration(this.sockJsServiceConfig);
			}
			this.transportHandlers.put(handler.getTransportType(), handler);
		}
	}


	/**
	 * Configure one or more WebSocket handshake request interceptors.
	 */
	public void setHandshakeInterceptors(List<HandshakeInterceptor> interceptors) {
		this.interceptors.clear();
		if (interceptors != null) {
			this.interceptors.addAll(interceptors);
		}
	}

	/**
	 * Return the configured WebSocket handshake request interceptors.
	 */
	public List<HandshakeInterceptor> getHandshakeInterceptors() {
		return this.interceptors;
	}

	/**
	 * The codec to use for encoding and decoding SockJS messages.
	 * @exception IllegalStateException if no {@link SockJsMessageCodec} is available
	 */
	public void setMessageCodec(SockJsMessageCodec messageCodec) {
		this.messageCodec = messageCodec;
	}

	public SockJsMessageCodec getMessageCodec() {
		return this.messageCodec;
	}

	public Map<TransportType, TransportHandler> getTransportHandlers() {
		return Collections.unmodifiableMap(this.transportHandlers);
	}

	@Override
	protected void handleRawWebSocketRequest(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler) throws IOException {

		if (!isWebSocketEnabled()) {
			return;
		}

		TransportHandler transportHandler = this.transportHandlers.get(TransportType.WEBSOCKET);
		if ((transportHandler == null) || !(transportHandler instanceof HandshakeHandler)) {
			logger.warn("No handler for raw WebSocket messages");
			response.setStatusCode(HttpStatus.NOT_FOUND);
			return;
		}

		HandshakeInterceptorChain chain = new HandshakeInterceptorChain(this.interceptors, wsHandler);
		HandshakeFailureException failure = null;

		try {
			Map<String, Object> attributes = new HashMap<String, Object>();
			if (!chain.applyBeforeHandshake(request, response, attributes)) {
				return;
			}
			((HandshakeHandler) transportHandler).doHandshake(request, response, wsHandler, attributes);
			chain.applyAfterHandshake(request, response, null);
		}
		catch (HandshakeFailureException ex) {
			failure = ex;
		}
		catch (Throwable t) {
			failure = new HandshakeFailureException("Uncaught failure for request " + request.getURI(), t);
		}
		finally {
			if (failure != null) {
				chain.applyAfterHandshake(request, response, failure);
				throw failure;
			}
		}
	}

	@Override
	protected void handleTransportRequest(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, String sessionId, String transport) throws SockJsException {

		TransportType transportType = TransportType.fromValue(transport);
		if (transportType == null) {
			logger.debug("Unknown transport type: " + transportType);
			response.setStatusCode(HttpStatus.NOT_FOUND);
			return;
		}

		TransportHandler transportHandler = this.transportHandlers.get(transportType);
		if (transportHandler == null) {
			logger.debug("Transport handler not found");
			response.setStatusCode(HttpStatus.NOT_FOUND);
			return;
		}

		HttpMethod supportedMethod = transportType.getHttpMethod();
		if (!supportedMethod.equals(request.getMethod())) {
			if (HttpMethod.OPTIONS.equals(request.getMethod()) && transportType.supportsCors()) {
				response.setStatusCode(HttpStatus.NO_CONTENT);
				addCorsHeaders(request, response, HttpMethod.OPTIONS, supportedMethod);
				addCacheHeaders(response);
			}
			else {
				List<HttpMethod> supportedMethods = Arrays.asList(supportedMethod);
				if (transportType.supportsCors()) {
					supportedMethods.add(HttpMethod.OPTIONS);
				}
				sendMethodNotAllowed(response, supportedMethods);
			}
			return;
		}

		HandshakeInterceptorChain chain = new HandshakeInterceptorChain(this.interceptors, wsHandler);
		SockJsException failure = null;

		try {
			WebSocketSession session = this.sessions.get(sessionId);
			if (session == null) {
				if (transportHandler instanceof SockJsSessionFactory) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					if (!chain.applyBeforeHandshake(request, response, attributes)) {
						return;
					}
					SockJsSessionFactory sessionFactory = (SockJsSessionFactory) transportHandler;
					session = createSockJsSession(sessionId, sessionFactory, wsHandler, attributes, request, response);
				}
				else {
					response.setStatusCode(HttpStatus.NOT_FOUND);
					logger.warn("Session not found");
					return;
				}
			}

			if (transportType.sendsNoCacheInstruction()) {
				addNoCacheHeaders(response);
			}

			if (transportType.supportsCors()) {
				addCorsHeaders(request, response);
			}

			transportHandler.handleRequest(request, response, wsHandler, session);
			chain.applyAfterHandshake(request, response, null);
		}
		catch (SockJsException ex) {
			failure = ex;
		}
		catch (Throwable t) {
			failure = new SockJsException("Uncaught failure for request " + request.getURI(), sessionId, t);
		}
		finally {
			if (failure != null) {
				chain.applyAfterHandshake(request, response, failure);
				throw failure;
			}
		}
	}

	private WebSocketSession createSockJsSession(String sessionId, SockJsSessionFactory sessionFactory,
			WebSocketHandler wsHandler, Map<String, Object> handshakeAttributes,
			ServerHttpRequest request, ServerHttpResponse response) {

		synchronized (this.sessions) {
			AbstractSockJsSession session = this.sessions.get(sessionId);
			if (session != null) {
				return session;
			}
			if (this.sessionCleanupTask == null) {
				scheduleSessionTask();
			}

			logger.debug("Creating new session with session id \"" + sessionId + "\"");
			session = sessionFactory.createSession(sessionId, wsHandler, handshakeAttributes);
			this.sessions.put(sessionId, session);
			return session;
		}
	}

	@Override
	protected boolean isValidTransportType(String lastSegment) {
		return TransportType.fromValue(lastSegment) != null;
	}

	private void scheduleSessionTask() {
		this.sessionCleanupTask = getTaskScheduler().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					int count = sessions.size();
					if (logger.isTraceEnabled() && (count != 0)) {
						logger.trace("Checking " + count + " session(s) for timeouts [" + getName() + "]");
					}
					for (AbstractSockJsSession session : sessions.values()) {
						if (session.getTimeSinceLastActive() > getDisconnectDelay()) {
							if (logger.isTraceEnabled()) {
								logger.trace("Removing " + session + " for [" + getName() + "]");
							}
							session.close();
							sessions.remove(session.getId());
						}
					}
					if (logger.isTraceEnabled() && (count != 0)) {
						logger.trace(sessions.size() + " remaining session(s) [" + getName() + "]");
					}
				}
				catch (Throwable t) {
					logger.error("Failed to complete session timeout checks for [" + getName() + "]", t);
				}
			}
		}, getDisconnectDelay());
	}


	private final SockJsServiceConfig sockJsServiceConfig = new SockJsServiceConfig() {

		@Override
		public int getStreamBytesLimit() {
			return DefaultSockJsService.this.getStreamBytesLimit();
		}

		@Override
		public long getHeartbeatTime() {
			return DefaultSockJsService.this.getHeartbeatTime();
		}

		@Override
		public TaskScheduler getTaskScheduler() {
			return DefaultSockJsService.this.getTaskScheduler();
		}

		@Override
		public SockJsMessageCodec getMessageCodec() {
			Assert.state(DefaultSockJsService.this.getMessageCodec() != null,
					"A SockJsMessageCodec is required but not available."
					+ " Either add Jackson 2 or Jackson 1.x to the classpath, or configure a SockJsMessageCode");
			return DefaultSockJsService.this.getMessageCodec();
		}

		@Override
		public int getHttpMessageCacheSize() {
			return DefaultSockJsService.this.getHttpMessageCacheSize();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9346.java