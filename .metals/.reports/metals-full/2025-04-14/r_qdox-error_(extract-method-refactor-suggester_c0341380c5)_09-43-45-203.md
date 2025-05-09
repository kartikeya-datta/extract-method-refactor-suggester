error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11189.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11189.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11189.java
text:
```scala
l@@ogger.trace("Message " + message);

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

package org.springframework.messaging.simp.stomp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.websocket.SubProtocolHandler;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.handler.MutableUserQueueSuffixResolver;
import org.springframework.messaging.simp.handler.SimpleUserQueueSuffixResolver;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * A {@link SubProtocolHandler} for STOMP that supports versions 1.0, 1.1, and 1.2 of the
 * STOMP specification.
 *
 * @author Rossen Stoyanchev
 * @author Andy Wilkinson
 */
public class StompProtocolHandler implements SubProtocolHandler {

	/**
	 * The name of the header set on the CONNECTED frame indicating the name of the user
	 * connected authenticated on the WebSocket session.
	 */
	public static final String CONNECTED_USER_HEADER = "user-name";

	/**
	 * A suffix unique to the current session that a client can use to append to
	 * a destination to make it unique.
	 *
	 * @see {@link org.springframework.messaging.simp.handler.UserDestinationMessageHandler}
	 */
	public static final String QUEUE_SUFFIX_HEADER = "queue-suffix";

	private final Log logger = LogFactory.getLog(StompProtocolHandler.class);

	private final StompMessageConverter stompMessageConverter = new StompMessageConverter();

	private MutableUserQueueSuffixResolver queueSuffixResolver = new SimpleUserQueueSuffixResolver();


	/**
	 * Configure a resolver to use to maintain queue suffixes for user
	 * @see {@link org.springframework.messaging.simp.handler.UserDestinationMessageHandler}
	 */
	public void setUserQueueSuffixResolver(MutableUserQueueSuffixResolver resolver) {
		this.queueSuffixResolver = resolver;
	}

	/**
	 * @return the resolver for queue suffixes for a user
	 */
	public MutableUserQueueSuffixResolver getUserQueueSuffixResolver() {
		return this.queueSuffixResolver;
	}

	@Override
	public List<String> getSupportedProtocols() {
		return Arrays.asList("v10.stomp", "v11.stomp", "v12.stomp");
	}

	/**
	 * Handle incoming WebSocket messages from clients.
	 */
	public void handleMessageFromClient(WebSocketSession session, WebSocketMessage webSocketMessage,
			MessageChannel outputChannel) {

		try {
			Assert.isInstanceOf(TextMessage.class,  webSocketMessage);
			String payload = ((TextMessage)webSocketMessage).getPayload();
			Message<?> message = this.stompMessageConverter.toMessage(payload);

			// TODO: validate size limits
			// http://stomp.github.io/stomp-specification-1.2.html#Size_Limits

			if (logger.isTraceEnabled()) {
				logger.trace("Processing STOMP message: " + message);
			}

			try {
				StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
				headers.setSessionId(session.getId());
				headers.setUser(session.getPrincipal());

				message = MessageBuilder.withPayloadAndHeaders(message.getPayload(), headers).build();

				if (SimpMessageType.CONNECT.equals(headers.getMessageType())) {
					handleConnect(session, message);
				}

				outputChannel.send(message);

			}
			catch (Throwable t) {
				logger.error("Terminating STOMP session due to failure to send message: ", t);
				sendErrorMessage(session, t);
			}

			// TODO: send RECEIPT message if incoming message has "receipt" header
			// http://stomp.github.io/stomp-specification-1.2.html#Header_receipt

		}
		catch (Throwable error) {
			sendErrorMessage(session, error);
		}
	}

	/**
	 * Handle STOMP messages going back out to WebSocket clients.
	 */
	@Override
	public void handleMessageToClient(WebSocketSession session, Message<?> message) {

		StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
		headers.setCommandIfNotSet(StompCommand.MESSAGE);

		if (StompCommand.CONNECTED.equals(headers.getCommand())) {
			// Ignore for now since we already sent it
			return;
		}

		if (StompCommand.MESSAGE.equals(headers.getCommand()) && (headers.getSubscriptionId() == null)) {
			// TODO: failed message delivery mechanism
			logger.error("Ignoring message, no subscriptionId header: " + message);
			return;
		}

		if (!(message.getPayload() instanceof byte[])) {
			// TODO: failed message delivery mechanism
			logger.error("Ignoring message, expected byte[] content: " + message);
			return;
		}

		try {
			message = MessageBuilder.withPayloadAndHeaders(message.getPayload(), headers).build();
			byte[] bytes = this.stompMessageConverter.fromMessage(message);
			session.sendMessage(new TextMessage(new String(bytes, Charset.forName("UTF-8"))));
		}
		catch (Throwable t) {
			sendErrorMessage(session, t);
		}
		finally {
			if (StompCommand.ERROR.equals(headers.getCommand())) {
				try {
					session.close(CloseStatus.PROTOCOL_ERROR);
				}
				catch (IOException e) {
				}
			}
		}
	}

	protected void handleConnect(WebSocketSession session, Message<?> message) throws IOException {

		StompHeaderAccessor connectHeaders = StompHeaderAccessor.wrap(message);
		StompHeaderAccessor connectedHeaders = StompHeaderAccessor.create(StompCommand.CONNECTED);

		Set<String> acceptVersions = connectHeaders.getAcceptVersion();
		if (acceptVersions.contains("1.2")) {
			connectedHeaders.setVersion("1.2");
		}
		else if (acceptVersions.contains("1.1")) {
			connectedHeaders.setVersion("1.1");
		}
		else if (acceptVersions.isEmpty()) {
			// 1.0
		}
		else {
			throw new StompConversionException("Unsupported version '" + acceptVersions + "'");
		}
		connectedHeaders.setHeartbeat(0,0); // TODO

		Principal principal = session.getPrincipal();
		if (principal != null) {
			connectedHeaders.setNativeHeader(CONNECTED_USER_HEADER, principal.getName());
			connectedHeaders.setNativeHeader(QUEUE_SUFFIX_HEADER, session.getId());

			if (this.queueSuffixResolver != null) {
				String suffix = session.getId();
				this.queueSuffixResolver.addQueueSuffix(principal.getName(), session.getId(), suffix);
			}
		}

		// TODO: security

		Message<?> connectedMessage = MessageBuilder.withPayloadAndHeaders(new byte[0], connectedHeaders).build();
		byte[] bytes = this.stompMessageConverter.fromMessage(connectedMessage);
		session.sendMessage(new TextMessage(new String(bytes, Charset.forName("UTF-8"))));
	}

	protected void sendErrorMessage(WebSocketSession session, Throwable error) {

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.ERROR);
		headers.setMessage(error.getMessage());
		Message<?> message = MessageBuilder.withPayloadAndHeaders(new byte[0], headers).build();
		byte[] bytes = this.stompMessageConverter.fromMessage(message);
		try {
			session.sendMessage(new TextMessage(new String(bytes, Charset.forName("UTF-8"))));
		}
		catch (Throwable t) {
			// ignore
		}
	}

	@Override
	public String resolveSessionId(Message<?> message) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
		return headers.getSessionId();
	}

	@Override
	public void afterSessionStarted(WebSocketSession session, MessageChannel outputChannel) {
	}

	@Override
	public void afterSessionEnded(WebSocketSession session, CloseStatus closeStatus, MessageChannel outputChannel) {

		if ((this.queueSuffixResolver != null) && (session.getPrincipal() != null)) {
			this.queueSuffixResolver.removeQueueSuffix(session.getPrincipal().getName(), session.getId());
		}

		StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.DISCONNECT);
		headers.setSessionId(session.getId());
		Message<?> message = MessageBuilder.withPayloadAndHeaders(new byte[0], headers).build();
		outputChannel.send(message);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11189.java